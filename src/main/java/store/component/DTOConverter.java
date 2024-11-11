package store.component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import store.domain.Bill;
import store.domain.CompletedPurchaseHistory;
import store.domain.product.Product;
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
        final List<CompletedPurchaseHistory> purchaseHistories = bill.getCompletedPurchaseHistories();
        return BillDTO.of(
                convertCompletedPurchaseHistoriesToPurchaseProducts(purchaseHistories),
                convertCompletedPurchaseHistoriesToFreeProducts(purchaseHistories),
                bill.getTotalPurchasePrice(), bill.getPromotionDiscount(), bill.getMembershipDiscount(),
                bill.getCheckoutPrice(), bill.getTotalAllQuantity());
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

    private boolean freeProductExsistsCondition(ProductDTO freeProduct) {
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
        final List<ProductDTO> productDTOs = new ArrayList<>();
        productInventory.getProducts().stream().forEach(product -> {
            addPromotionProductQuantity(productInventory.getPromotionQuantities(), product, productDTOs);
            addProductQuantity(productInventory.getQuantities(), product, productDTOs);
        });
        return productDTOs;
    }

    private void addProductQuantity(final List<ProductQuantity> productQuantities, final Product product,
                                    final List<ProductDTO> productDTOs) {
        productQuantities.stream()
                .filter(productQuantity -> productQuantity.isMatchProductName(product.getName()))
                .map(productQuantity -> convertProductDTO(productQuantity, product))
                .findAny()
                .ifPresent(productDTOs::add);
    }

    private void addPromotionProductQuantity(final List<PromotionProductQuantity> promotionProductQuantities,
                                             final Product product, final List<ProductDTO> productDTOs) {
        promotionProductQuantities.stream()
                .filter(productQuantity -> productQuantity.isMatchProductName(product.getName()))
                .map(productQuantity -> convertProductDTO(productQuantity, product))
                .findAny()
                .ifPresent(productDTOs::add);
    }

    private ProductDTO convertProductDTO(final ProductQuantity productQuantity, final Product product) {
        return ProductDTO.of(product.getName(), product.getPrice(), productQuantity.getQuantity(), EMPTY);
    }

    private ProductDTO convertProductDTO(final PromotionProductQuantity promotionProductQuantity,
                                         final Product product) {
        return ProductDTO.of(product.getName(), product.getPrice(), promotionProductQuantity.getQuantity(),
                promotionProductQuantity.getPromotion());
    }

}
