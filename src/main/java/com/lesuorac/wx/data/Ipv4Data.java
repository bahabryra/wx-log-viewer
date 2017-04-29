package com.lesuorac.wx.data;

import java.math.BigInteger;
import java.net.Inet4Address;
import java.net.UnknownHostException;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.StringUtils;

@Entity
public class Ipv4Data {

    @Id
    @Column(nullable = false)
    @Size(min = 1, max = 32)
    String ip;

    /**
     * 1.2.3.0/24
     */
    @Column(nullable = false)
    String address;

    @Column(nullable = false)
    Long asn;

    @Column(nullable = false)
    String aso;

    @Deprecated
    public Ipv4Data() {
        super();
    }

    public Ipv4Data(String address, Long asn, String aso) {
        super();

        this.ip = generateIp(address);
        this.address = address;
        this.asn = asn;
        this.aso = aso;
    }

    private String generateIp(String address) {
        try {
            String[] splits = address.split("/");
            BigInteger ip_i = new BigInteger(1, Inet4Address.getByName(splits[0]).getAddress());
            String ip_s = ip_i.toString(2);
            String ip = StringUtils.leftPad(ip_s, 32, '0');
            Integer netmask = Integer.valueOf(splits[1]);

            if (ip.length() != 32) {
                throw new IllegalArgumentException(address);
            }

            StringBuilder builder = new StringBuilder(32);
            for (int i = 0; i < 32; i++) {
                if (i < netmask) {
                    builder.append(ip.charAt(i));
                } else {
                    // Single character wildcard
                    builder.append('_');
                }
            }

            return builder.toString();
        } catch (NumberFormatException | UnknownHostException | StringIndexOutOfBoundsException e) {
            throw new IllegalArgumentException(address, e);
        }
    }

    /**
     * @return the ip
     * @see #bare_field_name
     */
    public final String getIp() {
        return this.ip;
    }

    /**
     * @param ip
     *            the ip to set
     * @see #bare_field_name
     */
    public final void setIp(String ip) {
        this.ip = ip;
    }

    /**
     * @return the address
     * @see #bare_field_name
     */
    public final String getAddress() {
        return this.address;
    }

    /**
     * @param address
     *            the address to set
     * @see #bare_field_name
     */
    public final void setAddress(String address) {
        this.address = address;
    }

    /**
     * @return the asn
     * @see #bare_field_name
     */
    public final Long getAsn() {
        return this.asn;
    }

    /**
     * @param asn
     *            the asn to set
     * @see #bare_field_name
     */
    public final void setAsn(Long asn) {
        this.asn = asn;
    }

    /**
     * @return the aso
     * @see #bare_field_name
     */
    public final String getAso() {
        return this.aso;
    }

    /**
     * @param aso
     *            the aso to set
     * @see #bare_field_name
     */
    public final void setAso(String aso) {
        this.aso = aso;
    }

}
