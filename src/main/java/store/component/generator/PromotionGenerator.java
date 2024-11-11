package store.component.generator;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import store.domain.product.BuyGet;
import store.domain.product.DateRange;
import store.domain.product.Promotion;
import store.enums.ErrorMessage;
import store.enums.GeneratorConstants;

public class PromotionGenerator {

    public Map<String, Optional<Promotion>> generate(final List<String> promotionLines) {
        validatePromotionLines(promotionLines);
        final Map<String, Optional<Promotion>> promotions = initPromotions();
        registerPromotions(promotions, promotionLines);
        return promotions;
    }

    private void validatePromotionLines(final List<String> promotionLines) {
        promotionLines.forEach(this::validatePromotionLineFormat);
    }

    private void validatePromotionLineFormat(final String promotionLine) {
        if (!promotionLine.matches(GeneratorConstants.PROMOTION_VALUE_REGEX.getValue())) {
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
        return new LinkedHashMap<>(Map.of("null", Optional.empty()));
    }

    private Promotion parsePromotion(final String promotion) {
        try {
            final String[] column = promotion.split(GeneratorConstants.TABLE_ROW_DELIMITER.getValue());
            return Promotion.of(column[0], BuyGet.of(parseInt(column[1]), parseInt(column[2])),
                    parseDateRange(column[3], column[4]));
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new IllegalArgumentException(ErrorMessage.INVALID_FILE_FORMAT.getMessage());
        }
    }

    private DateRange parseDateRange(final String startDate, final String endDate) {
        try {
            return DateRange.of(LocalDate.parse(startDate), LocalDate.parse(endDate));
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
