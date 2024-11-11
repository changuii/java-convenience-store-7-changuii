package store.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.Constants;

public class CompletedPurchaseHistoryTest {

    @DisplayName("결제 내역의 총 합을 계산하여 더한 후 반환한다.")
    @Test
    void getSumOfTotalPurchasePrice() {
        CompletedPurchaseHistory purchaseHistory = CompletedPurchaseHistory.of(Constants.PRODUCT_NAME, 10000, 10, 0, 0);
        int actual = 2000;
        int expected = 12000;

        actual = purchaseHistory.getSumOfTotalPurchasePrice(actual);

        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("모든 구매 수량을 계산하고 합산하여 반환한다.")
    @Test
    void getSumOfAllPurchaseQuantity() {
        CompletedPurchaseHistory purchaseHistory = CompletedPurchaseHistory.of(Constants.PRODUCT_NAME, 10000, 10, 5, 5);
        int actual = 10;
        int expected = 30;

        actual = purchaseHistory.getSumOfAllPurchaseQuantity(actual);

        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("프로모션 할인 금액을 계산하고 합산하여 반환한다.")
    @Test
    void getSumOfTotalPromotionDiscount() {
        CompletedPurchaseHistory purchaseHistory = CompletedPurchaseHistory.of(Constants.PRODUCT_NAME, 4000, 0, 4, 2);
        int actual = 3000;
        int expected = 5000;

        actual = purchaseHistory.getSumOfTotalPromotionDiscount(actual);

        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("정가로 구매한 금액을 계산하고 합산하여 반환한다.")
    @Test
    void getSumOfTotalPurchasePriceAtRegularPrice() {
        CompletedPurchaseHistory purchaseHistory = CompletedPurchaseHistory.of(Constants.PRODUCT_NAME, 6000, 2, 4, 2);
        int actual = 1000;
        int expected = 3000;

        actual = purchaseHistory.getSumOfTotalPromotionDiscount(actual);

        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("상품의 가격을 계산하여 반환한다.")
    @Test
    void calculateProductPrice() {
        CompletedPurchaseHistory purchaseHistory = CompletedPurchaseHistory.of(Constants.PRODUCT_NAME, 6000, 2, 4, 2);
        int expected = 1000;

        int actual = purchaseHistory.calculateProductPrice();

        assertThat(actual).isEqualTo(expected);
    }
}
