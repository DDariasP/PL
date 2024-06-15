package prole;

import prole.puntos.MyPuntos;
import prole.semantico.MyETDSDesc;
import prole.sintactico.MyParser;
import prole.sintactico.SintaxException;
import prole.lexico.Token;
import prole.lexico.MyLexer;
import prole.afd.AFD;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

/**
 *
 * @author diego
 */
public class Main {

    public static String[] ejemplo = {"Ej1", "Ej2", "Ej3"};

    /**
     * Punto de entrada de la aplicacion
     *
     * @param args Argumentos.
     * @throws java.io.IOException Error en File.
     * @throws prole.sintactico.SintaxException Error sintactico.
     */
    public static void main(String[] args) throws IOException, SintaxException {
        for (int i = 0; i < ejemplo.length; i++) {

            File mainfile = new File(ejemplo[i] + ".txt");

            //Analizador lexico
            try {
                File outputfile = new File(ejemplo[i] + "-Output.txt");
                outputfile.createNewFile();
                PrintStream stream = new PrintStream(outputfile);
                MyLexer lexer = new MyLexer(mainfile);
                Token tk;
                do {
                    tk = lexer.getNextToken();
                    stream.println(tk.toString());
                } while (tk.getKind() != Token.EOF);
                stream.close();
                System.out.println("\nAnalizador lexico:");
                System.out.println("Archivo " + ejemplo[i] + ".txt creado.");
            } catch (Error | Exception err) {
                System.out.println(err);
            }

            //Analizador sintactico
            MyParser parser = new MyParser();
            System.out.println("\nAnalizador sintactico:");
            if (parser.parse(mainfile)) {
                System.out.println("Correcto.");
            } else {
                System.out.println("Incorrecto.");
            }

            //Analizador semantico y AST
            System.out.println("\nAnalizador semantico:");
            MyETDSDesc etds = new MyETDSDesc();
            etds.parse(mainfile);
            System.out.println("AST resultado:");
            System.out.println(etds.getAST().arbol.getListaH());

            //Algoritmo de puntos y AFD
            AFD asd = MyPuntos.genAFD(etds.getAST());
            
            
            
            

            System.out.println("\n------------------------------------------------------------------------\n");
        }
    }

}
