package main.service;

import main.model.*;
import main.exceptions.AbonamentInvalidException;
import main.exceptions.CapacitateMaximaException;
import java.util.ArrayList;
import java.util.List;

public class RezervareService {
    private List<Plata> istoricPlati = new ArrayList<>();
    private List<Rezervare> toateRezervarile = new ArrayList<>();

    public void cumparaAonament(Client client, Abonament abonament, String metodaPlata){
        Plata plataNoua = new Plata( client, abonament, metodaPlata);
        istoricPlati.add(plataNoua);

        client.setAbonament(abonament);

        System.out.println("\n Tranzactie reusita!");
        plataNoua.afisareChitanta();
    }

    public void rezervareClasa(Client client, ClasaDans clasa) throws AbonamentInvalidException {
        Abonament ab = client.getAbonament();
        if(ab == null) {
            throw new AbonamentInvalidException("Nu aveti un abonament activ.");
        }
        if(!ab.poateRezerva()){
            throw new AbonamentInvalidException("Abonamentul a expirat sau nu mai are sedinte!");
        }

        clasa.inscrieClient(client);
        ab.consumaSedinta();
        Rezervare rezervareNoua = new Rezervare(client, clasa);
        toateRezervarile.add(rezervareNoua);
        client.getRezervari().add(rezervareNoua);

        System.out.println("\n Rezervare confirmata! " + client.getNume() + " te asteptam la " + clasa.getNume() + ".");
        System.out.println("Sedinte ramase: " + ab.getSedinteRamase());
    }
}
