package store.domain;

import java.time.LocalDate;

public class DateRange {
    private final LocalDate startDate;
    private final LocalDate endDate;

    private DateRange(final LocalDate startDate, final LocalDate endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public static DateRange of(final LocalDate startDate, final LocalDate endDate) {
        return new DateRange(startDate, endDate);
    }

    public boolean isInRangeNow(LocalDate today) {
        return isGreaterThanStartDate(today) && isLessThanEndDate(today);
    }

    private boolean isGreaterThanStartDate(LocalDate date) {
        return startDate.isAfter(date) || startDate.equals(date);
    }

    private boolean isLessThanEndDate(LocalDate date) {
        return endDate.isBefore(date) || endDate.equals(date);
    }
}
