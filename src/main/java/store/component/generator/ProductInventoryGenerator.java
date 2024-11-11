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
import store.enums.GeneratorConstants;

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
        if (!productLine.matches(GeneratorConstants.PRODUCT_VALUE_REGEX.getValue())) {
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
        parseProducts(productLines, promotions, productInfos, productQuantities, promotionQuantities);

        return ProductInventory.of(
                productInfos.stream().distinct().collect(Collectors.toList()),
                productQuantities,
                promotionQuantities
        );
    }

    private void parseProducts(final List<String> productLines, final Map<String, Optional<Promotion>> promotions,
                               final List<ProductInfo> productInfos,
                               final List<ProductQuantity> productQuantities,
                               final List<PromotionProductQuantity> promotionQuantities) {
        productLines.forEach((productLine) -> {
            parseProduct(productLine, promotions, productInfos, productQuantities, promotionQuantities);
        });
    }


    private void parseProduct(final String productLine, final Map<String, Optional<Promotion>> promotions,
                              final List<ProductInfo> productInfos,
                              final List<ProductQuantity> productQuantities,
                              final List<PromotionProductQuantity> promotionQuantities) {
        try {
            final String[] column = productLine.split(GeneratorConstants.TABLE_ROW_DELIMITER.getValue());
            validateContainPromotions(promotions, column[3]);
            addProduct(column, promotions, productInfos, productQuantities, promotionQuantities);
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new IllegalArgumentException(ErrorMessage.INVALID_FILE_FORMAT.getMessage());
        }
    }

    private void addProduct(final String[] column, final Map<String, Optional<Promotion>> promotions,
                            final List<ProductInfo> productInfos,
                            final List<ProductQuantity> productQuantities,
                            final List<PromotionProductQuantity> promotionQuantities) {
        addProductInfo(column[0], parseInt(column[1]), productInfos);
        Optional<Promotion> promotion = promotions.get(column[3]);
        if (promotion.isPresent()) {
            addProductQuantities(column[0], 0, productQuantities);
            addPromotionProductQuantities(column[0], parseInt(column[2]), promotion.get(), promotionQuantities);
            return;
        }
        addProductQuantities(column[0], parseInt(column[2]), productQuantities);
    }

    private void addProductInfo(final String productName, final int productPrice,
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

    private void addProductQuantities(final String productName, final int productQuantity,
                                      final List<ProductQuantity> productQuantities) {
        removeIfExsistsProductQuantity(productName, productQuantities);
        productQuantities.add(ProductQuantity.of(productName, productQuantity));
    }

    private void addPromotionProductQuantities(final String productName, final int productQuantity,
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
