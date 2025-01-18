package service;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.CompletableFuture;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class CrawlerService {
    private Set<String> visitedLinks = new HashSet<>();
    private  List<String> crawledLinks = new ArrayList<>();


    //Asynchronous calls on the URLs
    public CompletableFuture<Void> startAsynchronousCrawl(String url, int depth){
        return CompletableFuture.runAsync(() -> startCrawling(url, depth));
    }
    //method to crawl through urls
    public void startCrawling(String url, int depth){

        if(depth == 0 || visitedLinks.contains(url)){
            return; // Stop if depth is 0 or URL has already been visited
        }

        try{
            System.out.println("Crawling URL: " + url);
            visitedLinks.add(url);
            crawledLinks.add(url);

            Document document = Jsoup.connect(url).get(); //fetch doc content

            Elements links = document.select("a[href]");
            for (Element link : links){
                String nextUrl = link.absUrl("href");

                //avoid duplicate links
                if(!visitedLinks.contains(nextUrl)){
                    startCrawling(nextUrl, depth -1);
                }
            }

        } catch(Exception e){
            System.err.println("Error fetching URL: " + url + "_" + e.getMessage());
            e.printStackTrace();
        }

    }

    // Retrieve the list of all crawled links
    public List<String> getCrawledLinks(){
        return new ArrayList<>( crawledLinks);
    }

    //add crawled link
    public void addCrawledLink(String url){
        if(!visitedLinks.contains(url)){
            crawledLinks.add(url);
            visitedLinks.add(url);
        }
    }

    //update crawled links
    public void updateCrawledLink(int index, String newUrl){
        if(index >= 0 && index < crawledLinks.size()){
            String oldUrl = crawledLinks.get(index);
            visitedLinks.remove(oldUrl);
            crawledLinks.set(index, newUrl);
            visitedLinks.add(newUrl);
        }    }

    //delete crawled link
    public void deleteCrawledLink(int index){
        if(index >= 0 && index < crawledLinks.size()){
            String url = crawledLinks.get(index);
            visitedLinks.remove(url);
            crawledLinks.remove(index);
        }
    }
}
