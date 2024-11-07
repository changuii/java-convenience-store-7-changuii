package store.dto;

import java.util.List;

public class ConvenienceStoreDTO {
    private final List<ProductDTO> productInventory;

    private ConvenienceStoreDTO(final List<ProductDTO> productInventory) {
        this.productInventory = productInventory;
    }

    public static ConvenienceStoreDTO from(final List<ProductDTO> productInventory) {
        return new ConvenienceStoreDTO(productInventory);
    }

    public List<ProductDTO> getProductInventory() {
        return productInventory;
    }
}
