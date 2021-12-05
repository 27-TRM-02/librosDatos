
import java.io.File;
import java.io.FileOutputStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;
import java.io.IOException;
import javax.swing.JOptionPane;
import org.w3c.dom.DOMException;


/*********** ACCESO A DATOS **************
 * @author trm - cetys
 */
public class MetodosDOM {

    
    Document doc;

    /**
     * @param fichero
     * @return 0 = Todo OK  //  -1 = Error
     * Recibe fichero xml. Lo convierte a formato DOM y lo guarda en doc
     */ 
    public int abrir_XML_DOM(File fichero) {
        // doc representará el árbol DOM
        doc = null;
        try {
            // Se crea un objeto DocumentBuilderFactory
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            // Indica que el modelo DOM no debe contemplar los comentarios 
            // que tenga el XML.
            factory.setIgnoringComments(true);
            // Ignora los espacios en blanco que tenga el documento
            factory.setIgnoringElementContentWhitespace(true);
            // Creamos objeto para cargar la estructura del arbol del DOM
            DocumentBuilder builder = factory.newDocumentBuilder();
            // Parsea el documento XML y genera el DOM equivalente
            doc = builder.parse(fichero);
            // Nos apunta al arbol DOM listo para ser recorrido 
            return 0;
        } catch (Exception e) {
            return -1;
        }
    }

    /**
     *
     * @param titulo
     * @param autor
     * @param anno
     * @return 0 = Todo OK  //  -1 = Error
     * Recibe elementos de libro, y crea dicho libro, guardandolo en doc
     */
    public int annadirLibroDOM(String titulo, String autor, String anno) {
        try {
            // Se crea un nodo tipo Element con nombre Titulo
            Node ntitulo = doc.createElement("Titulo");
            // Nodo texto con el titulo del libro
            Node ntitulo_text = doc.createTextNode(titulo);
            // Nodo texto como hijo de Titulo
            ntitulo.appendChild(ntitulo_text);
            // Hacemos lo mismo con el Element Autor
            Node nautor = doc.createElement("Autor");
            Node nautor_text = doc.createTextNode(autor);
            nautor.appendChild(nautor_text);
            // Creamos Nodo Element de Libro
            Node nlibro = doc.createElement("Libro");
            // Al nodo Libro se le añade atributo "publicado_en"
            ((Element) nlibro).setAttribute("publicado_en", anno);
            // Añadimos a libro los nodos Titulo y Autor
            nlibro.appendChild(ntitulo);
            nlibro.appendChild(nautor);
            // Obtenemos primer nodo del doc --> (Libros)
            Node raiz = doc.getFirstChild();
            // añadimos como hijo Libro creado arriba
            raiz.appendChild(nlibro);

            return 0;
        } catch (DOMException e) {
            System.out.println(e.toString());
            return -1;
        }
    }

