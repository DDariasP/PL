package proleg.sintactico;

import proleg.lexico.*;
import java.io.File;
import java.io.IOException;

/**
 * Analizador sintactico basado en una gramatica BNF y LL(1)
 *
 * @author Diego Francisco Darias Pino
 *
 */
public class MyParser implements MyConstants {

    /**
     * Analizador lexico
     */
    private MyLexer lexer;

    /**
     * Siguiente token de la cadena de entrada
     */
    private Token nextToken;

    /**
     * Metodo de analisis de un fichero
     *
     * @param file Fichero a analizar
     * @return Resultado del analisis sintactico.
     * @throws java.io.IOException Error en File.
     * @throws proleg.sintactico.SintaxException Error sintáctico.
     */
    public boolean parse(File file) throws IOException, SintaxException {
        this.lexer = new MyLexer(file);
        this.nextToken = lexer.getNextToken();
        //Comienza por el simbolo inicial
        parseF();
        //Si ha llegado a EOF, es correcto
        if (nextToken.getKind() == EOF) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Metodo que consume un token de la cadena de entrada
     *
     * @param kind Tipo de token a consumir
     * @throws SintaxException Si el tipo no coincide con el token
     */
    private void match(int kind) throws SintaxException {
        if (nextToken.getKind() == kind) {
            nextToken = lexer.getNextToken();
        } else {
            throw new SintaxException(nextToken, kind);
        }
    }

    /**
     * Analiza el simbolo inicial <F>
     *
     * @throws SintaxException Error sintáctico.
     */
    private void parseF() throws SintaxException {
        //Comienza por el identificador de la expresion
        int[] expected = {ID};
        switch (nextToken.getKind()) {
            //Parte derecha de la regla
            case ID:
                match(ID);
                match(EQ);
                parseE();
                match(SEMI);
                break;
            default:
                throw new SintaxException(nextToken, expected);
        }
    }

    /**
     * Analiza el simbolo <E>
     *
     * @throws SintaxException Error sintáctico.
     */
    private void parseE() throws SintaxException {
        int[] expected = {SYMBOL, RPAREN, SEMI, LPAREN};
        switch (nextToken.getKind()) {
            //Parte derecha de la regla
            case SYMBOL:
            case RPAREN:
            case SEMI:
            case LPAREN:
                parseD();
                parseAA();
                break;
            default:
                throw new SintaxException(nextToken, expected);
        }
    }

    /**
     * Analiza el simbolo <AA>
     *
     * @throws SintaxException Error sintáctico.
     */
    private void parseAA() throws SintaxException {
        int[] expected = {OR, SEMI, RPAREN};
        switch (nextToken.getKind()) {
            //Parte derecha de la regla
            case OR:
                match(OR);
                parseD();
                parseAA();
                break;
            case SEMI:
            case RPAREN:
                break;
            default:
                throw new SintaxException(nextToken, expected);
        }
    }

    /**
     * Analiza el simbolo <D>
     *
     * @throws SintaxException Error sintáctico.
     */
    private void parseD() throws SintaxException {
        int[] expected = {SYMBOL, LPAREN};
        switch (nextToken.getKind()) {
            //Parte derecha de la regla
            case SYMBOL:
            case LPAREN:
                parseB();
                parseBB();
                break;
            default:
                throw new SintaxException(nextToken, expected);
        }
    }

    /**
     * Analiza el simbolo <BB>
     *
     * @throws SintaxException Error sintáctico.
     */
    private void parseBB() throws SintaxException {
        int[] expected = {SYMBOL, LPAREN, OR, SEMI, RPAREN};
        switch (nextToken.getKind()) {
            //Parte derecha de la regla
            case SYMBOL:
            case LPAREN:
                parseB();
                parseBB();
                break;
            case OR:
            case SEMI:
            case RPAREN:
                break;
            default:
                throw new SintaxException(nextToken, expected);
        }
    }

    /**
     * Analiza el simbolo <B>
     *
     * @throws SintaxException Error sintáctico.
     */
    private void parseB() throws SintaxException {
        int[] expected = {SYMBOL, LPAREN};
        switch (nextToken.getKind()) {
            //Parte derecha de la regla
            case SYMBOL:
                match(SYMBOL);
                break;
            case LPAREN:
                match(LPAREN);
                parseE();
                match(RPAREN);
                parseO();
                break;
            default:
                throw new SintaxException(nextToken, expected);
        }
    }

    /**
     * Analiza el simbolo <O>
     *
     * @throws SintaxException Error sintáctico.
     */
    private void parseO() throws SintaxException {
        int[] expected = {STAR, PLUS, HOOK, SYMBOL, LPAREN, OR, SEMI, RPAREN};
        switch (nextToken.getKind()) {
            //Parte derecha de la regla
            case STAR:
                match(STAR);
                break;
            case PLUS:
                match(PLUS);
                break;
            case HOOK:
                match(HOOK);
                break;
            case SYMBOL:
            case LPAREN:
            case OR:
            case SEMI:
            case RPAREN:
                break;
            default:
                throw new SintaxException(nextToken, expected);
        }
    }
}
