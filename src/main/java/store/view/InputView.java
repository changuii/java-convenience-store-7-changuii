package store.view;

import camp.nextstep.edu.missionutils.Console;
import java.util.List;
import store.dto.PurchaseProductDTO;

public class InputView {

    private final InputValidator inputValidator;
    private final InputParser inputParser;

    public InputView(InputValidator inputValidator, InputParser inputParser) {
        this.inputValidator = inputValidator;
        this.inputParser = inputParser;
    }

    public List<PurchaseProductDTO> readPurchaseProducts() {
        String input = Console.readLine();
        inputValidator.validatePurchaseProducts(input);
        List<PurchaseProductDTO> purchaseProductDTOs = inputParser.parsePurchaseProducts(input);
        inputValidator.validatePurchaseProductDTOs(purchaseProductDTOs);
        return purchaseProductDTOs;
    }

    public String readAnswer() {
        String input = Console.readLine();
        inputValidator.validateAnswer(input);
        return input;
    }

}
