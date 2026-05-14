package main.repository;

import main.config.DatabaseConnection;
import main.model.Sala;
import main.model.Studio;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudioRepository {
    public void create(Studio s) {
        String sql = "INSERT INTO studiouri(nume, adresa) VALUES (?, ?)";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, s.getNume());
            stmt.setString(2, s.getLocatie());
            stmt.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    public List<Studio> findAll() {
        List<Studio> studiouri = new ArrayList<>();
        String sqlStudiouri = "SELECT * FROM studiouri";
        // Query pentru a extrage salile aferente unui anumit studio
        String sqlSali = "SELECT * FROM sali WHERE studio_id = ?";

        try (Connection con = main.config.DatabaseConnection.getConnection();
             PreparedStatement stmtStudiouri = con.prepareStatement(sqlStudiouri);
             ResultSet rsStudiouri = stmtStudiouri.executeQuery()) {

            while (rsStudiouri.next()) {
                Studio studio = new Studio(
                        rsStudiouri.getInt("id"),
                        rsStudiouri.getString("nume"),
                        rsStudiouri.getString("adresa")
                );
                try (PreparedStatement stmtSali = con.prepareStatement(sqlSali)) {
                    stmtSali.setInt(1, studio.getId());
                    try (ResultSet rsSali = stmtSali.executeQuery()) {
                        while (rsSali.next()) {
                            Sala sala = new Sala(
                                    rsSali.getString("nume"),
                                    rsSali.getInt("capacitate"),
                                    studio
                            );
                            sala.setId(rsSali.getInt("id"));
                            studio.getSali().add(sala);
                        }
                    }
                }
                studiouri.add(studio);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return studiouri;
    }
}