package prole.puntos;

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
        Tupla parA = v.get(p).parA;
        nuevaE.posP = parA.pos + 1;
        Expresion.clausuraLambda(nuevaE, listaCanon);
    }

    // .|( x | y | z )| => |( x | .y | z )|
    //                     |( x | y | .z )|
    public static void R3(Expresion exp, ArrayList<Expresion> listaCanon) {
        Tupla tp = exp.nodos.get(exp.posP);
        ArrayList<Tupla> paresOR = tp.paresOR;
        for (int i = 0; i < paresOR.size(); i++) {
            Expresion nuevaE = new Expresion(exp);
            nuevaE.posP = paresOR.get(i).pos + 1;
            Expresion.clausuraLambda(nuevaE, listaCanon);
        }
    }

    // |( x. | y | z )| => |( x | y | z )|.
    // |( x | y. | z )| => |( x | y | z )|.
    public static void R4(Expresion exp, ArrayList<Expresion> listaCanon) {
        Expresion nuevaE = new Expresion(exp);
        Tupla tp = exp.nodos.get(exp.posP);
        nuevaE.posP = tp.parB.pos + 1;
        Expresion.clausuraLambda(nuevaE, listaCanon);
    }

}
