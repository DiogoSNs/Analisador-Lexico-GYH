import java.io.IOException;

/**
 * Classe Main - Ponto de entrada do compilador
 * 
 * Executa as fases de compilação em ordem:
 * 1. Análise Léxica - transforma código em tokens
 * 2. Análise Sintática - verifica estrutura gramatical
 * 
 * Fases futuras (não implementadas ainda):
 * 3. Análise Semântica - verifica significado/tipos
 * 4. Geração de Código - produz código executável
 */
public class Main {
    public static void main(String[] args) {
        try {
            // Nome do arquivo de entrada (.gyh)
            String nomeArquivo = "Compilador-Linguagem-GYH/programa.gyh";
            
            // Cabeçalho do compilador
            System.out.println("╔════════════════════════════════════════╗");
            System.out.println("║   COMPILADOR DA LINGUAGEM GYH          ║");
            System.out.println("╚════════════════════════════════════════╝");
            
            // ========== FASE 1: ANÁLISE LÉXICA ==========
            System.out.println("\n>>> FASE 1: ANÁLISE LÉXICA");
            System.out.println("Arquivo: " + nomeArquivo);
            
            // Cria o analisador léxico e processa o arquivo
            AnalisadorLexico lexico = new AnalisadorLexico(nomeArquivo);

            // Exibe todos os tokens encontrados
            System.out.println("\n--- TOKENS ENCONTRADOS ---");
            for (Token t : lexico.getTokens()) {
                System.out.println(t);
            }

            // Verifica se houve erros léxicos
            if (!lexico.getErros().isEmpty()) {
                System.out.println("\n--- ERROS LÉXICOS ---");
                for (String erro : lexico.getErros()) {
                    System.out.println(erro);
                }
                System.out.println("\n❌ Análise léxica falhou! Corrija os erros antes de continuar.");
                // Se há erros léxicos, não continua para análise sintática
                return;
            } else {
                System.out.println("\n✓ Análise léxica concluída sem erros!");
            }

            // ========== FASE 2: ANÁLISE SINTÁTICA ==========
            System.out.println("\n>>> FASE 2: ANÁLISE SINTÁTICA");
            
            // Cria o analisador sintático passando os tokens do léxico
            AnalisadorSintatico sintatico = new AnalisadorSintatico(lexico.getTokens());
            // Inicia a análise sintática
            sintatico.analisar();
            
            // ========== RESULTADO FINAL ==========
            System.out.println("\n╔════════════════════════════════════════╗");
            if (!lexico.getErros().isEmpty() || sintatico.temErros()) {
                // Houve erros em alguma fase
                System.out.println("║   COMPILAÇÃO FALHOU                    ║");
                System.out.println("╚════════════════════════════════════════╝");
            } else {
                // Compilação bem-sucedida!
                System.out.println("║   COMPILAÇÃO BEM-SUCEDIDA!             ║");
                System.out.println("╚════════════════════════════════════════╝");
                System.out.println("\n✓ Programa pronto para próximas fases!");
            }

        } catch (IOException e) {
            // Tratamento de erro caso o arquivo não seja encontrado
            System.out.println("\n❌ Erro ao ler o arquivo: " + e.getMessage());
            System.out.println("Verifique se o arquivo 'programa.gyh' existe no diretório do projeto.");
        }
    }
}