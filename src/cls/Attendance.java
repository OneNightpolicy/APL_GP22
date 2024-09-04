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

public class Attendance {

    Connection con = DBConnection.getConnection();
    PreparedStatement ps;

    public int getMax() {
        int id = 0;
        Statement st;
        try {
            st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT max(id) FROM Attendance");
            while (rs.next()) {
                id = rs.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Attendance.class.getName()).log(Level.SEVERE, null, ex);
        }
        return id + 1;
    }

    public boolean getId(int id) {
        try {
            ps = con.prepareStatement("SELECT * FROM student WHERE id = ? ");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Home.txtAID2.setText(String.valueOf(rs.getInt(1)));
                return true;
            } else {
                JOptionPane.showMessageDialog(null, "Student id doesn't exist");
            }
        } catch (SQLException ex) {
            Logger.getLogger(Attendance.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public boolean isAttendanceExist(int id) {
        try {
            ps = con.prepareStatement("SELECT * FROM Attendance WHERE id=?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(Attendance.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    //insert date into attendance table
    public void insert(int id, int sid, String date, int timeslot, String status) {
    String sql = "INSERT INTO Attendance (id, Student_id, Date, Timeslot, Status) VALUES (?, ?, ?, ?, ?)";

    PreparedStatement ps = null;
    try {
        ps = con.prepareStatement(sql);
        ps.setInt(1, id);
        ps.setInt(2, sid);
        ps.setString(3, date);  // Ensure this is in the correct format
        ps.setInt(4, timeslot);
        ps.setString(5, status);
        int rowsInserted = ps.executeUpdate();
        if (rowsInserted > 0) {
            JOptionPane.showMessageDialog(null, "Attendance recorded successfully.");
        }
    } catch (SQLException ex) {
        Logger.getLogger(Attendance.class.getName()).log(Level.SEVERE, null, ex);
        JOptionPane.showMessageDialog(null, "Error inserting student data: " + ex.getMessage());
    } finally {
        // Close the PreparedStatement and Connection
        if (ps != null) {
            try {
                ps.close();
            } catch (SQLException ex) {
                Logger.getLogger(Attendance.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}


    //get all the attendance values from  database attendance table
    public void getAttendanceValue(JTable table, String searchValue) {
        String sql = "SELECT * FROM Attendance where CONCAT(id)like ? order by id desc";
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, "%" + searchValue + "%");
            ResultSet rs = ps.executeQuery();
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            Object[] row;
            while (rs.next()) {
                row = new Object[5];
                row[0] = rs.getInt(1);
                row[1] = rs.getString(2);
                row[2] = rs.getString(3);
                row[3] = rs.getString(4);
                row[4] = rs.getString(5);

                model.addRow(row);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Attendance.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
