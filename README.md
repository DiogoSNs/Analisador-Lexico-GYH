# 🔍 Analisador Léxico e Sintático - Linguagem GYH

Um **analisador léxico e sintático completo** desenvolvido em **Java** para a linguagem de programação personalizada **GYH**.  
Este projeto implementa as **duas primeiras fases de um compilador** — a análise léxica e a análise sintática — transformando o código fonte em tokens estruturados e validando sua conformidade com a gramática da linguagem.

---

## 📋 Sobre o Projeto

O compilador GYH é dividido em duas etapas principais:

### 🧩 **Analisador Léxico**

Responsável por:

- Ler o código fonte da linguagem GYH
- Identificar e classificar tokens (palavras-chave, variáveis, números, operadores, delimitadores)
- Ignorar comentários e espaços em branco
- Detectar e reportar **erros léxicos** com número de linha
- Gerar uma lista estruturada de tokens para o analisador sintático

### 🧠 **Analisador Sintático**

Responsável por:

- Validar a **estrutura gramatical** do código fonte
- Verificar a **ordem correta dos tokens** segundo a gramática GYH
- Detectar e reportar **erros sintáticos** indicando o ponto de falha
- Confirmar se o código está **sintaticamente correto**

---

## 🎯 Funcionalidades

- ✅ Reconhecimento de **12 palavras-chave**
- ✅ Suporte a **operadores aritméticos, relacionais e booleanos**
- ✅ Validação de **variáveis** (devem começar com letra minúscula)
- ✅ Reconhecimento de **números inteiros e reais**
- ✅ Suporte a **strings** entre aspas duplas
- ✅ **Tratamento de comentários** (linhas iniciadas com `#`)
- ✅ **Detecção de erros léxicos e sintáticos** com número da linha
- ✅ **Token EOF** para marcar fim do arquivo
- ✅ **Validação sintática completa** com base na gramática GYH

---

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

---

## 💡 Exemplo de Código GYH

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

---

## 🏗️ Estrutura do Projeto

Desenvolvido no **Eclipse IDE** com a seguinte arquitetura:

```
AnalisadorLex/
├── 📁 JRE System Library [JavaSE-21]
├── 📁 src/
│   └── 📦 (default package)
│       ├── 📄 AnalisadorLexico.java       # Analisador léxico principal
│       ├── 📄 AnalisadorSintatico.java    # Novo analisador sintático (descendente recursivo)
│       ├── 📄 LeitorArquivo.java          # Classe para leitura de arquivos
│       ├── 📄 Token.java                  # Classe que representa um token
│       ├── 📄 TipoToken.java              # Enum com todos os tipos de tokens
│       └── 📄 Main.java                   # Classe principal para execução
└── 📄 programa.gyh                        # Arquivo de exemplo
```

---

## 🚀 Como Executar

### Pré-requisitos

- Java JDK 21 (ou superior)
- Eclipse IDE ou VS Code com extensão Java
- Terminal/Prompt de comando

### Passos

1. Clone o repositório:

```bash
git clone https://github.com/DiogoSNs/Analisador-Lexico-GYH.git
cd analisador-lexico-gyh
```

2. **No Eclipse:**

   - Import → Existing Projects into Workspace
   - Selecione a pasta do projeto
   - Run `Main.java`

3. **No VS Code:**

   - Abra a pasta do projeto
   - Instale a extensão _Java Extension Pack_
   - Execute `Main.java`
   - Certifique-se de que `programa.gyh` está na raiz

4. **Ou via terminal:**

```bash
cd src
javac *.java
java Main
```

---

## 📤 Saída Esperada

### 🧩 Análise Léxica

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

Análise léxica concluída sem erros!
```

### 🧠 Análise Sintática

```
=== INÍCIO DA ANÁLISE SINTÁTICA ===
Programa reconhecido com sucesso!
Análise sintática concluída sem erros!
```

---

## ⚙️ Funcionamento Técnico

### 1. **Analisador Léxico (Regex + FSM)**

Utiliza expressões regulares para identificar padrões de tokens:

```java
String regex = "(\"[^\"]*\"|\w+|\d+\.\d+|\d+|:=|<=|>=|==|!=|\+|\-|\*|/|\[|\]|\(|\)|:|<|>)";
```

### 2. **Analisador Sintático (Descendente Recursivo)**

Implementa funções que refletem diretamente as produções da gramática GYH.
Cada método consome tokens e valida as regras sintáticas esperadas.

---

## 🎓 Conceitos Aplicados

- **Análise Léxica** – Identificação de tokens
- **Análise Sintática** – Validação da estrutura gramatical
- **Expressões Regulares** – Reconhecimento de padrões
- **Máquina de Estados** – Automação do reconhecimento léxico
- **Parser Descendente Recursivo** – Implementação sintática direta
- **Tratamento de Erros** – Detecção e mensagens detalhadas
- **Separação de Responsabilidades** – Arquitetura modular

---

## 🤝 Contribuições

Contribuições são bem-vindas! Você pode:

- Reportar bugs
- Sugerir melhorias
- Adicionar novos recursos (ex: analisador semântico)
- Melhorar a documentação

---

## 👤 Autor

**Diogo Augusto Silvério Nascimento**  
Desenvolvido como projeto acadêmico para a disciplina de **Compiladores**.

---

⭐ Se este projeto foi útil para você, considere deixar uma estrela no repositório!
