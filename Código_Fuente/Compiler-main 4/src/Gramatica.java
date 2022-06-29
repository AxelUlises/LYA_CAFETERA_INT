
import compilerTools.Token;
import java.util.ArrayList;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author Equipo "Los 7 Fantásticos"
 */
public class Gramatica {

    private ArrayList<Token> tokens;
    private int cont;
    private For f;

    public Gramatica(For f) {
        this.tokens = Compilador.tokens;
        this.f = f;
    }

    public int padre(int contador) {
        cont = contador;
        if (tokens.size() <= cont) {
            Compilador.errors.add("ERROR GENERAL: SE HAN TERMINADO LOS TOKENS PARA ANALIZAR");
            return -1;
        }

        switch (tokens.get(cont++).getLexicalComp()) {
            case "TIPO_DATO":
                return declaracion();
            case "FUNCION":
                return funciones();
            case "Estructura_IF":
                return esIf();
            case "Estructura_Mientras":
                return mientras();
            default:
                Token t = tokens.get(cont - 1);
                Compilador.errors.add("ERROR SINTACTICO: Se esperaba ( tipo_dato | funcion | Estructura_IF | Estructura_Mientras | Para_Esto ) en la posicion [" + t.getLine() + ", " + t.getColumn() + "]");
                return cont;
        }
    }

    private int declaracion() {
        String regla = "DECLARACION ::= tipo_dato ";
        if (tokens.get(cont++).getLexicalComp().equalsIgnoreCase("identificador")) {
            regla += "identificador ";
        } else {
            Token t = tokens.get(cont - 1);
            regla += "ERROR ";
            Compilador.errors.add("ERROR SINTACTICO: Se esperaba ( identificador ) en la posicion [" + t.getLine() + ", " + t.getColumn() + "]");
        }

        if (tokens.get(cont++).getLexicalComp().equalsIgnoreCase("OP_asignacion")) {
            regla += "OP_asignacion ";
        } else {
            Token t = tokens.get(cont - 1);
            regla += "ERROR ";
            Compilador.errors.add("ERROR SINTACTICO: Se esperaba ( OP_asignacion ) en la posicion [" + t.getLine() + ", " + t.getColumn() + "]");
        }
        regla += "VALOR ";
        cont = valor();

        if (tokens.get(cont++).getLexicalComp().equalsIgnoreCase("Punto_Coma")) {
            regla += "Punto_Coma ";
        } else {
            Token t = tokens.get(cont - 1);
            regla += "ERROR ";
            Compilador.errors.add("ERROR SINTACTICO: Se esperaba ( Punto_Coma ) en la posicion [" + t.getLine() + ", " + t.getColumn() + "]");
        }

        Compilador.gramaticas.add(regla);
        return cont;
    }

    private int funciones() {
        String regla = "FUNCION::= funcion ";
        if (tokens.get(cont++).getLexicalComp().equalsIgnoreCase("Parentesis_Apertura")) {
            regla += "Parentesis_Apertura ";
        } else {
            Token t = tokens.get(cont - 1);
            regla += "ERROR ";
            Compilador.errors.add("ERROR SINTACTICO: Se esperaba ( Parentesis_Apertura ) en la posicion [" + t.getLine() + ", " + t.getColumn() + "]");
        }

        switch (tokens.get(cont).getLexicalComp()) {
            case "P_Reservada":
                regla += "P_Reservada ";
                cont++;
                break;
            case "Número":
                regla += "VALOR ";
                cont = valor();
                break;
            default:
                Token t = tokens.get(cont);
                regla += "ERROR ";
                Compilador.errors.add("ERROR SINTACTICO: Se esperaba ( P_Reservada | TIPO_DATO ) en la posicion [" + t.getLine() + ", " + t.getColumn() + "]");
                break;
        }

        while (tokens.get(cont++).getLexicalComp().equalsIgnoreCase("COMA")) {
            switch (tokens.get(cont).getLexicalComp()) {
                case "P_Reservada":
                    regla += "coma P_Reservada ";
                    cont++;
                    break;
                case "Número":
                    regla += "coma VALOR ";
                    cont = valor();
                    break;
                default:
                    Token t = tokens.get(cont);
                    regla += "coma ERROR ";
                    Compilador.errors.add("ERROR SINTACTICO: Se esperaba ( P_Reservada | TIPO_DATO ) en la posicion [" + t.getLine() + ", " + t.getColumn() + "]");
                    break;
            }
        }

        if (tokens.get(cont - 1).getLexicalComp().equalsIgnoreCase("Parentesis_Cierre")) {
            regla += "Parentesis_Cierre ";
        } else {
            Token t = tokens.get(cont - 1);
            regla += "ERROR ";
            Compilador.errors.add("ERROR SINTACTICO: Se esperaba ( Parentesis_Cierre ) en la posicion [" + t.getLine() + ", " + t.getColumn() + "]");
        }

        if (tokens.get(cont++).getLexicalComp().equalsIgnoreCase("Punto_Coma")) {
            regla += "Punto_Coma ";
        } else {
            Token t = tokens.get(cont - 1);
            regla += "ERROR ";
            Compilador.errors.add("ERROR SINTACTICO: Se esperaba ( Punto_Coma ) en la posicion [" + t.getLine() + ", " + t.getColumn() + "]");
        }

        Compilador.gramaticas.add(regla);
        return cont;
    }

