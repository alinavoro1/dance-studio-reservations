package main.repository;

import main.config.DatabaseConnection;
import main.model.Review;
import main.model.ClasaDans;
import main.model.Profesor;
import main.model.Client;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReviewRepository {

    public void createForClass(int clientId, int clasaId, int nota, String comentariu) {
        String sql = "INSERT INTO reviewuri(client_id, clasa_id, nota, comentariu) VALUES (?, ?, ?, ?)";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, clientId);
            stmt.setInt(2, clasaId);
            stmt.setInt(3, nota);
            stmt.setString(4, comentariu);
            stmt.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    public void createForProfesor(int clientId, int profesorId, int nota, String comentariu) {
        String sql = "INSERT INTO reviewuri(client_id, profesor_id, nota, comentariu) VALUES (?, ?, ?, ?)";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, clientId);
            stmt.setInt(2, profesorId);
            stmt.setInt(3, nota);
            stmt.setString(4, comentariu);
            stmt.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    public List<Review> findByClasa(ClasaDans clasa) {
        String sql = "SELECT id, client_id, nota, comentariu FROM reviewuri WHERE clasa_id = ?";
        List<Review> rez = new ArrayList<>();
        ClientRepository clientRepo = new ClientRepository();
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, clasa.getId());
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int clientId = rs.getInt("client_id");
                    Client client = clientRepo.findById(clientId);
                    Review r = new Review(rs.getInt("id"), client, clasa, rs.getInt("nota"), rs.getString("comentariu"));
                    rez.add(r);
                }
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return rez;
    }

    public List<Review> findByProfesor(Profesor prof) {
        String sql = "SELECT id, client_id, nota, comentariu FROM reviewuri WHERE profesor_id = ?";
        List<Review> rez = new ArrayList<>();
        ClientRepository clientRepo = new ClientRepository();
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, prof.getId());
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int clientId = rs.getInt("client_id");
                    Client client = clientRepo.findById(clientId);
                    Review r = new Review(rs.getInt("id"), client, prof, rs.getInt("nota"), rs.getString("comentariu"));
                    rez.add(r);
                }
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return rez;
    }
}