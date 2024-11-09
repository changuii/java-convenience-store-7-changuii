package store.domain;

public class PurchaseProduct {
    private final String productName;
    private final int totalPurchasePrice;
    private final int quantity;
    private final int promotionQuantity;
    private final int freeQuantity;

    private PurchaseProduct(final String productName, final int totalPurchasePrice, final int quantity,
                            final int promotionQuantity, final int freeQuantity) {
        this.productName = productName;
        this.totalPurchasePrice = totalPurchasePrice;
        this.quantity = quantity;
        this.promotionQuantity = promotionQuantity;
        this.freeQuantity = freeQuantity;
    }

    public static PurchaseProduct of(final String productName, final int totalPurchasePrice, final int quantity,
                                     final int promotionQuantity, final int freeQuantity) {
        return new PurchaseProduct(productName, totalPurchasePrice, quantity, promotionQuantity, freeQuantity);
    }


}
