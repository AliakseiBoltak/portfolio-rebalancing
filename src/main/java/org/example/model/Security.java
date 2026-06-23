package org.example.model;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

@Value
@Builder
public class Security {

    String symbol;
    BigDecimal targetPercentage;
    BigDecimal currentPercentage;
    BigDecimal price;

}
