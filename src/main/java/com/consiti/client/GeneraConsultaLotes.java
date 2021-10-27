package com.consiti.client;


import com.consiti.utils.ReadProperties;
import org.w3c.dom.*;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author carlos Peñate, info: carlossalazar2228@gmail.com
 */
public class GeneraConsultaLotes {
    public static void main(String[] args) {
        System.out.println("Iniciando conversion...");
        WriteXMLFile(args[0], args[1], args[2],args[3], args[4], args[5], args[6]);
    }

    public static void WriteXMLFile(String XMLUserName, String XMLPassword,String XMLSourceBank,
                                    String XMLDestinationBank, String XMLCustomerId, String XMLBeginDate,
                                    String XMLEndDate) {
        try {
            //date format
            String pattern = "yyyy-MM-dd-HHMMss";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
            String date = simpleDateFormat.format(new Date());

            //structure name
            String routeOut = ReadProperties.getProperty("dir.request");
            String name = "ConsultaLotesCliente["+date+"].xml";
            String request = routeOut+name;

            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            // root elements
            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("soapenv:Envelope");
            doc.appendChild(rootElement);

            // set attribute to staff element
            rootElement.setAttribute("xmlns:soapenv", "http://schemas.xmlsoap.org/soap/envelope/");
            rootElement.setAttribute(ReadProperties.getProperty("aut.type"),ReadProperties.getProperty("aut.service"));
            rootElement.setAttribute(ReadProperties.getProperty("pag.type"),ReadProperties.getProperty("pag.service"));

            // Header elements
            Element header = doc.createElement("soapenv:Header");
            rootElement.appendChild(header);

            //RequestHeader elements
            Element requestHeader = doc.createElement(ReadProperties.getProperty("aut.requestHeader"));
            header.appendChild(requestHeader);

            //Authentication elements
            Element authentication = doc.createElement(ReadProperties.getProperty("pag.authentication"));
            requestHeader.appendChild(authentication);

            // UserName elements
            Element userName = doc.createElement("UserName");
            userName.appendChild(doc.createTextNode(XMLUserName));
            authentication.appendChild(userName);

            // Password elements
            Element password = doc.createElement("Password");
            password.appendChild(doc.createTextNode(XMLPassword));
            authentication.appendChild(password);

            //Region elements
            Element region = doc.createElement("Region");
            requestHeader.appendChild(region);

            // SourceBank elements
            Element sourceBank = doc.createElement("SourceBank");
            sourceBank.appendChild(doc.createTextNode(XMLSourceBank));
            region.appendChild(sourceBank);

            // DestinationBank elements
            Element destinationBank = doc.createElement("DestinationBank");
            destinationBank.appendChild(doc.createTextNode(XMLDestinationBank));
            region.appendChild(destinationBank);

            //Body elements
            Element body = doc.createElement("soapenv:Body");
            rootElement.appendChild(body);

            //consultaLotesCliente elements
            Element consultaLotesCliente = doc.createElement(ReadProperties.getProperty("pag.consultaLoteCliene"));
            body.appendChild(consultaLotesCliente);

            //CUSTOMER_ID elements
            Element customerId = doc.createElement("CUSTOMER_ID");
            customerId.appendChild(doc.createTextNode(XMLCustomerId));
            consultaLotesCliente.appendChild(customerId);

            //BEGIN_DATE elements
            Element beginDate = doc.createElement("BEGIN_DATE");
            beginDate.appendChild(doc.createTextNode(XMLBeginDate));
            consultaLotesCliente.appendChild(beginDate);

            //BEGIN_DATE elements
            Element endDate = doc.createElement("END_DATE");
            endDate.appendChild(doc.createTextNode(XMLEndDate));
            consultaLotesCliente.appendChild(endDate);

            // write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            // pretty print XML
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");

            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(request));

            transformer.transform(source, result);

            System.out.println("File saved!");

        } catch (ParserConfigurationException | TransformerException e) {
            System.out.println("Exception found: "+e.getMessage());
        }
    }
}
