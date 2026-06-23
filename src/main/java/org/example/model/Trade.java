package org.example.model;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

@Value
@Builder
public class Trade {

    String symbol;
    TradeAction action;
    BigDecimal quantity;

}
