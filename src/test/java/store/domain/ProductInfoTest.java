package store.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class ProductInfoTest {



    @ParameterizedTest
    @CsvSource(value = {"1000:10:10000", "500:3:1500", "300:15:4500", "17500:7:122500"}, delimiter = ':')
    void 수량에_대한_가격을_계산하여_반환한다(int productPrice, int quantity, int expected){
        ProductInfo productInfo = ProductInfo.of("", productPrice);

        int actual = productInfo.calculateTotalPrice(quantity);

        assertThat(actual).isEqualTo(expected);
    }


}
