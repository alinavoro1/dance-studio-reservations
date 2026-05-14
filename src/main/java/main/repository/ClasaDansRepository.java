package main.repository;

import main.config.DatabaseConnection;
import main.model.ClasaDans;
import main.model.Program;
import main.model.StilDans;
import main.model.Dificultate;
import main.model.Sala;
import main.model.Profesor;
import main.model.Review;

import java.sql.*;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class ClasaDansRepository {

    public void create(ClasaDans c, int salaId, int profesorId) {
        String sqlClasa = "INSERT INTO clase_dans(nume, stil, dificultate, sala_id, profesor_id) VALUES (?, ?, ?, ?, ?)";
        String sqlProgram = "INSERT INTO program_clase(clasa_id, ziua, ora_inceput, ora_sfarsit) VALUES (?, ?, ?, ?)";

        try (Connection con = DatabaseConnection.getConnection()) {
            con.setAutoCommit(false);
            try (PreparedStatement stmt = con.prepareStatement(sqlClasa, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, c.getNume());
                stmt.setString(2, c.getTip() != null ? c.getTip().name() : null);
                stmt.setString(3, c.getDificultate() != null ? c.getDificultate().name() : null);
                stmt.setInt(4, salaId);
                stmt.setInt(5, profesorId);
                stmt.executeUpdate();

                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    int clasaId = rs.getInt(1);
                    try (PreparedStatement stmtP = con.prepareStatement(sqlProgram)) {
                        for (Program p : c.getProgram()) {
                            stmtP.setInt(1, clasaId);
                            stmtP.setString(2, p.getZi().name());
                            stmtP.setTime(3, java.sql.Time.valueOf(p.getOraStart()));
                            stmtP.setTime(4, java.sql.Time.valueOf(p.getOraFinal()));
                            stmtP.addBatch();
                        }
                        stmtP.executeBatch();
                    }
                }
                con.commit();
            } catch (SQLException e) {
                con.rollback();
                throw e;
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    public List<ClasaDans> findAll() {
        List<ClasaDans> clase = new ArrayList<>();
        String sql = "SELECT id, nume, stil, dificultate, sala_id, profesor_id FROM clase_dans";
        SalaRepository salaRepo = new SalaRepository();
        ProfesorRepository profRepo = new ProfesorRepository();
        ReviewRepository reviewRepo = new ReviewRepository();

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                ClasaDans c = new ClasaDans();
                c.setId(rs.getInt("id"));
                c.setNume(rs.getString("nume"));

                String stilStr = rs.getString("stil");
                if (stilStr != null) c.setTip(StilDans.valueOf(stilStr));

                String difStr = rs.getString("dificultate");
                if (difStr != null) c.setDificultate(Dificultate.valueOf(difStr));

                int salaId = rs.getInt("sala_id");
                if (!rs.wasNull()) {
                    try (PreparedStatement ps = con.prepareStatement("SELECT id, nume, capacitate, studio_id FROM sali WHERE id = ?")) {
                        ps.setInt(1, salaId);
                        try (ResultSet rs2 = ps.executeQuery()) {
                            if (rs2.next()) {
                                Sala sala = new Sala(rs2.getString("nume"), rs2.getInt("capacitate"), null);
                                sala.setId(rs2.getInt("id"));
                                c.getProgram(); // ensure list exists
                                c.setSala(sala);
                            }
                        }
                    }
                }

                int profId = rs.getInt("profesor_id");
                if (!rs.wasNull()) {
                    Profesor p = profRepo.findById(profId);
                    c.setProfesor(p);
                    p.getClase().add(c);
                    p.calculeazaSuma();
                }
                loadProgramForClass(c, con);
                List<Review> reviews = reviewRepo.findByClasa(c);
                for (Review r : reviews) c.adaugaReview(r);

                clase.add(c);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return clase;
    }

    public ClasaDans findById(int id) {
        String sql = "SELECT id, nume, stil, dificultate, sala_id, profesor_id FROM clase_dans WHERE id = ?";
        ReviewRepository reviewRepo = new ReviewRepository();
        ProfesorRepository profRepo = new ProfesorRepository();

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    ClasaDans c = new ClasaDans();
                    c.setId(rs.getInt("id"));
                    c.setNume(rs.getString("nume"));

                    String stilStr = rs.getString("stil");
                    if (stilStr != null) c.setTip(StilDans.valueOf(stilStr));

                    String difStr = rs.getString("dificultate");
                    if (difStr != null) c.setDificultate(Dificultate.valueOf(difStr));

                    int salaId = rs.getInt("sala_id");
                    if (!rs.wasNull()) {
                        try (PreparedStatement ps = con.prepareStatement("SELECT id, nume, capacitate, studio_id FROM sali WHERE id = ?")) {
                            ps.setInt(1, salaId);
                            try (ResultSet rs2 = ps.executeQuery()) {
                                if (rs2.next()) {
                                    Sala sala = new Sala(rs2.getString("nume"), rs2.getInt("capacitate"), null);
                                    sala.setId(rs2.getInt("id"));
                                    c.setSala(sala);
                                }
                            }
                        }
                    }

                    int profId = rs.getInt("profesor_id");
                    if (!rs.wasNull()) {
                        Profesor p = profRepo.findById(profId);
                        c.setProfesor(p);
                        p.getClase().add(c);
                        p.calculeazaSuma();
                    }

                    loadProgramForClass(c, con);

                    List<Review> reviews = reviewRepo.findByClasa(c);
                    for (Review r : reviews) c.adaugaReview(r);

                    return c;
                }
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return null;
    }

    public List<ClasaDans> findByProfesorId(int profesorId) {
        List<ClasaDans> clase = new ArrayList<>();
        String sql = "SELECT id, nume, stil, dificultate, sala_id FROM clase_dans WHERE profesor_id = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, profesorId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ClasaDans c = new ClasaDans();
                    c.setId(rs.getInt("id"));
                    c.setNume(rs.getString("nume"));

                    String stilStr = rs.getString("stil");
                    if (stilStr != null) c.setTip(StilDans.valueOf(stilStr));

                    String difStr = rs.getString("dificultate");
                    if (difStr != null) c.setDificultate(Dificultate.valueOf(difStr));

                    int salaId = rs.getInt("sala_id");
                    if (!rs.wasNull()) {
                        try (PreparedStatement ps = con.prepareStatement("SELECT id, nume, capacitate, studio_id FROM sali WHERE id = ?")) {
                            ps.setInt(1, salaId);
                            try (ResultSet rs2 = ps.executeQuery()) {
                                if (rs2.next()) {
                                    Sala sala = new Sala(rs2.getString("nume"), rs2.getInt("capacitate"), null);
                                    sala.setId(rs2.getInt("id"));
                                    c.setSala(sala);
                                }
                            }
                        }
                    }

                    loadProgramForClass(c, con);

                    clase.add(c);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return clase;
    }

    public static class Session {
        public int clasaId;
        public String clasaNume;
        public Time oraStart;
        public Time oraFinal;
        public String salaNume;
        public int salaId;
        public String profesorNume;
        public int profesorId;
    }

    public List<Session> getSessionsForStudioAndDay(int studioId, String ziCautata) {
        String sql = "SELECT c.id AS clasa_id, c.nume AS clasa_nume, p.ora_inceput, p.ora_sfarsit, s.id AS sala_id, s.nume as sala_nume, prof.id as profesor_id, prof.nume as profesor_nume " +
                "FROM clase_dans c " +
                "JOIN sali s ON c.sala_id = s.id " +
                "LEFT JOIN profesori prof ON c.profesor_id = prof.id " +
                "JOIN program_clase p ON c.id = p.clasa_id " +
                "WHERE s.studio_id = ? AND p.ziua = ? " +
                "ORDER BY p.ora_inceput";

        List<Session> rez = new ArrayList<>();
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, studioId);
            stmt.setString(2, ziCautata);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Session s = new Session();
                    s.clasaId = rs.getInt("clasa_id");
                    s.clasaNume = rs.getString("clasa_nume");
                    s.oraStart = rs.getTime("ora_inceput");
                    s.oraFinal = rs.getTime("ora_sfarsit");
                    s.salaId = rs.getInt("sala_id");
                    s.salaNume = rs.getString("sala_nume");
                    s.profesorId = rs.getInt("profesor_id");
                    s.profesorNume = rs.getString("profesor_nume");
                    rez.add(s);
                }
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return rez;
    }

    public void afiseazaClasePentruStudio(int studioId, String ziCautata) {
        List<Session> sesiuni = getSessionsForStudioAndDay(studioId, ziCautata);
        if (sesiuni.isEmpty()) {
            System.out.println("Nu exista clase programate pentru aceasta zi.");
            return;
        }
        for (Session s : sesiuni) {
            System.out.println(s.oraStart + " - " + s.oraFinal +
                    " | " + s.clasaNume +
                    " | Prof: " + (s.profesorNume != null ? s.profesorNume : "—") +
                    " @ " + (s.salaNume != null ? s.salaNume : "—"));
        }
    }

    private void loadProgramForClass(ClasaDans clasa, Connection con) throws SQLException {
        String sql = "SELECT ziua, ora_inceput, ora_sfarsit FROM program_clase WHERE clasa_id = ?";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, clasa.getId());
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    DayOfWeek zi = DayOfWeek.valueOf(rs.getString("ziua"));
                    Time tStart = rs.getTime("ora_inceput");
                    Time tFinal = rs.getTime("ora_sfarsit");
                    if (tStart != null && tFinal != null) {
                        LocalTime oraStart = tStart.toLocalTime();
                        LocalTime oraFinal = tFinal.toLocalTime();
                        clasa.getProgram().add(new Program(zi, oraStart, oraFinal));
                    }
                }
            }
        }
    }

    public void afiseazaDupaStil(String stil) {
        String sql = "SELECT id, nume, stil, dificultate FROM clase_dans WHERE stil = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, stil);
            try (ResultSet rs = stmt.executeQuery()) {
                boolean gasit = false;
                while (rs.next()) {
                    gasit = true;
                    System.out.println("[" + rs.getInt("id") + "] " + rs.getString("nume")
                            + " | Stil: " + rs.getString("stil")
                            + " | Dificultate: " + rs.getString("dificultate"));
                }
                if (!gasit) {
                    System.out.println("Nu s-au gasit clase pentru stilul: " + stil);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void afiseazaDupaDificultate(String dificultate) {
        String sql = "SELECT id, nume, stil, dificultate FROM clase_dans WHERE dificultate = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, dificultate);
            try (ResultSet rs = stmt.executeQuery()) {
                boolean gasit = false;
                while (rs.next()) {
                    gasit = true;
                    System.out.println("[" + rs.getInt("id") + "] " + rs.getString("nume")
                            + " | Stil: " + rs.getString("stil")
                            + " | Dificultate: " + rs.getString("dificultate"));
                }
                if (!gasit) {
                    System.out.println("Nu exista clase cu dificultatea: " + dificultate);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteProgramSlot(int clasaId, DayOfWeek zi, LocalTime oraStart, LocalTime oraFinal) {
        String sql = "DELETE FROM program_clase WHERE clasa_id = ? AND ziua = ? AND ora_inceput = ? AND ora_sfarsit = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, clasaId);
            stmt.setString(2, zi.name());
            stmt.setTime(3, Time.valueOf(oraStart));
            stmt.setTime(4, Time.valueOf(oraFinal));
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void deleteClassById(int clasaId) {
        String delProgram = "DELETE FROM program_clase WHERE clasa_id = ?";
        String delRezervari = "DELETE FROM rezervari WHERE clasa_id = ?";
        String delReview = "DELETE FROM reviewuri WHERE clasa_id = ?";
        String delClasa = "DELETE FROM clase_dans WHERE id = ?";
        try (Connection con = DatabaseConnection.getConnection()) {
            con.setAutoCommit(false);
            try (PreparedStatement p1 = con.prepareStatement(delProgram);
                 PreparedStatement p2 = con.prepareStatement(delRezervari);
                 PreparedStatement p3 = con.prepareStatement(delReview);
                 PreparedStatement p4 = con.prepareStatement(delClasa)) {

                p1.setInt(1, clasaId); p1.executeUpdate();
                p2.setInt(1, clasaId); p2.executeUpdate();
                p3.setInt(1, clasaId); p3.executeUpdate();
                p4.setInt(1, clasaId); p4.executeUpdate();
                con.commit();
            } catch (SQLException ex) {
                con.rollback();
                throw ex;
            } finally {
                con.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}