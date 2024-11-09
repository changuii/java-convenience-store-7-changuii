package store.domain.product;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import store.Constants;
import store.domain.PurchaseProduct;

public class ProductInfoTest {


    @DisplayName("수량에 대한 가격을 계산하여 반환한다.")
    @ParameterizedTest
    @CsvSource(value = {"1000:10:10000", "500:3:1500", "300:15:4500", "17500:7:122500"}, delimiter = ':')
    void calculateTotalPrice(final int productPrice, final int quantity, final int expected) {
        ProductInfo productInfo = ProductInfo.of("", productPrice);

        assertThat(productInfo.calculateTotalPrice(quantity)).isEqualTo(expected);
    }

    @DisplayName("purchaseProduct와 이름이 일치한다면 true 아니라면 false를 반환한다.")
    @ParameterizedTest
    @CsvSource(value = {"PRODUCT:true", "사이다:false", "김치:false"}, delimiter = ':')
    void isMatchProduct(final String purchaseProductName, final boolean expected) {
        ProductInfo productInfo = ProductInfo.of(Constants.PRODUCT_NAME, Constants.PRODUCT_PRICE);
        PurchaseProduct purchaseProduct = PurchaseProduct.of(purchaseProductName, Constants.PRODUCT_QUANTITY_VALUE);

        assertThat(productInfo.isMatchProduct(purchaseProduct)).isEqualTo(expected);
    }

}
