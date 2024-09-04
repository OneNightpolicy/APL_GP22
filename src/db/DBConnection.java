/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
/**
 *
 * @author tlong
 */
public class DBConnection {
    public static Connection getConnection(){
        Connection con = null;
        try{
            String url = "jdbc:mysql://127.0.0.1:3306/Student_infor";
            String user = "root";
            String password = "kim24"; 
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(url,user,password);
        } catch (ClassNotFoundException | SQLException e) {
        }
        return con;
    }
    
}
