package main;

import main.model.*;
import main.service.*;
import main.exceptions.*;
import main.util.InputHelper;


import java.util.ArrayList;
import java.util.List;
import java.time.DayOfWeek;
import java.time.LocalTime;


public class Main{
    private static ClientService clientService = new ClientService();
    private static ProfesorService profesorService = new ProfesorService();
    private static StudioService studioService = new StudioService();
    private static RezervareService rezervareService = new RezervareService();

    public static void main(String[] args) {
        populeazaDateInitiale(clientService, profesorService, studioService);

        System.out.println("=== Dance Studio Manager ===");
        int optiune = -1;
        while (optiune != 0){
            afiseazaMeniu();
            optiune = InputHelper.citesteInt("Alege o optiune: ");
            try{
                switch(optiune){
                    case 1:
                        meniuAfisareProgram();
                        break;
                    case 2:
                        meniuAdaugaClient();
                        break;
                    case 3:
                        meniuCumparaAbonament();
                        break;
                    case 4:
                        meniuRezervaClasa();
                        break;
                    case 5:
                        meniuAdaugaProfesor();
                        break;
                    case 6:
                        meniuAdaugaClasa();
                        break;
                    case 7:
                        meniuStergeClasa();
                        break;
                    case 8:
                        System.out.println("\n ---Lista clienti ---");
                        clientService.afiseazaClienti();
                        break;
                    case 9:
                        System.out.println("\n ---Lista profesori ---");
                        profesorService.afiseazaProfesori();
                        break;
                    case 0:
                        System.out.println("La revedere! Sistem inchis.");
                        break;
                    default:
                        System.out.println("Optiune invalida!");
                }
            }catch (Exception e) {
                System.out.println("Eroare la executie: " + e.getMessage());
            }
        }
    }

    private static void afiseazaMeniu(){
        System.out.println("=====  MENIU  =====");
        System.out.println("1. Afiseaza programul studioului");
        System.out.println("2. Client nou");
        System.out.println("3. Cumpara un abonament");
        System.out.println("4. Rezerva o clasa ");
        System.out.println("5. Profesor nou");
        System.out.println("6. Adauga o clasa de dans noua");
        System.out.println("7. Sterge o clasa din program");
        System.out.println("8. Afiseaza toti clientii");
        System.out.println("9. Afiseaza toti profesorii");
        System.out.println("0. Iesire" );
        System.out.println("====================");

    }

    private static void meniuAfisareProgram(){
        System.out.println("\n --- PROGRAM ---");
        Studio studio = alegeStudio();
        if (studio == null) return;

        System.out.println("Zile: 1.Luni | 2.Marti | 3. Miercuri | 4. Joi | 5. Vineri | 6. Sambata | 7. Duminica");
        int ziua = InputHelper.citesteInt("Alege ziua: ");
        DayOfWeek zi = DayOfWeek.of(ziua);
        studioService.afiseazaProgram(studio, zi);

    }

    private static void meniuAdaugaClient() {
        System.out.println("\n --- Adauga client --- " );
        String nume = InputHelper.citesteText("Nume Complet: ");
        String email = InputHelper.citesteText("Email: ");
        String telefon = InputHelper.citesteText("Telefon: ");
        Client c = new Client(nume, email, telefon);

        clientService.adaugaClient(c);
        System.out.println("Clientul " + nume + " a fost adaugat cu succes!");

    }

    private static void meniuAdaugaProfesor(){
        System.out.println("\n --- Adauga profesor --- " );
        String nume = InputHelper.citesteText("Nume: ");
        String email = InputHelper.citesteText("Email: ");
        String telefon = InputHelper.citesteText("Telefon: ");

        System.out.println("Alege stilul de dans: ");
        StilDans[] stiluri = StilDans.values();
        for(int i = 0; i < stiluri.length; i++){
            System.out.println((i+1) + ". " + stiluri[i]);
        }
        int index = InputHelper.citesteInt("Alege ID -ul stilului: " ) -1;

        Profesor p = new Profesor(nume, email, telefon, stiluri[index]);
        profesorService.adaugaProfesor(p);
        System.out.println("Profesorul " + nume + " a fost adaugat cu succes!");

    }

