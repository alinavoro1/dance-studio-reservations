package main.repository;

import main.config.DatabaseConnection;
import main.model.Sala;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SalaRepository {
    public void create(Sala s, int studioId) {
        String sql = "INSERT INTO sali(nume, capacitate, studio_id) VALUES (?, ?, ?)";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, s.getNume());
            stmt.setInt(2, s.getCapacitate());
            stmt.setInt(3, studioId);
            stmt.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    public void delete(int id) {
        String sql = "DELETE FROM sali WHERE id=?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }
}