package main.model;

import java.util.*;
import java.util.stream.Collectors;


public class Profesor extends Persoana implements Calculable, Comparable<Profesor>{
    private Set<StilDans> specializari = new HashSet<>();
    private double salariu;
    private List<ClasaDans> clase;
    private List<Review> reviewuri = new ArrayList<>();

    public Profesor(){
        this.clase = new ArrayList<>();
        calculeazaSuma();
    }

    public Profesor(int id,String nume, String email, String telefon, Set<StilDans> specializari) {
        super(id,nume, email, telefon);
        this.specializari = specializari;
        this.clase = new ArrayList<>();
        calculeazaSuma();
    }

    public Profesor(String nume, String email, String telefon, Set<StilDans> specializari) {
        super(nume, email, telefon);
        this.specializari = specializari;
        this.clase = new ArrayList<>();
        calculeazaSuma();
    }

    public void adaugaClasa(ClasaDans c){
        boolean areSpecializarea = false;
        for(StilDans s: specializari){
            if(c.getTip() == s){
                areSpecializarea = true;
                break;
            }
        }
        if(areSpecializarea == false){
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
        print();
        
        System.out.println("Specializari (Stiluri de dans): ");
        if (specializari.isEmpty()) {
            System.out.println("  - Niciun stil atribuit");
        } else {
            for (StilDans s : specializari) {
                System.out.println("  - " + s.name());
            }
        }

        System.out.println("Clase predate: ");
        if (clase == null || clase.isEmpty()) {
            System.out.println("  - Nicio clasa predata in acest moment");
        } else {
            for (ClasaDans c : clase) {
                System.out.println("  - " + c.getNume() + " (Stil: " + c.getTip() + ")");
            }
        }

        System.out.println("Salariu estimat: " + calculeazaSuma() + " RON");
        System.out.println("-----------------------------------");
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

    public void adaugaReview(Review r) {
        this.reviewuri.add(r);
    }

    public void afiseazaReviewUri() {
        if(reviewuri.isEmpty()) {
            System.out.println("Nu exista review-uri.");
            return;
        }
        for(Review r : reviewuri) {
            r.print();
        }
    }

    public void setSpecializare(StilDans stil) { specializari.add(stil); }

    public Set<StilDans> getSpecializari() {
        return Collections.unmodifiableSet(specializari);
    }

    public String getSpecializare() {
        if (specializari == null || specializari.isEmpty()) return "—";
        return specializari.stream()
                .map(StilDans::name)
                .collect(Collectors.joining(", "));
    }
//    public void setSpecializare(StilDans specializare) {
//        this.specializare = specializare;
//    }

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
