package cls;

import Forms.Home;
import db.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class Score {

    Connection con = DBConnection.getConnection();
    PreparedStatement ps;

    public int getMax() {
        int id = 0;
        Statement st;
        try {
            st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT max(id) FROM score");
            while (rs.next()) {
                id = rs.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Score.class.getName()).log(Level.SEVERE, null, ex);
        }
        return id + 1;
    }

    public boolean getDetails(int SGID, int SemNO) {
        try {
            ps = con.prepareStatement("SELECT * FROM course WHERE student_id = ? and semester = ? ");
            ps.setInt(1, SGID);
            ps.setInt(2, SemNO);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Home.txtGstudent.setText(String.valueOf(rs.getInt(2)));
                Home.txtGSemester.setText(String.valueOf(rs.getInt(3)));
                Home.txtGC1.setText(rs.getString(4));
                Home.txtGC2.setText(rs.getString(5));
                Home.txtGC3.setText(rs.getString(6));
                return true;
            } else {
                JOptionPane.showMessageDialog(null, "Student id or semester doesn't exist");
            }
        } catch (SQLException ex) {
            Logger.getLogger(Score.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public boolean isIDExist(int id) {
        try {
            ps = con.prepareStatement("SELECT * FROM score WHERE id=?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(Student.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public boolean isSidSemesterNOExist(int sid, int semesterNO) {
        try {
            ps = con.prepareStatement("SELECT * FROM score WHERE id=? AND semester = ?");
            ps.setInt(1, sid);
            ps.setInt(2, semesterNO);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(Student.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public void insert(int id, int sid, int semester, String course1, String course2, String course3, double score1, double score2, double score3, double average) {
        String sql = "INSERT INTO score VALUES (?, ?, ?, ?, ?, ?, ?,? ,?,?)";

        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ps.setInt(2, sid);
            ps.setInt(3, semester);
            ps.setString(4, course1);
            ps.setDouble(5, score1);
            ps.setString(6, course2);
            ps.setDouble(7, score2);
            ps.setString(8, course3);
            ps.setDouble(9, score3);
            ps.setDouble(10, average);

            int rowsInserted = ps.executeUpdate();
            if (rowsInserted > 0) {
                JOptionPane.showMessageDialog(null, "score added successfully.");
            }
        } catch (SQLException ex) {
            Logger.getLogger(Score.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "Error inserting student data: " + ex.getMessage());
        } finally {
            // Close the PreparedStatement and Connection
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Score.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    
    public void getScoreValue(JTable table, String searchValue) {
        String sql = "SELECT * FROM score where CONCAT(id)like ? order by id desc";
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, "%" + searchValue + "%");
            ResultSet rs = ps.executeQuery();
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            Object[] row;
            while (rs.next()) {
                row = new Object[10];
                row[0] = rs.getInt(1);
                row[1] = rs.getInt(2);
                row[2] = rs.getInt(3);
                row[3] = rs.getString(4);
                row[4] = rs.getDouble(5);
                row[5] = rs.getString(6);
                row[6] = rs.getDouble(7);
                row[7] = rs.getString(8);
                row[8] = rs.getDouble(9);
                row[9] = rs.getDouble(10);
                model.addRow(row);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Score.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
