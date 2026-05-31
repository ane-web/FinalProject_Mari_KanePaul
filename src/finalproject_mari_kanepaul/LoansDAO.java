/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package finalproject_mari_kanepaul;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
/**
 *
 * @author Kane
 */
public class LoansDAO {

    public boolean checkOutBook(int copyId, int userId) {
        String updateCopySql = """
            UPDATE book_copies
            SET status = 'Checked Out'
            WHERE copy_id = ? AND status = 'Available'
        """;

        String loanSql = """
            INSERT INTO loans (copy_id, user_id, issue_date, due_date)
            VALUES (?, ?, CURDATE(), DATE_ADD(CURDATE(), INTERVAL 7 DAY))
        """;

        try (Connection conn = DBConnection.getConnection()) {
            if (conn == null) {
                return false;
            }

            conn.setAutoCommit(false);

            try (PreparedStatement updateCopyStmt = conn.prepareStatement(updateCopySql);
                 PreparedStatement loanStmt = conn.prepareStatement(loanSql)) {

                updateCopyStmt.setInt(1, copyId);
                if (updateCopyStmt.executeUpdate() == 0) {
                    conn.rollback();
                    return false;
                }

                loanStmt.setInt(1, copyId);
                loanStmt.setInt(2, userId);
                loanStmt.executeUpdate();

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

    public boolean checkInBook(int copyId) {
        String loanSql = """
            UPDATE loans
            SET return_date = CURDATE()
            WHERE copy_id = ? AND return_date IS NULL
        """;

        String copySql = """
            UPDATE book_copies
            SET status = 'Available'
            WHERE copy_id = ?
        """;

        try (Connection conn = DBConnection.getConnection()) {
            if (conn == null) {
                return false;
            }

            conn.setAutoCommit(false);

            try (PreparedStatement loanStmt = conn.prepareStatement(loanSql);
                 PreparedStatement copyStmt = conn.prepareStatement(copySql)) {

                loanStmt.setInt(1, copyId);
                loanStmt.executeUpdate();

                copyStmt.setInt(1, copyId);
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
}
