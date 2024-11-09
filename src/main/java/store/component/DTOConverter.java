package store.component;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import store.domain.ProductInfo;
import store.domain.ProductInventory;
import store.domain.ProductQuantity;
import store.domain.PromotionProductQuantity;
import store.domain.PurchaseProduct;
import store.dto.ProductInfoDTO;
import store.dto.ProductInventoryDTO;
import store.dto.ProductQuantityDTO;
import store.dto.PromotionProductQuantityDTO;
import store.dto.PurchaseProductDTO;

public class DTOConverter {

    public ProductInventoryDTO convertProductInventoryDTO(final ProductInventory productInventory) {
        return ProductInventoryDTO.of(
                convertProductInfoDTOs(productInventory.getInfos()),
                convertProductQuantityDTOs(productInventory.getQuantities()),
                convertPromotionProductQuantityDTOs(productInventory.getPromotionQuantities())
        );
    }

    public List<PurchaseProduct> convertPurchaseProducts(final List<PurchaseProductDTO> purchaseProductDTOs){
        return purchaseProductDTOs.stream()
                .map(this::convertPurchaseProduct)
                .collect(Collectors.toList());
    }

    private PurchaseProduct convertPurchaseProduct(final PurchaseProductDTO purchaseProductDTO){
        return PurchaseProduct.of(purchaseProductDTO.getName(), purchaseProductDTO.getQuantity());
    }


    private Map<String, ProductInfoDTO> convertProductInfoDTOs(final Map<String, ProductInfo> infos) {
        Map<String, ProductInfoDTO> productInfoDTOs = new LinkedHashMap<>();

        infos.keySet().stream()
                .forEach(productName -> {
                    productInfoDTOs.put(productName, convertProductInfoDTO(infos.get(productName)));
                });
        return productInfoDTOs;
    }

    private Map<String, ProductQuantityDTO> convertProductQuantityDTOs(final Map<String, ProductQuantity> quantities) {
        Map<String, ProductQuantityDTO> productQuantityDTOs = new LinkedHashMap<>();

        quantities.keySet().stream()
                .forEach(productName -> {
                    productQuantityDTOs.put(productName, convertProductQuantityDTO(quantities.get(productName)));
                });
        return productQuantityDTOs;
    }

    private Map<String, PromotionProductQuantityDTO> convertPromotionProductQuantityDTOs(
            final Map<String, PromotionProductQuantity> promotionQuantities) {
        Map<String, PromotionProductQuantityDTO> promotionQuantityDTOs = new LinkedHashMap<>();

        promotionQuantities.keySet().stream()
                .forEach(productName -> {
                    promotionQuantityDTOs.put(productName,
                            convertPromotionProductQuantityDTO(promotionQuantities.get(productName)));
                });
        return promotionQuantityDTOs;
    }

    private ProductInfoDTO convertProductInfoDTO(final ProductInfo productInfo) {
        return ProductInfoDTO.of(productInfo.getName(), productInfo.getPrice());
    }

    private ProductQuantityDTO convertProductQuantityDTO(final ProductQuantity productQuantity) {
        return ProductQuantityDTO.from(productQuantity.getQuantity());
    }

    private PromotionProductQuantityDTO convertPromotionProductQuantityDTO(
            final PromotionProductQuantity promotionQuantity) {
        return PromotionProductQuantityDTO.of(promotionQuantity.getQuantity(), promotionQuantity.getPromotion());
    }
}