    private int valor() {
        String regla = "";
        if (tokens.get(cont).getLexicalComp().equals("Número") && tokens.get(cont + 1).getLexicalComp().equals("Dos_Puntos")) {
            regla = "VALOR ::= HORA";
            Compilador.gramaticas.add(regla);
            regla = "HORA ::= Número Dos_Puntos";
            cont += 2;
            if (tokens.get(cont).getLexicalComp().equals("Número")) {
                regla += "Número";
                Compilador.gramaticas.add(regla);
                return ++cont;
            } else {
                regla += "ERROR";
                Compilador.gramaticas.add(regla);
                Token t = tokens.get(cont);
                Compilador.errors.add("ERROR SINTACTICO: Se esperaba ( Número ) en la posicion [" + t.getLine() + ", " + t.getColumn() + "]");
                return ++cont;
            }
        } else if (tokens.get(cont).getLexicalComp().equals("Número")) {
            regla = "VALOR ::= Número";
            Compilador.gramaticas.add(regla);
            return ++cont;
        } else {
            Compilador.gramaticas.add("VALOR ::= ERROR");
            Token t = tokens.get(cont);
            Compilador.errors.add("ERROR SINTACTICO: Se esperaba ( Número ) en la posicion [" + t.getLine() + ", " + t.getColumn() + "]");
            return ++cont;
        }
    }

    private int sentencia() {
        String regla = "SENTENCIA::= ";
        if (tokens.get(cont++).getLexicalComp().equalsIgnoreCase("identificador")) {
            regla += "identificador ";
        } else {
            Token t = tokens.get(cont - 1);
            regla += "ERROR ";
            Compilador.errors.add("ERROR SINTACTICO: Se esperaba ( identificador ) en la posicion [" + t.getLine() + ", " + t.getColumn() + "]");
        }
        if (tokens.get(cont++).getLexicalComp().equalsIgnoreCase("OP_RELACIONAL")) {
            regla += "OP_RELACIONAL ";
        } else {
            Token t = tokens.get(cont - 1);
            regla += "ERROR ";
            Compilador.errors.add("ERROR SINTACTICO: Se esperaba ( OP_RELACIONAL ) en la posicion [" + t.getLine() + ", " + t.getColumn() + "]");
        }

        regla += "VALOR";
        Compilador.gramaticas.add(regla);
        return valor();
    }

