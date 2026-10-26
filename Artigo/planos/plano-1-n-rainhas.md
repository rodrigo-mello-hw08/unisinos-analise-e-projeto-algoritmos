# Planos de Execução — Análise do Impacto das Estratégias de Poda

**Projeto:** Análise do Impacto das Estratégias de Poda no Desempenho de Algoritmos de Busca: Um Estudo sobre os Problemas da Mochila e das N Rainhas

**Tecnologias:** Java 21, Maven, JUnit 5 (JUnit Jupiter) e exportação de resultados em CSV.

## Plano 1 — Implementação dos algoritmos para o problema das N rainhas

### 1. Objetivo

Implementar três variantes de busca para o problema das N rainhas, permitindo comparar o desempenho de uma busca exaustiva com estratégias de poda por restrição e por limite superior.

Todas as variantes deverão contar **todas as soluções válidas** de uma instância, de forma que os resultados possam ser comparados diretamente.

### 2. Modelagem do problema

Representar o tabuleiro utilizando um vetor `int[] queens`, no qual:

- O índice representa a linha do tabuleiro.
- O valor armazenado representa a coluna ocupada pela rainha naquela linha.
- O valor `queens[row] = col` indica que uma rainha foi posicionada na posição `(row, col)`.

Uma configuração é válida quando nenhuma rainha compartilha uma coluna ou diagonal com outra rainha.

### 3. Implementação dos algoritmos

#### 3.1. Força bruta

Criar a classe `NQueensBruteForce`.

Responsabilidades:

1. Gerar todas as configurações possíveis, posicionando uma rainha por linha.
2. Não verificar conflitos durante a construção parcial do tabuleiro.
3. Verificar todas as restrições somente quando uma configuração completa for construída.
4. Contabilizar todas as configurações examinadas e todas as configurações completas rejeitadas.
5. Contar e retornar todas as soluções válidas.

Essa variante servirá como referência de busca exaustiva. Seu espaço de busca possui até \(N^N\) configurações completas.

#### 3.2. Backtracking

Criar a classe `NQueensBacktracking`.

Responsabilidades:

1. Posicionar as rainhas recursivamente, uma linha por vez.
2. Antes de posicionar uma rainha, verificar se a coluna está ocupada.
3. Verificar se a nova posição apresenta conflito diagonal com alguma rainha já posicionada.
4. Descartar imediatamente posições inválidas, sem continuar explorando o ramo.
5. Contabilizar as tentativas de posicionamento e as podas realizadas por coluna e diagonal.
6. Contar todas as soluções válidas.

Para otimizar as verificações, utilizar estruturas auxiliares, como vetores booleanos para colunas e diagonais.

#### 3.3. Branch and Bound

Criar a classe `NQueensBranchAndBound`.

Responsabilidades:

1. Reutilizar a representação do tabuleiro e as verificações de restrições.
2. Calcular um limite superior para a quantidade de rainhas que ainda podem ser posicionadas.
3. Considerar uma linha restante potencialmente viável quando existe pelo menos uma coluna compatível com as rainhas já posicionadas.
4. Calcular o limite superior como:

   `rainhasJáPosicionadas + quantidadeDeLinhasRestantesComPosiçõesViáveis`

5. Descartar o ramo quando o limite superior for menor que `N`, pois não será possível completar uma solução.
6. Contabilizar separadamente as podas por restrição e as podas por limite superior.
7. Contar todas as soluções válidas que não forem descartadas por um limite seguro.

Esse limite é uma estimativa otimista: ele desconsidera possíveis conflitos entre as rainhas que ainda serão posicionadas, portanto não subestima a quantidade máxima que poderia ser alcançada.

**Nota metodológica:** N rainhas é naturalmente um problema de satisfação de restrições. Para que essa terceira variante seja distinta do backtracking tradicional, o limite superior deverá ser implementado e medido explicitamente. Não basta renomear uma implementação comum de backtracking como Branch and Bound.

### 4. Estrutura sugerida de classes

```text
src/main/java/
└── nqueens/
    ├── NQueensSolver.java
    ├── NQueensResult.java
    ├── NQueensBruteForce.java
    ├── NQueensBacktracking.java
    └── NQueensBranchAndBound.java

└── metrics/
    └── SearchMetrics.java
```

`NQueensSolver` define o contrato comum de execução. `NQueensResult` armazena o total de soluções e as métricas da execução. `SearchMetrics` concentra os contadores e o tempo de execução.

### 5. Métricas obrigatórias

Cada execução deverá registrar:

- **Tempo de execução:** tempo medido em nanossegundos com `System.nanoTime()`.
- **Nós explorados:** quantidade de estados de busca efetivamente visitados.
- **Tentativas de posicionamento:** quantidade de posições `(linha, coluna)` consideradas durante a busca.
- **Podas por coluna:** quantidade de ramos rejeitados por conflito de coluna.
- **Podas por diagonal:** quantidade de ramos rejeitados por conflito diagonal.
- **Podas por limite:** quantidade de ramos descartados pelo limite superior.
- **Configurações completas rejeitadas:** quantidade de tabuleiros completos inválidos na variante de força bruta.
- **Avaliações do limite:** quantidade de vezes em que o Branch and Bound calculou seu limite superior.

A força bruta não realiza podas antecipadas. Nesse caso, suas podas por restrição devem ser registradas como zero, e as configurações inválidas devem ser contabilizadas separadamente. Assim, a ausência de poda não será confundida com a ausência de configurações inválidas.

### 6. Critérios de conclusão

- As três variantes compilam utilizando Java 21.
- Todas retornam a mesma quantidade de soluções para as instâncias em que forem executadas.
- As soluções respeitam todas as restrições do problema.
- Cada métrica possui uma definição consistente entre as implementações.
- Os resultados podem ser exportados para CSV.
- A variante Branch and Bound contabiliza separadamente o custo das avaliações do limite superior.

### 7. Entregável

Um módulo Java funcional com três algoritmos de busca, implementação de métricas, testes de correção e suporte para execução automatizada dos experimentos.

---
