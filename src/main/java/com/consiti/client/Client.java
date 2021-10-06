/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.consiti.client;

import com.consiti.utils.ReadProperties;
import java.io.*;
import java.net.*;

/**
 *
 * @author carlo
 */
public class Client {

    public static void main(String[] args) throws Exception {
        String archivo = "consulta.xml";
        String respuesta = "response.xml";
        sendSoapRequest(args[0], args[1]);
    }

    public static void sendSoapRequest(String xmlFile2Send, String responseFileName) throws Exception {

        //File route properties: routeIn, routeOut and SOAPUrl
        String routeIn = ReadProperties.getProperty("dir.request");
        String routeOut = ReadProperties.getProperty("dir.response");
        String routeInComplete = routeIn + xmlFile2Send;
        String routeOutComplete = routeOut + responseFileName;

        String SOAPUrl = ReadProperties.getProperty("ws.url");
        //String xmlFile2Send = "F:/proyecto/FICOHSA/consulta.xml";
        //String responseFileName = "F:/proyecto/FICOHSA/response.xml";
        String SOAPAction = "";

        // Create the connection where we're going to send the file.
        URL url = new URL(SOAPUrl);
        URLConnection connection = url.openConnection();
        HttpURLConnection httpConn = (HttpURLConnection) connection;

        FileInputStream fin = new FileInputStream(routeInComplete);
        ByteArrayOutputStream bout = new ByteArrayOutputStream();

        // Copy SOAP file to the open connection.
        copy(fin, bout);
        fin.close();

        byte[] b = bout.toByteArray();
        StringBuilder buf = new StringBuilder();
        String s = new String(b);
        //System.out.println(s); //print all xml
        b = s.getBytes();
        // Set the appropriate HTTP parameters.
        httpConn.setRequestProperty("Content-Length", String.valueOf(b.length));
        httpConn.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
        httpConn.setRequestProperty("SOAPAction", SOAPAction);
        httpConn.setRequestMethod("POST");
        httpConn.setDoOutput(true);
        //httpConn.setDoInput(true);

        // send the XML that was read in to b.
        OutputStream out = httpConn.getOutputStream();
        out.write(b);
        out.close();

        // Read the response.
        httpConn.connect();
        System.out.println("http connection status :" + httpConn.getResponseMessage());
        InputStreamReader isr = new InputStreamReader(httpConn.getInputStream());
        BufferedReader in = new BufferedReader(isr);

        /*
        while ((inputLine = in.readLine()) != null) {
            System.out.println(inputLine);
        }*/
        FileOutputStream fos = new FileOutputStream(routeOutComplete);
        copy(httpConn.getInputStream(), fos);
        in.close();
    }

    public static void copy(InputStream in, OutputStream out)
            throws IOException {

        synchronized (in) {
            synchronized (out) {
                byte[] buffer = new byte[256];
                while (true) {
                    int bytesRead = in.read(buffer);
                    if (bytesRead == -1) {
                        break;
                    }
                    out.write(buffer, 0, bytesRead);
                }
            }
        }
    }
}
