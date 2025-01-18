package controllers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpServer;
import handlers.CrawlHandler;
import handlers.HandleJsonResponse;
import service.CrawlerService;

import java.io.IOException;
import java.net.InetSocketAddress;

public class CrawlerController {
    private final HttpServer httpServer;


    public CrawlerController(CrawlerService crawlerService, Gson gson, HandleJsonResponse handleJsonResponse, int port) throws IOException {
        // Initialize server
        this.httpServer = HttpServer.create(new InetSocketAddress(port), 0);
        httpServer.createContext("/api/v1/crawl", new CrawlHandler(crawlerService, gson, handleJsonResponse));
        httpServer.setExecutor(null); // Optionally sets an executor for thread pool
    }

    public void start() {
        httpServer.start();
        System.out.println("Server started on port " + httpServer.getAddress().getPort());
    }

    public void stop(int delay) {
        httpServer.stop(delay);
        System.out.println("Server stopped.");
    }
//    public static void main(String[] args) throws IOException {
//        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
//        server.createContext("/api/v1/crawl", new CrawlHandler());
//
//        //comment this section if not testing
//        server.setExecutor(null);
//        server.start();
//        System.out.println("Server started on port 8080");
//
//    }
}
