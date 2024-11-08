package store.view;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import store.dto.PurchaseProductDTO;
import store.enums.ErrorMessage;

public class InputValidator {


    private static final String PURCHASE_PRODUCTS_FORMAT_REGEX
            = "^\\[[A-Za-z0-9가-힣]+-[0-9]+\\](,\\[[A-Za-z0-9가-힣]+-[0-9]+\\])*$";
    private static final Pattern PURCHASE_PRODUCTS_FORMAT = Pattern.compile(PURCHASE_PRODUCTS_FORMAT_REGEX);
    private static final String ANSWER_FORMAT_REGEX = "^[YN]$";
    private static final Pattern ANSWER_FORMAT = Pattern.compile(ANSWER_FORMAT_REGEX);
    private static final int PURCHASE_QUANTITY_MIN = 0;


    public void validatePurchaseProducts(String products) {
        if (!PURCHASE_PRODUCTS_FORMAT.matcher(products).matches()) {
            throw new IllegalArgumentException(ErrorMessage.INVALID_FORMAT.getMessage());
        }
    }

    public void validatePurchaseProductDTOs(List<PurchaseProductDTO> purchaseProductDTOs) {
        validateDuplicationPurchaseProductDTOs(purchaseProductDTOs);
        purchaseProductDTOs.forEach(purchaseProductDTO -> {
            validatePurchaseQuantityLessThanMin(purchaseProductDTO);
        });
    }

    private void validateDuplicationPurchaseProductDTOs(List<PurchaseProductDTO> purchaseProductDTOs) {
        if (purchaseProductDTOs.stream().map(PurchaseProductDTO::getName).distinct().count()
                != purchaseProductDTOs.size()) {
            throw new IllegalArgumentException(ErrorMessage.INVALID_FORMAT.getMessage());
        }
    }

    private void validatePurchaseQuantityLessThanMin(PurchaseProductDTO purchaseProductDTO){
        if(purchaseProductDTO.getQuantity() <= PURCHASE_QUANTITY_MIN){
            throw new IllegalArgumentException(ErrorMessage.INVALID_FORMAT.getMessage());
        }
    }

    public void validateAnswer(String answer) {
        if (!ANSWER_FORMAT.matcher(answer).matches()) {
            throw new IllegalArgumentException(ErrorMessage.INVALID_FORMAT.getMessage());
        }
    }

}
