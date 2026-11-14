# {{título definitivo — P13}}

> **DEV · COMO LER ESTE ARQUIVO** (este bloco não vai para o artigo)
>
> | Formato | Significa |
> |---|---|
> | texto normal, sem `>` | **vai para o artigo** |
> | bloco de citação começando com **DEV** | **nunca vai**: pendências, status, material de apoio, achados, comandos |
> | `{{...}}` dentro do texto | **lacuna** a preencher |
> | ✅ 🟡 🔴 | só dentro de blocos DEV. ✅ pronto (conferir) · 🟡 rascunho (revisar/fonte/dado) · 🔴 depende de decisão sua |
>
> **REGRA DA PROFESSORA:** texto gerado por IA = plágio (nota zero). Por isso a prosa é **toda do grupo**: cada seção tem um `{{texto do grupo}}` e um bloco **DEV · material** com tópicos (fatos, números, código, fontes). O Claude só estrutura, confere números, gera tabelas/gráficos a partir dos dados, busca fontes e revisa.
>
> **Antes de exportar:** o PDF final é escrito pelo grupo em LaTeX/Overleaf (template SBC); este arquivo é o roteiro. §0 e Apêndices são 100% DEV.
>
> **Estado:** ESQUELETO v1 — 2026-11-14. Entrega **23/11/2026 19:30** (PDF, Moodle, "Aula 7 - Artigo Científico"). Limite: **10–15 laudas** SBC.

> ## 0. PENDÊNCIAS
>
> ### Sessão de código (opcional)
>
> - [ ] **P4 — Medição de tempo confiável.** Os CSVs atuais vêm de **uma execução, sem aquecimento** (ex.: TSP n=1 força bruta = 1,76 ms, artefato de JIT). O próprio plano 3 pede aquecimento + repetições + mediana. Destrava a §5 (tempos) e a §6. Feito por quem mexe no código (Rodrigo) ou via skill `reproducao` (ferramentas em `Artigo/src/analysis`, sem alterar o código existente).
> - [ ] **P5 — Ampliar tamanhos.** N rainhas só vai até N=7 (plano: BT/B&B até 12, FB até 8 com orçamento e `SKIPPED_BY_BUDGET`); TSP até n=10. Com poucos pontos, as curvas de crescimento da §5 ficam pobres.
> - [ ] **P11 — Testes de correção (JUnit) do plano 3** não existem no código. Opcional; a §5 pode citar a verificação "mesma contagem/custo nas 3 variantes" a partir dos CSVs.
>
> ### ⏳ Aguardando terceiros
>
> - [ ] **P6 — Máquina oficial dos tempos** (CPU, RAM, SO, JDK). Nos relatórios anteriores foi o Ryzen 7 5700X do Rodrigo. Perguntar ao Rodrigo em qual máquina gerou os CSVs de 12/11 e se re-medirá lá.
> - [ ] **P7 — Integrantes.** Dupla Vini + Rodrigo, como nos relatórios? (enunciado: individual ou dupla). E-mails institucionais para o cabeçalho SBC.
>
> ### ⏸ Adiadas
>
> ### Decisões em aberto
>
> - [ ] **P13 — Título e objetivos ajustados para N rainhas + TSP.** Isso é texto do grupo. Uma base possível é o título do próprio plano 2b, sem a mochila. Também é preciso ajustar o objetivo geral do rascunho do Rodrigo, que cita a mochila.
> - [ ] **P3 — Referências científicas.** O enunciado pede "fontes científicas efetivamente utilizadas". Até agora a disciplina usou só o CPH (Laaksonen, 2018), que cobre backtracking/poda (busca completa) mas é fraco em B&B e limite por MST. Precisa de você (e do Rodrigo) escolher 2–4 fontes (livro-texto da disciplina, slides da professora, artigos). Eu só busco/cito o que for aprovado.
> - [ ] **P12 — Backtracking do TSP = força bruta.** As duas variantes exploram a mesma árvore (n=10: 986.410 nós e 181.440 rotas nas duas), porque a força bruta já não repete cidades. Mudam só o contador e o tempo. Opções:
>   - (a) Redefinir a força bruta como enumeração com repetição (n^(n−1) sequências, validadas na folha), igual à das rainhas. Assim o BT passa a ter poda real por restrição. Exige mudança de código.
>   - (b) Manter o código e assumir no artigo que, no TSP, só o B&B poda. A comparação fica FB/BT × B&B.
>   - (c) Tirar o BT do TSP do artigo.
>
>   Sugestão: (a) se o Rodrigo topar; senão, (b). **Vini (14/11): fica pendente.**
>
> ### ✅ Fechadas
>
> - ~~P1~~ Problemas: **N rainhas + TSP**; mochila fora (Vini, 14/11).
> - ~~P2~~ PDF em **LaTeX/Overleaf**, template SBC da professora; este `.md` é roteiro, dados e material (Vini, 14/11).
> - ~~P10~~ Sem repositório próprio: o trabalho fica em `Artigo/` no repositório do Rodrigo (Vini, 14/11).
> - ~~P8~~ Regra anti-IA: Claude não redige prosa do artigo (enunciado, 14/11).
> - ~~P9~~ Cabeçalho do enunciado com "Simulação e Modelagem de Sistemas" é erro de cópia; entrega é em APA (14/11).

