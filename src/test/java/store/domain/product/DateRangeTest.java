package store.domain.product;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import store.domain.product.DateRange;

public class DateRangeTest {


    @DisplayName("현재 날짜가 startDate 이상, endDate 이하라면, True 아니라면 False를 반환한다.")
    @ParameterizedTest
    @CsvSource(value = {"2023-11-01:true", "2023-11-30:true", "2023-10-31:false", "2023-12-01:false"}, delimiter = ':')
    void isInRangeNow(LocalDate today, boolean expected){
        LocalDate start = LocalDate.of(2023, 11, 01);
        LocalDate end = LocalDate.of(2023, 11, 30);
        DateRange dateRange = DateRange.of(start, end);

        assertThat(dateRange.isInRangeNow(today)).isEqualTo(expected);
    }
}
