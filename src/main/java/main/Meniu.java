package main;

import main.model.*;
import main.repository.ClasaDansRepository;
import main.repository.ReviewRepository;
import main.service.*;
import main.exceptions.*;
import main.util.InputHelper;

import java.util.ArrayList;
import java.util.List;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

public class Meniu{
    private  ClientService clientService = new ClientService();
    private ProfesorService profesorService = new ProfesorService();
    private StudioService studioService = new StudioService();
    private RezervareService rezervareService = new RezervareService();
    private ReviewRepository reviewRepo = new ReviewRepository();
    private ClasaDansRepository clasaDansRepository = new ClasaDansRepository();

    public void start(){
        main.config.DatabaseInitializer.initializeDatabase();

        System.out.println("=== Dance Studio Manager ===");
        int optiune = -1;
        while (optiune != 0){
            afiseazaMeniu();
            optiune = InputHelper.citesteIntInRange("Alege o optiune: ", 0, 14);
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
                        int idClient = InputHelper.citesteIntInRange("\nSelecteaza ID client pentru detalii (0 = inapoi): ", 0, Integer.MAX_VALUE);
                        if (idClient == 0) break;
                        Client clientSelectat = clientService.findById(idClient);
                        if (clientSelectat == null) {
                            System.out.println("Client inexistent.");
                            break;
                        }
                        System.out.println("\n--- Detalii client ---");
                        clientSelectat.print();
                        if (clientSelectat.getAbonament() != null) {
                            System.out.println("Are abonament: " + clientSelectat.getAbonament().getClass().getSimpleName().replace("Abonament", ""));
                            System.out.println("Sedinte ramase: " + clientSelectat.getAbonament().getSedinteRamase());
                            if (clientSelectat.getAbonament().getDataExpirare() != null) {
                                System.out.println("Expira la: " + clientSelectat.getAbonament().getDataExpirare());
                            } else {
                                System.out.println("Expira la: Neactivat (se activeaza la prima rezervare)");
                            }
                        } else {
                            System.out.println("Acest client nu are abonament.");
                        }
                        break;
                    case 9:
                        System.out.println("\n ---Lista profesori ---");
                        profesorService.afiseazaProfesori();
                        int idProf = InputHelper.citesteIntInRange("Selecteaza ID profesor pentru actiuni (0 = inapoi): ", 0, Integer.MAX_VALUE);
                        if (idProf == 0) break;
                        Profesor profSelectat = profesorService.findById(idProf);
                        if (profSelectat == null) {
                            System.out.println("Profesor inexistent.");
                            break;
                        }
                        System.out.println("Ai selectat: " + profSelectat.getNume());
                        System.out.println("1. Adauga un stil nou");
                        System.out.println("2. Vezi detalii");
                        System.out.println("0. Inapoi");
                        int optP = InputHelper.citesteIntInRange("Alege optiunea: ", 0, 2);
                        if (optP == 1) {
                            StilDans[] stiluri = StilDans.values();
                            for (int i = 0; i < stiluri.length; i++) {
                                System.out.println((i+1) + ". " + stiluri[i]);
                            }
                            int s = InputHelper.citesteIntInRange("Alege stilul (numar): ", 1, stiluri.length);
                            StilDans stilAles = stiluri[s - 1];
                            if (profSelectat.getSpecializari().contains(stilAles)) {
                                System.out.println("Profesorul deja preda acest stil.");
                            } else {
                                profesorService.addStyleToProfesor(profSelectat.getId(), stilAles);
                                profSelectat.setSpecializare(stilAles);
                                System.out.println("Stil adaugat cu succes.");
                            }
                        } else if (optP == 2) {
                            profSelectat.afisare();
                        }
                        break;
                    case 10:
                        meniuAdaugaReview(); break;
                    case 11:
                        meniuCautareClient();
                        break;
                    case 12:
                        meniuCautareProfesor();
                        break;
                    case 13:
                        meniuFiltreazaStil();
                        break;
                    case 14:
                        meniuFiltreazaDificultate();
                        break;
                    case 0:
                        System.out.println("La revedere! Sistem inchis.");
                        break;
                    default:
                        System.out.println("Optiune invalida!");
                }
                if(optiune != 0){
                    System.out.println("\n---------------------------------");
                    InputHelper.citesteText("Apasa ENTER pentru a te intoarce la meniu...");

                }
            }catch (Exception e) {
                System.out.println("Eroare la executie: " + e.getMessage());
                InputHelper.citesteText("Apasa ENTER pentru a continua...");
            }
        }
    }

    private void afiseazaMeniu(){
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
        System.out.println("10. Adauga un review");
        System.out.println("11. Cauta client dupa nume");
        System.out.println("12. Cauta profesor dupa nume");
        System.out.println("13. Filtreaza clase dupa stilul de dans");
        System.out.println("14. Filtreaza clase dupa dificultate");
        System.out.println("0. Iesire" );
        System.out.println("====================");

    }

    private ClasaDans alegeClasa(boolean showStyleInfo){
        List<ClasaDans> clase  = studioService.getToateClasele();
        if(clase.isEmpty()){
            System.out.println("Nu exista clase in sistem.");
            return null;
        }
        while(true){
            System.out.println("\nLista claselor disponibile:");
            for(int i = 0 ; i< clase.size(); i++){
                ClasaDans c = clase.get(i);
                System.out.println((i+1) + ". " + c.getNume() + " [Stil: " + c.getTip() + "]");

            }
            int index;
            if(showStyleInfo){
                System.out.println("0. Info: Citeste detalii despre stilurile de dans");
                index = InputHelper.citesteIntInRange("Alege ID clasa (sau 0 pentru info):", 0, clase.size());
            }
            else {
                index = InputHelper.citesteIntInRange("Alege ID clasa:", 1, clase.size());
            }

            if (index == 0 && showStyleInfo) {
                System.out.println("\n----- 🕺 INFO STILURI -----");
                for (StilDans s : StilDans.values()) {
                    System.out.println("🔸 " + s.name() + ": " + s.getDescriere());
                }
                System.out.println("------------------------------\n");
            } else if (index >= 1 && index <= clase.size()) {
                ClasaDans clasaSelectata = clase.get(index - 1);
                System.out.println("\n>> Ai selectat clasa: " + clasaSelectata.getNume());
                System.out.println("1. Confirma selectia | 2. Citeste review-urile acestei clase | 3. Inapoi la lista");
                int opt = InputHelper.citesteIntInRange("Alege o optiune: ", 1, 3);

                if (opt == 1) return clasaSelectata;
                else if (opt == 2) {
                    System.out.println("\n--- Review-uri ---");
                    clasaSelectata.afiseazaReviewUri();
                    System.out.println("------------------");
                }
                // if 3 -> loop again
            } else {
                System.out.println("Selectie invalida! Reincearca.");
            }
        }
    }

    private void meniuAdaugaReview() {
        System.out.println("\n--- Lasa un review ---");

        List<Client> clienti = new ArrayList<>(clientService.getClientiSortatiNume());
        if(clienti.isEmpty()) {
            System.out.println("Nu exista clienti in sistem pentru a lasa un review.");
            return;
        }
        System.out.println("Cine lasa review-ul?");
        for (int i = 0; i < clienti.size(); i++) {
            System.out.println((i + 1) + ". " + clienti.get(i).getNume());
        }
        int indexClient = InputHelper.citesteIntInRange("Alege numarul clientului din lista: ", 1, clienti.size()) - 1;
        Client autor = clienti.get(indexClient);

        int nota = InputHelper.citesteIntInRange("Acorda o nota (1-5): ", 1, 5);
        String text = InputHelper.citesteNonEmptyString("Lasa un scurt comentariu: ");

        System.out.println("La ce vrei sa lasi review? 1. Profesor | 2. Clasa de dans");
        int opt = InputHelper.citesteIntInRange("Alegere: ", 1, 2);

        if (opt == 1) {
            Profesor prof = alegeProfesor();
            if (prof != null) {
                Review review = new Review(autor, prof, nota, text);
                prof.adaugaReview(review);
                reviewRepo.createForProfesor(autor.getId(), prof.getId(), nota, text);
                System.out.println("Review adaugat! Iata review-urile lui " + prof.getNume() + ":");
                prof.afiseazaReviewUri();
            }
        } else if (opt == 2) {
            ClasaDans clasa = alegeClasa(true);
            if (clasa != null) {
                Review review = new Review(autor, clasa, nota, text);
                clasa.adaugaReview(review);
                reviewRepo.createForClass(autor.getId(), clasa.getId(), nota, text);
                System.out.println("Review adaugat clasei " + clasa.getNume() + "!");
            }
        }
    }

    private void meniuCautareClient(){
        System.out.println("\n--- Cauta client ---");
        String nume = InputHelper.citesteNonEmptyString("Introdu numele: ");
        clientService.cautaClientDupaNume(nume);
    }

    private void meniuCautareProfesor() {
        System.out.println("\n--- Cauta profesor ---");
        String nume = InputHelper.citesteNonEmptyString("Introdu numele: ");
        List<Profesor> rezultate = profesorService.cautaProfesorDupaNume(nume);

        if (rezultate.isEmpty()) {
            System.out.println(" Nu s-a gasit niciun profesor cu numele: " + nume);
            return;
        }

        System.out.println("\nRezultatele cautarii:");
        for (int i = 0; i < rezultate.size(); i++) {
            System.out.println((i+1) + ". " + rezultate.get(i).getNume() + " (Stil: " + rezultate.get(i).getSpecializare() + ")");
        }

        int index = InputHelper.citesteIntInRange("\nAlege ID-ul profesorului pentru profil (sau 0 pentru a iesi): ", 0, rezultate.size());
        if (index == 0) return;

        Profesor profAles = rezultate.get(index - 1);

        System.out.println("\n--- Profil Profesor ---");
        profAles.afisare();
        int optiuneReview = InputHelper.citesteIntInRange("\nVrei sa afisezi review-urile pentru acest profesor? (1. Da | 2. Nu): ", 1, 2);

        if (optiuneReview == 1) {
            System.out.println("\n--- Review-uri " + profAles.getNume() + " ---");
            profAles.afiseazaReviewUri();
            System.out.println("-------------------------");
        } else {
            System.out.println("Ne intoarcem la meniu...");
        }
    }

    private void meniuFiltreazaStil(){
        System.out.println("\n--- Filtreaza clase dupa stilul de dans ---");
        StilDans[] stiluri = StilDans.values();
        for(int i = 0; i< stiluri.length; i++){
            System.out.println((i+1) + ". " +stiluri[i].name());
        }
        int idStil = InputHelper.citesteIntInRange("Alege ID-ul stilului: ", 1, stiluri.length) -1;
        System.out.println("\n--- Rezultatele pentru " + stiluri[idStil].name() + " ---");
        studioService.filtreazaClaseDupaStil(stiluri[idStil]);
    }

    private void meniuAfisareProgram(){
        System.out.println("\n --- PROGRAM ---");
        Studio studio = alegeStudio();
        if (studio == null) return;

        System.out.println("Zile: 1.Luni | 2.Marti | 3. Miercuri | 4. Joi | 5. Vineri | 6. Sambata | 7. Duminica");
        int ziua = InputHelper.citesteIntInRange("Alege ziua: ", 1, 7);
        DayOfWeek zi = DayOfWeek.of(ziua);
        studioService.afiseazaProgram(studio, zi);

    }

    private void meniuFiltreazaDificultate() {
        System.out.println("\n--- Filtrare clase dupa dificultate ---");
        Dificultate[] dificultati = Dificultate.values();
        for (int i = 0; i < dificultati.length; i++) {
            System.out.println((i + 1) + ". " + dificultati[i].name());
        }
        int idDif = InputHelper.citesteIntInRange("Alege ID-ul dificultatii: ", 1, dificultati.length) - 1;
        System.out.println("\n--- Rezultate pentru " + dificultati[idDif].name() + " ---");
        studioService.filtreazaClaseDupaDificultate(dificultati[idDif]);
    }

    private void meniuAdaugaClient() {
        System.out.println("\n --- Adauga client --- " );
        String nume = InputHelper.citesteNonEmptyString("Nume Complet: ");
        String email = InputHelper.citesteNonEmptyString("Email: ");
        String telefon = InputHelper.citesteNonEmptyString("Telefon: ");
        Client c = new Client(nume, email, telefon);

        clientService.adaugaClient(c);
        System.out.println("Clientul " + nume + " a fost adaugat cu succes!");

    }

    private void meniuAdaugaProfesor(){
        System.out.println("\n --- Adauga profesor --- " );
        String nume = InputHelper.citesteNonEmptyString("Nume: ");
        String email = InputHelper.citesteNonEmptyString("Email: ");
        String telefon = InputHelper.citesteNonEmptyString("Telefon: ");

        System.out.println("Alege stilul de dans: ");
        StilDans[] stiluri = StilDans.values();
        for(int i = 0; i < stiluri.length; i++){
            System.out.println((i+1) + ". " + stiluri[i]);
        }
        int index = InputHelper.citesteIntInRange("Alege ID -ul stilului: ", 1, stiluri.length) -1;

        // construim set cu un singur stil la creare
        java.util.Set<StilDans> set = new java.util.HashSet<>();
        set.add(stiluri[index]);

        Profesor p = new Profesor(nume, email, telefon, set);
        profesorService.adaugaProfesor(p);
        System.out.println("Profesorul " + nume + " a fost adaugat cu succes!");

    }

    private void meniuAdaugaClasa(){
        System.out.println("\n --- Adauga clasa --- " );
        String numeClasa = InputHelper.citesteNonEmptyString("Numele clasei: ");

        Profesor prof = alegeProfesor();
        if (prof == null) return;

        Studio studio = alegeStudio();
        if(studio == null) return;

        System.out.println("Sali disponibile in " + studio.getNume() + ":");
        for(int i = 0 ; i < studio.getSali().size() ; i++){
            System.out.println((i+1) + ". " + studio.getSali().get(i).getNume());
        }
        int index = InputHelper.citesteIntInRange("Alege ID sala: ", 1, studio.getSali().size())-1;
        Sala sala = studio.getSali().get(index);

        System.out.println("Stiluri disponibile:");
        StilDans[] stiluri = StilDans.values();
        for (int i = 0; i < stiluri.length; i++) {
            System.out.println((i+1) + ". " + stiluri[i].name());
        }
        int stilIdx = InputHelper.citesteIntInRange("Alege stilul (numar): ", 1, stiluri.length);
        StilDans stil = stiluri[stilIdx - 1];

        // verificare: profesor trebuie sa predea stilul ales
        if (!prof.getSpecializari().contains(stil)) {
            System.out.println("Profesorul selectat nu preda stilul " + stil + ". Alege alt profesor sau schimba stilul clasei.");
            return;
        }

        System.out.println("Dificultate (1.BEGINNER | 2.INTERMEDIATE | 3.ADVANCED):");
        int difIndex = InputHelper.citesteIntInRange("Alege: ", 1, Dificultate.values().length) - 1;
        Dificultate dif = Dificultate.values()[difIndex];

        int ziIdx = InputHelper.citesteIntInRange("Alege ziua (1-7): ", 1, 7);
        DayOfWeek zi = DayOfWeek.of(ziIdx);

        LocalTime oraStart = parseOra("Ora start (HH:mm): ");
        LocalTime oraFinal = parseOra("Ora final (HH:mm): ");

        ClasaDans clasa = new ClasaDans(numeClasa, prof, sala, dif);
        Program p = new Program(zi, oraStart, oraFinal);
        clasa.getProgram().add(p);
        clasa.setTip(stil);
        prof.adaugaClasa(clasa);
        studioService.adaugaClasaDans(clasa);
        System.out.println("Clasa a fost creata cu succes!");
    }


    private void meniuStergeClasa(){
        System.out.println("\n --- Sterge clasa din program ---");

        ClasaDans clasa = alegeClasa(false);
        if (clasa == null) {
            System.out.println("Operatie anulata.");
            return;
        }

        // daca nu are program
        if (clasa.getProgram() == null || clasa.getProgram().isEmpty()) {
            System.out.println("Clasa selectata nu are intervale de orar de sters.");
            String confirmWhole = InputHelper.citesteNonEmptyString("Vrei sa stergi intreaga clasa? (da/nu): ");
            if ("da".equalsIgnoreCase(confirmWhole)) {
                clasaDansRepository.deleteClassById(clasa.getId());
                System.out.println("Clasa si datele asociate au fost sterse.");
            } else {
                System.out.println("Operatie anulata.");
            }
            return;
        }

        System.out.println("1. Sterge un interval | 2. Sterge intreaga clasa | 0. Anuleaza");
        int opt = InputHelper.citesteIntInRange("Alege optiunea: ", 0, 2);
        if (opt == 0) {
            System.out.println("Operatie anulata.");
            return;
        }

        if (opt == 1) {
            System.out.println("Orarul curent al clasei " + clasa.getNume() + ": ");
            for (int i = 0; i < clasa.getProgram().size(); i++) {
                System.out.println((i + 1) + ". " + clasa.getProgram().get(i));
            }
            int idx = InputHelper.citesteIntInRange("Alege ID-ul intervalului: ", 1, clasa.getProgram().size()) - 1;
            Program p = clasa.getProgram().get(idx);

            clasaDansRepository.deleteProgramSlot(clasa.getId(), p.getZi(), p.getOraStart(), p.getOraFinal());
            clasa.getProgram().remove(idx);
            System.out.println("Interval sters.");
            
            if (clasa.getProgram().isEmpty()) {
                clasaDansRepository.deleteClassById(clasa.getId());
                System.out.println("Acesta a fost ultimul interval al clasei. Clasa '" + clasa.getNume() + "' a fost stearsa complet din sistem automat.");
            }

        } else if (opt == 2) {
            String confirm = InputHelper.citesteNonEmptyString("Stergi clasa si toate datele asociate? (da/nu): ");
            if ("da".equalsIgnoreCase(confirm)) {
                clasaDansRepository.deleteClassById(clasa.getId());
                System.out.println("Clasa si datele asociate au fost sterse.");
            } else {
                System.out.println("Operatie anulata.");
            }
        }
    }

    private void meniuCumparaAbonament(){
        System.out.println("\n --- Cumpara abonament ---");
        System.out.println("1.Introdu datele clientului | 2. Alege din lista de clienti ");
        int nr = InputHelper.citesteIntInRange("Alege varianta: ", 1, 2);

        Client clientAles = null;
        if (nr == 1 ){
            String nume = InputHelper.citesteNonEmptyString("Nume Complet: ");
            String email = InputHelper.citesteNonEmptyString("Email: ");
            String telefon = InputHelper.citesteNonEmptyString("Telefon: ");
            clientAles = new Client(nume,email,telefon);

            clientService.adaugaClient(clientAles);

        }
        else {
            List<Client> listaClienti = new ArrayList<>(clientService.getClientiSortatiNume());
            if(listaClienti.isEmpty()){
                System.out.println("Nu exista clienti in sistem. Te rog sa alegi varianta 1.");
                return;
            }
            for (int i = 0; i < listaClienti.size(); i++){
                System.out.println((i+1) + ". " + listaClienti.get(i).getNume());
            }
            int index = InputHelper.citesteIntInRange("Alege numarul clientului din lista: ", 1, listaClienti.size()) -1;
            clientAles = listaClienti.get(index);
        }

        System.out.println("Client selectat: " + clientAles.getNume());

        System.out.println("Tipuri:\n 1. Single (80 RON, valabil o luna) \n 2. Silver (4 ședințe / luna) \n 3. Gold (8 ședințe / luna)");
        int tip = InputHelper.citesteIntInRange("Alege tipul: ", 1, 3);
        Abonament abNou = null;
        if(tip == 1){
            abNou = new AbonamentSingle();
        }
        else {
            System.out.println("Perioada: 1 luna | 3 luni | 6 luni.");
            int luni = InputHelper.citesteIntInRange("Alege numarul de luni: ", 1, 12);
            if(tip == 2 ) abNou = new AbonamentSilver(luni);
            else if(tip == 3 ) abNou = new AbonamentGold(luni);
        }

        if(abNou != null){
            int metoda = InputHelper.citesteIntInRange("Metoda de plata (1.cash/2.card): ", 1, 2);
            if (metoda == 1) rezervareService.cumparaAbonament(clientAles,abNou,"cash");
            else rezervareService.cumparaAbonament(clientAles,abNou,"card");
        }
    }

    private void meniuRezervaClasa(){
        System.out.println("\n --- Rezerva o clasa ---");
        System.out.println("1.Introdu datele clientului | 2. Alege din lista de clienti ");
        int nr = InputHelper.citesteIntInRange("Alege varianta: ", 1, 2);

        Client clientAles = null;
        if (nr == 1) {
            String nume = InputHelper.citesteNonEmptyString("Nume Complet: ");
            String email = InputHelper.citesteNonEmptyString("Email: ");
            String telefon = InputHelper.citesteNonEmptyString("Telefon: ");
            clientAles = new Client(nume, email, telefon);
            clientService.adaugaClient(clientAles);
        } else {
            List<Client> listaClienti = new ArrayList<>(clientService.getClientiSortatiNume());
            if (listaClienti.isEmpty()) {
                System.out.println("Nu exista clienti in sistem. Te rog sa alegi varianta 1.");
                return;
            }
            for (int i = 0; i < listaClienti.size(); i++) {
                System.out.println((i + 1) + ". " + listaClienti.get(i).getNume());
            }
            int index = InputHelper.citesteIntInRange("Alege numarul clientului din lista: ", 1, listaClienti.size()) - 1;
            clientAles = listaClienti.get(index);
        }

        System.out.println("Client selectat: " + clientAles.getNume());

        System.out.println("\n --- Alege clasa la care vrei sa participi ---");
        ClasaDans clasaAleasa = alegeClasa(true);

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

    private Studio alegeStudio() {
        List<Studio> studiouri = studioService.getStudiouri();
        if (studiouri.isEmpty()) return null;
        for (int i = 0; i < studiouri.size(); i++) {
            System.out.println((i + 1) + ". " + studiouri.get(i).getNume());
        }
        int index = InputHelper.citesteIntInRange("Alege ID studio: ", 1, studiouri.size()) - 1;
        return studiouri.get(index);
    }

    private Profesor alegeProfesor(){
        List<Profesor> listaProfesori = new ArrayList<>(profesorService.getProfesoriSortatiNume());
        if (listaProfesori.isEmpty()) {
            System.out.println("Nu exista profesori în sistem! Adauga un profesor din meniul principal mai intai.");
            return null;
        }
        System.out.println("Lista Profesorilor:");
        for (int i = 0; i < listaProfesori.size(); i++) {
            System.out.println((i+1) + ". " + listaProfesori.get(i).getNume());
        }
        int index = InputHelper.citesteIntInRange("Alege ID profesor: ", 1, listaProfesori.size()) - 1;

        return listaProfesori.get(index);
    }

    private LocalTime parseOra(String prompt) {
        while (true) {
            String s = InputHelper.citesteNonEmptyString(prompt);
            try {
                return LocalTime.parse(s);
            } catch (DateTimeParseException ex) {
                System.out.println("Format ora invalid. Foloseste HH:mm (ex: 18:30).");
            }
        }
    }
}