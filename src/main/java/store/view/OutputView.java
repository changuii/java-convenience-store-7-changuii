package store.view;

import java.text.DecimalFormat;
import store.dto.ConvenienceStoreDTO;
import store.dto.ProductDTO;

public class OutputView {

    private static final String WELCOME_MESSAGE = "안녕하세요. W편의점입니다.";
    private static final String INTRODUCE_STORE_PRODUCTS_MESSAGE = "현재 보유하고 있는 상품입니다.";
    private static final String PRODUCTS_FORMAT = "- %s %s원 %s %s";
    private static final String PRODUCT_QUANTITY_FORMAT = "%d개";
    private static final String PRODUCT_QUANTITY_ZERO = "재고 없음";
    private static final DecimalFormat DECIMAL_FORMATTER = new DecimalFormat("###,###,###,###");
    private static final String CONTINUE_CHECKOUT_MESSAGE = "감사합니다. 구매하고 싶은 다른 상품이 있나요? (Y/N)";


    public void printWelcomeMessage() {
        System.out.println(WELCOME_MESSAGE);
    }

    public void printStoreIntroduce(final ConvenienceStoreDTO convenienceStoreDTO) {
        System.out.println(INTRODUCE_STORE_PRODUCTS_MESSAGE);
        printLineBreak();
        printStoreProducts(convenienceStoreDTO);
        printLineBreak();
    }

    public void printRequestContinueCheckoutMessage() {
        System.out.println(CONTINUE_CHECKOUT_MESSAGE);
    }


    private void printStoreProducts(final ConvenienceStoreDTO convenienceStoreDTO) {
        convenienceStoreDTO.getProductInventory().forEach(this::printProduct);
    }

    private void printProduct(final ProductDTO productDTO) {
        String price = DECIMAL_FORMATTER.format(productDTO.getPrice());
        String quantity = formatProductQuantity(productDTO.getQuantity());
        System.out.println(
                String.format(PRODUCTS_FORMAT, productDTO.getName(), price, quantity, productDTO.getPromotion()));
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
