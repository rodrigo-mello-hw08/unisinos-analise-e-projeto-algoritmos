# Planos de Execução — Análise do Impacto das Estratégias de Poda

**Projeto:** Análise do Impacto das Estratégias de Poda no Desempenho de Algoritmos de Busca: Um Estudo sobre os Problemas da Mochila e das N Rainhas

**Tecnologias:** Java 21, Maven, JUnit 5 (JUnit Jupiter) e exportação de resultados em CSV.

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
