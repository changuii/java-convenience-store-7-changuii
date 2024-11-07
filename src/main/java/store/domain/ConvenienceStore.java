package store.domain;

import java.util.LinkedHashMap;
import java.util.Map;
import store.dto.ConvenienceStoreDTO;
import store.dto.ProductDTO;

public class ConvenienceStore {
    private final Map<String, Product> products;
    private final Map<String, Product> promotionProducts;

    private ConvenienceStore(final Map<String, Product> products, final Map<String, Product> promotionProducts) {
        this.products = products;
        this.promotionProducts = promotionProducts;
    }

    public static ConvenienceStore of(final Map<String, Product> products,
                                      final Map<String, Product> promotionProducts) {
        return new ConvenienceStore(products, promotionProducts);
    }


    public ConvenienceStoreDTO toDTO() {
        Map<String, ProductDTO> productDTOs = new LinkedHashMap<>();
        Map<String, ProductDTO> promotionProductDTOs = new LinkedHashMap<>();

        products.keySet().stream()
                .forEach(productName -> registerProductDTO(productDTOs, promotionProductDTOs, productName));
        return ConvenienceStoreDTO.of(productDTOs, promotionProductDTOs);
    }

    private void registerProductDTO(final Map<String, ProductDTO> productDTOs,
                                    final Map<String, ProductDTO> promotionProductDTOs, String productName) {
        productDTOs.put(productName, products.get(productName).toDTO());
        if (promotionProducts.containsKey(productName)) {
            promotionProductDTOs.put(productName, promotionProducts.get(productName).toDTO());
        }
    }


}
