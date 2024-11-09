package store.component.generator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import store.domain.product.ProductInfo;
import store.domain.ProductInventory;
import store.domain.product.ProductQuantity;
import store.domain.product.Promotion;
import store.domain.product.PromotionProductQuantity;
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
        final List<ProductInfo> productInfos = new ArrayList<>();
        final List<ProductQuantity> productQuantities = new ArrayList<>();
        final List<PromotionProductQuantity> promotionQuantities = new ArrayList<>();

        productLines.forEach((productLine) -> {
            parseProduct(productLine, promotions, productInfos, productQuantities, promotionQuantities);
        });

        return ProductInventory.of(productInfos.stream().distinct().collect(Collectors.toList()), productQuantities,
                promotionQuantities);
    }


    private void parseProduct(final String productLine, final Map<String, Optional<Promotion>> promotions,
                              final List<ProductInfo> productInfos,
                              final List<ProductQuantity> productQuantities,
                              final List<PromotionProductQuantity> promotionQuantities) {
        try {
            String[] column = productLine.split(StoreConfig.TABLE_ROW_DELIMITER.getValue());
            validateContainPromotions(promotions, column[3]);
            registerProduct(column, promotions, productInfos, productQuantities, promotionQuantities);
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new IllegalArgumentException(ErrorMessage.INVALID_FILE_FORMAT.getMessage());
        }
    }

    private void registerProduct(final String[] column, final Map<String, Optional<Promotion>> promotions,
                                 final List<ProductInfo> productInfos,
                                 final List<ProductQuantity> productQuantities,
                                 final List<PromotionProductQuantity> promotionQuantities) {
        registerProductInfo(column[0], parseInt(column[1]), productInfos);
        Optional<Promotion> promotion = promotions.get(column[3]);
        if (promotion.isPresent()) {
            registerProductQuantities(column[0], 0, productQuantities);
            registerPromotionProductQuantities(column[0], parseInt(column[2]), promotion.get(), promotionQuantities);
            return;
        }
        registerProductQuantities(column[0], parseInt(column[2]), productQuantities);
    }

    private void registerProductInfo(final String productName, final int productPrice,
                                     final List<ProductInfo> productInfos) {
        removeIfExsistsInfo(productName, productInfos);
        productInfos.add(ProductInfo.of(productName, productPrice));
    }

    private void removeIfExsistsInfo(final String productName, final List<ProductInfo> productInfos) {
        productInfos.stream()
                .filter(productInfo -> productInfo.isMatchProductName(productName))
                .findAny()
                .ifPresent(productInfo -> productInfos.removeLast());
    }

    private void registerProductQuantities(final String productName, final int productQuantity,
                                           final List<ProductQuantity> productQuantities) {
        removeIfExsistsProductQuantity(productName, productQuantities);
        productQuantities.add(ProductQuantity.of(productName, productQuantity));
    }

    private void registerPromotionProductQuantities(final String productName, final int productQuantity,
                                                    final Promotion promotion,
                                                    final List<PromotionProductQuantity> promotionQuantities) {

        promotionQuantities.add(PromotionProductQuantity.of(productName, productQuantity, promotion));
    }

    private void removeIfExsistsProductQuantity(final String productName,
                                                final List<ProductQuantity> productQuantities) {
        productQuantities.stream()
                .filter(productQuantity -> productQuantity.isMatchProductName(productName))
                .findAny()
                .ifPresent(productQuantity -> productQuantities.removeLast());
    }

    private int parseInt(final String number) {
        try {
            return Integer.parseInt(number);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(ErrorMessage.INVALID_FILE_FORMAT.getMessage());
        }
    }
}