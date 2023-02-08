package demos.java13;

import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class XmlDemo {

    public static void main(String[] args) {
        var factory = DocumentBuilderFactory.newNSInstance();
        // factory.setNamespaceAware(true);
        try {
            var builder = factory.newDocumentBuilder();
            var document = builder.parse(Files.newInputStream(Path.of("src/main/resources/employee.xml")));
            var element = document.getElementsByTagNameNS("https://training.com/employees", "name")
                    .item(0);
            System.out.println(element.getNodeName());
            System.out.println(element.getLocalName());
            System.out.println(element.getNamespaceURI());
            System.out.println(element.getTextContent());

        } catch (IOException | SAXException | ParserConfigurationException e) {
            throw new RuntimeException(e);
        }

    }
}
