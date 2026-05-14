package main.model;

public class AbonamentGold extends Abonament {

    public AbonamentGold(int numarLuni) {
        super(numarLuni);
        numarClase = numarLuni * 8;
        this.pret = calculeazaSuma();
    }

    @Override
    public double calculeazaSuma() {
        double pretPerClasa;

        if (numarLuni >= 6) pretPerClasa = 50.0;
        else if (numarLuni >= 3) pretPerClasa = 55.0;
        else pretPerClasa = 60.0;

        return numarClase * pretPerClasa;
    }

    @Override
    public String toString() {
        return "Gold (" + numarLuni + " luni, " + numarClase + " clase) - Pret total: " + pret + " RON (Redus la " + (pret/numarClase) + "/clasa)";
    }
}
