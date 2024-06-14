package proleg;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import proleg.afd.*;
import proleg.puntos.*;

/**
 *
 * @author diego
 */
public class Coder {

    public static void crearJAVA(AFD afd) {
        try {
            File java = new File(afd.nombre + ".java");
            if (java.exists()) {
                java.delete();
                System.out.println("\nArchivo " + java.getName() + " sobreescrito.\n");
            } else {
                System.out.println("\nArchivo " + java.getName() + " creado.\n");
            }
            java.createNewFile();

            FileWriter writer = new FileWriter(afd.nombre + ".java");
            String output = "public class " + afd.nombre + " {\n\n";
            writer.write(output);
            output = "    public int transition(int state, char symbol) {\n";
            writer.write(output);
            output = "\tswitch(state) {\n";
            writer.write(output);
            for (int i = 0; i < afd.listaS.size(); i++) {
                Estado s = afd.listaS.get(i);
                output = "\t  case " + s.nombre + ":\n";
                writer.write(output);
                for (int j = 0; j < s.destinos.length; j++) {
                    Tupla tp = s.destinos[j];
                    if (tp != null) {
                        output = "\t      if(symbol == '" + tp.symT
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
            for (int i = 0; i < afd.listaS.size(); i++) {
                Estado s = afd.listaS.get(i);
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
