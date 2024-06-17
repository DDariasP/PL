package prole.semantico;

import prole.ast.Base;
import prole.ast.INodo;
import prole.ast.AST;
import prole.ast.Operador;
import prole.sintactico.SintaxException;
import prole.lexico.MyConstants;
import prole.lexico.Token;
import prole.lexico.MyLexer;
import java.io.File;
import java.io.IOException;

/**
 * Analizador semantico basado ETDS descendente
 *
 * @author Diego Francisco Darias Pino
 *
 */
public class MyETDSDesc implements MyConstants {

    /**
     * Analizador lexico
     */
    private MyLexer lexer;

    /**
     * Siguiente token de la cadena de entrada
     */
    private Token nextToken;

    /*
     * Arbol Sintactico Abstracto
     */
    private AST ast;

    /**
     * Metodo de analisis de un fichero
     *
     * @param file Fichero a analizar.
     * @return Resultado del analisis semántico.
     * @throws java.io.IOException Error en File.
     * @throws prole.sintactico.SintaxException Error sintáctico.
     */
    public boolean parse(File file) throws IOException, SintaxException {
        //Crea un arbol vacio
        ast = new AST();
        //La raiz del arbol es el simbolo inicial
        INodo F_h = ast.arbol;
        this.lexer = new MyLexer(file);
        this.nextToken = lexer.getNextToken();
        INodo F_s = parseF(F_h);
        if (nextToken.getKind() == EOF) {
            ast.arbol = F_s;
            ast.print();
            return true;
        } else {
            return false;
        }
    }

    /**
     * Metodo que consume un token de la cadena de entrada.
     *
     * @param kind Tipo de token a consumir.
     * @throws SintaxException Si el tipo no coincide con el token.
     */
    private Token match(int kind) throws SintaxException {
        Token tk = nextToken;
        if (nextToken.getKind() == kind) {
            nextToken = lexer.getNextToken();
        } else {
            throw new SintaxException(nextToken, kind);
        }
        return tk;
    }

    /**
     * Analiza el simbolo <F>
     *
     * @throws SintaxException Error sintáctico.
     */
    private INodo parseF(INodo F_h) throws SintaxException {
        INodo F_s = null;
        int[] expected = {ID};
        switch (nextToken.getKind()) {
            case ID:
                Token tk = match(ID);
                F_h.setID(tk.getKind());
                F_h.setNombre(tk.getLexeme());
                match(EQ);
                INodo E_s = parseE(F_h);
                F_s = E_s;
                match(SEMI);
                break;
            default:
                throw new SintaxException(nextToken, expected);
        }
        return F_s;
    }

    /**
     * Analiza el simbolo <E>
     *
     * @throws SintaxException Error sintáctico.
     */
    private INodo parseE(INodo E_h) throws SintaxException {
        INodo E_s = null;
        int[] expected = {SYMBOL, RPAREN, SEMI, LPAREN};
        switch (nextToken.getKind()) {
            case SYMBOL:
            case RPAREN:
            case SEMI:
            case LPAREN:
                INodo D_s = parseD(E_h);
                INodo AA_s = parseAA(D_s);
                E_s = AA_s;
                break;
            default:
                throw new SintaxException(nextToken, expected);
        }
        return E_s;
    }

    /**
     * Analiza el simbolo <D>
     *
     * @throws SintaxException Error sintáctico.
     */
    private INodo parseD(INodo D_h) throws SintaxException {
        INodo D_s = null;
        int[] expected = {SYMBOL, LPAREN};
        switch (nextToken.getKind()) {
            case SYMBOL:
            case LPAREN:
                INodo B_s = parseB(D_h);
                INodo BB_s = parseBB(B_s);
                D_s = BB_s;
                break;
            default:
                throw new SintaxException(nextToken, expected);
        }
        return D_s;
    }

    /**
     * Analiza el simbolo <B>
     *
     * @throws SintaxException Error sintáctico.
     */
    private INodo parseB(INodo B_h) throws SintaxException {
        INodo B_s = null;
        int[] expected = {SYMBOL, LPAREN};
        switch (nextToken.getKind()) {
            case SYMBOL:
                Token tk1 = match(SYMBOL);
                Base B1_s = new Base();
                B1_s.setID(tk1.getKind());
                B1_s.setNombre(tk1.getLexeme());
                B_h.addHijo(B1_s);
                B_s = B_h;
                break;
            case LPAREN:
                Token tk2 = match(LPAREN);
                Operador B2_s = new Operador();
                B2_s.setID(tk2.getKind());
                B2_s.setNombre(tk2.getLexeme());
                INodo E_s = parseE(B2_s);
                match(RPAREN);
                INodo O_s = parseO(E_s);
                B_h.addHijo(O_s);
                B_s = B_h;
                break;
            default:
                throw new SintaxException(nextToken, expected);
        }
        return B_s;
    }

    /**
     * Analiza el simbolo <BB>
     *
     * @throws SintaxException Error sintáctico.
     */
    private INodo parseBB(INodo BB_h) throws SintaxException {
        INodo BB_s = null;
        int[] expected = {SYMBOL, LPAREN, OR, SEMI, RPAREN};
        switch (nextToken.getKind()) {
            case SYMBOL:
            case LPAREN:
                INodo B_s = parseB(BB_h);
                BB_s = parseBB(B_s);
                break;
            case OR:
            case SEMI:
            case RPAREN:
                BB_s = BB_h;
                break;
            default:
                throw new SintaxException(nextToken, expected);
        }
        return BB_s;
    }

    /**
     * Analiza el simbolo <O>
     *
     * @throws SintaxException Error sintáctico.
     */
    private INodo parseO(INodo O_h) throws SintaxException {
        INodo O_s = null;
        int[] expected = {STAR, PLUS, HOOK, SYMBOL, LPAREN, OR, SEMI, RPAREN};
        switch (nextToken.getKind()) {
            case STAR:
                Token tk1 = match(STAR);
                O_h.setID(tk1.getKind());
                O_h.setNombre(tk1.getLexeme());
                O_s = O_h;
                break;
            case PLUS:
                Token tk2 = match(PLUS);
                O_h.setID(tk2.getKind());
                O_h.setNombre(tk2.getLexeme());
                O_s = O_h;
                break;
            case HOOK:
                Token tk3 = match(HOOK);
                O_h.setID(tk3.getKind());
                O_h.setNombre(tk3.getLexeme());
                O_s = O_h;
                break;
            case OR:
            case SYMBOL:
            case LPAREN:
            case SEMI:
            case RPAREN:
                O_s = O_h;
                break;
            default:
                throw new SintaxException(nextToken, expected);
        }
        return O_s;
    }

    /**
     * Analiza el simbolo <AA>
     *
     * @throws SintaxException Error sintáctico.
     */
    private INodo parseAA(INodo AA_h) throws SintaxException {
        INodo AA_s = null;
        int[] expected = {OR, SEMI, RPAREN};
        switch (nextToken.getKind()) {
            case OR:
                Token tk1 = match(OR);
                Operador AA1_s = new Operador();
                AA1_s.setID(tk1.getKind());
                AA1_s.setNombre(tk1.getLexeme());
                AA_h.addHijo(AA1_s);
                INodo D_s = parseD(AA_h);
                INodo AA2_s = parseAA(D_s);
                AA_s = AA2_s;
                break;
            case SEMI:
            case RPAREN:
                AA_s = AA_h;
                break;
            default:
                throw new SintaxException(nextToken, expected);
        }
        return AA_s;
    }

    /**
     * Metodo para consultar el arbol.
     *
     * @return arbol AST.
     */
    public AST getAST() {
        return ast;
    }

}
