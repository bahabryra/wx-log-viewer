package com.lesuorac.wx.data;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.lesuorac.wx.common.LogType;
import com.lesuorac.wx.common.Syslog;

@Entity
public class DnsMasqLog extends Syslog {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private Timestamp date;

    /**
     * The type of query (i.e. 'A')
     */
    @Column(nullable = false)
    private String queryType;

    /**
     * The hostname in question (i.e. 'sls.update.microsoft.com')
     */
    @Column(nullable = false)
    private String hostname;

    /**
     * The computer that was looking up the hostname (i.e. 192.168.1.104)
     */
    @Column(nullable = false)
    private String requesterIp;

    /**
     * The actual ip that the hostname resolves to (i.e. 157.56.77.141)
     */
    @Column(nullable = false)
    private String actualIp;

    /**
     * The ip that was given to the computer requesting the hostname (i.e.
     * 192.168.1.9)s
     */
    @Column(nullable = false)
    private String servedIp;

    @Deprecated
    public DnsMasqLog() {
        super(LogType.DNS);
    }

    public DnsMasqLog(Timestamp date, String queryType, String hostname, String requesterIp, String actualIp,
            String servedIp) {
        this();

        this.date = date;
        this.queryType = queryType;
        this.hostname = hostname;
        this.requesterIp = requesterIp;
        this.actualIp = actualIp;
        this.servedIp = servedIp;
    }

    /**
     * @return the date
     * @see #bare_field_name
     */
    public final Timestamp getDate() {
        return this.date;
    }

    /**
     * @param date
     *            the date to set
     * @see #bare_field_name
     */
    public final void setDate(Timestamp date) {
        this.date = date;
    }

    /**
     * @return the queryType
     * @see #bare_field_name
     */
    public final String getQueryType() {
        return this.queryType;
    }

    /**
     * @param queryType
     *            the queryType to set
     * @see #bare_field_name
     */
    public final void setQueryType(String queryType) {
        this.queryType = queryType;
    }

    /**
     * @return the hostname
     * @see #bare_field_name
     */
    public final String getHostname() {
        return this.hostname;
    }

    /**
     * @param hostname
     *            the hostname to set
     * @see #bare_field_name
     */
    public final void setHostname(String hostname) {
        this.hostname = hostname;
    }

    /**
     * @return the requesterIp
     * @see #bare_field_name
     */
    public final String getRequesterIp() {
        return this.requesterIp;
    }

    /**
     * @param requesterIp
     *            the requesterIp to set
     * @see #bare_field_name
     */
    public final void setRequesterIp(String requesterIp) {
        this.requesterIp = requesterIp;
    }

    /**
     * @return the actualIp
     * @see #bare_field_name
     */
    public final String getActualIp() {
        return this.actualIp;
    }

    /**
     * @param actualIp
     *            the actualIp to set
     * @see #bare_field_name
     */
    public final void setActualIp(String actualIp) {
        this.actualIp = actualIp;
    }

    /**
     * @return the servedIp
     * @see #bare_field_name
     */
    public final String getServedIp() {
        return this.servedIp;
    }

    /**
     * @param servedIp
     *            the servedIp to set
     * @see #bare_field_name
     */
    public final void setServedIp(String servedIp) {
        this.servedIp = servedIp;
    }

}
