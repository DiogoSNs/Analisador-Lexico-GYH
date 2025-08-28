import java.io.*;
import java.util.*;
import java.util.regex.*;

public class AnalisadorLexico {
    private List<Token> tokens = new ArrayList<>();
    private List<String> erros = new ArrayList<>();

    // Mapa com todas as palavras-chave da linguagem
    private final Map<String, TipoToken> palavrasChave = Map.ofEntries(
        Map.entry("DECLARAR", TipoToken.PCDec),
        Map.entry("PROGRAMA", TipoToken.PCProg),
        Map.entry("INTEGER", TipoToken.PCInt),
        Map.entry("REAL", TipoToken.PCReal),
        Map.entry("LER", TipoToken.PCLer),
        Map.entry("IMPRIMIR", TipoToken.PCImprimir),
        Map.entry("SE", TipoToken.PCSe),
        Map.entry("SENAO", TipoToken.PCSenao),
        Map.entry("ENTAO", TipoToken.PCEntao),
        Map.entry("ENQTO", TipoToken.PCEnqto),
        Map.entry("INICIO", TipoToken.PCIni),
        Map.entry("FINAL", TipoToken.PCFim)
    );

    // Construtor que ja processa o arquivo automaticamente
    public AnalisadorLexico(String arquivo) throws IOException {
        processarArquivo(arquivo);
    }

    // Le o arquivo linha por linha e analisa cada uma
    private void processarArquivo(String arquivo) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(arquivo));
        String linha;
        int numLinha = 1;

        while ((linha = reader.readLine()) != null) {
            analisarLinha(linha, numLinha);
            numLinha++;
        }

        // Adiciona token EOF no final para marcar fim do arquivo
        tokens.add(new Token(TipoToken.EOF, "EOF"));
        reader.close();
    }

    // Analisa uma linha do codigo e extrai os tokens
    private void analisarLinha(String linha, int numLinha) {
        // Pula linhas vazias
        if (linha.trim().isEmpty()) {
            return;
        }

        // Remove comentarios (tudo depois do #)
        if (linha.trim().startsWith("#")) {
            return;
        }
        if (linha.contains("#")) {
            linha = linha.substring(0, linha.indexOf("#"));
        }

        // Expressao regular que captura todos os tipos de token
        String regex = "(\"[^\"]*\"|\\w+|\\d+\\.\\d+|\\d+|:=|<=|>=|==|!=|\\+|\\-|\\*|/|\\[|\\]|\\(|\\)|:|<|>)";
        Matcher matcher = Pattern.compile(regex).matcher(linha);

        while (matcher.find()) {
            String lexema = matcher.group();

            // Verifica se e palavra-chave
            if (palavrasChave.containsKey(lexema)) {
                tokens.add(new Token(palavrasChave.get(lexema), lexema));
            }
            // String entre aspas
            else if (lexema.matches("\"[^\"]*\"")) {
                tokens.add(new Token(TipoToken.Cadeia, lexema));
            }
            // Variavel valida (comeca com minuscula)
            else if (lexema.matches("[a-z][a-zA-Z0-9]*")) {
                tokens.add(new Token(TipoToken.Var, lexema));
            }
            // Operadores booleanos E e OU
            else if (lexema.equals("E")) {
                tokens.add(new Token(TipoToken.OpBoolE, lexema));
            }
            else if (lexema.equals("OU")) {
                tokens.add(new Token(TipoToken.OpBoolOu, lexema));
            }
            // Detecta erro: variavel com inicial maiuscula
            else if (lexema.matches("[A-Z][a-zA-Z0-9]*")) {
                erros.add("Erro Léxico na linha " + numLinha + ": variável inválida \"" + lexema + "\" (deve começar com minúscula)");
            }
            // Numeros inteiros
            else if (lexema.matches("\\d+")) {
                tokens.add(new Token(TipoToken.NumInt, lexema));
            }
            // Numeros reais (com ponto decimal)
            else if (lexema.matches("\\d+\\.\\d+")) {
                tokens.add(new Token(TipoToken.NumReal, lexema));
            }
            // Delimitadores e operadores
            else if (lexema.equals("[")) tokens.add(new Token(TipoToken.DelimAbre, lexema));
            else if (lexema.equals("]")) tokens.add(new Token(TipoToken.DelimFecha, lexema));
            else if (lexema.equals(":")) tokens.add(new Token(TipoToken.Delim, lexema));
            else if (lexema.equals(":=")) tokens.add(new Token(TipoToken.Atrib, lexema));
            else if (lexema.equals("==")) tokens.add(new Token(TipoToken.OpRelIgual, lexema));
            else if (lexema.equals("!=")) tokens.add(new Token(TipoToken.OpRelDif, lexema));
            else if (lexema.equals("<=")) tokens.add(new Token(TipoToken.OpRelMenorIgual, lexema));
            else if (lexema.equals(">=")) tokens.add(new Token(TipoToken.OpRelMaiorIgual, lexema));
            else if (lexema.equals("<")) tokens.add(new Token(TipoToken.OpRelMenor, lexema));
            else if (lexema.equals(">")) tokens.add(new Token(TipoToken.OpRelMaior, lexema));
            else if (lexema.equals("+")) tokens.add(new Token(TipoToken.OpAritSoma, lexema));
            else if (lexema.equals("-")) tokens.add(new Token(TipoToken.OpAritSub, lexema));
            else if (lexema.equals("*")) tokens.add(new Token(TipoToken.OpAritMult, lexema));
            else if (lexema.equals("/")) tokens.add(new Token(TipoToken.OpAritDiv, lexema));
            else if (lexema.equals("(")) tokens.add(new Token(TipoToken.AbrePar, lexema));
            else if (lexema.equals(")")) tokens.add(new Token(TipoToken.FechaPar, lexema));
            // Se chegou ate aqui, e um token desconhecido
            else {
                erros.add("Erro Léxico na linha " + numLinha + ": desconhecido \"" + lexema + "\"");
            }
        }
    }

    // Retorna a lista de tokens encontrados
    public List<Token> getTokens() {
        return tokens;
    }

    // Retorna a lista de erros encontrados
    public List<String> getErros() {
        return erros;
    }
}