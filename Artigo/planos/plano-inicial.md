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

## Plano 2 — Implementação dos algoritmos para o problema da mochila 0/1

### 1. Objetivo

Implementar três variantes de busca para o problema clássico da mochila 0/1, comparando o custo de uma busca exaustiva, a poda por excesso de capacidade e a poda por limite superior.

O objetivo de todas as implementações será encontrar uma combinação de itens que maximize o valor total sem ultrapassar a capacidade da mochila.

### 2. Modelagem do problema

Criar a classe `KnapsackItem`, que deverá armazenar:

- Identificador do item.
- Peso do item.
- Valor do item.

Criar uma representação da instância contendo a lista de itens e a capacidade máxima da mochila.

Cada item poderá ser selecionado uma única vez ou não ser selecionado.

Os pesos devem ser positivos, e os valores devem ser não negativos ou positivos, conforme a convenção definida para o experimento. Utilizar tipos numéricos adequados para evitar estouro no cálculo de pesos e valores acumulados.

### 3. Implementação dos algoritmos

#### 3.1. Força bruta

Criar a classe `KnapsackBruteForce`.

Responsabilidades:

1. Enumerar todos os subconjuntos possíveis de itens.
2. Considerar, para cada item, as alternativas de inclusão e exclusão.
3. Não interromper a exploração de um ramo por excesso de capacidade.
4. Avaliar a viabilidade dos subconjuntos completos.
5. Identificar e armazenar a combinação viável com maior valor total.
6. Contabilizar os nós explorados, as decisões sobre os itens e os subconjuntos completos avaliados.

O espaço de busca possui \(2^n\) subconjuntos, sendo `n` o número de itens.

#### 3.2. Backtracking

Criar a classe `KnapsackBacktracking`.

Responsabilidades:

1. Construir a solução recursivamente, decidindo se cada item será incluído ou excluído.
2. Manter o peso e o valor acumulados durante a exploração.
3. Interromper um ramo assim que o peso acumulado ultrapassar a capacidade.
4. Atualizar a melhor solução viável encontrada.
5. Contabilizar separadamente as podas por excesso de capacidade.

Como os pesos serão positivos, acrescentar outros itens nunca tornará viável um ramo cujo peso atual já ultrapassa a capacidade.

#### 3.3. Branch and Bound

Criar a classe `KnapsackBranchAndBound`.

Responsabilidades:

1. Ordenar os itens por razão valor/peso em ordem decrescente, utilizando uma cópia da lista para não alterar os dados compartilhados pelos experimentos.
2. Aplicar a poda por excesso de capacidade.
3. Calcular um limite superior para o valor máximo que poderá ser alcançado em cada ramo.
4. Calcular esse limite utilizando a relaxação da mochila fracionária: os itens restantes podem ser adicionados integralmente e, se necessário, uma fração do próximo item será utilizada apenas para estimar o limite.
5. Manter o melhor valor viável encontrado até o momento.
6. Descartar um ramo quando seu limite superior não superar o melhor valor já encontrado.
7. Contabilizar separadamente as podas por capacidade e por limite superior.

Para evitar podas incorretas devido a erros de ponto flutuante, implementar o cálculo do limite com uma margem numérica conservadora ou outra comparação segura.

### 4. Estrutura sugerida de classes

```text
src/main/java/
└── knapsack/
    ├── KnapsackItem.java
    ├── KnapsackInstance.java
    ├── KnapsackResult.java
    ├── KnapsackSolver.java
    ├── KnapsackBruteForce.java
    ├── KnapsackBacktracking.java
    └── KnapsackBranchAndBound.java

└── metrics/
    └── SearchMetrics.java
```

### 5. Métricas obrigatórias

Registrar para cada algoritmo:

