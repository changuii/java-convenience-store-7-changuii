package store.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.component.LocalDateGenerator;
import store.domain.BuyGet;
import store.domain.DateRange;
import store.domain.ProductInfo;
import store.domain.ProductInventory;
import store.domain.ProductQuantity;
import store.domain.Promotion;
import store.domain.PromotionProductQuantity;
import store.dto.PurchaseProductDTO;
import store.enums.ErrorMessage;

public class ConvenienceStoreServiceTest {

    private ConvenienceStoreService convenienceStoreService;
    private MockTodayGenerator mockTodayGenerator;
    private Map<String, ProductInfo> infos;
    private Map<String, ProductQuantity> quantities;
    private Map<String, PromotionProductQuantity> promotionQuantities;

    @BeforeEach
    void init() {
        LocalDate start = LocalDate.of(2023, 11, 01);
        LocalDate end = LocalDate.of(2023, 11, 30);
        Promotion promotion = Promotion.of("MOCK_PROMOTION", BuyGet.of(1, 1), DateRange.of(start, end));
        infos = Map.of("MOCK", ProductInfo.of("MOCK", 1000));
        quantities = Map.of("MOCK", ProductQuantity.from(5));
        promotionQuantities = Map.of("MOCK", PromotionProductQuantity.of(10, promotion));
        ProductInventory inventory = ProductInventory.of(infos, quantities, promotionQuantities);
        mockTodayGenerator = new MockTodayGenerator();
        convenienceStoreService = ConvenienceStoreService.of(inventory, mockTodayGenerator);
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

    @DisplayName("현재 날짜가 포함되지 않아서 수량이 부족한 것을 보여주는 테스트")
    @Test
    void 프로모션중인_상품은_현재_날짜가_진행중인_날짜일때만_수량을_적용한다() {
        List<PurchaseProductDTO> purchaseProductDTOs = List.of(PurchaseProductDTO.of("MOCK", 15));
        mockTodayGenerator.setToday(LocalDate.of(2023, 10, 31));

        assertThatException(convenienceStoreService::validatePurchaseProducts, purchaseProductDTOs,
                ErrorMessage.MORE_THAN_PURCHASE_PRODUCT_QUANTITY);
    }

    @DisplayName("현재 날짜가 포함되어 수량을 더해 충분한 수량임을 보여주는 테스트")
    @Test
    void 프로모션중인_상품은_현재_날짜가_진행중인_날짜인경우_수량을_적용한다() {
        List<PurchaseProductDTO> purchaseProductDTOs = List.of(PurchaseProductDTO.of("MOCK", 15));
        mockTodayGenerator.setToday(LocalDate.of(2023, 11, 01));

        convenienceStoreService.validatePurchaseProducts(purchaseProductDTOs);
    }

    @Test
    void ProductInventory에_존재하고_수량이_충분한_상품은_예외가_발생하지_않는다() {
        List<PurchaseProductDTO> purchaseProductDTOs = List.of(PurchaseProductDTO.of("MOCK", 1));

        convenienceStoreService.validatePurchaseProducts(purchaseProductDTOs);
    }

    private <T> void assertThatException(Consumer<T> logic, T data, ErrorMessage errorMessage) {
        assertThatThrownBy(() -> logic.accept(data))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(errorMessage.getMessage());
    }


    private class MockTodayGenerator implements LocalDateGenerator {
        private LocalDate today;

        public MockTodayGenerator() {
            today = LocalDate.now();
        }

        @Override
        public LocalDate generate() {
            return today;
        }

        public void setToday(LocalDate today) {
            this.today = today;
        }
    }
}
