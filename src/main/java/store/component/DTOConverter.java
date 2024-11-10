package store.component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import store.domain.Bill;
import store.domain.CompletedPurchaseHistory;
import store.domain.product.ProductInfo;
import store.domain.ProductInventory;
import store.domain.product.ProductQuantity;
import store.domain.product.PromotionProductQuantity;
import store.domain.PurchaseProduct;
import store.dto.BillDTO;
import store.dto.ProductDTO;
import store.dto.ProductInventoryDTO;
import store.dto.PurchaseProductDTO;

public class DTOConverter {

    private static final String EMPTY = "";
    private static final int EMPTY_NUMBER = 0;

    public ProductInventoryDTO convertProductInventoryDTO(final ProductInventory productInventory) {
        return ProductInventoryDTO.from(convertProductDTOs(productInventory));
    }

    public List<PurchaseProduct> convertPurchaseProducts(final List<PurchaseProductDTO> purchaseProductDTOs) {
        return purchaseProductDTOs.stream()
                .map(this::convertPurchaseProduct)
                .collect(Collectors.toList());
    }

    public BillDTO convertBillDTO(final Bill bill) {
        List<CompletedPurchaseHistory> purchaseHistories = bill.getCompletedPurchaseHistories();
        int totalPurchasePrice = bill.getTotalPurchasePrice();
        int promotionDiscount = bill.getPromotionDiscount();
        int membershipDiscount = bill.getMembershipDiscount();
        int checkoutPrice = bill.getCheckoutPrice();
        int totalAllQuantity = bill.getTotalAllQuantity();
        List<ProductDTO> purchaseProducts = convertCompletedPurchaseHistoriesToPurchaseProducts(purchaseHistories);
        List<ProductDTO> freeProducts = convertCompletedPurchaseHistoriesToFreeProducts(purchaseHistories);

        return BillDTO.of(purchaseProducts, freeProducts, totalPurchasePrice, promotionDiscount, membershipDiscount,
                checkoutPrice, bill.getTotalAllQuantity());
    }

    private List<ProductDTO> convertCompletedPurchaseHistoriesToPurchaseProducts(
            final List<CompletedPurchaseHistory> completedPurchaseHistories) {
        return completedPurchaseHistories.stream()
                .map(this::convertCompletedPurchaseHistoryToPurchaseProduct)
                .toList();
    }

    private ProductDTO convertCompletedPurchaseHistoryToPurchaseProduct(
            final CompletedPurchaseHistory completedPurchaseHistory) {
        return ProductDTO.of(
                completedPurchaseHistory.getProductName(),
                completedPurchaseHistory.calculateProductPrice(),
                completedPurchaseHistory.getAllQuantity(),
                EMPTY
        );
    }

    private List<ProductDTO> convertCompletedPurchaseHistoriesToFreeProducts(
            final List<CompletedPurchaseHistory> completedPurchaseHistories) {
        return completedPurchaseHistories.stream()
                .map(this::convertCompletedPurchaseHistoryToFreeProduct)
                .filter(this::freeProductExsistsCondition)
                .toList();
    }

    private boolean freeProductExsistsCondition(ProductDTO freeProduct){
        return freeProduct.getQuantity() != EMPTY_NUMBER;
    }

    private ProductDTO convertCompletedPurchaseHistoryToFreeProduct(
            final CompletedPurchaseHistory completedPurchaseHistory) {
        return ProductDTO.of(
                completedPurchaseHistory.getProductName(), EMPTY_NUMBER,
                completedPurchaseHistory.getFreeQuantity(), EMPTY
        );
    }

    private PurchaseProduct convertPurchaseProduct(final PurchaseProductDTO purchaseProductDTO) {
        return PurchaseProduct.of(purchaseProductDTO.getName(), purchaseProductDTO.getQuantity());
    }

    private List<ProductDTO> convertProductDTOs(final ProductInventory productInventory) {
        List<ProductDTO> productDTOs = new ArrayList<>();
        productInventory.getInfos().stream().forEach(productInfo -> {
            addPromotionProductQuantity(productInventory.getPromotionQuantities(), productInfo, productDTOs);
            addProductQuantity(productInventory.getQuantities(), productInfo, productDTOs);
        });
        return productDTOs;
    }

    private void addProductQuantity(final List<ProductQuantity> productQuantities, final ProductInfo productInfo,
                                    final List<ProductDTO> productDTOs) {
        productQuantities.stream()
                .filter(productQuantity -> productQuantity.isMatchProductName(productInfo.getName()))
                .map(productQuantity -> convertProductDTO(productQuantity, productInfo))
                .findAny()
                .ifPresent(productDTOs::add);
    }

    private void addPromotionProductQuantity(final List<PromotionProductQuantity> promotionProductQuantities,
                                             final ProductInfo productInfo, final List<ProductDTO> productDTOs) {
        promotionProductQuantities.stream()
                .filter(productQuantity -> productQuantity.isMatchProductName(productInfo.getName()))
                .map(productQuantity -> convertProductDTO(productQuantity, productInfo))
                .findAny()
                .ifPresent(productDTOs::add);
    }

    private ProductDTO convertProductDTO(final ProductQuantity productQuantity, final ProductInfo productInfo) {
        return ProductDTO.of(productInfo.getName(), productInfo.getPrice(), productQuantity.getQuantity(), EMPTY);
    }

    private ProductDTO convertProductDTO(final PromotionProductQuantity promotionProductQuantity,
                                         final ProductInfo productInfo) {
        return ProductDTO.of(productInfo.getName(), productInfo.getPrice(), promotionProductQuantity.getQuantity(),
                promotionProductQuantity.getPromotion());
    }

}
