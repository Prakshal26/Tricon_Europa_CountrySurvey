package handlers;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import pojo.Country;

public class AuthorHandler {

    static void handleIndividualAuthor(NodeList nodeList, StringBuilder authorBuilder) {

        for (int i=0; i<nodeList.getLength();i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.TEXT_NODE) {
                if (!node.getNodeValue().trim().isEmpty()) {
                    authorBuilder.append(node.getNodeValue().trim());
                    authorBuilder.append(" ");
                }
            }
            if (node.hasChildNodes()) {
                handleIndividualAuthor(node.getChildNodes(),authorBuilder);
            }
        }
    }

   public static void handleAuthorGroup(Element element, Country country) {

       NodeList nodeList = element.getChildNodes();

       StringBuilder authorBuilder = new StringBuilder();

       for (int i=0; i<nodeList.getLength();i++) {
           Node node = nodeList.item(i);
           if (node.getNodeType() == Node.ELEMENT_NODE) {
               Element subElement = (Element) node;
               if (subElement.getTagName().equalsIgnoreCase("PRIMARY-AUTHORS")) {
                   authorBuilder.append("<p><b>");
                   AuthorHandler.handleIndividualAuthor(subElement.getChildNodes(),authorBuilder);
                   authorBuilder.append("</b></p>");
               }
               if (subElement.getTagName().equalsIgnoreCase("OTHER-AUTHORS")) {
                   authorBuilder.append("<p>");
                   AuthorHandler.handleIndividualAuthor(subElement.getChildNodes(),authorBuilder);
                   authorBuilder.append("</p>");
               }
           }
       }
       country.setAuthor(authorBuilder.toString());
    }
}
