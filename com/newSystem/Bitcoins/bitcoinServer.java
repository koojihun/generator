package com.newSystem.Bitcoins;

import com.newSystem.Main;
import com.newSystem.Settings;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;


public class bitcoinServer extends Thread {
    public void run() {
        try {
            HttpServer httpServer = HttpServer.create(new InetSocketAddress(9999), 0);
            httpServer.createContext("/", new Handler());
            httpServer.setExecutor(null);
            httpServer.start();
        } catch (Exception e) {
            System.err.println(e.toString());
            System.exit(1);
        }
    }
    public class  Handler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            StringBuilder response = new StringBuilder();
            Map <String,String> params = queryToMap(httpExchange.getRequestURI().getQuery());
            String method = params.get("method");
            String pid;
            String companyAddress;
            String companyName;
            String companyIp;
            switch (Integer.valueOf(method)) {
                case 0:
                    // method:0 --> import address request.
                    companyName = params.get("companyName");
                    companyAddress = params.get("companyAddress");
                    Main.bitcoinJSONRPCClient.importAddress(companyName, companyAddress, true);
                    break;
                case 1:
                    // method:1 --> send_to_address.
                    companyAddress = params.get("companyAddress");
                    pid = params.get("pid");
                    response.append(Main.bitcoinJSONRPCClient.send_to_address(companyAddress, pid));
                    break;
                case 2:
                    // method:2 --> track_product.
                    String pid_to_track = params.get("pid");
                    String raw_ip = getIp(httpExchange);
                    String ip = raw_ip.substring(raw_ip.indexOf('/') + 1, raw_ip.indexOf(':'));
                    Main.trackingDb.insert(pid_to_track, ip);
                    response.append(Main.bitcoinJSONRPCClient.track_product(pid_to_track));
                    break;
                case 3:
                    // method:3 --> each company will send their ip address when their program is started.
                    companyName = params.get("companyName");
                    String tmpIp = getIp(httpExchange);
                    companyIp = tmpIp.substring(tmpIp.indexOf('/') + 1, tmpIp.indexOf(':'));
                    Main.companyIPs.put(companyName, companyIp);
                    for (Map.Entry<String, String> entry : Main.companyIPs.entrySet())
                        System.out.println(entry.getKey() + ", " + entry.getValue());
                    break;
                case 4:
                    // method:4 --> redirection of request to the mover.
                    companyName = params.get("companyName");
                    if (companyName.equals(Settings.companyName)) {
                        pid = params.get("pid");
                        companyAddress = params.get("address");
                        response.append(Main.bitcoinJSONRPCClient.send_to_address(companyAddress, pid));
                    } else {
                        companyIp = Main.companyIPs.get(companyName);
                        pid = params.get("pid");
                        companyAddress = params.get("address");
                        String mover_url = companyIp + ":9999/?method=4&pid=" + pid + "&companyName=" + companyName +
                                "&address=" + companyAddress;
                        System.out.println(mover_url);
                        Headers headers = httpExchange.getResponseHeaders();
                        headers.add("Location", mover_url);
                        httpExchange.sendResponseHeaders(301, -1);
                        httpExchange.close();
                    }
                    break;
                case 5:
                    // method: 5 --> registration of each middle node.
                    companyName = params.get("companyName");
                    String directorName = params.get("directorName");
                    String directorEmail = params.get("directorEmail");
                    String directorPhone = params.get("directorPhone");
                    System.out.println("------------<< New Registration >>------------");
                    System.out.println(companyName);
                    System.out.println(directorName);
                    System.out.println(directorEmail);
                    System.out.println(directorPhone);
            }
            writeResponse(httpExchange, response.toString());
            httpExchange.close();
        }
    }
    // url 뒤 parameter들을 파싱 하기 위한 함수들. (ex. ip주소:port/?method=1&address=~~~&pid=~~)
    static public Map<String, String> queryToMap(String query) {
        Map<String, String> result = new HashMap<String, String>();
        for (String param : query.split("&")) {
            String pair[] = param.split("=");
            if (pair.length > 1)
                result.put(pair[0], pair[1]);
            else
                result.put(pair[0], "");
        }
        return result;
    }
    static public void writeResponse(HttpExchange httpExchange, String response) throws IOException {
        httpExchange.sendResponseHeaders(200, response.length());
        OutputStream os = httpExchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
    public String getIp(HttpExchange httpExchange) {
        Headers headers = httpExchange.getRequestHeaders();
        String ip = headers.getFirst("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = headers.getFirst("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = headers.getFirst("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = headers.getFirst("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = headers.getFirst("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = headers.getFirst("X-Real-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = headers.getFirst("X-RealIP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = httpExchange.getRemoteAddress().toString();
        }
        return ip;
    }
}
