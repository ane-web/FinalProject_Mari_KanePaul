/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package finalproject_mari_kanepaul;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
/**
 *
 * @author Kane
 */
public class ShelvesDAO {
    
    public int getOrCreateShelfId(String shelfCode) {
        String selectSql = "SELECT shelf_id FROM shelves WHERE shelf_code = ?";
        String insertSql = "INSERT INTO shelves (floor, section, shelf_code) VALUES (?, ?, ?)";

        try (Connection conn = DBConnection.getConnection()) {
            // 1. Check if shelf already exists
            try (PreparedStatement selectStmt = conn.prepareStatement(selectSql)) {
                selectStmt.setString(1, shelfCode);
                try (ResultSet rs = selectStmt.executeQuery()) {
                    if (rs.next()) {
                        return rs.getInt("shelf_id"); // Found existing ID
                    }
                }
            }

            // 2. If it doesn't exist, create it (defaulting floor to 1 and section to General)
            try (PreparedStatement insertStmt = conn.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS)) {
                insertStmt.setInt(1, 1); // Default Floor
                insertStmt.setString(2, "General"); // Default Section
                insertStmt.setString(3, shelfCode);
                insertStmt.executeUpdate();

                try (ResultSet generatedKeys = insertStmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1); // Return new auto-incremented ID
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Operation failed
    }
    
}
