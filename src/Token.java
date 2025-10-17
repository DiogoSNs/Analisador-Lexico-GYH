public class Token {
    private TipoToken tipo;
    private String lexema;

    // Construtor para criar um token com seu tipo e o texto que representa
    public Token(TipoToken tipo, String lexema) {
        this.tipo = tipo;
        this.lexema = lexema;
    }

    // Retorna o tipo do token (palavra-chave, variavel, numero, etc.)
    public TipoToken getTipo() {
        return tipo;
    }

    // Retorna o texto original encontrado no codigo fonte
    public String getLexema() {
        return lexema;
    }

    // Formata o token no padrão <TipoToken, "lexema"> para exibição
    @Override
    public String toString() {
        return "<" + tipo + ", \"" + lexema + "\">";
    }
}