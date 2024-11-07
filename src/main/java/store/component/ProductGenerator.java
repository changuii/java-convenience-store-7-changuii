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
        validate(productLines);
        productLines.stream()
                .map(productLine -> parseProduct(productLine, promotions))
                .forEach(product -> product.registerProduct(products, promotionProducts));
    }

    private void validate(final List<String> productLines) {
        productLines.forEach(this::validateProductLineFormat);
    }

    private void validateProductLineFormat(final String productLine) {
        if (!productLine.matches(StoreConfig.PRODUCT_VALUE_REGEX.getValue())) {
            throw new IllegalArgumentException(ErrorMessage.INVALID_FILE_FORMAT.getMessage());
        }
    }

    private void validateContainPromotions(final Map<String, Optional<Promotion>> promotions,
                                           final String promotionName) {
        if (!promotions.containsKey(promotionName)) {
            throw new IllegalArgumentException(ErrorMessage.INVALID_FILE_FORMAT.getMessage());
        }
    }

    private Product parseProduct(final String productLine, final Map<String, Optional<Promotion>> promotions) {
        try {
            String[] product = productLine.split(StoreConfig.TABLE_ROW_DELIMITER.getValue());
            validateContainPromotions(promotions, product[3]);
            return Product.of(product[0], parseInt(product[1]), parseInt(product[2]), promotions.get(product[3]));
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new IllegalArgumentException(ErrorMessage.INVALID_FILE_FORMAT.getMessage());
        }
    }

    private int parseInt(final String number) {
        try {
            return Integer.parseInt(number);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(ErrorMessage.INVALID_FILE_FORMAT.getMessage());
        }
    }
}
