package proleg.puntos;

import proleg.ast.*;
import proleg.afd.*;
import java.util.Arrays;

/**
 * Clase para la informacion de cada token
 *
 * @author Diego Francisco Darias Pino
 */
public class Tupla {

    //Representa la lectura de eof
    public static Tupla EOF = new Tupla("eof", -1);
    //Clasifica las dos mitades de los simbolos no terminales
    public static String[] listaL = {"*(", "+(", "?(", "|("};
    public static String[] listaR = {")*", ")+", ")?", ")|"};
    //Tupla a partir de un INodo
    public String sym;
    public boolean terminal;
    public int pos;
    public Tupla par1, par2, parOR;
    public boolean paired;
    //Tupla a partir de un Estado
    public String symT;
    public Estado destino;

    //Tuplas de INodo
    public Tupla(String s, int n) {
        sym = s;
        //Si no es un simbolo Operador, es terminal
        terminal = !Arrays.asList(Operador.lista).contains(sym);
        pos = n;
        par1 = null;
        par2 = null;
        parOR = null;
        paired = false;
    }

    //Tuplas de Estado
    public Tupla(String s, Estado d) {
        symT = s;
        destino = d;
    }

    //Enlaza con punteros las dos mitades de cada simbolo no terminal
    public static void asociarPares(Tupla[] array) {
        Tupla sig = array[0];
        int puntero = 0;
        //Debe recordar la ultima coincidencia por la izquierda al avanzar
        Tupla lastL = null;
        //Y recordar la primera coincidencia por la derecha al retroceder
        Tupla firstR = null;
        //Comienza avanzando
        avanza(array, puntero, lastL, firstR, sig);
    }

    //Lee la Tupla siguiente del vector
    private static void avanza(Tupla[] array, int puntero,
            Tupla lastL, Tupla firstR, Tupla sig) {
        //Si no ha llegado al final
        if (sig != EOF) {
            //Si el siguiente simbolo es una mitad izquierda
            if (Arrays.asList(listaL).contains(sig.sym)) {
                //Lo memoriza
                guardaL(array, puntero, lastL, firstR, sig);
                //Si es una mitad derecha
            } else if (Arrays.asList(listaR).contains(sig.sym)) {
                //Debe parar
                paraR(array, puntero, lastL, firstR, sig);
            } else {
                //Si no, sigue avanzando
                puntero++;
                if (puntero < array.length) {
                    sig = array[puntero];
                } else {
                    //eof si el siguiente simbolo esta fuera del vector
                    sig = EOF;
                }
                avanza(array, puntero, lastL, firstR, sig);
            }
        }
    }

    //Memoriza el simbolo y continua
    private static void guardaL(Tupla[] array, int puntero,
            Tupla lastL, Tupla firstR, Tupla sig) {
        lastL = array[puntero];
        puntero++;
        if (puntero < array.length) {
            sig = array[puntero];
        } else {
            sig = EOF;
        }
        avanza(array, puntero, lastL, firstR, sig);
    }

    //Memoriza el simbolo y comienza a retroceder
    private static void paraR(Tupla[] array, int puntero,
            Tupla lastL, Tupla firstR, Tupla sig) {
        firstR = sig;
        retrocede(array, puntero, lastL, firstR, sig);
        //Cuando no necesita retroceder mas, sigue avanzando
        puntero++;
        if (puntero < array.length) {
            sig = array[puntero];
        } else {
            sig = EOF;
        }
        avanza(array, puntero, lastL, firstR, sig);
    }

    //Comienza a retroceder
    private static void retrocede(Tupla[] array, int puntero,
            Tupla lastL, Tupla firstR, Tupla ante) {
        puntero--;
        ante = array[puntero];
        boolean encontrado = false;
        //Si la expresion es correcta, nunca se saldra del vector
        while (!encontrado) {
            //Retrocede hasta que encuentra otra mitad izquierda
            if (!Arrays.asList(listaL).contains(ante.sym)) {
                puntero--;
                ante = array[puntero];
            } else {
                //Si la mitad encontrada es complementaria a su tipo
                //y no está enlazada con otra
                if (ante.sym.charAt(0) == firstR.sym.charAt(1)
                        && !ante.paired) {
                    //Termina de retroceder
                    encontrado = true;
                    //Si no, sigue retrocediendo
                } else {
                    puntero--;
                    ante = array[puntero];
                }

            }
        }
        //Tras encontrar su mitad complementaria, las enlaza
        //y marca ambas como enlazadas
        firstR.par1 = ante;
        firstR.paired = true;
        ante.par1 = firstR;
        ante.paired = true;
    }

    //Cuando ha enlazado el resto de simbolos, enlaza las partes de los '|'
    public static void asociarOR(Tupla[] array) {
        for (int i = 0; i < array.length; i++) {
            Tupla buscador = array[i];
            //Solo las mitades izquierdas buscan sus partes
            if (buscador.sym.equals("|(")) {
                boolean encontrado = false;
                int pos = i + 1;
                //Recuerda las mitades de su tipo encontradas hasta el momento,
                //sean o no complementarias
                int contL = 0;
                int contR = 0;
                //Mientras no encuentre sus partes y no llegue al final de la expresion
                while (!encontrado && pos < array.length) {
                    Tupla sig = array[pos];
                    switch (sig.sym) {
                        //Suma las mitades izquierdas
                        case "|(":
                            contL++;
                            break;
                        //Suma las mitades derechas
                        case ")|":
                            contR++;
                            break;
                        //Cuando encuentra su parte intermedia
                        case "|":
                            //Si ha encontrado el mismo numero de mitades de
                            //cada tipo, el '|' encontrado es su parte intermedia
                            if (contL == contR) {
                                encontrado = true;
                                //Se enlaza con el '|'
                                buscador.parOR = sig;
                                buscador.par1.parOR = sig;
                                sig.par1 = buscador;
                                //Enlaza al '|' con su mitad complementaria
                                sig.par2 = buscador.par1;
                                sig.paired = true;
                                break;
                            }
                        default:
                    }
                    //Si no, sigue avanzando
                    pos++;
                }
            }
        }
    }

    //Para cada tupla muestra su simbolo y posicion, y el simbolo y posicion
    //de su mitad complementaria
    //Las partes de OR muestran el simbolo y posicion de todas sus partes
    @Override
    public String toString() {
        String output = "['" + sym + "'," + pos + "]";
        if (par1 != null) {
            output = output + " ['" + par1.sym + "'," + par1.pos + "]";
        }
        if (par2 != null) {
            output = output + " ['" + par2.sym + "'," + par2.pos + "]";
        }
        if (parOR != null) {
            output = output + " ['" + parOR.sym + "'," + parOR.pos + "]";
        }
        return output;
    }

}