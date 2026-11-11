# Planos de Execução — Análise do Impacto das Estratégias de Poda

**Projeto:** Análise do Impacto das Estratégias de Poda no Desempenho de Algoritmos de Busca: um estudo sobre N Rainhas, Mochila e Caixeiro-Viajante

**Tecnologias:** Java 21, Maven, JUnit 5 (JUnit Jupiter) e exportação de resultados em CSV.

## Plano 2b — Implementação dos algoritmos para o Problema do Caixeiro-Viajante

### 1. Objetivo

Implementar três variantes de busca para o Problema do Caixeiro-Viajante (TSP), comparando força bruta, backtracking e branch and bound. Todas deverão encontrar o custo mínimo de uma rota que parte de uma cidade, visita todas as demais exatamente uma vez e retorna à cidade inicial.

### 2. Modelagem do problema

Criar uma instância TSP contendo uma matriz quadrada de distâncias entre cidades. Para este experimento, o grafo será completo, não direcionado e simétrico:

- A distância entre duas cidades será um inteiro não negativo.
- A distância de uma cidade para si mesma será zero.
- A matriz será simétrica: `distancias[i][j] == distancias[j][i]`.
- Uma rota será representada por uma sequência de índices de cidades, começando e terminando na cidade `0`.
- Cada cidade diferente da origem deverá aparecer uma única vez na rota.

Validar que a instância possui pelo menos uma cidade, que a matriz é quadrada e que todas as distâncias são não negativas e simétricas. Para uma instância de uma cidade, a rota será `[0, 0]` e seu custo será zero.

### 3. Implementação dos algoritmos

#### 3.1. Força bruta

Criar a classe `TspBruteForce`.

Responsabilidades:

1. Fixar a cidade `0` como origem e gerar todas as permutações das outras cidades.
2. Fechar cada rota retornando à origem e calcular seu custo total.
3. Avaliar todas as rotas completas, sem podar ramos durante a construção.
4. Guardar a rota de menor custo encontrada.
5. Contabilizar os estados, as tentativas de escolha de cidade e as rotas completas avaliadas.

Fixar a origem evita enumerar rotações equivalentes. Como o grafo é simétrico, a rota e sua reversa têm o mesmo custo; para reduzir duplicação, considerar uma única orientação impondo que a segunda cidade tenha índice menor que o da última. Essa regra deverá ser aplicada igualmente à força bruta, ao backtracking e ao branch and bound.

O número de rotas avaliadas será `(n - 1)! / 2` para `n > 2`; para uma ou duas cidades, tratar o caso diretamente sem divisão por dois.

#### 3.2. Backtracking

Criar a classe `TspBacktracking`.

Responsabilidades:

1. Construir a rota recursivamente, partindo da cidade `0`.
2. Em cada nível, considerar somente cidades ainda não visitadas.
3. Não aplicar limites de custo para podar ramos; a poda desta variante será pela restrição de não repetir cidades.
4. Ao completar a rota, incluir o custo de retorno à origem e atualizar a melhor solução.
5. Aplicar a mesma regra de orientação usada pela força bruta para evitar contar a rota reversa.
6. Contabilizar tentativas, podas por cidade já visitada e rotas completas avaliadas.

Essa variante deve gerar apenas prefixos válidos, enquanto a força bruta permuta e avalia as rotas completas. Não deve ser rotulada como branch and bound, pois não calcula um limite para descartar ramos com base na melhor solução conhecida.

#### 3.3. Branch and Bound

Criar a classe `TspBranchAndBound`.

Responsabilidades:

1. Reutilizar a construção incremental da rota e a restrição de cidades não visitadas.
2. Manter o menor custo de rota completa encontrado até o momento.
3. Calcular um limite inferior seguro para o custo total de cada prefixo.
4. Podar o ramo quando o custo parcial mais o limite inferior for maior ou igual ao melhor custo conhecido.
5. Aplicar a mesma regra de orientação das demais variantes.
6. Contabilizar separadamente podas por cidade já visitada, podas por limite e avaliações do limite.

Calcular o limite inferior a partir da árvore geradora mínima (MST) das cidades ainda não visitadas, somada à menor conexão da cidade atual com uma cidade não visitada e à menor conexão de uma cidade não visitada com a origem. Quando não houver cidades restantes, o limite será apenas o custo da conexão da cidade atual à origem. Para o caso de uma cidade restante, as duas conexões são entre a cidade atual, essa cidade e a origem.