---

> **Mapeamento enunciado → este arquivo:** Título · Resumo · Abstract · §1 Introdução · §2 Fundamentação Teórica · §3 Metodologia · §4 Análise Teórica · §5 Experimentos e Resultados · §6 Discussão · §7 Conclusão · Referências — exatamente as seções mínimas do enunciado (II). O template de *relatório* da disciplina não se aplica. Orçamento sugerido (15 laudas máx.): Resumo+Abstract 1 · §1 1 · §2 2,5 · §3 1,5 · §4 2 · §5 3,5 · §6 1,5 · §7 0,5 · Ref. 0,5.

**Autores:** {{nomes e e-mails institucionais — P7}}
**Instituição:** Universidade do Vale do Rio dos Sinos (Unisinos), Ciência da Computação

> **DEV 🟡** cabeçalho SBC: nome, afiliação, e-mail; depende de P7.

## Resumo

{{texto do grupo: problema, objetivo, método, principais resultados, conclusão — 1 parágrafo, até ~10 linhas}}

## Abstract

{{texto do grupo: versão em inglês do resumo}}

> **DEV 🔴 · material** Resumo/Abstract: escrever por último. Depende de P13 e dos números finais (P4). Pergunta de pesquisa do rascunho do Rodrigo: "como diferentes estratégias de poda influenciam o desempenho computacional de algoritmos de busca aplicados a problemas combinatórios?".

## 1. Introdução

{{texto do grupo: contexto (problemas combinatórios, explosão do espaço de busca), problema, justificativa, questão de investigação, objetivo geral e específicos, organização do artigo}}

> **DEV 🟡 · material** §1
> - Objetivo geral e 5 objetivos específicos já escritos pelo Rodrigo em `docs/rascunho-rodrigo.md` (texto dele, pode ser aproveitado por vocês). Trocar "mochila" por TSP (P13).
> - Resultado esperado dos planos (`../planos/plano-inicial.md`, fim): 5 perguntas que o experimento responde — boa base para a questão de investigação.
> - Fechar com um parágrafo "o artigo está organizado assim: seção 2 … seção 7".

## 2. Fundamentação Teórica

### 2.1 Espaço de busca e busca exaustiva

{{texto do grupo}}

### 2.2 Backtracking e poda por restrição

{{texto do grupo}}

### 2.3 Branch and Bound e poda por limite

{{texto do grupo}}

### 2.4 Problemas estudados

{{texto do grupo: N rainhas; Problema do Caixeiro-Viajante}}

> **DEV 🟡 · material** §2 (cada item precisa de fonte aprovada — P3)
> - Árvore de estados; força bruta enumera todas as folhas; backtracking abandona prefixos que violam restrição; B&B usa um limite (superior em maximização, inferior em minimização) comparado à melhor solução conhecida.
> - N rainhas: problema de **satisfação** de restrições (contar todas as soluções). Não tem função objetivo — por isso o "B&B" das rainhas usa um limite de viabilidade (rainhas posicionáveis), ponto metodológico já destacado no plano 1 (§3.3, "nota metodológica"). Vale discutir isso honestamente no artigo.
> - TSP: otimização (minimização), NP-difícil; rotas `(n−1)!/2` no caso simétrico fixando a origem e uma orientação. Limite inferior por **árvore geradora mínima** dos não visitados + duas conexões mínimas (plano 2b §3.3).
> - Sem pseudocódigo; descrever por texto, fórmula ou referência a classe/método.

