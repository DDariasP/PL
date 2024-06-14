package proleg.puntos;

import proleg.ast.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

/**
 * Clase que define las expresiones a analizar
 *
 * @author Diego Francisco Darias Pino
 */
public class Expresion {

    //Caracteres que se eliminan al parsear
    public static String[] listaNulos = {"[", "]", ","};
    public int posP;
    public ArrayList<INodo> listaOri;
    public ArrayList<Tupla> nodos;

    //Expresion de entrada completa, Raiz del AST
    public Expresion(AST ast) {
        posP = 0;
        listaOri = ast.arbol.getListaH();
        nodos = getNodos(getTokens());
    }

    //Expresion copiada, para mover el punto
    public Expresion(Expresion exp) {
        posP = exp.posP;
        listaOri = exp.listaOri;
        nodos = exp.nodos;
    }

    //Convierte el AST en un array de simbolos
    private String[] getTokens() {
        String simbolos = listaOri.toString();
        //Separa la cadena de simbolos por sus espacios
        String[] array = simbolos.split(" ");
        //Borra los caracteres basura
        for (int t = 0; t < array.length; t++) {
            StringBuilder sb = new StringBuilder(array[t]);
            for (int i = 0; i < sb.length(); i++) {
                String s = String.valueOf(sb.charAt(i));
                if (Arrays.asList(listaNulos).contains(s)) {
                    sb.deleteCharAt(i);
                }
            }
            array[t] = sb.toString();
        }
        System.out.println(Arrays.toString(array));
        return array;
    }

    //Transforma los caracteres en Tupla, una clase que
    //contiene informacion necesaria para el algoritmo de puntos
    private ArrayList<Tupla> getNodos(String[] tokens) {
        ArrayList<Tupla> listaTP = new ArrayList<>();
        for (int i = 0; i < tokens.length; i++) {
            //Guarda el simbolo y la posicion de cada caracter
            String tk = tokens[i];
            Tupla tp = new Tupla(tk, i);
            listaTP.add(tp);
        }
        //Convierte la lista de Tupla en array temporalmente,
        //para realizar operaciones sobre ella
        Tupla[] array = listaTP.toArray(Tupla[]::new);
        //Enlaza los simbolos no terminales
        Tupla.asociarPares(array);
        return listaTP;
    }

    //Necesaria para que, al desplazar el punto, las expresiones resultado sean correctas
    public static void clausuraLambda(Expresion exp, ArrayList<Expresion> listaCanon) {
        //Toma la lista de Tupla de una expresion
        ArrayList<Tupla> listaTP = exp.nodos;
        //Toma la posicion del punto en la expresion
        int p = exp.posP;
        //Si el punto todavia puede avanzar
        if (p < listaTP.size() - 1) {
            //Comprueba si el simbolo delante del punto es terminal
            if (listaTP.get(p).terminal) {
                //Si la expresion no esta repetida, se añade a la lista
                //resultado de la clausura o lista de expresiones canonicas
                if (!listaCanon.contains(exp)) {
                    listaCanon.add(exp);
                }
            } else //Si el simbolo es no terminal 
            {   //Su tipo determina que regla se aplica al avanzar el punto
                switch (listaTP.get(p).sym) {
                    case "*(":
                    case ")*":
                    case ")+":
                    case "?(":
                        //Avanzar una posicion
                        Reglas.R1(exp, listaCanon);
                        //Saltar delante de su simbolo complementario
                        Reglas.R2(exp, listaCanon);
                        break;
                    case "?)":
                        //Saltar delante de su simbolo complementario
                        Reglas.R2(exp, listaCanon);
                        break;
                    case "+(":
                        //Avanzar una posicion
                        Reglas.R1(exp, listaCanon);
                        break;
                    case "|(":
                        //Avanzar una posicion
                        Reglas.R1(exp, listaCanon);
                        //Saltar delante de su simbolo complementario
                        Reglas.R2(exp, listaCanon);
                        break;
                    case ")|":
                        //Avanzar una posicion
                        Reglas.R1(exp, listaCanon);
                        break;
                    default:
                        //No deberia haber otros simbolos no terminales
                        throw new AssertionError();
                }
            }
        } else //Si el punto ya no puede avanzar, es la expresion final 
        {   //Si no esta ya en la lista, se añade
            if (!listaCanon.contains(exp)) {
                listaCanon.add(exp);
            }
        }
    }

    //Comprueba si dos listas de expresiones son iguales
    public static boolean sonIguales(ArrayList<Expresion> listaExp, ArrayList<Expresion> proto) {
        boolean iguales = true;
        //No puede existir un Estado con lista vacia
        if (listaExp.isEmpty()) {
            iguales = false;
        } else {
            //Deben ser de la misma longitud para ser iguales
            if (listaExp.size() != proto.size()) {
                iguales = false;
            } else {
                //Cuenta los elementos de la primera lista que estan
                //presentes en la segunda
                int cont = 0;
                for (int i = 0; i < listaExp.size(); i++) {
                    for (int j = 0; j < proto.size(); j++) {
                        if (listaExp.get(i).equals(proto.get(j))) {
                            cont++;
                        }
                    }
                }
                //Si no hay coincidencias o si las coincidencias son menos
                //que el total de expresiones, no pueden ser iguales
                if (cont == 0 || cont != listaExp.size()) {
                    iguales = false;
                }
            }
        }
        //De lo contrario, son iguales
        return iguales;
    }

    //Pone en la segunda lista las expresiones de la primera que no contenga ya
    public static void vertir(ArrayList<Expresion> listaCanon, ArrayList<Expresion> proto) {
        for (int i = 0; i < listaCanon.size(); i++) {
            Expresion exp = listaCanon.get(i);
            if (!contiene(proto, exp)) {
                proto.add(exp);
            }
        }
    }

    //Comprueba si una expresion ya esta presente en una lista
    public static boolean contiene(ArrayList<Expresion> proto, Expresion exp) {
        boolean encontrado = false;
        int pos = 0;
        while (!encontrado && pos < proto.size()) {
            if (proto.get(pos).equals(exp)) {
                encontrado = true;
            }
            pos++;
        }
        return encontrado;
    }

    //Para que dos expresiones sean consideradas iguales, solo tiene en cuenta
    //la posicion del punto y su lista de nodos excluyendo el punto
    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if (!(o instanceof Expresion)) {
            return false;
        }

        Expresion obj = (Expresion) o;

        if (posP != obj.posP) {
            return false;
        }

        return listaOri.equals(obj.listaOri);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + this.posP;
        hash = 29 * hash + Objects.hashCode(this.listaOri);
        return hash;
    }

    @Override
    public String toString() {
        String output = " ";
        for (int i = 0; i < nodos.size(); i++) {
            Tupla tp = nodos.get(i);
            //Coloca el punto delante del simbolo al que marca
            if (i == posP) {
                output = output + ". ";
            }
            output = output + tp.sym + " ";
        }
        //Coloca el punto al final si no marca a ningun simbolo
        if (posP == nodos.size()) {
            output = output + ". ";
        }
        return output;
    }

}
