package store.dto;

public class PurchaseProductDTO {
    private final String name;
    private final int count;

    private PurchaseProductDTO(final String name, final int count) {
        this.name = name;
        this.count = count;
    }

    public static PurchaseProductDTO of(final String name, final int count) {
        return new PurchaseProductDTO(name, count);
    }

    public String getName() {
        return name;
    }

    public int getCount() {
        return count;
    }
}
