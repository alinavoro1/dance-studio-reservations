package main.service;

import main.model.*;
import main.repository.StudioRepository;
import main.repository.ClasaDansRepository;
import java.time.DayOfWeek;
import java.util.*;
import java.util.stream.Collectors;

public class StudioService {
    private StudioRepository studioRepository = new StudioRepository();
    private ClasaDansRepository clasaDansRepository = new ClasaDansRepository();
    private AuditService auditService = AuditService.getInstance();

    public void adaugaStudio(Studio s) {
        studioRepository.create(s);
        auditService.logAction("adauga_studio");
    }

    public List<Studio> getStudiouri() {
        return studioRepository.findAll();
    }

    public void filtreazaClaseDupaStil(StilDans stil){
        auditService.logAction("filtrare_stil");
        clasaDansRepository.afiseazaDupaStil(stil.name());
    }

    public List<ClasaDans> getToateClasele() {
        return clasaDansRepository.findAll();
    }

    public void adaugaClasaDans(ClasaDans c) {
        int salaId = (c.getSala() != null) ? c.getSala().getId() : 0;
        int profId = (c.getProfesor() != null) ? c.getProfesor().getId() : 0;

        clasaDansRepository.create(c, salaId, profId);
        auditService.logAction("adauga_clasa_dans");
    }

    public void filtreazaClaseDupaDificultate(Dificultate dif) {
        auditService.logAction("filtrare_dificultate");
        clasaDansRepository.afiseazaDupaDificultate(dif.name());
    }

    public void afiseazaProgram(Studio studio, DayOfWeek zi) {
        System.out.println("\n --- Clase disponibile la " + studio.getNume() + " ---");
        clasaDansRepository.afiseazaClasePentruStudio(studio.getId(), zi.name());
        auditService.logAction("afisare_program");
    }
}