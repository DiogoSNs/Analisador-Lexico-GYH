import java.util.List;

public class AnalisadorSintatico {
    private List<Token> tokens;
    private int posicao;
    private Token tokenAtual;
    private StringBuilder erros;
    private boolean temErro;

    public AnalisadorSintatico(List<Token> tokens) {
        this.tokens = tokens;
        this.posicao = 0;
        this.tokenAtual = tokens.get(0);
        this.erros = new StringBuilder();
        this.temErro = false;
    }

    // ========== MÉTODOS DA GRAMÁTICA ========== 

    // Avança para o próximo token
    private void avancar() {
        if (posicao < tokens.size() - 1) {
            posicao++;
            tokenAtual = tokens.get(posicao);
        }
    }

    // Verifica se o token atual é do tipo esperado e avança
    private boolean match(TipoToken tipo) {
        if (tokenAtual.getTipo() == tipo) {
            avancar();
            return true;
        }
        return false;
    }

    // Consome um token esperado ou gera erro
    private void consumir(TipoToken tipo, String mensagemErro) {
        if (tokenAtual.getTipo() == tipo) {
            avancar();
        } else {
            erro(mensagemErro + ", foi encontrado: '" + tokenAtual.getLexema() + "'");
        }
    }

    // Registra um erro sintático
    private void erro(String mensagem) {
        temErro = true;
        erros.append("Erro Sintático: ").append(mensagem).append("\n");
    }

    // Verifica se o token atual é do tipo especificado
    private boolean check(TipoToken tipo) {
        return tokenAtual.getTipo() == tipo;
    }

    // ========== MÉTODOS DA GRAMÁTICA ==========

    // Programa → '[' 'DECLARAR' ']' ListaDeclaracoes '[' 'PROGRAMA' ']' ListaComandos
    public void programa() {
        consumir(TipoToken.DelimAbre, "Esperado '[' no início do programa");
        consumir(TipoToken.PCDec, "Esperado palavra-chave 'DECLARAR'");
        consumir(TipoToken.DelimFecha, "Esperado ']' após 'DECLARAR'");
        
        listaDeclaracoes();
        
        consumir(TipoToken.DelimAbre, "Esperado '[' antes de 'PROGRAMA'");
        consumir(TipoToken.PCProg, "Esperado palavra-chave 'PROGRAMA'");
        consumir(TipoToken.DelimFecha, "Esperado ']' após 'PROGRAMA'");
        
        listaComandos();
        
        if (!check(TipoToken.EOF)) {
            erro("Esperado fim do arquivo após os comandos");
        }
    }

    // ListaDeclaracoes → Declaracao ListaDeclaracoes'
    private void listaDeclaracoes() {
        if (check(TipoToken.Var)) {
            declaracao();
            listaDeclaracoesLinha();
        } else {
            erro("Esperado pelo menos uma declaração de variável");
        }
    }

    // ListaDeclaracoes' → Declaracao ListaDeclaracoes' | ε
    private void listaDeclaracoesLinha() {
        if (check(TipoToken.Var)) {
            declaracao();
            listaDeclaracoesLinha();
        }
        // ε (epsilon) - produção vazia
    }

    // Declaracao → VARIAVEL ':' TipoVar
    private void declaracao() {
        consumir(TipoToken.Var, "Esperado nome de variável na declaração");
        consumir(TipoToken.Delim, "Esperado ':' após nome da variável");
        tipoVar();
    }

    // TipoVar → 'INTEGER' | 'REAL'
    private void tipoVar() {
        if (check(TipoToken.PCInt)) {
            match(TipoToken.PCInt);
        } else if (check(TipoToken.PCReal)) {
            match(TipoToken.PCReal);
        } else {
            erro("Esperado tipo 'INTEGER' ou 'REAL' na declaração");
        }
    }

    // ExpressaoAritmetica → TermoAritmetico ExpressaoAritmetica'
    private void expressaoAritmetica() {
        termoAritmetico();
        expressaoAritmeticaLinha();
    }

    // ExpressaoAritmetica' → '+' TermoAritmetico ExpressaoAritmetica' | '-' TermoAritmetico ExpressaoAritmetica' | ε
    private void expressaoAritmeticaLinha() {
        if (check(TipoToken.OpAritSoma)) {
            match(TipoToken.OpAritSoma);
            termoAritmetico();
            expressaoAritmeticaLinha();
        } else if (check(TipoToken.OpAritSub)) {
            match(TipoToken.OpAritSub);
            termoAritmetico();
            expressaoAritmeticaLinha();
        }
        // ε (epsilon)
    }

    // TermoAritmetico → FatorAritmetico TermoAritmetico'
    private void termoAritmetico() {
        fatorAritmetico();
        termoAritmeticoLinha();
    }

    // TermoAritmetico' → '*' FatorAritmetico TermoAritmetico' | '/' FatorAritmetico TermoAritmetico' | ε
    private void termoAritmeticoLinha() {
        if (check(TipoToken.OpAritMult)) {
            match(TipoToken.OpAritMult);
            fatorAritmetico();
            termoAritmeticoLinha();
        } else if (check(TipoToken.OpAritDiv)) {
            match(TipoToken.OpAritDiv);
            fatorAritmetico();
            termoAritmeticoLinha();
        }
        // ε (epsilon)
    }

    // FatorAritmetico → NUMINT | NUMREAL | VARIAVEL | '(' ExpressaoAritmetica ')'
    private void fatorAritmetico() {
        if (check(TipoToken.NumInt)) {
            match(TipoToken.NumInt);
        } else if (check(TipoToken.NumReal)) {
            match(TipoToken.NumReal);
        } else if (check(TipoToken.Var)) {
            match(TipoToken.Var);
        } else if (check(TipoToken.AbrePar)) {
            match(TipoToken.AbrePar);
            expressaoAritmetica();
            consumir(TipoToken.FechaPar, "Esperado ')' para fechar expressão");
        } else {
            erro("Esperado número, variável ou '(' em expressão aritmética");
        }
    }

