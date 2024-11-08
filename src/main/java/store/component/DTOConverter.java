package store.component;

import java.util.LinkedHashMap;
import java.util.Map;
import store.domain.ProductInfo;
import store.domain.ProductInventory;
import store.domain.ProductQuantity;
import store.domain.PromotionProductQuantity;
import store.dto.ProductInfoDTO;
import store.dto.ProductInventoryDTO;
import store.dto.ProductQuantityDTO;
import store.dto.PromotionProductQuantityDTO;

public class DTOConverter {

    public ProductInventoryDTO convertProductInventoryDTO(ProductInventory productInventory) {
        return ProductInventoryDTO.from(
                convertProductInfoDTOs(productInventory.getInfos()),
                convertProductQuantityDTOs(productInventory.getQuantities()),
                convertPromotionProductQuantityDTOs(productInventory.getPromotionQuantities())
        );
    }

    private Map<String, ProductInfoDTO> convertProductInfoDTOs(Map<String, ProductInfo> infos) {
        Map<String, ProductInfoDTO> productInfoDTOs = new LinkedHashMap<>();

        infos.keySet().stream()
                .forEach(productName -> {
                    productInfoDTOs.put(productName, convertProductInfoDTO(infos.get(productName)));
                });
        return productInfoDTOs;
    }

    private Map<String, ProductQuantityDTO> convertProductQuantityDTOs(Map<String, ProductQuantity> quantities) {
        Map<String, ProductQuantityDTO> productQuantityDTOs = new LinkedHashMap<>();

        quantities.keySet().stream()
                .forEach(productName -> {
                    productQuantityDTOs.put(productName, convertProductQuantityDTO(quantities.get(productName)));
                });
        return productQuantityDTOs;
    }

    private Map<String, PromotionProductQuantityDTO> convertPromotionProductQuantityDTOs(
            Map<String, PromotionProductQuantity> promotionQuantities) {
        Map<String, PromotionProductQuantityDTO> promotionQuantityDTOs = new LinkedHashMap<>();

        promotionQuantities.keySet().stream()
                .forEach(productName -> {
                    promotionQuantityDTOs.put(productName,
                            convertPromotionProductQuantityDTO(promotionQuantities.get(productName)));
                });
        return promotionQuantityDTOs;
    }

    private ProductInfoDTO convertProductInfoDTO(ProductInfo productInfo) {
        return ProductInfoDTO.of(productInfo.getName(), productInfo.getPrice());
    }

    private ProductQuantityDTO convertProductQuantityDTO(ProductQuantity productQuantity) {
        return ProductQuantityDTO.from(productQuantity.getQuantity());
    }

    private PromotionProductQuantityDTO convertPromotionProductQuantityDTO(PromotionProductQuantity promotionQuantity) {
        return PromotionProductQuantityDTO.of(promotionQuantity.getQuantity(), promotionQuantity.getPromotion());
    }
}
