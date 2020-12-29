package handlers;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import pojo.Country;

import java.util.Set;


public class GeneralSection {

    static void handleGeneralList(NodeList nodeList, StringBuilder stringBuilder) {

        for (int i=0;i<nodeList.getLength();i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.TEXT_NODE) {
                stringBuilder.append(node.getNodeValue().trim());
            }
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element subElement = (Element) node;
                if (subElement.getTagName().equalsIgnoreCase("ITEM")) {
                    stringBuilder.append(subElement.getTextContent());
                    stringBuilder.append("<br>");
                }
                if (subElement.getTagName().equalsIgnoreCase("I")) {
                    stringBuilder.append("<i> ");
                    stringBuilder.append(subElement.getTextContent());
                    stringBuilder.append("</i>");
                }
                if (subElement.hasChildNodes() && subElement.getTagName()!="I" &&
                        subElement.getTagName()!="ITEM") {
                    handleGeneralList(subElement.getChildNodes(),stringBuilder);
                }
            }
        }
    }

   public static void handleParagraph(NodeList nodeList, StringBuilder stringBuilder) {

        for (int i=0;i<nodeList.getLength();i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType()==Node.TEXT_NODE) {
                stringBuilder.append(node.getNodeValue().trim());
            }
            if (node.getNodeType()==Node.ELEMENT_NODE) {
                Element subElement = (Element) node;
                if (subElement.getTagName().equalsIgnoreCase("I")) {
                    stringBuilder.append("<i> ");
                    stringBuilder.append(subElement.getTextContent());
                    stringBuilder.append("</i>");
                }
                if (subElement.getTagName().equalsIgnoreCase("B")) {
                    stringBuilder.append("<b> ");
                    stringBuilder.append(subElement.getTextContent());
                    stringBuilder.append("</b>");
                }
                if (subElement.getTagName().equalsIgnoreCase("XR")) {
                    if (subElement.hasAttribute("REF")) {
                        stringBuilder.append(" ");
                        stringBuilder.append("<a href =\"https://www.europaworld.com/entry/");
                        stringBuilder.append(subElement.getAttribute("REF"));
                        stringBuilder.append("\">");
                        stringBuilder.append(subElement.getTextContent());
                        stringBuilder.append("</a>");
                    }
                }
                if (subElement.getTagName().equalsIgnoreCase("SC")) {
                    stringBuilder.append("<span style=\"font-variant:small-caps; font-weight:bold\">");
                    stringBuilder.append(" ");
                    stringBuilder.append(subElement.getTextContent());
                    stringBuilder.append("</span>");
                }
                if (subElement.getTagName().equalsIgnoreCase("PERSON-NAME")) {
                    stringBuilder.append("<b> ");
                    AuthorHandler.handleIndividualAuthor(subElement.getChildNodes(),stringBuilder);
                    stringBuilder.append("</b>");
                }
                if (subElement.hasChildNodes() && subElement.getTagName()!="B" &&
                        subElement.getTagName()!="I" &&
                        subElement.getTagName()!="XR" &&
                        subElement.getTagName()!="SC" &&
                        subElement.getTagName()!="PERSON-NAME") {
                    handleParagraph(subElement.getChildNodes(),stringBuilder);
                }
            }
        }
    }

    static void handleSubElements(Element element, StringBuilder stringBuilder) {

        String tagName = element.getTagName();
        switch (tagName) {
            case "HEADING":
                stringBuilder.append("<p><b>");
                stringBuilder.append(element.getTextContent());
                stringBuilder.append("</b></p>");
                break;
            case "P":
            case "BLOCKQUOTE":
                stringBuilder.append("<p>");
                handleParagraph(element.getChildNodes(),stringBuilder);
                stringBuilder.append("</p>");
                break;
            case "GEN-LIST":
                stringBuilder.append("<p>");
                handleGeneralList(element.getChildNodes(),stringBuilder);
                stringBuilder.append("</p>");
                break;
            case "FIGURE-GROUP":
                break;
            case "TABLE-BLOCK":
                stringBuilder.append("<p>");
                TableHandler.handleTGroup(element.getChildNodes(), stringBuilder);
                stringBuilder.append("</p>");
                break;
            case "GEN-SECTION":
                //Gen-Section also has Gen-Section tag inside it.
                handleGeneralSection(element,stringBuilder);
                break;
            default:
                break;
        }
    }

    public static void handleGeneralSection(Element element, StringBuilder generalSectionBuilder) {

        NodeList nodeList = element.getChildNodes();
        for (int i=0; i<nodeList.getLength();i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType()==Node.ELEMENT_NODE) {
                Element subElement = (Element) node;
                handleSubElements(subElement, generalSectionBuilder);
            }
        }
    }
}
