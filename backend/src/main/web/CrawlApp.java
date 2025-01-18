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
