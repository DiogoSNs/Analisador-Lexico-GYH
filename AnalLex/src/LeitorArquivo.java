import java.io.*;

public class LeitorArquivo {
    private BufferedReader reader;
    private int linha = 1;

    // Abre o arquivo para leitura
    public LeitorArquivo(String nomeArquivo) throws FileNotFoundException {
        reader = new BufferedReader(new FileReader(nomeArquivo));
    }

    // Le uma linha do arquivo e retorna null quando chegar no fim
    public String lerLinha() throws IOException {
        String linhaLida = reader.readLine();
        if (linhaLida != null) {
            linha++; // incrementa o contador de linha apenas quando le uma linha
            return linhaLida;
        }
        return null;
    }

    // Retorna o numero da linha atual (para controle de erros)
    public int getLinha() {
        return linha;
    }

    // Fecha o arquivo quando terminar de usar
    public void fechar() throws IOException {
        reader.close();
    }
}