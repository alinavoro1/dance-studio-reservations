package main.model;

import java.time.LocalDateTime;

public class Rezervare {
    private int id;
    private Client client;
    private ClasaDans clasa;
    private LocalDateTime dataRezervare;
    private boolean activa;
    private static int contorId = 0;

    public Rezervare(Client client, ClasaDans clasa){
        this.id = contorId++;
        this.client = client;
        this.clasa = clasa;
        this.activa = true;
        this.dataRezervare = LocalDateTime.now();
    }

    public Client getClient() { return client; }
    public ClasaDans getClasa() { return clasa; }
    public boolean isActiva() { return activa; }
    public void anuleaza() { this.activa = false; }

    @Override
    public String toString() {
        return "Rezervare #" + id + " | " + clasa.getNume() + " | Data: " + dataRezervare + " | Status: " + (activa ? "Activa" : "Anulata");

    }
}
