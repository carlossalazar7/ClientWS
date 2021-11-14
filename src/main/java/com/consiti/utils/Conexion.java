package com.consiti.utils;


import org.pentaho.di.core.encryption.Encr;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {
    private static Connection conn = null;
    public static Connection connectToDB(String ruta){
        Kettler.loadProperties(ruta);

        String URL = "jdbc:oracle:thin:@"+Kettler.getProperty("SIMAC.SERVER.IP")+":"+Kettler.getProperty("SIMAC.SERVER.PORT")+"/"+Kettler.getProperty("SIMAC.SERVER.DATABASENAME");
        String USER = Kettler.getProperty("SIMAC.SERVER.USER");
        String PASSWORD = Encr.decryptPasswordOptionallyEncrypted(Kettler.getProperty("SIMAC.SERVER.PASS"));
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            conn = DriverManager.getConnection(URL,USER,PASSWORD);
            if (conn != null){
                System.out.println("Se establecio la conexion :)");
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            return conn;
        }
    }

    public static void shutdown() throws SQLException {
        if (conn!=null) {
            conn.close();
        }
    }
}
