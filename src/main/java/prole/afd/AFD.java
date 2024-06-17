package prole.afd;

import prole.puntos.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Implementa un AFD y los métodos que definen su comportamiento.
 *
 * @author Diego Francisco Darias Pino
 */
public class AFD {

    public String nombre;
    public Estado inicial;
    public Estado verde;
    public Transicion ultimaT;
    public HashMap<String, Estado> tablaS;
    public HashMap<String, Transicion> tablaT;

    /**
     * Constructor.
     *
     * @param nom Nombre del AFD.
     * @param listaS Estados del AFD resultado del algoritmo de puntos.
     */
    public AFD(String nom, ArrayList<Estado> listaS) {
        nombre = nom;
        inicial = listaS.get(0);
        verde = listaS.get(0);
        tablaS = new HashMap<>();
        tablaT = new HashMap<>();
        for (int i = 0; i < listaS.size(); i++) {
            Estado s = listaS.get(i);
            tablaS.put(s.nombre, s);
            for (int j = 0; j < s.destinos.length; j++) {
                if (s.destinos[j] != null) {
                    Tupla tp = s.destinos[j];
                    Transicion t = new Transicion(s, tp.st, tp.destino);
                    tablaT.put(s.nombre + tp.st, t);
                }
            }
        }
    }

    /**
     * Comprueba si un estado es final.
     *
     * @param s Estado a comprobar.
     * @return true si es final o false en caso contrario.
     */
    public boolean esFinal(Estado s) {
        return s.ultimo;
    }

    /**
     * Comprueba paso a paso si el autómata reconoce o no la cadena.
     *
     * @param carac String con el carácter a comprobar.
     * @return true en caso de reconocer el carácter o false en caso contrario.
     */
    public boolean paso(String carac) {
        boolean consumir = false;
        Transicion t = tablaT.get(verde.nombre + carac);
        if (t != null) {
            consumir = true;
            verde = t.destino;
            ultimaT = t;
        }
        return consumir;
    }

}
