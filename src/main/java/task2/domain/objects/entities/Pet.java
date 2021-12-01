package task2.domain.objects.entities;

import task2.domain.objects.aggregates.Appointment;
import task2.domain.objects.value_objects.Species;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

public class Pet {
    private final long id;
    private final String name;
    private final LocalDate birthDate;
    private final Species species;
    private final Client client;
    private final List<Appointment> history;

    public Pet(long id, String name, LocalDate birthDate, Species species, Client client) {
        this.id = id;
        this.name = name;
        this.birthDate = birthDate;
        this.species = species;
        this.client = client;
        history = new CopyOnWriteArrayList<>();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pet pet = (Pet) o;
        return id == pet.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash("Pet", id);
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public Species getSpecies() {
        return species;
    }

    public Client getClient() {
        return client;
    }

    public List<Appointment> getMedicalHistory() {
        return new ArrayList<>(history);
    }

    public void addToHistory(Appointment appointment) {
        history.add(appointment);
    }
}
