package proleg.puntos;

import proleg.afd.*;
import proleg.ast.*;
import proleg.lexico.*;
import java.util.ArrayList;

/**
 * Algoritmo de transformación de expresiones regulares en AFD
 *
 * @author Diego Francisco Darias Pino
 *
 */
public class MyPuntos implements MyConstants {

    //Genera el Estado inicial y lo pasa al algoritmo
    public static AFD genAFD(AST ast) {
        //Expresion inicial
        Expresion exp0 = new Expresion(ast);
        //Estado inicial
        Estado s0 = new Estado(exp0);
        //Lista de Estado del AFD
        ArrayList<Estado> listaAFD = new ArrayList<>();
        //Para buscar todos los simbolos no terminales
        //usa la expresion inicial
        ArrayList<String> listaST = new ArrayList<>();
        for (int i = 0; i < exp0.nodos.size(); i++) {
            Tupla tp = exp0.nodos.get(i);
            if (tp.terminal && !listaST.contains(tp.sym)) {
                String sym = exp0.nodos.get(i).sym;
                listaST.add(sym);
            }
        }
        //Hace la clausura lambda del Estado inicial
        ArrayList<Expresion> listaCanon = new ArrayList<>();
        ArrayList<Expresion> listaExp = s0.listaExp;
        for (int i = 0; i < listaExp.size(); i++) {
            Expresion exp = listaExp.get(i);
            Expresion.clausuraLambda(exp, listaCanon);
        }
        //Actualiza la lista de Expresion del Estado inicial
        s0.listaExp = listaCanon;
        //Mantiene un orden FIFO en el analisis de Estado
        ArrayList<Estado> pilaS = new ArrayList<>();
        //Añade el Estado inicial a la pila
        pilaS.add(s0);
        //Añade el Estado inicial a la lista del AFD
        listaAFD.add(s0);
        //Avanza el punto
        transicion(listaST, pilaS, listaAFD);
        //Muestra el resultado
        for (int i = 0; i < listaAFD.size(); i++) {
            System.out.println(listaAFD.get(i));
        }
        //Devuelve el AFD
        AFD afd = new AFD(listaAFD);
        afd.nombre = ast.arbol.getNombre();
        afd.st = listaST.toArray(String[]::new);
        for (int i = 0; i < afd.listaS.size(); i++) {
            Estado s = afd.listaS.get(i);
            Expresion exp = s.listaExp.get(0);
            if (s.listaExp.size() == 1
                    && exp.posP == exp.nodos.size()) {
                s.ultimo = true;
            }
        }
        return afd;
    }

    //Algoritmo de avanzar el punto
    private static void transicion(ArrayList<String> listaST, ArrayList<Estado> pilaS, ArrayList<Estado> listaAFD) {
        //Mientras haya Estado en la pila
        while (!pilaS.isEmpty()) {
            //Toma el Estado cima
            Estado cima = pilaS.get(0);
            pilaS.remove(0);
            //Inicializa el vector destinos del Estado cima
            cima.destinos = new Tupla[listaST.size()];
            //Para cada simbolo terminal
            for (int numSym = 0; numSym < listaST.size(); numSym++) {
                String symST = listaST.get(numSym);
                //Crea un proto-Estado vacio
                ArrayList<Expresion> proto = new ArrayList<>();
                //Para cada Expresion del Estado cima
                for (int i = 0; i < cima.listaExp.size(); i++) {
                    Expresion exp = cima.listaExp.get(i);
                    //Si el punto no esta al final
                    if (exp.posP < exp.nodos.size()) {
                        //Comprueba el simbolo delante del punto
                        Tupla symP = exp.nodos.get(exp.posP);
                        //Si es terminal y es el simbolo de transicion analizado
                        if (symP.terminal && symP.sym.equals(symST)) {
                            //Avanza el punto
                            Expresion nuevaE = new Expresion(exp);
                            nuevaE.posP++;
                            //Hace la clausura lambda de la nueva Expresion
                            ArrayList<Expresion> listaCanon = new ArrayList<>();
                            Expresion.clausuraLambda(nuevaE, listaCanon);
                            //Guarda las Expresion no repetidas en el proto-Estado
                            Expresion.vertir(listaCanon, proto);
                        }
                    }
                    //Si esta al final, su proto-Estado queda vacio (no transiciona)
                }
                //Cuando ha comprobado todas las Expresion para un simbolo
                //comprueba si el proto-Estado resultado esta vacio
                if (!proto.isEmpty()) {
                    //Comprueba si existe un Estado igual al proto-Estado resultante
                    //en la lista del AFD
                    int pos = Estado.contiene(listaAFD, proto);
                    if (pos != -1) {
                        //Anota el Estado existente como destino
                        //del Estado cima para el simbolo analizado
                        Estado exiS = listaAFD.get(pos);
                        cima.destinos[numSym] = new Tupla(symST, exiS);

                    } else {
                        //Si no, crea un nuevo Estado a partir del proto-Estado
                        Estado sigS = new Estado(proto);
                        //Lo guarda en la pila y en la lista del AFD
                        pilaS.add(sigS);
                        listaAFD.add(sigS);
                        //Anota el Estado siguiente como destino
                        //del Estado cima para el simbolo analizado
                        cima.destinos[numSym] = new Tupla(symST, sigS);
                    }
                }
            }
        }
    }

}
