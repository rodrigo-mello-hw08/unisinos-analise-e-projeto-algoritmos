# Figuras do artigo

Rodar em `Artigo/`, com o projeto já compilado em `out/` (comando no `CLAUDE.md`). A saída vai para `artigo/images/`: um PDF para o Overleaf e um PNG para conferir.

| Script | Gera | Depende de |
|---|---|---|
| `figuras_fixas.py` | `arvore-rainhas-4`, `solucoes-rainhas-4`, `tsp-instancia-6` | só do algoritmo e do gerador de instâncias (`ExportarInstanciaTsp.java` usa as classes de `src/`) |
| `figuras_dados.py` | `nos-rainhas`, `nos-tsp`, `podas-rainhas`, `tempo-rainhas`, `tempo-tsp` (com aquecimento) e `frio-aquecido` (colunas `warmup_elapsed_ns` × `tempo_decorrido_ns`) | `resources/resultados-*.csv` — **rodar de novo sempre que o Main regerar os CSVs** |

```
py -3.12 figuras/figuras_fixas.py [--tsp-n 6]
py -3.12 figuras/figuras_dados.py [--nqueens CSV] [--tsp CSV]
```

Requisitos: Python 3 com `matplotlib` e o JDK 21. No Overleaf: `\includegraphics[width=\linewidth]{images/nos-rainhas.pdf}`.
