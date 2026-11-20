# Aula 11 — Divisão e Conquista × Programação Dinâmica × Algoritmos Gulosos (problema da mochila)

**Disciplina:** Análise e Projeto de Algoritmos — Unisinos — 2026/2 — Profa. Maria Adelina Raupp Sganzerla
**Dupla:** Rodrigo Mello e Vinicius de Avila Bezerra

Continuação das Aulas 09 (algoritmos gulosos) e 10 (programação dinâmica). Mesmo problema — mochila 0/1 — e mesmas instâncias, agora resolvido também por **Divisão e Conquista**: recursão sobre a decisão de levar ou não cada item, com poda por limite superior (estimativa fracionária), sem programação dinâmica nem memoização.

## Estrutura

```
Aula11/
├── src/
│   ├── Main.java              executa GreedyRunner, DynamicRunner, DivideAndConquerRunner e ComparisonRunner
│   ├── domain/                modelos (itens e resultados das mochilas)
│   ├── service/               algoritmos
│   │   ├── classical/greedy/            guloso 0/1 e força bruta (KnapsackOptimalSolver)
│   │   ├── classical/dynamic/           PD 0/1 (KnapsackClassicDynamicProblem)
│   │   ├── classical/divideandconquer/  divisão e conquista 0/1 (KnapsackClassicDivideAndConquerProblem)
│   │   └── fractional/                  guloso fracionário
│   ├── runner/                execução dos testes; KnapsackInstances = instâncias pequena, média e grande
│   ├── util/                  medição de tempo e impressão
│   └── analysis/              ferramentas de ANÁLISE usadas nos relatórios (não fazem parte da solução)
│       ├── DcStats.java           contadores da divisão e conquista (chamadas, divisões, podas, profundidade)
│       ├── DcFigures.java         fluxograma e árvore de recursão da instância pequena (PNG)
│       └── Benchmark.java, DpTable.java, ChartPng.java   herdadas da Aula 10 (relatório de PD)
├── executar.ps1 / executar.sh roteiro que compila e gera os resultados do relatório da Aula 11
└── resultados/                saídas usadas no relatório
```

As classes de `analysis/` apenas **usam** as classes do projeto. `DcStats` repete a lógica de `KnapsackClassicDivideAndConquerProblem` (mesma ordenação, mesma ordem dos ramos e mesma poda) só para contar as chamadas, e confere o valor obtido com a classe original, interrompendo com erro se divergir.

## Como executar

Requisito: **JDK 21 ou superior** (`javac` e `java` no PATH). Nenhuma outra dependência.

Windows (PowerShell), dentro da pasta `Aula11`:

```powershell
powershell -ExecutionPolicy Bypass -File .\executar.ps1
```

Linux / macOS / Git Bash, dentro da pasta `Aula11`:

```bash
bash executar.sh
```

O script compila `src/` (incluindo `src/analysis/`) para `out/` e grava os resultados em `resultados/`. Leva poucos segundos.

Para rodar só o programa principal:

```bash
javac -encoding UTF-8 -d out $(find src -name '*.java')
java -cp out Main
```

## Resultados (`resultados/`) e onde aparecem no relatório

| Arquivo | Conteúdo | Gerado por | Seção do relatório |
|---|---|---|---|
| `ambiente.txt` | SO, CPU, RAM e versão do Java da máquina que executou | scripts | Codificação/Ambiente |
| `saida-main.txt` | Guloso, PD e divisão e conquista nas instâncias pequena, média e grande; cenários-contraexemplo; comparação dos três sobre os mesmos dados, com tempos | `Main` | Testes e Comparação dos três paradigmas |
| `dc-contadores.txt` | Chamadas recursivas, divisões, podas e profundidade da divisão e conquista nas três instâncias; chamadas da versão sem poda (só onde é viável) | `analysis.DcStats` | Testes experimentais e Análise crítica |
| `fluxograma-dc-mochila.png` | Fluxograma da divisão e conquista aplicada à mochila 0/1 | `analysis.DcFigures` | Modelagem da solução |
| `arvore-recursao-pequena.png` | Árvore de recursão da instância pequena (calculada pela mesma lógica do algoritmo) | `analysis.DcFigures` | Visualização da recursão |

`acerto-guloso.csv`, `acerto-guloso.png` e `tabela-dp-pequena.md` são cópias dos resultados da Aula 10 (relatório de PD) e não são regenerados por este roteiro.

## Observações

- **Valores e contadores** são determinísticos: as instâncias são geradas por fórmulas em `KnapsackInstances`. Devem coincidir em qualquer máquina.
- **Tempos** dependem da máquina; o hardware da execução fica registrado em `ambiente.txt`.
- A versão sem poda (recursão pura) explora até 2ⁿ combinações. Ela só é executada por `DcStats` na instância pequena; nas instâncias média (100 itens) e grande (2000 itens) seria inviável, e foi por isso que a implementação final acrescentou a poda.
