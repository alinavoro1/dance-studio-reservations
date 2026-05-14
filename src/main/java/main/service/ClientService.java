package main.service;
import main.repository.ClientRepository;
import java.util.stream.Collectors;

import main.model.Client;

import java.util.*;

import java.util.Map;

public class ClientService {
    private Map<Integer, Client> clienti = new HashMap<>();
    private ClientRepository clientRepository = new ClientRepository();
    private AuditService auditService = AuditService.getInstance();

    public void adaugaClient(Client c){
        clientRepository.create(c);
        auditService.logAction("adauga_client");
    }

    public void afiseazaClienti(){
        List<Client> lista = clientRepository.findAll();
        lista.forEach(Client::afisare);
        auditService.logAction("afisare_clienti");
    }

    public Collection<Client> getClientiSortatiNume() {
        return new TreeSet<>(clientRepository.findAll());
    }

    public void cautaClientDupaNume(String nume){
        List<Client> rezultate = clientRepository.findAll().stream()
                .filter(c -> c.getNume().toLowerCase().contains(nume.toLowerCase()))
                .collect(Collectors.toList());

        if(rezultate.isEmpty()) {
            System.out.println("Nu s-a gasit niciun client cu numele: " + nume);
        } else {
            System.out.println("\nRezultatele cautarii:");
            rezultate.forEach(Client::afisare);
        }
        auditService.logAction("cauta_client");
    }

    public Client findById(int id) {
        return clientRepository.findById(id);
    }
}
