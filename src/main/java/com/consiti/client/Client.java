/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.consiti.client;

import com.consiti.utils.ReadProperties;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.*;
import java.net.*;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author carlos Peñate, info: carlossalazar2228@gmail.com
 */
public class Client {

    public static void main(String[] args) throws Exception {
        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[] {
                new X509TrustManager() {
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[0];
                    }
                    public void checkClientTrusted(
                            java.security.cert.X509Certificate[] certs, String authType) {
                    }
                    public void checkServerTrusted(
                            java.security.cert.X509Certificate[] certs, String authType) {
                    }
                }
        };

        // Install the all-trusting trust manager
        try {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (GeneralSecurityException e) {
            System.out.println("General Security Exception: "+e.getMessage());
        }


        //Put everything after here in your function.
        KeyStore trustStore  = KeyStore.getInstance(KeyStore.getDefaultType());
        trustStore.load(null);//Make an empty store
        InputStream fis = new FileInputStream("/home/integra/h2h/certificado/soabus.cer");
        BufferedInputStream bis = new BufferedInputStream(fis);

        CertificateFactory cf = CertificateFactory.getInstance("X.509");

        while (bis.available() > 0) {
            Certificate cert = cf.generateCertificate(bis);
            trustStore.setCertificateEntry("fiddler"+bis.available(), cert);
        }

        sendSoapRequest(args[0], args[1]);
    }

    public static void sendSoapRequest(String xmlFile2Send, String responseFileName) {


        //get date
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();


        System.out.println("starting process...");
        System.out.println("reading ws: "+ReadProperties.getProperty("ws.url")+"\n");
        //File route properties: routeIn, routeOut and SOAPUrl
        String routeIn = ReadProperties.getProperty("dir.request");
        String routeOut = ReadProperties.getProperty("dir.response");
        String routeInComplete = routeIn + xmlFile2Send;
        String routeOutComplete = routeOut + responseFileName;

        String SOAPUrl = ReadProperties.getProperty("ws.url");
        String SOAPAction = "";

        try{
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
            System.out.println("Execution date: "+dtf.format(now));
            System.out.println("http connection status :" + httpConn.getResponseMessage()+"\n");
            InputStreamReader isr = new InputStreamReader(httpConn.getInputStream());
            BufferedReader in = new BufferedReader(isr);

        /*
        while ((inputLine = in.readLine()) != null) {
            System.out.println(inputLine);
        }*/
            FileOutputStream fos = new FileOutputStream(routeOutComplete);
            copy(httpConn.getInputStream(), fos);
            in.close();

        }catch(Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
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
