package testdata;

import org.example.model.Portfolio;
import org.example.model.Security;

import java.math.BigDecimal;
import java.util.List;

public class PortfolioTestData {

    public static final String IBM = "IBM";
    public static final String MSFT = "MSFT";
    public static final String ORCL = "ORCL";
    public static final String AAPL = "AAPL";
    public static final String HD = "HD";
    private static final BigDecimal TOTAL_ASSET_VALUE = BigDecimal.valueOf(100_000);
    private static final BigDecimal TARGET_PERCENTAGE = BigDecimal.valueOf(20);

    public static Portfolio portfolioWithAllSecurities() {
        return Portfolio.builder()
                .totalAssetValue(TOTAL_ASSET_VALUE)
                .securities(List.of(
                        Security.builder()
                                .symbol(IBM)
                                .targetPercentage(TARGET_PERCENTAGE)
                                .currentPercentage(BigDecimal.valueOf(10))
                                .price(BigDecimal.valueOf(150))
                                .build(),
                        Security.builder()
                                .symbol(MSFT)
                                .targetPercentage(TARGET_PERCENTAGE)
                                .currentPercentage(BigDecimal.valueOf(20))
                                .price(BigDecimal.valueOf(90))
                                .build(),
                        Security.builder()
                                .symbol(ORCL)
                                .targetPercentage(TARGET_PERCENTAGE)
                                .currentPercentage(BigDecimal.valueOf(30))
                                .price(BigDecimal.valueOf(220))
                                .build(),
                        Security.builder()
                                .symbol(AAPL)
                                .targetPercentage(TARGET_PERCENTAGE)
                                .currentPercentage(BigDecimal.valueOf(20))
                                .price(BigDecimal.valueOf(450))
                                .build(),
                        Security.builder()
                                .symbol(HD)
                                .targetPercentage(TARGET_PERCENTAGE)
                                .currentPercentage(BigDecimal.valueOf(20))
                                .price(BigDecimal.valueOf(70))
                                .build()))
                .build();
    }

    public static Portfolio portfolioWithoutSecurities() {
        return Portfolio.builder()
                .totalAssetValue(TOTAL_ASSET_VALUE)
                .securities(List.of())
                .build();
    }

    public static Portfolio portfolioWithGivenCurrentPercentage(BigDecimal currentPercentage) {
        return Portfolio.builder()
                .totalAssetValue(TOTAL_ASSET_VALUE)
                .securities(List.of(
                        Security.builder()
                                .symbol(IBM)
                                .targetPercentage(TARGET_PERCENTAGE)
                                .currentPercentage(currentPercentage)
                                .price(BigDecimal.valueOf(150))
                                .build()
                ))
                .build();
    }

}
