package org.example.core;

import org.example.model.Portfolio;
import org.example.model.Security;
import org.example.model.Trade;
import org.example.model.TradeAction;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class PortfolioRebalancingCalculator {

    private static final int QUANTITY_SCALE = 4;
    private static final RoundingMode QUANTITY_ROUNDING_MODE = RoundingMode.HALF_UP;

    public List<Trade> calculateTrades(Portfolio portfolio) {
        return portfolio.getSecurities().stream()
                .map(security -> calculateTrade(security, portfolio.getTotalAssetValue()))
                .toList();
    }

    private Trade calculateTrade(Security security, BigDecimal totalAssetValue) {
        BigDecimal targetVariance = calculateTargetVariance(security);
        TradeAction action = resolveTradeAction(targetVariance);
        BigDecimal quantity = calculateQuantity(totalAssetValue, targetVariance, security.getPrice());
        return Trade.builder()
                .symbol(security.getSymbol())
                .action(action)
                .quantity(quantity)
                .build();
    }

    private BigDecimal calculateTargetVariance(Security security) {
        return security.getCurrentPercentage()
                .subtract(security.getTargetPercentage());
    }

    private TradeAction resolveTradeAction(BigDecimal targetVariance) {
        int comparisonResult = targetVariance.compareTo(BigDecimal.ZERO);
        if (comparisonResult < 0) {
            return TradeAction.BUY;
        }
        if (comparisonResult > 0) {
            return TradeAction.SELL;
        }
        return TradeAction.HOLD;
    }

    private BigDecimal calculateQuantity(BigDecimal totalAssetValue,
                                         BigDecimal targetVariance,
                                         BigDecimal price) {
        BigDecimal rebalanceAmount = calculateRebalanceAmount(totalAssetValue, targetVariance);
        return rebalanceAmount.divide(price, QUANTITY_SCALE, QUANTITY_ROUNDING_MODE);
    }

    private BigDecimal calculateRebalanceAmount(BigDecimal totalAssetValue,
                                                BigDecimal targetVariance) {
        return totalAssetValue
                .multiply(targetVariance.abs())
                .divide(BigDecimal.valueOf(100), QUANTITY_SCALE, QUANTITY_ROUNDING_MODE);
    }

}
