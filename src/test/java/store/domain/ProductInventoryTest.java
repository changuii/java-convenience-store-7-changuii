package store.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import store.Constants;
import store.domain.product.BuyGet;
import store.domain.product.DateRange;
import store.domain.product.ProductInfo;
import store.domain.product.ProductQuantity;
import store.domain.product.Promotion;
import store.domain.product.PromotionProductQuantity;

public class ProductInventoryTest {
    private static final int GARBAGE_QUANTITY = 1;
    private ProductInventory productInventory;


    @BeforeEach
    void init() {
        LocalDate start = LocalDate.of(2023, 11, 01);
        LocalDate end = LocalDate.of(2023, 11, 30);
        Promotion promotion = Promotion.of(Constants.PROMOTION_NAME, BuyGet.of(1, 1), DateRange.of(start, end));
        List<ProductInfo> infos = List.of(ProductInfo.of(Constants.PRODUCT_NAME, 1000));
        List<ProductQuantity> quantities = List.of(ProductQuantity.of(Constants.PRODUCT_NAME, 5));
        List<PromotionProductQuantity> promotionQuantity = List.of(
                PromotionProductQuantity.of(Constants.PRODUCT_NAME, 10, promotion));
        productInventory = ProductInventory.of(infos, quantities, promotionQuantity);
    }

    @DisplayName("물건이 존재한다면 true, 존재하지 않는다면 false를 반환한다.")
    @ParameterizedTest
    @CsvSource(value = {"PRODUCT:true", "김치:false", "오렌지주스:false"}, delimiter = ':')
    void containsProductName(String productName, boolean expected) {
        PurchaseProduct purchaseProduct = PurchaseProduct.of(productName, GARBAGE_QUANTITY);

        assertThat(productInventory.containsProduct(purchaseProduct)).isEqualTo(expected);
    }

    @DisplayName("수량이 충분하면 true, 수량이 부족하면 false를 반환한다.")
    @ParameterizedTest
    @CsvSource(value = {"1:true", "10:true", "15:true", "16:false", "200:false"}, delimiter = ':')
    void isLessThanQuantity(int purchaseQuantity, boolean expected) {
        PurchaseProduct purchaseProduct = PurchaseProduct.of(Constants.PRODUCT_NAME, purchaseQuantity);
        LocalDate inPromotionRangeDate = LocalDate.of(2023, 11, 01);

        assertThat(productInventory.isLessThanQuantity(purchaseProduct, inPromotionRangeDate)).isEqualTo(expected);
    }

    @DisplayName("현재날짜가 프로모션 날짜에 포함되면 프로모션의 수량을 포함하고, 그렇지 않다면 포함하지 않는다.")
    @ParameterizedTest
    @CsvSource(value = {"2023-11-01:true", "2023-11-30:true", "2023-10-31:false", "2023-12-01:false"}, delimiter = ':')
    void 프로모션_날짜_테스트(LocalDate today, boolean expected) {
        int purchaseQuantity = 15;
        PurchaseProduct purchaseProduct = PurchaseProduct.of(Constants.PRODUCT_NAME, purchaseQuantity);

        assertThat(productInventory.isLessThanQuantity(purchaseProduct, today)).isEqualTo(expected);
    }

    @DisplayName("현재날짜가 포로모션 날짜에 포함하면서, 프로모션 물품이 있다면 true 아니라면 false를 반환한다.")
    @ParameterizedTest
    @CsvSource(value = {"2023-11-01:PRODUCT:true", "2023-10-31:PRODUCT:false", "2023-11-01:김치:false"}, delimiter = ':')
    void 프로모션_상품_존재_여부_테스트(final LocalDate today, final String productName, final boolean expected) {
        PurchaseProduct purchaseProduct = PurchaseProduct.of(productName, GARBAGE_QUANTITY);

        assertThat(productInventory.isPromotionProduct(purchaseProduct, today)).isEqualTo(expected);
    }

    @DisplayName("구매 상품에 대해서 재고를 차감하고, 구매 내역을 기록한다.")
    @Test
    void purchaseProduct(){
        PurchaseProduct actual = PurchaseProduct.of(Constants.PRODUCT_NAME, GARBAGE_QUANTITY);
        PurchaseProduct expected = PurchaseProduct.of(Constants.PRODUCT_NAME, 0);
        expected.writePurchaseHistory(1, 1000);

        productInventory.purchaseProduct(actual);

        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }


}
