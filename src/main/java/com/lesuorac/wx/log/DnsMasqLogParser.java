package com.lesuorac.wx.log;

import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lesuorac.wx.data.DnsMasqLog;
import com.lesuorac.wx.data.Ipv4Data;
import com.lesuorac.wx.data.Ipv4Repository;

@Component
public class DnsMasqLogParser implements RsyslogParser<DnsMasqLog> {

    private static final Pattern queryPattern = Pattern.compile("query\\[(.*)\\]", Pattern.CASE_INSENSITIVE);

    private static final Logger LOGGER = LogManager.getFormatterLogger();

    private Ipv4Repository ipv4Repo;

    @Autowired
    public DnsMasqLogParser(Ipv4Repository ipv4Repo) {
        this.ipv4Repo = ipv4Repo;
    }

    @Override
    public List<DnsMasqLog> parse(List<String> rows) {
        List<DnsMasqLog> logs = new ArrayList<>();
        for (int i = 0; (i + 1) < rows.size(); i++) {
            try {
                logs.add(parse(rows.get(i), rows.get(i + 1)));
            } catch (ParseException | ArrayIndexOutOfBoundsException e) {
                /*
                 * Most log messages aren't going to be from the dnsmasq daemon
                 */
            }
        }

        return logs;
    }

    private DnsMasqLog parse(String row1, String row2) throws ParseException {
        // Apr 22 20:54:20 dns dnsmasq[4350]: query[A] sls.update.microsoft.com
        // from
        // 192.168.1.104
        // Apr 22 20:54:20 dns dnsmasq[4350]: config sls.update.microsoft.com is
        // 192.168.1.9
        String[] row1Splits = row1.split(" ");
        int row1Counter = 0;
        String[] row2Splits = row2.split(" ");
        int row2Counter = 3;

        int year = LocalDateTime.now().getYear();// :(
        String month = row1Splits[row1Counter++];
        String day = row1Splits[row1Counter++];
        String time = row1Splits[row1Counter++];
        LocalDateTime date = LocalDateTime.parse(
                String.format("%d %s %s %s", year, month, day, time),
                DateTimeFormatter.ofPattern("yyyy MMM dd HH:mm:ss"));

        row1Counter++;// "dns"
        row2Counter++;// "dns"

        row1Counter++;// "dnsmasq[4350]:"
        row2Counter++;// "dnsmasq[4350]:"

        String query = row1Splits[row1Counter++];
        Matcher matcher = queryPattern.matcher(query);
        row2Counter++; // "config"

        if (!matcher.matches()) {
            throw new ParseException(String.format("queryPattern could not parse input:[%s]", query), row1Counter);
        }
        String queryType = matcher.group(1);

        String hostname = row1Splits[row1Counter++];
        row2Counter++; // hostname

        row1Counter++;// "from"
        row2Counter++;// "is"

        String requesterIp = row1Splits[row1Counter++];
        String servedIp = row2Splits[row2Counter++];

        String actualIp;
        try {
            actualIp = Inet4Address.getByName(hostname).getHostAddress();
        } catch (UnknownHostException e) {
            throw new ParseException(String.format("Given hostname:[%s] was not valid", hostname), row1Counter);
        }

        String aso = "";
        Long asn = 0L;

        try {
            Ipv4Data ipv4data = this.ipv4Repo.findByIpLike(this.ipv4Repo.convertAddressToBits(actualIp));

            if (ipv4data != null) {
                aso = ipv4data.getAso();
                asn = ipv4data.getAsn();
            }

        } catch (Throwable e) {
            LOGGER.error("Problem parsing address: [%s]", actualIp, e);
        }

        return new DnsMasqLog(Timestamp.valueOf(date), queryType, hostname, requesterIp, actualIp, servedIp, asn, aso);
    }

}
