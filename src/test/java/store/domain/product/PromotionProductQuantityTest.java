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


    @DisplayName("매개변수로_들어온_ProductQuantity와_재고를_합산해_재고가_충분히_있는지_검증한다")
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

    @DisplayName("구매하려는 상품의 수량이 필요한 총 프로모션 수량 이하라면 true 초과라면 false를 반환한다.")
    @ParameterizedTest
    @CsvSource(value = {"7:6:true", "7:5:false", "8:9:true", "8:8:false"}, delimiter = ':')
    void isQuantityEnough(final int purchaseQutntity, final int promotionQuantity, final boolean expected) {
        PurchaseProduct purchaseProduct = PurchaseProduct.of(Constants.PRODUCT_NAME, purchaseQutntity);
        PromotionProductQuantity promotionProductQuantity = PromotionProductQuantity.of(Constants.PRODUCT_NAME,
                promotionQuantity, promotion);

        boolean actual = promotionProductQuantity.isRequirementMetForApplyPromotion(purchaseProduct);

        assertThat(actual).isEqualTo(expected);
    }
}
