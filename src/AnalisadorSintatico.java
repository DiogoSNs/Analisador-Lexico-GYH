import java.util.List;

/**
 * Analisador Sintático - Implementação de um Parser Descendente Recursivo
 * 
 * Este analisador verifica se a sequência de tokens gerada pelo analisador léxico
 * está de acordo com a gramática da linguagem GYH.
 * 
 * Características:
 * - Parser Descendente Recursivo (cada regra da gramática vira um método)
 * - Para no primeiro erro encontrado
 * - Reporta a linha onde ocorreu o erro sintático
 */
public class AnalisadorSintatico {
    private List<Token> tokens;        // Lista de tokens recebida do analisador léxico
    private int posicao;               // Posição atual na lista de tokens
    private Token tokenAtual;          // Token sendo analisado no momento
    private StringBuilder erros;       // Armazena mensagens de erro
    private boolean temErro;           // Flag que indica se encontrou algum erro

    /**
     * Construtor do Analisador Sintático
     * @param tokens Lista de tokens gerada pelo analisador léxico
     */
    public AnalisadorSintatico(List<Token> tokens) {
        this.tokens = tokens;
        this.posicao = 0;
        // Proteção contra lista vazia - evita NullPointerException
        this.tokenAtual = tokens.isEmpty() ? null : tokens.get(0);
        this.erros = new StringBuilder();
        this.temErro = false;
    }

    // ========== MÉTODOS AUXILIARES ========== 

    /**
     * Avança para o próximo token da lista
     * Garante que não ultrapasse o limite da lista
     */
    private void avancar() {
        if (posicao < tokens.size() - 1) {
            posicao++;
            tokenAtual = tokens.get(posicao);
        }
    }

    /**
     * Verifica se o token atual é do tipo esperado e avança
     * Usado quando o token é opcional ou quando já sabemos que é o correto
     * 
     * @param tipo Tipo de token esperado
     * @return true se o token foi encontrado e consumido, false caso contrário
     */
    private boolean match(TipoToken tipo) {
        // Se já tem erro ou lista acabou, não processa mais
        if (temErro || tokenAtual == null) return false;
        
        if (tokenAtual.getTipo() == tipo) {
            avancar();
            return true;
        }
        return false;
    }

    /**
     * Consome um token obrigatório ou gera erro
     * Este método é usado quando esperamos um token específico obrigatoriamente
     * 
     * @param tipo Tipo de token esperado
     * @param mensagemErro Mensagem de erro caso o token não seja encontrado
     */
    private void consumir(TipoToken tipo, String mensagemErro) {
        // Se já tem erro, para a análise
        if (temErro || tokenAtual == null) return;
        
        if (tokenAtual.getTipo() == tipo) {
            avancar();
        } else {
            // Token esperado não foi encontrado - gera erro
            erro(mensagemErro);
        }
    }

    /**
     * Registra um erro sintático com o número da linha
     * Como paramos no primeiro erro, só registra se ainda não houver erro
     * 
     * @param mensagem Descrição do erro encontrado
     */
    private void erro(String mensagem) {
        if (temErro) return; // Só registra o primeiro erro
        
        temErro = true;
        // Captura informações do token para exibir no erro
        int linha = (tokenAtual != null) ? tokenAtual.getLinha() : -1;
        String lexema = (tokenAtual != null) ? tokenAtual.getLexema() : "EOF";
        
        // Formata a mensagem de erro com linha e token encontrado
        erros.append("Erro Sintático na linha ")
             .append(linha)
             .append(": ")
             .append(mensagem)
             .append(", foi encontrado: '")
             .append(lexema)
             .append("'\n");
    }

    /**
     * Verifica se o token atual é do tipo especificado SEM consumir
     * Usado para lookahead (olhar à frente) nas decisões da gramática
     * 
     * @param tipo Tipo de token a verificar
     * @return true se o token atual é do tipo especificado
     */
    private boolean check(TipoToken tipo) {
        if (temErro || tokenAtual == null) return false;
        return tokenAtual.getTipo() == tipo;
    }

