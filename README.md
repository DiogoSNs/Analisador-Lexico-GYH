# 🔍 Analisador Léxico - Linguagem GYH

Um analisador léxico completo desenvolvido em Java para uma linguagem de programação personalizada chamada **GYH**. Este projeto implementa a primeira fase de um compilador, transformando código fonte em tokens estruturados.

## 📋 Sobre o Projeto

O analisador léxico é responsável por:
- Ler código fonte da linguagem GYH
- Identificar e classificar tokens (palavras-chave, variáveis, números, operadores)
- Detectar erros léxicos
- Gerar saída estruturada para análise sintática

### 🎯 Funcionalidades

- ✅ Reconhecimento de **12 palavras-chave**
- ✅ Suporte a **operadores aritméticos, relacionais e booleanos**
- ✅ Validação de **variáveis** (devem começar com letra minúscula)
- ✅ Reconhecimento de **números inteiros e reais**
- ✅ Suporte a **strings** entre aspas duplas
- ✅ **Tratamento de comentários** (linhas iniciadas com #)
- ✅ **Detecção de erros léxicos** com número da linha
- ✅ Token **EOF** para marcar fim do arquivo

## 🔤 Linguagem GYH - Especificação

### Palavras-Chave
```
DECLARAR | PROGRAMA | INTEGER | REAL | LER | IMPRIMIR
SE | SENAO | ENTAO | ENQTO | INICIO | FINAL
```

### Operadores
```
Aritméticos: + - * /
Relacionais: < <= > >= == !=
Booleanos: E OU
Atribuição: :=
```

### Delimitadores
```
[ ] ( ) :
```

### Exemplo de Código GYH
```gyh
[DECLARAR]
parametro: INTEGER
fatorial: INTEGER

[PROGRAMA]
# Calcula o fatorial de um número
LER parametro
fatorial := parametro
SE parametro == 0 ENTAO fatorial := 1
ENQTO parametro > 1
INICIO
    fatorial := fatorial * (parametro - 1)
    parametro := parametro - 1
FINAL
IMPRIMIR fatorial
```

## 🏗️ Estrutura do Projeto

```
src/
├── Token.java              # Classe que representa um token
├── TipoToken.java          # Enum com todos os tipos de tokens
├── LeitorArquivo.java      # Classe para leitura de arquivos
├── AnalisadorLexico.java   # Analisador léxico principal
├── Main.java               # Classe principal para execução
└── programa.gyh            # Arquivo de exemplo
```

## 🚀 Como Executar

### Pré-requisitos
- Java JDK 11 ou superior
- IDE Java (Eclipse, IntelliJ, VSCode) ou terminal

### Passos
1. Clone o repositório:
```bash
git clone https://github.com/seu-usuario/analisador-lexico-gyh.git
cd analisador-lexico-gyh
```

2. Compile os arquivos Java:
```bash
javac *.java
```

3. Crie um arquivo `programa.gyh` com código na linguagem GYH

4. Execute o analisador:
```bash
java Main
```

### Saída Esperada
```
=== TOKENS ENCONTRADOS ===
<DelimAbre, "[">
<PCDec, "DECLARAR">
<DelimFecha, "]">
<Var, "parametro">
<Delim, ":">
<PCInt, "INTEGER">
...
<EOF, "EOF">

Analise lexica concluida sem erros!
```

## 🔧 Funcionamento Técnico

### 1. **Análise por Regex**
O analisador utiliza expressões regulares para identificar padrões:
```java
String regex = "(\"[^\"]*\"|\\w+|\\d+\\.\\d+|\\d+|:=|<=|>=|==|!=|\\+|\\-|\\*|/|\\[|\\]|\\(|\\)|:|<|>)";
```

### 2. **Classificação de Tokens**
Cada lexema encontrado é classificado seguindo uma hierarquia:
1. Palavras-chave (mapa de consulta)
2. Strings entre aspas
3. Variáveis válidas (começam com minúscula)
4. Operadores booleanos
5. Números (inteiros/reais)
6. Símbolos e delimitadores

### 3. **Tratamento de Erros**
- Variáveis com inicial maiúscula → Erro léxico
- Caracteres não reconhecidos → Erro léxico
- Comentários são ignorados automaticamente

## 🎓 Conceitos Aplicados

- **Análise Léxica** - Primeira fase da compilação
- **Expressões Regulares** - Reconhecimento de padrões
- **Máquina de Estados** - Classificação de tokens
- **Tratamento de Erros** - Detecção e relatório de problemas
- **Design Patterns** - Separação de responsabilidades

## 📊 Exemplos de Uso

### Entrada Válida
```gyh
[DECLARAR] x: INTEGER
[PROGRAMA] x := 10
```

### Saída
```
<DelimAbre, "[">
<PCDec, "DECLARAR">
<DelimFecha, "]">
<Var, "x">
<Delim, ":">
<PCInt, "INTEGER">
<DelimAbre, "[">
<PCProg, "PROGRAMA">
<DelimFecha, "]">
<Var, "x">
<Atrib, ":=">
<NumInt, "10">
<EOF, "EOF">
```

### Entrada com Erro
```gyh
[DECLARAR] Parametro: INTEGER
```

### Saída
```
<DelimAbre, "[">
<PCDec, "DECLARAR">
<DelimFecha, "]">
<Delim, ":">
<PCInt, "INTEGER">
<EOF, "EOF">

=== ERROS LEXICOS ===
Erro Léxico na linha 1: variável inválida "Parametro" (deve começar com minúscula)
```

## 🤝 Contribuições

Contribuições são bem-vindas! Sinta-se à vontade para:
- Reportar bugs
- Sugerir melhorias
- Adicionar novos recursos
- Melhorar a documentação

## 📝 Licença

Este projeto é licenciado sob a MIT License - veja o arquivo [LICENSE](LICENSE) para detalhes.

## 👤 Autor

Desenvolvido como projeto acadêmico para a disciplina de Compiladores.

---

⭐ Se este projeto foi útil para você, considere dar uma estrela!
