package main.service;

import main.model.Client;

import java.util.*;

import java.util.Map;

public class ClientService {
    private Map<Integer, Client> clienti = new HashMap<>();

    public void adaugaClient(Client c){
        clienti.put(c.getId(), c);
    }

    public void afiseazaClienti(){
        for(Client c : clienti.values()){
            c.afisare();
        }
    }

    public Collection<Client> getClienitSortatiNume() {
        return new TreeSet<>(clienti.values());
    }
}
