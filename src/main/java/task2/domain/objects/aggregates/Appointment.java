package task2.domain.objects.aggregates;

import task2.domain.objects.entities.Disease;
import task2.domain.objects.entities.Doctor;
import task2.domain.objects.entities.Pet;
import task2.domain.objects.value_objects.Medicine;
import task2.domain.objects.value_objects.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Objects;
import java.util.concurrent.Callable;

public class Appointment implements Callable {
    private final int BASIC_TIME = 1_000;
    private final LocalDateTime dateTime;
    private final long id;
    private final Doctor doctor;
    private final Pet pet;
    private final Service service;
    private final Medicine medicine;
    private final int price;
    private final Disease disease;

    public Appointment(long id, Doctor doctor, Pet pet, Service service, Medicine medicine, Disease disease) {
        dateTime = LocalDateTime.now();
        this.id = id;
        this.doctor = doctor;
        this.pet = pet;
        this.service = service;
        this.medicine = medicine;
        this.disease = disease;
        price = service.getPrice() + ((medicine == null) ? 0 : medicine.getPrice());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Appointment that = (Appointment) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash("Appointment", id);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Appointment{");
        final DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT);
        sb.append("dateTime=").append(dateTime.format(formatter));
        sb.append(", id=").append(id);
        sb.append(", doctor=").append(doctor.getName());
        sb.append(", pet=").append(pet.getName());
        sb.append(", service=").append(service.getName());
        sb.append(", medicine=").append((medicine != null) ? medicine.getTitle() : "no");
        sb.append(", price=").append(price);
        sb.append(", disease=").append(disease.toString());
        sb.append('}');
        return sb.toString();
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public long getId() {
        return id;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public Pet getPet() {
        return pet;
    }

    public Service getService() {
        return service;
    }

    public Medicine getMedicine() {
        return medicine;
    }

    public int getPrice() {
        return price;
    }

    public Disease getDisease() {
        return disease;
    }

    @Override
    public Appointment call() {
        System.out.printf("Врач '%s' оказывает услугу '%s' питомцу '%s' клиента '%s'%n",
                doctor.getName(), service.getName(), pet.getName(), pet.getClient().getName());


        try {
            Thread.sleep(BASIC_TIME + (int) (Math.random() * BASIC_TIME / 5));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        doctor.setFree(true);
        System.out.printf("Врач '%s' освободился%n", doctor.getName());
        return this;
    }
}
