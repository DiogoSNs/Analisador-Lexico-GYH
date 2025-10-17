import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        try {
            // ========== FASE 1: ANÁLISE LÉXICA ==========
            System.out.println("===============================================");
            System.out.println("     COMPILADOR DA LINGUAGEM GYH");
            System.out.println("===============================================\n");
            
            AnalisadorLexico lex = new AnalisadorLexico("Analisador-Lexico-GYH/programa.gyh");

            System.out.println("=== TOKENS ENCONTRADOS ===");
            for (Token t : lex.getTokens()) {
                System.out.println(t);
            }

            // Verifica se há erros léxicos
            if (!lex.getErros().isEmpty()) {
                System.out.println("\n=== ERROS LÉXICOS ===");
                for (String erro : lex.getErros()) {
                    System.out.println(erro);
                }
                System.out.println("\nAnálise interrompida devido a erros léxicos.");
                return; // Não continua para análise sintática se há erros léxicos
            } else {
                System.out.println("\nAnálise léxica concluída sem erros!");
            }

            // ========== FASE 2: ANÁLISE SINTÁTICA ==========
            AnalisadorSintatico parser = new AnalisadorSintatico(lex.getTokens());
            parser.analisar();

            // ========== RESULTADO FINAL ==========
            System.out.println("\n===============================================");
            if (!parser.temErros()) {
                System.out.println("COMPILAÇÃO BEM-SUCEDIDA!");
            } else {
                System.out.println("COMPILAÇÃO FALHOU!");
                System.out.println("Corrija os erros e tente novamente.");
            }
            System.out.println("===============================================");

        } catch (IOException e) {
            System.out.println("Erro ao ler o arquivo: " + e.getMessage());
            System.out.println("Verifique se o arquivo 'programa.gyh' existe na pasta do projeto.");
        }
    }
}