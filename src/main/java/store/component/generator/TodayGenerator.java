package store.component.generator;

import camp.nextstep.edu.missionutils.DateTimes;
import java.time.LocalDate;

public class TodayGenerator implements LocalDateGenerator {
    @Override
    public LocalDate generate() {
        return DateTimes.now().toLocalDate();
    }
}