    private static void meniuAdaugaClasa(){
        System.out.println("\n --- Adauga clasa --- " );
        String numeClasa = InputHelper.citesteText("Numele clasei: ");

        Profesor prof = alegeProfesor();
        if (prof == null) return;

        Studio studio = alegeStudio();
        if(studio == null) return;

        System.out.println("Sali disponibile in " + studio.getNume() + ":");
        for(int i = 0 ; i < studio.getSali().size() ; i++){
            System.out.println((i+1) + ". " + studio.getSali().get(i).getNume());
        }
        int index = InputHelper.citesteInt("Alege ID sala: ")-1;
        Sala sala = studio.getSali().get(index);
        ClasaDans clasa = new ClasaDans(numeClasa, prof, sala);
        clasa.setTip(prof.getSpecializare());
        prof.adaugaClasa(clasa);
        studioService.adaugaClasaDans(clasa);
        System.out.println("Clasa a fost creata cu succes!");
    }

    private static void meniuStergeClasa(){
        System.out.println("\n --- Sterge clasa din program ---");
        ClasaDans clasa = alegeClasa();
        if(clasa == null || clasa.getProgram().isEmpty()){
            System.out.println("Nu exista clasa sau intervale orare de sters.");
            return;
        }
        System.out.println("Orarul curent al clasei " + clasa.getNume() + ": ");
        for(int i = 0 ; i < clasa.getProgram().size(); i++){
            System.out.println((i+1) + ". " + clasa.getProgram().get(i));
        }
        int index = InputHelper.citesteInt("Alege ID-ul intelvalului orar pe care vrei sa il stergi: ") -1;
        clasa.getProgram().remove(index);
        System.out.println("Interval orar sters cu succes!");


    }

    private static void meniuCumparaAbonament(){
        System.out.println("\n --- Cumpara abonament ---");
        System.out.println("1.Introdu datele clientului | 2. Alege din lista de clienti ");
        int nr = InputHelper.citesteInt("Alege varianta: ");

        Client clientAles = null;
        if (nr == 1 ){
            String nume = InputHelper.citesteText("Nume Complet: ");
            String email = InputHelper.citesteText("Email: ");
            String telefon = InputHelper.citesteText("Telefon: ");
            clientAles = new Client(nume,email,telefon);

            clientService.adaugaClient(clientAles);

        }
        else if(nr == 2){
            List<Client> listaClienti = new ArrayList<>(clientService.getClienitSortatiNume());
            if(listaClienti.isEmpty()){
                System.out.println("Nu exista clienit in sistem. Te rog sa alegi varianta 1.");
                return;
            }
            for (int i = 0; i < listaClienti.size(); i++){
                System.out.println((i+1) + ". " + listaClienti.get(i).getNume());
            }
            int index = InputHelper.citesteInt("Alege ID-ul clientului: ") -1;
            if (index >= 0 && index < listaClienti.size()){
                clientAles = listaClienti.get(index);

            }
            else {
                System.out.println("Selectie invalida! Operatie anulata.");
                return;
            }
        }
        else {
            System.out.println("Opitune invalida");
            return;
        }

        System.out.println("Client selectat: " + clientAles.getNume());

        System.out.println("Tipuri:\n 1. Single (80 RON, valabil o luna) \n 2. Silver (4 ședințe / luna) \n 3. Gold (8 ședințe / luna)");
        int tip = InputHelper.citesteInt("Alege tipul: ");
        Abonament abNou = null;
        if(tip == 1){
            abNou = new AbonamentSingle();
        }
        else {
            System.out.println("Perioada: 1 luna | 3 luni | 6 luni.");
            int luni = InputHelper.citesteInt("Alege numarul de luni: ");
            if(tip == 2 ) abNou = new AbonamentSilver(luni);
            else if(tip == 3 ) abNou = new AbonamentGold(luni);
            else{
                System.out.println("Tip invalid. Repeta procesul");
                return;
            }
        }

        if(abNou != null){
            int metoda = InputHelper.citesteInt("Metoda de plata (1.cash/2.card): ");
            if (metoda == 1) rezervareService.cumparaAonament(clientAles,abNou,"cash");
            else if (metoda == 2) rezervareService.cumparaAonament(clientAles,abNou,"card");
            else{
                System.out.println("Metoda invalida. Repeta procesul");
                return;
            }

        }
    }


