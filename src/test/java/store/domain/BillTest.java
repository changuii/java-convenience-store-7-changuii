package store.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.Constants;

public class BillTest {
    private static final double DISCOUNT_PERSANTAGE = 0.3;
    private static final int MAX_DISCOUNT_PRICE = 8000;

    @DisplayName("멤버십 할인을 적용한다.")
    @Test
    void discountMembership() {
        CompletedPurchaseHistory purchaseHistory = CompletedPurchaseHistory.of(Constants.PRODUCT_NAME, 6000, 2, 4, 2);
        Bill bill = Bill.from(List.of(purchaseHistory));
        int expected = 600;

        bill.discountMembership(DISCOUNT_PERSANTAGE, MAX_DISCOUNT_PRICE);

        assertThat(bill).extracting("membershipDiscount").isEqualTo(expected);
    }

}
