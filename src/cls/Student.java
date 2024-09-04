package cls;

import java.sql.*;
import db.DBConnection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class Student {  // Use PascalCase for class names

    Connection con = DBConnection.getConnection();
    PreparedStatement ps;

    private static final Logger LOGGER = Logger.getLogger(Student.class.getName());

    public int getMax() {
        int id = 0;
        Statement st;
        try {
            st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT max(id) FROM student");
            while (rs.next()) {
                id = rs.getInt(1);
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
        return id + 1;
    }

    //insert date into student table
    public void insert(int id, String name, String date, String gender, String parent, String phone, String address) {
        String sql = "INSERT INTO student (id, name, date_of_birth, gender, parent_name, phone, address) VALUES (?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ps.setString(2, name);
            ps.setString(3, date);  // Ensure this is in the correct format
            ps.setString(4, gender);
            ps.setString(5, parent);
            ps.setString(6, phone);
            ps.setString(7, address);

            int rowsInserted = ps.executeUpdate(); 
            if (rowsInserted > 0) {
                JOptionPane.showMessageDialog(null, "New student added successfully.");
            }
        } catch (SQLException ex) {
            Logger.getLogger(Student.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "Error inserting student data: " + ex.getMessage());
        } finally {
            // Close the PreparedStatement and Connection
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Student.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    //get all the student values from  database student table
    public void getStudentValue(JTable table, String searchValue) {
        String sql = "SELECT * FROM student where CONCAT(id)like ? order by id desc";
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, "%" + searchValue + "%");
            ResultSet rs = ps.executeQuery();
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            Object[] row;
            while (rs.next()) {
                row = new Object[7];
                row[0] = rs.getInt(1);
                row[1] = rs.getString(2);
                row[2] = rs.getString(3);
                row[3] = rs.getString(4);
                row[4] = rs.getString(5);
                row[5] = rs.getString(6);
                row[6] = rs.getString(7);
                model.addRow(row);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Student.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //Update student value
    public void update(int id, String name, String date, String gender, String parent, String phone, String address) {
        String sql = "Update student set name=?,date_of_birth=?,gender=?,parent_name=?,phone=?,address=? WHERE id=?";
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, name);
            ps.setString(2, date);
            ps.setString(3, gender);
            ps.setString(4, parent);
            ps.setString(5, phone);
            ps.setString(6, address);
            ps.setInt(7, id);
            if (ps.executeUpdate() > 0) {
                JOptionPane.showMessageDialog(null, "Student data Updated successfully");
            }
        } catch (SQLException ex) {
            Logger.getLogger(Student.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public boolean isIDExist(int id) {
        try {
            ps = con.prepareStatement("SELECT * FROM student WHERE id=?");
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

    //student data delete
    public void delete(int id) {
        int YesNo = JOptionPane.showConfirmDialog(null, "if you delete date this it will delete all date about this student ", "Student delete", JOptionPane.OK_CANCEL_OPTION, 0);
        if (YesNo == JOptionPane.OK_OPTION) {
            try {
                ps = con.prepareStatement("DELETE FROM student WHERE id=?");
                ps.setInt(1, id);
                if (ps.executeUpdate() > 0) {
                    JOptionPane.showMessageDialog(null, "Student deleted successfully");
                }
            } catch (SQLException ex) {
                Logger.getLogger(Student.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }
}
