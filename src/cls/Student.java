package cls;

import java.sql.*;
import db.DBConnection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

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
}
