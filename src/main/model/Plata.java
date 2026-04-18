package main.model;

import java.time.LocalDateTime;

public class Plata implements Calculable {
    private int id;
    private Client client;
    private double suma;
    private LocalDateTime data;
    private String metoda;// card / cash
    private Abonament abonamentCumparat;
    private static int contorId = 0;

    public Plata(Client client, Abonament abonament, String metoda){
        this.id = contorId++;
        this.client = client;
        this.abonamentCumparat = abonament;
        this.metoda = metoda;
        this.suma = calculeazaSuma();
        this.data = LocalDateTime.now();
    }

    @Override
    public double calculeazaSuma() {
        return abonamentCumparat.getPret();
    }

    public void afisareChitanta(){
        System.out.println("=== Chitanta #" + id + " ===");
        System.out.println("Client: " + client.getNume());
        System.out.println("Achizitie: " + abonamentCumparat);
        System.out.println("Total achitat: " + suma + " RON (metoda: " + metoda + ")");
        System.out.println("Data: " + data);
    }
}
