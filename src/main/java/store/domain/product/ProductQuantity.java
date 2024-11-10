package store.domain.product;

import store.domain.PurchaseProduct;

public class ProductQuantity {
    private final String productName;
    private int quantity;

    private ProductQuantity(final String productName, final int quantity) {
        this.productName = productName;
        this.quantity = quantity;
    }

    public static ProductQuantity of(final String productName, final int quantity) {
        return new ProductQuantity(productName, quantity);
    }

    public boolean isLessThanQuantity(final int quantity) {
        return this.quantity >= quantity;
    }

    public int deductQuantity(final PurchaseProduct purchaseProduct) {
        int deductQuantity = purchaseProduct.deductQuantity(quantity);
        quantity -= deductQuantity;
        return deductQuantity;
    }

    public boolean isMatchProduct(final PurchaseProduct purchaseProduct) {
        return purchaseProduct.isMatchProductName(productName);
    }

    public boolean isMatchProductName(final String name) {
        return productName.equals(name);
    }

    public int getQuantity() {
        return quantity;
    }
}
