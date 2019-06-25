package io.demor.model;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.io.CharStreams;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) throws IOException {
        Gson gson = new Gson();
        ClassLoader cl = Main.class.getClassLoader();

        String data300 = CharStreams.toString(new InputStreamReader(cl.getResourceAsStream("data/300.json")));
        String data500 = CharStreams.toString(new InputStreamReader(cl.getResourceAsStream("data/500.json")));
        String dataSec = CharStreams.toString(new InputStreamReader(cl.getResourceAsStream("data/security.json")));

        List<KLine> kLine300 = parseKLine(data300);
        List<KLine> kline500 = parseKLine(data500);
        List<KLine> klineSec = parseKLine(dataSec);
    }

    public static List<KLine> parseKLine(String data) {
        return Lists.newLinkedList(new Gson()
                .fromJson(data, JsonObject.class)
                .getAsJsonObject("data")
                .getAsJsonArray("klines"))
                .stream()
                .map(JsonElement::getAsString)
                .map(str -> Splitter.on(',').splitToList(str))
                .map(list-> KLine.builder()
                        .date(list.get(0))
                        .begin(Double.parseDouble(list.get(1)))
                        .end(Double.parseDouble(list.get(2)))
                        .max(Double.parseDouble(list.get(3)))
                        .min(Double.parseDouble(list.get(4)))
                        .build())
                .collect(Collectors.toList());
    }
}