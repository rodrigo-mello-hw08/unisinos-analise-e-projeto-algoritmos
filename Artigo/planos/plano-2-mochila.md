# Planos de Execução — Análise do Impacto das Estratégias de Poda

**Projeto:** Análise do Impacto das Estratégias de Poda no Desempenho de Algoritmos de Busca: Um Estudo sobre os Problemas da Mochila e das N Rainhas

**Tecnologias:** Java 21, Maven, JUnit 5 (JUnit Jupiter) e exportação de resultados em CSV.

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
