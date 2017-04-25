package com.lesuorac.wx.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RestController;

import com.lesuorac.wx.data.DnsRepository;
import com.lesuorac.wx.data.FirewallRepository;

@RestController
@ComponentScan(basePackageClasses = { DnsRepository.class, FirewallRepository.class })
public class TestController {

    private DnsRepository dnsRepo;
    private FirewallRepository firewallRepo;

    private LogWebsocketHandler handler;

    @Autowired
    public TestController(DnsRepository dnsRepo, FirewallRepository firewallRepo, LogWebsocketHandler handler) {
        this.dnsRepo = dnsRepo;
        this.firewallRepo = firewallRepo;

        this.handler = handler;
    }

    @Scheduled(fixedDelay = 10000)
    public void onScheduled() {
        this.handler.broadcast(this.dnsRepo.findAll());
        this.handler.broadcast(this.firewallRepo.findAll());
    }

}
