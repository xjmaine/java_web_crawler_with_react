package handlers;

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
    private static CrawlerService crawlerService;
    private static Gson gson;
    private static HandleJsonResponse handleJsonResponse;

    public CrawlHandler(CrawlerService crawlerService, Gson gson,HandleJsonResponse handleJsonResponse ){
        this.crawlerService = crawlerService;
        this.gson = gson;
        this.handleJsonResponse = handleJsonResponse;
    }
    public CrawlHandler(){}

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String method = exchange.getRequestMethod();

        switch (path) {
            case "/api/v1/crawl":
                if ("POST".equals(method)) {
                    handlePost(exchange);
                } else {
                    exchange.sendResponseHeaders(405, -1); // Method not allowed
                }
                break;
            case "/api/v1/crawl/links":
                if ("GET".equals(method)) {
                    handleGetLinks(exchange);
                } else {
                    exchange.sendResponseHeaders(405, -1); // Method not allowed
                }
                break;
            case "/api/v1/crawl/link":
                switch (method) {
                    case "POST":
                        handlePostLink(exchange);
                        break;
                    case "PUT":
                        handlePutLink(exchange);
                        break;
                    case "DELETE":
                        handleDeleteLink(exchange);
                        break;
                    default:
                        exchange.sendResponseHeaders(405, -1); // Method not allowed
                }
                break;
            default:
                exchange.sendResponseHeaders(404, -1); // Not found
        }
    }

//    @Override
    public void handlePost(HttpExchange exchange) throws IOException {
        InputStreamReader isr = new InputStreamReader(exchange.getRequestBody(), "utf-8");
        BufferedReader br = new BufferedReader(isr);
        StringBuilder requestBody = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            requestBody.append(line);
        }

        String url = "https://www.iana.org/help/example-domains"; // Parse from requestBody
        int depth = 5; // Parse from requestBody

        crawlerService.startCrawling(url, depth);
        HandleJsonResponse.sendJsonResponseBody(exchange, crawlerService, gson);
    }

    private void handleGetLinks(HttpExchange exchange) throws IOException {
        HandleJsonResponse.sendJsonResponseBody(exchange, crawlerService, gson);
    }

    private void handlePostLink(HttpExchange exchange) throws IOException {
        InputStreamReader isr = new InputStreamReader(exchange.getRequestBody(), "utf-8");
        BufferedReader br = new BufferedReader(isr);
        StringBuilder requestBody = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            requestBody.append(line);
        }

        String url = "https://www.iana.org/help/example-domains"; // Parse from requestBody
        crawlerService.addCrawledLink(url);

        String response = "Link added: " + url;
        exchange.getResponseHeaders().set("Content-Type", "text/plain");
        exchange.sendResponseHeaders(200, response.length());
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    private void handlePutLink(HttpExchange exchange) throws IOException {
        InputStreamReader isr = new InputStreamReader(exchange.getRequestBody(), "utf-8");
        BufferedReader br = new BufferedReader(isr);
        StringBuilder requestBody = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            requestBody.append(line);
        }

        int index = 0; // Parse from requestBody
        String newUrl = "https://www.iana.org/help/example-domains"; // Parse from requestBody
        crawlerService.updateCrawledLink(index, newUrl);

        String response = "Link updated: " + newUrl;
        exchange.getResponseHeaders().set("Content-Type", "text/plain");
        exchange.sendResponseHeaders(200, response.length());
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    private void handleDeleteLink(HttpExchange exchange) throws IOException {
        InputStreamReader isr = new InputStreamReader(exchange.getRequestBody(), "utf-8");
        BufferedReader br = new BufferedReader(isr);
        StringBuilder requestBody = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            requestBody.append(line);
        }

        int index = 0; // Parse from requestBody
        crawlerService.deleteCrawledLink(index);

        String response = "Link deleted at index: " + index;
        exchange.getResponseHeaders().set("Content-Type", "text/plain");
        exchange.sendResponseHeaders(200, response.length());
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}
