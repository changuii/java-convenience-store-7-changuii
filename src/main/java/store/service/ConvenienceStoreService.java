package store.service;

import java.util.List;
import store.component.LocalDateGenerator;
import store.domain.ProductInventory;
import store.dto.PurchaseProductDTO;
import store.enums.ErrorMessage;

public class ConvenienceStoreService {
    private final ProductInventory productInventory;
    private final LocalDateGenerator localDateGenerator;

    private ConvenienceStoreService(final ProductInventory productInventory,
                                    final LocalDateGenerator localDateGenerator) {
        this.productInventory = productInventory;
        this.localDateGenerator = localDateGenerator;
    }

    public static ConvenienceStoreService of(final ProductInventory productInventory,
                                             final LocalDateGenerator localDateGenerator) {
        return new ConvenienceStoreService(productInventory, localDateGenerator);
    }

    public void validatePurchaseProducts(final List<PurchaseProductDTO> purchaseProductDTOs) {
        purchaseProductDTOs.forEach(this::validatePurchaseProduct);
    }

    private void validatePurchaseProduct(final PurchaseProductDTO purchaseProductDTO) {
        validateExistsProductName(purchaseProductDTO.getName());
        validateLessThanQuantity(purchaseProductDTO.getName(), purchaseProductDTO.getQuantity());
    }

    private void validateExistsProductName(final String productName) {
        if (!productInventory.containsProductName(productName)) {
            throw new IllegalArgumentException(ErrorMessage.NOT_EXISTS_PRODUCT_NAME.getMessage());
        }
    }

    private void validateLessThanQuantity(final String productName, final int quantity) {
        if (!productInventory.isLessThanQuantity(productName, quantity, localDateGenerator.generate())) {
            throw new IllegalArgumentException(ErrorMessage.MORE_THAN_PURCHASE_PRODUCT_QUANTITY.getMessage());
        }
    }


    public ProductInventory getProductInventory() {
        return productInventory;
    }

}
