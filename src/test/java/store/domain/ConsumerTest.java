package store.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import store.Constants;

public class ConsumerTest {



    @DisplayName("소비자가 현재 구매 상품을 구매 완료하였다면, true 아니라면 false를 반환한다.")
    @ParameterizedTest
    @CsvSource(value = {"0:true", "1:false", "2:false", "10:false"}, delimiter = ':')
    void isCurrentPurchaseProductDone(int quantity, boolean expected){
        Consumer consumer = getConsumer(quantity);

        boolean actual = consumer.isCurrentPurchaseProductDone();

        assertThat(actual).isEqualTo(expected);
    }


    private Consumer getConsumer(int quantity){
        return Consumer.from(List.of(PurchaseProduct.of(Constants.PRODUCT_NAME, quantity)));
    }

}
