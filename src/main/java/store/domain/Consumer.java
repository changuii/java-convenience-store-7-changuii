package store.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Consumer {
    private final List<PurchaseProduct> purchaseProducts;
    private final List<CompletedPurchaseHistory> purchaseHistories;

    private Consumer(final List<PurchaseProduct> purchaseProducts) {
        this.purchaseProducts = purchaseProducts;
        this.purchaseHistories = new ArrayList<>();
    }

    public static Consumer from(final List<PurchaseProduct> purchaseProducts) {
        return new Consumer(purchaseProducts);
    }

    public boolean isCurrentProductPromotionInProgress(final ProductInventory productInventory, final LocalDate today) {
        return productInventory.isPromotionInProgress(currentPurchaseProduct(), today);
    }

    public boolean isInventoryQuantityRequirementMetForApplyPromotion(final ProductInventory productInventory) {
        return productInventory.isRequirementMetForApplyPromotion(currentPurchaseProduct());
    }

    public boolean isCurrentProductQuantitySufficientForApplyPromotion(final ProductInventory productInventory) {
        return productInventory.isQuantitySufficientForApplyPromotion(currentPurchaseProduct());
    }

    public void additionCurrentProductQuantityForApplyPromotion() {
        currentPurchaseProduct().additionQuantityForApplyPromotion();
    }

    public int calculateCurrentProductQuantityAtRegularPrice(final ProductInventory productInventory) {
        return productInventory.calculateQuantityAtRegularPrice(currentPurchaseProduct());
    }

    public void deductCurrentProductQuantityAtRegularPrice(final int regularPriceQuantity) {
        currentPurchaseProduct().deductRegularPriceQuantity(regularPriceQuantity);
    }

    public void purchaseProduct(final ProductInventory productInventory) {
        productInventory.purchaseRegularPriceProduct(currentPurchaseProduct());
        addIfCurrentPurchaseProductComplete();
    }

    public void purchasePromotionProduct(final ProductInventory productInventory) {
        productInventory.purchasePromotionProduct(currentPurchaseProduct());
        productInventory.purchaseRegularPricePromotionProduct(currentPurchaseProduct());
        productInventory.purchaseRegularPriceProduct(currentPurchaseProduct());
        addIfCurrentPurchaseProductComplete();
    }

    public void purchaseRegularPricePromotionProduct(final ProductInventory productInventory) {
        productInventory.purchaseRegularPricePromotionProduct(currentPurchaseProduct());
        addIfCurrentPurchaseProductComplete();
    }

    public void addIfCurrentPurchaseProductComplete() {
        if (currentPurchaseProduct().isPurchaseCompleted()) {
            purchaseHistories.add(currentPurchaseProduct().generateCompletedPurchaseHistory());
        }
    }

    public boolean isCurrentPurchaseProductDone() {
        return currentPurchaseProduct().isPurchaseCompleted();
    }

    public void nextPurchaseProduct() {
        removePurchaseCompletedProduct();
    }

    public String currentProductName() {
        return currentPurchaseProduct().getName();
    }

    private PurchaseProduct currentPurchaseProduct() {
        return purchaseProducts.getFirst();
    }

    private void removePurchaseCompletedProduct() {
        purchaseProducts.removeFirst();
    }

    public boolean isPurchaseCompleted() {
        return purchaseProducts.isEmpty();
    }

}
