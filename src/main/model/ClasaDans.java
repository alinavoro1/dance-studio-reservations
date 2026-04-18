package main.model;

import main.exceptions.CapacitateMaximaException;
import main.exceptions.SuprapunereProgramException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ClasaDans {
    private int id;
    private String nume;
    private StilDans tip;
    private Profesor profesor;
    private Sala sala;
    private List<Program> program = new ArrayList<>();
    private Set<Client> participanti = new HashSet<>();
    private static int contorId = 0;

    public ClasaDans(){
        this.id = contorId++;
    }

    public ClasaDans(String nume, Profesor profesor, Sala sala) {
        this.id = contorId++;
        this.nume = nume;
        this.profesor = profesor;
        this.sala = sala;
    }

    public void adaugaProgram(Program p) throws SuprapunereProgramException {
        for(Program existent : program){
            if(existent.seSuprapune(p)) throw new SuprapunereProgramException("Conflict orar!");
        }
        program.add(p);
    }

    public void inscrieClient(Client c) throws CapacitateMaximaException {
        if(participanti.size() >= sala.getCapacitate()) {
            throw new CapacitateMaximaException("Aceasta clasa nu mai are locuri!");
        }
        participanti.add(c);
    }

    public int getId() {
        return id;
    }

    public String getNume() {
        return nume;
    }

    public StilDans getTip() {
        return tip;
    }

    public void setTip(StilDans tip) {
        this.tip = tip;
    }

    public int getLocuriLibere() { return sala.getCapacitate() - participanti.size(); }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public Sala getSala() { return sala; }

    public List<Program> getProgram() { return program; }
}
