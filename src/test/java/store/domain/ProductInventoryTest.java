package store.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class ProductInventoryTest {

    private static final String PRODUCT_NAME = "MOCK";
    private ProductInventory productInventory;


    @BeforeEach
    void init() {
        LocalDate start = LocalDate.of(2023, 11, 01);
        LocalDate end = LocalDate.of(2023, 11, 30);
        Promotion promotion = Promotion.of("MOCK_PROMOTION", BuyGet.of(1, 1), DateRange.of(start, end));
        Map<String, ProductInfo> infos = Map.of(PRODUCT_NAME, ProductInfo.of("MOCK", 1000));
        Map<String, ProductQuantity> quantities = Map.of(PRODUCT_NAME, ProductQuantity.from(5));
        Map<String, PromotionProductQuantity> promotionQuantities = Map.of(PRODUCT_NAME,
                PromotionProductQuantity.of(10, promotion));
        productInventory = ProductInventory.of(infos, quantities, promotionQuantities);
    }

    @DisplayName("물건이 존재한다면 true, 존재하지 않는다면 false를 반환한다.")
    @ParameterizedTest
    @CsvSource(value = {"MOCK:true", "김치:false", "오렌지주스:false"}, delimiter = ':')
    void containsProductName(String productName, boolean expected){
        assertThat(productInventory.containsProductName(productName)).isEqualTo(expected);
    }

    @DisplayName("수량이 충분하면 true, 수량이 부족하면 false를 반환한다.")
    @ParameterizedTest
    @CsvSource(value = {"1:true", "10:true", "15:true", "16:false", "200:false"}, delimiter = ':')
    void isLessThanQuantity(int purchaseQuantity, boolean expected){
        LocalDate inPromotionRangeDate = LocalDate.of(2023,11,01);

        assertThat(productInventory.isLessThanQuantity(PRODUCT_NAME, purchaseQuantity, inPromotionRangeDate))
                .isEqualTo(expected);
    }

    @DisplayName("현재날짜가 프로모션 날짜에 포함되면 프로모션의 수량을 포함하고, 그렇지 않다면 포함하지 않는다.")
    @ParameterizedTest
    @CsvSource(value = {"2023-11-01:true", "2023-11-30:true", "2023-10-31:false", "2023-12-01:false"}, delimiter = ':')
    void 프로모션_날짜_테스트(LocalDate today, boolean expected){
        int purchaseQuantity = 15;

        assertThat(productInventory.isLessThanQuantity(PRODUCT_NAME, purchaseQuantity, today))
                .isEqualTo(expected);
    }


}
