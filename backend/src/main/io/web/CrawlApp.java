import controllers.CrawlerController;

import javax.servlet.http.HttpServlet;
import java.io.IOException;

public class CrawlApp {
    public static void main(String[] args) {
        System.out.println("Crawler Server Started...");

        try{
            //initialise and start server
            CrawlerController.main(args);
        }catch(IOException e){
            System.err.println("Failed to start server: "+ e.getMessage());
        }
    }

}
