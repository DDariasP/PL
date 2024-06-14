package proleg.puntos;

import proleg.semantico.*;
import proleg.sintactico.*;
import java.io.*;

/**
 * Clase que lanza el algoritmo de puntos
 *
 * @author Diego Francisco Darias Pino
 *
 */
public class MyCompilerPuntos {

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
            MyPuntos.genAFD(parser.getAST());
        } else {
            System.out.println("Incorrecto");
        }
    }

}