    // ========== MÉTODOS DA GRAMÁTICA (DESCENDENTE RECURSIVO) ==========
    // Cada método abaixo representa uma regra de produção da gramática
    // A estrutura segue exatamente a definição da gramática da linguagem

    /**
     * Programa → '[' 'DECLARAR' ']' ListaDeclaracoes '[' 'PROGRAMA' ']' ListaComandos
     * 
     * Esta é a regra inicial da gramática - todo programa válido deve seguir esta estrutura:
     * 1. Seção de declarações entre [DECLARAR] e ]
     * 2. Seção de comandos entre [PROGRAMA] e ]
     */
    public void programa() {
        // Início da seção de declarações: [DECLARAR]
        consumir(TipoToken.DelimAbre, "Esperado '[' no início do programa");
        if (temErro) return;
        
        consumir(TipoToken.PCDec, "Esperado palavra-chave 'DECLARAR'");
        if (temErro) return;
        
        consumir(TipoToken.DelimFecha, "Esperado ']' após 'DECLARAR'");
        if (temErro) return;
        
        // Processa todas as declarações de variáveis
        listaDeclaracoes();
        if (temErro) return;
        
        // Início da seção de comandos: [PROGRAMA]
        consumir(TipoToken.DelimAbre, "Esperado '[' antes de 'PROGRAMA'");
        if (temErro) return;
        
        consumir(TipoToken.PCProg, "Esperado palavra-chave 'PROGRAMA'");
        if (temErro) return;
        
        consumir(TipoToken.DelimFecha, "Esperado ']' após 'PROGRAMA'");
        if (temErro) return;
        
        // Processa todos os comandos do programa
        listaComandos();
        if (temErro) return;
        
        // Verifica se chegou ao final do arquivo
        if (!check(TipoToken.EOF)) {
            erro("Esperado fim do arquivo após os comandos");
        }
    }

    /**
     * ListaDeclaracoes → Declaracao ListaDeclaracoes'
     * 
     * Processa a primeira declaração obrigatória
     * A gramática exige pelo menos UMA declaração de variável
     */
    private void listaDeclaracoes() {
        if (temErro) return;
        
        if (check(TipoToken.Var)) {
            declaracao();
            if (!temErro) {
                // Chama a versão recursiva para processar declarações adicionais
                listaDeclaracoesLinha();
            }
        } else {
            erro("Esperado pelo menos uma declaração de variável");
        }
    }

    /**
     * ListaDeclaracoes' → Declaracao ListaDeclaracoes' | ε
     * 
     * Processa declarações adicionais (zero ou mais)
     * ε (epsilon) representa produção vazia - pode não ter nenhuma declaração extra
     * 
     * RECURSÃO À DIREITA: permite processar múltiplas declarações
     */
    private void listaDeclaracoesLinha() {
        if (temErro) return;
        
        if (check(TipoToken.Var)) {
            declaracao();
            if (!temErro) {
                // RECURSÃO: chama a si mesmo para processar mais declarações
                listaDeclaracoesLinha();
            }
        }
        // ε (epsilon) - se não encontrar variável, apenas retorna (produção vazia)
    }

    /**
     * Declaracao → VARIAVEL ':' TipoVar
     * 
     * Estrutura de uma declaração:
     * - Nome da variável (começa com letra minúscula)
     * - Dois pontos (:)
     * - Tipo da variável (INTEGER ou REAL)
     * 
     * Exemplo: x:INTEGER
     */
    private void declaracao() {
        if (temErro) return;
        
        consumir(TipoToken.Var, "Esperado nome de variável na declaração");
        if (temErro) return;
        
        consumir(TipoToken.Delim, "Esperado ':' após nome da variável");
        if (temErro) return;
        
        tipoVar();
    }

