package main.service;

import main.model.Client;
import main.model.Profesor;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

public class ProfesorService {
    private Map<Integer, Profesor> profesori = new HashMap<>();

    public void adaugaProfesor(Profesor p){
        profesori.put(p.getId(), p);
    }

    public void afiseazaProfesori(){
        for(Profesor p : profesori.values()){
            p.afisare();
        }
    }

    public Collection<Profesor> getProfesori() {
        return new TreeSet<>(profesori.values());
    }
}
