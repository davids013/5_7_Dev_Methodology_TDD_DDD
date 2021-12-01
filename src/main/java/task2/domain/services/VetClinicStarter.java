package task2.domain.services;

import task2.domain.objects.aggregates.VetClinic;
import task2.domain.objects.entities.Client;
import task2.domain.objects.entities.Doctor;
import task2.domain.objects.entities.Pet;
import task2.domain.objects.value_objects.Medicine;
import task2.domain.objects.value_objects.Service;
import task2.domain.objects.value_objects.Species;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class VetClinicStarter {
    private static final int TOTAL_DOCTORS = 2;
    private static final int TOTAL_PETS = 3;
    private static final int TOTAL_CYCLES = 2;

    public static void main(String[] args) {
        System.out.println("\n\tЗадача 2*. Разработка с применением DDD (Domain Driven Design)");

        final VetClinic clinic = getTestClinic("VetHelp", TOTAL_DOCTORS);
        final List<Pet> pets = getTestPets(TOTAL_PETS);
        final ExecutorService pool = Executors.newFixedThreadPool(pets.size() + 1);

        pool.submit(clinic);
        for (int i = 0; i < TOTAL_CYCLES; i++) {
            for (Pet pet : pets) {
                pool.submit(() -> clinic.newPatient(pet));
            }
        }
        try {
            Thread.sleep(100);
            clinic.setWorks(false);
            pool.shutdown();
            while (!pool.isTerminated()) Thread.sleep(10);
            System.out.println("Работа клиники завершена");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private static VetClinic getTestClinic(String name, int numOfDocs) {
        final VetClinic clinic = new VetClinic(name);
        final List<Doctor> doctors = new ArrayList<>();
        final List<Service> services = new ArrayList<>();
        final Map<Medicine, Integer> meds = new HashMap<>();
        for (int i = 1; i <= numOfDocs; i++) {
            doctors.add(new Doctor(i, "Doc" + i, LocalDate.now(), LocalDate.now()));
        }
        services.add(new Service("Осмотр", 500));
        services.add(new Service("Лечение", 2000));
        services.add(new Service("Профилактика", 1000));
        meds.put(new Medicine("Miracle-101", 20, 4500), 8);
        meds.put(new Medicine("Aliver-N", 5, 8000), 3);
        clinic.setDoctors(doctors);
        clinic.setServices(services);
        clinic.setMedicines(meds);
        return clinic;
    }

    public static List<Pet> getTestPets(int numOfPets) {
        final List<Pet> pets = new ArrayList<>();
        final List<Client> clients = new ArrayList<>();
        final Random rnd = new Random();
        for (int i = 1; i <= numOfPets / 2; i++) {
            clients.add(new Client(i, "client" + i, "+7-950-...", LocalDate.now()));
        }
        if (clients.size() == 0)
            clients.add(new Client(1, "client1", "+7-950-...", LocalDate.now()));
        for (int i = 1; i <= numOfPets; i++) {
            pets.add(new Pet(i, "pet" + i, LocalDate.now(),
                    Species.values()[rnd.nextInt(Species.values().length)],
                    clients.get(rnd.nextInt(clients.size()))));
        }
        return pets;
    }
}
