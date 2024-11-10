package store.dto;

import java.util.List;

public class BillDTO {
    private final List<ProductDTO> purchaseProducts;
    private final List<ProductDTO> freeProducts;
    private final int totalPurchasePrice;
    private final int promotionDiscount;
    private final int membershipDiscount;
    private final int checkoutPrice;
    private final int totalPurchaseQuantity;

    private BillDTO(final List<ProductDTO> purchaseProducts, final List<ProductDTO> freeProducts,
                    final int totalPurchasePrice, final int promotionDiscount, final int membershipDiscount,
                    final int checkoutPrice, final int totalPurchaseQuantity) {
        this.purchaseProducts = purchaseProducts;
        this.freeProducts = freeProducts;
        this.totalPurchasePrice = totalPurchasePrice;
        this.promotionDiscount = promotionDiscount;
        this.membershipDiscount = membershipDiscount;
        this.checkoutPrice = checkoutPrice;
        this.totalPurchaseQuantity = totalPurchaseQuantity;
    }

    public static BillDTO of(final List<ProductDTO> purchaseProducts, final List<ProductDTO> freeProducts,
                             final int totalPurchasePrice, final int promotionDiscount, final int membershipDiscount,
                             final int checkoutPrice, final int totalPurchaseQuantity) {
        return new BillDTO(purchaseProducts, freeProducts, totalPurchasePrice, promotionDiscount, membershipDiscount,
                checkoutPrice, totalPurchaseQuantity);
    }

    public List<ProductDTO> getPurchaseProducts() {
        return purchaseProducts;
    }

    public List<ProductDTO> getFreeProducts() {
        return freeProducts;
    }

    public int getTotalPurchasePrice() {
        return totalPurchasePrice;
    }

    public int getPromotionDiscount() {
        return promotionDiscount;
    }

    public int getMembershipDiscount() {
        return membershipDiscount;
    }

    public int getCheckoutPrice() {
        return checkoutPrice;
    }

    public int getTotalPurchaseQuantity() {
        return totalPurchaseQuantity;
    }
}
