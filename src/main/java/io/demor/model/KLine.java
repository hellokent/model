package io.demor.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 2019-06-25,5020.20,4974.99,5020.20,4910.20,99202181,85713850368.00"
 *            开盘     收盘    最高     最低     成交量   成交额
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KLine {

    private String date;

    private double min;

    private double max;

    private double begin;

    private double end;
}
