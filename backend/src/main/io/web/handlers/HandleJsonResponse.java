package handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import service.CrawlerService;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class HandleJsonResponse {
    static void sendJsonResponseBody(HttpExchange exchange, CrawlerService crawlerService, Gson gson) throws IOException {
        List<String> crawledLinks = crawlerService.getCrawledLinks();

        String jsonResponse = gson.toJson(crawledLinks);

        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(200, jsonResponse.length());
        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(jsonResponse.getBytes());
        outputStream.close();
    }
}
