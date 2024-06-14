package proleg.semantico;

import java.io.*;

/**
 * Clase que desarrolla el analisis semantico
 *
 * @author Diego Francisco Darias Pino
 *
 */
public class MyCompilerSem {

    /**
     * Punto de entrada de la aplicacion
     *
     * @param args Argumentos.
     */
    public static void main(String[] args) {
        File mainfile = new File("Ejemplo.txt");
        try {
            //Lanza el analizador semantico
            MyETDSDesc parser = new MyETDSDesc();
            if (parser.parse(mainfile)) {
                printOutput("Correcto");
            } else {
                printOutput("Incorrecto");
            }
        } catch (Error err) {
            printError(mainfile.getName(), err);
            printOutput("Incorrecto");

        } catch (Exception ex) {
            printError(mainfile.getName(), ex);
            printOutput("Incorrecto");
        }
    }

    /**
     * Genera el fichero de error
     *
     * @param e Error a mostrar
     */
    private static void printError(String filename, Throwable e) {
        try {
            File errorfile = new File("Errors.txt");
            if (errorfile.exists()) {
                errorfile.delete();
                System.out.println("\nArchivo " + errorfile.getName() + " sobreescrito.");
            } else {
                System.out.println("\nArchivo " + errorfile.getName() + " creado.");
            }
            errorfile.createNewFile();
            PrintStream errorStream = new PrintStream(errorfile);
            errorStream.println("[File " + filename + "] 1 error found:");
            errorStream.println(e.toString());
            errorStream.close();
        } catch (Exception ex) {
        }
    }

    /**
     * Genera el fichero de salida
     *
     */
    private static void printOutput(String msg) {
        try {
            File outputfile = new File("Output.txt");
            if (outputfile.exists()) {
                outputfile.delete();
                System.out.println("\nArchivo " + outputfile.getName() + " sobreescrito.");
            } else {
                System.out.println("\nArchivo " + outputfile.getName() + " creado.");
            }
            outputfile.createNewFile();
            PrintStream stream = new PrintStream(outputfile);
            stream.println(msg);
            stream.close();
        } catch (Exception ex) {
        }
    }
}
