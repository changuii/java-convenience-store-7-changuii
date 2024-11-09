package store.component;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import store.domain.ProductInfo;
import store.domain.ProductInventory;
import store.domain.ProductQuantity;
import store.domain.Promotion;
import store.domain.PromotionProductQuantity;
import store.enums.ErrorMessage;
import store.enums.StoreConfig;

public class ProductInventoryGenerator {


    public ProductInventory generate(final Map<String, Optional<Promotion>> promotions,
                                     final List<String> productLines) {
        validate(productLines);
        return createProductInventory(promotions, productLines);
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

    private ProductInventory createProductInventory(final Map<String, Optional<Promotion>> promotions,
                                                    final List<String> productLines) {
        final Map<String, ProductInfo> productInfos = new LinkedHashMap<>();
        final Map<String, ProductQuantity> productQuantities = new LinkedHashMap<>();
        final Map<String, PromotionProductQuantity> promotionQuantities = new LinkedHashMap<>();

        productLines.forEach((productLine) -> {
            parseProduct(productLine, promotions, productInfos, productQuantities, promotionQuantities);
        });
        return ProductInventory.of(productInfos, productQuantities, promotionQuantities);
    }


    private void parseProduct(final String productLine, final Map<String, Optional<Promotion>> promotions,
                              final Map<String, ProductInfo> productInfos,
                              final Map<String, ProductQuantity> productQuantities,
                              final Map<String, PromotionProductQuantity> promotionQuantities) {
        try {
            String[] product = productLine.split(StoreConfig.TABLE_ROW_DELIMITER.getValue());
            validateContainPromotions(promotions, product[3]);
            registerProduct(product, promotions, productInfos, productQuantities, promotionQuantities);
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new IllegalArgumentException(ErrorMessage.INVALID_FILE_FORMAT.getMessage());
        }
    }

    private void registerProduct(final String[] product, final Map<String, Optional<Promotion>> promotions,
                                 final Map<String, ProductInfo> productInfos,
                                 final Map<String, ProductQuantity> productQuantities,
                                 final Map<String, PromotionProductQuantity> promotionQuantities) {
        registerProductInfo(product[0], parseInt(product[1]), productInfos);
        Optional<Promotion> promotion = promotions.get(product[3]);
        if (promotion.isPresent()) {
            registerProductQuantities(product[0], 0, productQuantities);
            registerPromotionProductQuantities(product[0], parseInt(product[2]), promotion.get(), promotionQuantities);
            return;
        }
        registerProductQuantities(product[0], parseInt(product[2]), productQuantities);
    }

    private void registerProductInfo(final String productName, final int productPrice,
                                     final Map<String, ProductInfo> productInfos) {
        productInfos.put(productName, ProductInfo.of(productName, productPrice));
    }

    private void registerProductQuantities(final String productName, final int productQuantity,
                                           final Map<String, ProductQuantity> quantities) {
        quantities.put(productName, ProductQuantity.from(productQuantity));
    }

    private void registerPromotionProductQuantities(final String productName, final int productQuantity,
                                                    final Promotion promotion,
                                                    final Map<String, PromotionProductQuantity> quantities) {
        quantities.put(productName, PromotionProductQuantity.of(productQuantity, promotion));
    }

    private int parseInt(final String number) {
        try {
            return Integer.parseInt(number);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(ErrorMessage.INVALID_FILE_FORMAT.getMessage());
        }
    }
}
