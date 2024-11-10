package store.domain;

public class CompletedPurchaseHistory {
    private final String productName;
    private final int totalPurchasePrice;
    private final int quantity;
    private final int promotionQuantity;
    private final int freeQuantity;

    private CompletedPurchaseHistory(final String productName, final int totalPurchasePrice, final int quantity,
                                     final int promotionQuantity, final int freeQuantity) {
        this.productName = productName;
        this.totalPurchasePrice = totalPurchasePrice;
        this.quantity = quantity;
        this.promotionQuantity = promotionQuantity;
        this.freeQuantity = freeQuantity;
    }

    public static CompletedPurchaseHistory of(final String productName, final int totalPurchasePrice, final int quantity,
                                              final int promotionQuantity, final int freeQuantity){
        return new CompletedPurchaseHistory(productName, totalPurchasePrice, quantity, promotionQuantity, freeQuantity);
    }
}
