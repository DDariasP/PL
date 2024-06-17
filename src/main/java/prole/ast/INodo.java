package prole.ast;

import java.util.ArrayList;

/**
 * Interfaz que define un nodo generico del arbol.
 *
 * @author Diego Francisco Darias Pino
 */
public interface INodo {

    public int getID();

    public void setID(int n);

    public String getNombre();

    public void setNombre(String s);

    public int getNumHijos();

    public INodo getHijoN(int n);

    public ArrayList<INodo> getListaH();

    public void addHijo(INodo n);

    public boolean esTerminal();

}
