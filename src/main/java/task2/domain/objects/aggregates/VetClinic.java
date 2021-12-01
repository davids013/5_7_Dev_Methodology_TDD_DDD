package task2.domain.objects.aggregates;

import task2.domain.objects.entities.Client;
import task2.domain.objects.entities.Disease;
import task2.domain.objects.entities.Doctor;
import task2.domain.objects.entities.Pet;
import task2.domain.objects.value_objects.Medicine;
import task2.domain.objects.value_objects.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class VetClinic implements Runnable {
    private final String HISTORY_DIR = "src" + File.separator + "main" + File.separator + "java" + File.separator +
            "task2" + File.separator + "resources" + File.separator;
    private final String name;
    private final Map<String, Doctor> doctors = new HashMap<>();
    private final Map<String, Service> services = new HashMap<>();
    private final Map<String, Pet> pets = new HashMap<>();
    private final Map<String, Client> clients = new HashMap<>();
    private final Map<Medicine, Integer> medicines = new HashMap<>();
    private final List<Appointment> appointments = new CopyOnWriteArrayList<>();
    private final Queue<Pet> petQueue = new LinkedBlockingQueue<>();
    private boolean isWorks = true;
    private final AtomicInteger appointmentCounter = new AtomicInteger(0);

    public VetClinic(String name) {
        this.name = name;
    }

    public void setDoctors(Collection<Doctor> doctors) {
        doctors.forEach((doc) -> this.doctors.put(doc.getName(), doc));
    }

    public void setServices(Collection<Service> services) {
        services.forEach((service) -> this.services.put(service.getName(), service));
    }

    public void setMedicines(Map<Medicine, Integer> medicines) {
        medicines.keySet().forEach((med) -> this.medicines.put(med, medicines.get(med)));
    }

    public void setWorks(boolean isWorks) {
        this.isWorks = isWorks;
    }

    public String getName() {
        return name;
    }

    public Map<String, Doctor> getDoctors() {
        return new HashMap<String, Doctor>(doctors);
    }

    public Map<String, Service> getServices() {
        return new HashMap<String, Service>(services);
    }

    public Map<String, Pet> getPets() {
        return new HashMap<String, Pet>(pets);
    }

    public Map<String, Client> getClients() {
        return new HashMap<String, Client>(clients);
    }

    public Map<Medicine, Integer> getMedicines() {
        return new HashMap<Medicine, Integer>(medicines);
    }

    public List<Appointment> getAppointments() {
        return new ArrayList<>(appointments);
    }

    public boolean isWork() {
        return isWorks;
    }

    public void newPatient(Pet pet) {
        if (!pets.containsValue(pet)) pets.put(pet.getName(), pet);
        if (!clients.containsValue(pet.getClient())) clients.put(pet.getClient().getName(), pet.getClient());
        petQueue.offer(pet);
        System.out.println("Питомец '" + pet.getName() + "' ожидает приёма");
    }

    @Override
    public void run() {
        if (doctors.size() == 0) {
            System.out.println("В клинике никто не работает");
        } else {
            try {
                final ExecutorService pool = Executors.newFixedThreadPool(doctors.size());
                final List<Future<Appointment>> futures = new ArrayList<>();
                while (isWorks) {
                    while (petQueue.size() > 0) {
                        for (Doctor doc : doctors.values()) {
                            if (doc.isFree()) {
                                doc.setFree(false);
                                final Doctor doctor;
                                final Pet pet;
                                final Service service;
                                final Medicine medicine;
                                final Disease disease;
                                final Appointment appointment;
                                doctor = doc;
                                pet = petQueue.poll();
                                final Random rnd = new Random();
                                int position = rnd.nextInt(services.size());
                                service = services.get(services.keySet().toArray()[position]);
                                position = rnd.nextInt(medicines.size() + 1);
                                medicine = (position < medicines.size())
                                        ? (Medicine) medicines.keySet().toArray()[position] : null;
                                position = rnd.nextInt(Disease.values().length);
                                disease = Disease.values()[position];
                                appointment = new Appointment(
                                        appointmentCounter.incrementAndGet(), doctor, pet, service, medicine, disease);
                                final Future<Appointment> future = pool.submit(appointment);
                                futures.add(future);
                                System.out.println("Приём " + appointmentCounter.get() + " оформлен");
                                break;
                            }
                        }
                        Thread.sleep(10);
                    }
                }
                pool.shutdown();
                while (!pool.isTerminated()) Thread.sleep(10);
                futures.forEach((f) -> {
                    try {
                        final Appointment appointment = f.get();
                        appointments.add(appointment);
                        appointment.getPet().addToHistory(appointment);
                        new Thread(() -> {
                            saveMedHistory(appointment.getPet().getName() + ".txt", appointment);
                        }).start();
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                    }
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean saveMedHistory(String fileName, Appointment appointment) {
        final String LINE_SEPARATOR = "\r\n";
        final File file = new File(HISTORY_DIR + fileName);
        if (!file.exists()) {
            final String parent = file.getParent();
            try {
                if (!(new File(parent).exists())) {
                    Files.createDirectories(Paths.get(file.getParent()));
                }
                Files.createFile(Paths.get(file.getAbsolutePath()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try (final FileWriter writer = new FileWriter(file, true)) {
            final String info = appointment.toString();
            writer.write(info + LINE_SEPARATOR);
            writer.flush();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
