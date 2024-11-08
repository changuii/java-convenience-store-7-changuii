package store.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class PromotionProductQuantityTest {

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
        ProductQuantity productQuantity = ProductQuantity.from(quantity);
        PromotionProductQuantity promotionProductQuantity = PromotionProductQuantity.of(promotionQuantity, promotion);
        LocalDate today = LocalDate.of(2023, 11, 01);
        boolean actual = promotionProductQuantity.isLessThanQuantity(purchaseQuantity, productQuantity, today);

        assertThat(actual).isEqualTo(expected);
    }
}
