package com.project;

import java.io.File;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.*;


public class Main {

    Document doc;
    ArrayList<String[]> out = new ArrayList();

    public static void main(String[] args) {
        try {
            int tabs=0;
            Main app = new Main();

            File inputFile = new File("data.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            app.doc = dBuilder.parse(inputFile);
            app.doc.getDocumentElement().normalize();

            //Recursive function passing through each node.
            app.getData(app.doc.getChildNodes());

            for(String[] d: app.out){
                if(d[0].equals("start")){
                    System.out.println(app.gaps(tabs) + d[1]);
                    tabs++;
                }else if(d[0].equals("end")){
                    tabs--;
                    System.out.println(app.gaps(tabs) + d[1]);
                }else{
                    System.out.println(app.gaps(tabs) + d[1]);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String gaps(int quantity){
        String ret="";

        for(int i=0; i<quantity; i++){
            ret += "\t";
        }
        return ret;
    }

    public void getData(NodeList nl){
        for(int i=0; i < nl.getLength(); i++){
            if(nl.item(i).getNodeName().equals("#text")){
                continue;
            }
            out.add(new String[]{"start", nl.item(i).getNodeName()});
            if(nl.item(i).hasAttributes()){
               NamedNodeMap attributes = nl.item(i).getAttributes();
               for(int j=attributes.getLength()-1; j >= 0; j--) {
                   out.add(new String[]{"info", "Attribute: " + attributes.item(j).getNodeName() + " , Value: " + attributes.item(j).getTextContent()});
               }
            }
            NodeList n = nl.item(i).getChildNodes();
            if(n.getLength() > 1) {
                getData(n);
            }else{
                if(n.getLength() > 0) {
                    out.add(new String[]{"value", n.item(0).getTextContent()});
                }
            }
            out.add(new String[]{"end", nl.item(i).getNodeName()});
        }
    }
}
