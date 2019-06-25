package io.demor.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NorthMoney {

    private Date date;
    private double hsMoney;
    private double ssMonkey;
    private double northTotal;
}
