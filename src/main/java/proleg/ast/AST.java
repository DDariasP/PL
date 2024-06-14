package proleg.ast;

import proleg.lexico.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Definicion de un Arbol Sintactico Abstracto
 *
 * @author Diego Francisco Darias Pino
 */
public class AST {

    //Nodo raiz
    public INodo arbol;

    public AST() {
        //Raiz de la subclase Operador de la interfaz INodo
        arbol = new Operador();
    }

    //Escribe el arbol en un archivo txt
    public void print() {
        try {
            //Crea el archivo
            String filename = arbol.getNombre() + ".txt";
            File file = new File(filename);
            if (file.exists()) {
                file.delete();
                System.out.println("\nArchivo " + file.getName() + " sobreescrito.");
            } else {
                System.out.println("\nArchivo " + file.getName() + " creado.");
            }
            file.createNewFile();
            FileWriter writer = new FileWriter(filename);
            //Escribe el nombre
            writer.write(arbol.getNombre());
            //Escribe los elementos del nodo raiz
            for (int numh = 0; numh < arbol.getNumHijos(); numh++) {
                writer.write(pintarHijo(arbol, numh, 0));
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("Error en File.");
        }
    }

    //El nivel indica la profundidad alcanzada hasta el momento
    private static String pintarHijo(INodo a, int numh, int nivel) {
        //Pinta las ramas
        String tab = "\n";
        for (int i = 0; i < nivel; i++) {
            if (i % 2 == 0) {
                tab = tab + "|   ";
            } else {
                tab = tab + "    ";
            }
        }
        String output = tab + "|---";
        //Decide como pintar un nodo segun su tipo
        INodo hijo = a.getHijoN(numh);
        switch (hijo.getID()) {
            //Los simbolos terminales son todos del tipo SYMBOL
            //y de la subclase Base
            case MyConstants.SYMBOL:
                output = output + pintarBase(hijo);
                break;
            //Los operadores aplicados a expresiones regulares
            //son todos de la subclase Operador
            case MyConstants.OR:
            case MyConstants.LPAREN:
            case MyConstants.RPAREN:
            case MyConstants.STAR:
            case MyConstants.PLUS:
            case MyConstants.HOOK:
                //Baja un nivel y pinta
                output = output + pintarOperacion(hijo, nivel + 1);
                break;
            default:
                throw new AssertionError();
        }
        return output;
    }

    //Pinta los simbolos terminales directamente
    private static String pintarBase(INodo h) {
        String output = "Base: " + h.getNombre();
        return output;
    }

    //Pinta los operadores
    private static String pintarOperacion(INodo h, int nivel) {
        String output = "Operador: " + h.getNombre();
        //Baja de nivel para pintar los hijos del elemento
        for (int numh = 0; numh < h.getNumHijos(); numh++) {
            output = output + pintarHijo(h, numh, nivel + 1);
        }
        return output;
    }

}
