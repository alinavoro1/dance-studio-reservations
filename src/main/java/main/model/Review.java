package main.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Review implements Printable{
    private static int contorId = 0;
    private int id;
    private Client client;
    private Profesor profesor;
    private ClasaDans clasa;
    private LocalDateTime data;
    private String text;
    private int nota; //1-5

    public Review(Client client, ClasaDans clasa, int nota, String text) {
        contorId++;
        this.id = contorId;
        this.client = client;
        this.clasa = clasa;
        this.nota = nota;
        this.text = text;
        this.data = LocalDateTime.now();
    }
    public Review(int id, Client client, ClasaDans clasa, int nota, String text) {
        this.id = id;
        this.client = client;
        this.clasa = clasa;
        this.nota = nota;
        this.text = text;
        this.data = LocalDateTime.now();
    }

    public Review(Client client, Profesor profesor, int nota, String text) {
        contorId++;
        this.id = contorId;
        this.client = client;
        this.profesor = profesor;
        this.nota = nota;
        this.text = text;
        this.data = LocalDateTime.now();
    }

    public Review(int id,Client client, Profesor profesor, int nota, String text) {
        this.id = id;
        this.client = client;
        this.profesor = profesor;
        this.nota = nota;
        this.text = text;
        this.data = LocalDateTime.now();
    }

    @Override
    public void print(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        System.out.println("⭐ " + nota + "/5 | De la: " + client.getNume() + " | Data: " + data.format(formatter));
        if (clasa != null) {
            System.out.println("   Despre clasa: " + clasa.getNume());
        } else if (profesor != null) {
            System.out.println("   Despre profesor: " + profesor.getNume());
        }
        System.out.println("   Comentariu: " + text);
        System.out.println("-------------------------------------------------");
    }

    public int getId() {
        return id;
    }

    public ClasaDans getClasa() { return clasa; }
}
