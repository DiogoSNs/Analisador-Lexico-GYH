/**
 * Enum TipoToken - Define todos os tipos de tokens da linguagem GYH
 * 
 * Categorias:
 * 1. Palavras-chave (PC*) - palavras reservadas da linguagem
 * 2. Operadores aritméticos (OpArit*) - +, -, *, /
 * 3. Operadores relacionais (OpRel*) - <, <=, >, >=, ==, !=
 * 4. Operadores booleanos (OpBool*) - E, OU
 * 5. Delimitadores - [, ], :, :=, (, )
 * 6. Identificadores e literais - variáveis, números, strings
 * 7. EOF - marcador de fim de arquivo
 */
public enum TipoToken {
    // ===== PALAVRAS-CHAVE DA LINGUAGEM =====
    // Palavras reservadas que têm significado especial
    PCDec,          // DECLARAR - inicia seção de declarações
    PCProg,         // PROGRAMA - inicia seção de comandos
    PCInt,          // INTEGER - tipo de dado inteiro
    PCReal,         // REAL - tipo de dado real
    PCLer,          // LER - comando de entrada
    PCImprimir,     // IMPRIMIR - comando de saída
    PCSe,           // SE - início de condicional
    PCEntao,        // ENTAO - separador de condição
    PCSenao,        // SENAO - alternativa do SE
    PCEnqto,        // ENQTO - estrutura de repetição
    PCIni,          // INICIO - início de bloco
    PCFim,          // FINAL - fim de bloco
    
    // ===== OPERADORES ARITMÉTICOS =====
    // Usados em expressões matemáticas
    OpAritMult,     // * (multiplicação)
    OpAritDiv,      // / (divisão)
    OpAritSoma,     // + (adição)
    OpAritSub,      // - (subtração)
    
    // ===== OPERADORES RELACIONAIS =====
    // Usados para comparações
    OpRelMenor,        // < (menor que)
    OpRelMenorIgual,   // <= (menor ou igual)
    OpRelMaior,        // > (maior que)
    OpRelMaiorIgual,   // >= (maior ou igual)
    OpRelIgual,        // == (igual a)
    OpRelDif,          // != (diferente de)
    
    // ===== OPERADORES BOOLEANOS =====
    // Usados para combinar condições
    OpBoolE,        // E (AND lógico)
    OpBoolOu,       // OU (OR lógico)
    
    // ===== DELIMITADORES E SÍMBOLOS ESPECIAIS =====
    DelimAbre,      // [ (abre seção)
    DelimFecha,     // ] (fecha seção)
    Atrib,          // := (atribuição)
    AbrePar,        // ( (abre parênteses)
    FechaPar,       // ) (fecha parênteses)
    Delim,          // : (delimitador de declaração)
    
    // ===== IDENTIFICADORES E LITERAIS =====
    Var,            // Variáveis (começam com letra minúscula)
    NumInt,         // Números inteiros (ex: 42)
    NumReal,        // Números reais (ex: 3.14)
    Cadeia,         // Strings entre aspas (ex: "texto")
    
    // ===== MARCADOR ESPECIAL =====
    EOF             // End Of File - marca fim do arquivo
}