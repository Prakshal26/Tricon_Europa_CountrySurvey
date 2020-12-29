package handlers;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ChronListHandler {

    static void handleChronData(NodeList nodeList, StringBuilder stringBuilder) {

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
                        subElement.getTagName()!="PERSON-NAME" &&
                        subElement.getTagName()!="SC") {
                    handleChronData(subElement.getChildNodes(),stringBuilder);
                }
            }
        }
    }


    public static void handleSubElements(Element element, StringBuilder stringBuilder) {

        String tagName = element.getTagName();
        switch (tagName) {
            case "CHRON-HEAD":
                stringBuilder.append("<b>");
                handleChronData(element.getChildNodes(),stringBuilder);
                stringBuilder.append("</b>");
                break;
            case "CHRON-TEXT":
                handleChronData(element.getChildNodes(),stringBuilder);
                stringBuilder.append("</p>");
                break;
            default:
                break;
        }

    }
    public static String chronListHandler(Element element) {

       StringBuilder chronBuilder = new StringBuilder();
       NodeList nodeList = element.getElementsByTagName("CHRON-ENTRY");
       for (int i=0; i<nodeList.getLength(); i++) {
           Node node = nodeList.item(i);
           NodeList subNodeList = node.getChildNodes();
           for (int j=0; j< subNodeList.getLength();j++) {
               Node subNode = subNodeList.item(j);
               if (subNode.getNodeType()==Node.ELEMENT_NODE) {
                   chronBuilder.append("<p>");
                   Element subElement = (Element) subNode;
                   handleSubElements(subElement,chronBuilder);
                   chronBuilder.append("</p>");
               }
           }
       }
       return chronBuilder.toString();
    }
}
