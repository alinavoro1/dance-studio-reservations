package main.service;

import main.model.*;
import main.exceptions.AbonamentInvalidException;
import main.repository.AbonamentRepository;
import main.repository.RezervareRepository;

import java.time.LocalDate;

public class RezervareService {
    private RezervareRepository rezervareRepository = new RezervareRepository();
    private AbonamentRepository abonamentRepository = new AbonamentRepository();
    private AuditService auditService = AuditService.getInstance();

    public void cumparaAbonament(Client client, Abonament abonament, String metodaPlata){
        String tipAbonamentStr = abonament.getClass().getSimpleName();
        String dataExpStr = (abonament.getDataExpirare() != null) ?
                abonament.getDataExpirare().toString() : null;

        int abonamentId = abonamentRepository.create(client.getId(), tipAbonamentStr, abonament.getSedinteRamase(), dataExpStr);
        if (abonamentId > 0) {
            abonament.setId(abonamentId);
        }
        client.setAbonament(abonament);
        System.out.println("\n Tranzactie reusita!");
        auditService.logAction("cumpara_abonament");
    }

    public void rezervareClasa(Client client, ClasaDans clasa) throws AbonamentInvalidException {
        Abonament ab = client.getAbonament();
        if(ab == null || !ab.poateRezerva()) {
            throw new AbonamentInvalidException("Abonament invalid!");
        }

        clasa.inscrieClient(client);
        ab.consumaSedinta();
        rezervareRepository.create(client.getId(), clasa.getId());
        abonamentRepository.updateDupaRezervare(client.getId(), ab.getSedinteRamase(), ab.getDataExpirare());

        System.out.println("\n Rezervare confirmata!");
        auditService.logAction("rezervare_clasa");
    }
}