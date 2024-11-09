package store.component.generator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import store.domain.product.BuyGet;
import store.domain.product.DateRange;
import store.domain.product.ProductInfo;
import store.domain.ProductInventory;
import store.domain.product.ProductQuantity;
import store.domain.product.Promotion;
import store.domain.product.PromotionProductQuantity;
import store.enums.ErrorMessage;

public class ProductInventoryGeneratorTest {

    private final ProductInventoryGenerator productInventoryGenerator;
    private final List<ProductQuantity> quantities;
    private final List<PromotionProductQuantity> promotionQuantities;
    private final List<ProductInfo> infos;

    public ProductInventoryGeneratorTest() {
        productInventoryGenerator = new ProductInventoryGenerator();
        quantities = new ArrayList<>();
        promotionQuantities = new ArrayList<>();
        infos = new ArrayList<>();
    }

    private static Stream<Arguments> provideProductLineAndPromotionsAndProductData() {
        Promotion promotion = makePromotion("탄산2+1");
        Map<String, Optional<Promotion>> promotions =
                Map.of("탄산2+1", Optional.of(promotion), "null", Optional.empty());
        return Stream.of(
                Arguments.of(List.of("콜라,1000,10,탄산2+1"), promotions, "콜라", 10, promotion),
                Arguments.of(List.of("환타,2000,20,탄산2+1"), promotions, "환타", 20, promotion),
                Arguments.of(List.of("사이다,3000,30,탄산2+1"), promotions, "사이다", 30, promotion),
                Arguments.of(List.of("밀키스,4000,40,탄산2+1"), promotions, "밀키스", 40, promotion)
        );
    }

    private static Promotion makePromotion(final String name) {
        DateRange dateRange = DateRange.of(LocalDate.parse("2023-11-01"), LocalDate.parse("2023-12-01"));
        return Promotion.of(name, BuyGet.of(1, 1), dateRange);
    }

    @BeforeEach
    void init() {
        quantities.clear();
        promotionQuantities.clear();
        infos.clear();
    }

    @ParameterizedTest
    @ValueSource(strings = {"파격할인", "바겐세일", "초특가", "콜라1+1"})
    void 존재하지_않는_Promotion을_가진_경우_예외가_발생한다(String promotionName) {
        String input = String.format("콜라,1000,10,%s", promotionName);

        assertThatException(input);
    }

    @ParameterizedTest
    @ValueSource(strings = {"10000000000", "100000000000", "1000000000000", "2147483648"})
    void 상품_가격이_int의_범위를_벗어난_경우_예외가_발생한다(String productPrice) {
        String input = String.format("콜라,%s,10,null", productPrice);

        assertThatException(input);
    }

    @ParameterizedTest
    @ValueSource(strings = {"10000000000", "100000000000", "1000000000000", "2147483648"})
    void 재고_수량이_int의_범위를_벗어난_경우_예외가_발생한다(String productQuantity) {
        String input = String.format("콜라,1000,%s,null", productQuantity);

        assertThatException(input);
    }

    private void assertThatException(String input) {
        assertThatThrownBy(
                () -> productInventoryGenerator.generate(Map.of("null", Optional.empty()), List.of(input)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ErrorMessage.INVALID_FILE_FORMAT.getMessage());
    }


    @ParameterizedTest
    @ValueSource(strings = {"탄산음료", "김치", "감자", "삼겹살", "젤리"})
    void 일반_상품을_생성하면_일반_상품_재고만_생성되고_프로모션_재고는_생성되지_않는다(String name) {
        String input = String.format("%s,1000,10,%s", name, "null");
        quantities.add(ProductQuantity.of(name, 10));

        ProductInventory productInventory =
                productInventoryGenerator.generate(Map.of("null", Optional.empty()), List.of(input));

        assertThatQuantities(productInventory);
    }

    @ParameterizedTest
    @MethodSource("provideProductLineAndPromotionsAndProductData")
    void 프로모션_상품만_입력하는_경우_일반_상품_재고0의_상품을_생성한다(final List<String> productLines,
                                              final Map<String, Optional<Promotion>> promotions,
                                              final String productName, final int productQuantity,
                                              final Promotion promotion) {
        quantities.add(ProductQuantity.of(productName, 0));
        promotionQuantities.add(PromotionProductQuantity.of(productName, productQuantity, promotion));

        ProductInventory productInventory = productInventoryGenerator.generate(promotions, productLines);

        assertThatQuantities(productInventory);
    }

    @ParameterizedTest
    @CsvSource(value = {"탄산음료:100000", "김치:1000", "감자:1000000000", "삼겹살:100"}, delimiter = ':')
    void 상품인벤토리를_생성하면서_상품의_정보도_생성한다(String name, int price) {
        String input = String.format("%s,%d,10,null", name, price);
        infos.add(ProductInfo.of(name, price));

        ProductInventory productInventory =
                productInventoryGenerator.generate(Map.of("null", Optional.empty()), List.of(input));

        assertThat(productInventory.getInfos())
                .usingRecursiveComparison()
                .isEqualTo(infos);
    }

    private void assertThatQuantities(ProductInventory actual) {
        assertThat(actual.getQuantities())
                .usingRecursiveComparison()
                .isEqualTo(quantities);
        assertThat(actual.getPromotionQuantities())
                .usingRecursiveComparison()
                .isEqualTo(promotionQuantities);
    }


}
