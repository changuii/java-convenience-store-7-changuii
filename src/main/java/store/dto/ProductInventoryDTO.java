package store.dto;

import java.util.List;

public class ProductInventoryDTO {
    private final List<ProductDTO> products;

    public ProductInventoryDTO(final List<ProductDTO> products) {
        this.products = products;
    }

    public static ProductInventoryDTO from(final List<ProductDTO> products) {
        return new ProductInventoryDTO(products);
    }

    public List<ProductDTO> getProducts() {
        return products;
    }

}
