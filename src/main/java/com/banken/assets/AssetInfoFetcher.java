package com.banken.assets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

public class AssetInfoFetcher {

    public enum Stock {
        ERICSSON("https://www.nordnet.se/mux/web/marknaden/aktiehemsidan/index.html?identifier=101&marketid=11"),
        LATUOR("https://www.nordnet.se/mux/web/marknaden/aktiehemsidan/index.html?identifier=914&marketid=11"),
        LUNDBERGS("https://www.nordnet.se/mux/web/marknaden/aktiehemsidan/index.html?identifier=1012&marketid=11"),
        TRACTION("https://www.nordnet.se/mux/web/marknaden/aktiehemsidan/index.html?identifier=4963&marketid=11"),
        SPECTRACURE("https://www.nordnet.se/mux/web/marknaden/aktiehemsidan/index.html?identifier=4138&marketid=52"),
        OBSTECARE("https://www.nordnet.se/mux/web/marknaden/aktiehemsidan/index.html?identifier=44H7&marketid=52"),
        CLIMEON("https://www.nordnet.se/mux/web/marknaden/aktiehemsidan/index.html?identifier=144743&marketid=11"),

            ;
        private final String url;

        Stock(String url) {
            this.url = url;
        }

        public String getUrl() {
            return url;
        }
    }

    public static void main(String[] args) {
        AssetInfoFetcher assetInfoFetcher = new AssetInfoFetcher();
        assetInfoFetcher.fetch();
    }

    private void fetch() {
        Arrays.stream(Stock.values()).forEach(stock -> fetchStockData(stock));
    }

    private void fetchStockData(Stock stock) {
        try {
            URL url = new URL(stock.getUrl());
            BufferedReader b = new BufferedReader(new InputStreamReader(url.openStream()));
//            b.lines().forEach(line -> System.out.println(line));

            StockXmlParseHelper stockXmlParseHelper = new StockXmlParseHelper();
            String line;
            boolean foundValue = false;
            while ((line = b.readLine()) != null && !foundValue) {
                foundValue = stockXmlParseHelper.processLine(line);
            }
            String value = stockXmlParseHelper.getValue();
            System.out.println("value = " + value);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
