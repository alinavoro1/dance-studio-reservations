package main.model;

import java.util.ArrayList;
import java.util.List;

public class Client extends Persoana implements Comparable<Client>{
    private Abonament abonament;
    private List<Rezervare> rezervari;

    public Client(){
        this.rezervari = new ArrayList<>();
    }

    @Override
    public void afisare(){
        System.out.println("Client:");
        print();
        System.out.println("\n");
    }

    @Override
    public int compareTo(Client altClient){
        return this.getNume().compareToIgnoreCase(altClient.getNume());
    }
    public Client(int id, String nume, String email, String telefon) {
        super(id, nume, email, telefon);
        this.rezervari = new ArrayList<>();
    }
    public Client(String nume, String email, String telefon){
        super(nume, email, telefon);
        this.rezervari = new ArrayList<>();
    }
    public Client(String nume, String email, String telefon, Abonament abonament, List<Rezervare> rezervari){
        super(nume,email,telefon);
        this.abonament = abonament;
        this.rezervari = new ArrayList<>(rezervari);
    }

    public void adaugaRezervare(Rezervare r){
        rezervari.add(r);
    }

    public List<Rezervare> getRezervari(){
        return rezervari;
    }

    public Abonament getAbonament(){
        return abonament;
    }

    public void setAbonament(Abonament abonament) { this.abonament = abonament; }

}
