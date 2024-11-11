package store.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import store.Constants;
import store.component.generator.TodayGenerator;
import store.domain.Consumer;
import store.domain.PurchaseProduct;
import store.domain.product.BuyGet;
import store.domain.product.DateRange;
import store.domain.product.ProductInfo;
import store.domain.ProductInventory;
import store.domain.product.ProductQuantity;
import store.domain.product.Promotion;
import store.domain.product.PromotionProductQuantity;

public class ConvenienceStoreServiceTest {
    private ConvenienceStoreService convenienceStoreService;
    private List<ProductInfo> infos;
    private List<ProductQuantity> quantities;
    private List<PromotionProductQuantity> promotionQuantities;

    @BeforeEach
    void init() {
        LocalDate start = LocalDate.of(2024, 11, 01);
        LocalDate end = LocalDate.of(2024, 11, 30);
        Promotion promotion = Promotion.of("MOCK_PROMOTION", BuyGet.of(2, 1), DateRange.of(start, end));
        infos = List.of(ProductInfo.of(Constants.PRODUCT_NAME, Constants.PRODUCT_PRICE),
                ProductInfo.of("김밥", Constants.PRODUCT_PRICE));
        quantities = List.of(ProductQuantity.of(Constants.PRODUCT_NAME, 5), ProductQuantity.of("김밥", 100));
        promotionQuantities = List.of(PromotionProductQuantity.of(Constants.PRODUCT_NAME, 10, promotion));
        ProductInventory inventory = ProductInventory.of(infos, quantities, promotionQuantities);
        convenienceStoreService = ConvenienceStoreService.of(inventory, new TodayGenerator());
    }

    @DisplayName("고객이 구매하려는 현재 상품이 프로모션 진행중이라면 ture, 아니라면 false를 반환한다.")
    @ParameterizedTest
    @CsvSource(value = {"PRODUCT:true", "김밥:false"}, delimiter = ':')
    void isPromotionInProgress(final String productName, final boolean expected) {
        List<PurchaseProduct> purchaseProducts = List.of(PurchaseProduct.of(productName, 100));
        Consumer consumer = Consumer.from(purchaseProducts);

        boolean actual = convenienceStoreService.isPromotionInProgress(consumer);

        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("고객이 현재 결제하고자 하는 상품의 프로모션 재고가 충분하다면 true, 아니라면 false를 반환한다.")
    @ParameterizedTest
    @CsvSource(value = {"3:true", "5:true", "6:true", "9:true", "12:false", "11:false"}, delimiter = ':')
    void isInventoryQuantityRequirementMetForApplyPromotion(final int quantity, final boolean expected){
        List<PurchaseProduct> purchaseProducts = List.of(PurchaseProduct.of(Constants.PRODUCT_NAME, quantity));
        Consumer consumer = Consumer.from(purchaseProducts);

        boolean actual = convenienceStoreService.isInventoryQuantityRequirementMetForApplyPromotion(consumer);

        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("고객이 현재 결제하고자 하는 상품이 프로모션 혜택을 못받고 정가로 구매해야하는 상품의 수를 반환한다.")
    @ParameterizedTest
    @CsvSource(value = {"9:0", "10:1", "11:2", "12:3", "15:6"}, delimiter = ':')
    void getQuantityAtRegularPrice(final int quantity, final int expected){
        List<PurchaseProduct> purchaseProducts = List.of(PurchaseProduct.of(Constants.PRODUCT_NAME, quantity));
        Consumer consumer = Consumer.from(purchaseProducts);

        int actual = convenienceStoreService.getQuantityAtRegularPrice(consumer);

        assertThat(actual).isEqualTo(expected);
    }

}
