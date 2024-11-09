package store.domain.product;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class PromotionProductQuantityTest {
    private static final String PRODUCT_NAME = "MOCK";
    private static final int MOCK_QUANTITY = 100;
    private Promotion promotion;

    @BeforeEach
    void init() {
        DateRange dateRange = DateRange.of(LocalDate.of(2023, 11, 01), LocalDate.of(2023, 11, 30));
        promotion = Promotion.of("", BuyGet.of(1, 1), dateRange);
    }


    @DisplayName("매개변수로_들어온_ProductQuantity와_재고를_합산해_재고가_충분히_있는지_검증한다")
    @ParameterizedTest
    @CsvSource(value = {"1:1:2:true", "2:2:4:true", "1:0:2:false", "2:1:4:false"}, delimiter = ':')
    void isLessThanQuantityTest(int promotionQuantity, int quantity, int purchaseQuantity, boolean expected) {
        ProductQuantity productQuantity = ProductQuantity.of(PRODUCT_NAME, quantity);
        PromotionProductQuantity promotionProductQuantity = PromotionProductQuantity.of(PRODUCT_NAME, promotionQuantity,
                promotion);

        boolean actual = promotionProductQuantity.isLessThanQuantity(purchaseQuantity, productQuantity);

        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("현재 프로모션이 진행중인지 반환하는 메서드")
    @ParameterizedTest
    @CsvSource(value = {"2023-11-01:true", "2023-11-30:true", "2023-10-31:false", "2023-12-01:false"}, delimiter = ':')
    void isValidToday(final LocalDate today, final boolean expected) {
        PromotionProductQuantity promotionQuantity =
                PromotionProductQuantity.of(PRODUCT_NAME, MOCK_QUANTITY, promotion);

        assertThat(promotionQuantity.isValidToday(today)).isEqualTo(expected);
    }

}