- **Tempo de execução:** tempo medido em nanossegundos.
- **Nós explorados:** quantidade de estados efetivamente visitados.
- **Tentativas de decisão sobre itens:** quantidade de alternativas de inclusão e exclusão consideradas.
- **Podas por capacidade:** quantidade de ramos descartados por ultrapassar o peso máximo.
- **Podas por limite:** quantidade de ramos descartados porque o limite superior não supera a melhor solução conhecida.
- **Avaliações do limite:** quantidade de cálculos do limite superior.
- **Subconjuntos completos avaliados:** quantidade de combinações completas consideradas para verificar a viabilidade e o valor.
- **Melhor valor encontrado:** valor da solução ótima.
- **Peso da solução:** peso total dos itens selecionados.

A força bruta deverá registrar zero para as podas antecipadas por capacidade e por limite. Subconjuntos inviáveis serão contabilizados separadamente, pois serão avaliados sem interrupção antecipada.

### 6. Geração das instâncias experimentais

Gerar conjuntos de itens utilizando sementes fixas para garantir reprodutibilidade.

Configuração inicial sugerida:

| Bateria | Número de itens | Objetivo |
|---|---|---|
| Pequena | 8, 10 e 12 | Validar o comportamento dos algoritmos e comparar todas as variantes. |
| Média | 14, 16 e 18 | Observar o crescimento do espaço de busca. |
| Grande | 20, 22 e 24 | Avaliar a escalabilidade e o efeito das podas em instâncias maiores. |

Para cada instância, utilizar a mesma lista de itens e a mesma capacidade nas três variantes. A capacidade poderá corresponder a 30%, 50% ou 70% da soma dos pesos, permitindo analisar diferentes níveis de restrição.

O número de instâncias e repetições deverá ser definido para manter o experimento viável. Em instâncias grandes, executar força bruta apenas quando couber no orçamento de execução e registrar explicitamente as execuções omitidas por custo computacional.

### 7. Critérios de conclusão

- Os três algoritmos encontram o mesmo valor ótimo para cada instância avaliada.
- Nenhuma solução ultrapassa a capacidade máxima.
- O Branch and Bound contabiliza separadamente os dois tipos de poda.
- Os dados de entrada são idênticos entre as variantes comparadas.
- O custo da ordenação dos itens e das avaliações dos limites é tratado de forma consistente na medição.
- Os resultados são exportados em CSV para análise posterior.

### 8. Entregável

Um módulo Java contendo as três implementações para a mochila 0/1, geração reprodutível de instâncias, métricas de busca e exportação dos resultados experimentais.

---

## Plano 3 — Testes automatizados com JUnit 5 e baterias experimentais

### 1. Objetivo

Implementar testes automatizados para validar a correção dos algoritmos, garantir a consistência das métricas e coletar dados experimentais sobre o impacto das estratégias de poda nos problemas das N rainhas e da mochila 0/1.

Os testes serão desenvolvidos utilizando JUnit Jupiter, compatível com Java 21.

### 2. Separação entre testes de correção e experimentos

Organizar os testes em duas categorias.

**Testes de correção:**

- Validar resultados esperados em instâncias conhecidas.
- Comparar as soluções retornadas pelos diferentes algoritmos.
- Verificar a validade das soluções.
- Confirmar que os contadores são consistentes e não apresentam valores negativos.

**Testes experimentais de desempenho:**

- Executar baterias de instâncias de diferentes tamanhos.
- Coletar tempo de execução, nós explorados, tentativas e podas.
- Repetir as medições com dados reprodutíveis.
- Exportar os resultados para CSV.

Os testes experimentais não deverão falhar simplesmente porque uma execução foi mais lenta que outra. O tempo de execução será registrado e analisado, e não utilizado como uma asserção rígida de desempenho.

Utilizar `System.nanoTime()` para medir o tempo e executar um aquecimento prévio da JVM. A geração das instâncias e a gravação do CSV deverão ficar fora da região cronometrada.

### 3. Baterias de testes para N rainhas

#### 3.1. Bateria pequena

**Instâncias:** `N = 4, 5 e 6`.

**Algoritmos:** força bruta, backtracking e Branch and Bound.

**Objetivos:**

