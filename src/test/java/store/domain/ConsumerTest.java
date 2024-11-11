package store.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import store.Constants;
import store.domain.product.BuyGet;
import store.domain.product.DateRange;
import store.domain.product.Product;
import store.domain.product.ProductQuantity;
import store.domain.product.Promotion;
import store.domain.product.PromotionProductQuantity;

public class ConsumerTest {
    private ProductInventory productInventory;

    @BeforeEach
    void init() {
        LocalDate start = LocalDate.of(2023, 11, 01);
        LocalDate end = LocalDate.of(2023, 11, 30);
        Promotion promotion = Promotion.of(Constants.PROMOTION_NAME, BuyGet.of(2, 1), DateRange.of(start, end));
        List<Product> products = List.of(Product.of(Constants.PRODUCT_NAME, 1000), Product.of("김밥", 1000));
        List<ProductQuantity> quantities = List.of(ProductQuantity.of(Constants.PRODUCT_NAME, 5),
                ProductQuantity.of("김밥", 5));
        List<PromotionProductQuantity> promotionQuantity = List.of(
                PromotionProductQuantity.of(Constants.PRODUCT_NAME, 10, promotion));
        productInventory = ProductInventory.of(products, quantities, promotionQuantity);
    }

    @DisplayName("소비자가 현재 구매 상품을 구매 완료하였다면, true 아니라면 false를 반환한다.")
    @ParameterizedTest
    @CsvSource(value = {"0:true", "1:false", "2:false", "10:false"}, delimiter = ':')
    void isCurrentPurchaseProductDone(int quantity, boolean expected) {
        Consumer consumer = getConsumer(quantity);

        boolean actual = consumer.isCurrentPurchaseProductDone();

        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("현재 구매 물품이 프로모션 진행중이라면, true 아니라면 false를 반환한다.")
    @ParameterizedTest
    @CsvSource(value = {"PRODUCT:true", "김밥:false"}, delimiter = ':')
    void isCurrentProductPromotionInProgress(final String productName, final boolean expected) {
        Consumer consumer = getConsumer(productName);

        boolean actual = consumer.isCurrentProductPromotionInProgress(productInventory, LocalDate.of(2023, 11, 1));

        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("현재 구매 물품의 프로모션 수량이 프로모션 혜택을 적용받을 수 있다면 true, 아니라면 false를 반환한다.")
    @ParameterizedTest
    @CsvSource(value = {"3:true", "5:true", "9:true", "12:false"}, delimiter = ':')
    void isInventoryQuantityRequirementMetForApplyPromotion(final int quantity, final boolean expected) {
        Consumer consumer = getConsumer(quantity);

        boolean actual = consumer.isInventoryQuantityRequirementMetForApplyPromotion(productInventory);

        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("현재 상품이 프로모션 혜택을 충분히 받을 수 있는 수량이라면, true 아니라면 false를 반환한다.")
    @ParameterizedTest
    @CsvSource(value = {"2:false", "3:true", "5:false", "6:true", "8:false", "9:true"}, delimiter = ':')
    void isCurrentProductQuantitySufficientForApplyPromotion(final int quantity, final boolean expected) {
        Consumer consumer = getConsumer(quantity);

        boolean actual = consumer.isCurrentProductQuantitySufficientForApplyPromotion(productInventory);

        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("프로모션을 적용하기 위해 현재 상품의 수량을 1만큼 추가한다.")
    @ParameterizedTest
    @CsvSource(value = {"1:2", "2:3", "3:4"}, delimiter = ':')
    void additionCurrentProductQuantityForApplyPromotion(final int quantity, final int expected) {
        Consumer consumer = getConsumer(quantity);
        Consumer expectedConsumer = getConsumer(expected);

        consumer.additionCurrentProductQuantityForApplyPromotion();

        assertThat(consumer).usingRecursiveComparison().isEqualTo(expectedConsumer);
    }

    @DisplayName("현재 상품 수량 중, 정가로 구매해야하는 수량을 반환한다.")
    @ParameterizedTest
    @CsvSource(value = {"12:3", "13:4", "10:1", "9:0"}, delimiter = ':')
    void calculateCurrentProductQuantityAtRegularPrice(final int quantity, final int expected) {
        Consumer consumer = getConsumer(quantity);

        int actual = consumer.calculateCurrentProductQuantityAtRegularPrice(productInventory);

        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("현재 상품 수량에서 정가로 구매하는 수량을 제거한다.")
    @Test
    void deductCurrentProductQuantityAtRegularPrice() {
        Consumer consumer = getConsumer(12);
        Consumer expected = getConsumer(9);

        consumer.deductCurrentProductQuantityAtRegularPrice(3);

        assertThat(consumer).usingRecursiveComparison().isEqualTo(expected);
    }

    @DisplayName("현재 상품을 구매한다.")
    @Test
    void purchaseProduct() {
        Consumer consumer = getConsumer("김밥");

        consumer.purchaseProduct(productInventory);
        consumer.nextPurchaseProduct();

        assertThat(consumer).extracting("purchaseProducts").usingRecursiveComparison()
                .isEqualTo(new ArrayList<PurchaseProduct>());
    }

    @DisplayName("현재 상품을 프로모션 혜택을 적용하여 구매한다.")
    @Test
    void purchasePromotionProduct() {
        Consumer consumer = getConsumer(15);

        consumer.purchasePromotionProduct(productInventory);
        consumer.nextPurchaseProduct();

        assertThat(consumer).extracting("purchaseProducts").usingRecursiveComparison()
                .isEqualTo(new ArrayList<PurchaseProduct>());
    }

    @DisplayName("현재 상품을 프로모션 혜택없이 프로모션 재고를 구입한다.")
    @Test
    void purchaseRegularPricePromotionProduct() {
        Consumer consumer = getConsumer(10);

        consumer.purchaseRegularPricePromotionProduct(productInventory);
        consumer.nextPurchaseProduct();

        assertThat(consumer).extracting("purchaseProducts").usingRecursiveComparison()
                .isEqualTo(new ArrayList<PurchaseProduct>());
    }

    @DisplayName("현재 상품이 구매가 모두 완료되었다면, 해당 값으로 CompletedPurchaseHistory를 만들어 리스트에 넣는다.")
    @Test
    void addIfCurrentPurchaseProductComplete() {
        Consumer consumer = getConsumer(5);
        CompletedPurchaseHistory purchaseHistory = CompletedPurchaseHistory.of(Constants.PRODUCT_NAME, 5000, 5, 0, 0);

        consumer.purchaseProduct(productInventory);

        assertThat(consumer).extracting("completedPurchaseHistories").usingRecursiveComparison()
                .isEqualTo(new ArrayList<>(List.of(purchaseHistory)));
    }

    @DisplayName("현재 PurchaseProduct를 삭제한다.")
    @Test
    void nextPurchaseProduct() {
        Consumer consumer = getConsumer(5);
        Consumer expected = Consumer.from(List.of());

        consumer.nextPurchaseProduct();

        assertThat(consumer).usingRecursiveComparison().isEqualTo(expected);
    }

    @DisplayName("구매한 내역을 바탕으로 청구서를 생성한다.")
    @Test
    void generateBill() {
        Consumer consumer = getConsumer(5);
        Bill expected = Bill.from(List.of(CompletedPurchaseHistory.of(Constants.PRODUCT_NAME, 5000, 5, 0, 0)));

        consumer.purchaseProduct(productInventory);
        consumer.nextPurchaseProduct();
        Bill actual = consumer.generateBill();

        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @DisplayName("모든 구매가 완료되었다면 true, 아니라면 false를 반환한다.")
    @Test
    void isPurchaseCompleted() {
        Consumer consumer = Consumer.from(List.of());

        assertThat(consumer.isPurchaseCompleted()).isTrue();
    }

    private Consumer getConsumer(final int quantity) {
        return Consumer.from(new ArrayList<>(List.of(PurchaseProduct.of(Constants.PRODUCT_NAME, quantity))));
    }

    private Consumer getConsumer(final String productName) {
        return Consumer.from(new ArrayList<>(List.of(PurchaseProduct.of(productName, 1))));
    }
}
