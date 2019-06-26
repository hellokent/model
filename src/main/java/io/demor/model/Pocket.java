package io.demor.model;

import com.google.common.collect.Maps;
import lombok.Data;
import lombok.extern.log4j.Log4j2;

import java.util.Map;

@Data
@Log4j2
public class Pocket {

    private double amount;

    private Map<String, Stock> stockMap = Maps.newHashMap();

    public double buy(KLine kLine, double scale) {
        //使用收盘价来买入
        double unitPrice = kLine.getEnd();

        if (amount < unitPrice) {
            return 0;
        }

        double usingAmount = scale * amount;
        int count = (int) Math.floor(usingAmount / unitPrice);
        double cost = count * unitPrice * 1.0003; // 份额 * 单价 * 手续费

        if (cost > usingAmount) {
            return 0;
        }
        amount -= cost;

        Stock stock = stockMap.get(kLine.getName());
        if (stock == null) {
            stock = new Stock();
            stockMap.put(kLine.getName(), stock);
        }
        stock.addAmount(unitPrice, count);
        log.info("buy at {}, cost:{}, now:{}", kLine.getDate(), cost, amount);
        return cost;
    }

    public double sell(KLine kLine, double scale) {
        Stock stock = stockMap.get(kLine.getName());
        if (stock == null || stock.getAmount() == 0) {
            return 0;
        }

        int count = (int) Math.floor(stock.getAmount() * scale);
        stock.amount -= count;
        double earn = count * kLine.getEnd();
        amount += earn;

        if (stock.amount <= 0) {
            stockMap.remove(kLine.getName());
        }

        log.info("sell at {}, earn:{}, now:{}", kLine.getDate(), earn, amount);
        return earn;
    }

    public int getAmount(KLine kLine) {
        Stock stock = stockMap.get(kLine.getName());
        if (stock == null) {
            return 0;
        } else {
            return stock.amount;
        }
    }

    public Double getOriginPrice(KLine kLine) {
        Stock stock = stockMap.get(kLine.getName());
        if (stock == null) {
            return null;
        } else {
            return stock.getOriginPrice();
        }
    }

    @Data
    public static class Stock {

        private double originPrice;

        private int amount;

        void addAmount(double newUnitPrice, int addAmount) {
            double olderPrice = amount * originPrice;
            double newPrice = newUnitPrice * addAmount;

            amount += addAmount;
            olderPrice = (newPrice + olderPrice) / amount;
        }
    }
}
