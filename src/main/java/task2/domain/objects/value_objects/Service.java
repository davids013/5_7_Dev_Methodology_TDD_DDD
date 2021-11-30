package task2.domain.objects.value_objects;

public class Service {
    private final String name;
    private final int price;

    public Service(String name, int price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }
}
