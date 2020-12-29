package handlers;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class TableHandler {

    public static void handleEntry(NodeList nodeList, StringBuilder stringBuilder, String open, String close) {

        for (int i=0; i< nodeList.getLength();i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType()==Node.ELEMENT_NODE) {
                Element element = (Element) node;
                if (element.getTagName().equalsIgnoreCase("ENTRY")) {
                    stringBuilder.append(open);
                    stringBuilder.append(element.getTextContent());
                    stringBuilder.append(close);
                }
            }
        }
    }

    public static void handleTableRow(NodeList nodeList, StringBuilder stringBuilder, String open, String close) {

        for (int i=0; i< nodeList.getLength();i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                stringBuilder.append("<tr>");
                handleEntry(element.getChildNodes(), stringBuilder, open,close);
                stringBuilder.append("</tr>");
            }
        }
    }

    public static void handleTable(NodeList nodeList, StringBuilder stringBuilder) {

        for (int i=0; i< nodeList.getLength();i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType()==Node.ELEMENT_NODE) {
                Element element = (Element) node;
                if (element.getTagName().equalsIgnoreCase("THEAD")) {
                    handleTableRow(element.getChildNodes(), stringBuilder, "<th>","</th>");
                }
                if (element.getTagName().equalsIgnoreCase("TBODY")) {
                    handleTableRow(element.getChildNodes(), stringBuilder, "<td>","</td>");
                }
            }
        }

    }

    public static void match(Element element, StringBuilder stringBuilder) {

        String tagName = element.getTagName();

        switch (tagName) {

            case "TABLE-HEADING":
                stringBuilder.append("<b>");
                stringBuilder.append(element.getTextContent());
                stringBuilder.append("</b><br>");
                break;
            case "HEADING-NOTE":
                stringBuilder.append("(");
                stringBuilder.append(element.getTextContent());
                stringBuilder.append(")");
              //  stringBuilder.append("<br>");
                break;
            case "TABLE":
               NodeList tGroupList = element.getElementsByTagName("TGROUP");
                for (int i=0;i <tGroupList.getLength();i++) {
                    Node tGroup = tGroupList.item(i);
                    if (tGroup.getNodeType()==Node.ELEMENT_NODE) {
                        Element tGroupElement = (Element) tGroup;
                        /*
                        <!ELEMENT TABLE (tgroup+)
                        <!ELEMENT TGROUP (colspec*,thead?,tbody)
                        Here we are passing all childs of Tgroup i.e colspec, thead and tbody.
                         */
                        stringBuilder.append("<table>");
                        handleTable(tGroupElement.getChildNodes(),stringBuilder);
                        stringBuilder.append("</table>");
                    }
                }
                break;
            case "SOURCES":
                stringBuilder.append("<br>");
                stringBuilder.append(element.getTextContent());
                break;
            default:
                break;
        }

    }

    public static void handleTGroup (NodeList nodeList, StringBuilder stringBuilder) {

        for (int i=0; i< nodeList.getLength();i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType()==Node.ELEMENT_NODE) {
                Element element = (Element) node;
                match(element, stringBuilder);
            }
        }

    }
}
