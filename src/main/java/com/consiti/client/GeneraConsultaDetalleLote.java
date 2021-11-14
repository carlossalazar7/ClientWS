package com.consiti.client;

import com.consiti.utils.ReadProperties;
import org.w3c.dom.*;
import org.xml.sax.SAXException;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;

public class GeneraConsultaDetalleLote {

    public static void main(String[] args) {
        System.out.println("construyendo archivos");
        CreateXMLFile(args[0],args[1],args[2],args[3],args[4]);
    }

    public static void CreateXMLFile(String userName, String password, String sourceBank, String destinationBank,
                                     String XMLFileName){
        try {
            System.out.println("Reading file: "+XMLFileName);
            File file = new File(ReadProperties.getProperty("dir.response")+XMLFileName);


            String[] newCustomerId = XMLFileName.split("-");


            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document document = db.parse(file);
            document.getDocumentElement().normalize();
            NodeList nList = document.getElementsByTagName("BATCH");
            System.out.println("----------------------------");
            System.out.println("array: "+newCustomerId.toString());
                for (int temp = 0; temp < nList.getLength(); temp++) {
                    Node nNode = nList.item(temp);
                    if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element e = (Element) nNode;
                        WriteXML(userName,password,sourceBank, destinationBank, newCustomerId[1],
                                e.getElementsByTagName("BANK_BATCH_ID").item(0).getNodeName(),
                                e.getElementsByTagName("BANK_BATCH_ID").item(0).getTextContent());
                                //e.getElementsByTagName("0").item(0).getTextContent());
                    }
                }
        }
        catch(IOException | ParserConfigurationException | SAXException e) {
            System.out.println("General Exception: "+e);
        }
    }

    public static void WriteXML(String XMLUserName, String XMLPassword, String XMLSourceBank,
                                String XMLDestinationBank, String customerId, String queryType,String queryValue){
        try{
            System.out.println("Creating...");

            System.out.println("customerID: "+customerId);

            //structure name
            String routeOut = ReadProperties.getProperty("dir.request");
            String name = "ConsultaDetalleLote["+queryValue+"].xml";

            DocumentBuilderFactory Factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = Factory.newDocumentBuilder();

            // root elements
            Document document = docBuilder.newDocument();
            Element rootElement = document.createElement("soapenv:Envelope");
            document.appendChild(rootElement);

            // set attribute to staff element
            rootElement.setAttribute("xmlns:soapenv", "http://schemas.xmlsoap.org/soap/envelope/");
            rootElement.setAttribute(ReadProperties.getProperty("aut.type"),ReadProperties.getProperty("aut.service"));
            rootElement.setAttribute(ReadProperties.getProperty("pag.type"),ReadProperties.getProperty("pag.service"));

            // Header elements
            Element header = document.createElement("soapenv:Header");
            rootElement.appendChild(header);

            //RequestHeader elements
            Element requestHeader = document.createElement("aut:RequestHeader");
            header.appendChild(requestHeader);

            //Authentication elements
            Element authentication = document.createElement(ReadProperties.getProperty("pag.authentication"));
            requestHeader.appendChild(authentication);

            // UserName elements
            Element userName = document.createElement("UserName");
            userName.appendChild(document.createTextNode(XMLUserName));
            authentication.appendChild(userName);

            // Password elements
            Element password = document.createElement("Password");
            password.appendChild(document.createTextNode(XMLPassword));
            authentication.appendChild(password);

            //Region elements
            Element region = document.createElement("Region");
            requestHeader.appendChild(region);

            // SourceBank elements
            Element sourceBank = document.createElement("SourceBank");
            sourceBank.appendChild(document.createTextNode(XMLSourceBank));
            region.appendChild(sourceBank);

            // DestinationBank elements
            Element destinationBank = document.createElement("DestinationBank");
            destinationBank.appendChild(document.createTextNode(XMLDestinationBank));
            region.appendChild(destinationBank);

            //Body elements
            Element body = document.createElement("soapenv:Body");
            rootElement.appendChild(body);

            //pag:consultaDetalleLote elements
            Element consultaDetalleLote = document.createElement(ReadProperties.getProperty("pag.consultaDetalleLote"));
            body.appendChild(consultaDetalleLote);

            //CUSTOMER_ID elements
            Element customer = document.createElement("CUSTOMER_ID");
            customer.appendChild(document.createTextNode(customerId));
            consultaDetalleLote.appendChild(customer);

            //QUERY_TYPE elements
            Element query = document.createElement("QUERY_TYPE");
            query.appendChild(document.createTextNode(queryType));
            consultaDetalleLote.appendChild(query);

            //QUERY_TYPE elements
            Element queryV = document.createElement("QUERY_VALUE");
            queryV.appendChild(document.createTextNode(queryValue));
            consultaDetalleLote.appendChild(queryV);

            // write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            // pretty print XML
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");

            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(new File(routeOut+name));
            transformer.transform(source, result);
            System.out.println("File saved!");

        }catch (Exception ex){
            System.out.println("Exception: " + ex.getMessage());
        }
    }
}