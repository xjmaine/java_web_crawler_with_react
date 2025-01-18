package handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import service.CrawlerService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

public class CrawlHandler implements HttpHandlers {
    private static CrawlerService crawlerService;
    private static Gson gson;
    private static HandleJsonResponse handleJsonResponse;

    public CrawlHandler(CrawlerService crawlerService, Gson gson,HandleJsonResponse handleJsonResponse ){
        this.crawlerService = crawlerService;
        this.gson = gson;
        this.handleJsonResponse = handleJsonResponse;
    }


    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String method = exchange.getRequestMethod();

        switch (path) {
            case "/api/v1/crawl":
                if ("POST".equalsIgnoreCase(method)) {
                    handlePost(exchange);
                } else {
                    sendErrorResponse(exchange, 400, "Method not allowed for /api/v1/crawl");
                }
                break;

            case "/api/v1/crawl/links":
                if ("GET".equalsIgnoreCase(method)) {
                    handleGetLinks(exchange);
                } else {
                    sendErrorResponse(exchange, 400, "Method not allowed for /api/v1/crawl/links");
                }
                break;

            case "/api/v1/crawl/link":
                switch (method.toUpperCase()) {
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
                        sendErrorResponse(exchange, 405, "Method not allowed for /api/v1/crawl/link");
                }
                break;

            default:
                sendErrorResponse(exchange, 404, "Endpoint not found");
        }
    }

    @Override
    public void handlePost(HttpExchange exchange) throws IOException {
        try {
            String requestBody = HandleJsonResponse.readRequestBody(exchange.getRequestBody());
            Map<String, Object> requestBodyMap = gson.fromJson(requestBody, Map.class);

            // Validate and extract the URL and depth
             String url = (String) requestBodyMap.get("url");
//            int depth = ((Double) requestBodyMap.get("depth")).intValue();
            if(url == null || url.isEmpty()) {
                sendErrorResponse(exchange, 400, "URL is required");
                return;
            }

            Object objectDepth = requestBodyMap.get("depth");
            if(objectDepth == null|| !(objectDepth instanceof Double)){
                sendErrorResponse(exchange, 400, "Depth must be a number");
                return;
            }

            int depth = ((Double) objectDepth).intValue();
            if(depth <= 0 || depth > Integer.MAX_VALUE){
                sendErrorResponse(exchange, 400, "Depth must be a positive number and less than or equal to 10");
            }

            crawlerService.startCrawling(url, depth); //start crawling service

            //return crawled links in json reponse
            List<String> crawledResults = crawlerService.getCrawledLinks();
            handleJsonResponse.sendJsonResponse(exchange, Map.of(
                    "message", "Crawling completed successfully. ",
                    "url", url,
                    "depth", depth,
                    "crawledResults", crawledResults
            ));


        } catch (Exception e) {
            sendErrorResponse(exchange, 500, "Failed to process request: " + e.getMessage());
        }
    }

    private void handleGetLinks(HttpExchange exchange) throws IOException {
        try {
            handleJsonResponse.sendJsonResponse(exchange, crawlerService.getCrawledLinks());
        } catch (Exception e) {
            sendErrorResponse(exchange, 500, "Failed to retrieve links: " + e.getMessage());
        }
    }

    private void handlePostLink(HttpExchange exchange) throws IOException {
        try {
            String requestBody = HandleJsonResponse.readRequestBody(exchange.getRequestBody());
            Map<String, Object> requestBodyMap = gson.fromJson(requestBody, Map.class);

             String url = (String) requestBodyMap.get("url");
            crawlerService.addCrawledLink(url);

            handleJsonResponse.sendJsonResponse(exchange, Map.of("message", "Link added", "url", url));
        } catch (Exception e) {
            sendErrorResponse(exchange, 500, "Failed to add link: " + e.getMessage());
        }
    }

    private void handlePutLink(HttpExchange exchange) throws IOException {
        try {
            String requestBody = HandleJsonResponse.readRequestBody(exchange.getRequestBody());
            Map<String, Object> requestBodyMap = gson.fromJson(requestBody, Map.class);

            int index = ((Double) requestBodyMap.get("index")).intValue();
            String newUrl = (String) requestBodyMap.get("newUrl");

            crawlerService.updateCrawledLink(index, newUrl);
            handleJsonResponse.sendJsonResponse(exchange, Map.of("message", "Link updated", "newUrl", newUrl));
        } catch (Exception e) {
            sendErrorResponse(exchange, 500, "Failed to update link: " + e.getMessage());
        }
    }

    private void handleDeleteLink(HttpExchange exchange) throws IOException {
        try {
            String requestBody = HandleJsonResponse.readRequestBody(exchange.getRequestBody());
            Map<String, Object> requestBodyMap = gson.fromJson(requestBody, Map.class);

            int index = ((Double) requestBodyMap.get("index")).intValue();
            crawlerService.deleteCrawledLink(index);

            handleJsonResponse.sendJsonResponse(exchange, Map.of("message", "Link deleted", "index", index));
        } catch (Exception e) {
            sendErrorResponse(exchange, 500, "Failed to delete link: " + e.getMessage());
        }
    }

    // Utility method to send error responses
    private void sendErrorResponse(HttpExchange exchange, int statusCode, String message) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        String errorResponse = gson.toJson(Map.of("error", message));
        exchange.sendResponseHeaders(statusCode, errorResponse.length());
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(errorResponse.getBytes());
        }
    }

}