    /**
     * TipoVar → 'INTEGER' | 'REAL'
     * 
     * Define o tipo de uma variável
     * Apenas dois tipos são permitidos na linguagem GYH
     */
    private void tipoVar() {
        if (temErro) return;
        
        if (check(TipoToken.PCInt)) {
            match(TipoToken.PCInt);
        } else if (check(TipoToken.PCReal)) {
            match(TipoToken.PCReal);
        } else {
            erro("Esperado tipo 'INTEGER' ou 'REAL' na declaração");
        }
    }

    /**
     * ExpressaoAritmetica → TermoAritmetico ExpressaoAritmetica'
     * 
     * Processa expressões aritméticas com operadores + e -
     * Segue a precedência: multiplicação/divisão antes de soma/subtração
     */
    private void expressaoAritmetica() {
        if (temErro) return;
        
        // Primeiro processa termos (que têm precedência maior)
        termoAritmetico();
        if (!temErro) {
            // Depois processa adições e subtrações
            expressaoAritmeticaLinha();
        }
    }

    /**
     * ExpressaoAritmetica' → '+' TermoAritmetico ExpressaoAritmetica' 
     *                       | '-' TermoAritmetico ExpressaoAritmetica' 
     *                       | ε
     * 
     * Processa operadores de soma (+) e subtração (-)
     * RECURSÃO À DIREITA permite expressões como: a + b - c + d
     */
    private void expressaoAritmeticaLinha() {
        if (temErro) return;
        
        if (check(TipoToken.OpAritSoma)) {
            match(TipoToken.OpAritSoma);
            termoAritmetico();
            if (!temErro) {
                // RECURSÃO: permite encadear múltiplas operações
                expressaoAritmeticaLinha();
            }
        } else if (check(TipoToken.OpAritSub)) {
            match(TipoToken.OpAritSub);
            termoAritmetico();
            if (!temErro) {
                expressaoAritmeticaLinha();
            }
        }
        // ε (epsilon) - se não houver mais operadores, apenas retorna
    }

    /**
     * TermoAritmetico → FatorAritmetico TermoAritmetico'
     * 
     * Processa termos aritméticos (multiplicação e divisão)
     * Tem precedência MAIOR que soma e subtração
     */
    private void termoAritmetico() {
        if (temErro) return;
        
        // Primeiro processa fatores (números, variáveis, parênteses)
        fatorAritmetico();
        if (!temErro) {
            // Depois processa multiplicações e divisões
            termoAritmeticoLinha();
        }
    }

    /**
     * TermoAritmetico' → '*' FatorAritmetico TermoAritmetico' 
     *                  | '/' FatorAritmetico TermoAritmetico' 
     *                  | ε
     * 
     * Processa operadores de multiplicação (*) e divisão (/)
     * RECURSÃO À DIREITA permite expressões como: a * b / c * d
     */
    private void termoAritmeticoLinha() {
        if (temErro) return;
        
        if (check(TipoToken.OpAritMult)) {
            match(TipoToken.OpAritMult);
            fatorAritmetico();
            if (!temErro) {
                // RECURSÃO: permite encadear múltiplas multiplicações/divisões
                termoAritmeticoLinha();
            }
        } else if (check(TipoToken.OpAritDiv)) {
            match(TipoToken.OpAritDiv);
            fatorAritmetico();
            if (!temErro) {
                termoAritmeticoLinha();
            }
        }
        // ε (epsilon)
    }

    /**
     * FatorAritmetico → NUMINT | NUMREAL | VARIAVEL | '(' ExpressaoAritmetica ')'
     * 
     * Elementos básicos de uma expressão aritmética:
     * - Números inteiros (ex: 42)
     * - Números reais (ex: 3.14)
     * - Variáveis (ex: x)
     * - Expressões entre parênteses (ex: (a + b))
     */
    private void fatorAritmetico() {
        if (temErro) return;
        
        if (check(TipoToken.NumInt)) {
            match(TipoToken.NumInt);
        } else if (check(TipoToken.NumReal)) {
            match(TipoToken.NumReal);
        } else if (check(TipoToken.Var)) {
            match(TipoToken.Var);
        } else if (check(TipoToken.AbrePar)) {
            // Parênteses permitem alterar a precedência
            match(TipoToken.AbrePar);
            expressaoAritmetica();  // RECURSÃO INDIRETA: volta para ExpressaoAritmetica
            consumir(TipoToken.FechaPar, "Esperado ')' para fechar expressão");
        } else {
            erro("Esperado número, variável ou '(' em expressão aritmética");
        }
    }

