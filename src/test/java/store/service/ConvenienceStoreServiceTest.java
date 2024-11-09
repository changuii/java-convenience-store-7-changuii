package store.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import store.component.TodayGenerator;
import store.domain.BuyGet;
import store.domain.DateRange;
import store.domain.ProductInfo;
import store.domain.ProductInventory;
import store.domain.ProductQuantity;
import store.domain.Promotion;
import store.domain.PromotionProductQuantity;
import store.domain.PurchaseProduct;
import store.dto.PurchaseProductDTO;
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

    @Test
    void ProductInventory에_존재하지_않는_상품은_예외가_발생한다() {
        List<PurchaseProductDTO> purchaseProductDTOs = List.of(PurchaseProductDTO.of("존재하지않는상품", 1));

        assertThatException(convenienceStoreService::validatePurchaseProducts, purchaseProductDTOs,
                ErrorMessage.NOT_EXISTS_PRODUCT_NAME);
    }

    @Test
    void ProductInventory에_물건의_수량이_구매수량보다_작다면_예외가_발생한다() {
        List<PurchaseProductDTO> purchaseProductDTOs = List.of(PurchaseProductDTO.of("MOCK", 100));

        assertThatException(convenienceStoreService::validatePurchaseProducts, purchaseProductDTOs,
                ErrorMessage.MORE_THAN_PURCHASE_PRODUCT_QUANTITY);
    }

    @Test
    void ProductInventory에_존재하고_수량이_충분한_상품은_예외가_발생하지_않는다() {
        List<PurchaseProductDTO> purchaseProductDTOs = List.of(PurchaseProductDTO.of("MOCK", 1));

        convenienceStoreService.validatePurchaseProducts(purchaseProductDTOs);
    }

    @DisplayName("purchaseProductDTO의 상품이 프로모션 중이라면 true, 아니라면 false를 반환한다.")
    @ParameterizedTest
    @CsvSource(value = {"MOCK:true", "오렌지주스:false", "김치:false", "오레오:false"}, delimiter = ':')
    void isPromotionProduct(final String productName, final boolean expected) {
        PurchaseProductDTO purchaseProductDTO = PurchaseProductDTO.of(productName, 5);

        assertThat(convenienceStoreService.isPromotionProduct(purchaseProductDTO)).isEqualTo(expected);
    }

    @DisplayName("purchaseProductDTO 상품을 수량만큼 구매하고, 결과를 반환한다.")
    @ParameterizedTest
    @CsvSource(value = {"1:1000", "2:2000", "3:3000", "4:4000", "5:5000"}, delimiter = ':')
    void purchaseProduct(final int purchaseQuantity, final int expectedPrice) {
        PurchaseProductDTO purchaseProductDTO = PurchaseProductDTO.of(PRODUCT_NAME, purchaseQuantity);
        PurchaseProduct expected = PurchaseProduct.of(PRODUCT_NAME, expectedPrice, purchaseQuantity, 0, 0);

        PurchaseProduct actual = convenienceStoreService.purchaseProduct(purchaseProductDTO);

        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    private <T> void assertThatException(Consumer<T> logic, T data, ErrorMessage errorMessage) {
        assertThatThrownBy(() -> logic.accept(data))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(errorMessage.getMessage());
    }

}
