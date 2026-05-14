package main.repository;

import main.config.DatabaseConnection;
import java.sql.*;

public class RezervareRepository {
    public void create(int clientId, int clasaId) {
        String sql = "INSERT INTO rezervari(client_id, clasa_id) VALUES (?, ?)";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, clientId);
            stmt.setInt(2, clasaId);
            stmt.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    public void delete(int id) {
        String sql = "DELETE FROM rezervari WHERE id=?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }
}