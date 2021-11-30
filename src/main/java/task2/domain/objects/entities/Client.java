package task2.domain.objects.entities;

import java.time.LocalDate;
import java.util.Objects;

public class Client {
    private final long id;
    private final String name;
    private final String phoneNumber;
    private final LocalDate registrationDate;

    public Client(long id, String name, String phoneNumber, LocalDate registrationDate) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.registrationDate = registrationDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return id == client.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public LocalDate getRegistrationDate() {
        return registrationDate;
    }
}
