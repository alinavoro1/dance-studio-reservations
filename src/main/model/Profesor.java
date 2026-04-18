package main.model;

import java.util.ArrayList;
import java.util.List;


public class Profesor extends Persoana implements Calculable, Comparable<Profesor>{
    private StilDans specializare;
    private double salariu;
    private List<ClasaDans> clase;

    public Profesor(){
        this.clase = new ArrayList<>();
        calculeazaSuma();
    }

    public Profesor(String nume, String email, String telefon, StilDans specializare) {
        super(nume, email, telefon);
        this.specializare = specializare;
        this.clase = new ArrayList<>();
        calculeazaSuma();
    }

    public void adaugaClasa(ClasaDans c){
        if(c.getTip() != this.specializare){
            throw new IllegalArgumentException("Nu are specializarea necesara pentru a preda aceasta clasa!");
        }
        clase.add(c);
        calculeazaSuma();
    }

    public void stergeClasa(ClasaDans c) {
        clase.remove(c);
        calculeazaSuma();
    }

    public void afiseazaClase() {
        for (ClasaDans c : clase) {
            System.out.println(c.getNume());
        }
    }

    @Override
    public void afisare(){
        System.out.println("Profesor:");
        afisarePersoana();
        System.out.println("Specializare: " + specializare);
        System.out.println("Salariu: " + calculeazaSuma());
    }

    @Override
    public int compareTo(Profesor altProfesor){
        return this.getNume().compareToIgnoreCase(altProfesor.getNume());
    }

    @Override
    public double calculeazaSuma(){
        this.salariu = 4500 + clase.size()*300;
        return this.salariu;
    }

    public StilDans getSpecializare() {
        return specializare;
    }

    public void setSpecializare(StilDans specializare) {
        this.specializare = specializare;
    }

    public double getSalariu() {
        return salariu;
    }

    public List<ClasaDans> getClase() {
        return clase;
    }

    public void setClase(List<ClasaDans> clase) {
        this.clase = clase;
    }
}
