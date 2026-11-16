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
>   **Decisão (Vini, 16/11):** a §3 vai relatar as **duas** medições, **sem aquecimento** e **com aquecimento**, e a diferença entre elas. Para isso é preciso:
>   - manter o modo atual (execução única, a frio) como a medição "sem aquecimento";
>   - gerar uma medição "com aquecimento" (rodadas descartadas + repetições + mediana);
>   - fazer as duas na mesma máquina (P6), com os mesmos tamanhos e as mesmas instâncias.
>
>   Ainda não existem dados com aquecimento. **⏳ Aguardando o Rodrigo (Vini, 16/11): ele faz a medição com aquecimento, então o Claude não monta ferramenta própria.**
> - [ ] **P5 — Ampliar tamanhos.** N rainhas só vai até N=7 (plano: BT/B&B até 12, FB até 8 com orçamento e `SKIPPED_BY_BUDGET`); TSP até n=10. Com poucos pontos, as curvas de crescimento da §5 ficam pobres.
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
> - [x] **P3 — Referências científicas. FECHADA (14/11): ver ✅ Fechadas. O histórico da busca segue abaixo.** O enunciado pede "fontes científicas efetivamente utilizadas". Até agora a disciplina usou só o CPH (Laaksonen, 2018), que cobre backtracking/poda (busca completa) mas é fraco em B&B e limite por MST. Precisa de você (e do Rodrigo) escolher 2–4 fontes (livro-texto da disciplina, slides da professora, artigos). Eu só busco/cito o que for aprovado.
>   **Candidatas (14/11, dados bibliográficos conferidos na web; o conteúdo ainda não foi lido, só o resumo/índice):**
>
>   | # | Fonte | Sustenta | Acesso |
>   |---|---|---|---|
>   | C1 | Golomb, S. W.; Baumert, L. D. Backtrack programming. *J. ACM* 12(4):516–524, 1965. DOI 10.1145/321296.321300 | §2.2: definição clássica de backtracking | ACM (pago) |
>   | C2 | Haralick, R. M.; Elliott, G. L. Increasing tree search efficiency for constraint satisfaction problems. *Artificial Intelligence* 14(3):263–313, 1980. DOI 10.1016/0004-3702(80)90051-X | §2.2 e §6: *forward checking*, que é o que o "B&B" das rainhas faz na prática. Inclui experimentos com N rainhas | PDF no site do autor (haralick.org) |
>   | C3 | Bell, J.; Stevens, B. A survey of known results and research areas for n-queens. *Discrete Mathematics* 309(1):1–31, 2009. DOI 10.1016/j.disc.2007.12.043 | §2.4: o problema e o nº de soluções por N | Elsevier (pago) |
>   | C4 | Land, A. H.; Doig, A. G. An automatic method of solving discrete programming problems. *Econometrica* 28(3):497–520, 1960 | §2.3: origem do B&B | JSTOR |
>   | C5 | Little, J. D. C.; Murty, K. G.; Sweeney, D. W.; Karel, C. An algorithm for the traveling salesman problem. *Operations Research* 11(6):972–989, 1963. DOI 10.1287/opre.11.6.972 | §2.3/§2.4: primeiro B&B para o TSP (nome "branch and bound") | INFORMS (pago) |
>   | C6 | Held, M.; Karp, R. M. The traveling-salesman problem and minimum spanning trees. *Operations Research* 18(6):1138–1162, 1970. DOI 10.1287/opre.18.6.1138 | §2.3/§4.2: limite inferior por árvore geradora (1-trees), base do LB por MST do código | INFORMS (pago) |
>   | C7 | Karp, R. M. Reducibility among combinatorial problems. In: Miller; Thatcher (eds.) *Complexity of Computer Computations*, Plenum, 1972, p. 85–103. DOI 10.1007/978-1-4684-2001-2_9 | §1/§2.4: TSP (ciclo hamiltoniano) é NP-completo | Springer |
>   | C8 | Knuth, D. E. Estimating the efficiency of backtrack programs. *Mathematics of Computation* 29(129):121–136, 1975 | §4/§6: tamanho da árvore de backtracking, nós explorados como métrica | AMS (aberto) |
>   | C9 | Morrison, D. R. et al. Branch-and-bound algorithms: a survey of recent advances in searching, branching, and pruning. *Discrete Optimization* 19:79–102, 2016 | §2.3: visão moderna de B&B (estratégias de busca, ramificação e poda) | Elsevier (pago) |
>   | C10 | Levitin, A. *Introduction to the Design and Analysis of Algorithms*, 3. ed., Pearson, 2012, cap. 12 (12.1 Backtracking: n-Queens; 12.2 Branch-and-Bound: TSP) | §2 inteira e §4: livro-texto que cobre exatamente os dois problemas | biblioteca |
>
>   Também servem: o CPH (já usado, para backtracking/busca completa) e o Ziviani, *Projeto de Algoritmos* (Cengage, 2012, ed. Java/C++; está no Minha Biblioteca, mas não confirmei se o capítulo cobre B&B). Sugestão mínima: C10 + C1 + C6 + C2 + C7 (+ CPH). Cobre tudo e são 5–6 fontes, o que cabe em 15 laudas.
>
>   **Vini pediu só 2 fontes + CPH (14/11). Recomendação do subagente, feita a partir dos resumos (falta o OK final):**
>   - **N rainhas → C2 (Haralick & Elliott 1980).**
>     - É a fonte que define o *forward checking*, que é o que o "B&B" das rainhas faz na prática.
>     - Usa as N rainhas como problema de teste e mede o custo contando operações.
>     - Sustenta §2, §4 e §6 e dá o nome correto à variante.
>     - Acesso aberto: PDF digitalizado em haralick.org, longo; ler só as definições e os resultados das rainhas.
>   - **TSP → C6 (Held & Karp 1970).**
>     - É a origem do limite inferior por árvore geradora (1-árvore) no TSP simétrico. O limite do código é uma versão simplificada dele, sem os pesos π.
>     - Sustenta §2 (B&B com limite por MST), §4 (MST ≤ tour ótimo; Prim O(n²) por avaliação) e §6 (o limite do código é mais fraco que o de Held–Karp, o que serve de trabalho futuro).
>     - Pago (INFORMS) e matematicamente denso; ver se a biblioteca da Unisinos dá acesso.
>     - Alternativa aberta: C5 (Little et al. 1963, working paper em http://hdl.handle.net/1721.1/46828). Porém o limite de C5 é por redução de matriz, não por MST.
>   - Ressalva: o resumo de C2 só foi confirmado por fontes secundárias, porque o PDF não tem texto selecionável. Os resumos de C3, C9 e C1 estão ocultos pelas editoras.
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
> - ~~P3~~ Referências: **CPH** (busca completa/backtracking em geral), **Haralick & Elliott 1980** (N rainhas, *forward checking*) e **Held & Karp 1970** (TSP, limite por árvore geradora). Nenhuma outra fonte (Vini, 14/11).
> - ~~P14~~ Held & Karp não está na biblioteca da Unisinos. Foi substituído por **Cormen et al., *Algoritmos: teoria e prática*, 3. ed.**, que cobre a árvore geradora mínima, o algoritmo de Prim, o TSP como NP-completo e o argumento "árvore mínima ≤ circuito ótimo" (Vini, 16/11). A tabela de candidatas acima (C6) e a recomendação do subagente ficam só como histórico.
> - ~~P15~~ Fonte para branch and bound: entra **Little et al. 1963** (C5 da tabela acima), porque nem o CPH nem o Cormen cobrem o assunto (Vini, 16/11).
>   - Sustenta a §2.3 (origem do nome "branch and bound", ramificar e limitar para o TSP) e a §2.4 (TSP).
>   - O limite do artigo é por **redução da matriz de custos**, não por árvore geradora. O limite do código não vem dele.
> - ~~P8~~ Regra anti-IA: Claude não redige prosa do artigo (enunciado, 14/11).
> - ~~P11~~ Testes JUnit: não serão feitos. O Rodrigo marca como ignorada no próximo commit (Vini, 16/11). A §5 cita como verificação de correção apenas "mesma contagem/custo nas 3 variantes", que é checada pelo `Main`.
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

> **DEV 🟡 · material** §2. Fontes aprovadas: CPH para 2.1 e 2.2; Haralick & Elliott para 2.2 e para as N rainhas; Cormen para o TSP e o limite por árvore geradora. Citar com página impressa.
> - No Cormen (3. ed.), o que interessa ao TSP está em três lugares:
>   - cap. 23: árvore geradora mínima e algoritmo de Prim, que é o que o `lowerBound` usa;
>   - §34.5.4: o TSP é NP-completo;
>   - §35.2: o argumento de que remover uma aresta de um circuito dá uma árvore geradora, logo a árvore mínima não custa mais que o circuito ótimo. Esse argumento vale para qualquer peso não negativo; a desigualdade triangular só é usada na aproximação, não neste ponto.
> - O Cormen **não cobre branch and bound**. A ideia geral de B&B (§2.3) precisa vir do CPH; conferir com o `buscador-citacoes` se ele cobre o suficiente.
> - O limite do código (menor aresta de saída + árvore mínima das restantes + menor aresta de volta) é uma montagem própria do Rodrigo a partir dessa ideia. Não atribuir a fórmula exata a uma fonte.
> - **Citações no CPH** (`buscador-citacoes`, 16/11). As páginas são as **impressas**: no PDF, a página impressa + 10. Conferi por amostragem as p. 21, 51, 148 e 177.
>
>   | Afirmação | Veredito | Pág. | Seção do CPH |
>   |---|---|---|---|
>   | Busca completa gera todas as soluções e escolhe a melhor ou conta as válidas | ✅ sustenta | 47 | 5 Complete search |
>   | Gerar subconjuntos e permutações por recursão (árvore de chamadas) | ✅ sustenta | 47–49 | 5.1, 5.2 |
>   | Backtracking estende a solução passo a passo | 🟡 parcial: o livro não diz "volta/abandona" com essas palavras; o corte aparece na poda (p. 51) | 50 | 5.3 |
>   | N rainhas: restrições de coluna e diagonais; q(8) = 92; 2 soluções para N=4 | ✅ sustenta | 50–51 | 5.3 |
>   | Poda: detectar cedo que a solução parcial não pode ser completada; grande efeito no tempo | 🟡 parcial: exemplo do grid 7×7, de 483 s para 0,6 s (p. 54). **Não** diz que o pior caso não muda | 51, 54 | 5.4 Pruning the search |
>   | **Branch and bound / poda por limite de custo** | 🔴 **sem fonte**: o termo não aparece; a poda da 5.4 é por viabilidade, não por custo | — | — |
>   | Árvore geradora mínima: definição | ✅ sustenta | 141 | 15 |
>   | Prim e sua complexidade, O(n + m log m) com fila de prioridade (o código usa a versão O(n²) em matriz, que não está no CPH) | ✅ sustenta | 147–148 | 15.3 |
>   | Caminho/circuito hamiltoniano: sem método eficiente, NP-difícil; backtracking ≥ O(n!); PD em O(2ⁿn²) | 🟡 parcial: o CPH **não** fala de "caixeiro-viajante" nem de custos; citar como problema relacionado. O TSP propriamente dito fica com o Cormen | 177–178 | 19.2 |
>   | n!; O(n!) só é viável para n ≤ 10 (tabela de 1 s) | ✅ sustenta | 14, 20, 21 | 1, 3 |
>
>   - A contagem (n−1)!/2 de rotas não está no CPH. Ela sai do raciocínio, ou do Cormen se ele trouxer.
> - **Citações no Little et al. 1963** (`buscador-citacoes`, 16/11; *working paper*). As páginas são as **impressas** no cabeçalho de cada página. O deslocamento em relação ao PDF não é constante, porque há páginas inseridas (4a, 5a, 9a, 25a, 26a). Conferi no PDF os trechos das p. 1, 4 e 13 e o do resumo.
>
>   | Afirmação | Veredito | Pág. | Seção |
>   |---|---|---|---|
>   | Apresenta um algoritmo exato para o TSP chamado "branch and bound" | ✅ sustenta | resumo (sem número); 13 | Abstract; Branch and Bound |
>   | Ideia do B&B: dividir o conjunto de rotas em subconjuntos disjuntos e achar um limite inferior para cada um | ✅ sustenta | 13 | Branch and Bound |
>   | Poda: não examinar um nó cujo limite ≥ custo da melhor rota conhecida | ✅ sustenta | 6, 10, 21 (inferida), 22 | The Algorithm; Modifications |
>   | O limite deles é por redução da matriz (subtrair o mínimo de cada linha e coluna) | 🟡 parcial: esse é o limite **inicial**; os nós seguintes somam penalidades | 4, 6 | The Algorithm, passos 1–2 |
>   | Ordem de exploração | 🟡 parcial: o algoritmo básico expande o nó de **menor limite** (p. 8); o programa usa "Go To The Right"/"Throw Away the Tree", parecido com profundidade, mas o texto não usa esse nome | 8, 21 (inferida), 23 | passo 5; Modifications |
>   | Definição do TSP: visitar as n−1 outras cidades e voltar à origem; serve também para matriz assimétrica | ✅ sustenta | 1 (sem número), 11, 12 | Introduction; Problem Statement |
>   | Resultados: 10 cidades em 0,012 min, 20 em 0,084, 30 em 0,98, 40 em 8,37 (IBM 7090); tempo cresce aproximadamente exponencialmente | ✅ sustenta | resumo; 3; 25 (inferida); 25a | Abstract; Calculations; Table I |
>   | Existem (n−1)! rotas possíveis | ✅ sustenta | 1 (sem número) | Introduction |
>   | "Enumerar todas as rotas é inviável" | 🟡 parcial: o texto dá (n−1)! e fala da dificuldade, mas não diz "inviável". Essa conclusão é do grupo, apoiada nos números | 1, 3, 13–14 | — |
>
>   - **Não** escrever que Little et al. "cunharam" o termo: eles usam "branch and bound" entre aspas como nome do método, sem reivindicar a criação.
>   - **Não** citar Little et al. para o limite do **nosso** código. O deles é por redução de matriz; o nosso é por árvore geradora mínima (Cormen/CPH para a árvore). O que o Little sustenta é a ideia de limite inferior e a regra de poda.
>   - Nosso B&B é em profundidade e começa sem solução inicial. O algoritmo básico deles é "menor limite primeiro". É uma diferença que vale mencionar na §2.3 ou na §6.
>   - Os tempos deles (IBM 7090) não são comparáveis aos nossos. No máximo, servem para mostrar o crescimento.
>   - As páginas 21 e 25 não têm número impresso: foram inferidas pela posição. Formato sugerido: "p. 21 (inferida)", "resumo" e "p. 1".
> - Árvore de estados; força bruta enumera todas as folhas; backtracking abandona prefixos que violam restrição; B&B usa um limite (superior em maximização, inferior em minimização) comparado à melhor solução conhecida.
> - N rainhas: problema de **satisfação** de restrições (contar todas as soluções). Não tem função objetivo — por isso o "B&B" das rainhas usa um limite de viabilidade (rainhas posicionáveis), ponto metodológico já destacado no plano 1 (§3.3, "nota metodológica"). Vale discutir isso honestamente no artigo.
> - TSP: otimização (minimização), NP-difícil; rotas `(n−1)!/2` no caso simétrico fixando a origem e uma orientação. Limite inferior por **árvore geradora mínima** dos não visitados + duas conexões mínimas (plano 2b §3.3).
> - Sem pseudocódigo; descrever por texto, fórmula ou referência a classe/método.

> **DEV 🟢 · figuras da §2** (geradas por `figuras/figuras_fixas.py`, não dependem dos CSVs; arquivos em `images/`, PDF para o Overleaf e PNG para conferir). Legendas: {{texto do grupo}}.
> - `arvore-rainhas-4`: árvore de estados do backtracking para N=4, com um mini-tabuleiro por nó. Mostra 17 nós, 2 soluções (borda verde) e 4 nós sem continuação (borda laranja); `c`/`d` = tentativas podadas por coluna/diagonal no nó. Totais conferidos com o CSV: 17 nós, 28 podas por coluna e 16 por diagonal. Para contrastar: a força bruta gera 341 nós em N=4 (Tabela 1).
> - `solucoes-rainhas-4`: as 2 soluções de N=4, com as colunas (2, 4, 1, 3) e (3, 1, 4, 2).
> - `tsp-instancia-6`: a instância com n=6 do gerador do `Main` (semente 20261110 + n), desenhada como grafo completo, com a rota ótima no contorno e a matriz de distâncias ao lado. Rota 0→1→4→2→3→5→0, custo 138 (igual ao CSV). Para outro n: `--tsp-n 8`.

## 3. Metodologia

{{texto do grupo: algoritmos selecionados, linguagem, ambiente, instâncias e tamanhos, métricas, procedimento de medição}}

**Ambiente:** Java SDK 21 (JDK 21); {{CPU, RAM, SO da máquina oficial — P6}}. Código-fonte: <https://github.com/rodrigo-mello-hw08/unisinos-analise-e-projeto-algoritmos/tree/main/Artigo>.

> **DEV 🟡 · material** §3 — o mapeamento detalhado do código (classes, métricas, instâncias, como rodar) está no Apêndice A.
> - Três variantes por problema, mesma interface (`NQueensSolver`, `TspSolver`), mesmas instâncias.
> - Métricas registradas: tempo (ns, `System.nanoTime()`), nós explorados, tentativas, podas por causa (coluna, diagonal, limite, cidade visitada), avaliações do limite, soluções/rotas completas avaliadas.
> - Procedimento de medição (decisão de 16/11, ver P4): descrever as **duas** medições, **sem aquecimento** (execução única, a frio, que é o que os CSVs de 12/11 têm) e **com aquecimento** (rodadas descartadas + mediana de repetições), e a **diferença** entre elas.
>   - Fatos a explicar: a JVM começa interpretando o bytecode e depois compila os trechos mais executados (JIT). Por isso a primeira execução inclui carga de classes e compilação.
>   - Exemplo já visível no CSV atual: TSP n=1 com força bruta leva 1,76 ms, um tempo absurdo para um único nó.
>   - Precisa de fonte se for afirmado como teoria (nenhuma das 3 referências aprovadas cobre a JVM/JIT). Ou se descreve só o que foi observado, ou se pede uma fonte nova ao Vini.
>   - Números da comparação: 🔴 aguardam a medição com aquecimento (P4/P6).
>   - Onde mostrar: uma tabela pequena (algoritmo × tamanho, tempo frio × tempo aquecido, razão) na §3 ou junto da Tabela 4 na §5. A escolha é do grupo.
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
> - **Figuras da §5** (geradas por `figuras/figuras_dados.py` a partir dos CSVs; **rodar de novo quando os CSVs forem regerados**: `py -3.12 figuras/figuras_dados.py`). Legendas: {{texto do grupo}}.
>   - `nos-rainhas` e `nos-tsp`: nós explorados × tamanho, em escala log, 3 variantes, com o valor do maior tamanho rotulado. No TSP as linhas da força bruta e do backtracking coincidem (a da força bruta aparece como contorno), porque os nós são iguais.
>   - `podas-rainhas`: % das tentativas podadas por causa (coluna, diagonal, limite) no BT e no B&B, com o total de podas acima de cada barra.
>   - `tempo-rainhas` e `tempo-tsp`: tempo (ms) × tamanho, em escala log. 🔴 Com os CSVs atuais (execução única, a frio) os tempos são provisórios: regerar depois da P4.
>   - `frio-aquecido` 🔴: sai só quando houver os CSVs da medição com aquecimento (`--aquecido-nqueens X.csv --aquecido-tsp Y.csv`, mesmas colunas, `tempo_decorrido_ns` = mediana). Tracejado = sem aquecimento; contínuo = com aquecimento.

## 6. Discussão

{{texto do grupo: comparar teoria (§4) e experimentos (§5); responder as perguntas do plano: quanto a poda reduz nós, que restrição poda mais, se o custo do limite compensa, se menos nós sempre significa menos tempo}}

> **DEV 🔴 · material** §6: depende de P4 (tempos confiáveis). Pontos que os dados já sustentam: redução de nós de várias ordens de grandeza (rainhas N=7: 960.800 → 552; TSP n=10: 986.410 → 855); no TSP o ganho vem quase todo do limite (BT = FB em nós); nas rainhas o limite agrega pouco sobre a poda por restrição (552 → 464 nós) com custo extra de avaliação.

## 7. Conclusão

{{texto do grupo: síntese, resposta ao objetivo, limitações (tamanhos pequenos, uma máquina, instâncias aleatórias), trabalhos futuros}}

> **DEV 🔴** §7: escrever depois da §6. Limitações já visíveis: tamanhos pequenos (P5), uma máquina (P6), uma semente/instância por tamanho no TSP (confirmar no Apêndice A).

## Referências

Cormen, T. H., Leiserson, C. E., Rivest, R. L., and Stein, C. (2012). *Algoritmos: teoria e prática*. Elsevier, Rio de Janeiro, 3. edição. Tradução de *Introduction to Algorithms*, 3rd edition.

Haralick, R. M. and Elliott, G. L. (1980). Increasing tree search efficiency for constraint satisfaction problems. *Artificial Intelligence*, 14(3):263–313.

Laaksonen, A. (2018). *Competitive Programmer's Handbook*. Disponível em: <https://cses.fi/book/book.pdf>.

Little, J. D. C., Murty, K. G., Sweeney, D. W., and Karel, C. (1963). An algorithm for the traveling salesman problem. Working paper, School of Industrial Management, Massachusetts Institute of Technology, Cambridge, MA. Disponível em: <http://hdl.handle.net/1721.1/46828>.

> **DEV 🟡** Referências: aprovadas pelo Vini em 14/11 (P3). O Cormen substituiu Held & Karp em 16/11 (P14). Estão no estilo SBC de autor-data, em ordem alfabética.
> - No Overleaf, usar o `.bib` do template. DOI: Haralick e Elliott `10.1016/0004-3702(80)90051-X`.
> - Little et al. (P15, 16/11): a referência é a do **working paper**, que é a versão lida (`resources/algorithmfortrav00litt.pdf`, que não vai para o git).
>   - Página de rosto: 1º de março de 1963. A capa não traz número; a etiqueta da biblioteca diz "no. 07-63", o que é uma inferência e por isso ficou fora da referência.
>   - A versão da revista é *Operations Research* 11(6):972–989, DOI `10.1287/opre.11.6.972`. A paginação é outra: **não** misturar as duas.
> - Cormen: confira na ficha catalográfica do exemplar da biblioteca o ano, a editora e a cidade. A edição traduzida pode ter saído pela Elsevier ou, em reimpressões mais novas, pela GEN/LTC. Ajuste a referência ao exemplar que for de fato consultado.
> - O link do CPH é o PDF público do autor; confira se é a mesma edição do PDF local (`C:\Users\Vini\Downloads\CPH.pdf`).
> - Use o agente `buscador-citacoes` para achar a página impressa de cada afirmação, o que exige ter os PDFs. Haralick e Elliott: PDF aberto (escaneado). Cormen: PDF da Minha Biblioteca ou fotos das páginas.

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
