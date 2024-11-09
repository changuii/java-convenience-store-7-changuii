package store.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

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

    @DisplayName("현재날짜가 포로모션 날짜에 포함하면서, 프로모션 물품이 있다면 true 아니라면 false를 반환한다.")
    @ParameterizedTest
    @CsvSource(value = {"2023-11-01:MOCK:true", "2023-10-31:MOCK:false", "2023-11-01:김치:false"}, delimiter = ':')
    void 프로모션_상품_존재_여부_테스트(final LocalDate today, final String productName, final boolean expected){
        assertThat(productInventory.isPromotionProduct(productName, today))
                .isEqualTo(expected);
    }

    @DisplayName("상품 이름에 해당하는 상품 수량을 감소시킨다.")
    @ParameterizedTest
    @CsvSource(value = {"1:4", "2:3", "3:2", "4:1", "5:0"}, delimiter = ':')
    void 일반_상품_구매_테스트(final int purchaseQuantity, final int expected){
        Map<String, ProductQuantity> expectedQuantities = Map.of(PRODUCT_NAME, ProductQuantity.from(expected));

        productInventory.purchaseProduct(PRODUCT_NAME, purchaseQuantity);

        assertThat(productInventory)
                .extracting("quantities")
                .usingRecursiveComparison()
                .isEqualTo(expectedQuantities);
    }

    @DisplayName("상품 이름에 해당하는 상품 수량만큼의 가격을 반환한다.")
    @ParameterizedTest
    @CsvSource(value = {"1:1000", "2:2000", "3:3000", "4:4000", "5:5000"}, delimiter = ':')
    void 일반_상품_구매_금액총합_테스트(int purchaseQuantity, int expected){
        int actual = productInventory.purchaseProduct(PRODUCT_NAME, purchaseQuantity);

        assertThat(actual).isEqualTo(expected);
    }


}