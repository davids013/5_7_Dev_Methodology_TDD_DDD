package task2.domain.objects.value_objects;

public class Medicine {
    private final String title;
    private final int volume;
    private final int price;

    public Medicine(String title, int volume, int price) {
        this.title = title;
        this.volume = volume;
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public int getVolume() {
        return volume;
    }

    public int getPrice() {
        return price;
    }
}
