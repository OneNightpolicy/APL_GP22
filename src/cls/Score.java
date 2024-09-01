
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
    
    public boolean getDetails(int SGID, int SemNO){
        try {
            ps = con.prepareStatement("SELECT * FROM course WHERE student_id = ? and semester = ? ");
            ps.setInt(1, SGID);
            ps.setInt(2, SemNO);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                Home.txtGstudent.setText(String.valueOf(rs.getInt(2)));
                Home.txtGSemester.setText(String.valueOf(rs.getInt(3)));
                Home.txtGC1.setText(rs.getString(4));
                Home.txtGC2.setText(rs.getString(5));
                Home.txtGC3.setText(rs.getString(6));
                return true;
            }else{
                JOptionPane.showMessageDialog(null, "Student id or semester doesn't exist");
            }
        } catch (SQLException ex) {
            Logger.getLogger(Score.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
}
