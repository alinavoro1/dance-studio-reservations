package main.repository;

import main.config.DatabaseConnection;
import main.model.Abonament;
import main.model.AbonamentGold;
import main.model.AbonamentSilver;
import main.model.AbonamentSingle;

import java.sql.*;
import java.time.LocalDate;

public class AbonamentRepository {

    public int create(int clientId, String tip, int sedinte, String dataExp) {
        String sql = "INSERT INTO abonamente(client_id, tip_abonament, sedinte_ramase, data_expirare) VALUES (?, ?, ?, ?)";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, clientId);
            stmt.setString(2, tip);
            stmt.setInt(3, sedinte);
            if (dataExp != null && !dataExp.isEmpty()) {
                stmt.setDate(4, Date.valueOf(dataExp));
            } else {
                stmt.setNull(4, Types.DATE);
            }
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return -1;
    }

    public void updateSedinte(int clientId, int sedinteNoi) {
        String sql = "UPDATE abonamente SET sedinte_ramase=? WHERE client_id=?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, sedinteNoi);
            stmt.setInt(2, clientId);
            stmt.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    public void updateDupaRezervare(int clientId, int sedinteNoi, LocalDate dataExp) {
        String sql = "UPDATE abonamente SET sedinte_ramase=?, data_expirare=? WHERE client_id=?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, sedinteNoi);
            if (dataExp != null) {
                stmt.setDate(2, Date.valueOf(dataExp));
            } else {
                stmt.setNull(2, Types.DATE);
            }
            stmt.setInt(3, clientId);
            stmt.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    public Abonament findByClientId(int clientId) {
        String sql = "SELECT id, tip_abonament, sedinte_ramase, data_expirare FROM abonamente WHERE client_id = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, clientId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("id");
                    String tip = rs.getString("tip_abonament");
                    int sedinte = rs.getInt("sedinte_ramase");
                    Date dataExp = rs.getDate("data_expirare");

                    Abonament a;
                    if ("AbonamentSingle".equalsIgnoreCase(tip)) {
                        a = new AbonamentSingle();
                    } else if ("AbonamentSilver".equalsIgnoreCase(tip)) {
                        a = new AbonamentSilver(1);
                    } else if ("AbonamentGold".equalsIgnoreCase(tip)) {
                        a = new AbonamentGold(1);
                    } else {
                        a = new AbonamentSingle();
                    }
                    a.setId(id);
                    a.setSedinteRamase(sedinte);
                    if (dataExp != null) a.setDataExpirare(dataExp.toLocalDate());
                    return a;
                }
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return null;
    }
}