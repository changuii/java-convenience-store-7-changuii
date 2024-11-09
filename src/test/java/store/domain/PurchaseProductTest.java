package store.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import store.Constants;
import store.domain.product.BuyGet;
import store.domain.product.DateRange;
import store.domain.product.ProductQuantity;
import store.domain.product.Promotion;
import store.domain.product.PromotionProductQuantity;

public class PurchaseProductTest {


    @DisplayName("매개변수로 들어온 productName과 이름이 일치한다면 true 아니라면 false를 반환한다.")
    @ParameterizedTest
    @CsvSource(value = {"PRODUCT:true", "감자:false", "라면:false"}, delimiter = ':')
    void isMatchProductName(final String productName, final boolean expected) {
        PurchaseProduct purchaseProduct = PurchaseProduct.of(Constants.PRODUCT_NAME, Constants.PRODUCT_QUANTITY_VALUE);

        assertThat(purchaseProduct.isMatchProductName(productName)).isEqualTo(expected);
    }

    @DisplayName("현재 남은 수량(프로모션 + 일반상품)이 상품의 재고 수량 이하라면 true, 아니라면 false를 반환한다.")
    @ParameterizedTest
    @CsvSource(value = {"15:true", "10:true", "20:false", "16:false"}, delimiter = ':')
    void isLessThanQuantityWithPromotionQuantityAndProductQuantity(final int currentQuantity, final boolean expected) {
        DateRange dateRange = DateRange.of(LocalDate.of(2023, 11, 01), LocalDate.of(2023, 11, 30));
        Promotion promotion = Promotion.of(Constants.PROMOTION_NAME, BuyGet.of(1, 1), dateRange);
        ProductQuantity productQuantity = ProductQuantity.of(Constants.PRODUCT_NAME, 5);
        PromotionProductQuantity promotionProductQuantity = PromotionProductQuantity.of(Constants.PRODUCT_NAME, 10,
                promotion);
        PurchaseProduct purchaseProduct = PurchaseProduct.of(Constants.PRODUCT_NAME, currentQuantity);

        boolean actual = purchaseProduct.isLessThanQuantity(promotionProductQuantity, productQuantity);

        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("현재 남은 수량(일반상품)이 상품의 재고 수량 이하라면 true, 아니라면 false를 반환한다.")
    @ParameterizedTest
    @CsvSource(value = {"5:true", "1:true", "6:false", "100:false"}, delimiter = ':')
    void isLessThanQuantityWithProductQuantity(final int currentQuantity, final boolean expected) {
        ProductQuantity productQuantity = ProductQuantity.of(Constants.PRODUCT_NAME, 5);
        PurchaseProduct purchaseProduct = PurchaseProduct.of(Constants.PRODUCT_NAME, currentQuantity);

        boolean actual = purchaseProduct.isLessThanQuantity(productQuantity);

        assertThat(actual).isEqualTo(expected);
    }

}
