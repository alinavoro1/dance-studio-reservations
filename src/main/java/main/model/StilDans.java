package main.model;

public enum StilDans {
    CHOREO("Coregrafie originala, creata specific pe o anumita piesa, combinand mai multe stiluri."),
    DANCEHALL("Stil originar din Jamaica, plin de energie, miscare si atitudine."),
    HIPHOP("Stil urban, bazat pe ritm puternic si atitudine."),
    KPOP("Dans sincronizat inspirat din industria muzicala sud-coreeana."),
    COMMERCIAL("Dans comercial, folosit in videoclipuri muzicale si concerte."),
    CONTEMPORAN("Stil fluid, expresiv, care combina elemente de balet si jazz."),
    HEELS("Dans pe tocuri care pune accent pe linii corporale, echilibru si incredere."),
    KRUMP("Stil stradal foarte energetic, expresiv si exploziv, folosit adesea pentru eliberarea emotiilor. "),
    POPPING("Stil bazat pe contractia si relaxarea rapida a muschilor (pop/hit) pe ritmul muzicii."),
    WAACKING("Stil aparut in anii '70, caracterizat prin miscari rapide ale bratelor.");

    private String descriere;
    StilDans(String descriere){
        this.descriere = descriere;
    }

    public String getDescriere(){
        return descriere;
    }
}
