# 🧪 Arquivos de Teste de Erro - Corrigidos

## ❌ Erro Sintático 1 (erro_sin1.gyh)

```gyh
# Teste: Uso de = ao invés de :=
[DECLARAR]
x: INTEGER

[PROGRAMA]
x = 10
```

**Resultado Esperado:**

```
=== ERROS LÉXICOS ===
Erro Léxico na linha 5: Desconhecido "="

❌ Análise interrompida devido a erros léxicos.
```

**Explicação:** O erro acontece na **análise léxica** porque `=` não é um token válido na linguagem GYH. A linguagem só aceita `:=` para atribuição. Portanto, este é realmente um **erro léxico**, não sintático!

---

## ❌ Erro Sintático 2 (erro_sin2.gyh)

```gyh
# Teste: Falta ] após PROGRAMA
[DECLARAR]
x: INTEGER

[PROGRAMA
x := 10
IMPRIMIR x
```

**Resultado Esperado:**

```
=== TOKENS ENCONTRADOS ===
<DelimAbre, "[">
<PCDec, "DECLARAR">
<DelimFecha, "]">
<Var, "x">
<Delim, ":">
<PCInt, "INTEGER">
<DelimAbre, "[">
<PCProg, "PROGRAMA">
<Var, "x">
<Atrib, ":=">
<NumInt, "10">
<PCImprimir, "IMPRIMIR">
<Var, "x">
<EOF, "EOF">

✅ Análise léxica concluída sem erros!

=== INICIANDO ANÁLISE SINTÁTICA ===

=== ERROS SINTÁTICOS ENCONTRADOS ===
Erro Sintático: Esperado ']' após 'PROGRAMA'. Encontrado: 'x'

❌ COMPILAÇÃO FALHOU!
   Corrija os erros e tente novamente.
```

**Explicação:** O analisador esperava `]` após `PROGRAMA`, mas encontrou `x`. A mensagem está correta! O `x` que ele encontrou é o início do comando `x := 10`.

---

## ❌ Erro Sintático 3 (erro_sin3.gyh)

```gyh
# Teste: Falta : na declaração
[DECLARAR]
x INTEGER

[PROGRAMA]
x := 10
```

**Resultado Esperado:**

```
=== ERROS SINTÁTICOS ENCONTRADOS ===
Erro Sintático: Esperado ':' após nome da variável. Encontrado: 'INTEGER'
```

---

## ❌ Erro Sintático 4 (erro_sin4.gyh)

```gyh
# Teste: Falta ENTAO no comando SE
[DECLARAR]
x: INTEGER

[PROGRAMA]
x := 5
SE x > 5 IMPRIMIR "maior"
```

**Resultado Esperado:**

```
=== ERROS SINTÁTICOS ENCONTRADOS ===
Erro Sintático: Esperado palavra-chave 'ENTAO' após condição do SE. Encontrado: 'IMPRIMIR'
```

---

## ❌ Erro Sintático 5 (erro_sin5.gyh)

```gyh
# Teste: Falta FINAL no bloco INICIO
[DECLARAR]
x: INTEGER

[PROGRAMA]
INICIO
    x := 10
    IMPRIMIR x
```

**Resultado Esperado:**

```
=== ERROS SINTÁTICOS ENCONTRADOS ===
Erro Sintático: Esperado palavra-chave 'FINAL' para fechar bloco. Encontrado: 'EOF'
```

---

## ❌ Erro Léxico 2 (erro_lex2.gyh)

```gyh
# Teste: Variável com inicial maiúscula
[DECLARAR]
Nome: INTEGER

[PROGRAMA]
Nome := 10
```

**Resultado Esperado:**

```
=== ERROS LÉXICOS ===
Erro Léxico na linha 2: Desconhecido "Nome"
Erro Léxico na linha 5: Desconhecido "Nome"

❌ Análise interrompida devido a erros léxicos.
```

---

## ❌ Erro Léxico 3 (erro_lex3.gyh)

```gyh
# Teste: Caracteres inválidos
[DECLARAR]
x: INTEGER

[PROGRAMA]
x := 10 @ 5
```

**Resultado Esperado:**

```
=== ERROS LÉXICOS ===
Erro Léxico na linha 5: Desconhecido "@"

❌ Análise interrompida devido a erros léxicos.
```

---

## ✅ Programa Correto para Comparação (teste_correto.gyh)

```gyh
# Programa correto para comparação
[DECLARAR]
x: INTEGER
y: INTEGER

[PROGRAMA]
x := 10
y := 20
SE x < y ENTAO IMPRIMIR "x eh menor"
```

**Resultado Esperado:**

```
===============================================
     COMPILADOR DA LINGUAGEM GYH
===============================================

=== TOKENS ENCONTRADOS ===
<DelimAbre, "[">
<PCDec, "DECLARAR">
<DelimFecha, "]">
<Var, "x">
<Delim, ":">
<PCInt, "INTEGER">
<Var, "y">
<Delim, ":">
<PCInt, "INTEGER">
<DelimAbre, "[">
<PCProg, "PROGRAMA">
<DelimFecha, "]">
<Var, "x">
<Atrib, ":=">
<NumInt, "10">
<Var, "y">
<Atrib, ":=">
<NumInt, "20">
<PCSe, "SE">
<Var, "x">
<OpRelMenor, "<">
<Var, "y">
<PCEntao, "ENTAO">
<PCImprimir, "IMPRIMIR">
<Cadeia, ""x eh menor"">
<EOF, "EOF">

✅ Análise léxica concluída sem erros!

=== INICIANDO ANÁLISE SINTÁTICA ===

=== ANÁLISE SINTÁTICA CONCLUÍDA COM SUCESSO ===
✅ O programa está sintaticamente correto!

===============================================
✅ COMPILAÇÃO BEM-SUCEDIDA!
   O programa está léxica e sintaticamente correto.
===============================================
```

---

## 📊 Resumo dos Tipos de Erro

| Tipo de Erro       | Fase              | Exemplo                              |
| ------------------ | ----------------- | ------------------------------------ |
| **Erro Léxico**    | Análise Léxica    | `=`, `@`, `Nome` (maiúscula)         |
| **Erro Sintático** | Análise Sintática | Falta `:=`, falta `]`, falta `ENTAO` |

---

## 💡 Entendendo a Diferença

### Erro Léxico (Analisador Léxico)

- **O que é:** Token desconhecido ou inválido
- **Exemplos:** `=` sozinho, `@`, variável com maiúscula
- **Quando detectar:** O analisador léxico não consegue formar um token válido

### Erro Sintático (Analisador Sintático)

- **O que é:** Sequência de tokens inválida
- **Exemplos:** Falta `:=`, falta `]`, falta `ENTAO`
- **Quando detectar:** Os tokens são válidos, mas estão na ordem errada
