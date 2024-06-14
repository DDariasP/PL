package proleg.puntos;

import java.util.ArrayList;

/**
 * Clase que define las reglas de los operadores sobre expresiones regulares en
 * notacion BNF. Todas concluyen haciendo la clausura lambda de su resultado
 *
 * @author Diego Francisco Darias Pino
 */
public class Reglas {

    // .( x ) => ( .x )
    // ( x. ) => ( x ).
    public static void R1(Expresion exp, ArrayList<Expresion> listaCanon) {
        Expresion nuevaE = new Expresion(exp);
        nuevaE.posP++;
        Expresion.clausuraLambda(nuevaE, listaCanon);
    }

    // .( x ) => ( x ).
    // ( x. ) => ( .x )
    public static void R2(Expresion exp, ArrayList<Expresion> listaCanon) {
        Expresion nuevaE = new Expresion(exp);
        ArrayList<Tupla> v = nuevaE.nodos;
        int p = nuevaE.posP;
        Tupla par = v.get(p).par1;
        nuevaE.posP = par.pos + 1;
        Expresion.clausuraLambda(nuevaE, listaCanon);
    }

    // .( x | y ) => ( x | .y )
    public static void R3(Expresion exp, ArrayList<Expresion> listaCanon) {
        Expresion nuevaE = new Expresion(exp);
        ArrayList<Tupla> v = nuevaE.nodos;
        int p = nuevaE.posP;
        Tupla par = v.get(p).parOR;
        nuevaE.posP = par.pos + 1;
        Expresion.clausuraLambda(nuevaE, listaCanon);
    }

    // ( x. | y ) => ( x | y ).
    public static void R4(Expresion exp, ArrayList<Expresion> listaCanon) {
        Expresion nuevaE = new Expresion(exp);
        ArrayList<Tupla> v = nuevaE.nodos;
        int p = nuevaE.posP;
        Tupla par = v.get(p).par2;
        nuevaE.posP = par.pos + 1;
        Expresion.clausuraLambda(nuevaE, listaCanon);
    }

}
