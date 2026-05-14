package main.repository;

import main.config.DatabaseConnection;
import main.model.Profesor;
import main.model.StilDans;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProfesorRepository {

    public void create(Profesor p) {
        String sql = "INSERT INTO profesori(nume, email, telefon) VALUES (?, ?, ?)";
        String sqlStyle = "INSERT INTO profesor_stil(profesor_id, stil) VALUES (?, ?)";
        try (Connection con = DatabaseConnection.getConnection()) {
            con.setAutoCommit(false);
            try (PreparedStatement stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, p.getNume());
                stmt.setString(2, p.getEmail());
                stmt.setString(3, p.getTelefon());
                stmt.executeUpdate();

                try (ResultSet keys = stmt.getGeneratedKeys()) {
                    if (keys.next()) {
                        int genId = keys.getInt(1);
                        p.setId(genId);
                    } else {
                        throw new SQLException("Nu s-a generat id-ul pentru profesor.");
                    }
                }
            }
            try (PreparedStatement ps = con.prepareStatement(sqlStyle)) {
                for (StilDans s : p.getSpecializari()) {
                    ps.setInt(1, p.getId());
                    ps.setString(2, s.name());
                    ps.addBatch();
                }
                ps.executeBatch();
            }

            con.commit();
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la crearea profesorului: " + e.getMessage(), e);
        }
    }

    public List<Profesor> findAll() {
        List<Profesor> profesori = new ArrayList<>();
        ReviewRepository reviewRepo = new ReviewRepository();
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement("SELECT * FROM profesori");
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Profesor p = new Profesor(rs.getInt("id"), rs.getString("nume"), rs.getString("email"), rs.getString("telefon"), new java.util.HashSet<>());
                loadStylesForProfesor(p, con);
                List<main.model.Review> revs = reviewRepo.findByProfesor(p);
                for (main.model.Review r : revs) p.adaugaReview(r);

                // incarca clasele profesorului si le seteaza
                ClasaDansRepository clasaRepo = new ClasaDansRepository();
                List<main.model.ClasaDans> claseProfesor = clasaRepo.findByProfesorId(p.getId());
                for (main.model.ClasaDans c : claseProfesor) {
                    c.setProfesor(p);
                }
                p.setClase(claseProfesor);
                p.calculeazaSuma();

                profesori.add(p);
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return profesori;
    }

    public Profesor findById(int id) {
        String sql = "SELECT * FROM profesori WHERE id = ?";
        ReviewRepository reviewRepo = new ReviewRepository();
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Profesor p = new Profesor(rs.getInt("id"), rs.getString("nume"), rs.getString("email"), rs.getString("telefon"), new java.util.HashSet<>());
                    loadStylesForProfesor(p, con);
                    List<main.model.Review> revs = reviewRepo.findByProfesor(p);
                    for (main.model.Review r : revs) p.adaugaReview(r);

                    // incarca clasele profesorului si le seteaza
                    ClasaDansRepository clasaRepo = new ClasaDansRepository();
                    List<main.model.ClasaDans> claseProfesor = clasaRepo.findByProfesorId(p.getId());
                    for (main.model.ClasaDans c : claseProfesor) {
                        c.setProfesor(p);
                    }
                    p.setClase(claseProfesor);
                    p.calculeazaSuma();

                    return p;
                }
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return null;
    }

    public void delete(int id) {
        String sql = "DELETE FROM profesori WHERE id=?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    public void addStyle(int profesorId, StilDans stil) {
        String sql = "INSERT IGNORE INTO profesor_stil(profesor_id, stil) VALUES (?, ?)";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, profesorId);
            stmt.setString(2, stil.name());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadStylesForProfesor(Profesor p, Connection con) throws SQLException {
        String sqlStyles = "SELECT stil FROM profesor_stil WHERE profesor_id = ?";
        try (PreparedStatement ps = con.prepareStatement(sqlStyles)) {
            ps.setInt(1, p.getId());
            try (ResultSet rs2 = ps.executeQuery()) {
                while (rs2.next()) {
                    String stilStr = rs2.getString("stil");
                    try {
                        StilDans stil = StilDans.valueOf(stilStr);
                        p.setSpecializare(stil);
                    } catch (IllegalArgumentException ex) {
                        System.err.println("Stil necunoscut in DB pentru profesor id=" + p.getId() + ": " + stilStr);
                    }
                }
            }
        }
    }
}