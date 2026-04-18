package main.model;

public class Sala {
    private int id;
    private String nume;
    private int capacitate;
    private Studio studio;
    private static int contorId = 0;

    public Sala(String nume, int capacitate, Studio studio){
        this.id = contorId++;
        this.nume = nume;
        this.capacitate = capacitate;
        this.studio = studio;
    }

    public String getNume() { return  nume;}
    public int getCapacitate() { return capacitate; }
    public Studio getStudio() { return studio; }
}
