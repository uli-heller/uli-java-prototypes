package org.uli;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.base.Splitter;

public class EtcHosts {
    Map<String, InetAddress> map = new HashMap<String,InetAddress>();
    public static EtcHosts from(String filename) throws IOException {
        return from(new FileInputStream(new File(filename)));
    }

    public static EtcHosts from(InputStream inputStream) throws IOException {
        EtcHosts etcHosts = new EtcHosts();
        etcHosts.map = etcHosts.readIt(inputStream); 
        return etcHosts;
    }

    public void merge(InputStream inputStream) throws IOException {
        Map<String, InetAddress> map = this.readIt(inputStream);
        this.map.putAll(map);
    }

    public InetAddress get(String hostname) {
        return this.map.get(hostname);
    }

    private Map<String, InetAddress> readIt(InputStream inputStream) throws IOException {
        Map<String, InetAddress> map = new HashMap<String,InetAddress>();
        BufferedReader r = new BufferedReader(new InputStreamReader(inputStream));
        for (;;) {
            String line= r.readLine();
            if (line == null) {
                break;
            }
            parseLine(line, map);
        }
        return map;
    }
    
    private static final String COMMENT="#";
    private static final String IPADDRESS="^[0-9.:]*";
    private static final Pattern ipAddressPattern = Pattern.compile(IPADDRESS);

    public void parseLine(String line, Map<String, InetAddress> map) throws UnknownHostException {
        List<InetAddress> l = this.parseLine(line);
        for (InetAddress i : l) {
            map.put(i.getHostName(), i);
        }
    }

    protected List<InetAddress> parseLine(String line) throws UnknownHostException {
        List<InetAddress> l = new LinkedList<InetAddress>();
        line = line.trim();
        int comment = line.indexOf(COMMENT);
        if (comment >= 0) {
            line = line.substring(0, comment).trim();
        }
        Matcher m = ipAddressPattern.matcher(line);
        if (m.find()) {
            int start = m.start();
            int end = m.end();
            String ipAddress = line.substring(start, end);
            InetAddress ia = InetAddress.getByName(ipAddress);
            line = line.substring(end).trim();
            Iterable<String> names = Splitter.onPattern("[ \t]").omitEmptyStrings().split(line);
            for (String name : names) {
                InetAddress complete = InetAddress.getByAddress(name, ia.getAddress());
                l.add(complete);
            }
        }
        return l;
    }
}
