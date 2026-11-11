# Plano 3 — Testes experimentais: N-Queens e caixeiro viajante

**Projeto:** Análise do impacto das estratégias de poda em algoritmos de busca, aplicada exclusivamente ao problema das N rainhas (N-Queens) e ao problema do caixeiro viajante (TSP).

**Tecnologias:** Java 21, Maven, JUnit 5 e exportação dos resultados em CSV.

## 1. Objetivo

Validar a correção dos algoritmos de força bruta, backtracking e Branch and Bound para N-Queens e TSP e coletar dados reprodutíveis sobre nós explorados, tentativas, podas e tempo. O escopo deste plano contém somente esses dois problemas.

O `Main.java` atual já executa as três variantes para N-Queens (`N = 4, 5, 6, 7`) e TSP (`1, 2, 4, 6, 8, 10` cidades), compara custos ótimos do TSP, valida o formato das rotas e gera `resources/resultados-busca.csv`. As baterias abaixo ampliam e organizam essa execução, preservando conjuntos comparáveis entre algoritmos.

## 2. Organização e medição

Separar testes de correção das baterias de desempenho. Testes de correção verificam resultados conhecidos, validade das soluções/rotas, igualdade entre variantes e consistência de métricas. Experimentos registram dados; não devem falhar por variação de tempo.

Medir com `System.nanoTime()`, aquecer a JVM antes das medições e manter geração de instâncias e escrita do CSV fora da região cronometrada. Usar a mesma instância TSP para todos os algoritmos comparados. Repetir execuções determinísticas e reportar resultados brutos e mediana por configuração. Se houver geração aleatória, fixar e registrar a semente; confirmar se o gerador atual aceita semente ou tornar explícita a matriz de distâncias usada.

## 3. Baterias N-Queens

Executar força bruta, backtracking e Branch and Bound nos mesmos valores de N sempre que couber no orçamento. A força bruta enumera atribuições de colunas por linha e pode crescer rapidamente; definir limite operacional antes da bateria e registrar omissões como `SKIPPED_BY_BUDGET`, sem tempos ou contagens fictícias.

- **Pequena:** `N = 4, 5, 6`. Comparar contagem de soluções, nós, tentativas, podas por coluna/diagonal/limite e tempo.
- **Média:** `N = 7, 8`. Executar as três variantes sob o orçamento; se força bruta exceder o limite, registrar a omissão e manter as variantes com poda.
- **Grande:** `N = 9, 10, 11, 12`. Priorizar backtracking e Branch and Bound; força bruta somente se couber no orçamento.

Referências para contagem total de soluções: `N=1:1`, `2:0`, `3:0`, `4:2`, `5:10`, `6:4`, `7:40`, `8:92`, `9:352`, `10:724`, `11:2680`, `12:14200`. Conferir esses valores para os tamanhos executados.

## 4. Baterias TSP

Uma instância TSP deve definir número de cidades e matriz de custos/distâncias. Fixar a instância por tamanho e fornecê-la, sem alterações, às três variantes. Registrar origem fixa da rota e se custos são simétricos ou direcionados. Os tamanhos atuais do `Main.java` (`1, 2, 4, 6, 8, 10`) podem ser mantidos como baseline; incluir tamanhos intermediários conforme orçamento e capacidade do gerador.

- **Pequena:** `2, 4, 6` cidades. Comparar força bruta, backtracking e Branch and Bound, validando custo ótimo e rota.
- **Média:** `8, 10` cidades. Executar as três variantes para a mesma instância, com orçamento explícito para força bruta.
- **Grande:** testar tamanhos acima de 10 somente conforme viabilidade do gerador e limite de execução; priorizar backtracking e Branch and Bound. Marcar variantes omitidas pelo orçamento.

O caso de uma cidade pode ser mantido como teste de borda, separado da bateria comparativa. Para cada resultado, validar que a rota começa e termina na cidade 0, visita cada cidade exatamente uma vez e tem custo igual ao custo ótimo informado. As três variantes devem retornar o mesmo custo ótimo em cada instância comparável.

## 5. Métricas e CSV

Usar `SearchMetrics` compartilhado e `SearchMetricsCsv` existente, ampliando-os apenas se necessário para preservar as métricas específicas de cada problema. Cada linha deve identificar problema, tamanho, algoritmo, ID/seed da instância, repetição, tempo, nós, tentativas, podas aplicáveis e estado da execução. Incluir resultado principal: contagem de soluções em N-Queens; custo ótimo e, quando útil, rota em TSP.

Campos sem aplicação devem ficar vazios ou ser marcados explicitamente como não aplicáveis, nunca confundidos com zero podas. Preservar execuções brutas e produzir resumo com mediana dos tempos. Registrar execução omitida com estado explícito.

## 6. Estrutura sugerida

```text
src/test/java/
├── nqueens/
│   ├── NQueensCorrectnessTest.java
│   └── NQueensBenchmarkTest.java
├── tsp/
│   ├── TspCorrectnessTest.java
│   └── TspBenchmarkTest.java
└── support/
    ├── TestInstances.java
    ├── BenchmarkRunner.java
    └── CsvExporter.java
```

Usar `@ParameterizedTest` e `@MethodSource` para combinar tamanhos e algoritmos. Marcar experimentos com `@Tag("benchmark")` para execução separada dos testes de correção.

## 7. Testes obrigatórios

**N-Queens:** contagens de referência; validade de soluções retornadas; igualdade das contagens entre algoritmos; contadores não negativos e coerentes.

**TSP:** custo ótimo conhecido em instâncias pequenas e fixas; igualdade dos custos entre variantes; rota fechada, origem correta e visita única; testes de borda para instância mínima e custos; contadores não negativos.

**Relatório:** cabeçalho e registros CSV válidos; tempo não negativo; podas separadas por causa quando disponíveis; campos não aplicáveis explícitos; execuções omitidas identificadas.

## 8. Critérios de conclusão

1. As variantes passam pelos testes de correção nos dois problemas.
2. N-Queens coincide com as contagens de referência para os tamanhos cobertos.
3. As variantes TSP retornam o mesmo custo ótimo nas mesmas instâncias e rotas válidas.
4. As baterias dos dois problemas geram CSV válido, com dados brutos e omissões explícitas.
5. As métricas de tempo, nós, tentativas e podas disponíveis são registradas sem usar tempo como asserção rígida.
6. As instâncias e sementes (quando aplicável) permitem reproduzir as comparações.

## 9. Entregável e perguntas experimentais

Entregar testes JUnit 5 de correção e baterias experimentais de N-Queens e TSP, compatíveis com a geração do relatório CSV usado pelo `Main.java`. Os dados devem permitir avaliar quanto as podas reduzem nós, quais restrições eliminam mais ramos, se o custo de calcular limites compensa e como os resultados variam com o tamanho da instância. Conclusões devem se restringir às instâncias e configurações medidas.
