package com.lesuorac.wx.log;

import java.sql.Timestamp;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.lesuorac.wx.common.FilterAction;
import com.lesuorac.wx.common.FilterDirection;
import com.lesuorac.wx.data.FirewallFilterLog;

@Component
public class FirewallFilterLogParser implements RsyslogParser<FirewallFilterLog> {

    public static final int TCP_PROTOCOL = 6;

    public static final int UDP_PROTOCOL = 17;

    @Override
    public List<FirewallFilterLog> parse(List<String> rows) {
        List<FirewallFilterLog> logs = new ArrayList<>();
        for (String row : rows) {
            try {
                logs.add(parse(row));
            } catch (ParseException e) {
                /*
                 * Most log messages aren't going to be from the pfsense
                 * firewall
                 */
            }
        }

        return logs;
    }

    private FirewallFilterLog parse(String row) throws ParseException {
        String[] headerSplits = row.split(" ");
        int headerCounter = 0;

        if (headerSplits.length != 6) {
            throw new ParseException(
                    String.format(
                            "Given row:[%s] did not contain the correct amount of splits [%d] != 6",
                            row,
                            headerSplits.length),
                    headerCounter);
        }

        int year = LocalDateTime.now().getYear();// :(
        String month = headerSplits[headerCounter++];
        String day = headerSplits[headerCounter++];
        String time = headerSplits[headerCounter++];
        LocalDateTime date = LocalDateTime.parse(
                String.format("%d %s %s %s", year, month, day, time),
                DateTimeFormatter.ofPattern("yyyy MMM dd HH:mm:ss"));

        headerCounter++; // hostname
        if (!"filterlog:".equalsIgnoreCase(headerSplits[headerCounter++])) {
            throw new ParseException(
                    String.format(
                            "Given row:[%s] did not contain 'filterlog:' as its 4th split. Instead contained:[%s]",
                            row,
                            headerSplits[4]),
                    headerCounter);
        }

        String[] logSplits = headerSplits[headerCounter++].split(",");
        int logCounter = 0;
        long ruleNumber = Long.parseLong(logSplits[logCounter++]);
        long subRuleNumber = Long.parseLong(logSplits[logCounter++]);
        logCounter++; // anchor
        logCounter++; // tracker
        logCounter++; // reason
        String realInterface = logSplits[logCounter++];
        FilterAction action = FilterAction.parse(logSplits[logCounter++]);
        FilterDirection direction = FilterDirection.parse(logSplits[logCounter++]);
        int ipVersion = Integer.parseInt(logSplits[logCounter++]);

        int protocolId;
        String protocolText;
        if (ipVersion == 4) {
            logCounter++; // tos
            logCounter++; // ecn
            logCounter++; // ttl
            logCounter++; // id
            logCounter++; // offset
            logCounter++; // flags
            protocolId = Integer.parseInt(logSplits[logCounter++]);
            protocolText = logSplits[logCounter++];
        } else if (ipVersion == 6) {
            logCounter++; // class
            logCounter++; // flow-label
            logCounter++; // hop-limit
            protocolText = logSplits[logCounter++];
            protocolId = Integer.parseInt(logSplits[logCounter++]);
        } else {
            throw new ParseException(String.format("Given ipVersion[%d] was not 4 or 6", ipVersion), logCounter);
        }

        int length = Integer.parseInt(logSplits[logCounter++]);
        String sourceAddress = logSplits[logCounter++];
        String destinationAddress = logSplits[logCounter++];

        int sourcePort;
        int destPort;
        int dataLength;
        if (protocolId == TCP_PROTOCOL) {
            sourcePort = Integer.parseInt(logSplits[logCounter++]);
            destPort = Integer.parseInt(logSplits[logCounter++]);
            dataLength = Integer.parseInt(logSplits[logCounter++]);
            logCounter++; // tcp-flags
            logCounter++; // sequence-number
            logCounter++; // ack-number
            logCounter++; // urg
            logCounter++; // tcp-options
        } else if (protocolId == UDP_PROTOCOL) {
            sourcePort = Integer.parseInt(logSplits[logCounter++]);
            destPort = Integer.parseInt(logSplits[logCounter++]);
            dataLength = Integer.parseInt(logSplits[logCounter++]);
        } else {
            throw new ParseException(
                    String.format(
                            "Given protocolId[%d] was not UDP=%d or TCP=%d",
                            ipVersion,
                            UDP_PROTOCOL,
                            TCP_PROTOCOL),
                    logCounter);
        }

        return new FirewallFilterLog(
                Timestamp.valueOf(date),
                ruleNumber,
                subRuleNumber,
                realInterface,
                action,
                direction,
                ipVersion,
                protocolId,
                protocolText,
                length,
                sourceAddress,
                destinationAddress,
                sourcePort,
                destPort,
                dataLength);
    }

}
