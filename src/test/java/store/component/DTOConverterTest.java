package store.component;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import store.domain.BuyGet;
import store.domain.DateRange;
import store.domain.ProductInfo;
import store.domain.ProductInventory;
import store.domain.ProductQuantity;
import store.domain.Promotion;
import store.domain.PromotionProductQuantity;
import store.dto.ProductInfoDTO;
import store.dto.ProductInventoryDTO;
import store.dto.ProductQuantityDTO;
import store.dto.PromotionProductQuantityDTO;

public class DTOConverterTest {

    private final DTOConverter dtoConverter;
    private final Map<String, ProductInfo> infos;
    private final Map<String, ProductQuantity> quantities;
    private final Map<String, PromotionProductQuantity> promotionQuantities;
    private final Map<String, ProductInfoDTO> infoDTOs;
    private final Map<String, ProductQuantityDTO> quantityDTOs;
    private final Map<String, PromotionProductQuantityDTO> promotionQuantityDTOs;

    public DTOConverterTest() {
        dtoConverter = new DTOConverter();
        this.infos = new LinkedHashMap<>();
        this.quantities = new LinkedHashMap<>();
        this.promotionQuantities = new LinkedHashMap<>();
        this.infoDTOs = new LinkedHashMap<>();
        this.quantityDTOs = new LinkedHashMap<>();
        this.promotionQuantityDTOs = new LinkedHashMap<>();
    }

    @BeforeEach
    void init() {
        infos.clear();
        quantities.clear();
        promotionQuantities.clear();
        infoDTOs.clear();
        quantityDTOs.clear();
        promotionQuantityDTOs.clear();
    }


    @DisplayName("ProductInventory를 ProductInventoryDTO로 변환 테스트")
    @ParameterizedTest
    @CsvSource(value = {"콜라:1000:10:5", "김치:1000:10:5", "콜라:200:10:5", "콜라:1000:0:5", "콜라:1000:10:0"}, delimiter = ':')
    void convertProductInventoryDTO(final String productName, final int productPrice, final int quantity,
                                    final int promotionQuantity) {
        ProductInventory inventory = createProductInventory(productName, productPrice, quantity, promotionQuantity);
        ProductInventoryDTO expected = createProductInventoryDTO(productName, productPrice, quantity,
                promotionQuantity);

        ProductInventoryDTO actual = dtoConverter.convertProductInventoryDTO(inventory);

        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    private ProductInventory createProductInventory(final String productName, final int productPrice,
                                                    final int quantity, final int promotionQuantity) {
        infos.put(productName, ProductInfo.of(productName, productPrice));
        quantities.put(productName, ProductQuantity.from(quantity));
        promotionQuantities.put(productName, PromotionProductQuantity.of(promotionQuantity, emptyPromotion()));
        return ProductInventory.of(infos, quantities, promotionQuantities);
    }

    private ProductInventoryDTO createProductInventoryDTO(final String productName, final int productPrice,
                                                          final int quantity, final int promotionQuantity) {
        infoDTOs.put(productName, ProductInfoDTO.of(productName, productPrice));
        quantityDTOs.put(productName, ProductQuantityDTO.from(quantity));
        promotionQuantityDTOs.put(productName, PromotionProductQuantityDTO.of(promotionQuantity,
                emptyPromotion().getPromotionName()));
        return ProductInventoryDTO.of(infoDTOs, quantityDTOs, promotionQuantityDTOs);
    }


    private Promotion emptyPromotion() {
        return Promotion.of("프로모션", BuyGet.of(1, 1), DateRange.of(LocalDate.MIN, LocalDate.MAX));
    }
}
