package store.view;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import store.dto.PurchaseProductDTO;
import store.enums.ErrorMessage;

public class InputParserTest {

    private final InputParser inputParser;

    public InputParserTest() {
        this.inputParser = new InputParser();
    }

    private static Stream<Arguments> provideProductsAndParsingProducts() {
        return Stream.of(
                Arguments.of("[감자-1]",
                        List.of(PurchaseProductDTO.of("감자", 1))),
                Arguments.of("[감자-1],[김치-1]",
                        List.of(PurchaseProductDTO.of("감자", 1),
                                PurchaseProductDTO.of("김치", 1))),
                Arguments.of("[감자-1],[김치-2],[꽁치-3]",
                        List.of(PurchaseProductDTO.of("감자", 1),
                                PurchaseProductDTO.of("김치", 2),
                                PurchaseProductDTO.of("꽁치", 3))));
    }

    @ParameterizedTest
    @ValueSource(strings = {"[감자-10000000000]", "[김치-2147483648]"})
    void 상품의_수량이_int타입의_범위를_넘어가면_예외가_발생한다(String products) {
        assertThatThrownBy(() -> inputParser.parsePurchaseProducts(products))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ErrorMessage.INVALID_FORMAT.getMessage());
    }

    @ParameterizedTest
    @MethodSource("provideProductsAndParsingProducts")
    void 상품들을_나타내는_문자열을_상품DTO_리스트로_파싱한다(String products, List<PurchaseProductDTO> expectedProducts) {
        List<PurchaseProductDTO> actualProducts = inputParser.parsePurchaseProducts(products);

        assertThat(actualProducts)
                .usingRecursiveComparison()
                .isEqualTo(expectedProducts);
    }


}
