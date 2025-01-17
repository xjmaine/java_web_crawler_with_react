import com.google.gson.Gson;
import com.sun.net.httpserver.HttpServer;
import handlers.CrawlHandler;
import controllers.CrawlerController;
import handlers.HandleJsonResponse;
import service.CrawlerService;

import java.io.IOException;
import java.net.InetSocketAddress;

public class CrawlApp {
    CrawlerService service;
    Gson gson;
    HandleJsonResponse handleJsonResponse;

    public CrawlApp(CrawlerService service, Gson gson, HandleJsonResponse handleJsonResponse) {
        this.service = service;
        this.gson = gson;
        this.handleJsonResponse = handleJsonResponse;
    }

//    public static void main(String[] args) {
//        System.out.println("Crawler Server Started...");
//
//        try{
//            //initialise and start server
//            HttpServer server = HttpServer.create(new InetSocketAddress(8080),0);
//
//            // Single CrawlHandler instance reused with shared dependencies
//            CrawlHandler handler = new CrawlHandler(new CrawlerService(), new Gson(), new HandleJsonResponse());
//            server.createContext("/api/v1/crawl", handler);
//            server.createContext("/api/v1/crawl/links", handler);
//            server.createContext("/api/v1/crawl/link", handler);
//            server.start(); // Start the server
//
//        }catch(IOException e){
//            System.err.println("Failed to start server: "+ e.getMessage());
//            e.printStackTrace();
//        }
//    }

    public static void main(String[] args) {
        System.out.println("Crawler Server Starting...");

        try {
            // Initialize dependencies
            Gson gson = new Gson();
            CrawlerService crawlerService = new CrawlerService();
            HandleJsonResponse handleJsonResponse = new HandleJsonResponse();

            // Initialize and start the server
            CrawlerController controller = new CrawlerController(crawlerService, gson, handleJsonResponse, 8080);
            controller.start();

        } catch (Exception e) {
            System.err.println("Failed to start server: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
