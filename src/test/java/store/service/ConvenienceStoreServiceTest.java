package store.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.util.Map;
import java.util.function.Consumer;
import org.junit.jupiter.api.BeforeEach;
import store.component.generator.TodayGenerator;
import store.domain.product.BuyGet;
import store.domain.product.DateRange;
import store.domain.product.ProductInfo;
import store.domain.ProductInventory;
import store.domain.product.ProductQuantity;
import store.domain.product.Promotion;
import store.domain.product.PromotionProductQuantity;
import store.enums.ErrorMessage;

public class ConvenienceStoreServiceTest {

    private static final String PRODUCT_NAME = "MOCK";
    private static final int PRODUCT_PRICE = 1000;
    private ConvenienceStoreService convenienceStoreService;
    private Map<String, ProductInfo> infos;
    private Map<String, ProductQuantity> quantities;
    private Map<String, PromotionProductQuantity> promotionQuantities;

    @BeforeEach
    void init() {
        LocalDate start = LocalDate.of(2024, 11, 01);
        LocalDate end = LocalDate.of(2024, 11, 30);
        Promotion promotion = Promotion.of("MOCK_PROMOTION", BuyGet.of(1, 1), DateRange.of(start, end));
        infos = Map.of(PRODUCT_NAME, ProductInfo.of(PRODUCT_NAME, PRODUCT_PRICE));
        quantities = Map.of(PRODUCT_NAME, ProductQuantity.from(5));
        promotionQuantities = Map.of(PRODUCT_NAME, PromotionProductQuantity.of(10, promotion));
        ProductInventory inventory = ProductInventory.of(infos, quantities, promotionQuantities);
        convenienceStoreService = ConvenienceStoreService.of(inventory, new TodayGenerator());
    }

    private <T> void assertThatException(Consumer<T> logic, T data, ErrorMessage errorMessage) {
        assertThatThrownBy(() -> logic.accept(data))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(errorMessage.getMessage());
    }

}
