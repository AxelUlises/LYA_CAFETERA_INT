import compilerTools.Token;

%%
%class Lexer
%type Token
%line
%column
%{
    private Token token(String lexeme, String lexicalComp, int line, int column){
        return new Token(lexeme, lexicalComp, line+1, column+1);
    }
%}
/* Variables básicas de comentarios y espacios */
TerminadorDeLinea = \r|\n|\r\n
EntradaDeCaracter = [^\r\n]
EspacioEnBlanco = {TerminadorDeLinea} | [ \t\f]
ComentarioTradicional = "/*" [^*] ~"*/" | "/*" "*"+ "/"
FinDeLineaComentario = "//" {EntradaDeCaracter}* {TerminadorDeLinea}?
ContenidoComentario = ( [^*] | \*+ [^/*] )*
ComentarioDeDocumentacion = "/**" {ContenidoComentario} "*"+ "/"

/* Comentario */
Comentario = {ComentarioTradicional} | {FinDeLineaComentario} | {ComentarioDeDocumentacion}

/* Identificador */
Letra = [A-Za-zÑñ_ÁÉÍÓÚáéíóúÜü]
Digito = [0-9]
Identificador = {Letra}({Letra}|{Digito})*

/* Número */
Numero = 0 | [1-9][0-9]*

%%

/* Comentarios o espacios en blanco */
{Comentario}|{EspacioEnBlanco} { /*Ignorar*/ }

/* Identificador */
\${Identificador} {return token(yytext(), "IDENTIFICADOR", yyline, yycolumn);}

/*Tipos de dato*/
número |
tamano { return token(yytext(), "TIPO_DATO", yyline, yycolumn);}

/* Número */
{Numero} {return token(yytext(), "Número", yyline, yycolumn);}

/* Operadores de agrupacion */
"(" {return token(yytext(), "Parentesis_Apertura", yyline, yycolumn);}
")" {return token(yytext(), "Parentesis_Cierre", yyline, yycolumn);}
"{" {return token(yytext(), "Llave_Apertura", yyline, yycolumn);}
"}" {return token(yytext(), "Llave_Cierre", yyline, yycolumn);}

/* Signos de puntuacion */
"," {return token(yytext(), "COMA", yyline, yycolumn);}
";" {return token(yytext(), "Punto_Coma", yyline, yycolumn);}
":" {return token(yytext(), "Dos_Puntos", yyline, yycolumn);}

/* Operador de asignacion */
".=" {return token(yytext(), "OP_asignacion", yyline, yycolumn);}

/* operadores logicos */
"&" {return token (yytext(), "OP_LOGICO", yyline, yycolumn);}

/* Operadores relacionales */
">" |
"<" |
">=" |
"<=" |
"==" {return token (yytext(), "OP_RELACIONAL", yyline, yycolumn);}

/*FALTA DECLARAR FUNCIONES*/

/* Reservadas proyecto */
do|
cafe|
temp_cafe|
Complementos|
tiempo_cafe|
encender|
prender|
enciende|
prende|
apagar|
apaga|
Alexa|
alexa|
estado|
clima|
usuario|
Cantidad|
public|
new|
int|
doble|
grados|
String|
te|
import {return token(yytext(), "P_Reservada", yyline, yycolumn);}

/* FUNCIONES */
SERVIR| 
PREPARAR| 
ALARMA| 
ESTADO| 
CLIMA| 
RECOMENDAR| 
PROXIMIDAD|
PREFERENCIA {return token(yytext(), "FUNCION", yyline, yycolumn);}

/* Para esto */
for {return token(yytext(), "Para_Esto", yyline, yycolumn);}

/* Estructura si*/
if |
ifNot {return token(yytext(), "Estructura_IF", yyline, yycolumn);}

/* Estructura minetras*/
mientras |
hacerMientras {return token(yytext(), "Estructura_Mientras", yyline, yycolumn);}

. { return token(yytext(), "ERROR_1", yyline, yycolumn); }