    // ExpressaoRelacional → TermoRelacional ExpressaoRelacional'
    private void expressaoRelacional() {
        termoRelacional();
        expressaoRelacionalLinha();
    }

    // ExpressaoRelacional' → OperadorBooleano TermoRelacional ExpressaoRelacional' | ε
    private void expressaoRelacionalLinha() {
        if (check(TipoToken.OpBoolE) || check(TipoToken.OpBoolOu)) {
            operadorBooleano();
            termoRelacional();
            expressaoRelacionalLinha();
        }
        // ε (epsilon)
    }

    // TermoRelacional → ExpressaoAritmetica OP_REL ExpressaoAritmetica | '(' ExpressaoRelacional ')'
    private void termoRelacional() {
        if (check(TipoToken.AbrePar)) {
            match(TipoToken.AbrePar);
            expressaoRelacional();
            consumir(TipoToken.FechaPar, "Esperado ')' para fechar expressão relacional");
        } else {
            expressaoAritmetica();
            operadorRelacional();
            expressaoAritmetica();
        }
    }

    // Verifica operadores relacionais
    private void operadorRelacional() {
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

    // OperadorBooleano → 'E' | 'OU'
    private void operadorBooleano() {
        if (check(TipoToken.OpBoolE)) {
            match(TipoToken.OpBoolE);
        } else if (check(TipoToken.OpBoolOu)) {
            match(TipoToken.OpBoolOu);
        } else {
            erro("Esperado operador booleano 'E' ou 'OU'");
        }
    }

    // ListaComandos → Comando ListaComandos'
    private void listaComandos() {
        if (isInicioComando()) {
            comando();
            listaComandosLinha();
        } else {
            erro("Esperado pelo menos um comando após [PROGRAMA]");
        }
    }

    // ListaComandos' → Comando ListaComandos' | ε
    private void listaComandosLinha() {
        if (isInicioComando()) {
            comando();
            listaComandosLinha();
        }
        // ε (epsilon)
    }

    // Verifica se o token atual pode iniciar um comando
    private boolean isInicioComando() {
        return check(TipoToken.Var) || check(TipoToken.PCLer) || 
               check(TipoToken.PCImprimir) || check(TipoToken.PCSe) || 
               check(TipoToken.PCEnqto) || check(TipoToken.PCIni);
    }

    // Comando → ComandoAtribuicao | ComandoEntrada | ComandoSaida | ComandoCondicao | ComandoRepeticao | SubAlgoritmo
    private void comando() {
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

    // ComandoAtribuicao → VARIAVEL ':=' ExpressaoAritmetica
    private void comandoAtribuicao() {
        consumir(TipoToken.Var, "Esperado nome de variável no comando de atribuição");
        consumir(TipoToken.Atrib, "Esperado ':=' para atribuição (não use '=')");
        expressaoAritmetica();
    }

    // ComandoEntrada → 'LER' VARIAVEL
    private void comandoEntrada() {
        consumir(TipoToken.PCLer, "Esperado palavra-chave 'LER'");
        consumir(TipoToken.Var, "Esperado nome de variável após 'LER'");
    }

    // ComandoSaida → 'IMPRIMIR' ComandoSaida'
    // ComandoSaida' → VARIAVEL | CADEIA
    private void comandoSaida() {
        consumir(TipoToken.PCImprimir, "Esperado palavra-chave 'IMPRIMIR'");
        if (check(TipoToken.Var)) {
            match(TipoToken.Var);
        } else if (check(TipoToken.Cadeia)) {
            match(TipoToken.Cadeia);
        } else {
            erro("Esperado variável ou cadeia de caracteres após 'IMPRIMIR'");
        }
    }

    // ComandoCondicao → 'SE' ExpressaoRelacional 'ENTAO' Comando ComandoCondicao'
    // ComandoCondicao' → 'SENAO' Comando | ε
    private void comandoCondicao() {
        consumir(TipoToken.PCSe, "Esperado palavra-chave 'SE'");
        expressaoRelacional();
        consumir(TipoToken.PCEntao, "Esperado palavra-chave 'ENTAO' após condição do SE");
        comando();
        
        // Parte opcional SENAO
        if (check(TipoToken.PCSenao)) {
            match(TipoToken.PCSenao);
            comando();
        }
    }

    // ComandoRepeticao → 'ENQTO' ExpressaoRelacional Comando
    private void comandoRepeticao() {
        consumir(TipoToken.PCEnqto, "Esperado palavra-chave 'ENQTO'");
        expressaoRelacional();
        comando();
    }

    // SubAlgoritmo → 'INICIO' ListaComandos 'FINAL'
    private void subAlgoritmo() {
        consumir(TipoToken.PCIni, "Esperado palavra-chave 'INICIO'");
        listaComandos();
        consumir(TipoToken.PCFim, "Esperado palavra-chave 'FINAL' para fechar bloco");
    }

    // ========== MÉTODOS PÚBLICOS ==========

    public void analisar() {
        System.out.println("\n=== INICIANDO ANÁLISE SINTÁTICA ===\n");
        programa();
        
        if (temErro) {
            System.out.println("=== ERROS SINTÁTICOS ENCONTRADOS ===");
            System.out.println(erros.toString());
        } else {
            System.out.println("=== ANÁLISE SINTÁTICA CONCLUÍDA COM SUCESSO ===");
            System.out.println("O programa está sintaticamente correto!");
        }
    }

    public boolean temErros() {
        return temErro;
    }

    public String getErros() {
        return erros.toString();
    }
}