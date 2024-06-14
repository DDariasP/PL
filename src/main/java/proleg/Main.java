package proleg;

import java.io.File;
import java.io.IOException;
import proleg.afd.*;
import proleg.puntos.*;
import proleg.semantico.*;
import proleg.sintactico.*;

/**
 *
 * @author diego
 */
public class Main {

    /**
     * Punto de entrada de la aplicacion
     *
     * @param args Argumentos.
     * @throws java.io.IOException Error en File.
     * @throws proleg.sintactico.SintaxException Error sintáctico.
     */
    public static void main(String[] args) throws IOException, SintaxException {
        File mainfile = new File("Ejemplo.txt");
        //Comienza creando el arbol
        MyETDSDesc parser = new MyETDSDesc();
        if (parser.parse(mainfile)) {
            System.out.println("Correcto");
            //Inicia el algoritmo
            AFD afd = MyPuntos.genAFD(parser.getAST());
            Coder.crearJAVA(afd);
        } else {
            System.out.println("Incorrecto");
        }
    }

}
