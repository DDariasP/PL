package proleg.afd;

import java.util.ArrayList;
import proleg.puntos.Expresion;
import proleg.puntos.Tupla;

/**
 * Clase que define los estados del AFD
 *
 * @author Diego Francisco Darias Pino
 */
public class Estado {

    public static int NUM = 1;
    public String nombre;
    public ArrayList<Expresion> listaExp;
    public Tupla[] destinos;
    public boolean ultimo;

    //Estado vacio
    public Estado(String str) {
        nombre = str;
        listaExp = new ArrayList<>();
        listaExp = new ArrayList<>();
        ultimo = false;
    }

    //Estado inicial
    public Estado(Expresion exp0) {
        nombre = "s0";
        listaExp = new ArrayList<>();
        listaExp.add(exp0);
        ultimo = false;
    }

    //Estado generico
    public Estado(ArrayList<Expresion> proto) {
        nombre = "s" + NUM;
        NUM++;
        listaExp = proto;
        ultimo = false;
    }

    //Comprueba que no exista un Estado igual al proto-Estado en la lista
    public static int contiene(ArrayList<Estado> listaAFD, ArrayList<Expresion> proto) {
        int contiene = -1;
        int pos = 0;
        while (contiene == -1 && pos < listaAFD.size()) {
            Estado s = listaAFD.get(pos);
            if (Expresion.sonIguales(s.listaExp, proto)) {
                contiene = pos;
            }
            pos++;
        }
        return contiene;
    }

    //Muestra la lista de Expresion, los simbolos de transicion y 
    //y el Estado destino de cada uno
    @Override
    public String toString() {
        String output = "\n" + nombre + ":\n";
        for (int i = 0; i < destinos.length; i++) {
            Tupla tp = destinos[i];
            if (tp != null) {
                output = output + tp.symT + " => " + tp.destino.nombre + "\n";
            }
        }
        for (int i = 0; i < listaExp.size(); i++) {
            output = output + "\t" + listaExp.get(i) + "\n";
        }
        return output;
    }

}
