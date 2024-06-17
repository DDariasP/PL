package prole;

import prole.lexico.*;
import prole.sintactico.*;
import prole.semantico.*;
import prole.ast.*;
import prole.puntos.*;
import prole.afd.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Map;
import javax.swing.JFrame;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import static javax.swing.WindowConstants.*;

/**
 * Muestra los resultados de cada paso del proyecto.
 *
 * @author Diego Francisco Darias Pino
 */
public class Main {

    //3 AFD de ejemplo
    public static String[] ejemplo = {"Ej1", "Ej2", "Ej3"};
    //1 cadena para cada AFD, modificar para probar otras
    public static String[] good = {"b a a b", "a b b c", "a o b a a"};

    /**
     * Punto de entrada de la aplicacion.
     *
     * @param args Argumentos.
     * @throws java.io.IOException Error en File.
     * @throws prole.sintactico.SintaxException Error sintactico.
     */
    public static void main(String[] args) throws IOException, SintaxException {
        //Recorre la lista de ejemplos
        for (int i = 0; i < ejemplo.length; i++) {

            //Abre el archivo
            File mainfile = new File(ejemplo[i] + ".txt");
            System.out.println(ejemplo[i] + ".txt");

            //Analizador lexico
            try {
                //Crea un archivo para guardar el resultado del analisis
                File outputfile = new File(ejemplo[i] + "-Output.txt");
                outputfile.createNewFile();
                PrintStream stream = new PrintStream(outputfile);
                MyLexer lexer = new MyLexer(mainfile);
                Token tk;
                //Hace el analisis lexico
                do {
                    tk = lexer.getNextToken();
                    stream.println(tk.toString());
                } while (tk.getKind() != Token.EOF);
                stream.close();
                //Muestra en consola el contenido del archivo creado
                System.out.println("\nAnalizador lexico:");
                try (BufferedReader br = new BufferedReader(new FileReader(ejemplo[i] + "-Output.txt"))) {
                    String line;
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
            //Muestra en consola si el archivo es sintacticamente correcto
            if (parser.parse(mainfile)) {
                System.out.println("Correcto.");
            } else {
                System.out.println("Incorrecto.");
            }

            //Analizador semantico y AST
            System.out.println("\n\nAnalizador semantico y AST:");
            //Hace el analisis semantico
            MyETDSDesc etds = new MyETDSDesc();
            etds.parse(mainfile);
            //Toma el AST resultante
            AST ast = etds.getAST();
            //Lo muestra en consola
            try (BufferedReader br = new BufferedReader(new FileReader(ast.arbol.getNombre() + ".txt"))) {
                String line;
                while ((line = br.readLine()) != null) {
                    System.out.println(line);
                }
            } catch (IOException e) {
            }

            //Algoritmo de puntos y AFD
            System.out.println("\n\nAlgoritmo de puntos:");
            //Ejecuta el algoritmo de puntos
            AFD afd = MyPuntos.genAFD(etds.getAST());
            //Muestra los estados resultantes en consola
            for (Map.Entry<String, Estado> entry : afd.tablaS.entrySet()) {
                System.out.println(entry.getValue());
            }

            //Fichero .java
            System.out.println("\nFichero " + afd.nombre + ".java:");
            //Crea el archivo .java del AFD
            Coder.crearJAVA(afd);
            //Muestra el contenido del archivo en consola
            try (BufferedReader br = new BufferedReader(new FileReader(afd.nombre + ".java"))) {
                String line;
                while ((line = br.readLine()) != null) {
                    System.out.println(line);
                }
            } catch (IOException e) {
            }

            //Lectura de cadenas
            //Muestra el grafo
            JFrame g = new JFrame(afd.nombre);
            VisualizationViewer<String, String> grafo = GrafoAFD.crear(afd);
            g.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            g.add(grafo);
            g.setBounds(650 + 200 * i, 150 * i, 650, 650);
            g.setVisible(true);
            g.toFront();

            //Reconoce los caracteres
            Visualizador f = new Visualizador(afd, good[i], g);
            f.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            f.setBounds(200, 300 * i, 400, 240);
            f.setTitle("Paso a paso - " + afd.nombre);
            f.setVisible(true);

            //Hace que cerrar g tambien cierre f
            g.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    g.dispose();
                    f.dispose();
                }
            });

            //Hace que cerrar f tambien cierre g
            f.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    g.dispose();
                    f.dispose();
                }
            });

            //Separa los resultados de cada ejemplo
            System.out.println("\n------------------------------------------------------------------------\n");
        }
    }

}
