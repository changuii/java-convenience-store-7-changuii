package store.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import store.Constants;
import store.domain.product.BuyGet;
import store.domain.product.DateRange;
import store.domain.product.ProductQuantity;
import store.domain.product.Promotion;
import store.domain.product.PromotionProductQuantity;

public class PurchaseProductTest {
    @DisplayName("매개변수로 들어온 productName과 이름이 일치한다면 true 아니라면 false를 반환한다.")
    @ParameterizedTest
    @CsvSource(value = {"PRODUCT:true", "감자:false", "라면:false"}, delimiter = ':')
    void isMatchProductName(final String productName, final boolean expected) {
        PurchaseProduct purchaseProduct = PurchaseProduct.of(Constants.PRODUCT_NAME, Constants.PRODUCT_QUANTITY_VALUE);

        assertThat(purchaseProduct.isMatchProductName(productName)).isEqualTo(expected);
    }

    @DisplayName("현재 남은 수량(프로모션 + 일반상품)이 상품의 재고 수량 이하라면 true, 아니라면 false를 반환한다.")
    @ParameterizedTest
    @CsvSource(value = {"15:true", "10:true", "20:false", "16:false"}, delimiter = ':')
    void isLessThanQuantityWithPromotionQuantityAndProductQuantity(final int currentQuantity, final boolean expected) {
        DateRange dateRange = DateRange.of(LocalDate.of(2023, 11, 01), LocalDate.of(2023, 11, 30));
        Promotion promotion = Promotion.of(Constants.PROMOTION_NAME, BuyGet.of(1, 1), dateRange);
        ProductQuantity productQuantity = ProductQuantity.of(Constants.PRODUCT_NAME, 5);
        PromotionProductQuantity promotionProductQuantity = PromotionProductQuantity.of(Constants.PRODUCT_NAME, 10,
                promotion);
        PurchaseProduct purchaseProduct = PurchaseProduct.of(Constants.PRODUCT_NAME, currentQuantity);

        boolean actual = purchaseProduct.isLessThanQuantity(promotionProductQuantity, productQuantity);

        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("현재 남은 수량(일반상품)이 상품의 재고 수량 이하라면 true, 아니라면 false를 반환한다.")
    @ParameterizedTest
    @CsvSource(value = {"5:true", "1:true", "6:false", "100:false"}, delimiter = ':')
    void isLessThanQuantityWithProductQuantity(final int currentQuantity, final boolean expected) {
        ProductQuantity productQuantity = ProductQuantity.of(Constants.PRODUCT_NAME, 5);
        PurchaseProduct purchaseProduct = PurchaseProduct.of(Constants.PRODUCT_NAME, currentQuantity);

        boolean actual = purchaseProduct.isLessThanQuantity(productQuantity);

        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("상품의 재고가 현재 남은 갯수 이하라면, 모두 사버리고 이상이라면 남은 갯수만큼 차감하고 반환한다.")
    @ParameterizedTest
    @CsvSource(value = {"1:2:0", "5:10:0", "7:3:4", "150:87:63"}, delimiter = ':')
    void purchaseUntilAvailable(int currentQuantity, int productQuantity, int expected) {
        PurchaseProduct purchaseProduct = PurchaseProduct.of(Constants.PRODUCT_NAME, currentQuantity);
        PurchaseProduct expectedProduct = PurchaseProduct.of(Constants.PRODUCT_NAME, expected);

        purchaseProduct.purchaseUntilAvailable(productQuantity);

        assertThat(purchaseProduct).usingRecursiveComparison().isEqualTo(expectedProduct);
    }

    @DisplayName("PurchaseProduct는 결제가 완료되면, 기록된 내용을 바탕으로 CompleletedPurchaseHistory를 생성한다.")
    @ParameterizedTest
    @CsvSource(value = {"10:10000", "20:500", "30:700000"}, delimiter = ':')
    void generatePurchaseHistory(int purchaseQuantity, int totalPurchasePrice) {
        PurchaseProduct purchaseProduct = PurchaseProduct.of(Constants.PRODUCT_NAME, purchaseQuantity);
        CompletedPurchaseHistory expected = CompletedPurchaseHistory.of(Constants.PRODUCT_NAME,
                totalPurchasePrice, purchaseQuantity, Constants.EMPTY_NUM, Constants.EMPTY_NUM);

        purchaseProduct.recordRegularPricePurchaseHistory(purchaseQuantity, totalPurchasePrice);
        CompletedPurchaseHistory actual = purchaseProduct.generateCompletedPurchaseHistory();

        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @DisplayName("isPurchaseCompleted 메서드는 현재 재고가 0이라면 true, 아직 재고가 남아있다면 false를 반환한다.")
    @ParameterizedTest
    @CsvSource(value = {"0:true", "1:false", "2:false", "3:false"}, delimiter = ':')
    void isPurchaseCompleted(final int currentQuantity, final boolean expected) {
        PurchaseProduct purchaseProduct = PurchaseProduct.of(Constants.PRODUCT_NAME, currentQuantity);

        boolean actual = purchaseProduct.isPurchaseCompleted();

        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("구매하려는 수량에 대해 프로모션에 필요한 수량을 반환한다.")
    @ParameterizedTest
    @CsvSource(value = {"7:6", "10:9", "12:12", "5:6"}, delimiter = ':')
    void getRequiredQuantityForApplyPromotion(final int purchaseQuantity, final int expected) {
        BuyGet buyGet = BuyGet.of(2, 1);
        DateRange dateRange = DateRange.of(LocalDate.of(2023, 11, 01), LocalDate.of(2023, 11, 30));
        Promotion promotion = Promotion.of(Constants.PROMOTION_NAME, buyGet, dateRange);
        PurchaseProduct purchaseProduct = PurchaseProduct.of(Constants.PRODUCT_NAME, purchaseQuantity);

        int actual = purchaseProduct.getRequiredQuantityForApplyPromotion(promotion);

        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("구매하려는 수량에 대해 정가로 구매해야하는 수량을 반환한다.")
    @ParameterizedTest
    @CsvSource(value = {"7:6:1", "10:7:4", "5:4:2", "5:2:5", "7:3:4", "3:1:3"}, delimiter = ':')
    void calculateQuantityAtRegularPrice(final int purchaseQuantity, final int promotionQuantity, final int expected) {
        BuyGet buyGet = BuyGet.of(2, 1);
        DateRange dateRange = DateRange.of(LocalDate.of(2023, 11, 01), LocalDate.of(2023, 11, 30));
        Promotion promotion = Promotion.of(Constants.PROMOTION_NAME, buyGet, dateRange);
        PromotionProductQuantity promotionProductQuantity = PromotionProductQuantity.of(Constants.PRODUCT_NAME,
                promotionQuantity, promotion);
        PurchaseProduct purchaseProduct = PurchaseProduct.of(Constants.PRODUCT_NAME, purchaseQuantity);

        int actual = purchaseProduct.calculateQuantityAtRegularPrice(promotionProductQuantity);

        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("현재 구매할 남은 수량에 대해 주어진 프로모션에 적용 가능한 무료 수량을 반환한다.")
    @ParameterizedTest
    @CsvSource(value = {"1:0", "2:0", "3:1", "4:1", "5:1", "6:2", "8:2", "9:3"}, delimiter = ':')
    void getApplicableFreeQuantity(final int quantity, final int expected) {
        Promotion promotion = Promotion.of(Constants.PROMOTION_NAME, BuyGet.of(2, 1),
                DateRange.of(LocalDate.MIN, LocalDate.MAX));
        PurchaseProduct purchaseProduct = PurchaseProduct.of(Constants.PRODUCT_NAME, quantity);

        int actual = purchaseProduct.getApplicableFreeQuantity(promotion);

        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("현재 수량이 프로모션(수량도 고려)을 적용하기 위한 수량 조건을 만족한다면 true, 아니라면 false를 반환한다. (2+1 -> 3, 6, 9, 12)")
    @ParameterizedTest
    @CsvSource(value = {"1:true", "2:false", "3:true", "5:false", "6:true", "7:true"}, delimiter = ':')
    void hasQuantitySufficientForApplyPromotion(final int quantity, final boolean expected) {
        Promotion promotion = Promotion.of(Constants.PROMOTION_NAME, BuyGet.of(2, 1),
                DateRange.of(LocalDate.MIN, LocalDate.MAX));
        PromotionProductQuantity promotionProductQuantity = PromotionProductQuantity.of(Constants.PRODUCT_NAME,
                100, promotion);
        PurchaseProduct purchaseProduct = PurchaseProduct.of(Constants.PRODUCT_NAME, quantity);

        boolean actual = purchaseProduct.hasQuantitySufficientForApplyPromotion(promotionProductQuantity);

        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("현재 구매 수량을 1만큼 추가한다. (프로모션 적용이 가능하지만 부족하게 가져온 경우 사용)")
    @ParameterizedTest
    @CsvSource(value = {"1:2", "2:3", "3:4", "10:11", "15:16", "200:201"}, delimiter = ':')
    void additionQuantityForApplyPromotion(final int quantity, final int expected) {
        PurchaseProduct purchaseProduct = PurchaseProduct.of(Constants.PRODUCT_NAME, quantity);
        PurchaseProduct expectedProduct = PurchaseProduct.of(Constants.PRODUCT_NAME, expected);

        purchaseProduct.additionQuantityForApplyPromotion();

        assertThat(purchaseProduct).usingRecursiveComparison().isEqualTo(expectedProduct);
    }

    @DisplayName("정가로 구매한 상품에 대해서 구매 기록에 기록한다.")
    @ParameterizedTest
    @CsvSource(value = {"3:3000", "100:50000", "50:3000", "5:500", "700:10000000"}, delimiter = ':')
    void recordRegularPricePurchaseHistory(final int quantity, final int price) {
        PurchaseProduct purchaseProduct = PurchaseProduct.of(Constants.PRODUCT_NAME, 1000);
        PurchaseHistory expected = PurchaseHistory.from(Constants.PRODUCT_NAME);
        expected.addQuantity(quantity);
        expected.addPurchasePrice(price);

        purchaseProduct.recordRegularPricePurchaseHistory(quantity, price);

        assertThat(purchaseProduct).extracting("purchaseHistory").usingRecursiveComparison().isEqualTo(expected);
    }

    @DisplayName("프로모션 혜택을 받아 구매한 상품에 대해서 구매 기록에 기록한다.")
    @ParameterizedTest
    @CsvSource(value = {"3:3000:1", "100:50000:60", "50:3000:10", "5:500:2", "700:10000000:300"}, delimiter = ':')
    void recordPromotionAppliedPurchaseHistory(final int promotionQuantity, final int price, final int freeQuantity) {
        PurchaseProduct purchaseProduct = PurchaseProduct.of(Constants.PRODUCT_NAME, 1000);
        PurchaseHistory expected = PurchaseHistory.from(Constants.PRODUCT_NAME);
        expected.addPromotionQuantity(promotionQuantity);
        expected.addPurchasePrice(price);
        expected.addFreeQuantity(freeQuantity);

        purchaseProduct.recordPromotionAppliedPurchaseHistory(price, promotionQuantity, freeQuantity);

        assertThat(purchaseProduct).extracting("purchaseHistory").usingRecursiveComparison().isEqualTo(expected);
    }
}
