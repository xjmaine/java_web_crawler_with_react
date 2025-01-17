package service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class CrawlerService {
    private Set<String> visitedLinks = new HashSet<>();
    private  List<String> crawledLinks = new ArrayList<>();

    //method to crawl through urls
    public void startCrawling(String url, int depth){
        if(depth == 0){
            return;
        }
        //avoid duplicate links
        if(!visitedLinks.add(url)){
            return;
        }

        try{
            String linkTag = "a[href]";
            Document document = Jsoup.connect(url).get();
            Elements links = document.select(linkTag);

            for(Element link: links){
                String nextUrl = link.absUrl("href");
                if(!visitedLinks.contains(nextUrl)){
                    System.out.println("Crawling URL: "+nextUrl);
                    startCrawling(nextUrl, depth-1);
                }
            }
        }catch (IOException e){
            System.err.println("Error fetching url..." +url);
        }
    }

    public List<String> getCrawledLinks(){
        return  crawledLinks;
    }
}
