package threading.threadpool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

public class RestCall {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        URLProcessor[] urls = {
                new URLProcessor("url1"),
                new URLProcessor("url2"),
                new URLProcessor("url3"),
                new URLProcessor("url4"),
                new URLProcessor("url5"),
                new URLProcessor("url6")
        };
        ExecutorService service = Executors.newFixedThreadPool(3);
        Future<String> future = null;
        for (URLProcessor url : urls) {
             future = service.submit(url);
            System.out.println(future.get());
        }
        service.shutdown();

        List<String> urlList = new ArrayList<>();
        urlList.add("url1");
        urlList.add("url2");
        urlList.add("url3");
        urlList.add("url4");
        urlList.add("url5");
        urlList.add("url6");
        for (String url : urlList) {
            CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> {
                return getUrlDetails(url);
            });
            System.out.println(completableFuture.get());
    }
}

    private static <U> String getUrlDetails(String url) {
        Map<String, String> urlMap = new HashMap<>();
        urlMap.put("url1", "{\"result\" : \"hello url1\"}");
        urlMap.put("url2", "{\"result\" : \"hello url2\"}");
        urlMap.put("url3", "{\"result\" : \"hello url3\"}");
        urlMap.put("url4", "{\"result\" : \"hello url4\"}");
        urlMap.put("url5", "{\"result\" : \"hello url5\"}");
        urlMap.put("url6", "{\"result\" : \"hello url6\"}");
        return urlMap.get(url);
    }
    }

    class URLProcessor implements Callable<String> {
    String url;

    URLProcessor(String url) {
        this.url = url;
    }

    public String call() {
        try {
            Thread.sleep(1000);
            return getUrlDetails(this.url);
        } catch (InterruptedException e) {
        }
        return null;
    }

    private String getUrlDetails(String url) {
        Map<String, String> urlMap = new HashMap<>();
        urlMap.put("url1", "{\"result\" : \"hello url1\"}");
        urlMap.put("url2", "{\"result\" : \"hello url2\"}");
        urlMap.put("url3", "{\"result\" : \"hello url3\"}");
        urlMap.put("url4", "{\"result\" : \"hello url4\"}");
        urlMap.put("url5", "{\"result\" : \"hello url5\"}");
        urlMap.put("url6", "{\"result\" : \"hello url6\"}");
        return urlMap.get(url);
    }
}