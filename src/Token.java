/**
 * Classe Token - Representa um token reconhecido pelo analisador léxico
 * 
 * Um token é a menor unidade léxica com significado na linguagem.
 * Contém:
 * - Tipo: classificação do token (palavra-chave, operador, número, etc)
 * - Lexema: texto original do código fonte
 * - Linha: número da linha onde foi encontrado (para mensagens de erro)
 * 
 * Exemplos de tokens:
 * - <PCInt, "INTEGER", linha:5>
 * - <Var, "num1", linha:7>
 * - <OpAritSoma, "+", linha:10>
 */
public class Token {
    private TipoToken tipo;   // Classificação do token
    private String lexema;    // Texto original do código
    private int linha;        // Linha onde aparece no código fonte

    /**
     * Construtor completo do Token
     * 
     * @param tipo Tipo/classificação do token
     * @param lexema Texto original encontrado no código
     * @param linha Número da linha no arquivo fonte
     */
    public Token(TipoToken tipo, String lexema, int linha) {
        this.tipo = tipo;
        this.lexema = lexema;
        this.linha = linha;
    }

    /**
     * Construtor alternativo sem número de linha
     * Usado em casos especiais onde a linha não é relevante
     */
    public Token(TipoToken tipo, String lexema) {
        this.tipo = tipo;
        this.lexema = lexema;
    }

    /**
     * Retorna o tipo do token
     * @return Tipo do token (enum TipoToken)
     */
    public TipoToken getTipo() {
        return tipo;
    }

    /**
     * Retorna o lexema (texto original) do token
     * @return String com o texto do token
     */
    public String getLexema() {
        return lexema;
    }

    /**
     * Retorna o número da linha onde o token foi encontrado
     * @return Número da linha
     */
    public int getLinha() {
        return linha;
    }

    /**
     * Formata o token para exibição
     * Formato: <TipoToken, "lexema", linha:N>
     * 
     * @return String formatada do token
     */
    @Override
    public String toString() {
        return "<" + tipo + ", \"" + lexema + "\", linha:" + linha + ">";
    }
}