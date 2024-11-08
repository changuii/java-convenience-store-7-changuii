package store.domain;

public class ConvenienceStore {
    private final ProductInventory productInventory;

    private ConvenienceStore(final ProductInventory productInventory) {
        this.productInventory = productInventory;
    }

    public static ConvenienceStore from(final ProductInventory productInventory) {
        return new ConvenienceStore(productInventory);
    }

    public ProductInventory getProductInventory() {
        return productInventory;
    }

}
