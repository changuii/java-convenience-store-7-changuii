package store.dto;

import java.util.Map;

public class ProductInventoryDTO {
    private final Map<String, ProductInfoDTO> infos;
    private final Map<String, ProductQuantityDTO> quantities;
    private final Map<String, PromotionProductQuantityDTO> promotionQuantities;

    public ProductInventoryDTO(final Map<String, ProductInfoDTO> infos,
                               final Map<String, ProductQuantityDTO> quantities,
                               final Map<String, PromotionProductQuantityDTO> promotionQuantities) {
        this.infos = infos;
        this.quantities = quantities;
        this.promotionQuantities = promotionQuantities;
    }

    public static ProductInventoryDTO of(final Map<String, ProductInfoDTO> infos,
                                           final Map<String, ProductQuantityDTO> quantities,
                                           final Map<String, PromotionProductQuantityDTO> promotionQuantities) {
        return new ProductInventoryDTO(infos, quantities, promotionQuantities);
    }

    public Map<String, ProductInfoDTO> getInfos() {
        return infos;
    }

    public Map<String, ProductQuantityDTO> getQuantities() {
        return quantities;
    }

    public Map<String, PromotionProductQuantityDTO> getPromotionQuantities() {
        return promotionQuantities;
    }
}