## 3. Metodologia

{{texto do grupo: algoritmos selecionados, linguagem, ambiente, instâncias e tamanhos, métricas, procedimento de medição}}

**Ambiente:** Java SDK 21 (JDK 21); {{CPU, RAM, SO da máquina oficial — P6}}. Código-fonte: <https://github.com/rodrigo-mello-hw08/unisinos-analise-e-projeto-algoritmos/tree/main/Artigo>.

> **DEV 🟡 · material** §3 — o mapeamento detalhado do código (classes, métricas, instâncias, como rodar) está no Apêndice A.
> - Três variantes por problema, mesma interface (`NQueensSolver`, `TspSolver`), mesmas instâncias.
> - Métricas registradas: tempo (ns, `System.nanoTime()`), nós explorados, tentativas, podas por causa (coluna, diagonal, limite, cidade visitada), avaliações do limite, soluções/rotas completas avaliadas.
> - Procedimento de medição: **depende de P4** (hoje é execução única sem aquecimento — não descrever assim no artigo sem decidir).
> - Tamanhos: N rainhas N=4..7; TSP n=1,2,4,6,8,10 (P5 pode ampliar).

## 4. Análise Teórica dos Algoritmos

### 4.1 N rainhas

{{texto do grupo}}

### 4.2 Problema do Caixeiro-Viajante

{{texto do grupo}}

> **DEV 🟡 · material** §4 (enunciado: contagem de operações, somatórios, recorrências). Conferir com o Apêndice A (complexidade da implementação real).
> - N rainhas, força bruta: nós = Σ_{k=0..N} N^k = (N^{N+1} − 1)/(N − 1). Confere com o CSV: N=4 → 341, N=5 → 3906, N=6 → 55987, N=7 → 960800. Folhas = N^N, cada uma validada em O(N²) por `isValid` (confirmado) → O(N^N · N²).
> - N rainhas, B&B: mesma árvore do BT, podada; o limite custa O((N−row)·N) por nó → O(N!·N²) no pior caso.
> - N rainhas, backtracking: limitado por N! folhas (uma rainha por coluna) → O(N!) no pior caso, muito menor na prática pelas diagonais.
> - TSP, força bruta/backtracking: (n−1)! folhas, das quais (n−1)!/2 são canônicas e avaliadas (n=8 → 2520, n=10 → 181440, confere com o CSV). Nós = Σ_{k} (n−1)!/(n−1−k)! ≈ e·(n−1)!. Custo total O((n−1)!·n).
> - TSP, B&B: mesmo pior caso O(n!), mais o custo do limite (MST em O(n²) por nó com Prim em matriz) → O(n! · n²) no pior caso; ganho é no caso prático.
> - Recorrência possível: T(k) = (n−k)·T(k+1) + custo do nó, para o nível k da árvore.

## 5. Experimentos e Resultados

{{texto do grupo: apresentar as tabelas/figuras e descrever o que mostram, sem interpretar (a interpretação vai na §6)}}

**Tabela 1. N rainhas: nós explorados e soluções encontradas.**

| N | Soluções | Nós (força bruta) | Nós (backtracking) | Nós (branch and bound) |
|---:|---:|---:|---:|---:|
| 4 | 2 | 341 | 17 | 17 |
| 5 | 10 | 3.906 | 54 | 54 |
| 6 | 4 | 55.987 | 153 | 131 |
| 7 | 40 | 960.800 | 552 | 464 |

**Tabela 2. N rainhas: podas por causa.**

| N | BT coluna | BT diagonal | B&B coluna | B&B diagonal | B&B limite | B&B avaliações do limite |
|---:|---:|---:|---:|---:|---:|---:|
| 4 | 28 | 16 | 18 | 10 | 4 | 15 |
| 5 | 119 | 48 | 105 | 42 | 4 | 44 |
| 6 | 538 | 204 | 242 | 126 | 44 | 127 |
| 7 | 2.239 | 794 | 1.151 | 444 | 130 | 424 |

