package parser;

import database.PostgreConnect;
import handlers.AuthorHandler;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import pojo.Country;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Set;

public class ElementParse {

    static void match(Element element, Country country) {

        String tagName = element.getTagName();
        switch (tagName) {

            case "HEADING":
                country.setHeading(element.getTextContent());
                break;
            case "SUB-HEAD":
                break;
            case "AUTHOR-GROUP":
                AuthorHandler.handleAuthorGroup(element, country);
                break;
            case "GEN-SECTION":
                break;
            case "CHRON-LIST":
                break;
            default:
                break;
        }
    }

    static void parseFiles(Document doc) {

        doc.getDocumentElement().normalize();
        Node entryNode = doc.getDocumentElement();

        Country country = new Country();
        NodeList nodeList = entryNode.getChildNodes();

        Element entryElement = (Element) entryNode;

        if (entryElement.hasAttribute("ISO")) {
            country.setIso(entryElement.getAttribute("ISO"));
        }
        if (entryElement.hasAttribute("ID")) {
            country.setXmlId(entryElement.getAttribute("ID"));
        }

        for (int i=0; i<nodeList.getLength();i++) {
            Node nNode = nodeList.item(i);
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element)nNode;
                ElementParse.match(element, country);
            }
        }
        PostgreConnect.insertCountry(country);
    }
}

