package main.service;

import main.model.*;

import java.sql.SQLOutput;
import java.time.DayOfWeek;
import java.util.*;

public class StudioService {
    private List<Studio> studiouri = new ArrayList<>();
    private List<ClasaDans> toateClasele = new ArrayList<>();

    public void adaugaStudio(Studio s) { studiouri.add(s); }
    public void adaugaClasaDans(ClasaDans c) { toateClasele.add(c); }

    public List<ClasaDans> getToateClasele() { return toateClasele; }

    public List<Studio> getStudiouri() { return studiouri; }

    public void afiseazaProgram(Studio studio, DayOfWeek zi) {
        System.out.println("\n --- Program " + studio.getNume() + " - " + zi + " ---\n");
        boolean gasit = false;

        for ( ClasaDans clasa : toateClasele) {
            if(clasa.getSala().getStudio().equals(studio)) {
                for(Program p : clasa.getProgram()){
                    if(p.getZi() == zi) {
                        System.out.println("[" + clasa.getId() +"]." + clasa.getNume() + " | Ora: " + p.getOraStart() + " - " + p.getOraFinal() +
                                " | Sala: " + clasa.getSala().getNume() +
                                " | Locuri libere: " + clasa.getLocuriLibere());
                        gasit = true;
                    }
                }
            }
        }
        if ( !gasit){
            System.out.println("Nu exista clase azi la acest studio.");
        }
    }
}
