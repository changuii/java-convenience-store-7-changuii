package store.component.generator;

import java.util.List;
import store.domain.Consumer;
import store.domain.ProductInventory;
import store.domain.PurchaseProduct;
import store.enums.ErrorMessage;

public class ConsumerGenerator {

    private final ProductInventory productInventory;
    private final LocalDateGenerator localDateGenerator;

    public ConsumerGenerator(final ProductInventory productInventory, final LocalDateGenerator localDateGenerator) {
        this.productInventory = productInventory;
        this.localDateGenerator = localDateGenerator;
    }

    public Consumer generate(List<PurchaseProduct> purchaseProducts) {
        validatePurchaseProducts(purchaseProducts);
        return Consumer.from(purchaseProducts);
    }


    public void validatePurchaseProducts(final List<PurchaseProduct> purchaseProducts) {
        purchaseProducts.forEach(this::validatePurchaseProduct);
    }

    private void validatePurchaseProduct(final PurchaseProduct purchaseProduct) {
        validateExistsProduct(purchaseProduct);
        validateLessThanQuantity(purchaseProduct);
    }

    private void validateExistsProduct(final PurchaseProduct purchaseProduct) {
        if (!productInventory.containsProduct(purchaseProduct)) {
            throw new IllegalArgumentException(ErrorMessage.NOT_EXISTS_PRODUCT_NAME.getMessage());
        }
    }

    private void validateLessThanQuantity(final PurchaseProduct purchaseProduct) {
        if (!productInventory.isLessThanQuantity(purchaseProduct, localDateGenerator.generate())) {
            throw new IllegalArgumentException(ErrorMessage.MORE_THAN_PURCHASE_PRODUCT_QUANTITY.getMessage());
        }
    }


}
