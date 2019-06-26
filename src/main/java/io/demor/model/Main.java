package io.demor.model;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.io.CharStreams;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
public class Main {

    public static void main(String[] args) throws IOException {
        ClassLoader cl = Main.class.getClassLoader();

        String data300 = CharStreams.toString(new InputStreamReader(cl.getResourceAsStream("data/300.json")));
        String data500 = CharStreams.toString(new InputStreamReader(cl.getResourceAsStream("data/500.json")));
        String dataSec = CharStreams.toString(new InputStreamReader(cl.getResourceAsStream("data/security.json")));

        List<KLine> kline300 = parseKLine(data300, "300");
        List<KLine> kline500 = parseKLine(data500, "500");
        List<KLine> klineSec = parseKLine(dataSec, "sec");

        single(kline300, 10_0000);
        single(kline500, 10_0000);
        single(klineSec, 10_0000);
    }

    static void single(List<KLine> lines, double initAmount) {
        Pocket pocket = new Pocket();
        pocket.setAmount(initAmount);

        pocket.buy(lines.get(0), 1.0f);

        pocket.sell(lines.get(lines.size() - 1), 1.0f);

        log.info("finally, kLine {}, amount={}", lines.get(0).getName(), pocket.getAmount());
    }

    public static List<KLine> parseKLine(String data, String name) {
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
                        .name(name)
                        .build())
                .collect(Collectors.toList());
    }
}