package prole;

import prole.lexico.*;
import prole.sintactico.*;
import prole.semantico.*;
import prole.ast.*;
import prole.puntos.*;
import prole.afd.*;
import java.io.*;
import java.util.Map;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;

/**
 *
 * @author Diego Francisco Darias Pino
 */
public class Main {

    public static String[] ejemplo = {"Ej1", "Ej2", "Ej3"};
    public static String[] good = {"b a a b", "a b b c", "a o b a a"};
    public static String[] bad = {"b b a a", "c a a c", "a a b a"};

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
            System.out.println(ejemplo[i] + ".txt");

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
                // Initialize the BufferedReader
                try (BufferedReader br = new BufferedReader(new FileReader(ejemplo[i] + "-Output.txt"))) {
                    String line;
                    // Read and print each line until the end of the file is reached
                    while ((line = br.readLine()) != null) {
                        System.out.println(line);
                    }
                }
            } catch (Error | Exception err) {
                System.out.println(err);
            }

            //Analizador sintactico
            MyParser parser = new MyParser();
            System.out.println("\n\nAnalizador sintactico:");
            if (parser.parse(mainfile)) {
                System.out.println("Correcto.");
            } else {
                System.out.println("Incorrecto.");
            }

            //Analizador semantico y AST
            System.out.println("\n\nAnalizador semantico y AST:");
            MyETDSDesc etds = new MyETDSDesc();
            etds.parse(mainfile);
            AST ast = etds.getAST();
            // Initialize the BufferedReader
            try (BufferedReader br = new BufferedReader(new FileReader(ast.arbol.getNombre() + ".txt"))) {
                String line;
                // Read and print each line until the end of the file is reached
                while ((line = br.readLine()) != null) {
                    System.out.println(line);
                }
            } catch (IOException e) {
            }

            //Algoritmo de puntos y AFD
            System.out.println("\n\nAlgoritmo de puntos:");
            AFD afd = MyPuntos.genAFD(etds.getAST());
            for (Map.Entry<String, Estado> entry : afd.tablaS.entrySet()) {
                System.out.println(entry.getValue());
            }

            //Fichero .java
            System.out.println("\nFichero " + afd.nombre + ".java:");
            Coder.crearJAVA(afd);
            // Initialize the BufferedReader
            try (BufferedReader br = new BufferedReader(new FileReader(afd.nombre + ".java"))) {
                String line;
                // Read and print each line until the end of the file is reached
                while ((line = br.readLine()) != null) {
                    System.out.println(line);
                }
            } catch (IOException e) {
            }

            //Representacion del AFD y lectura de cadenas
            Visualizador f = new Visualizador(afd, good[i]);
            f.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            f.setBounds(100, 300, 500, 240);
            f.setTitle("Paso a paso");
            f.setVisible(true);

            System.out.println("\n------------------------------------------------------------------------\n");
        }
    }

}
