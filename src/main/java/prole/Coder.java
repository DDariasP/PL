package prole;

import prole.afd.*;
import prole.puntos.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

/**
 * Crea un archivo .java a partir del AFD.
 *
 * @author Diego Francisco Darias Pino
 */
public class Coder {

    public static void crearJAVA(AFD afd) {
        try {
            //Crea el archivo
            File java = new File(afd.nombre + ".java");
            java.createNewFile();
            //Escribe el codigo
            FileWriter writer = new FileWriter(afd.nombre + ".java");
            String output = "public class " + afd.nombre + " {\n\n";
            writer.write(output);
            output = "    public int transition(int state, char symbol) {\n";
            writer.write(output);
            output = "\tswitch(state) {\n";
            writer.write(output);
            for (Map.Entry<String, Estado> entry : afd.tablaS.entrySet()) {
                Estado s = entry.getValue();
                output = "\t  case " + s.nombre + ":\n";
                writer.write(output);
                for (int j = 0; j < s.destinos.length; j++) {
                    Tupla tp = s.destinos[j];
                    if (tp != null) {
                        output = "\t      if(symbol == '" + tp.st
                                + "') return " + tp.destino.nombre + ";\n";
                        writer.write(output);
                    }
                }
                output = "\t      return -1;\n";
                writer.write(output);
            }
            output = "\t  default:\n\t      return -1;\n";
            output = output + "\t  }\n    }\n\n";
            writer.write(output);
            output = "    public boolean isFinal(int state) {\n";
            output = output + "\tswitch(state) {\n";
            writer.write(output);
            for (Map.Entry<String, Estado> entry : afd.tablaS.entrySet()) {
                Estado s = entry.getValue();
                output = "\t  case " + s.nombre + ": return "
                        + s.ultimo + ";\n";
                writer.write(output);
            }
            output = "\t  default: return false;\n";
            output = output + "\t}\n    }\n\n}";
            writer.write(output);
            writer.close();
        } catch (IOException e) {
            System.out.println("Error en File.");
        }
    }

}
