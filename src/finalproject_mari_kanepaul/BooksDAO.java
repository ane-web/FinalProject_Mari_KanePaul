/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package finalproject_mari_kanepaul;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
/**
 *
 * @author Kane
 */
public class BooksDAO {
    
    public void ensureBookExists(String isbn, String title, String author) {
        String checkSql = "SELECT 1 FROM books WHERE isbn = ?";
        String insertSql = "INSERT INTO books (isbn, title, author, genre) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection()) {
            // 1. Check if the master book information exists
            try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
                checkStmt.setString(1, isbn);
                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (rs.next()) {
                        return; // Book exists, no action required
                    }
                }
            }

            // 2. Insert new master book profile if missing
            try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                insertStmt.setString(1, isbn);
                insertStmt.setString(2, title);
                insertStmt.setString(3, author);
                insertStmt.setString(4, "General"); // Default Genre placeholder
                insertStmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public List<Object[]> getBookCards() {
        List<Object[]> bookCards = new ArrayList<>();

        String sql = """
            SELECT 
                bc.copy_id,
                b.title,
                b.author,
                b.isbn,
                s.shelf_code,
                bc.status,
                u.full_name AS borrower,
                l.due_date
            FROM book_copies bc
            JOIN books b ON bc.isbn = b.isbn
            LEFT JOIN shelves s ON bc.shelf_id = s.shelf_id
            LEFT JOIN loans l ON bc.copy_id = l.copy_id AND l.return_date IS NULL
            LEFT JOIN users u ON l.user_id = u.user_id
            ORDER BY b.title
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Object[] row = {
                    rs.getInt("copy_id"),
                    rs.getString("title"),
                    rs.getString("author"),
                    rs.getString("isbn"),
                    rs.getString("shelf_code"),
                    rs.getString("status"),
                    rs.getString("borrower"),
                    rs.getString("due_date")
                };

                bookCards.add(row);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return bookCards;
    }
    
    public int countAllCopies() {
        return countBySql("SELECT COUNT(*) FROM book_copies");
    }

    public int countAvailableCopies() {
        return countBySql("SELECT COUNT(*) FROM book_copies WHERE status = 'Available'");
    }

    public int countCheckedOutCopies() {
        return countBySql("SELECT COUNT(*) FROM book_copies WHERE status = 'Checked Out'");
    }

    private int countBySql(String sql) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }
    
}
