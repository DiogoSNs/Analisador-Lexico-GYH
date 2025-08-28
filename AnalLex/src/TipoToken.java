public enum TipoToken {
    // Palavras-chave da linguagem
    PCDec, PCProg, PCInt, PCReal, PCLer, PCImprimir,
    PCSe, PCEntao, PCSenao, PCEnqto, PCIni, PCFim,
    
    // Operadores aritmeticos: *, /, +, -
    OpAritMult, OpAritDiv, OpAritSoma, OpAritSub,
    
    // Operadores relacionais: <, <=, >, >=, ==, !=
    OpRelMenor, OpRelMenorIgual, OpRelMaior,
    OpRelMaiorIgual, OpRelIgual, OpRelDif,
    
    // Operadores booleanos: E, OU
    OpBoolE, OpBoolOu,
    
    // Delimitadores e simbolos especiais
    DelimAbre,    // [
    DelimFecha,   // ]
    Atrib,        // :=
    AbrePar,      // (
    FechaPar,     // )
    Delim,        // :
    
    // Identificadores e literais
    Var,          // variaveis (começam com minuscula)
    NumInt,       // numeros inteiros
    NumReal,      // numeros reais (com ponto)
    Cadeia,       // strings entre aspas
    
    // Fim do arquivo
    EOF
}