    /**
     * ExpressaoRelacional → TermoRelacional ExpressaoRelacional'
     * 
     * Processa expressões relacionais/booleanas
     * Usadas em condições (SE, ENQTO)
     */
    private void expressaoRelacional() {
        if (temErro) return;
        
        termoRelacional();
        if (!temErro) {
            expressaoRelacionalLinha();
        }
    }

    /**
     * ExpressaoRelacional' → OperadorBooleano TermoRelacional ExpressaoRelacional' | ε
     * 
     * Processa operadores lógicos E e OU
     * Permite encadear múltiplas condições: a > b E c < d OU e == f
     */
    private void expressaoRelacionalLinha() {
        if (temErro) return;
        
        if (check(TipoToken.OpBoolE) || check(TipoToken.OpBoolOu)) {
            operadorBooleano();
            if (!temErro) {
                termoRelacional();
            }
            if (!temErro) {
                // RECURSÃO: permite múltiplas condições encadeadas
                expressaoRelacionalLinha();
            }
        }
        // ε (epsilon)
    }

    /**
     * TermoRelacional → ExpressaoAritmetica OP_REL ExpressaoAritmetica 
     *                 | '(' ExpressaoRelacional ')'
     * 
     * Comparações entre expressões aritméticas (ex: x + 1 > y - 2)
     * Ou expressões relacionais entre parênteses para alterar precedência
     */
    private void termoRelacional() {
        if (temErro) return;
        
        if (check(TipoToken.AbrePar)) {
            // Parênteses em expressões relacionais
            match(TipoToken.AbrePar);
            expressaoRelacional();  // RECURSÃO INDIRETA
            consumir(TipoToken.FechaPar, "Esperado ')' para fechar expressão relacional");
        } else {
            // Comparação: expressão OP expressão (ex: a + b > c * d)
            expressaoAritmetica();
            if (!temErro) {
                operadorRelacional();
            }
            if (!temErro) {
                expressaoAritmetica();
            }
        }
    }

    /**
     * OperadorRelacional → '<' | '<=' | '>' | '>=' | '==' | '!='
     * 
     * Operadores de comparação disponíveis na linguagem
     */
    private void operadorRelacional() {
        if (temErro) return;
        
        if (check(TipoToken.OpRelMenor)) {
            match(TipoToken.OpRelMenor);
        } else if (check(TipoToken.OpRelMenorIgual)) {
            match(TipoToken.OpRelMenorIgual);
        } else if (check(TipoToken.OpRelMaior)) {
            match(TipoToken.OpRelMaior);
        } else if (check(TipoToken.OpRelMaiorIgual)) {
            match(TipoToken.OpRelMaiorIgual);
        } else if (check(TipoToken.OpRelIgual)) {
            match(TipoToken.OpRelIgual);
        } else if (check(TipoToken.OpRelDif)) {
            match(TipoToken.OpRelDif);
        } else {
            erro("Esperado operador relacional (<, <=, >, >=, ==, !=)");
        }
    }

    /**
     * OperadorBooleano → 'E' | 'OU'
     * 
     * Operadores lógicos para combinar condições
     */
    private void operadorBooleano() {
        if (temErro) return;
        
        if (check(TipoToken.OpBoolE)) {
            match(TipoToken.OpBoolE);
        } else if (check(TipoToken.OpBoolOu)) {
            match(TipoToken.OpBoolOu);
        } else {
            erro("Esperado operador booleano 'E' ou 'OU'");
        }
    }