    /**
     * @return String delDOM del xml
     * Recorre e interpreta doc para devolver representación del DOM
     */
    public String recorrerDOMyMostrar() {
        String salida = "";
        Node node;
        String datos_nodo[] = null;
        // Obtiene el primer nodo del DOM (primer hijo) = LIBROS
        Node raiz = doc.getFirstChild();
        // Obtiene lista de nodos con todos los nodos hijo del raíz = LIBRO * 3
        NodeList nodelist = raiz.getChildNodes();
        // Procesa los nodos hijos
        for (int i = 0; i < nodelist.getLength(); i++) {
            node = nodelist.item(i);

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                // Es un nodo elemento = libro
                datos_nodo = procesarLibro(node);
                salida += "\n " + "Publicado en: " + datos_nodo[0];
                salida += "\n " + "El Título es: " + datos_nodo[1];
                salida += "\n " + "El Autor es: " + datos_nodo[2];
                salida += "\n ----------------------------------";
            }
        }
        return salida;
    }

    /**
     * 
     * @param tituloViejo
     * @param tituloNuevo
     * @return 0=Funciono bien / -1=Error
     * Reemplaza nombre del titulo 
     */
    public int replaceTitle(String tituloViejo, String tituloNuevo) {
        try {
            // Lista con todos los elementos Título del xml
            NodeList listaTitulos = doc.getElementsByTagName("Titulo");
            // Lista con todos los elementos Autor del xml
            NodeList listaAutores = doc.getElementsByTagName("Autor");
            // Recorremos Titulos
            for (int i=0; i < listaTitulos.getLength(); i++){
                Node nTitulo = listaTitulos.item(i);
                // Cuando tituloViejo = titulo existente de la lista
                if (nTitulo.getFirstChild().getNodeValue().equalsIgnoreCase(tituloViejo)){
                    // Nombre del autor del libro seleccionado
                    String autor = listaAutores.item(i).getFirstChild().getNodeValue();
                    // Yes = 0 ; No = 1 ; Cancell = 2 ;closeWindow = -1
                    int res = JOptionPane.showConfirmDialog(null, "¿Estas seguro que quieres cambiar el libro "
                            + tituloViejo +" del autor "+autor+"?");
                    if (res == 0){ // Si selecciona Yes
                        // Actualizamos el valor del título
                        nTitulo.getFirstChild().setTextContent(tituloNuevo);
                    }
                }
            }
            // Ha reemplazado correctamente
            return 0;
        } catch (Exception e) {
            System.out.println(e.toString());
            return -1;
        }
    }
    
    /**
     * Reemplaza nombre del autor
     * @param autorViejo
     * @param autorNuevo
     * @return 0=Funciono bien / -1=Error
     */
    public int replaceAutor(String autorViejo, String autorNuevo) {
        try {
            // Lista con todos los elementos Título del xml
            NodeList listaTitulos = doc.getElementsByTagName("Titulo");
            // Lista con todos los elementos Autor del xml
            NodeList listaAutores = doc.getElementsByTagName("Autor");
            // Recorremos Autores
            for (int i=0; i < listaAutores.getLength(); i++){
                Node nAutor = listaAutores.item(i);
                // Cuando tituloViejo = titulo existente de la lista
                if (nAutor.getFirstChild().getNodeValue().equalsIgnoreCase(autorViejo)){
                    // Nombre del autor del libro seleccionado
                    String titulo = listaTitulos.item(i).getFirstChild().getNodeValue();
                    // Yes = 0 ; No = 1 ; Cancell = 2 ;closeWindow = -1
                    int res = JOptionPane.showConfirmDialog(null, 
                            "¿Estas seguro que quieres cambiar el autor: "
                            + autorViejo +" con título: "+titulo+"?");
                    if (res == 0){ // Si selecciona Yes
                        // Actualizamos el valor del título
                        nAutor.getFirstChild().setTextContent(autorNuevo);
                    }
                }
            }
            // Ha reemplazado correctamente
            return 0;
        } catch (Exception e) {
            System.out.println(e.toString());
            return -1;
        }
    }
    
    /**
     * Reemplaza el año indicado
     * @param anoAntiguo
     * @param anoNuevo
     * @return 0=Funciono bien / -1=Error
     */
    public int replaceAno(String anoAntiguo, String anoNuevo){
        try {
            // Lista con todos los elementos libro del xml
            NodeList listaLibros = doc.getElementsByTagName("Libro");
            // Lista con todos los elementos Título del xml
            NodeList listaTitulos = doc.getElementsByTagName("Titulo");
            // Recorremos Libro
            for (int i=0; i < listaLibros.getLength(); i++){
                Node nlibro = listaLibros.item(i);
                
                String ano = nlibro.getAttributes().getNamedItem("publicado_en").getNodeValue();
                if (ano.equalsIgnoreCase(anoAntiguo)){
                    String titulo = listaTitulos.item(i).getFirstChild().getNodeValue();
                    // Yes = 0 ; No = 1 ; Cancell = 2 ;closeWindow = -1
                    int res = JOptionPane.showConfirmDialog(null, 
                            "¿Estas seguro que quieres cambiar el año: "
                            + anoAntiguo +" con título: "+titulo+"?");
                    if (res == 0){ // Si selecciona Yes
                        // Actualizamos el valor del año
                        nlibro.getAttributes().getNamedItem("publicado_en").setNodeValue(anoNuevo);
                    }             
                }
            }
            return 0;
        } catch (Exception e) {
            System.out.println(e.toString());
            return -1;
        }
    }
    
    /**
     * Recorre los elementos DOM, y almacena sus valores
     * en un array de Strings que devuelve
     * @param n
     * @return Strin[] Datos del lihro
     */
    private String[] procesarLibro(Node n) { // n= nodo libro
        String datos[] = new String[3];
        Node ntemp = null;
        int contador = 1;
        // Obtiene el valor del primer atributo del nodo.
        // En libros solo hay 1 atributo. por eso no hace falta bucle
        datos[0] = n.getAttributes().item(0).getNodeValue(); // = valor del atributo
        // Obtiene los hijos del Libro (titulo y autor)
        NodeList nodos = n.getChildNodes();
        // Recorre todos los nodos de la lista 'nodos'
        for (int i = 0; i < nodos.getLength(); i++) {
            ntemp = nodos.item(i);
            // Comprobamos que es nodo Elemento (titulo y autor)
            if (ntemp.getNodeType() == Node.ELEMENT_NODE) {
                datos[contador] = ntemp.getFirstChild().getNodeValue();
                contador++;
            }
        }
        return datos;
    }

    /**
     * @param archivo
     * @return 0 = Funciono bien / -1 = Error
     * Guarda file recibido con el título indicado
     */
    public int guardarDOMcomoFile(File archivo) {
        try {
            // Genera extension .xml si no se le indica
            if (!archivo.getAbsolutePath().endsWith(".xml")) {
                File xmlArchivo = new File(archivo.getAbsolutePath() + ".xml");
                if (!xmlArchivo.exists()){
                    // Crea fichero vacio con ruta correcta
                    new FileOutputStream(xmlArchivo).close();
                }
                archivo = xmlArchivo;
            }
            OutputFormat format = new OutputFormat(doc);
            format.setIndenting(true);
            // salida indentada
            format.setIndenting(true);
            // Escribo contenido en el File
            XMLSerializer serializer = new XMLSerializer(new FileOutputStream(archivo), format);
            serializer.serialize(doc);
            // Se ha ejecutado correctamente
            return 0;
        } catch (IOException e) {
            return 1;
        }
    }

}
