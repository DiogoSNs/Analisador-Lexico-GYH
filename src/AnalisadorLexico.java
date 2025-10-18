import java.io.*;
import java.util.*;
import java.util.regex.*;

/**
 * Analisador Léxico da linguagem GYH
 * 
 * Responsável pela primeira fase da compilação: análise léxica
 * Lê o código fonte e o transforma em uma sequência de tokens
 * 
 * Funcionalidades:
 * - Reconhece palavras-chave da linguagem
 * - Identifica operadores aritméticos, relacionais e booleanos
 * - Reconhece números inteiros e reais
 * - Reconhece variáveis (começam com letra minúscula)
 * - Identifica cadeias de caracteres (strings entre aspas)
 * - Ignora comentários (linhas iniciadas com #)
 * - Ignora espaços em branco
 * - Detecta tokens desconhecidos e reporta erros
 */
public class AnalisadorLexico {
    private List<Token> tokens = new ArrayList<>();  // Lista de tokens reconhecidos
    private List<String> erros = new ArrayList<>();  // Lista de erros léxicos encontrados

    /**
     * Mapa com todas as palavras-chave (palavras reservadas) da linguagem GYH
     * 
     * Palavras-chave são identificadores especiais que têm significado específico
     * na linguagem e não podem ser usados como nomes de variáveis
     */
    private final Map<String, TipoToken> palavrasChave = Map.ofEntries(
        Map.entry("DECLARAR", TipoToken.PCDec),      // Início da seção de declarações
        Map.entry("PROGRAMA", TipoToken.PCProg),     // Início da seção de comandos
        Map.entry("INTEGER", TipoToken.PCInt),       // Tipo de dado inteiro
        Map.entry("REAL", TipoToken.PCReal),         // Tipo de dado real (ponto flutuante)
        Map.entry("LER", TipoToken.PCLer),           // Comando de entrada
        Map.entry("IMPRIMIR", TipoToken.PCImprimir), // Comando de saída
        Map.entry("SE", TipoToken.PCSe),             // Início de estrutura condicional
        Map.entry("SENAO", TipoToken.PCSenao),       // Alternativa da estrutura condicional
        Map.entry("ENTAO", TipoToken.PCEntao),       // Separador de condição e comando
        Map.entry("ENQTO", TipoToken.PCEnqto),       // Estrutura de repetição (while)
        Map.entry("INICIO", TipoToken.PCIni),        // Início de bloco de comandos
        Map.entry("FINAL", TipoToken.PCFim)          // Fim de bloco de comandos
    );

    /**
     * Construtor do Analisador Léxico
     * Recebe o nome do arquivo e já processa automaticamente
     * 
     * @param arquivo Caminho do arquivo .gyh a ser analisado
     * @throws IOException Se houver erro ao ler o arquivo
     */
    public AnalisadorLexico(String arquivo) throws IOException {
        processarArquivo(arquivo);
    }

