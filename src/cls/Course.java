/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cls;

import Forms.Home;
import java.sql.*;
import db.DBConnection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class Course {
    Connection con = DBConnection.getConnection();
    PreparedStatement ps;
    
    private static final java.util.logging.Logger LOGGER = java.util.logging.Logger.getLogger(Course.class.getName());

    public int getMax() {
        int id = 0;
        Statement st;
        try {
            st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT max(id) FROM course");
            while (rs.next()) {
                id = rs.getInt(1);
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
        return id + 1;
    }
    public boolean getId(int id){
        try {
            ps = con.prepareStatement("SELECT * FROM student WHERE id = ? ");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                Home.txtCstudentID.setText(String.valueOf(rs.getInt(1)));
                return true;
            }else{
                JOptionPane.showMessageDialog(null, "Student id doesn't exist");
            }
        } catch (SQLException ex) {
            Logger.getLogger(Course.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    public int countSemester(int id){
        int total = 0;
        try {
            ps = con.prepareStatement("SELECT count(*) as 'total' FROM course WHERE student_id = ?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                total = rs.getInt(1);
            }
            if(total == 8){
                JOptionPane.showMessageDialog(null, "This student has competed all the course");
                return -1;
            }
        } catch (SQLException ex) {
            Logger.getLogger(Course.class.getName()).log(Level.SEVERE, null, ex);
        }
        return total; 
    }
    public boolean isSemesterExist(int sid ,int semesterNo){
        try {
            ps = con.prepareStatement("SELECT * FROM course WHERE student_id = ? AND semester = ? ");
            ps.setInt(1, sid);
            ps.setInt(2, semesterNo);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(Course.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    //Check student has already takes this semester or not
    public boolean isCourseExist(int sid ,String courseNo, String course){
        String sql = "SELECT * FROM course WHERE student_id = ? AND "+courseNo + " = ? ";
        try {
            ps = con.prepareStatement(sql);
            ps.setInt(1, sid);
            ps.setString(2, course);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(Course.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
        //insert date into course table
    public void insert(int id, int sid, int semester, String course1, String course2, String course3) {
        String sql = "INSERT INTO course (id, student_id, semester, course1, course2, course3) VALUES (?,?,?,?,?,?)";
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ps.setInt(2, sid);
            ps.setInt(3, semester);  // Ensure this is in the correct format
            ps.setString(4, course1);
            ps.setString(5, course2);
            ps.setString(6, course3);

            int rowsInserted = ps.executeUpdate();
            if (rowsInserted > 0) {
                JOptionPane.showMessageDialog(null, "course added successfully.");
            }
        } catch (SQLException ex) {
            Logger.getLogger(Course.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "Error inserting student data: " + ex.getMessage());
        } finally {
            // Close the PreparedStatement and Connection
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Course.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    
    //get all the course values from  database student table
    public void getCourseValue(JTable table, String searchValue) {
        String sql = "SELECT * FROM course where CONCAT(id)like ? order by id desc";
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, "%" + searchValue + "%");
            ResultSet rs = ps.executeQuery();
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            Object[] row;
            while (rs.next()) {
                row = new Object[6];
                row[0] = rs.getInt(1);
                row[1] = rs.getString(2);
                row[2] = rs.getString(3);
                row[3] = rs.getString(4);
                row[4] = rs.getString(5);
                row[5] = rs.getString(6);
                model.addRow(row);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Course.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
