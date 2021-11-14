package com.consiti.client;

import com.consiti.utils.Conexion;
import com.consiti.utils.Email;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Locale;

public class SendEmail {
    public static void main(String[] args) {
        String text ="";
        try {
            //establecer conexión BDD
            Connection conn = Conexion.connectToDB(args[0]);
            //Paso 1: crear consulta sql
            String consulta = "select NVL(BANK_BATCH_ID,NULL) BANK_BATCH_ID,NVL(CUSTOMER_BATCH_ID,'-') CUSTOMER_BATCH_ID, NVL(MESSAGE,'-') MESSAGE,NVL(MESSAGE_HEADER,'-') MESSAGE_HEADER, NVL(ERROR_MESSAGE,'-') ERROR_MESSAGE , STATUS, FILE_NAME, NVL(CUSTOMER_ID,'-')CUSTOMER_ID , CREATE_ON from SIMAC.SI_H2H_FICOHSA_BACTH_RESPONSE WHERE STATUS =  ?";
            //Paso 2: prepare statement
            PreparedStatement sentencia= conn.prepareStatement(consulta);
            //Paso 3: set parameter
            sentencia.setString(1, "P");
            //Paso 4: execute
            ResultSet rs = sentencia.executeQuery();
            //Paso 5: foreach
            int count=0;
            while(rs.next()) {
                count++;
                System.out.println(rs.getString("MESSAGE_HEADER"));
                if (rs.getString("MESSAGE_HEADER").equals("-")){
                    if(rs.getString("MESSAGE").trim().equals("ERROR")){
                        text +="<tr style=\"text-align: center;\">" +
                                "<td style=\"padding: 15px;background-color: #FFFFFF;color: red;border: 0.5px solid red;\">"+rs.getString("FILE_NAME")+"</td>" +
                                "<td style=\"padding: 15px;background-color: #FFFFFF;color: #191919;border: 0.5px solid #191919;\">"+rs.getString("CUSTOMER_ID")+"</td>" +
                                "<td style=\"padding: 15px;background-color: #FFFFFF;color: #191919;border: 0.5px solid #191919;\">"+rs.getInt("BANK_BATCH_ID")+"</td>" +
                                "<td style=\"padding: 15px;background-color: #FFFFFF;color: #191919;border: 0.5px solid #191919;\">"+rs.getString("CUSTOMER_BATCH_ID")+"</td>" +
                                "<td style=\"padding: 15px;background-color: #FFFFFF;color: red;border: 0.5px solid red;\">"+rs.getString("MESSAGE")+"</td>" +
                                "<td style=\"padding: 15px;background-color: #FFFFFF;color: #191919;border: 0.5px solid red;\">"+rs.getString("ERROR_MESSAGE")+"</td>" +
                                "<tr>";
                    }else{
                        text +="<tr style=\"text-align: center;\">" +
                                "<td style=\"padding: 15px;background-color: #FFFFFF;color: #191919;border: 0.5px solid #191919;\">"+rs.getString("FILE_NAME")+"</td>" +
                                "<td style=\"padding: 15px;background-color: #FFFFFF;color: #191919;border: 0.5px solid #191919;\">"+rs.getString("CUSTOMER_ID")+"</td>" +
                                "<td style=\"padding: 15px;background-color: #FFFFFF;color: #191919;border: 0.5px solid #191919;\">"+rs.getInt("BANK_BATCH_ID")+"</td>" +
                                "<td style=\"padding: 15px;background-color: #FFFFFF;color: #191919;border: 0.5px solid #191919;\">"+rs.getString("CUSTOMER_BATCH_ID")+"</td>" +
                                "<td style=\"padding: 15px;background-color: #FFFFFF;color: green;border: 0.5px solid #191919;\">"+rs.getString("MESSAGE")+"</td>" +
                                "<td style=\"padding: 15px;background-color: #FFFFFF;color: #191919;border: 0.5px solid #191919;\">"+rs.getString("ERROR_MESSAGE")+"</td>" +
                                "<tr>";
                    }
                }
                else{
                    text +="<tr style=\"text-align: center;\">" +
                            "<td style=\"padding: 15px;background-color: #FFFFFF;color: red;border: 0.5px solid red;\">"+rs.getString("FILE_NAME")+"</td>" +
                            "<td style=\"padding: 15px;background-color: #FFFFFF;color: #191919;border: 0.5px solid #191919;\">"+rs.getString("CUSTOMER_ID")+"</td>" +
                            "<td style=\"padding: 15px;background-color: #FFFFFF;color: #191919;border: 0.5px solid #191919;\">"+rs.getInt("BANK_BATCH_ID")+"</td>" +
                            "<td style=\"padding: 15px;background-color: #FFFFFF;color: #191919;border: 0.5px solid #191919;\">"+rs.getString("CUSTOMER_BATCH_ID")+"</td>" +
                            "<td style=\"padding: 15px;background-color: #FFFFFF;color: red;border: 0.5px solid red;\">"+rs.getString("MESSAGE")+"</td>" +
                            "<td style=\"padding: 15px;background-color: #FFFFFF;color: #191919;border: 0.5px solid red;\">"+rs.getString("MESSAGE_HEADER")+"</td>" +
                            "<tr>";
                }
            }
            //Paso 6: contruir archivo
            String body="<!DOCTYPE html>" +
                    "<html>" +
                    "<head>" +
                    "<meta charset=\"utf-8\">" +
                    "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">" +
                    "<title>Pago a Proovedores</title>" +
                    "<link href=\"https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css\" rel=\"stylesheet\" integrity=\"sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC\" crossorigin=\"anonymous\">" +
                    "</head>" +
                    "<body>" +
                    "<div class=\"container w-100 mx-auto\">" +
                    "<h2  style=\"color:#124f91;\" class=\"text-center\">Pago a Proovedores</h2>" +
                    "<table style=\"width:100%;border-collapse: collapse;overflow: hidden;box-shadow: 0 0 20px rgba(0,0,0,0.1);\" class=\"table w-50 mx-auto m-3 table-striped table-hover table-bordered border-primary\">" +
                    "  <thead class=\"table-dark\">" +
                        "<th style=\"padding: 15px;background-color: #191919;color: #fff;border: 0.5px solid #fff;\">File Name</th>  " +
                        "<th style=\"padding: 15px;background-color: #191919;color: #fff;border: 0.5px solid #fff;\">Customer Id</th>  " +
                        "<th style=\"padding: 15px;background-color: #191919;color: #fff;border: 0.5px solid #fff;\">Bank Batch Id</th>" +
                        "<th style=\"padding: 15px;background-color: #191919;color: #fff;border: 0.5px solid #fff;\">Customer Batch Id</th>" +
                        "<th style=\"padding: 15px;background-color: #191919;color: #fff;border: 0.5px solid #fff;\">Status</th>" +
                        "<th style=\"padding: 15px;background-color: #191919;color: #fff;border: 0.5px solid #fff;\">Error Message</th>" +
                    "  </thead>" +
                    "  <tbody>" +
                    "    "+text+" "+
                    "  </tbody>" +
                    "</table>" +
                    "</div>" +
                    "</body>" +
                    "</html> ";
            //enviar mensaje
            if (count > 0){
                Email.send(body);
            }else{
                System.out.println("No hay pagos que mostrar...");
            }


            //update table

            //Paso 1: prepare statement
            String consulta2 = "UPDATE SIMAC.SI_H2H_FICOHSA_BACTH_RESPONSE SET STATUS= DECODE(BANK_BATCH_ID, NULL,'E','C') WHERE STATUS = 'P' ";
            PreparedStatement ps2 = conn.prepareStatement(consulta2);
            //execute
            ps2.executeUpdate();
            System.out.println("Updating table...");
            ps2.close();
        } catch (Exception e) {
            System.out.println("Error: "+e.getMessage());
            e.printStackTrace();
        }
    }
}