- Validar a correção dos três algoritmos.
- Comparar os nós explorados.
- Registrar as tentativas de posicionamento.
- Medir as podas por coluna, diagonal e limite superior.
- Medir o tempo de execução.

Essa bateria servirá como referência inicial, pois o tamanho do espaço de busca permite executar as três variantes sem custo excessivo.

#### 3.2. Bateria média

**Instâncias:** `N = 7 e 8`.

**Algoritmos:** força bruta, backtracking e Branch and Bound, condicionados ao orçamento de execução previamente definido.

**Objetivos:**

- Avaliar o crescimento do número de configurações examinadas.
- Comparar a redução de nós obtida com as podas.
- Medir o custo adicional da avaliação dos limites.
- Verificar se a diminuição dos nós explorados também se traduz em menor tempo de execução.

Como a força bruta explora até \(N^N\) configurações, a execução para `N = 8` poderá ser mais demorada. Por isso, executar essa variante uma única vez por instância ou omiti-la caso ultrapasse o limite operacional estabelecido.

#### 3.3. Bateria grande

**Instâncias:** `N = 9, 10, 11 e 12`.

**Algoritmos:** backtracking e Branch and Bound.

**Objetivos:**

- Avaliar o desempenho das variantes com poda em instâncias maiores.
- Comparar o número de estados explorados e as tentativas de posicionamento.
- Identificar o custo de calcular limites superiores.
- Examinar o crescimento do tempo de execução.
- Registrar quando a força bruta não for executada por exceder o orçamento experimental.

A força bruta não será contabilizada como uma execução de tempo zero quando for omitida. O CSV deverá registrar o estado como `SKIPPED_BY_BUDGET` ou equivalente.

#### 3.4. Validação dos resultados

Utilizar quantidades conhecidas de soluções como referência:

| N | Quantidade de soluções |
|---:|---:|
| 1 | 1 |
| 2 | 0 |
| 3 | 0 |
| 4 | 2 |
| 5 | 10 |
| 6 | 4 |
| 7 | 40 |
| 8 | 92 |
| 9 | 352 |
| 10 | 724 |
| 11 | 2.680 |
| 12 | 14.200 |

Para todos os tamanhos executados pelas três variantes, verificar se a contagem de soluções é igual.

### 4. Baterias de testes para a mochila 0/1

#### 4.1. Bateria pequena

**Número de itens:** `8, 10 e 12`.

**Algoritmos:** força bruta, backtracking e Branch and Bound.

**Objetivos:**

- Validar o valor ótimo encontrado.
- Confirmar que o peso da solução não ultrapassa a capacidade.
- Comparar o número de nós explorados.
- Registrar as tentativas de inclusão e exclusão.
- Medir o tempo e as podas por capacidade e por limite superior.

#### 4.2. Bateria média

**Número de itens:** `14, 16 e 18`.

**Algoritmos:** força bruta, backtracking e Branch and Bound.

**Objetivos:**

- Analisar o crescimento do esforço computacional.
- Comparar o efeito da poda por capacidade com o efeito adicional do limite superior.
- Medir a quantidade de limites calculados e o tempo necessário para calculá-los.
- Identificar se a redução de nós compensa o custo do Branch and Bound.

#### 4.3. Bateria grande

**Número de itens:** `20, 22 e 24`.

**Algoritmos:** força bruta, backtracking e Branch and Bound, dentro do orçamento de execução definido.

**Objetivos:**

- Observar a escalabilidade das três variantes.
- Comparar os nós explorados e o tempo de execução.
- Avaliar o número de podas por capacidade e por limite superior.
- Identificar os tamanhos em que a força bruta deixa de ser prática.
- Registrar todas as execuções omitidas, sem atribuir resultados fictícios.

Para garantir a comparação correta, utilizar exatamente os mesmos itens, valores, pesos e capacidades nas três variantes de cada instância.

### 5. Métricas e formato de saída

Criar uma estrutura comum de métricas, como `SearchMetrics`, que armazene os contadores e o tempo de execução.

Os resultados devem ser exportados para um arquivo CSV contendo, no mínimo:

