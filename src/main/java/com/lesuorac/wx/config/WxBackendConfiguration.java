package com.lesuorac.wx.config;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "wx")
public class WxBackendConfiguration {

    /**
     * The commands to run to get the log data
     */
    @Valid
    @NotNull
    @Size(min = 1)
    List<String> logCommand;

    /**
     * @return the logCommand
     * @see #bare_field_name
     */
    public final List<String> getLogCommand() {
        return this.logCommand;
    }

    /**
     * @param logCommand
     *            the logCommand to set
     * @see #bare_field_name
     */
    public final void setLogCommand(List<String> logCommand) {
        this.logCommand = logCommand;
    }

}
