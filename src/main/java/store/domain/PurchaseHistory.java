package store.domain;

public class PurchaseHistory {
    private final String productName;
    private final int totalPurchasePrice;
    private final int quantity;
    private final int promotionQuantity;
    private final int freeQuantity;

    private PurchaseHistory(final String productName, final int totalPurchasePrice, final int quantity,
                            final int promotionQuantity, final int freeQuantity) {
        this.productName = productName;
        this.totalPurchasePrice = totalPurchasePrice;
        this.quantity = quantity;
        this.promotionQuantity = promotionQuantity;
        this.freeQuantity = freeQuantity;
    }

    public static PurchaseHistory of(final String productName, final int totalPurchasePrice, final int quantity,
                                     final int promotionQuantity, final int freeQuantity) {
        return new PurchaseHistory(productName, totalPurchasePrice, quantity, promotionQuantity, freeQuantity);
    }


}
