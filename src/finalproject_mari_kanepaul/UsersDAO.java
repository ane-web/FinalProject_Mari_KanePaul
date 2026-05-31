/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package finalproject_mari_kanepaul;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
/**
 *
 * @author Kane
 */
public class UsersDAO {
    
    public int getOrCreateUser(String fullName) {
        String selectSql = "SELECT user_id FROM users WHERE full_name = ? LIMIT 1";
        String insertSql = "INSERT INTO users (full_name, email, phone) VALUES (?, ?, ?)";

        try (Connection conn = DBConnection.getConnection()) {
            if (conn == null) {
                return -1;
            }

            try (PreparedStatement selectStmt = conn.prepareStatement(selectSql)) {
                selectStmt.setString(1, fullName);
                try (ResultSet rs = selectStmt.executeQuery()) {
                    if (rs.next()) {
                        return rs.getInt("user_id");
                    }
                }
            }

            try (PreparedStatement insertStmt = conn.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS)) {
                insertStmt.setString(1, fullName);
                insertStmt.setString(2, makeEmail(fullName));
                insertStmt.setString(3, "");
                insertStmt.executeUpdate();

                try (ResultSet keys = insertStmt.getGeneratedKeys()) {
                    if (keys.next()) {
                        return keys.getInt(1);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }

    private String makeEmail(String fullName) {
        String cleanName = fullName.toLowerCase().replaceAll("[^a-z0-9]+", ".");
        cleanName = cleanName.replaceAll("^\\.+|\\.+$", "");

        if (cleanName.isBlank()) {
            cleanName = "borrower";
        }

        return cleanName + "." + System.currentTimeMillis() + "@library.local";
    }
    
}
