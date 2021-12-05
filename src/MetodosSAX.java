
import java.io.File;
import java.io.IOException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author trm
 */
public class MetodosSAX {

    SAXParser parser;
    ManejadorSAX sh;
    File ficheroXML;

    /**
     * Recibe fichero xml y lo guarda a uno SAX
     * @param fichero xml
     * @return 0 = todo BIEN //  -1 = FALLO
     */
    public int abrir_XML_SAX(File fichero) {
        try {
            // Se crea un objeto SAXParser para interpretar el doc XML
            SAXParserFactory factory = SAXParserFactory.newInstance();
            parser = factory.newSAXParser();
            // Se crea una instancia del manejador que será el que 
            // recorra el documento XML secuencialmente
            sh = new ManejadorSAX();

            ficheroXML = fichero;

            return 0;
        } catch (Exception e) {
            System.out.println(e.toString());
            return -1;
        }
    }
    
    /**
     * 
     * @return 
     */
    public String recorrerSAX() {
        try {
            sh.cadena_resultado="";
            parser.parse(ficheroXML, sh);
            return sh.cadena_resultado;
            
        } catch (IOException | SAXException e) {
            return "Error al parsear con SAX";
        }
    }
}

class ManejadorSAX extends DefaultHandler {

    String cadena_resultado = "";
    /**
     * Cndo detecta cadena de txt lo guarda en la variable de salida
     * @param ch
     * @param start
     * @param length
     * @throws SAXException
     */
    public void characters(char[] ch, int start, int length)
            throws SAXException {
        for (int i = start; i < length + start; i++) {
            cadena_resultado += ch[i];
        }
        // trim() = quita los espacios en blanco de delante y del final
        cadena_resultado = cadena_resultado.trim() + "\n";
    }

    /**
     * Cuando se detecta el final de un elemento <libro>
     * se pone una linea discontinua
     * @param uri
     * @param localName
     * @param qname
     * @throws SAXException
     */
    public void endElement(String uri, String localName, String qname)
            throws SAXException {
        if (qname.equals("Libro")) {
            cadena_resultado += "--------------------\n";
        }
    }

    /**
     * Se ejecuta cuando se encuentra un elemento de apertura de xml
     * @param uri
     * @param localName
     * @param qname
     * @param attributes
     * @throws SAXException 
     */
    public void startElement(String uri, String localName, String qname,
            Attributes attributes) throws SAXException {
        if (qname.equalsIgnoreCase("Libros")) {
            cadena_resultado += "Se van a mostrar los libros de este documento";
        } else if (qname.equalsIgnoreCase("Libro")) {
            cadena_resultado += "Publicado en: "
                    + attributes.getValue(attributes.getQName(0).trim());
        } else if (qname.equalsIgnoreCase("Titulo")) {
            cadena_resultado += "El título es: ";
        } else if (qname.equalsIgnoreCase("Autor")) {
            cadena_resultado += "El autor es: ";
        }
    }
}
