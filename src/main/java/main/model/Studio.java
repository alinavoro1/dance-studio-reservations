package main.model;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Studio {
    private static int contorId = 0;
    private int id;
    private String nume;
    private String locatie;
    private List<Sala> sali = new ArrayList<>();
    private Set<Profesor> profesori = new HashSet<>();

    public Studio(String nume, String locatie){
        this.id = contorId++;
        this.nume = nume;
        this.locatie = locatie;
    }

    public Studio(int id,String nume, String locatie){
        this.id = id;
        this.nume = nume;
        this.locatie = locatie;
    }

    public Sala adaugaSala(String nume, int capacitate){
        Sala s = new Sala(nume, capacitate, this);
        sali.add(s);
        return s;
    }

    public String getNume() { return nume; }
    public String getLocatie() { return locatie; }
    public List<Sala> getSali() { return sali; }
    public int getId() { return id; }

}
