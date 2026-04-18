package main.model;

public abstract class Persoana {
    private int id;
    private String nume;
    private String email;
    private String telefon;
    private static int contorId = 0;

    public Persoana(){
        this.id = contorId++;
    }

    public Persoana(String nume, String email, String telefon) {
        this.id = contorId++;
        this.nume = nume;
        this.email = email;
        this.telefon = telefon;
    }

    void initializare(String nume, String email, String telefon){
        this.nume = nume;
        this.email = email;
        this.telefon = telefon;
    }

    public String getTelefon() {
        return telefon;
    }

    public void afisarePersoana(){
        System.out.println("Nume: " + nume + "\n" + "Email: " + email + "\n" +
                "Numar de telefon: " + telefon + "\n" + "Id: " + id + "\n");
    }

    public abstract void afisare();

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
