package store.component;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import store.domain.BuyGet;
import store.domain.DateRange;
import store.domain.Promotion;
import store.enums.ErrorMessage;

public class PromotionGeneratorTest {
    private final PromotionGenerator promotionGenerator;

    public PromotionGeneratorTest() {
        this.promotionGenerator = new PromotionGenerator();
    }

    private static Stream<Arguments> proviedeReadLinesAndPromotions() {
        return Stream.of(
                Arguments.of(List.of("탄산2+1,2,1,2024-01-01,2024-12-31"),
                        Promotion.of("탄산2+1", BuyGet.of(2, 1),
                                DateRange.of(LocalDate.parse("2024-01-01"), LocalDate.parse("2024-12-31"))), "탄산2+1"),
                Arguments.of(List.of("MD추천상품,1,1,2024-01-01,2024-12-31"),
                        Promotion.of("MD추천상품", BuyGet.of(1, 1),
                                DateRange.of(LocalDate.parse("2024-01-01"), LocalDate.parse("2024-12-31"))), "MD추천상품"),
                Arguments.of(List.of("반짝할인,1,1,2024-11-01,2024-11-30"),
                        Promotion.of("반짝할인", BuyGet.of(1, 1),
                                DateRange.of(LocalDate.parse("2024-11-01"), LocalDate.parse("2024-11-30"))), "반짝할인")
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {"행사,1,1,2024-01-99,2024-12-31, 행사,1,1,2024-01-01,2024-30-31",
            "행사,1,1,2024-01-01,2024-12-99"})
    void 날짜_형식이_잘못된_경우_예외가_발생한다(String input) {
        assertThatThrownBy(() -> promotionGenerator.generate(List.of(input)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ErrorMessage.INVALID_FILE_FORMAT.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"행사,1,1", "행사,1,2024-01-01,2024-12-31", "행사,2024-01-01,2024-12-31",
            "행사,1,1,2024-01-01", "1,1,2024-01-01,2024-12-31"})
    void 프로모션_형식이_잘못된_경우_예외가_발생한다(String input) {
        assertThatThrownBy(() -> promotionGenerator.generate(List.of(input)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ErrorMessage.INVALID_FILE_FORMAT.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"10000000000", "100000000000", "1000000000000", "-1000000000000"})
    void 프로모션_수량이_int의_범위를_벗어난_경우_예외가_발생한다(String input) {
        String format = "탄산2+1,%s,1,2024-01-01,2024-12-31";

        assertThatThrownBy(() -> promotionGenerator.generate(List.of(String.format(format, input))))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ErrorMessage.INVALID_FILE_FORMAT.getMessage());
    }

    @DisplayName("올바른 형식으로 작성된 파일이라면, 예외가 발생하지 않는다.")
    @ParameterizedTest
    @MethodSource("proviedeReadLinesAndPromotions")
    void generate(List<String> readLines, Promotion expectedPromotion, String promotionName) {

        Map<String, Optional<Promotion>> promotions = promotionGenerator.generate(readLines);
        Promotion actualPromotion = promotions.get(promotionName).get();

        assertThat(actualPromotion)
                .usingRecursiveComparison()
                .isEqualTo(expectedPromotion);
    }


}
