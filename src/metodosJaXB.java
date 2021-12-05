
import com.sun.jmx.remote.internal.Unmarshal;
import java.io.File;
import java.util.List;
import javaLibros.Libros;
import javaLibros.Libros.Libro;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
/**
  * @author trm
 */
public class metodosJaXB {
    
    Libros misLibros;
    
    /**
     * Abre Fichero 
     * @param fichero
     * @return 
     */
    public int abrir_XML_JAXB(File fichero){
        try {
            // creamos instancia JAXB
            JAXBContext contexto = JAXBContext.newInstance(Libros.class);
            // Crea objeto Unmarshal
            Unmarshaller u = contexto.createUnmarshaller();
            // Deserializa fichero
            misLibros = (Libros) u.unmarshal(fichero);
            return 0;
        } catch (Exception e) {
            return -1;
        }
    }
    
    /**
     * Recorre el JAXB
     * @return String datos Libros 
     */
    public String recorrerJAXB() {
        String cadena_resultado = "";
        List<Libros.Libro> lLibros = misLibros.getLibro();
        // recorremos libros
        for (int i=0; i<lLibros.size(); i++) {
            Libro libro_temp = lLibros.get(i);
            cadena_resultado += "\nPublicado en: " + libro_temp.getPublicadoEn();
            cadena_resultado += "\nTitulo: " + libro_temp.getTitulo();
            cadena_resultado += "\nAutor: " + libro_temp.getAutor();
            cadena_resultado += "\nEditorial: " + libro_temp.getEditorial();
            cadena_resultado += "\n----------------------------------";
        }
        return cadena_resultado;
    }
    
}
