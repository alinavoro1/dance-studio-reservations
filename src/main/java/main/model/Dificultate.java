package main.model;

public enum Dificultate {
    BEGINNER("Ideal pentru cei care abia descopera dansul. Se invata pasii de baza si coordonarea."),
    INTERMEDIATE("Pentru cei cu o baza solida. Coregrafii mai lungi si tehnici mai complexe."),
    ADVANCED("Ritm rapid, coregrafii complexe, necesita memorie musculara si tehnica avansata.");

    private String descriere;

    Dificultate(String descriere){
        this.descriere = descriere;
    }
    public String getDescriere(){
        return descriere;
    }
}
