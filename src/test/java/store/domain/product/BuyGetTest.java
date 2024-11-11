package store.domain.product;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class BuyGetTest {
    @DisplayName("주어진 수량에 대해 프로모션을 적용하기 위해 필요한 수량을 계산하여 반환한다.")
    @ParameterizedTest
    @CsvSource(value = {"5:2:1:6", "7:3:1:8", "2:1:1:2", "3:1:1:4"}, delimiter = ':')
    void calculateQuantityCondition(final int quantity, final int buy, final int get, final int expected) {
        BuyGet buyGet = BuyGet.of(buy, get);

        int actual = buyGet.calculateRequiredQuantityForApplyPromotion(quantity);

        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("주어진 재고에 대해 프로모션을 적용 가능한 수량을 계산하여 반환한다.")
    @ParameterizedTest
    @CsvSource(value = {"10:2:1:9", "7:3:1:4", "2:1:1:2", "3:1:1:2", "7:2:1:6"}, delimiter = ':')
    void calculateApplicableQuantity(final int quantity, final int buy, final int get, final int expected) {
        BuyGet buyGet = BuyGet.of(buy, get);

        int actual = buyGet.calculateApplicableQuantity(quantity);

        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("주어진 재고에 대해 적용 가능한 무료 수량을 계산하여 반환한다.")
    @ParameterizedTest
    @CsvSource(value = {"5:2:1:1", "7:3:1:1", "2:1:1:1", "3:1:1:1", "7:2:1:2"}, delimiter = ':')
    void calculateApplicableFreeQuantity(final int quantity, final int buy, final int get, final int expected) {
        BuyGet buyGet = BuyGet.of(buy, get);

        int actual = buyGet.calculateApplicableFreeQuantity(quantity);

        assertThat(actual).isEqualTo(expected);
    }

}
