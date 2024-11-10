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

    @DisplayName("상품의 재고가 현재 남은 갯수 이하라면, 모두 사버리고 이상이라면 남은 갯수만큼 차감하고 차감한 만큼 반환한다.")
    @ParameterizedTest
    @CsvSource(value = {"1:2:1", "5:10:5", "7:3:3", "150:87:87"}, delimiter = ':')
    void deductQuantity(int currentQuantity, int productQuantity, int expected) {
        PurchaseProduct purchaseProduct = PurchaseProduct.of(Constants.PRODUCT_NAME, currentQuantity);

        int actual = purchaseProduct.deductQuantity(productQuantity);

        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("PurchaseProduct는 결제가 완료되면, 기록된 내용을 바탕으로 CompleletedPurchaseHistory를 생성한다.")
    @ParameterizedTest
    @CsvSource(value = {"10:10000", "20:500", "30:700000"}, delimiter = ':')
    void generatePurchaseHistory(int purchaseQuantity, int totalPurchasePrice) {
        PurchaseProduct purchaseProduct = PurchaseProduct.of(Constants.PRODUCT_NAME, purchaseQuantity);
        CompletedPurchaseHistory expected = CompletedPurchaseHistory.of(Constants.PRODUCT_NAME,
                totalPurchasePrice, purchaseQuantity, Constants.EMPTY_NUM, Constants.EMPTY_NUM);

        purchaseProduct.writePurchaseHistory(purchaseQuantity, totalPurchasePrice);
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
    void calculateNeedQuantity(final int purchaseQuantity, final int expected) {
        BuyGet buyGet = BuyGet.of(2, 1);
        DateRange dateRange = DateRange.of(LocalDate.of(2023, 11, 01), LocalDate.of(2023, 11, 30));
        Promotion promotion = Promotion.of(Constants.PROMOTION_NAME, buyGet, dateRange);
        PurchaseProduct purchaseProduct = PurchaseProduct.of(Constants.PRODUCT_NAME, purchaseQuantity);

        int actual = purchaseProduct.calculateNeedQuantity(promotion);

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

}
