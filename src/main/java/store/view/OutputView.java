package store.view;

import java.text.DecimalFormat;
import java.util.Map;
import store.dto.ProductInfoDTO;
import store.dto.ProductInventoryDTO;
import store.dto.ProductQuantityDTO;
import store.dto.PromotionProductQuantityDTO;

public class OutputView {

    private static final String WELCOME_MESSAGE = "안녕하세요. W편의점입니다.";
    private static final String INTRODUCE_STORE_PRODUCTS_MESSAGE = "현재 보유하고 있는 상품입니다.";
    private static final String PRODUCT_INFO_FORMAT = "- %s %s원 ";
    private static final String PRODUCT_QUANTITY_FORMAT = "%d개 ";
    private static final String PRODUCT_QUANTITY_ZERO = "재고 없음 ";
    private static final DecimalFormat DECIMAL_FORMATTER = new DecimalFormat("###,###,###,###");
    private static final String PURCHASE_PRODUCTS_INPUT_MESSAGE = "구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])";
    private static final String CONTINUE_CHECKOUT_MESSAGE = "감사합니다. 구매하고 싶은 다른 상품이 있나요? (Y/N)";


    public void printWelcomeMessage() {
        System.out.println(WELCOME_MESSAGE);
    }

    public void printRequestContinueCheckoutMessage() {
        System.out.println(CONTINUE_CHECKOUT_MESSAGE);
    }

    public void printErrorMessage(final IllegalArgumentException e) {
        System.out.println(e.getMessage());
    }

    public void printStoreIntroduce(final ProductInventoryDTO productInventoryDTO) {
        System.out.println(INTRODUCE_STORE_PRODUCTS_MESSAGE);
        printLineBreak();
        printStoreProducts(productInventoryDTO);
        printLineBreak();
    }

    public void printPurchaseProductsInputMessage() {
        System.out.println(PURCHASE_PRODUCTS_INPUT_MESSAGE);
    }


    private void printStoreProducts(final ProductInventoryDTO productInventoryDTO) {
        Map<String, ProductInfoDTO> infos = productInventoryDTO.getInfos();
        Map<String, ProductQuantityDTO> quantities = productInventoryDTO.getQuantities();
        Map<String, PromotionProductQuantityDTO> promotionQuantities = productInventoryDTO.getPromotionQuantities();

        quantities.keySet().forEach(productName -> {
            printPromotionProduct(infos, promotionQuantities, productName);
            printProduct(infos, quantities, productName);
        });
    }

    private void printProduct(final Map<String, ProductInfoDTO> infos, final Map<String, ProductQuantityDTO> quantities,
                              final String productName) {
        printProductInfo(infos.get(productName));
        printProductQuantity(quantities.get(productName));
        printLineBreak();
    }

    private void printPromotionProduct(final Map<String, ProductInfoDTO> infos,
                                       final Map<String, PromotionProductQuantityDTO> promotionQuantities,
                                       final String productName) {
        if (promotionQuantities.containsKey(productName)) {
            printProductInfo(infos.get(productName));
            printPromotionQuantity(promotionQuantities.get(productName));
            printLineBreak();
        }
    }


    private void printPromotionQuantity(final PromotionProductQuantityDTO promotionProductQuantityDTO) {
        printProductQuantity(promotionProductQuantityDTO.getQuantity());
        System.out.print(promotionProductQuantityDTO.getPromotion());
    }

    private void printProductQuantity(final ProductQuantityDTO productQuantityDTO) {
        System.out.print(formatProductQuantity(productQuantityDTO.getQuantity()));
    }

    private void printProductInfo(final ProductInfoDTO productInfoDTO) {
        String price = DECIMAL_FORMATTER.format(productInfoDTO.getPrice());
        System.out.print(String.format(PRODUCT_INFO_FORMAT, productInfoDTO.getName(), price));
    }

    private String formatProductQuantity(int quantity) {
        if (quantity == 0) {
            return PRODUCT_QUANTITY_ZERO;
        }
        return String.format(PRODUCT_QUANTITY_FORMAT, quantity);
    }

    public void printLineBreak() {
        System.out.println();
    }

}