    /**
     * ListaComandos → Comando ListaComandos'
     * 
     * Processa a lista de comandos do programa
     * Deve ter pelo menos um comando após [PROGRAMA]
     */
    private void listaComandos() {
        if (temErro) return;
        
        if (isInicioComando()) {
            comando();
            if (!temErro) {
                listaComandosLinha();
            }
        } else if (!check(TipoToken.EOF) && !check(TipoToken.PCFim)) {
            // Se não é comando, EOF ou FINAL, então é erro
            erro("Esperado comando válido após [PROGRAMA]");
        }
    }

    /**
     * ListaComandos' → Comando ListaComandos' | ε
     * 
     * Processa comandos adicionais (zero ou mais)
     * RECURSÃO À DIREITA permite múltiplos comandos sequenciais
     */
    private void listaComandosLinha() {
        if (temErro) return;
        
        if (isInicioComando()) {
            comando();
            if (!temErro) {
                // RECURSÃO: processa próximo comando
                listaComandosLinha();
            }
        }
        // ε (epsilon) - fim da lista de comandos
    }

    /**
     * Verifica se o token atual pode iniciar um comando válido
     * 
     * Tokens que podem iniciar comandos:
     * - Var (atribuição)
     * - LER (entrada)
     * - IMPRIMIR (saída)
     * - SE (condicional)
     * - ENQTO (repetição)
     * - INICIO (bloco de comandos)
     * 
     * @return true se pode iniciar um comando
     */
    private boolean isInicioComando() {
        if (temErro || tokenAtual == null) return false;
        return check(TipoToken.Var) || check(TipoToken.PCLer) || 
               check(TipoToken.PCImprimir) || check(TipoToken.PCSe) || 
               check(TipoToken.PCEnqto) || check(TipoToken.PCIni);
    }

    /**
     * Comando → ComandoAtribuicao | ComandoEntrada | ComandoSaida 
     *         | ComandoCondicao | ComandoRepeticao | SubAlgoritmo
     * 
     * Decide qual tipo de comando processar baseado no token atual
     * Usa o primeiro token para fazer essa decisão (lookahead)
     */
    private void comando() {
        if (temErro) return;
        
        if (check(TipoToken.Var)) {
            comandoAtribuicao();
        } else if (check(TipoToken.PCLer)) {
            comandoEntrada();
        } else if (check(TipoToken.PCImprimir)) {
            comandoSaida();
        } else if (check(TipoToken.PCSe)) {
            comandoCondicao();
        } else if (check(TipoToken.PCEnqto)) {
            comandoRepeticao();
        } else if (check(TipoToken.PCIni)) {
            subAlgoritmo();
        } else {
            erro("Comando inválido. Esperado variável, LER, IMPRIMIR, SE, ENQTO ou INICIO");
        }
    }

    /**
     * ComandoAtribuicao → VARIAVEL ':=' ExpressaoAritmetica
     * 
     * Atribui o resultado de uma expressão a uma variável
     * Exemplo: x := 2 + 3 * 4
     */
    private void comandoAtribuicao() {
        if (temErro) return;
        
        consumir(TipoToken.Var, "Esperado nome de variável no comando de atribuição");
        if (temErro) return;
        
        consumir(TipoToken.Atrib, "Esperado ':=' para atribuição");
        if (temErro) return;
        
        expressaoAritmetica();
    }

    /**
     * ComandoEntrada → 'LER' VARIAVEL
     * 
     * Lê um valor e armazena em uma variável
     * Exemplo: LER x
     */
    private void comandoEntrada() {
        if (temErro) return;
        
        consumir(TipoToken.PCLer, "Esperado palavra-chave 'LER'");
        if (temErro) return;
        
        consumir(TipoToken.Var, "Esperado nome de variável após 'LER'");
    }

