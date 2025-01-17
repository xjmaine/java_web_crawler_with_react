package handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import service.CrawlerService;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class HandleJsonResponse {

    /**
     * Sends a JSON response body containing the crawled links.
     *
     * @param exchange        the HttpExchange object for the request/response
     * @param crawlerService  the service providing crawled link data
     * @param gson            the Gson instance for JSON serialization
     * @throws IOException if an I/O error occurs
     */

    public static void sendJsonResponse(HttpExchange exchange, Object data) throws IOException {
        Gson gson = new Gson();
        String jsonResponse = gson.toJson(data);

        // Set response headers
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        byte[] responseBytes = jsonResponse.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(200, responseBytes.length);


        try (OutputStream outputStream = exchange.getResponseBody()) {
            outputStream.write(responseBytes); // Write the response body
        }
    }


    /**
     * Sends a JSON error response with the specified status code and message.
     *
     * @param exchange    the HttpExchange object for the request/response
     * @param statusCode  the HTTP status code
     * @param errorMessage the error message to include in the response
     * @throws IOException if an I/O error occurs
     */

    public static void sendErrorResponse(HttpExchange exchange, int statusCode, String errorMessage) throws IOException {
        // Construct the JSON error message
        String errorResponse = String.format("{\"error\": \"%s\"}", errorMessage);

        // Set response headers
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(statusCode, errorResponse.getBytes(StandardCharsets.UTF_8).length);

        // Write the response body
        try (OutputStream outputStream = exchange.getResponseBody()) {
            outputStream.write(errorResponse.getBytes(StandardCharsets.UTF_8));
        }
    }

    public static String readRequestBody(InputStream requestBody) throws IOException {
        StringBuilder requestBodyBuilder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(requestBody, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                requestBodyBuilder.append(line);
            }
        }
        return requestBodyBuilder.toString();
    }
}
