package com.lesuorac.wx.data;

import java.math.BigInteger;
import java.net.Inet4Address;
import java.net.UnknownHostException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.repository.CrudRepository;

public interface Ipv4Repository extends CrudRepository<Ipv4Data, String> {

    default String convertAddressToBits(String address) throws UnknownHostException {
        BigInteger ip_i = new BigInteger(1, Inet4Address.getByName(address).getAddress());
        String ip_s = ip_i.toString(2);
        return StringUtils.leftPad(ip_s, 32, '0');
    }

    Ipv4Data findByIpLike(String bits);
}