    private static void meniuRezervaClasa(){
        System.out.println("\n --- Rezerva o clasa ---");
        System.out.println("1.Introdu datele clientului | 2. Alege din lista de clienti ");
        int nr = InputHelper.citesteInt("Alege varianta: ");

        Client clientAles = null;
        if (nr == 1) {
            String nume = InputHelper.citesteText("Nume Complet: ");
            String email = InputHelper.citesteText("Email: ");
            String telefon = InputHelper.citesteText("Telefon: ");
            clientAles = new Client(nume, email, telefon);
            clientService.adaugaClient(clientAles);
        } else if (nr == 2) {
            List<Client> listaClienti = new ArrayList<>(clientService.getClienitSortatiNume());
            if (listaClienti.isEmpty()) {
                System.out.println("Nu exista clienti in sistem. Te rog sa alegi varianta 1.");
                return;
            }
            for (int i = 0; i < listaClienti.size(); i++) {
                System.out.println((i + 1) + ". " + listaClienti.get(i).getNume());
            }
            int index = InputHelper.citesteInt("Alege ID-ul clientului: ") - 1;
            if (index >= 0 && index < listaClienti.size()) {
                clientAles = listaClienti.get(index);
            } else {
                System.out.println("Selectie invalida! Operatie anulata.");
                return;
            }
        } else {
            System.out.println("Optiune invalida");
            return;
        }

        System.out.println("Client selectat: " + clientAles.getNume());

        System.out.println("\n --- Alege clasa la care vrei sa participi ---");
        ClasaDans clasaAleasa = alegeClasa();

        if (clasaAleasa == null) {
            return;
        }

        System.out.println("Procesam rezervarea pentru " + clasaAleasa.getNume() + "...");
        try {
            rezervareService.rezervareClasa(clientAles, clasaAleasa);

        } catch (AbonamentInvalidException e) {
            System.out.println("Rezervare esuata: Acest client nu are un abonament valid sau a ramas fara sedinte.");
            System.out.println("Detalii tehnice: " + e.getMessage());

        } catch (CapacitateMaximaException e) {
            System.out.println("Rezervare esuata: Sala a atins capacitatea maxima!");
            System.out.println("Detalii tehnice: " + e.getMessage());
        }
    }


    private static ClasaDans alegeClasa(){
        List<ClasaDans> clase = studioService.getToateClasele(); // Asigură-te că ai făcut un getter în StudioService pt 'toateClasele'
        if (clase.isEmpty()) return null;
        for (int i = 0; i < clase.size(); i++) {
            System.out.println((i + 1) + ". " + clase.get(i).getNume());
        }
        int index = InputHelper.citesteInt("Alege ID clasa: ") - 1;
        return clase.get(index);
    }

    private static Studio alegeStudio() {
        List<Studio> studiouri = studioService.getStudiouri();
        if (studiouri.isEmpty()) return null;
        for (int i = 0; i < studiouri.size(); i++) {
            System.out.println((i + 1) + ". " + studiouri.get(i).getNume());
        }
        int index = InputHelper.citesteInt("Alege ID studio: ") - 1;
        return studiouri.get(index);
    }

    private static Profesor alegeProfesor(){
        List<Profesor> listaProfesori = new ArrayList<>(profesorService.getProfesori());
        if (listaProfesori.isEmpty()) {
            System.out.println("Nu exista profesori în sistem! Adauga un profesor din meniul principal mai intai.");
            return null;
        }
        System.out.println("Lista Profesorilor:");
        for (int i = 0; i < listaProfesori.size(); i++) {
            System.out.println((i + 1) + ". " + listaProfesori.get(i).getNume());
        }
        int index = InputHelper.citesteInt("Alege ID profesor: ") - 1;

        if (index >= 0 && index < listaProfesori.size()) {
            return listaProfesori.get(index);
        } else {
            System.out.println("Selectie invalida! Operatie anulata.");
            return null;
        }

    }