    private int esIf() {
        String regla = "ESTRUCTURA_IF::= estructura_if ";
        if (tokens.get(cont++).getLexicalComp().equalsIgnoreCase("Parentesis_Apertura")) {
            regla += "Parentesis_Apertura ";
        } else {
            Token t = tokens.get(cont - 1);
            regla += "ERROR ";
            Compilador.errors.add("ERROR SINTACTICO: Se esperaba ( Parentesis_Apertura ) en la posicion [" + t.getLine() + ", " + t.getColumn() + "]");
        }

        regla += "SENTENCIA ";
        cont = sentencia();

        if (tokens.get(cont++).getLexicalComp().equalsIgnoreCase("Parentesis_Cierre")) {
            regla += "Parentesis_Cierre ";
        } else {
            Token t = tokens.get(cont - 1);
            regla += "ERROR ";
            Compilador.errors.add("ERROR SINTACTICO: Se esperaba ( Parentesis_Cierre ) en la posicion [" + t.getLine() + ", " + t.getColumn() + "]");
        }

        if (tokens.get(cont++).getLexicalComp().equalsIgnoreCase("Llave_Apertura")) {
            regla += "Llave_Apertura ";
        } else {
            Token t = tokens.get(cont - 1);
            regla += "ERROR ";
            Compilador.errors.add("ERROR SINTACTICO: Se esperaba ( Llave_Apertura ) en la posicion [" + t.getLine() + ", " + t.getColumn() + "]");
        }

        regla += "GRAMATICAS ";
        switch (tokens.get(cont).getLexicalComp()) {
            case "Para_Esto":
                cont = f.For(cont);
                break;
            default:
                cont = padre(cont);
                break;
        }
        while (!tokens.get(cont).getLexicalComp().equalsIgnoreCase("Llave_Cierre") && cont != -1) {
            if (tokens.get(cont).getLexicalComp().equals("Para_Esto")) {
                cont = f.For(cont);
            } else {
                cont = padre(cont);
            }
        }

        if (tokens.get(cont++).getLexicalComp().equalsIgnoreCase("Llave_Cierre")) {
            regla += "Llave_Cierre ";
        } else {
            Token t = tokens.get(cont - 1);
            regla += "ERROR ";
            Compilador.errors.add("ERROR SINTACTICO: Se esperaba ( Llave_Cierre ) en la posicion [" + t.getLine() + ", " + t.getColumn() + "]");
        }

        Compilador.gramaticas.add(regla);
        return cont;
    }

    private int mientras() {
        String regla = "ESTRUCTURA_MIENTRAS::= estructura_mientras ";
        if (tokens.get(cont++).getLexicalComp().equalsIgnoreCase("Parentesis_Apertura")) {
            regla += "Parentesis_Apertura ";
        } else {
            Token t = tokens.get(cont - 1);
            regla += "ERROR ";
            Compilador.errors.add("ERROR SINTACTICO: Se esperaba ( Parentesis_Apertura ) en la posicion [" + t.getLine() + ", " + t.getColumn() + "]");
        }

        regla += "SENTENCIA ";
        cont = sentencia();

        if (tokens.get(cont++).getLexicalComp().equalsIgnoreCase("Parentesis_Cierre")) {
            regla += "Parentesis_Cierre ";
        } else {
            Token t = tokens.get(cont - 1);
            regla += "ERROR ";
            Compilador.errors.add("ERROR SINTACTICO: Se esperaba ( Parentesis_Cierre ) en la posicion [" + t.getLine() + ", " + t.getColumn() + "]");
        }

        if (tokens.get(cont++).getLexicalComp().equalsIgnoreCase("Llave_Apertura")) {
            regla += "Llave_Apertura ";
        } else {
            Token t = tokens.get(cont - 1);
            regla += "ERROR ";
            Compilador.errors.add("ERROR SINTACTICO: Se esperaba ( Llave_Apertura ) en la posicion [" + t.getLine() + ", " + t.getColumn() + "]");
        }

        regla += "GRAMATICAS ";
        switch (tokens.get(cont).getLexicalComp()) {
            case "Para_Esto":
                cont = f.For(cont);
                break;
            default:
                cont = padre(cont);
                break;
        }
        while (!tokens.get(cont).getLexicalComp().equalsIgnoreCase("Llave_Cierre") && cont != -1) {
            if (tokens.get(cont).getLexicalComp().equals("Para_Esto")) {
                cont = f.For(cont);
            } else {
                cont = padre(cont);
            }
        }

        if (tokens.get(cont++).getLexicalComp().equalsIgnoreCase("Llave_Cierre")) {
            regla += "Llave_Cierre ";
        } else {
            Token t = tokens.get(cont - 1);
            regla += "ERROR ";
            Compilador.errors.add("ERROR SINTACTICO: Se esperaba ( Llave_Cierre ) en la posicion [" + t.getLine() + ", " + t.getColumn() + "]");
        }

        Compilador.gramaticas.add(regla);
        return cont;
    }
}
