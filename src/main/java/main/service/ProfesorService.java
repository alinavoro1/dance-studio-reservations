package main.service;

import main.model.Profesor;
import main.model.StilDans;
import main.repository.ProfesorRepository;
import java.util.*;
import java.util.stream.Collectors;

public class ProfesorService {
    private ProfesorRepository profesorRepository = new ProfesorRepository();
    private AuditService auditService = AuditService.getInstance();

    public void adaugaProfesor(Profesor p){
        profesorRepository.create(p);
        auditService.logAction("adauga_profesor");
    }

    public void afiseazaProfesori(){
        profesorRepository.findAll().forEach(p -> {
            System.out.println(p.getId() + ". " + p.getNume() + " (Email: " + p.getEmail() + ")");
        });
        auditService.logAction("afisare_profesori");
    }

    public List<Profesor> cautaProfesorDupaNume(String nume){
        auditService.logAction("cauta_profesor");
        return profesorRepository.findAll().stream()
                .filter(p -> p.getNume().toLowerCase().contains(nume.toLowerCase()))
                .collect(Collectors.toList());
    }
    public List<Profesor> getProfesori() {
        return profesorRepository.findAll();
    }

    public Collection<Profesor> getProfesoriSortatiNume() {
        return new TreeSet<>(profesorRepository.findAll());
    }

    public void addStyleToProfesor(int profesorId, StilDans stil) {
        profesorRepository.addStyle(profesorId, stil);
        auditService.logAction("adauga_stil_profesor");
    }

    public Profesor findById(int id) {
        auditService.logAction("cauta_profesor_by_id");
        return profesorRepository.findById(id);
    }
}