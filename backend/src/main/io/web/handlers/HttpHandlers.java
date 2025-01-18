package handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

public interface HttpHandlers extends HttpHandler {
//    void handle (HttpExchange exchange) throws IOException;
    void handlePost (HttpExchange exchange) throws IOException;
}
