package task2.domain.objects.entities;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MedicalHistory {
    private final long id;
    private final Pet pet;
    private final Map<LocalDateTime, Disease> diseases;

    public MedicalHistory(long id, Pet pet) {
        this.id = id;
        this.pet = pet;
        diseases = new HashMap<>();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MedicalHistory that = (MedicalHistory) o;
        return id == that.id && pet.equals(that.pet) && Objects.equals(diseases, that.diseases);
    }

    @Override
    public int hashCode() {
        return Objects.hash("MedicalHistory", id, pet);
    }

    public long getId() {
        return id;
    }

    public Pet getPet() {
        return pet;
    }

    public Map<LocalDateTime, Disease> getDiseases() {
        return new HashMap<LocalDateTime, Disease>(diseases);
    }

    public void addDisease(LocalDateTime dateTime, Disease disease) {
        this.diseases.put(dateTime, disease);
    }
}
