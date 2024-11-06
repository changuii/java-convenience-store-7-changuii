package store.dto;

public class ProductDTO {
    private final String name;
    private final int count;

    private ProductDTO(final String name, final int count) {
        this.name = name;
        this.count = count;
    }

    public static ProductDTO of(final String name, final int count) {
        return new ProductDTO(name, count);
    }

    public String getName(){
        return name;
    }

    public int getCount(){
        return count;
    }
}
