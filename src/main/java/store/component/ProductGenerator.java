package store.component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import store.domain.Product;
import store.domain.Promotion;
import store.enums.ErrorMessage;
import store.enums.StoreConfig;

public class ProductGenerator {


    public void generate(final Map<String, Optional<Promotion>> promotions,
                          final Map<String, Product> products,
                          final Map<String, Product> promotionProducts, final List<String> productLines) {
        productLines.stream()
                .map(productLine -> parseProduct(productLine, promotions))
                .forEach(product -> product.registerProduct(products, promotionProducts));
    }


    private Product parseProduct(final String productLine, final Map<String, Optional<Promotion>> promotions) {
        String[] product = productLine.split(StoreConfig.TABLE_ROW_DELIMITER.getValue());
        String productName = product[0];
        int productPrice = parseInt(product[1]);
        int productQuantity = parseInt(product[2]);
        Optional<Promotion> promotion = promotions.get(product[3]);
        return Product.of(productName, productPrice, productQuantity, promotion);
    }

    private int parseInt(final String number) {
        try {
            return Integer.parseInt(number);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(ErrorMessage.INVALID_FILE_FORMAT.getMessage());
        }
    }
}
