package store.component.generator;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import store.component.generator.TodayGenerator;

public class TodayGeneratorTest {

    private final TodayGenerator todayGenerator;

    public TodayGeneratorTest(){
        todayGenerator = new TodayGenerator();
    }

    @Test
    void 오늘_날짜를_정확히_반환하는지_테스트(){
        LocalDate actualToday = todayGenerator.generate();

        assertThat(actualToday).isToday();
    }
}