    /**
     * ComandoSaida → 'IMPRIMIR' (VARIAVEL | CADEIA)
     * 
     * Imprime o valor de uma variável ou uma string literal
     * Exemplos: 
     * - IMPRIMIR x
     * - IMPRIMIR "Resultado:"
     */
    private void comandoSaida() {
        if (temErro) return;
        
        consumir(TipoToken.PCImprimir, "Esperado palavra-chave 'IMPRIMIR'");
        if (temErro) return;
        
        if (check(TipoToken.Var)) {
            match(TipoToken.Var);
        } else if (check(TipoToken.Cadeia)) {
            match(TipoToken.Cadeia);
        } else {
            erro("Esperado variável ou cadeia de caracteres após 'IMPRIMIR'");
        }
    }

    /**
     * ComandoCondicao → 'SE' ExpressaoRelacional 'ENTAO' Comando ['SENAO' Comando]
     * 
     * Estrutura condicional SE-ENTAO-SENAO
     * A parte SENAO é opcional (indicada por [])
     * 
     * Exemplo:
     * SE x > 10 ENTAO
     *    IMPRIMIR "maior"
     * SENAO
     *    IMPRIMIR "menor"
     */
    private void comandoCondicao() {
        if (temErro) return;
        
        consumir(TipoToken.PCSe, "Esperado palavra-chave 'SE'");
        if (temErro) return;
        
        expressaoRelacional();
        if (temErro) return;
        
        consumir(TipoToken.PCEntao, "Esperado palavra-chave 'ENTAO' após condição do SE");
        if (temErro) return;
        
        comando();
        if (temErro) return;
        
        // Parte opcional SENAO
        if (check(TipoToken.PCSenao)) {
            match(TipoToken.PCSenao);
            if (!temErro) {
                comando();
            }
        }
    }

    /**
     * ComandoRepeticao → 'ENQTO' ExpressaoRelacional Comando
     * 
     * Estrutura de repetição (laço WHILE)
     * 
     * Exemplo:
     * ENQTO x < 10
     *    x := x + 1
     */
    private void comandoRepeticao() {
        if (temErro) return;
        
        consumir(TipoToken.PCEnqto, "Esperado palavra-chave 'ENQTO'");
        if (temErro) return;
        
        expressaoRelacional();
        if (temErro) return;
        
        comando();
    }

    /**
     * SubAlgoritmo → 'INICIO' ListaComandos 'FINAL'
     * 
     * Bloco de comandos delimitado por INICIO e FINAL
     * Usado para agrupar múltiplos comandos em estruturas de controle
     * 
     * Exemplo:
     * INICIO
     *    x := 1
     *    y := 2
     *    z := x + y
     * FINAL
     */
    private void subAlgoritmo() {
        if (temErro) return;
        
        consumir(TipoToken.PCIni, "Esperado palavra-chave 'INICIO'");
        if (temErro) return;
        
        listaComandos();
        if (temErro) return;
        
        consumir(TipoToken.PCFim, "Esperado palavra-chave 'FINAL' para fechar bloco");
    }

    // ========== MÉTODOS PÚBLICOS ==========

    /**
     * Método principal que inicia a análise sintática
     * Chama o método programa() que é a raiz da gramática
     * Exibe o resultado da análise (sucesso ou erro)
     */
    public void analisar() {
        System.out.println("\n=== INICIANDO ANÁLISE SINTÁTICA ===\n");
        
        // Validação: verifica se há tokens para analisar
        if (tokens == null || tokens.isEmpty()) {
            System.out.println("ERRO: Nenhum token para analisar!");
            return;
        }
        
        // Inicia a análise pela regra inicial da gramática
        programa();
        
        // Exibe resultado
        if (temErro) {
            System.out.println("\n=== ERRO SINTÁTICO ENCONTRADO ===");
            System.out.println(erros.toString());
        } else {
            System.out.println("\n=== ANÁLISE SINTÁTICA CONCLUÍDA COM SUCESSO ===");
            System.out.println("✓ O programa está sintaticamente correto!");
        }
    }

    /**
     * Retorna se houve erros durante a análise
     * @return true se encontrou erro, false caso contrário
     */
    public boolean temErros() {
        return temErro;
    }

    /**
     * Retorna a string com as mensagens de erro
     * @return String contendo todos os erros encontrados
     */
    public String getErros() {
        return erros.toString();
    }
}