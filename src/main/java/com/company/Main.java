package com.company;

import jdk.nashorn.internal.runtime.JSONFunctions;
import org.jsoup.*;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Document;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.json.simple.JSONValue;

public class Main {

    public static void main(String[] args) throws IOException {
        // write your code here
        Document page;
        String top10[] = new String[10];
        Integer size[];
        String names[];
        HashMap<String, Integer> map = new HashMap<String, Integer>();
        ValueComparator bvc = new ValueComparator(map);
        TreeMap<String, Integer> sorted_map = new TreeMap<String, Integer>(bvc);
        int i = 0;
        String tmp;


        File input = new File("C:/Users/Gowtham/Downloads/fb/html/messages.htm");
        File output = new File("C:/Users/Gowtham/Downloads/fb/html/messages_top.json");
        if (!output.exists()) {
            output.createNewFile();
        } else {
            output.delete();
            output.createNewFile();
        }
        FileWriter fw = new FileWriter(output);
        BufferedWriter bw = new BufferedWriter(fw);
        try {
            // Document tester = Jsoup.parse(test);
            // System.out.println(tester.select(".thread").select(".user").first().text());
            page = Jsoup.parse(input, "UTF-8");
            Elements threads = page.select(".thread");
            System.out.println("Number of threads :" + threads.size());
            for (Element x : threads) {
                Elements message = x.select(".message");
                i++;
                tmp = x.select(".user").first().text();
                if (map.containsKey(tmp)) {
                    if (tmp.equals("Gow Tham")) {
                        if (x.select(".user").size() > 2) {
                            tmp = x.select(".user").get(1).text() + (Math.random() % 100) + "a";
                        } else
                            tmp = x.select(".user").last().text() + (Math.random() % 100) + "b";
                    } else {
                        tmp += (Math.random() % 100) + "c";
                    }
                }
                map.put(tmp, message.size());

            }
        } catch (Exception e) {
            System.err.print(e);
        }
        System.out.println(map);
        sorted_map.putAll(map);
        System.out.println(sorted_map);
        bw.write(JSONValue.toJSONString(sorted_map));
        bw.close();


    }
}

class ValueComparator implements Comparator<String> {

    Map<String, Integer> base;

    public ValueComparator(Map<String, Integer> base) {
        this.base = base;
    }

    // Note: this comparator imposes orderings that are inconsistent with equals.
    public int compare(String a, String b) {
        if (base.get(a) >= base.get(b)) {
            return -1;
        } else {
            return 1;
        } // returning 0 would merge keys
    }
}