package prole.lexico;

/**
 * Interfaz que define los codigos de las diferentes categorias lexicas.
 *
 * * @author Diego Francisco Darias Pino
 *
 */
public interface MyConstants {

    /**
     * Final de fichero
     */
    public int EOF = 0;

    /**
     * Blanco ( " " | "\r" | "\n" | "\t" )
     */
    public int BLANK = 1;

    /**
     * Comentario "/*" ( ("*")* ~["*", "/"] | "/" )* ("*")+ "/"
     */
    public int COMM = 2;

    /**
     * Identificador ["_","a"-"z","A"-"z"] ( ["_","a"-"z","A"-"Z","0"-"9"] )*
     */
    public int ID = 3;

    /**
     * Literales " ' " ( ~[" ' ","\\","\n","\r"]) | ("\\"
     * ["n","t","b","r","f","\\","'","\""] ) " ' "
     */
    public int SYMBOL = 4;

    /**
     * Asignacion "::="
     */
    public int EQ = 5;

    /**
     * Alternativa "|"
     */
    public int OR = 6;

    /**
     * Punto y coma ";"
     */
    public int SEMI = 7;

    /**
     * Parentesis abierto "("
     */
    public int LPAREN = 8;

    /**
     * Parentesis cerrado ")"
     */
    public int RPAREN = 9;

    /**
     * Clausura Kleen "*"
     */
    public int STAR = 10;

    /**
     * Clausura positiva "+"
     */
    public int PLUS = 11;

    /**
     * Opcionalidad "?"
     */
    public int HOOK = 12;

}
