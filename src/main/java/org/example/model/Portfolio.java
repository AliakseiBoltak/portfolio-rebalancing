package org.example.model;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.util.List;

@Value
@Builder
public class Portfolio {

    BigDecimal totalAssetValue;
    List<Security> securities;

}