```text
problem,battery,algorithm,instance_id,seed,size,capacity,
elapsed_nanos,nodes_explored,attempts,
pruned_by_column,pruned_by_diagonal,
pruned_by_capacity,pruned_by_bound,
bound_evaluations,invalid_complete_candidates,
solution_count,best_value,best_weight,status
```

Os campos que não se aplicam a determinado problema poderão ficar vazios ou receber um valor explicitamente definido como não aplicável. Não devem ser confundidos com uma contagem real de zero podas.

Cada registro deverá corresponder a uma execução de um algoritmo sobre uma instância específica. Para cada configuração, calcular a mediana dos tempos medidos e manter os resultados brutos para análise.

### 6. Estrutura sugerida dos testes

```text
src/test/java/
├── nqueens/
│   ├── NQueensCorrectnessTest.java
│   ├── NQueensSmallBenchmarkTest.java
│   ├── NQueensMediumBenchmarkTest.java
│   └── NQueensLargeBenchmarkTest.java
├── knapsack/
│   ├── KnapsackCorrectnessTest.java
│   ├── KnapsackSmallBenchmarkTest.java
│   ├── KnapsackMediumBenchmarkTest.java
│   └── KnapsackLargeBenchmarkTest.java
└── support/
    ├── TestInstances.java
    ├── BenchmarkRunner.java
    └── CsvExporter.java
```

Utilizar `@ParameterizedTest` e `@MethodSource` para executar os algoritmos com diferentes tamanhos de instância. Os testes experimentais podem ser identificados com `@Tag("benchmark")`, facilitando sua execução separada dos testes de correção.

### 7. Testes de correção obrigatórios

**N rainhas:**

- Verificar as contagens conhecidas de soluções.
- Verificar que não existem conflitos em uma solução retornada.
- Comparar os resultados de todos os algoritmos nas instâncias comuns.
- Validar os contadores de nós, tentativas e podas.

**Mochila 0/1:**

- Verificar uma instância pequena com valor ótimo conhecido.
- Comparar o valor ótimo das três variantes.
- Verificar que o peso total não ultrapassa a capacidade.
- Testar casos-limite, como capacidade zero, lista vazia e itens mais pesados que a capacidade.
- Verificar a consistência dos contadores.

**Métricas e exportação:**

- Confirmar que o tempo medido não é negativo.
- Confirmar que contadores não são negativos.
- Verificar que as podas por restrição e limite são contabilizadas separadamente.
- Validar o cabeçalho do CSV e a presença dos resultados das execuções.
- Verificar se execuções omitidas estão identificadas explicitamente.

### 8. Critérios de conclusão

O plano será considerado concluído quando:

1. Todas as implementações passarem pelos testes de correção.
2. As contagens de soluções das N rainhas coincidirem com os valores de referência.
3. As variantes da mochila retornarem o mesmo valor ótimo em todas as instâncias comparáveis.
4. As três baterias de cada problema produzirem arquivos CSV válidos.
5. O experimento registrar tempo, nós explorados, tentativas e podas por causa.
6. As execuções forem reprodutíveis por meio de sementes fixas.
7. O tempo de execução não for utilizado como um critério de aprovação rígido dos testes de unidade.

### 9. Entregável

Uma suíte automatizada em JUnit 5, com testes de correção e seis baterias experimentais — três para N rainhas e três para a mochila 0/1 —, capaz de gerar dados para análise estatística e construção dos gráficos do artigo.

---

## Resultado esperado dos três planos

Ao concluir os três planos, o projeto deverá permitir responder experimentalmente:

- Quanto as estratégias de poda reduzem o número de nós explorados?
- Quais restrições eliminam mais ramos em cada problema?
- O custo de calcular limites superiores compensa a redução do espaço de busca?
- Como os resultados mudam conforme o tamanho da instância?
- A redução do número de nós explorados sempre resulta em menor tempo de execução?

As conclusões deverão ser sustentadas pelos dados coletados e limitadas às instâncias e configurações efetivamente avaliadas.