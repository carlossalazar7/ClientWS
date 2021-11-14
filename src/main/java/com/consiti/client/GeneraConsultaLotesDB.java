package com.consiti.client;

import com.consiti.utils.Conexion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GeneraConsultaLotesDB {
    public static void main(String[] args) throws SQLException {
        //establecer conexión BDD
        Connection conn = Conexion.connectToDB(args[0]);
        //Paso 1: crear consulta sql
        String consulta = "SELECT * FROM SIMAC.si_h2h_pending_batch_query_vw";
        //Paso 2: prepare statement
        PreparedStatement sentencia= conn.prepareStatement(consulta);
        //Paso 3: set parameter
        ResultSet rs = sentencia.executeQuery();
        //Paso 5: foreach
        int count=0;
        while(rs.next()) {
            count++;
            GeneraConsultaDetalleLote.WriteXML(rs.getString("FICOHSA_USER"),
                    rs.getString("FICOHSA_PASSWORD"), rs.getString("DEST_BANK"),
                    rs.getString("SOURCE_BANK"),rs.getString("CUSTOMER_ID"),
                    "BANK_BATCH_ID", rs.getString("BANK_BATCH_ID"));
        }

        //update table

        //Paso 1: prepare statement
        String consulta2 = "UPDATE SIMAC.SI_H2H_FICOHSA_BACTH_RESPONSE SET STATUS= DECODE(BANK_BATCH_ID, NULL,'E','F') WHERE STATUS = 'C' ";
        PreparedStatement ps2 = conn.prepareStatement(consulta2);
        //execute
        ps2.executeUpdate();
        System.out.println("Updating table...");

    }
}