O limite deverá ser otimista: nunca poderá exceder o custo de uma conclusão ótima possível do prefixo. A construção da MST e das conexões mínimas deverá usar apenas cidades ainda disponíveis e as distâncias da instância. Contabilizar cada cálculo do limite, inclusive quando o ramo é podado.

### 4. Estrutura sugerida de classes

```text
src/
├── tsp/
│   ├── domain/
│   │   ├── TspInstance.java
│   │   └── TspResult.java
│   └── solver/
│       ├── TspSolver.java
│       ├── TspSupport.java
│       ├── TspBruteForce.java
│       ├── TspBacktracking.java
│       └── TspBranchAndBound.java
├── metrics/
│   └── SearchMetrics.java
└── reporting/
    └── SearchMetricsCsv.java
```

`TspSolver` definirá um contrato comum para executar uma instância. `TspResult` armazenará o custo ótimo, a rota correspondente e as métricas. Os tipos do TSP não deverão depender de `NQueensResult`.

### 5. Reutilização de métricas e CSV

Reutilizar `metrics.SearchMetrics` e `reporting.SearchMetricsCsv`, ampliando-os sem remover os dados usados pelos módulos existentes.

Registrar para cada execução:

- **Tempo:** nanossegundos medidos com `System.nanoTime()`.
- **Nós explorados:** estados de busca visitados.
- **Tentativas:** cidades consideradas como próxima escolha.
- **Podas por cidade visitada:** tentativas descartadas por violarem a restrição de não repetir cidades.
- **Podas por limite:** ramos descartados pelo limite inferior do branch and bound.
- **Avaliações do limite:** cálculos do limite inferior.
- **Rotas completas avaliadas:** rotas cujo custo total foi calculado.
- **Resultado principal:** custo da rota ótima.

Adicionar a `SearchMetrics` um contador para podas por cidade visitada, mantendo os contadores existentes de coluna, diagonal, limite, configurações inválidas e avaliações de limite para compatibilidade com N-Rainhas e Mochila.

Ampliar `SearchMetricsCsv` com uma coluna `resultado_principal`, destinada a guardar a quantidade de soluções no N-Rainhas, o valor ótimo na Mochila e o custo ótimo no TSP. Manter as colunas de métricas compartilhadas existentes e acrescentar a métrica de podas por cidade visitada. Atualizar a operação de adição de execução para receber um valor de resultado principal sem restringi-lo a uma quantidade de soluções. Para métricas não aplicáveis a um problema, registrar zero.

### 6. Instâncias experimentais

Gerar matrizes simétricas de distâncias inteiras não negativas com semente fixa, garantindo reprodutibilidade. Usar exatamente a mesma instância nas três variantes.

| Cidades | Uso | Execuções |
|---:|---|---|
| 1 e 2 | Casos de borda e validação | Todas as variantes |
| 4, 6 e 8 | Comparação de correção e métricas | Todas as variantes |
| 10 | Observação do custo de busca | Força bruta apenas se couber no orçamento definido; backtracking e branch and bound conforme viabilidade |

As distâncias, a semente e a identificação da instância deverão ser registradas no relatório experimental. Se a força bruta for omitida em algum tamanho por custo computacional, registrar explicitamente a omissão.

### 7. Testes e critérios de conclusão

- Validar a instância: matriz vazia, não quadrada, assimétrica ou com distância negativa deve ser rejeitada.
- Testar instâncias de uma e duas cidades e uma instância pequena com custo ótimo conhecido.
- Verificar que toda rota retornada começa e termina na origem, visita cada cidade uma única vez e tem custo igual ao custo informado.
- Confirmar que os três algoritmos retornam o mesmo custo ótimo para cada instância testada.
- Testar empates: rotas ótimas diferentes podem existir, mas todas devem ter o custo ótimo correto.
- Comparar contadores em casos pequenos: força bruta não deve podar prefixos; backtracking não deve usar poda por limite; branch and bound deve registrar avaliações e, quando aplicável, podas por limite.
- Confirmar que o limite inferior nunca elimina uma rota ótima nas instâncias de teste.
- Confirmar que o CSV mantém cabeçalho e dados existentes, inclui o resultado principal e registra corretamente o custo ótimo do TSP.
- Compilar com Java 21 e executar os testes automatizados com JUnit 5.

### 8. Entregável

Um módulo Java para o Problema do Caixeiro-Viajante com três variantes de busca, resultado contendo rota e custo ótimo, métricas compartilhadas, exportação compatível em CSV e testes de correção para instâncias pequenas.

---
