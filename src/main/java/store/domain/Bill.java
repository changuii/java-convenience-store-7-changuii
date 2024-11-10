package store.domain;

import java.util.ArrayList;
import java.util.List;

public class Bill {
    private final List<CompletedPurchaseHistory> completedPurchaseHistories;
    private int totalPurchasePrice;
    private int totalAllQuantity;
    private int totalPurchasePriceAtRegularPrice;
    private int promotionDiscount;
    private int membershipDiscount;
    private int checkoutPrice;

    private Bill(final List<CompletedPurchaseHistory> completedPurchaseHistories) {
        this.completedPurchaseHistories = new ArrayList<>();
        totalPurchasePrice = 0;
        totalAllQuantity = 0;
        promotionDiscount = 0;
        membershipDiscount = 0;
        checkoutPrice = 0;
        settleCompletedPurchaseHistories(completedPurchaseHistories);
    }

    public static Bill from(final List<CompletedPurchaseHistory> completedPurchaseHistories) {
        return new Bill(completedPurchaseHistories);
    }

    private void settleCompletedPurchaseHistories(final List<CompletedPurchaseHistory> completedPurchaseHistories) {
        completedPurchaseHistories.forEach(this::additionCompletedPurchaseHistory);
    }

    private void additionCompletedPurchaseHistory(final CompletedPurchaseHistory completedPurchaseHistory) {
        totalPurchasePrice = completedPurchaseHistory.getSumOfTotalPurchasePrice(totalPurchasePrice);
        totalAllQuantity = completedPurchaseHistory.getSumOfAllPurchaseQuantity(totalAllQuantity);
        promotionDiscount = completedPurchaseHistory.getSumOfTotalPromotionDiscount(promotionDiscount);
        totalPurchasePriceAtRegularPrice = completedPurchaseHistory.getSumOfTotalPurchasePriceAtRegularPrice(
                totalPurchasePriceAtRegularPrice);
        completedPurchaseHistories.add(completedPurchaseHistory);
    }

    public void discountMembership(final double membershipPercentage, final int maxDiscount) {
        if (totalPurchasePriceAtRegularPrice * membershipPercentage > maxDiscount) {
            membershipDiscount = maxDiscount;
            return;
        }
        membershipDiscount = (int) Math.round(totalPurchasePriceAtRegularPrice * membershipPercentage);
    }

    public void calculateCheckoutPrice() {
        checkoutPrice = totalPurchasePrice - promotionDiscount - membershipDiscount;
    }
}
