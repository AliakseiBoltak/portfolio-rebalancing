package tests;

import org.example.core.PortfolioRebalancingCalculator;
import org.example.model.Trade;
import org.example.model.TradeAction;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static testdata.PortfolioTestData.*;

public class PortfolioRebalancingTest {

    private PortfolioRebalancingCalculator calculator;
    private List<Trade> trades;

    @DataProvider(name = "expectedTrades")
    public Object[][] expectedTrades() {
        return new Object[][]{
                {IBM, TradeAction.BUY, new BigDecimal("66.6667")},
                {MSFT, TradeAction.HOLD, new BigDecimal("0.0000")},
                {ORCL, TradeAction.SELL, new BigDecimal("45.4545")},
                {AAPL, TradeAction.HOLD, new BigDecimal("0.0000")},
                {HD, TradeAction.HOLD, new BigDecimal("0.0000")}
        };
    }

    @DataProvider(name = "targetVarianceBoundaryData")
    public Object[][] targetVarianceBoundaryData() {
        return new Object[][]{
                {new BigDecimal("19.9999"), TradeAction.BUY},
                {new BigDecimal("20.0000"), TradeAction.HOLD},
                {new BigDecimal("20.0001"), TradeAction.SELL}
        };
    }

    @BeforeClass
    public void setUp() {
        calculator = new PortfolioRebalancingCalculator();
        trades = calculator.calculateTrades(portfolioWithAllSecurities());
    }

    @Test(dataProvider = "expectedTrades",
            description = "AT-001 (TC-001, TC-002, TC-003, TC-004, TC-005) " +
                    "- Verify expected trade action and number of shares for each security.")
    public void shouldCalculateExpectedTradeForEachSecurity(String symbol,
                                                            TradeAction expectedAction,
                                                            BigDecimal expectedQuantity) {
        assertThat(trades)
                .filteredOn(trade -> trade.getSymbol().equals(symbol))
                .singleElement()
                .satisfies(trade -> {
                    assertThat(trade.getAction()).isEqualTo(expectedAction);
                    assertThat(trade.getQuantity()).isEqualByComparingTo(expectedQuantity);
                });
    }

    @Test(description = "AT-002 (TC-006, TC-008) " +
            "- Verify that a trade instruction is returned for every security in the portfolio.")
    public void shouldCalculateTradesForAllSecurities() {
        assertThat(trades)
                .hasSize(5);

        assertThat(trades)
                .extracting(Trade::getSymbol)
                .containsExactly(IBM, MSFT, ORCL, AAPL, HD);

        assertThat(trades)
                .extracting(Trade::getAction)
                .containsExactly(
                        TradeAction.BUY,
                        TradeAction.HOLD,
                        TradeAction.SELL,
                        TradeAction.HOLD,
                        TradeAction.HOLD
                );
    }

    @Test(description = "AT-003 (TC-007) " +
                    "- Verify that calculated number of shares is rounded to four decimal places.")
    public void shouldRoundNumberOfSharesToFourDecimalPlaces() {
        assertThat(trades)
                .filteredOn(trade -> trade.getSymbol().equals(IBM))
                .singleElement()
                .extracting(Trade::getQuantity)
                .isEqualTo(new BigDecimal("66.6667"));
    }

    @Test(description = "AT-004 (TC-009) " +
                    "- Verify that empty trade list is returned when portfolio contains no securities.")
    public void shouldReturnEmptyTradeListWhenPortfolioHasNoSecurities() {
        List<Trade> actualTrades = calculator.calculateTrades(portfolioWithoutSecurities());

        assertThat(actualTrades)
                .isEmpty();
    }

    @Test(dataProvider = "targetVarianceBoundaryData",
            description = "AT-005 (TC-010, TC-011, TC-012) " +
                    "- Verify trade action around the target allocation boundary.")
    public void shouldDetermineTradeActionAroundTargetAllocationBoundary(BigDecimal currentPercentage,
                                                                         TradeAction expectedAction) {
        List<Trade> actualTrades = calculator
                .calculateTrades(portfolioWithGivenCurrentPercentage(currentPercentage));

        assertThat(actualTrades)
                .singleElement()
                .extracting(Trade::getAction)
                .isEqualTo(expectedAction);
    }

}