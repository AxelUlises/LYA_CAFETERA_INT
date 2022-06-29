import compilerTools.TextColor;
import java.awt.Color;

%%
%class LexerColor
%type TextColor
%char
%{
    private TextColor textColor(long start, int size, Color color){
        return new TextColor((int) start, size, color);
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
{Comentario} { return textColor(yychar, yylength(), new Color(146, 146, 146)); }
{EspacioEnBlanco} { /*Ignorar*/ }

/* Identificador */
\${Identificador}  { return textColor(yychar, yylength(), new Color(48, 216, 106));}

/*Tipos de dato*/
número |
tamano { return textColor(yychar, yylength(), new Color(10, 181, 43));}

/* Número */
{Numero} { /* Ignorar */}

/* Operadores de agrupacion */
"(" { return textColor(yychar, yylength(), new Color(34, 170, 226 ));}
")" { return textColor(yychar, yylength(), new Color(34, 170, 226 ));}
"{" { return textColor(yychar, yylength(), new Color(34, 170, 226 ));}
"}" { return textColor(yychar, yylength(), new Color(34, 170, 226 ));}

/* Signos de puntuacion */
","  { /* Ignorar */}
";"  { /* Ignorar */}
":"  { /* Ignorar */}

/* Operador de asignacion */
".=" { return textColor(yychar, yylength(), new Color(19, 52, 205 ));}

/* Operadores logicos y relacionales */
"&" |
">" |
"<" |
">=" |
"<=" |
"==" { /* Ignorar */ }

/* Reservadas */
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
te|
import { return textColor(yychar, yylength(), new Color(236, 143, 45));}


/* Para esto */
for { return textColor(yychar, yylength(), new Color(245, 100, 247)); }

/* FUNCIONES */
SERVIR| 
PREPARAR| 
ALARMA| 
ESTADO| 
CLIMA| 
RECOMENDAR| 
PROXIMIDAD|
PREFERENCIA {return textColor(yychar, yylength(), new Color(130, 55, 239 ));}

/* Estructura si*/
if |
ifNot { return textColor(yychar, yylength(), new Color(245, 100, 247)); }

/* Estructura minetras*/
mientras |
hacerMientras { return textColor(yychar, yylength(), new Color(245, 100, 247 ));}

. {return textColor(yychar, yylength(), new Color(237, 0, 0));}