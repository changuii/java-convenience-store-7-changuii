package store.component.generator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.component.FileParser;
import store.domain.Consumer;
import store.domain.PurchaseProduct;
import store.enums.ErrorMessage;
import store.service.ConvenienceStoreService;

public class ConsumerGeneratorTest {

    private final ConsumerGenerator consumerGenerator;

    public ConsumerGeneratorTest() {
        ConvenienceStoreService convenienceStoreService = new ConvenienceStoreServiceGnerator(new FileParser(),
                new PromotionGenerator(),
                new ProductInventoryGenerator()).generate();
        this.consumerGenerator = new ConsumerGenerator(convenienceStoreService.getProductInventory(),
                new TodayGenerator());
    }

    @DisplayName("ProductInventory에 존재하지 않는 상품이 입력되면, 예외가 발생한다.")
    @Test
    void 존재하지_않는_상품_예외_테스트() {
        List<PurchaseProduct> purchaseProducts = List.of(PurchaseProduct.of("김밥", 1));

        assertThatThrownBy(() -> consumerGenerator.generate(purchaseProducts))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ErrorMessage.NOT_EXISTS_PRODUCT.getMessage());
    }

    @DisplayName("ProductInventory에 수량보다 큰 수량이 들어온 경우, 예외가 발생한다.")
    @Test
    void 수량_초과_예외_테스트() {
        List<PurchaseProduct> purchaseProducts = List.of(PurchaseProduct.of("콜라", 21));

        assertThatThrownBy(() -> consumerGenerator.generate(purchaseProducts))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ErrorMessage.MORE_THAN_PURCHASE_PRODUCT_QUANTITY.getMessage());
    }

    @Test
    void 생성_테스트() {
        List<PurchaseProduct> purchaseProducts = List.of(PurchaseProduct.of("콜라", 20));
        Consumer expected = Consumer.from(purchaseProducts);

        Consumer actual = consumerGenerator.generate(purchaseProducts);

        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

}
