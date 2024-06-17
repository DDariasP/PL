package prole.afd;

import prole.puntos.*;
import java.util.ArrayList;

/**
 * Define los estados del AFD.
 *
 * @author Diego Francisco Darias Pino
 */
public class Estado {

    public String nombre;
    public ArrayList<Expresion> listaExp;
    public Tupla[] destinos;
    public boolean ultimo;

    //Estado vacio
    public Estado() {
        nombre = "";
        listaExp = new ArrayList<>();
        destinos = new Tupla[0];
        ultimo = false;
    }

    //Estado inicial
    public Estado(Expresion exp0) {
        nombre = "0";
        listaExp = new ArrayList<>();
        listaExp.add(exp0);
        destinos = new Tupla[0];
        ultimo = false;
    }

    //Estado generico
    public Estado(ArrayList<Expresion> proto) {
        nombre = "";
        listaExp = proto;
        destinos = new Tupla[0];
        ultimo = false;
    }

    //Comprueba que no exista un Estado igual al proto-Estado en la del AFD
    public static int contiene(ArrayList<Estado> listaAFD, ArrayList<Expresion> proto) {
        int contiene = -1;
        int pos = 0;
        while (contiene == -1 && pos < listaAFD.size()) {
            Estado s = listaAFD.get(pos);
            //Si el Estado y el proto-Estado tienen la misma lista de Expresion
            if (Expresion.sonIguales(s.listaExp, proto)) {
                //Son iguales
                contiene = pos;
            }
            pos++;
        }
        //Devuelve la posicion del Estado coincidente si lo hay
        return contiene;
    }

    //Muestra la lista de Expresion, los simbolos de transicion y 
    //y el Estado destino de cada uno
    @Override
    public String toString() {
        String output = "\nEstado " + nombre + ":\n";
        for (int i = 0; i < destinos.length; i++) {
            Tupla tp = destinos[i];
            if (tp != null) {
                output = output + tp.st + " => " + tp.destino.nombre + "\n";
            }
        }
        for (int i = 0; i < listaExp.size(); i++) {
            output = output + "\t" + listaExp.get(i).mostrar() + "\n";
        }
        return output;
    }

}
