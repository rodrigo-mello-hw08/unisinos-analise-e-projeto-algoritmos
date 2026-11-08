Instancia pequena: capacidade 10, itens A(2,10) B(3,14) C(5,25) D(4,16) E(1,3)

Tabela DP (negrito = caminho percorrido na reconstrução):

| i \ c | 0 | 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 | 9 | 10 |
|---|---|---|---|---|---|---|---|---|---|---|---|
| 0 (nenhum item) | **0** | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 1 (A: p=2, v=10) | 0 | 0 | **10** | 10 | 10 | 10 | 10 | 10 | 10 | 10 | 10 |
| 2 (B: p=3, v=14) | 0 | 0 | 10 | 14 | 14 | **24** | 24 | 24 | 24 | 24 | 24 |
| 3 (C: p=5, v=25) | 0 | 0 | 10 | 14 | 14 | 25 | 25 | 35 | 39 | 39 | **49** |
| 4 (D: p=4, v=16) | 0 | 0 | 10 | 14 | 16 | 25 | 26 | 35 | 39 | 41 | **49** |
| 5 (E: p=1, v=3) | 0 | 3 | 10 | 14 | 17 | 25 | 28 | 35 | 39 | 42 | **49** |

Reconstrução (de dp[5][10] até dp[0][...]):

- `dp[5][10] = 49` = `dp[4][10] = 49` → o item **E** não foi escolhido; capacidade segue 10.
- `dp[4][10] = 49` = `dp[3][10] = 49` → o item **D** não foi escolhido; capacidade segue 10.
- `dp[3][10] = 49` > `dp[2][10] = 24` → o item **C** (peso 5, valor 25) foi escolhido; capacidade restante 10 → 5.
- `dp[2][5] = 24` > `dp[1][5] = 10` → o item **B** (peso 3, valor 14) foi escolhido; capacidade restante 5 → 2.
- `dp[1][2] = 10` > `dp[0][2] = 0` → o item **A** (peso 2, valor 10) foi escolhido; capacidade restante 2 → 0.

Itens escolhidos: [C, B, A] | valor ótimo dp[5][10] = 49

Exemplo de reaproveitamento (item C, peso 5):

- `dp[3][10] = max( dp[2][10] = 24 , dp[2][5] + 25 = 24 + 25 )` = 49
- A célula `dp[2][5] = 24` foi calculada uma única vez e é lida de novo por `dp[3][5]` (ramo "não levar") e por `dp[3][10]` (ramo "levar", pois 5 + 5 = 10).

Conferência com KnapsackClassicDynamicProblem: OK (mesmo valor e mesmos itens)
