package store.view;

import java.text.DecimalFormat;
import java.util.Map;
import store.dto.ConvenienceStoreDTO;
import store.dto.ProductDTO;

public class OutputView {

    private static final String WELCOME_MESSAGE = "안녕하세요. W편의점입니다.";
    private static final String INTRODUCE_STORE_PRODUCTS_MESSAGE = "현재 보유하고 있는 상품입니다.";
    private static final String PRODUCTS_FORMAT = "- %s %s원 %s %s";
    private static final String PRODUCT_QUANTITY_FORMAT = "%d개";
    private static final String PRODUCT_QUANTITY_ZERO = "재고 없음";
    private static final DecimalFormat DECIMAL_FORMATTER = new DecimalFormat("###,###,###,###");


    public void printWelcomeMessage() {
        System.out.println(WELCOME_MESSAGE);
    }

    public void printStoreIntroduce(ConvenienceStoreDTO convenienceStoreDTO) {
        System.out.println(INTRODUCE_STORE_PRODUCTS_MESSAGE);
        printLineBreak();
        printStoreProducts(convenienceStoreDTO);
    }

    private void printStoreProducts(ConvenienceStoreDTO convenienceStoreDTO) {
        Map<String, ProductDTO> products = convenienceStoreDTO.getProducts();
        Map<String, ProductDTO> promotionProducts = convenienceStoreDTO.getPromotionProducts();

        products.keySet().stream()
                .forEach(productName -> printProducts(products, promotionProducts, productName));
    }

    private void printProducts(final Map<String, ProductDTO> products,
                               final Map<String, ProductDTO> promotionProducts, final String productName) {
        if (promotionProducts.containsKey(productName)) {
            printProduct(promotionProducts.get(productName));
        }
        printProduct(products.get(productName));
    }

    private void printProduct(ProductDTO productDTO) {
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

    private void printLineBreak() {
        System.out.println();
    }

}
