package com.consiti.client;


import com.consiti.utils.Conexion;
import com.consiti.utils.ReadProperties;

import java.sql.CallableStatement;
import java.sql.Connection;

public class SendEmailDetalleLote {
    public static void main(String[] args) {
        System.out.println("enviando mensaje...");
       try{
           //establecer conexión BDD
           Connection conn = Conexion.connectToDB(args[0]);

           String query = "{call SIMAC.SI_H2H_FICOHSA_RESPONSE(?) }";
           CallableStatement statement = conn.prepareCall(query);
           statement.setString(1, "'"+ReadProperties.getProperty("mail.addresss")+"'");
           statement.execute();

           System.out.println("Mensaje enviado!!!");

       }catch (Exception e){
           System.out.println("Exception: "+e.getMessage());
           e.printStackTrace();
       }

    }
}
