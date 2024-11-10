package store.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import store.Constants;

public class PurchaseHistoryTest {

    private PurchaseHistory purchaseHistory;


    @BeforeEach
    void init() {
        purchaseHistory = PurchaseHistory.from(Constants.PRODUCT_NAME);
    }

    private static Stream<Arguments> provideNumbersAndSumNumber() {
        return Stream.of(
                Arguments.of(List.of(1000), 1000),
                Arguments.of(List.of(1000, 6000), 7000),
                Arguments.of(List.of(1000, 9000, 100000), 110000)
        );
    }

    @DisplayName("구매 내역에 구매 가격을 추가로 기록한다.")
    @ParameterizedTest
    @MethodSource("provideNumbersAndSumNumber")
    void addPurchasePrice(final List<Integer> prices, final int expectedPrice) {
        PurchaseHistory expected = PurchaseHistory.from(Constants.PRODUCT_NAME);
        expected.addPurchasePrice(expectedPrice);

        for (final int price : prices) {
            purchaseHistory.addPurchasePrice(price);
        }

        assertThat(purchaseHistory).usingRecursiveComparison().isEqualTo(expected);
    }

    @DisplayName("구매 내역에 구매 수량을 추가로 기록한다.")
    @ParameterizedTest
    @MethodSource("provideNumbersAndSumNumber")
    void addQuantity(final List<Integer> quantities, final int expectedQuantity) {
        PurchaseHistory expected = PurchaseHistory.from(Constants.PRODUCT_NAME);
        expected.addQuantity(expectedQuantity);

        for (final int quantity : quantities) {
            purchaseHistory.addQuantity(quantity);
        }

        assertThat(purchaseHistory).usingRecursiveComparison().isEqualTo(expected);
    }

    @DisplayName("구매 내역에 프로모션 구매 수량을 추가로 기록한다.")
    @ParameterizedTest
    @MethodSource("provideNumbersAndSumNumber")
    void addPromotionQuantity(final List<Integer> promotionQuantities, final int expectedPromotionQuantity) {
        PurchaseHistory expected = PurchaseHistory.from(Constants.PRODUCT_NAME);
        expected.addPromotionQuantity(expectedPromotionQuantity);

        for (final int promotionQuantity : promotionQuantities) {
            purchaseHistory.addPromotionQuantity(promotionQuantity);
        }

        assertThat(purchaseHistory).usingRecursiveComparison().isEqualTo(expected);
    }

    @DisplayName("구매 내역에 프로모션 혜택으로 받은 무료 수량을 추가로 기록한다.")
    @ParameterizedTest
    @MethodSource("provideNumbersAndSumNumber")
    void addFreeQuantity(final List<Integer> freeQuantities, final int expectedFreeQuantity) {
        PurchaseHistory expected = PurchaseHistory.from(Constants.PRODUCT_NAME);
        expected.addFreeQuantity(expectedFreeQuantity);

        for (final int freeQuantity : freeQuantities) {
            purchaseHistory.addFreeQuantity(freeQuantity);
        }

        assertThat(purchaseHistory).usingRecursiveComparison().isEqualTo(expected);
    }

    @DisplayName("기록된 구매 내역을 바탕으로 완료된 구매 내역을 생성한다.")
    @ParameterizedTest
    @CsvSource(value = {"1000:1:1:1", "1000:2:3:4", "4000:3:2:1", "1000:1:0:0"}, delimiter = ':')
    void purchaseComplete(final int price, final int quantity, final int promotionQuantity, final int freeQuantity) {
        CompletedPurchaseHistory expected = CompletedPurchaseHistory.of(Constants.PRODUCT_NAME, price, quantity,
                promotionQuantity, freeQuantity);

        purchaseHistory.addPurchasePrice(price);
        purchaseHistory.addQuantity(quantity);
        purchaseHistory.addPromotionQuantity(promotionQuantity);
        purchaseHistory.addFreeQuantity(freeQuantity);
        CompletedPurchaseHistory actual = purchaseHistory.purchaseComplete();

        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }


}
