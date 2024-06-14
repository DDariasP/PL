package proleg.lexico;

import java.io.*;

/**
 * Clase que desarrolla mi analizador lexico
 *
 * @author Diego Francisco Darias Pino
 *
 */
public class MyLexer extends Lexer implements MyConstants {

    /**
     * Transiciones del automata del analizador lexico
     *
     * @param state Estado inicial
     * @param symbol Simbolo del alfabeto
     * @return Estado final
     */
    @Override
    protected int transition(int state, char symbol) {
        switch (state) {
            case 0:
                if (symbol == ' ' || symbol == '\t') {
                    return 1;
                } else if (symbol == '\r' || symbol == '\n') {
                    return 1;
                } else if (symbol == '/') {
                    return 2;
                } else if (symbol >= 'a' && symbol <= 'z') {
                    return 6;
                } else if (symbol >= 'A' && symbol <= 'Z') {
                    return 6;
                } else if (symbol == '_') {
                    return 6;
                } else if (symbol == ':') {
                    return 7;
                } else if (symbol == '|') {
                    return 10;
                } else if (symbol == ';') {
                    return 11;
                } else if (symbol == '(') {
                    return 12;
                } else if (symbol == ')') {
                    return 13;
                } else if (symbol == '*') {
                    return 14;
                } else if (symbol == '+') {
                    return 15;
                } else if (symbol == '?') {
                    return 16;
                } else if (symbol == '\'') {
                    return 17;
                } else {
                    return -1;
                }
            case 1:
                if (symbol == ' ' || symbol == '\t') {
                    return 1;
                } else if (symbol == '\r' || symbol == '\n') {
                    return 1;
                } else {
                    return -1;
                }
            case 2:
                if (symbol == '*') {
                    return 3;
                } else {
                    return -1;
                }
            case 3:
                if (symbol == '*') {
                    return 4;
                } else {
                    return 3;
                }
            case 4:
                if (symbol == '*') {
                    return 4;
                } else if (symbol == '/') {
                    return 5;
                } else {
                    return 3;
                }
            case 6:
                if (symbol >= 'a' && symbol <= 'z') {
                    return 6;
                } else if (symbol >= 'A' && symbol <= 'Z') {
                    return 6;
                } else if (symbol >= '0' && symbol <= '9') {
                    return 6;
                } else if (symbol == '_') {
                    return 6;
                } else {
                    return -1;
                }
            case 7:
                if (symbol == ':') {
                    return 8;
                } else {
                    return -1;
                }
            case 8:
                if (symbol == '=') {
                    return 9;
                } else {
                    return -1;
                }
            case 17:
                if (symbol == '\\') {
                    return 18;
                } else if (symbol != '\'' && symbol != '\n' && symbol != '\r') {
                    return 19;
                } else {
                    return -1;
                }
            case 18:
                if (symbol == 'n' || symbol == 't' || symbol == 'b') {
                    return 19;
                } else if (symbol == 'r' || symbol == 'f' || symbol == '\\') {
                    return 19;
                } else if (symbol == '\'' || symbol == '\"') {
                    return 19;
                } else {
                    return -1;
                }
            case 19:
                if (symbol == '\'') {
                    return 20;
                } else {
                    return -1;
                }
            default:
                return -1;
        }
    }

    /**
     * Verifica si un estado es final
     *
     * @param state Estado
     * @return true, si el estado es final
     */
    @Override
    protected boolean isFinal(int state) {
        if (state <= 0 || state > 20) {
            return false;
        }
        switch (state) {
            case 2:
            case 3:
            case 4:
            case 7:
            case 8:
            case 17:
            case 18:
            case 19:
                return false;
            default:
                return true;
        }
    }

    /**
     * Genera el componente lexico correspondiente al estado final y al lexema
     * encontrado. Devuelve null si la accion asociada al estado final es omitir
     * (SKIP).
     *
     * @param state Estado final alcanzado
     * @param lexeme Lexema reconocido
     * @param row Fila de comienzo del lexema
     * @param column Columna de comienzo del lexema
     * @return Componente lexico correspondiente al estado final y al lexema
     */
    @Override
    protected Token getToken(int state, String lexeme, int row, int column) {
        switch (state) {
            case 1:
                return null;
            case 5:
                return null;
            case 6:
                return new Token(ID, lexeme, row, column);
            case 9:
                return new Token(EQ, lexeme, row, column);
            case 10:
                return new Token(OR, lexeme, row, column);
            case 11:
                return new Token(SEMI, lexeme, row, column);
            case 12:
                return new Token(LPAREN, lexeme, row, column);
            case 13:
                return new Token(RPAREN, lexeme, row, column);
            case 14:
                return new Token(STAR, lexeme, row, column);
            case 15:
                return new Token(PLUS, lexeme, row, column);
            case 16:
                return new Token(HOOK, lexeme, row, column);
            case 20:
                return new Token(SYMBOL, lexeme, row, column);
            default:
                return null;
        }
    }

    /**
     * Constructor
     *
     * @param file Nombre del fichero fuente
     * @throws IOException En caso de problemas con el flujo de entrada
     */
    public MyLexer(File file) throws IOException {
        super(file);
    }

}
