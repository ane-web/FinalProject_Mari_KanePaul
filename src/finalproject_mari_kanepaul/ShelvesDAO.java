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
    
    public java.util.List<String> getShelfCodes() {
    java.util.List<String> shelfCodes = new java.util.ArrayList<>();
    String sql = "SELECT shelf_code FROM shelves ORDER BY shelf_code";

    try (Connection conn = DBConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql);
         ResultSet rs = stmt.executeQuery()) {

        while (rs.next()) {
            shelfCodes.add(rs.getString("shelf_code"));
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }

    return shelfCodes;
}
    
    public int getOrCreateShelfId(String shelfCode) {
        String selectSql = "SELECT shelf_id FROM shelves WHERE shelf_code = ?";
        String insertSql = "INSERT INTO shelves (floor, section, shelf_code) VALUES (?, ?, ?)";

        try (Connection conn = DBConnection.getConnection()) {
            
            try (PreparedStatement selectStmt = conn.prepareStatement(selectSql)) {
                selectStmt.setString(1, shelfCode);
                try (ResultSet rs = selectStmt.executeQuery()) {
                    if (rs.next()) {
                        return rs.getInt("shelf_id"); 
                    }
                }
            }
            try (PreparedStatement insertStmt = conn.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS)) {
                insertStmt.setInt(1, 1); 
                insertStmt.setString(2, "General"); 
                insertStmt.setString(3, shelfCode);
                insertStmt.executeUpdate();

                try (ResultSet generatedKeys = insertStmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1); 
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; 
    }
    
}
