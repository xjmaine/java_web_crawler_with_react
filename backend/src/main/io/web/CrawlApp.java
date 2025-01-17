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
    Gson gson = new Gson();
    HandleJsonResponse handleJsonResponse;

    public CrawlApp(CrawlerService service, Gson gson, HandleJsonResponse handleJsonResponse) {
        this.service = service;
        this.gson = gson;
        this.handleJsonResponse = handleJsonResponse;
    }
//     CrawlHandler crawlHandler = new CrawlHandler(service, gson, handleJsonResponse);

    public static void main(String[] args) {
        System.out.println("Crawler Server Started...");

        try{
            //initialise and start server
            HttpServer server = HttpServer.create(new InetSocketAddress(8080),0);
            server.createContext("/api/v1/crawl", new CrawlHandler());
            server.createContext("/api/v1/crawl/links", new CrawlHandler());
            server.createContext("/api/v1/crawl/link", new CrawlHandler());
            CrawlerController.main(args);
        }catch(IOException e){
            System.err.println("Failed to start server: "+ e.getMessage());
        }
    }

}
