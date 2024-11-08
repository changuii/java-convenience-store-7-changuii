package store.service;

import store.domain.ProductInventory;

public class ConvenienceStoreService {
    private final ProductInventory productInventory;

    private ConvenienceStoreService(final ProductInventory productInventory) {
        this.productInventory = productInventory;
    }

    public static ConvenienceStoreService from(final ProductInventory productInventory) {
        return new ConvenienceStoreService(productInventory);
    }

    public ProductInventory getProductInventory() {
        return productInventory;
    }

}
