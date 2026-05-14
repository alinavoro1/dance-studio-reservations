package main.repository;

import main.config.DatabaseConnection;
import main.model.Client;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClientRepository {
    public void create(Client c) {
        String sql = "INSERT INTO clienti(nume, email, telefon) VALUES (?, ?, ?)";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, c.getNume());
            stmt.setString(2, c.getEmail());
            stmt.setString(3, c.getTelefon());
            stmt.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    public List<Client> findAll() {
        List<Client> clients = new ArrayList<>();
        String sql = "SELECT * FROM clienti";
        AbonamentRepository abonRepo = new AbonamentRepository();
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Client c = new Client(rs.getInt("id"), rs.getString("nume"), rs.getString("email"), rs.getString("telefon"));
                try {
                    main.model.Abonament a = abonRepo.findByClientId(c.getId());
                    if (a != null) c.setAbonament(a);
                } catch (Exception ex) {
                }
                clients.add(c);
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return clients;
    }

    public Client findById(int id) {
        String sql = "SELECT * FROM clienti WHERE id = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Client c = new Client(rs.getInt("id"), rs.getString("nume"), rs.getString("email"), rs.getString("telefon"));
                    // încarcă abonamentul
                    AbonamentRepository abonRepo = new AbonamentRepository();
                    main.model.Abonament a = abonRepo.findByClientId(c.getId());
                    if (a != null) c.setAbonament(a);
                    return c;
                }
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return null;
    }

    public void update(Client c) {
        String sql = "UPDATE clienti SET nume=?, email=?, telefon=? WHERE id=?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, c.getNume());
            stmt.setString(2, c.getEmail());
            stmt.setString(3, c.getTelefon());
            stmt.setInt(4, c.getId());
            stmt.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    public void delete(int id) {
        String sql = "DELETE FROM clienti WHERE id=?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }
}