package task2.domain.services;

import task2.domain.objects.aggregates.Appointment;
import task2.domain.objects.aggregates.VetClinic;
import task2.domain.objects.entities.Client;
import task2.domain.objects.entities.Doctor;
import task2.domain.objects.entities.Pet;
import task2.domain.objects.value_objects.Medicine;
import task2.domain.objects.value_objects.Service;
import task2.domain.objects.value_objects.Species;

import java.time.LocalDate;
import java.util.*;

public class VetClinicStarter {
    public static void main(String[] args) throws InterruptedException {
        final VetClinic clinic = new VetClinic("VetHelp");

        initClinic(clinic);

        System.out.println(clinic.getDoctors().size());
        System.out.println(clinic.getServices().size());
        System.out.println(clinic.getMedicines().size());

        final Pet pet1 = new Pet(1, "pet1", LocalDate.now(), Species.CAT,
                new Client(1, "client1", "+7-950-...", LocalDate.now()));
        final Pet pet2 = new Pet(2, "pet2", LocalDate.now(), Species.FISH,
                new Client(2, "client2", "+7-950-...", LocalDate.now()));
        final Pet pet3 = new Pet(3, "pet3", LocalDate.now(), Species.DOG,
                new Client(2, "client2", "+7-950-...", LocalDate.now()));

        Thread clinicThread = new Thread(clinic);
        clinicThread.start();
        new Thread(() -> clinic.newPatient(pet1)).start();
        new Thread(() -> clinic.newPatient(pet2)).start();
        new Thread(() -> clinic.newPatient(pet3)).start();

        Thread.sleep(100);
        clinic.setWorks(false);
        Thread.sleep(10);
        new Thread(() -> clinic.newPatient(pet1)).start();
        Thread.sleep(3_000);
        for (Appointment a : clinic.getAppointments()) {
            System.out.println(a.getPet().getName() + ", " + a.getPrice() + ", " + a.getDoctor().getName() +
                    ", " + ((a.getMedicine() != null) ? a.getMedicine().getTitle() : "null") + ", " + a.getDisease());
        }
        clinic.getPets().keySet().forEach(System.out::println);
        clinic.getClients().keySet().forEach(System.out::println);
        System.out.println(pet1.getMedicalHistory().getDiseases().size());
        pet1.getMedicalHistory().getDiseases().values().forEach(System.out::println);
    }

    private static void initClinic(VetClinic clinic) {
        final Random rnd = new Random();
        final List<Doctor> doctors = new ArrayList<>();
        final List<Service> services = new ArrayList<>();
        final Map<Medicine, Integer> meds = new HashMap<>();
        doctors.add(new Doctor(rnd.nextLong(), "Doc1", LocalDate.now(), LocalDate.now()));
        doctors.add(new Doctor(rnd.nextLong(), "Doc2", LocalDate.now(), LocalDate.now()));
        services.add(new Service("Осмотр", 500));
        services.add(new Service("Лечение", 2000));
        services.add(new Service("Профилактика", 1000));
        meds.put(new Medicine("Miracle-101", 20, 4500), 8);
        meds.put(new Medicine("Aliver-N", 5, 8000), 3);
        clinic.setDoctors(doctors);
        clinic.setServices(services);
        clinic.setMedicines(meds);
    }
}