**Tabela 3. Caixeiro-Viajante: custo ótimo, nós explorados e rotas completas avaliadas.**

| n | Custo ótimo | Nós (FB) | Nós (BT) | Nós (B&B) | Rotas (FB) | Rotas (BT) | Rotas (B&B) |
|---:|---:|---:|---:|---:|---:|---:|---:|
| 4 | 259 | 16 | 16 | 7 | 3 | 3 | 1 |
| 6 | 138 | 326 | 326 | 41 | 60 | 60 | 3 |
| 8 | 265 | 13.700 | 13.700 | 436 | 2.520 | 2.520 | 13 |
| 10 | 233 | 986.410 | 986.410 | 855 | 181.440 | 181.440 | 11 |

**Tabela 4. Tempo de execução (mediana de {{repetições}} execuções, ms).**

{{tabela de tempos — depende de P4 e P6}}

> **DEV 🟡** §5: Tabelas 1–3 copiadas de `../resources/resultados-{nqueens,tsp}.csv` (commit `03096d0`); contagens são determinísticas, mas re-conferir se o código mudar. Tabela 3 omite n=1 e n=2 (triviais). As 3 variantes concordam em todas as linhas (mesmo nº de soluções / mesmo custo ótimo) — isso é a verificação de correção.
> - Tempos dos CSVs atuais (provisórios, execução única): N=7 FB 29,7 ms, BT 0,28 ms, B&B 0,62 ms; TSP n=10 FB 113,5 ms, BT 46,7 ms, B&B 2,8 ms. **Não usar** até P4.
> - Achados nos dados (confirmar com o Apêndice A):
>   - TSP: nós de FB = nós de BT. A força bruta já percorre só permutações (não repete cidade), então o "backtracking" sem limite não poda nada que a força bruta explore; a coluna `podas_por_cidade_visitada` do BT conta candidatos rejeitados que a FB nem gera. Precisa ser explicado na §3/§6, ou a FB redefinida (ex.: gerar n^(n−1) sequências com repetição, como nas rainhas).
>   - N rainhas: B&B poda poucos ramos por limite (130 em N=7) e gasta 424 avaliações; em N=7 foi ~2× mais lento que o BT na medição única — material para a §6 ("o custo do limite compensa?").
> - Figuras sugeridas (gerar por script, não à mão): nós explorados × tamanho em escala log, uma por problema. Gerar PNG ou PDF para o Overleaf, ou pgfplots.

## 6. Discussão

{{texto do grupo: comparar teoria (§4) e experimentos (§5); responder as perguntas do plano: quanto a poda reduz nós, que restrição poda mais, se o custo do limite compensa, se menos nós sempre significa menos tempo}}

> **DEV 🔴 · material** §6: depende de P4 (tempos confiáveis). Pontos que os dados já sustentam: redução de nós de várias ordens de grandeza (rainhas N=7: 960.800 → 552; TSP n=10: 986.410 → 855); no TSP o ganho vem quase todo do limite (BT = FB em nós); nas rainhas o limite agrega pouco sobre a poda por restrição (552 → 464 nós) com custo extra de avaliação.

## 7. Conclusão

{{texto do grupo: síntese, resposta ao objetivo, limitações (tamanhos pequenos, uma máquina, instâncias aleatórias), trabalhos futuros}}

> **DEV 🔴** §7: escrever depois da §6. Limitações já visíveis: tamanhos pequenos (P5), uma máquina (P6), uma semente/instância por tamanho no TSP (confirmar no Apêndice A).

## Referências

{{referências aprovadas — P3, no formato SBC}}

> **DEV 🔴** Referências: só fontes aprovadas pelo grupo (P3). Use o agente `buscador-citacoes` para achar a página impressa de cada afirmação da §2.

