
import compilerTools.Token;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author Equipo "Los 7 Fantásticos"
 */
public class For {

    private ArrayList<Token> tokens;
    private int cont;
    private DefaultTableModel modFor;

    public For() {
        this.tokens = Compilador.tokens;
        this.modFor = (DefaultTableModel) Automata.tblFor.getModel();
        modFor.setRowCount(0);
    }

    private boolean validarToken(String token, String lex) {
        Token t = tokens.get(cont++);
        if (t.getLexicalComp().equals(token)) {
            return true;
        }

        Compilador.errors.add("ERROR SINTACTICO: Se esperaba (" + lex + ") en la posicion [" + t.getLine() + ", " + t.getColumn() + "]");
        return false;
    }

    private boolean hayTokens() {
        return tokens.size() > cont;
    }

    /**
     * E1 del automata
     *
     * @param contador contador actual para el arreglo de tokens
     * @return posicion del ultimo token analizado
     */
    public int For(int contador) {
        this.cont = contador;
        if (!hayTokens()) {
            return -1;
        }

        if (validarToken("Para_Esto", "for")) {
            modFor.addRow(new Object[]{tokens.get(cont - 1).getLexeme(), "SE ACEPTA Y AVANZA HACIA EL ESTADO 2"});
            return E2(true);
        } else {
            modFor.addRow(new Object[]{tokens.get(cont - 1).getLexeme(), "SE RECHAZA Y AVANZA HACIA EL ESTADO 2"});
            return E2(false);
        }
    }

    private int E2(boolean aceptado) {
        if (!hayTokens()) {
            return -1;
        }

        if (validarToken("Parentesis_Apertura", "(")) {
            modFor.addRow(new Object[]{tokens.get(cont - 1).getLexeme(), "SE ACEPTA Y AVANZA HACIA EL ESTADO 3"});
            return E3(aceptado);
        } else {
            modFor.addRow(new Object[]{tokens.get(cont - 1).getLexeme(), "SE RECHAZA Y AVANZA HACIA EL ESTADO 3"});
            cont++;
            return E3(false);
        }
    }

    //gramatica en automata
    private int E3(boolean aceptado) {
        if (!hayTokens()) {
            return -1;
        }

        //mini gramatica de valor
        String regla = "";
        if (tokens.get(cont).getLexicalComp().equals("Número") && tokens.get(cont + 1).getLexicalComp().equals("Dos_Puntos")) {
            regla = "VALOR ::= HORA";
            Compilador.gramaticas.add(regla);
            regla = "HORA ::= Número Dos_Puntos";
            cont += 2;
            if (tokens.get(cont).getLexicalComp().equals("Número")) {
                regla += "Número";
                Compilador.gramaticas.add(regla);
                cont++;
                modFor.addRow(new Object[]{tokens.get(cont - 3).getLexeme() + tokens.get(cont - 2).getLexeme() + tokens.get(cont - 1).getLexeme(), "SE ACEPTA Y AVANZA HACIA EL ESTADO 4"});
                return E4(aceptado);
            } else {
                regla += "ERROR ";
                Compilador.gramaticas.add(regla);
                Token t = tokens.get(cont);
                Compilador.errors.add("ERROR SINTACTICO: Se esperaba ( Número ) en la posicion [" + t.getLine() + ", " + t.getColumn() + "]");
                cont++;
                modFor.addRow(new Object[]{tokens.get(cont - 3).getLexeme() + tokens.get(cont - 2).getLexeme() + tokens.get(cont - 1).getLexeme(), "SE RECHAZA Y AVANZA HACIA EL ESTADO 4"});
                return E4(false);
            }
        } else if (tokens.get(cont).getLexicalComp().equals("Número")) {
            regla = "VALOR ::= Número";
            Compilador.gramaticas.add(regla);
            cont++;
            modFor.addRow(new Object[]{tokens.get(cont - 1).getLexeme(), "SE ACEPTA Y AVANZA HACIA EL ESTADO 4"});
            return E4(aceptado);
        } else {
            Compilador.gramaticas.add("VALOR ::= ERROR");
            Token t = tokens.get(cont);
            Compilador.errors.add("ERROR SINTACTICO: Se esperaba ( Número ) en la posicion [" + t.getLine() + ", " + t.getColumn() + "]");
            cont++;
            modFor.addRow(new Object[]{tokens.get(cont - 1).getLexeme(), "SE RECHAZA Y AVANZA HACIA EL ESTADO 4"});
            return E4(false);
        }
    }

    private int E4(boolean aceptado) {
        if (!hayTokens()) {
            return -1;
        }

        if (validarToken("Parentesis_Cierre", ")")) {
            modFor.addRow(new Object[]{tokens.get(cont - 1).getLexeme(), "SE ACEPTA Y AVANZA HACIA EL ESTADO 5"});
            return E5(aceptado);
        } else {
            modFor.addRow(new Object[]{tokens.get(cont - 1).getLexeme(), "SE RECHAZA Y AVANZA HACIA EL ESTADO 5"});
            return E5(false);
        }
    }

    private int E5(boolean aceptado) {
        if (!hayTokens()) {
            return -1;
        }

        if (validarToken("Llave_Apertura", "{")) {
            modFor.addRow(new Object[]{tokens.get(cont - 1).getLexeme(), "SE ACEPTA Y AVANZA HACIA EL ESTADO 6"});
            return E6(aceptado);
        } else {
            modFor.addRow(new Object[]{tokens.get(cont - 1).getLexeme(), "SE RECHAZA Y AVANZA HACIA EL ESTADO 6"});
            return E6(false);
        }
    }

    //ANALIZAR EL RESTO DE GRAMATICAS
    private int E6(boolean aceptado) {
        if (!hayTokens()) {
            return -1;
        }
        Gramatica g = new Gramatica(this);
        while (!tokens.get(cont).getLexicalComp().equalsIgnoreCase("Llave_Cierre")) {
            if (tokens.get(cont).getLexicalComp().equals("Para_Esto")) {
                cont = For(cont);
            } else {
                cont = g.padre(cont);
            }
        }

        modFor.addRow(new Object[]{tokens.get(cont).getLexeme(), aceptado ? "SE ACEPTA LA ESTRUCTURA DEL FOR" : "SE RECHAZA LA ESTRUCTURA DEL FOR"});
        return ++cont;
    }
}
