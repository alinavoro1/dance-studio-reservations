package main.model;
import java.time.LocalDate;

public abstract class Abonament implements Calculable{
    private int id;
    protected double pret;
    protected LocalDate dataStart;
    protected LocalDate dataExpirare;
    protected int numarLuni;
    protected int numarClase;
    private static int contorId = 0;

    public Abonament() {
        this.id = contorId++;
    }

    public Abonament(int numarLuni){
        this.id = contorId++;
        this.numarLuni = numarLuni;
    }


    public boolean poateRezerva(){
        if (dataStart == null)
            return true;
        else if (numarClase > 0 && LocalDate.now().isBefore(dataExpirare.plusDays(1))){
            return true;
        }
        return false;
    }
    public void consumaSedinta(){
        if (!poateRezerva()){
            throw new IllegalStateException("Abonamentul nu mai este valid");
        }
        if(dataStart == null){
            this.dataStart = LocalDate.now();
            this.dataExpirare = this.dataStart.plusMonths(numarLuni);
            System.out.println("Abonamentul a fost activat astazi! Va expira pe: " + dataExpirare);
        }

        numarClase--;
    }

    @Override
    public abstract double calculeazaSuma();

    public double getPret() { return pret; }
    public LocalDate getDataStart() { return dataStart; }
    public LocalDate getDataExpirare() { return dataExpirare; }
    public int getSedinteRamase() { return numarClase; }
}
