package main.model;

public class AbonamentSilver extends Abonament {

    public AbonamentSilver(int numarLuni){
        super(numarLuni);
        this.numarClase = numarLuni * 4;
        this.pret = calculeazaSuma();
    }

    @Override
    public double calculeazaSuma(){
        double pretPerClasa;

        if(numarLuni ==6 ) pretPerClasa = 60.0;
        else if (numarLuni == 3) pretPerClasa = 65.0;
        else pretPerClasa = 70.0;

        return numarClase * pretPerClasa;
    }

    @Override
    public String toString() {
        return "Silver (" + numarLuni + " luni, " + numarClase + " clase) - Pret total: " + pret + " RON (Redus la " + (pret/numarClase) + "/clasa)";
    }
}
