package store.domain.product;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import store.Constants;
import store.domain.PurchaseProduct;

public class ProductInfoTest {


    @ParameterizedTest
    @CsvSource(value = {"1000:10:10000", "500:3:1500", "300:15:4500", "17500:7:122500"}, delimiter = ':')
    void 수량에_대한_가격을_계산하여_반환한다(int productPrice, int quantity, int expected) {
        ProductInfo productInfo = ProductInfo.of("", productPrice);

        int actual = productInfo.calculateTotalPrice(quantity);

        assertThat(actual).isEqualTo(expected);
    }

    @ParameterizedTest
    @CsvSource(value = {"PRODUCT:true", "사이다:false", "김치:false"}, delimiter = ':')
    void 구매_상품과_이름과_일치하는지_검사하는_메서드(String purchaseProductName, boolean expected){
        ProductInfo productInfo = ProductInfo.of(Constants.PRODUCT_NAME, Constants.PRODUCT_PRICE);
        PurchaseProduct purchaseProduct = PurchaseProduct.of(purchaseProductName, Constants.PRODUCT_QUANTITY_VALUE);

        assertThat(productInfo.isMatchProduct(purchaseProduct)).isEqualTo(expected);
    }

}
