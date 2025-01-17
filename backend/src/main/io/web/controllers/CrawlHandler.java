package controllers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import service.CrawlerService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.List;
import com.google.gson.Gson;

public class CrawlHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if("POST".equals(exchange.getRequestMethod())){
            InputStreamReader inputStreamReader = new InputStreamReader(exchange.getRequestBody(), "UTF-8");
            String response = getString(inputStreamReader);
            exchange.sendResponseHeaders(200, response.length());
            OutputStream outputStream = exchange.getResponseBody();
            outputStream.write(response.getBytes());
            outputStream.close();
        }
        else{
            exchange.sendResponseHeaders(405, -1); // Method not allowed
        }
    }

    private static String getString(InputStreamReader inputStreamReader) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        StringBuilder requestBody = new StringBuilder();

        String line;

        while ((line = bufferedReader.readLine()) != null){
            requestBody.append(line);
        }

        String url = "https://www.iana.org/help/example-domains"; //parse from requestBody
        int depth = 5; //parse from requestBody

        CrawlerService crawlerService = new CrawlerService();
        crawlerService.startCrawling(url, depth);

        List<String> crawledLinks = crawlerService.getCrawledLinks();

        Gson gson = new Gson();
        String jsonResponse = gson.toJson(crawledLinks);
        HttpExchange exchange = null;
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(200, jsonResponse.length());
        OutputStream os = exchange.getResponseBody();
        os.write(jsonResponse.getBytes());
        os.close();
        String response = "Crawling started for URL: " + url;
        return response;
    }
}
