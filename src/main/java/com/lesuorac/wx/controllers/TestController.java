package com.lesuorac.wx.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lesuorac.wx.data.DnsRepository;
import com.lesuorac.wx.data.FirewallRepository;
import com.lesuorac.wx.data.UserPreferences;
import com.lesuorac.wx.data.UserRepository;

@RestController
@ComponentScan(basePackageClasses = { DnsRepository.class, FirewallRepository.class })
public class TestController {

    private DnsRepository dnsRepo;
    private FirewallRepository firewallRepo;
    private UserRepository userRepo;

    private LogWebsocketHandler handler;

    @Autowired
    public TestController(DnsRepository dnsRepo, FirewallRepository firewallRepo, UserRepository userRepo,
            LogWebsocketHandler handler) {
        this.dnsRepo = dnsRepo;
        this.firewallRepo = firewallRepo;
        this.userRepo = userRepo;

        this.handler = handler;
    }

    @RequestMapping(path = "dns", method = RequestMethod.POST)
    public void addDns(@RequestParam("hostname") String hostname, @RequestParam("address") String address) {
        // SSH to 1.0.1.2
        // Add entry to /etc/dnsmasq.hosts
        // sudo systemctl restart dnsmasq.service
    }

    @RequestMapping(path = "ipv4", method = RequestMethod.POST)
    public void allowIpv4Address(@RequestParam("address") String address) {

    }

    @RequestMapping(path = "preferences", method = RequestMethod.GET)
    public UserPreferences getPreferences(@RequestParam("username") String user) {
        UserPreferences preferences = this.userRepo.findOne(user);
        if (preferences == null) {
            return new UserPreferences(user);
        }

        return preferences;
    }

    @RequestMapping(path = "preferences", method = RequestMethod.POST)
    public UserPreferences setPreferences(@RequestBody UserPreferences preferences) {
        return this.userRepo.save(preferences);
    }

}
