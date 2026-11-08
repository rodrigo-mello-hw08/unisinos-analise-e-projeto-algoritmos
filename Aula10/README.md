# Aula 10 — Programação Dinâmica × Algoritmos Gulosos (problema da mochila)

**Disciplina:** Análise e Projeto de Algoritmos — Unisinos — 2026/2 — Profa. Maria Adelina Raupp Sganzerla
**Dupla:** Rodrigo Mello e Vinicius de Avila Bezerra

Continuação da Aula 09 (algoritmos gulosos). Mesmo problema — mochila 0/1 e fracionária —, agora resolvido também por **Programação Dinâmica (Bottom-Up)** e comparado com o guloso sobre os mesmos dados.

## Estrutura

```
Aula10/
├── src/
│   ├── Main.java              executa GreedyRunner, DynamicRunner e ComparisonRunner
│   ├── domain/                modelos (itens e resultados das mochilas)
│   ├── service/               algoritmos
│   │   ├── classical/greedy/      guloso 0/1 e força bruta (KnapsackOptimalSolver)
│   │   ├── classical/dynamic/     PD 0/1 (KnapsackClassicDynamicProblem)
│   │   └── fractional/            guloso fracionário
│   ├── runner/                execução dos testes; KnapsackInstances = instâncias pequena, média e grande
│   ├── util/                  medição de tempo e impressão
│   └── analysis/              ferramentas de ANÁLISE usadas no relatório (não fazem parte da solução)
│       ├── Benchmark.java         benchmark de acerto do guloso 0/1, com a PD como referência
│       ├── DpTable.java           tabela da PD da instância pequena, passo a passo
│       └── ChartPng.java          gráfico do benchmark em PNG
├── executar.ps1 / executar.sh roteiro que compila e gera todos os resultados
└── resultados/                saídas usadas no relatório
```

As classes de `analysis/` apenas **usam** as classes do projeto (guloso, PD, fracionária e força bruta); nenhuma delas reimplementa os algoritmos.

## Como executar

Requisito: **JDK 21 ou superior** (`javac` e `java` no PATH). Nenhuma outra dependência.

Windows (PowerShell), dentro da pasta `Aula10`:

```powershell
powershell -ExecutionPolicy Bypass -File .\executar.ps1
```

Linux / macOS / Git Bash, dentro da pasta `Aula10`:

```bash
bash executar.sh
```

O script compila `src/` (incluindo `src/analysis/`) para `out/` e grava os resultados em `resultados/`. Leva cerca de 20 segundos, a maior parte no benchmark.

Para rodar só o programa principal:

```bash
javac -encoding UTF-8 -d out $(find src -name '*.java')
java -cp out Main
```

## Resultados (`resultados/`) e onde aparecem no relatório

| Arquivo | Conteúdo | Gerado por | Seção do relatório |
|---|---|---|---|
| `ambiente.txt` | SO, CPU, RAM e versão do Java da máquina que executou | scripts | Codificação/Ambiente |
| `saida-main.txt` | Guloso (0/1 e fracionário) e PD (0/1) nas instâncias pequena, média e grande; cenários-contraexemplo; tempo guloso × PD na instância grande | `Main` | Testes e Comparação |
| `tabela-dp-pequena.md` | Tabela `dp[i][c]` da instância pequena (5 itens, W = 10), reconstrução e reaproveitamento de células; confere o resultado com a classe da PD | `analysis.DpTable` | Visualização da PD |
| `acerto-guloso.csv` | Em quantas instâncias aleatórias o guloso 0/1 atinge o ótimo da PD; erro médio e máximo; conferência PD = força bruta (n ≤ 15) | `analysis.Benchmark` | Benchmark de acerto do guloso |
| `acerto-guloso.png` | Gráfico do benchmark | `analysis.ChartPng` | Benchmark de acerto do guloso |

Os arquivos versionados em `resultados/` são os usados no relatório.

## Observações

- **Valores** são determinísticos: as instâncias do projeto são geradas por fórmulas em `KnapsackInstances`, e o benchmark usa semente fixa (42). Devem coincidir em qualquer máquina.
- **Tempos** dependem da máquina; o hardware da execução fica registrado em `ambiente.txt`.
- Benchmark: pesos uniformes em [1, 20], valores uniformes em [1, 100], capacidade igual a uma fração do peso total (série A: 50%, variando n; série B: 10% a 90%, com n = 20 e n = 50); 2000 instâncias por ponto. Ele também verifica, em toda instância, que `guloso 0/1 ≤ PD 0/1 ≤ fracionária` e, quando n ≤ 15, que a PD coincide com a força bruta (`KnapsackOptimalSolver`); qualquer violação interrompe a execução com erro.
