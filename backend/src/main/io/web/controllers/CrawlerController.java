package controllers;

import com.sun.net.httpserver.HttpServer;
import handlers.CrawlHandler;

import java.io.IOException;
import java.net.InetSocketAddress;

public class CrawlerController {
    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/api/v1/crawl", new CrawlHandler());

        //comment this section if not testing
        server.setExecutor(null);
        server.start();
        System.out.println("Server started on port 8080");

    }
}
