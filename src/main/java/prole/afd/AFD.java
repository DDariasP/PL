package prole.afd;

import prole.puntos.Tupla;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Implementa un AFD y los métodos que definen su comportamiento.
 *
 * @author diego
 */
public class AFD {

    public String nombre;
    public Estado inicial;
    public Estado verde;
    public Transicion ultima;
    public ArrayList<Estado> listaS;
    public ArrayList<Transicion> listaT;
    public String[] st;

    /**
     * Constructor.
     *
     * @param afd Autómata resultado del algoritmo de puntos.
     */
    public AFD(ArrayList<Estado> afd) {

        inicial = afd.get(0);
        verde = afd.get(afd.size() - 1);
        listaS = afd;
        listaT = new ArrayList<>();
        for (int i = 0; i < listaS.size(); i++) {
            Estado s = listaS.get(i);
            for (int j = 0; j < s.destinos.length; j++) {
                Tupla tp = s.destinos[j];
                if (tp != null) {
                    listaT.add(new Transicion(s, tp.st, tp.destino));
                }
            }
        }

    }

    /**
     * Constructor de copia. Los métodos que modifican listas lo hacen creando
     * listas nuevas y sustituyendo las referencias, por lo que la copia puede
     * compartir las referencias del original.
     *
     * @param a Autómata original.
     */
    public AFD(AFD a) {
        AFD original = (AFD) a;
        inicial = original.inicial;
        verde = original.inicial;
        listaS = original.listaS;
        listaT = original.listaT;
    }

    /**
     * Comprueba si un estado es final.
     *
     * @param s Nombre del estado a comprobar.
     * @return true si es final o false en caso contrario.
     */
    public boolean esFinal(String s) {
        boolean encontrado = false;
        int pos = 0;
        while (!encontrado && pos < listaS.size()) {
            if (listaS.get(pos).nombre.equals(s)) {
                encontrado = listaS.get(pos).ultimo;
            }
            pos++;
        }
        return encontrado;
    }

    /**
     * Comprueba la cadena pasada e indica si el autómata la reconoce o no.
     *
     * @param tokens Array con la cadena a comprobar.
     * @return true en caso de reconocer la cadena o false en caso contrario.
     */
    public boolean reconocer(String[] tokens) {
        //el primer estado es el inicial
        Estado actual = inicial;
        //analiza cada carácter de la cadena
        for (int i = 0; i < tokens.length; i++) {
            //toma el siguiente carácter
            String s = tokens[i];
            System.out.println(Arrays.toString(tokens));
            //comprueba las transiciones posibles
            int j = 0;
            boolean encontrado = false;
            while (j < listaT.size() && !encontrado) {
                Transicion t = listaT.get(j);
                //cuando el estado transiciona con el carácter que se analiza
                if (t.origen == actual && t.simbolo.equals(s)) {
                    //termina de comprobar las transiciones
                    encontrado = true;
                    //da el siguiente paso
                    actual = t.destino;
                }
                j++;
            }
            //si no hay transiciones posibles, rechaza la cadena
            if (!encontrado) {
                return false;
            }
        }
        //devuelve true si el estado es final
        return actual.ultimo;
    }

    /**
     * Devuelve una representación en formato String de los estados y las
     * transiciones del autómata.
     *
     * @return String con los datos del autómata.
     */
    @Override
    public String toString() {
        return ("AFD  = { estados:" + listaS.toString() + ", transiciones:" + listaT.toString() + " }");
    }

    /**
     * Comprueba paso a paso si el autómata reconoce o no la cadena.
     *
     * @param a AFD donde se da el paso.
     * @param s String con el carácter a comprobar.
     * @return true en caso de reconocer el carácter o false en caso contrario.
     */
    public static boolean paso(AFD a, String s) {
        //analiza el estado actual
        Estado actual = a.verde;
        //el estado del siguiente paso comienza vacío
        Estado siguiente = new Estado("null");
        //comprueba las transiciones posibles
        int i = 0;
        boolean encontrado = false;
        while (i < a.listaT.size() && !encontrado) {
            Transicion t = a.listaT.get(i);
            //cuando el estado transiciona con el carácter que se analiza
            if (t.origen == actual && t.simbolo.equals(s)) {
                //termina de comprobar las transiciones
                encontrado = true;
                //guarda el estado siguiente
                siguiente = t.destino;
                //guarda la transición realizada
                a.ultima = t;
            }
            i++;
        }
        //prepara siguiente paso
        a.verde = siguiente;
        //devuelve false si no hay transición posible
        return encontrado;
    }

}
