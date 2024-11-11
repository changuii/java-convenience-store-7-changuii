package store.component;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import store.Constants;
import store.domain.Bill;
import store.domain.CompletedPurchaseHistory;
import store.domain.PurchaseProduct;
import store.domain.product.BuyGet;
import store.domain.product.DateRange;
import store.domain.product.ProductInfo;
import store.domain.ProductInventory;
import store.domain.product.ProductQuantity;
import store.domain.product.Promotion;
import store.domain.product.PromotionProductQuantity;
import store.dto.BillDTO;
import store.dto.ProductDTO;
import store.dto.ProductInventoryDTO;
import store.dto.PurchaseProductDTO;

public class DTOConverterTest {
    private final DTOConverter dtoConverter;
    private List<ProductInfo> productInfos;
    private List<ProductQuantity> productQuantities;
    private List<PromotionProductQuantity> promotionProductQuantities;

    public DTOConverterTest() {
        dtoConverter = new DTOConverter();
        productInfos = new ArrayList<>();
        productQuantities = new ArrayList<>();
        promotionProductQuantities = new ArrayList<>();
    }

    @BeforeEach
    void init() {
        productInfos.clear();
        productQuantities.clear();
        promotionProductQuantities.clear();
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

    @DisplayName("PurchaseProductDTO를 PurchaseProduct로 변환 테스트")
    @Test
    void convertPurchaseProducts() {
        List<PurchaseProductDTO> purchaseProductDTOs = List.of(
                PurchaseProductDTO.of(Constants.PROMOTION_NAME, Constants.PRODUCT_QUANTITY_VALUE));
        List<PurchaseProduct> expected = List.of(
                PurchaseProduct.of(Constants.PROMOTION_NAME, Constants.PRODUCT_QUANTITY_VALUE));

        List<PurchaseProduct> actual = dtoConverter.convertPurchaseProducts(purchaseProductDTOs);

        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @DisplayName("Bill을 BillDTO로 변환 테스트")
    @Test
    void convertBillDTO() {
        Bill bill = Bill.from(List.of(CompletedPurchaseHistory.of(Constants.PRODUCT_NAME, 6000, 2, 4, 2)));
        BillDTO expected = BillDTO.of(
                List.of(ProductDTO.of(Constants.PRODUCT_NAME, 1000, 8, "")),
                List.of(ProductDTO.of(Constants.PRODUCT_NAME, 0, 2, "")), 8000, 2000, 0, 6000, 8);

        BillDTO actual = dtoConverter.convertBillDTO(bill);

        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }


    private ProductInventory createProductInventory(final String productName, final int productPrice,
                                                    final int quantity, final int promotionQuantity) {
        productInfos.add(ProductInfo.of(productName, productPrice));
        productQuantities.add(ProductQuantity.of(productName, quantity));
        promotionProductQuantities.add(PromotionProductQuantity.of(productName, promotionQuantity, emptyPromotion()));
        return ProductInventory.of(productInfos, productQuantities, promotionProductQuantities);
    }

    private ProductInventoryDTO createProductInventoryDTO(final String productName, final int productPrice,
                                                          final int quantity, final int promotionQuantity) {
        ProductDTO promotion = ProductDTO.of(productName, productPrice, promotionQuantity, Constants.PROMOTION_NAME);
        ProductDTO product = ProductDTO.of(productName, productPrice, quantity, Constants.EMPTY);
        return ProductInventoryDTO.from(List.of(promotion, product));
    }


    private Promotion emptyPromotion() {
        return Promotion.of(Constants.PROMOTION_NAME, BuyGet.of(1, 1), DateRange.of(LocalDate.MIN, LocalDate.MAX));
    }
}
