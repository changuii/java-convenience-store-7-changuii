package store.domain.product;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import store.Constants;
import store.domain.PurchaseProduct;

public class ProductTest {
    @DisplayName("수량에 대한 가격을 계산하여 반환한다.")
    @ParameterizedTest
    @CsvSource(value = {"1000:10:10000", "500:3:1500", "300:15:4500", "17500:7:122500"}, delimiter = ':')
    void calculateTotalPrice(final int productPrice, final int quantity, final int expected) {
        Product product = Product.of("", productPrice);

        assertThat(product.calculateTotalPrice(quantity)).isEqualTo(expected);
    }

    @DisplayName("purchaseProduct와 이름이 일치한다면 true 아니라면 false를 반환한다.")
    @ParameterizedTest
    @CsvSource(value = {"PRODUCT:true", "사이다:false", "김치:false"}, delimiter = ':')
    void isMatchProduct(final String purchaseProductName, final boolean expected) {
        Product product = Product.of(Constants.PRODUCT_NAME, Constants.PRODUCT_PRICE);
        PurchaseProduct purchaseProduct = PurchaseProduct.of(purchaseProductName, Constants.PRODUCT_QUANTITY_VALUE);

        assertThat(product.isMatchProduct(purchaseProduct)).isEqualTo(expected);
    }

    @DisplayName("purchaseProductName과 이름이 일치한다면 true 아니라면 false를 반환한다.")
    @ParameterizedTest
    @CsvSource(value = {"PRODUCT:true", "사이다:false", "김치:false"}, delimiter = ':')
    void isMatchProductName(final String purchaseProductName, final boolean expected) {
        Product product = Product.of(Constants.PRODUCT_NAME, Constants.PRODUCT_PRICE);

        assertThat(product.isMatchProductName(purchaseProductName)).isEqualTo(expected);
    }
}
