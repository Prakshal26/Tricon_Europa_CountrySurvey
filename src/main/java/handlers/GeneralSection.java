package handlers;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import pojo.Country;

import java.util.Set;


public class GeneralSection {

    static void handleParagraph(NodeList nodeList, StringBuilder stringBuilder) {

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
                    stringBuilder.append("<a href");
                    stringBuilder.append(subElement.getNodeValue());
                    stringBuilder.append("</a>");
                }
                if (subElement.getTagName().equalsIgnoreCase("PERSON-NAME")) {
                    stringBuilder.append("<b> ");
                    AuthorHandler.handleIndividualAuthor(subElement.getChildNodes(),stringBuilder);
                    stringBuilder.append("</b>");
                }
                if (subElement.hasChildNodes() && subElement.getTagName()!="B" &&
                        subElement.getTagName()!="I" &&
                        subElement.getTagName()!="XR" &&
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
                stringBuilder.append("</p></b>");
                break;
            case "P":
            case "BLOCKQUOTE":
                stringBuilder.append("<p>");
                handleParagraph(element.getChildNodes(),stringBuilder);
                stringBuilder.append("</p>");
                break;
            case "GEN-LIST":
                break;
            case "FIGURE-GROUP":
                break;
            case "TABLE-BLOCK":
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
