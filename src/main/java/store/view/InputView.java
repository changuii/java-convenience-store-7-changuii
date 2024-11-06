package store.view;

import camp.nextstep.edu.missionutils.Console;
import java.util.List;
import store.dto.ProductDTO;

public class InputView {

    private final InputValidator inputValidator;
    private final InputParser inputParser;

    public InputView(InputValidator inputValidator, InputParser inputParser) {
        this.inputValidator = inputValidator;
        this.inputParser = inputParser;
    }

    public List<ProductDTO> readProducts() {
        String input = Console.readLine();
        inputValidator.validateProducts(input);
        return inputParser.parseProducts(input);
    }

}
