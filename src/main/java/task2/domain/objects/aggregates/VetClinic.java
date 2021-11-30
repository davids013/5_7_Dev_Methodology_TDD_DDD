package task2.domain.objects.aggregates;

import task2.domain.objects.entities.Client;
import task2.domain.objects.entities.Disease;
import task2.domain.objects.entities.Doctor;
import task2.domain.objects.entities.Pet;
import task2.domain.objects.value_objects.Medicine;
import task2.domain.objects.value_objects.Service;

import java.util.*;
import java.util.concurrent.*;

public class VetClinic implements Runnable {
    private final String name;
    private Map<String, Doctor> doctors = new HashMap<>();
    private Map<String, Service> services = new HashMap<>();
    private Map<String, Pet> pets = new HashMap<>();
    private Map<String, Client> clients = new HashMap<>();
    private Map<Medicine, Integer> medicines = new HashMap<>();
    private List<Appointment> appointments = new CopyOnWriteArrayList<>();
    private Queue<Pet> petQueue = new LinkedBlockingQueue<>();
    private boolean isWorks = true;

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

    public void addAppointment(Appointment appointment) {
        appointments.add(appointment);
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
                                        appointments.size(), doctor, pet, service, medicine, disease);
                                final Future<Appointment> future = pool.submit(appointment);
                                futures.add(future);
                                System.out.println("Приём " + futures.size() + " оформлен");
                                break;
                            }
                        }
                        Thread.sleep(10);
                    }
                };
                futures.forEach((f) -> {
                    try {
                        final Appointment appointment = f.get();
                        appointments.add(appointment);
//                        if (appointment.getDisease() != Disease.HEALTHY) {
                            appointment.getPet().addDisease(appointment.getDateTime(), appointment.getDisease());
//                        }
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                    }
                });
                pool.shutdown();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
}
