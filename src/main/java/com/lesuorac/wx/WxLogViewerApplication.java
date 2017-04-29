package com.lesuorac.wx;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

import com.lesuorac.wx.controllers.LogWebsocketHandler;
import com.lesuorac.wx.controllers.TestController;
import com.lesuorac.wx.data.DnsMasqLog;
import com.lesuorac.wx.data.FirewallFilterLog;

@SpringBootApplication(scanBasePackageClasses = { TestController.class, WxBackend.class, IpInserter.class,
        LogWebsocketHandler.class })
@EntityScan(basePackageClasses = { DnsMasqLog.class, FirewallFilterLog.class })
public class WxLogViewerApplication {

    public static void main(String[] args) {
        SpringApplication.run(WxLogViewerApplication.class, args);
    }

}
