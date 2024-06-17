package prole.afd;

/**
 * Tipo de datos para manejar las transiciones del autómata.
 *
 * @author Diego Francisco Darias Pino
 */
public class Transicion {

    public final Estado origen;
    public final String simbolo;
    public final Estado destino;

    /**
     * Constructor.
     *
     * @param a Estado origen.
     * @param b Símbolo que lee para transicionar.
     * @param c Estado destino.
     */
    public Transicion(Estado a, String b, Estado c) {
        origen = a;
        simbolo = b;
        destino = c;
    }

    /**
     * Devuelve una representación en formato String de las transiciones del
     * autómata.
     *
     * @return String con los atributos de la transición.
     */
    @Override
    public String toString() {
        return ("{" + origen.nombre + ",'" + simbolo + "'," + destino.nombre + "}");
    }

}
