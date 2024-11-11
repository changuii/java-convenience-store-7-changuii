package store.domain.product;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import store.Constants;
import store.domain.PurchaseProduct;

public class PromotionProductQuantityTest {
    private final DateRange dateRange;
    private final BuyGet buyGet;
    private Promotion promotion;

    public PromotionProductQuantityTest() {
        dateRange = DateRange.of(LocalDate.of(2023, 11, 01), LocalDate.of(2023, 11, 30));
        buyGet = BuyGet.of(2, 1);
    }

    @BeforeEach
    void init() {
        promotion = Promotion.of(Constants.PROMOTION_NAME, buyGet, dateRange);
    }


    @DisplayName("매개변수로 들어온 ProductQuantity와 재고를 합산해_재고가 충분히_있는지 검증한다")
    @ParameterizedTest
    @CsvSource(value = {"1:1:2:true", "2:2:4:true", "1:0:2:false", "2:1:4:false"}, delimiter = ':')
    void isLessThanQuantityTest(final int promotionQuantity, final int quantity, final int purchaseQuantity,
                                final boolean expected) {
        ProductQuantity productQuantity = ProductQuantity.of(Constants.PRODUCT_NAME, quantity);
        PromotionProductQuantity promotionProductQuantity = PromotionProductQuantity.of(Constants.PRODUCT_NAME,
                promotionQuantity,
                promotion);

        boolean actual = promotionProductQuantity.isLessThanQuantity(purchaseQuantity, productQuantity);

        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("현재 프로모션이 진행중인지 반환하는 메서드")
    @ParameterizedTest
    @CsvSource(value = {"2023-11-01:true", "2023-11-30:true", "2023-10-31:false", "2023-12-01:false"}, delimiter = ':')
    void isValidToday(final LocalDate today, final boolean expected) {
        PromotionProductQuantity promotionQuantity =
                PromotionProductQuantity.of(Constants.PRODUCT_NAME, Constants.PRODUCT_QUANTITY_VALUE, promotion);

        assertThat(promotionQuantity.isValidToday(today)).isEqualTo(expected);
    }

    @DisplayName("purchaseProduct와 이름이 같다면 true를 반환하고 아니라면 false를 반환한다.")
    @ParameterizedTest
    @CsvSource(value = {"PRODUCT:true", "감자:false", "치킨:false"}, delimiter = ':')
    void isMatchProduct(final String purchaseProductName, final boolean expected) {
        PromotionProductQuantity promotionProductQuantity = PromotionProductQuantity.of(Constants.PRODUCT_NAME,
                Constants.PRODUCT_QUANTITY_VALUE, promotion);
        PurchaseProduct purchaseProduct = PurchaseProduct.of(purchaseProductName, Constants.PRODUCT_QUANTITY_VALUE);

        assertThat(promotionProductQuantity.isMatchProduct(purchaseProduct)).isEqualTo(expected);
    }

    @DisplayName("purchaseProductName과 이름이 같다면 true를 반환하고 아니라면 false를 반환한다.")
    @ParameterizedTest
    @CsvSource(value = {"PRODUCT:true", "감자:false", "치킨:false"}, delimiter = ':')
    void isMatchProductName(final String purchaseProductName, final boolean expected) {
        PromotionProductQuantity promotionProductQuantity = PromotionProductQuantity.of(Constants.PRODUCT_NAME,
                Constants.PRODUCT_QUANTITY_VALUE, promotion);

        assertThat(promotionProductQuantity.isMatchProductName(purchaseProductName)).isEqualTo(expected);
    }

    @DisplayName("구매하려는 상품의 수량이 프로모션을 적용하는데 필요한 프로모션 수량 이하라면 true 초과라면 false를 반환한다.")
    @ParameterizedTest
    @CsvSource(value = {"7:6:true", "7:5:false", "8:9:true", "8:8:false"}, delimiter = ':')
    void isRequirementMetForApplyPromotion(final int purchaseQutntity, final int promotionQuantity,
                                           final boolean expected) {
        PurchaseProduct purchaseProduct = PurchaseProduct.of(Constants.PRODUCT_NAME, purchaseQutntity);
        PromotionProductQuantity promotionProductQuantity = PromotionProductQuantity.of(Constants.PRODUCT_NAME,
                promotionQuantity, promotion);

        boolean actual = promotionProductQuantity.isRequirementMetForApplyPromotion(purchaseProduct);

        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("현재 남은 재고에 대해 프로모션을 적용가능한 수량을 반환한다.")
    @ParameterizedTest
    @CsvSource(value = {"6:6", "5:3", "3:3", "2:0", "1:0", "9:9", "8:6", "12:12", "11:9", "14:12"}, delimiter = ':')
    void getApplicableQuantity(final int promotionQuantity, final int expected) {
        PromotionProductQuantity promotionProductQuantity = PromotionProductQuantity.of(Constants.PRODUCT_NAME,
                promotionQuantity, promotion);

        int actual = promotionProductQuantity.getApplicableQuantity();

        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("주어진 수량에 대해 프로모션을 적용하기 위한 요구 수량을 반환한다.")
    @ParameterizedTest
    @CsvSource(value = {"3:3", "2:3", "1:0", "4:3", "5:6", "8:9", "9:9", "11:12", "13:12"}, delimiter = ':')
    void getRequiredQuantityForApplyPromotion(final int quantity, final int expected) {
        int requirementMetQuantityForApplyPromotion = 100;
        PromotionProductQuantity promotionProductQuantity = PromotionProductQuantity.of(Constants.PRODUCT_NAME,
                requirementMetQuantityForApplyPromotion, promotion);

        int actual = promotionProductQuantity.getRequiredQuantityForApplyPromotion(quantity);

        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("주어진 PurchaseProduct가 남은 수량만큼 프로모션 제공 없이 프로모션 제고를 차감하고 차감한 수량을 반환한다.")
    @ParameterizedTest
    @CsvSource(value = {"5:5", "1:1", "50:50", "100:100", "120:100"}, delimiter = ':')
    void deductQuantityWithoutPromotion(final int quantity, final int expectedDeductedQuantity) {
        PurchaseProduct purchaseProduct = PurchaseProduct.of(Constants.PRODUCT_NAME, quantity);
        int requirementMetQuantityForPurchase = 100;
        PromotionProductQuantity promotionProductQuantity = PromotionProductQuantity.of(Constants.PRODUCT_NAME,
                requirementMetQuantityForPurchase, promotion);

        int actualDeductedQuantity = promotionProductQuantity.deductQuantityWithoutPromotion(purchaseProduct);

        assertThat(actualDeductedQuantity).isEqualTo(expectedDeductedQuantity);
    }

    @DisplayName("PromotionQuantity가 남은 재고에서 프로모션을 적용 가능한 수량만큼 구매하고, 무료 수량을 제외한 값을 반환한다.")
    @ParameterizedTest
    @CsvSource(value = {"1:1", "3:2", "6:4", "9:6", "12:8"}, delimiter = ':')
    void deductQuantityApplyPromotion(final int quantity, final int expectedDeductedQuantity) {
        PurchaseProduct purchaseProduct = PurchaseProduct.of(Constants.PRODUCT_NAME, quantity);
        int requirementMetQuantityForPurchase = 100;
        PromotionProductQuantity promotionProductQuantity = PromotionProductQuantity.of(Constants.PRODUCT_NAME,
                requirementMetQuantityForPurchase, promotion);
        int freeQuantity = promotionProductQuantity.getApplicableFreeQuantity(purchaseProduct);

        int actualDeductedQuantity = promotionProductQuantity.deductQuantityApplyPromotion(purchaseProduct,
                freeQuantity);

        assertThat(actualDeductedQuantity).isEqualTo(expectedDeductedQuantity);
    }

    @DisplayName("주어진 PurchaseProduct의 수량에 대해 프로모션이 적용가능한 무료 수량을 반환한다.")
    @ParameterizedTest
    @CsvSource(value = {"6:5:1", "6:6:2", "5:6:1", "3:1:0", "3:2:0", "3:3:1"}, delimiter = ':')
    void getApplicableFreeQuantity(final int quantity, final int promotionQuantity, final int expected) {
        PurchaseProduct purchaseProduct = PurchaseProduct.of(Constants.PRODUCT_NAME, quantity);
        PromotionProductQuantity promotionProductQuantity = PromotionProductQuantity.of(Constants.PRODUCT_NAME,
                promotionQuantity, promotion);

        int actual = promotionProductQuantity.getApplicableFreeQuantity(purchaseProduct);

        assertThat(actual).isEqualTo(expected);
    }

}
