package task2.domain.objects.entities;

import java.time.LocalDate;
import java.util.Objects;

public class Doctor {
    private final long id;
    private final String name;
    private final LocalDate birthDate;
    private final LocalDate experienceFromDate;
    private boolean isFree;

    public Doctor(long id, String name, LocalDate birthDate, LocalDate expirienceFromDate) {
        this.id = id;
        this.name = name;
        this.birthDate = birthDate;
        this.experienceFromDate = expirienceFromDate;
        isFree = true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Doctor doctor = (Doctor) o;
        return id == doctor.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash("Doctor", id);
    }

    public void setFree(boolean free) {
        isFree = free;
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

    public LocalDate getExperienceFromDate() {
        return experienceFromDate;
    }

    public boolean isFree() {
        return isFree;
    }
}
