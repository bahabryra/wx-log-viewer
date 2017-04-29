package com.lesuorac.wx.data;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.lesuorac.wx.common.FilterAction;
import com.lesuorac.wx.common.FilterDirection;
import com.lesuorac.wx.common.LogType;
import com.lesuorac.wx.common.Syslog;

@Entity
public class FirewallFilterLog extends Syslog {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private Timestamp date;

    @Column(nullable = false)
    private long ruleNumber;

    @Column(nullable = false)
    private long subRuleNumber;

    @Column(nullable = false)
    private String realInterface;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private FilterAction action;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private FilterDirection direction;

    @Column(nullable = false)
    private int ipVersion;

    @Column(nullable = false)
    private int protocolId;

    @Column(nullable = false)
    private String protocolText;

    @Column(nullable = false)
    private int length;

    @Column(nullable = false)
    private String sourceAddress;

    @Column(nullable = false)
    private String destinationAddress;

    @Column(nullable = false)
    private int sourcePort;

    @Column(nullable = false)
    private int destPort;

    @Column(nullable = false)
    private int dataLength;

    @Column(nullable = true)
    private Long destAsn;

    @Column(nullable = true)
    private String destAso;

    @Deprecated
    public FirewallFilterLog() {
        super(LogType.FIREWALL);
    }

    public FirewallFilterLog(Timestamp date, long ruleNumber, long subRuleNumber, String realInterface,
            FilterAction action, FilterDirection direction, int ipVersion, int protocolId, String protocolText,
            int length, String sourceAddress, String destinationAddress, int sourcePort, int destPort, int dataLength,
            Long destAsn, String destAso) {
        this();

        this.date = date;
        this.ruleNumber = ruleNumber;
        this.subRuleNumber = subRuleNumber;
        this.realInterface = realInterface;
        this.action = action;
        this.direction = direction;
        this.ipVersion = ipVersion;
        this.protocolId = protocolId;
        this.protocolText = protocolText;
        this.length = length;
        this.sourceAddress = sourceAddress;
        this.destinationAddress = destinationAddress;
        this.sourcePort = sourcePort;
        this.destPort = destPort;
        this.dataLength = dataLength;
        this.destAsn = destAsn;
        this.destAso = destAso;
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
     * @return the ruleNumber
     * @see #bare_field_name
     */
    public final long getRuleNumber() {
        return this.ruleNumber;
    }

    /**
     * @param ruleNumber
     *            the ruleNumber to set
     * @see #bare_field_name
     */
    public final void setRuleNumber(long ruleNumber) {
        this.ruleNumber = ruleNumber;
    }

    /**
     * @return the subRuleNumber
     * @see #bare_field_name
     */
    public final long getSubRuleNumber() {
        return this.subRuleNumber;
    }

    /**
     * @param subRuleNumber
     *            the subRuleNumber to set
     * @see #bare_field_name
     */
    public final void setSubRuleNumber(long subRuleNumber) {
        this.subRuleNumber = subRuleNumber;
    }

    /**
     * @return the realInterface
     * @see #bare_field_name
     */
    public final String getRealInterface() {
        return this.realInterface;
    }

    /**
     * @param realInterface
     *            the realInterface to set
     * @see #bare_field_name
     */
    public final void setRealInterface(String realInterface) {
        this.realInterface = realInterface;
    }

    /**
     * @return the action
     * @see #bare_field_name
     */
    public final FilterAction getAction() {
        return this.action;
    }

    /**
     * @param action
     *            the action to set
     * @see #bare_field_name
     */
    public final void setAction(FilterAction action) {
        this.action = action;
    }

    /**
     * @return the direction
     * @see #bare_field_name
     */
    public final FilterDirection getDirection() {
        return this.direction;
    }

    /**
     * @param direction
     *            the direction to set
     * @see #bare_field_name
     */
    public final void setDirection(FilterDirection direction) {
        this.direction = direction;
    }

    /**
     * @return the ipVersion
     * @see #bare_field_name
     */
    public final int getIpVersion() {
        return this.ipVersion;
    }

    /**
     * @param ipVersion
     *            the ipVersion to set
     * @see #bare_field_name
     */
    public final void setIpVersion(int ipVersion) {
        this.ipVersion = ipVersion;
    }

    /**
     * @return the protocolId
     * @see #bare_field_name
     */
    public final int getProtocolId() {
        return this.protocolId;
    }

    /**
     * @param protocolId
     *            the protocolId to set
     * @see #bare_field_name
     */
    public final void setProtocolId(int protocolId) {
        this.protocolId = protocolId;
    }

    /**
     * @return the protocolText
     * @see #bare_field_name
     */
    public final String getProtocolText() {
        return this.protocolText;
    }

    /**
     * @param protocolText
     *            the protocolText to set
     * @see #bare_field_name
     */
    public final void setProtocolText(String protocolText) {
        this.protocolText = protocolText;
    }

    /**
     * @return the length
     * @see #bare_field_name
     */
    public final int getLength() {
        return this.length;
    }

    /**
     * @param length
     *            the length to set
     * @see #bare_field_name
     */
    public final void setLength(int length) {
        this.length = length;
    }

    /**
     * @return the sourceAddress
     * @see #bare_field_name
     */
    public final String getSourceAddress() {
        return this.sourceAddress;
    }

    /**
     * @param sourceAddress
     *            the sourceAddress to set
     * @see #bare_field_name
     */
    public final void setSourceAddress(String sourceAddress) {
        this.sourceAddress = sourceAddress;
    }

    /**
     * @return the destinationAddress
     * @see #bare_field_name
     */
    public final String getDestinationAddress() {
        return this.destinationAddress;
    }

    /**
     * @param destinationAddress
     *            the destinationAddress to set
     * @see #bare_field_name
     */
    public final void setDestinationAddress(String destinationAddress) {
        this.destinationAddress = destinationAddress;
    }

    /**
     * @return the sourcePort
     * @see #bare_field_name
     */
    public final int getSourcePort() {
        return this.sourcePort;
    }

    /**
     * @param sourcePort
     *            the sourcePort to set
     * @see #bare_field_name
     */
    public final void setSourcePort(int sourcePort) {
        this.sourcePort = sourcePort;
    }

    /**
     * @return the destPort
     * @see #bare_field_name
     */
    public final int getDestPort() {
        return this.destPort;
    }

    /**
     * @param destPort
     *            the destPort to set
     * @see #bare_field_name
     */
    public final void setDestPort(int destPort) {
        this.destPort = destPort;
    }

    /**
     * @return the dataLength
     * @see #bare_field_name
     */
    public final int getDataLength() {
        return this.dataLength;
    }

    /**
     * @param dataLength
     *            the dataLength to set
     * @see #bare_field_name
     */
    public final void setDataLength(int dataLength) {
        this.dataLength = dataLength;
    }

    /**
     * @return the destAsn
     * @see #bare_field_name
     */
    public final Long getDestAsn() {
        return this.destAsn;
    }

    /**
     * @param destAsn
     *            the destAsn to set
     * @see #bare_field_name
     */
    public final void setDestAsn(Long destAsn) {
        this.destAsn = destAsn;
    }

    /**
     * @return the destAso
     * @see #bare_field_name
     */
    public final String getDestAso() {
        return this.destAso;
    }

    /**
     * @param destAso
     *            the destAso to set
     * @see #bare_field_name
     */
    public final void setDestAso(String destAso) {
        this.destAso = destAso;
    }

}
