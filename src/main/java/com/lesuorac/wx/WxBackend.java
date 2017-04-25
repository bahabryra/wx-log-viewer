package com.lesuorac.wx;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.lesuorac.wx.config.WxBackendConfiguration;
import com.lesuorac.wx.controllers.LogWebsocketHandler;
import com.lesuorac.wx.data.DnsMasqLog;
import com.lesuorac.wx.data.DnsRepository;
import com.lesuorac.wx.data.FirewallFilterLog;
import com.lesuorac.wx.data.FirewallRepository;
import com.lesuorac.wx.log.RsyslogParser;

@Controller
public class WxBackend {

    private static final Logger LOGGER = LogManager.getFormatterLogger();

    private List<RsyslogParser<?>> syslogParsers;

    private LinkedBlockingDeque<String> logMessages;
    private Thread inputStreamThread;
    private Thread errorStreamThread;
    private AtomicInteger runningThreads;

    private Thread logMessageThread;

    private DnsRepository dnsRepo;
    private FirewallRepository firewallRepo;

    private LogWebsocketHandler wsHandler;

    @Autowired
    public WxBackend(List<RsyslogParser<?>> syslogParsers, WxBackendConfiguration configuration, DnsRepository dnsRepo,
            FirewallRepository firewallRepo, LogWebsocketHandler wsHandler) throws IOException {
        this.syslogParsers = syslogParsers;
        this.logMessages = new LinkedBlockingDeque<>();
        this.runningThreads = new AtomicInteger(0);
        this.dnsRepo = dnsRepo;
        this.firewallRepo = firewallRepo;
        this.wsHandler = wsHandler;

        setupLogCommandProcesses(configuration);
        setupLogMessageProcessor();
    }

    private void setupLogMessageProcessor() {
        this.logMessageThread = new Thread(() -> {
            List<String> rows = new ArrayList<>();
            try {
                while (!Thread.interrupted()) {
                    for (RsyslogParser<?> parser : this.syslogParsers) {
                        List<?> logs = parser.parse(rows);

                        if (logs.isEmpty()) {
                            continue;
                        }

                        rows.clear();
                        for (Object log : logs) {
                            if (log instanceof DnsMasqLog) {
                                DnsMasqLog dnsLog = (DnsMasqLog) log;
                                LOGGER.trace(
                                        "Computer:[%s] attempted to lookup hostname:[%s] of actual ip:[%s] and was served:[%s]",
                                        dnsLog.getRequesterIp(),
                                        dnsLog.getHostname(),
                                        dnsLog.getActualIp(),
                                        dnsLog.getServedIp());
                                this.dnsRepo.save(dnsLog);

                            } else if (log instanceof FirewallFilterLog) {
                                FirewallFilterLog firewallLog = (FirewallFilterLog) log;
                                LOGGER.trace(
                                        "Computer:[%s] attempted to open a socket to [%s]:[%d] using protocol: [%s]=%d",
                                        firewallLog.getSourceAddress(),
                                        firewallLog.getDestinationAddress(),
                                        firewallLog.getDestPort(),
                                        firewallLog.getProtocolText(),
                                        firewallLog.getProtocolId());
                                this.firewallRepo.save(firewallLog);
                            }
                        }
                        this.wsHandler.broadcast(logs);
                    }

                    rows.add(this.logMessages.takeFirst());
                }
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        });

        this.logMessageThread.setDaemon(true);
        this.logMessageThread.setName("log-command logMessageStream");
        this.logMessageThread
                .setUncaughtExceptionHandler((thread, exception) -> LOGGER.error("Thread:[%s]", thread, exception));
        this.logMessageThread.start();

    }

    private void setupLogCommandProcesses(WxBackendConfiguration configuration) throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder(configuration.getLogCommand());
        Process process = processBuilder.start();
        this.runningThreads.set(2);

        this.inputStreamThread = new Thread(() -> {
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            try {
                String line = reader.readLine();
                while (line != null && (this.runningThreads.get() == 2) && !Thread.interrupted()) {
                    this.logMessages.addLast(line);
                    line = reader.readLine();
                }

                LOGGER.fatal("InputStreamThread terminated due to eof");
            } catch (IOException e) {
                LOGGER.error("InputStreamThread is terminating", e);
            }

            this.runningThreads.decrementAndGet();
        });
        this.inputStreamThread.setDaemon(true);
        this.inputStreamThread.setName("log-command inputStream");
        this.inputStreamThread
                .setUncaughtExceptionHandler((thread, exception) -> LOGGER.error("Thread:[%s]", thread, exception));
        this.inputStreamThread.start();

        this.errorStreamThread = new Thread(() -> {
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()));

            try {
                String line = reader.readLine();
                while (line != null && (this.runningThreads.get() == 2) && !Thread.interrupted()) {
                    LOGGER.error(reader.readLine());
                    line = reader.readLine();
                }

                LOGGER.fatal("ErrorStreamThread terminated due to eof");
            } catch (IOException e) {
                LOGGER.error("ErrorStreamThread is terminating", e);
            }

            this.runningThreads.decrementAndGet();
        });
        this.errorStreamThread.setDaemon(true);
        this.errorStreamThread.setName("log-command errorStream");
        this.errorStreamThread
                .setUncaughtExceptionHandler((thread, exception) -> LOGGER.error("Thread:[%s]", thread, exception));
        this.errorStreamThread.start();

    }
}
