package store.component;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import store.domain.DateRange;
import store.domain.Product;
import store.domain.Promotion;
import store.enums.ErrorMessage;

public class ProductGeneratorTest {

    private final ProductGenerator productGenerator;
    private Map<String, Product> basicProducts;
    private Map<String, Product> promotionProducts;

    public ProductGeneratorTest() {
        productGenerator = new ProductGenerator();
    }

    @BeforeEach
    void init() {
        basicProducts = new LinkedHashMap<>();
        promotionProducts = new LinkedHashMap<>();
    }

    @ParameterizedTest
    @ValueSource(strings = {"파격할인", "바겐세일", "초특가", "콜라1+1"})
    void 존재하지_않는_Promotion을_가진_경우_예외가_발생한다(String promotionName){
        String input = String.format("콜라,1000,10,%s", promotionName);

        assertThatThrownBy(
                () -> productGenerator.generate(Map.of("null", Optional.empty()), basicProducts, promotionProducts,
                        List.of(input)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ErrorMessage.INVALID_FILE_FORMAT.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"10000000000", "100000000000", "1000000000000", "2147483648"})
    void 상품_가격이_int의_범위를_벗어난_경우_예외가_발생한다(String productPrice) {
        String input = String.format("콜라,%s,10,null", productPrice);

        assertThatThrownBy(
                () -> productGenerator.generate(Map.of("null", Optional.empty()), basicProducts, promotionProducts,
                        List.of(input)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ErrorMessage.INVALID_FILE_FORMAT.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"10000000000", "100000000000", "1000000000000", "2147483648"})
    void 재고_수량이_int의_범위를_벗어난_경우_예외가_발생한다(String productQuantity) {
        String input = String.format("콜라,1000,%s,null", productQuantity);

        assertThatThrownBy(
                () -> productGenerator.generate(Map.of("null", Optional.empty()), basicProducts, promotionProducts,
                        List.of(input)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ErrorMessage.INVALID_FILE_FORMAT.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"탄산음료", "김치", "감자", "삼겹살", "젤리"})
    void 상품_생성_테스트(String name) {
        Optional<Promotion> promotion = Optional.empty();
        Product product = Product.of(name, 1000, 10, promotion);
        String input = String.format("%s,1000,10,%s", name, "null");

        productGenerator.generate(Map.of("null", promotion), basicProducts, promotionProducts, List.of(input));

        assertThat(basicProducts.get(name))
                .usingRecursiveComparison()
                .isEqualTo(product);
        assertThat(promotionProducts)
                .isEmpty();

    }

    @ParameterizedTest
    @MethodSource("provideProductLineAndPromotionsAndProductData")
    void 프로모션_상품만_입력하는_경우_일반_상품_재고0의_상품을_생성한다(final List<String> productLines,
                                              final Map<String, Optional<Promotion>> promotions, String productName,
                                              int productPrice, int productQuantity, Optional<Promotion> promotion) {
        Product promotionProduct = Product.of(productName, productPrice, productQuantity, promotion);
        Product basicProduct = Product.of(productName, productPrice, 0, Optional.empty());

        productGenerator.generate(promotions, basicProducts, promotionProducts, productLines);

        assertThat(basicProducts.get(productName)).usingRecursiveComparison().isEqualTo(basicProduct);
        assertThat(promotionProducts.get(productName)).usingRecursiveComparison().isEqualTo(promotionProduct);
    }

    private static Stream<Arguments> provideProductLineAndPromotionsAndProductData() {
        Optional<Promotion> promotion = makePromotion("탄산2+1");
        Map<String, Optional<Promotion>> promotions = Map.of(
                "탄산2+1", promotion, "null", Optional.empty());
        return Stream.of(
                Arguments.of(List.of("콜라,1000,10,탄산2+1"), promotions, "콜라", 1000, 10, promotion),
                Arguments.of(List.of("환타,2000,20,탄산2+1"), promotions, "환타", 2000, 20, promotion),
                Arguments.of(List.of("사이다,3000,30,탄산2+1"), promotions, "사이다", 3000, 30, promotion),
                Arguments.of(List.of("밀키스,4000,40,탄산2+1"), promotions, "밀키스", 4000, 40, promotion)
        );
    }

    private static Optional<Promotion> makePromotion(final String name) {
        DateRange dateRange = DateRange.of(LocalDate.parse("2023-11-01"), LocalDate.parse("2023-12-01"));
        return Optional.of(Promotion.of(name, 10, dateRange));
    }


}
