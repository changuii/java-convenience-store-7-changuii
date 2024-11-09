package store.component.generator;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import store.domain.product.BuyGet;
import store.domain.product.DateRange;
import store.domain.product.Promotion;
import store.enums.ErrorMessage;
import store.enums.StoreConfig;

public class PromotionGenerator {

    public Map<String, Optional<Promotion>> generate(final List<String> promotionLines) {
        validatePromotionLines(promotionLines);
        Map<String, Optional<Promotion>> promotions = initPromotions();
        registerPromotions(promotions, promotionLines);

        return promotions;
    }

    private void validatePromotionLines(final List<String> promotionLines) {
        promotionLines.forEach(this::validatePromotionLineFormat);
    }

    private void validatePromotionLineFormat(final String promotionLine) {
        if (!promotionLine.matches(StoreConfig.PROMOTION_VALUE_REGEX.getValue())) {
            throw new IllegalArgumentException(ErrorMessage.INVALID_FILE_FORMAT.getMessage());
        }
    }

    private void registerPromotions(final Map<String, Optional<Promotion>> promotions,
                                    final List<String> promotionLines) {
        promotionLines.stream()
                .map(this::parsePromotion)
                .forEach(promotion -> promotions.put(promotion.getPromotionName(), Optional.of(promotion)));
    }

    private Map<String, Optional<Promotion>> initPromotions() {
        Map<String, Optional<Promotion>> promotions = new HashMap<>();
        promotions.put("null", Optional.empty());
        return promotions;
    }

    private Promotion parsePromotion(final String promotion) {
        try {
            String[] column = promotion.split(StoreConfig.TABLE_ROW_DELIMITER.getValue());
            String promotionName = column[0];
            BuyGet buyGet = BuyGet.of(parseInt(column[1]), parseInt(column[2]));
            DateRange dateRange = parseDateRange(column[3], column[4]);

            return Promotion.of(promotionName, buyGet, dateRange);
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new IllegalArgumentException(ErrorMessage.INVALID_FILE_FORMAT.getMessage());
        }
    }

    private DateRange parseDateRange(final String startDate, final String endDate) {
        try {
            LocalDate start = LocalDate.parse(startDate);
            LocalDate end = LocalDate.parse(endDate);
            return DateRange.of(start, end);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException(ErrorMessage.INVALID_FILE_FORMAT.getMessage());
        }
    }

    private int parseInt(final String number) {
        try {
            return Integer.parseInt(number);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(ErrorMessage.INVALID_FILE_FORMAT.getMessage());
        }
    }

}
