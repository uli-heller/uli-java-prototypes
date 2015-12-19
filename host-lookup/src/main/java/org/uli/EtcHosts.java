package org.uli;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EtcHosts {
    Map<String, InetAddress> byHostName = new HashMap<String,InetAddress>();
    Map<InetAddressWrapper, InetAddress> byAddress = new HashMap<InetAddressWrapper,InetAddress>();
    public static EtcHosts from(String filename) throws IOException {
        return from(new FileInputStream(new File(filename)));
    }

    public static EtcHosts from(InputStream inputStream) throws IOException {
        EtcHosts etcHosts = new EtcHosts();
        etcHosts.byHostName = etcHosts.readIt(inputStream);
        etcHosts.byAddress = etcHosts.initByAddress(etcHosts.byHostName);
        return etcHosts;
    }

    public void merge(InputStream inputStream) throws IOException {
        Map<String, InetAddress> additionalHosts = this.readIt(inputStream);
        Map<InetAddressWrapper, InetAddress> additionalAddresses = this.initByAddress(additionalHosts);
        this.byHostName.putAll(additionalHosts);
        this.byAddress.putAll(additionalAddresses);
    }

    public InetAddress getByHostName(String hostName) {
        return this.byHostName.get(hostName);
    }

    public InetAddress getByAddress(byte[] address) {
        return this.byAddress.get(new InetAddressWrapper(address));
    }

    public InetAddress getByAddress(String address) {
        InetAddress result = null;
        Matcher m = ipAddressPattern.matcher(address.trim());
        if (m.matches()) {
            try {
                InetAddress ia = InetAddress.getByName(address);
                result = this.getByAddress(ia.getAddress());
            } catch (UnknownHostException e) {
                result = null;
            }
        }
        return result;
    }

    private Map<String, InetAddress> readIt(InputStream inputStream) throws IOException {
        Map<String, InetAddress> map = new LinkedHashMap<String,InetAddress>();
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

    private Map<InetAddressWrapper, InetAddress> initByAddress(Map<String, InetAddress> byHostName) {
        Map<InetAddressWrapper, InetAddress> newByAddress = new HashMap<InetAddressWrapper, InetAddress>();
        Set<Entry<String, InetAddress>> entries = byHostName.entrySet();
        List<Entry<String, InetAddress>> l = new LinkedList<Entry<String, InetAddress>>(entries);
        Collections.reverse(l);
        for (Entry<String, InetAddress> entry : l) {
            InetAddress ia = entry.getValue();
            InetAddressWrapper iaw = new InetAddressWrapper(ia.getAddress());
            newByAddress.put(iaw, ia);
        }
        return newByAddress;
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
            for (String name : line.split("[ \t]")) {
                name = name.trim();
                if (name.length() > 0) {
                    InetAddress complete = InetAddress.getByAddress(name, ia.getAddress());
                    l.add(complete);
                }
            }
        }
        return l;
    }

    private class InetAddressWrapper {
        byte[] address;

        private InetAddressWrapper(byte[] address) {
            this.address = address;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + getOuterType().hashCode();
            result = prime * result + Arrays.hashCode(address);
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            InetAddressWrapper other = (InetAddressWrapper) obj;
            if (!getOuterType().equals(other.getOuterType()))
                return false;
            if (!Arrays.equals(address, other.address))
                return false;
            return true;
        }

        private EtcHosts getOuterType() {
            return EtcHosts.this;
        }
    }
}
