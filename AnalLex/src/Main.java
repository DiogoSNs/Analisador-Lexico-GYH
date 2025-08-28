import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        try {
            // Cria o analisador lexico para o arquivo de codigo fonte
            AnalisadorLexico lex = new AnalisadorLexico("programa.gyh");

            // Mostra todos os tokens encontrados
            System.out.println("=== TOKENS ENCONTRADOS ===");
            for (Token t : lex.getTokens()) {
                System.out.println(t);
            }

            // Se houver erros lexicos, mostra eles tambem
            if (!lex.getErros().isEmpty()) {
                System.out.println("\n=== ERROS LEXICOS ===");
                for (String erro : lex.getErros()) {
                    System.out.println(erro);
                }
            } else {
                System.out.println("\nAnalise lexica concluida sem erros!");
            }

        } catch (IOException e) {
            System.out.println("Erro ao ler o arquivo: " + e.getMessage());
            System.out.println("Verifique se o arquivo 'programa.gyh' existe na pasta do projeto.");
        }
    }
}