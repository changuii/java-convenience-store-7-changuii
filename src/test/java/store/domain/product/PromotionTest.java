package store.domain.product;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import store.domain.product.BuyGet;
import store.domain.product.DateRange;
import store.domain.product.Promotion;

public class PromotionTest {

    @DisplayName("매개변수로 들어온 LocalDate가 DateRange 범위 이내라면 true 아니라면 false를 반환한다.")
    @ParameterizedTest
    @CsvSource(value = {"2023-11-01:true", "2023-11-30:true", "2023-10-31:false", "2023-12-01:false"}, delimiter = ':')
    void isValidNow(LocalDate today, boolean expected) {
        DateRange dateRange = DateRange.of(LocalDate.of(2023, 11, 01), LocalDate.of(2023, 11, 30));
        Promotion promotion = Promotion.of("1+1", BuyGet.of(1,1), dateRange);

        assertThat(promotion.isValidNow(today)).isEqualTo(expected);
    }
}
