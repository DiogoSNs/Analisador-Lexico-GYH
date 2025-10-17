# Gramática GYH Transformada

## Gramática sem Recursão à Esquerda e Não-Determinismo

### Programa Principal
```
Programa → '[' 'DECLARAR' ']' ListaDeclaracoes '[' 'PROGRAMA' ']' ListaComandos
```

### Declarações
```
ListaDeclaracoes → Declaracao ListaDeclaracoes'
ListaDeclaracoes' → Declaracao ListaDeclaracoes' | ε

Declaracao → VARIAVEL ':' TipoVar

TipoVar → 'INTEGER' | 'REAL'
```

### Expressões Aritméticas
```
ExpressaoAritmetica → TermoAritmetico ExpressaoAritmetica'
ExpressaoAritmetica' → '+' TermoAritmetico ExpressaoAritmetica'
                     | '-' TermoAritmetico ExpressaoAritmetica'
                     | ε

TermoAritmetico → FatorAritmetico TermoAritmetico'
TermoAritmetico' → '*' FatorAritmetico TermoAritmetico'
                 | '/' FatorAritmetico TermoAritmetico'
                 | ε

FatorAritmetico → NUMINT 
                | NUMREAL 
                | VARIAVEL 
                | '(' ExpressaoAritmetica ')'
```

### Expressões Relacionais
```
ExpressaoRelacional → TermoRelacional ExpressaoRelacional'
ExpressaoRelacional' → OperadorBooleano TermoRelacional ExpressaoRelacional'
                     | ε

TermoRelacional → ExpressaoAritmetica OP_REL ExpressaoAritmetica 
                | '(' ExpressaoRelacional ')'

OperadorBooleano → 'E' | 'OU'
```

### Comandos
```
ListaComandos → Comando ListaComandos'
ListaComandos' → Comando ListaComandos' | ε

Comando → ComandoAtribuicao 
        | ComandoEntrada 
        | ComandoSaida 
        | ComandoCondicao 
        | ComandoRepeticao 
        | SubAlgoritmo

ComandoAtribuicao → VARIAVEL ':=' ExpressaoAritmetica

ComandoEntrada → 'LER' VARIAVEL

ComandoSaida → 'IMPRIMIR' ComandoSaida'
ComandoSaida' → VARIAVEL | CADEIA

ComandoCondicao → 'SE' ExpressaoRelacional 'ENTAO' Comando ComandoCondicao'
ComandoCondicao' → 'SENAO' Comando | ε

ComandoRepeticao → 'ENQTO' ExpressaoRelacional Comando

SubAlgoritmo → 'INICIO' ListaComandos 'FINAL'
```

---

## Resumo das Transformações Aplicadas

### ✅ Recursão à Esquerda Eliminada em:
- ExpressaoAritmetica → ExpressaoAritmetica'
- TermoAritmetico → TermoAritmetico'
- ExpressaoRelacional → ExpressaoRelacional'
- ListaDeclaracoes → ListaDeclaracoes'
- ListaComandos → ListaComandos'

### ✅ Não-Determinismo Eliminado em:
- ComandoSaida (fatorado 'IMPRIMIR')
- ComandoCondicao (fatorado parte obrigatória)
- ListaDeclaracoes e ListaComandos (uso de linha vazia ε)

### ✅ Ambiguidade:
- Já estava correta (precedência de operadores mantida)

---

## Novos Não-Terminais Criados
- `ExpressaoAritmetica'` (linha)
- `TermoAritmetico'` (linha)
- `ExpressaoRelacional'` (linha)
- `ListaDeclaracoes'` (linha)
- `ListaComandos'` (linha)
- `ComandoSaida'` (continuação)
- `ComandoCondicao'` (parte opcional)

**Observação:** O símbolo `ε` (epsilon) representa a produção vazia.
