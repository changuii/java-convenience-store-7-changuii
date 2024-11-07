package store.dto;

import java.util.Map;

public class ConvenienceStoreDTO {
    private final Map<String, ProductDTO> products;
    private final Map<String, ProductDTO> promotionProducts;

    private ConvenienceStoreDTO(final Map<String, ProductDTO> products,
                                final Map<String, ProductDTO> promotionProducts) {
        this.products = products;
        this.promotionProducts = promotionProducts;
    }

    public static ConvenienceStoreDTO of(final Map<String, ProductDTO> products,
                                         final Map<String, ProductDTO> promotionProducts) {
        return new ConvenienceStoreDTO(products, promotionProducts);
    }

    public Map<String, ProductDTO> getProducts() {
        return products;
    }

    public Map<String, ProductDTO> getPromotionProducts() {
        return promotionProducts;
    }
}
