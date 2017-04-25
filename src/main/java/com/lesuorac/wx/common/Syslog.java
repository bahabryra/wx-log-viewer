package com.lesuorac.wx.common;

public abstract class Syslog {

    private LogType type;

    public Syslog(LogType type) {
        this.type = type;
    }

    /**
     * @return the type
     * @see #bare_field_name
     */
    public final LogType getType() {
        return this.type;
    }

    /**
     * @param type
     *            the type to set
     * @see #bare_field_name
     */
    public final void setType(LogType type) {
        this.type = type;
    }

}
