package store.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class ProductQuantityTest {


    @DisplayName("현재 재고보다 작거나 같은지 여부를 검사하는 메서드 테스트")
    @ParameterizedTest
    @CsvSource(value = {"1:1:true", "2:1:true", "1:2:false", "0:1:false"}, delimiter = ':')
    void isLessThanQuantityTest(int currentQuantity, int quantity, boolean expected) {
        ProductQuantity productQuantity = ProductQuantity.from(currentQuantity);

        assertThat(productQuantity.isLessThanQuantity(quantity)).isEqualTo(expected);
    }

    @DisplayName("재고를 차감하는 메서드 테스트")
    @ParameterizedTest
    @CsvSource(value = {"10:1:9", "12:1:11", "10:9:1", "832:172:660"}, delimiter = ':')
    void deductQuantity(final int currentQuantity, final int quantity, final int expected) {
        ProductQuantity productQuantity = ProductQuantity.from(currentQuantity);
        ProductQuantity expectedQuantity = ProductQuantity.from(expected);

        productQuantity.deductQuantity(quantity);

        assertThat(productQuantity).usingRecursiveComparison().isEqualTo(expectedQuantity);
    }
}