> ## Apêndices (DEV)
>
> ### A. Mapa do código (mapeador-codigo, 14/11/2026, commit `03096d0`)
>
> **Compilar e rodar** (em `Artigo/`, ~segundos; grava `resources/*.csv` no diretório corrente):
> `javac -encoding UTF-8 -d out src/Main.java src/metrics/*.java src/nqueens/*/*.java src/reporting/*.java src/tsp/*/*.java` e `java -Dfile.encoding=UTF-8 -cp out Main`. Sem Maven nem testes, apesar do que dizem os planos.
>
> **Classes:** `Main.java:11` (N rainhas `{4,5,6,7}` na linha 14; TSP `{1,2,4,6,8,10}` na linha 15) · `metrics/SearchMetrics` (contadores; `finish` na linha 73) · `nqueens/solver/{NQueensBruteForce, NQueensBacktracking, NQueensBranchAndBound, NQueensSupport}` · `tsp/solver/{TspBruteForce, TspBacktracking, TspBranchAndBound, TspSupport}` · `tsp/domain/TspInstanceGenerator` · `*/experiment/*ExperimentRunner` · `reporting/*Csv`.
>
> **N rainhas:** uma rainha por linha.
> - A força bruta (`NQueensBruteForce.java:23-39`) gera N^N folhas e valida cada uma com `isValid`, que custa O(N²).
> - O backtracking (`NQueensBacktracking.java:22-52`) usa vetores `cols`, `down[2n−1]` (índice `row−col+n−1`) e `up[2n−1]` (índice `row+col`). Poda por coluna (linha 33) e por diagonal (linha 39).
> - O B&B (`NQueensBranchAndBound.java:22-78`) é o backtracking com um limite extra. Para cada linha futura, verifica se ainda há alguma coluna livre e conta essas linhas em `possibleRows`. Poda se `row + possibleRows < n` (linha 52). Na prática é *forward checking* (verificação antecipada de viabilidade), não um limite de otimização.
>
> **TSP:**
> - A origem é a cidade 0. A orientação só é eliminada na folha, por `isCanonicalOrientation`, que exige `route[1] < route[n−1]`.
> - Força bruta e backtracking (`TspBruteForce.java:29-54`, `TspBacktracking.java:29-55`) percorrem **a mesma árvore**. A única diferença é que o backtracking conta `pruneVisitedCity` (ver P12).
> - B&B (`TspBranchAndBound.java:29-64`): poda se `custoParcial + LB ≥ melhor` (linhas 46-47). `LB = minDist(atual→não visitadas) + MST(não visitadas, Prim O(r²)) + minDist(não visitadas→0)` (`lowerBound`, linhas 66-129). O melhor custo começa em `Long.MAX_VALUE`, sem solução inicial heurística nem ordenação de ramos.
>
> **Instâncias TSP:** semente `20261110 + n` (`TspInstanceGenerator.java:6,15`). Distâncias uniformes em [1,100], matriz simétrica e sem coordenadas, então a desigualdade triangular não é garantida. Uma instância por tamanho.
>
> **Métricas:**
> - `nodesExplored` conta cada chamada recursiva, incluindo a raiz e as folhas.
> - `placementAttempts` conta cada iteração do laço; não é exportado no CSV do TSP.
> - `boundEvaluations` não inclui as folhas.
> - O tempo vem de `System.nanoTime()` em torno de `solve`: uma execução por solver, sem aquecimento e na ordem fixa FB → BT → B&B.
> - Reexecutando, o BT do TSP com n=10 levou 13,5 ms, contra 46,7 ms no CSV.
>
> **Complexidade real (pior caso):**
>
> | Algoritmo | Tempo |
> |---|---|
> | Rainhas FB | O(N^N·N²) |
> | Rainhas BT | O(N!) |
> | Rainhas B&B | O(N!·N²) (o limite custa O(N²) por nó) |
> | TSP FB e BT | O((n−1)!·n) (nós ≈ e·(n−1)!, confere: 986.410 para n=10) |
> | TSP B&B | O((n−1)!·n²) |
>
> Espaço: O(N) ou O(n) em todos os casos (no B&B do TSP, O(n²) somando as alocações transitórias ao longo da pilha).
>
> **Divergências em relação aos planos:**
> - Não há mochila, Maven, JUnit, baterias, `SKIPPED_BY_BUDGET`, mediana nem colunas `battery`/`seed`/`instance_id`.
> - As N rainhas não têm checagem cruzada no código. O runner do TSP aborta se os custos divergirem (`TspExperimentRunner.java:28-32`).
