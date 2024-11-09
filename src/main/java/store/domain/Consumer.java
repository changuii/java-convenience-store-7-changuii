package store.domain;

import java.util.ArrayList;
import java.util.List;

public class Consumer {
    private final List<PurchaseProduct> purchaseProducts;
    private final List<PurchaseHistory> purchaseHistories;

    private Consumer(final List<PurchaseProduct> purchaseProducts){
        this.purchaseProducts = purchaseProducts;
        this.purchaseHistories = new ArrayList<>();
    }

    public static Consumer from(final List<PurchaseProduct> purchaseProducts){
        return new Consumer(purchaseProducts);
    }


}
