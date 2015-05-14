package com.company;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.json.Json;
import javax.json.stream.JsonGenerator;
import javax.json.stream.JsonGeneratorFactory;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;


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
        Map<String, Object> properties = new HashMap<String, Object>(1);
        properties.put(JsonGenerator.PRETTY_PRINTING, true);
        JsonGeneratorFactory jgf = Json.createGeneratorFactory(properties);
        DateFormat formatter1 = new SimpleDateFormat("EEEE, MMMMM dd, yyyy 'at' hh:mmaa z");
        DateFormat ISO = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
        ISO.setTimeZone(TimeZone.getTimeZone("UTC"));
        Element msgdet;




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
        JsonGenerator gf = jgf.createGenerator(bw);
        String convo;
        gf.writeStartArray();
        try {
            // Document tester = Jsoup.parse(test);
            // System.out.println(tester.select(".thread").select(".user").first().text());
            page = Jsoup.parse(input, "UTF-8");
            Elements threads = page.select(".thread");
            System.out.println("Number of threads :" + threads.size());
            for (Element x : threads) {
                convo = x.html().toString().substring(0, x.html().toString().indexOf('<'));
                Elements message = x.select("p");
                Elements msgdet1 = x.select(".message");

                Iterator<Element> msgdet2 = msgdet1.iterator();

                map.put(convo, message.size());
                gf.writeStartObject().write("Name", convo).write("Messageno", message.size()).writeStartArray("Messages");
                for (Element msg : message) {
                    if (msgdet2.hasNext()) {
                        msgdet = msgdet2.next();
                        Date s1 = (formatter1.parse(msgdet.select(".meta").first().html().toString()));
                        String dateinISO = ISO.format(s1);
                        gf.writeStartObject().write("msg", msg.html().toString()).write("Name", msgdet.select(".user").first().html().toString()).write("Date", dateinISO).writeEnd();

                    }
                }
                gf.writeEnd().writeEnd();

            }
        } catch (Exception e) {
            System.err.print(e);
        }
        gf.writeEnd();
        gf.close();
        //System.out.println(map);
        //sorted_map.putAll(map);
        // System.out.println(sorted_map);


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