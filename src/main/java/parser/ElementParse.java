package parser;

import database.PostgreConnect;
import handlers.AuthorHandler;
import handlers.ChronListHandler;
import handlers.GeneralSection;
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

    static void match(Element element, Country country, StringBuilder generalSectionBuilder, StringBuilder introductionBuilder) {

        String tagName = element.getTagName();
        switch (tagName) {

            case "HEADING":
                country.setHeading(element.getTextContent());
                break;
                //some files even have P under root element. Check AD.HI
            case "SUB-HEAD":
                country.setSubheading(element.getTextContent());
                break;
            case "P":
                introductionBuilder.append("<p>");
                GeneralSection.handleParagraph(element.getChildNodes(),introductionBuilder);
                introductionBuilder.append("</p>");
                break;
            case "AUTHOR-GROUP":
                country.setAuthor(AuthorHandler.handleAuthorGroup(element, country));
                break;
            case "GEN-SECTION":
                GeneralSection.handleGeneralSection(element, generalSectionBuilder);
                generalSectionBuilder.append("<br>");
                break;
            case "CHRON-LIST":
                country.setChronData(ChronListHandler.chronListHandler(element));
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

        StringBuilder generalSectionBuilder = new StringBuilder();
        StringBuilder introductionBuilder = new StringBuilder();

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
                ElementParse.match(element, country, generalSectionBuilder, introductionBuilder);
            }
        }
        if (!introductionBuilder.toString().isEmpty()) {
            country.setIntroduction(introductionBuilder.toString());
        }
        if (!generalSectionBuilder.toString().isEmpty()) {
            country.setGeneralData(generalSectionBuilder.toString());
        }

        PostgreConnect.insertCountry(country);
    }
}

