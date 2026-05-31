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
            try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
                checkStmt.setString(1, isbn);
                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (rs.next()) {
                        return; 
                    }
                }
            }       
            try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                insertStmt.setString(1, isbn);
                insertStmt.setString(2, title);
                insertStmt.setString(3, author);
                insertStmt.setString(4, "General"); 
                insertStmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public boolean addBookWithCopy(String isbn, String title, String author, String genre, int shelfId) {
    String bookSql = """
        INSERT INTO books (isbn, title, author, genre)
        VALUES (?, ?, ?, ?)
        ON DUPLICATE KEY UPDATE title = ?, author = ?, genre = ?
    """;

    String copySql = """
        INSERT INTO book_copies (isbn, shelf_id, status)
        VALUES (?, ?, 'Available')
    """;

    try (Connection conn = DBConnection.getConnection()) {
        if (conn == null) {
            return false;
        }

        conn.setAutoCommit(false);

        try (PreparedStatement bookStmt = conn.prepareStatement(bookSql);
             PreparedStatement copyStmt = conn.prepareStatement(copySql)) {

            bookStmt.setString(1, isbn);
            bookStmt.setString(2, title);
            bookStmt.setString(3, author);
            bookStmt.setString(4, genre);
            bookStmt.setString(5, title);
            bookStmt.setString(6, author);
            bookStmt.setString(7, genre);
            bookStmt.executeUpdate();

            copyStmt.setString(1, isbn);
            if (shelfId > 0) {
                copyStmt.setInt(2, shelfId);
            } else {
                copyStmt.setNull(2, java.sql.Types.INTEGER);
            }
            copyStmt.executeUpdate();

            conn.commit();
            return true;
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);
        }
    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}
    
    public List<Object[]> getBookCards(String searchText, String statusFilter, String shelfFilter) {
    List<Object[]> bookCards = new ArrayList<>();
    List<Object> params = new ArrayList<>();

    StringBuilder sql = new StringBuilder("""
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
        WHERE 1 = 1
    """);

    if (searchText != null && !searchText.isBlank()) {
        sql.append(" AND (LOWER(b.title) LIKE ? OR LOWER(b.author) LIKE ? OR LOWER(b.isbn) LIKE ?) ");
        String search = "%" + searchText.toLowerCase() + "%";
        params.add(search);
        params.add(search);
        params.add(search);
    }

    if (statusFilter != null && !"All Books".equals(statusFilter)) {
        sql.append(" AND bc.status = ? ");
        params.add(statusFilter);
    }

    if (shelfFilter != null && !"All Shelves".equals(shelfFilter)) {
        sql.append(" AND s.shelf_code = ? ");
        params.add(shelfFilter);
    }

    sql.append(" ORDER BY b.title ");

    try (Connection conn = DBConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql.toString())) {

        for (int i = 0; i < params.size(); i++) {
            stmt.setObject(i + 1, params.get(i));
        }

        try (ResultSet rs = stmt.executeQuery()) {
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
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }

    return bookCards;
}
    
    public List<Object[]> getBookCards() {
    return getBookCards("", "All Books", "All Shelves");
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
    public boolean updateBookCopy(int copyId, String title, String author, String genre, int shelfId) {
    String sql = """
        UPDATE books b
        JOIN book_copies bc ON b.isbn = bc.isbn
        SET b.title = ?, b.author = ?, b.genre = ?, bc.shelf_id = ?
        WHERE bc.copy_id = ?
    """;

    try (Connection conn = DBConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setString(1, title);
        stmt.setString(2, author);
        stmt.setString(3, genre);
        stmt.setInt(4, shelfId);
        stmt.setInt(5, copyId);

        return stmt.executeUpdate() > 0;
    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}

public boolean deleteBookCopy(int copyId) {
    String deleteLoansSql = "DELETE FROM loans WHERE copy_id = ?";
    String deleteCopySql = "DELETE FROM book_copies WHERE copy_id = ?";

    try (Connection conn = DBConnection.getConnection()) {
        conn.setAutoCommit(false);

        try (PreparedStatement loanStmt = conn.prepareStatement(deleteLoansSql);
             PreparedStatement copyStmt = conn.prepareStatement(deleteCopySql)) {

            loanStmt.setInt(1, copyId);
            loanStmt.executeUpdate();

            copyStmt.setInt(1, copyId);
            int deleted = copyStmt.executeUpdate();

            conn.commit();
            return deleted > 0;
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        }
    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}

}