    private static void populeazaDateInitiale(ClientService cs, ProfesorService ps, StudioService ss){
        try{
            Studio studio1 = new Studio("Cobo", "Bucuresti - Centru");
            Sala sala11 = studio1.adaugaSala("Sala 1", 20);
            Sala sala12 = studio1.adaugaSala("Sala 2", 15);
            Sala sala13 = studio1.adaugaSala("Sala 3", 10);
            ss.adaugaStudio(studio1);

            Studio studio2 = new Studio("Prestige", "Bucuresti - Sector 2");
            Sala sala21 = studio2.adaugaSala("Sala 1", 20);
            Sala sala22 = studio2.adaugaSala("Sala 2", 17);
            Sala sala23 = studio2.adaugaSala("Sala 3", 15);
            ss.adaugaStudio(studio2);

            Studio studio3 = new Studio("Piticu", "Bucuresti - Sector 4");
            Sala sala31 = studio3.adaugaSala("Sala 1", 30);
            Sala sala32 = studio3.adaugaSala("Sala 2", 20);
            Sala sala33 = studio3.adaugaSala("Sala 3", 10);
            ss.adaugaStudio(studio3);

            Profesor prof1 = new Profesor("Ana", "ana@mail.com", "0711", StilDans.HIPHOP);
            ps.adaugaProfesor(prof1);
            Profesor prof2 = new Profesor("Ioana", "ioana@mail.com", "0712", StilDans.CHOREO);
            ps.adaugaProfesor(prof2);
            Profesor prof3 = new Profesor("Alex", "alex@mail.com", "0713", StilDans.COMMERCIAL);
            ps.adaugaProfesor(prof3);
            Profesor prof4 = new Profesor("Beatrice", "beatrice@mail.com", "0714", StilDans.WAACKING);
            ps.adaugaProfesor(prof4);
            Profesor prof5 = new Profesor("Ema", "ema@mail.com", "0715", StilDans.KPOP);
            ps.adaugaProfesor(prof5);
            Profesor prof6 = new Profesor("Andra", "andra@mail.com", "0716", StilDans.CONTEMPORAN);
            ps.adaugaProfesor(prof6);

            ClasaDans clasa1 = new ClasaDans("Commercial - Beginners", prof3, sala12);
            clasa1.setTip(prof3.getSpecializare());
            prof3.adaugaClasa(clasa1);
            clasa1.adaugaProgram(new Program(DayOfWeek.MONDAY, LocalTime.of(18, 0), LocalTime.of(19, 0)));
            clasa1.adaugaProgram(new Program(DayOfWeek.WEDNESDAY, LocalTime.of(18, 0), LocalTime.of(19, 0)));
            ss.adaugaClasaDans(clasa1);

            ClasaDans clasa2 = new ClasaDans("Hip-Hop Choreography", prof1, sala11);
            clasa2.setTip(prof1.getSpecializare());
            prof1.adaugaClasa(clasa2);
            clasa2.adaugaProgram(new Program(DayOfWeek.TUESDAY, LocalTime.of(19, 30), LocalTime.of(21, 0)));
            clasa2.adaugaProgram(new Program(DayOfWeek.THURSDAY, LocalTime.of(19, 30), LocalTime.of(21, 0)));
            ss.adaugaClasaDans(clasa2);

            ClasaDans clasa3 = new ClasaDans("K-Pop Cover Dance", prof5, sala21);
            clasa3.setTip(prof5.getSpecializare());
            prof5.adaugaClasa(clasa3);
            clasa3.adaugaProgram(new Program(DayOfWeek.MONDAY, LocalTime.of(17, 30), LocalTime.of(19, 0)));
            clasa3.adaugaProgram(new Program(DayOfWeek.FRIDAY, LocalTime.of(18, 0), LocalTime.of(19, 30)));
            ss.adaugaClasaDans(clasa3);

            ClasaDans clasa4 = new ClasaDans("Waacking Basics", prof4, sala22);
            clasa4.setTip(prof4.getSpecializare());
            prof4.adaugaClasa(clasa4);
            clasa4.adaugaProgram(new Program(DayOfWeek.WEDNESDAY, LocalTime.of(20, 0), LocalTime.of(21, 30)));
            ss.adaugaClasaDans(clasa4);

            ClasaDans clasa5 = new ClasaDans("Contemporan Flow", prof6, sala31);
            clasa5.setTip(prof6.getSpecializare());
            prof6.adaugaClasa(clasa5);
            clasa5.adaugaProgram(new Program(DayOfWeek.SATURDAY, LocalTime.of(10, 0), LocalTime.of(12, 0)));
            clasa5.adaugaProgram(new Program(DayOfWeek.SUNDAY, LocalTime.of(10, 0), LocalTime.of(12, 0)));
            ss.adaugaClasaDans(clasa5);

            cs.adaugaClient(new Client("Maria Popescu", "maria@mail.com", "0722"));
            cs.adaugaClient(new Client("Alex Ionescu", "alex@mail.com", "0733"));
            cs.adaugaClient(new Client("Elena Dumitru", "elena@mail.com", "0744"));
            cs.adaugaClient(new Client("Andrei Radu", "andrei@mail.com", "0755"));
            System.out.println("[Sistem] Baza de date a fost încărcată automat!\n");
        } catch (SuprapunereProgramException e){
            System.out.println("Eroare la datele initiale: " + e.getMessage());
        }
    }
}
