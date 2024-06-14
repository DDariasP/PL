package proleg.ast;

import java.util.ArrayList;
import proleg.lexico.*;

/**
 * Clase para los operadores y el nodo raiz
 *
 * @author Diego Francisco Darias Pino
 */
public class Operador implements INodo {

    //Lista de simbolos de operadores
    public static String[] lista
            = {"*(", ")*", "+(", ")+", "?(", ")?", "|(", "|", ")|"};
    private static boolean terminal = false;
    private int ID;
    private String nombre;
    private ArrayList<INodo> hijos;

    public Operador() {
        hijos = new ArrayList<>();
    }

    @Override
    public int getID() {
        return ID;
    }

    @Override
    public void setID(int n) {
        ID = n;
    }

    @Override
    public String getNombre() {
        return nombre;
    }

    @Override
    public void setNombre(String s) {
        nombre = s;
    }

    @Override
    public int getNumHijos() {
        return hijos.size();
    }

    @Override
    public INodo getHijoN(int n) {
        return hijos.get(n);
    }

    @Override
    public ArrayList<INodo> getListaH() {
        return hijos;
    }

    @Override
    public void addHijo(INodo n) {
        hijos.add(n);
    }

    @Override
    public boolean esTerminal() {
        return terminal;
    }

    @Override
    public String toString() {
        String output = "";
        switch (ID) {
            //Muestra cada tipo de operador con su simbolo
            case MyConstants.OR:
                output = nombre;
                break;
            case MyConstants.LPAREN:
            case MyConstants.RPAREN:
                output = "|( ";
                for (int i = 0; i < hijos.size(); i++) {
                    output = output + hijos.get(i) + " ";
                }
                output = output + ")|";
                break;
            case MyConstants.STAR:
            case MyConstants.PLUS:
            case MyConstants.HOOK:
                output = nombre + "( ";
                for (int i = 0; i < hijos.size(); i++) {
                    output = output + hijos.get(i) + " ";
                }
                output = output + ")" + nombre;
                break;
            default:
                throw new AssertionError();
        }
        return output;
    }

}
