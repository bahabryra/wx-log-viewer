package com.lesuorac.wx;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lesuorac.wx.data.Ipv4Data;
import com.lesuorac.wx.data.Ipv4Repository;

@Service
public class IpInserter {

    private Ipv4Repository ipv4Repo;

    @Autowired
    public IpInserter(Ipv4Repository ipv4Repo) {
        this.ipv4Repo = ipv4Repo;
    }

    @PostConstruct
    public void onPostConstruct() {
        Thread t = new Thread(() -> this.insert());

        t.setName("ipv4_geolite2");
        t.setDaemon(true);
        t.start();
    }

    public void insert() {

        try (InputStream stream = getClass().getClassLoader().getResourceAsStream("GeoLite2-ASN-Blocks-IPv4.csv")) {
            if (stream == null) {
                return;
            }

            BufferedReader in = new BufferedReader(new InputStreamReader(stream));

            in.lines().forEach(line -> {
                String[] splits = line.split(",", 3);
                String address = splits[0];
                Long asn = Long.valueOf(splits[1]);
                String aso = splits[2];

                Ipv4Data data = new Ipv4Data(address, asn, aso);

                if (!this.ipv4Repo.exists(data.getIp())) {
                    this.ipv4Repo.save(new Ipv4Data(address, asn, aso));
                }

            });

            in.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