    /**
     * Lê o arquivo fonte linha por linha e analisa cada uma
     * Ao final, adiciona um token EOF (End Of File) para marcar o fim
     * 
     * @param arquivo Caminho do arquivo a ser processado
     * @throws IOException Se houver erro na leitura do arquivo
     */
    private void processarArquivo(String arquivo) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(arquivo));
        String linha;
        int numLinha = 1;

        // Processa cada linha do arquivo
        while ((linha = reader.readLine()) != null) {
            analisarLinha(linha, numLinha);
            numLinha++;
        }

        // Adiciona token EOF no final - importante para o analisador sintático
        // saber onde o arquivo termina
        tokens.add(new Token(TipoToken.EOF, "EOF", numLinha));
        reader.close();
    }

    /**
     * Analisa uma linha do código fonte e extrai os tokens
     * 
     * Este método é o coração do analisador léxico. Usa expressões regulares
     * para identificar padrões e classificar cada lexema encontrado.
     * 
     * @param linha Linha do código fonte a ser analisada
     * @param numLinha Número da linha (para reportar erros)
     */
    private void analisarLinha(String linha, int numLinha) {
        // Ignora linhas vazias - não há tokens para processar
        if (linha.trim().isEmpty()) {
            return;
        }

        // Remove comentários (tudo depois do #)
        // Comentários são ignorados pelo compilador
        if (linha.trim().startsWith("#")) {
            return; // Linha inteira é comentário
        }
        if (linha.contains("#")) {
            // Remove comentário ao final da linha
            linha = linha.substring(0, linha.indexOf("#"));
        }

        /**
         * Expressão Regular para capturar tokens
         * 
         * IMPORTANTE: A ordem importa!
         * Operadores compostos (:=, <=, >=, ==, !=) DEVEM vir ANTES dos simples
         * 
         * Componentes da regex:
         * - \"[^\"]*\"       → Strings entre aspas (ex: "texto")
         * - \\d+\\.\\d+      → Números reais (ex: 3.14)
         * - \\d+             → Números inteiros (ex: 42)
         * - \\w+             → Palavras (variáveis e palavras-chave)
         * - :=, <=, >=, ==, != → Operadores compostos (DEVEM VIR PRIMEIRO!)
         * - +, -, *, /       → Operadores aritméticos
         * - [, ], (, )       → Delimitadores
         * - :, <, >          → Operadores simples (DEPOIS dos compostos)
         * - \\S              → Qualquer outro caractere não-espaço
         */
        String regex = "(\"[^\"]*\"|\\d+\\.\\d+|\\d+|\\w+|:=|<=|>=|==|!=|\\+|\\-|\\*|/|\\[|\\]|\\(|\\)|:|<|>|\\S)";
        Matcher matcher = Pattern.compile(regex).matcher(linha);

        // Processa cada token encontrado na linha
        while (matcher.find()) {
            String lexema = matcher.group();

            // ===== CLASSIFICAÇÃO DO TOKEN =====
            // Verifica em ordem de prioridade qual tipo de token é

            // 1. PALAVRAS-CHAVE (têm prioridade sobre variáveis)
            if (palavrasChave.containsKey(lexema)) {
                tokens.add(new Token(palavrasChave.get(lexema), lexema, numLinha));
            }
            
            // 2. CADEIAS DE CARACTERES (strings entre aspas duplas)
            // Exemplo: "Hello World"
            else if (lexema.matches("\"[^\"]*\"")) {
                tokens.add(new Token(TipoToken.Cadeia, lexema, numLinha));
            }
            
            // 3. VARIÁVEIS VÁLIDAS (começam com letra minúscula)
            // Podem conter letras maiúsculas e números depois
            // Exemplos válidos: x, num1, valorTotal
            else if (lexema.matches("[a-z][a-zA-Z0-9]*")) {
                tokens.add(new Token(TipoToken.Var, lexema, numLinha));
            }
            
            // 4. OPERADORES BOOLEANOS
            // E e OU não são palavras-chave porque podem aparecer em diferentes contextos
            else if (lexema.equals("E")) {
                tokens.add(new Token(TipoToken.OpBoolE, lexema, numLinha));
            }
            else if (lexema.equals("OU")) {
                tokens.add(new Token(TipoToken.OpBoolOu, lexema, numLinha));
            }
            
            // 5. ERRO: Identificador com inicial maiúscula (não é palavra-chave)
            // Variáveis DEVEM começar com minúscula na linguagem GYH
            // Exemplo inválido: Nome, Valor
            else if (lexema.matches("[A-Z][a-zA-Z0-9]*")) {
                erros.add("Erro Léxico na linha " + numLinha + ": Desconhecido \"" + lexema + "\"");
            }
            
            // 6. NÚMEROS INTEIROS
            // Exemplos: 0, 42, 1000
            else if (lexema.matches("\\d+")) {
                tokens.add(new Token(TipoToken.NumInt, lexema, numLinha));
            }
            
            // 7. NÚMEROS REAIS (com ponto decimal obrigatório)
            // Exemplos: 3.14, 0.5, 100.0
            else if (lexema.matches("\\d+\\.\\d+")) {
                tokens.add(new Token(TipoToken.NumReal, lexema, numLinha));
            }
            
            // 8. DELIMITADORES E OPERADORES
            // Cada símbolo é testado individualmente
            
            // Delimitadores de seção
            else if (lexema.equals("[")) tokens.add(new Token(TipoToken.DelimAbre, lexema, numLinha));
            else if (lexema.equals("]")) tokens.add(new Token(TipoToken.DelimFecha, lexema, numLinha));
            
            // Símbolos especiais
            else if (lexema.equals(":")) tokens.add(new Token(TipoToken.Delim, lexema, numLinha));
            else if (lexema.equals(":=")) tokens.add(new Token(TipoToken.Atrib, lexema, numLinha));
            
            // Operadores relacionais
            else if (lexema.equals("==")) tokens.add(new Token(TipoToken.OpRelIgual, lexema, numLinha));
            else if (lexema.equals("!=")) tokens.add(new Token(TipoToken.OpRelDif, lexema, numLinha));
            else if (lexema.equals("<=")) tokens.add(new Token(TipoToken.OpRelMenorIgual, lexema, numLinha));
            else if (lexema.equals(">=")) tokens.add(new Token(TipoToken.OpRelMaiorIgual, lexema, numLinha));
            else if (lexema.equals("<")) tokens.add(new Token(TipoToken.OpRelMenor, lexema, numLinha));
            else if (lexema.equals(">")) tokens.add(new Token(TipoToken.OpRelMaior, lexema, numLinha));
            
            // Operadores aritméticos
            else if (lexema.equals("+")) tokens.add(new Token(TipoToken.OpAritSoma, lexema, numLinha));
            else if (lexema.equals("-")) tokens.add(new Token(TipoToken.OpAritSub, lexema, numLinha));
            else if (lexema.equals("*")) tokens.add(new Token(TipoToken.OpAritMult, lexema, numLinha));
            else if (lexema.equals("/")) tokens.add(new Token(TipoToken.OpAritDiv, lexema, numLinha));
            
            // Parênteses (para alterar precedência em expressões)
            else if (lexema.equals("(")) tokens.add(new Token(TipoToken.AbrePar, lexema, numLinha));
            else if (lexema.equals(")")) tokens.add(new Token(TipoToken.FechaPar, lexema, numLinha));
            
            // 9. TOKEN DESCONHECIDO
            // Se chegou até aqui, não reconheceu o lexema - é um erro léxico
            else {
                erros.add("Erro Léxico na linha " + numLinha + ": Desconhecido \"" + lexema + "\"");
            }
        }
    }

    /**
     * Retorna a lista de tokens reconhecidos
     * Esta lista será usada pelo analisador sintático
     * 
     * @return Lista de objetos Token
     */
    public List<Token> getTokens() {
        return tokens;
    }

    /**
     * Retorna a lista de erros léxicos encontrados
     * 
     * @return Lista de strings com as mensagens de erro
     */
    public List<String> getErros() {
        return erros;
    }
}