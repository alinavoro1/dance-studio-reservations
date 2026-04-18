package main.model;

public class AbonamentSingle extends Abonament {

    public AbonamentSingle() {
        super(1);
        numarClase = 1;
        this.pret = calculeazaSuma();
    }

    @Override
    public double calculeazaSuma() {
        return 80.0;
    }

    @Override
    public String toString() {
        return "Single (1 clasa) - Pret: 80 RON";
    }
}