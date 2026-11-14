# CLAUDE.md — contexto do projeto (leia antes de qualquer coisa)

Pasta `Artigo/` (código + texto) do **Artigo Científico (Trabalho Grau B)** da disciplina *Análise e Projeto de Algoritmos* (Unisinos, 2026/2, Profa. Maria Adelina Raupp Sganzerla). Integrantes: ver "Grupo".
Tema (rascunho do Rodrigo): *Análise do Impacto das Estratégias de Poda no Desempenho de Algoritmos de Busca* — força bruta × backtracking × branch and bound. Não estende os relatórios de Greedy/PD (são relatórios; este é um **artigo**).

**Prazo:** PDF no Moodle ("Tarefa | Aula 7 - Artigo Científico") até **23/11/2026 às 19:30**. Um integrante entrega. Formato **SBC**, **10 a 15 laudas**.

## REGRA CRÍTICA DA PROFESSORA (enunciado)

> LLMs permitidos **apenas para auxiliar na busca e revisão**. Texto gerado por IA = **plágio, nota zero**.

Consequência para o Claude neste repo:
- **Não redigir prosa do artigo** (resumo, introdução, fundamentação, discussão, conclusão…). As seções ficam com `{{texto do grupo}}` + bloco DEV de **material** (fatos, números, onde está no código, fontes a consultar), em tópicos curtos, nunca em parágrafos prontos para colar.
- Permitido: estrutura, tabelas de dados extraídas dos CSVs, scripts de medição/gráficos, conferência de números, busca de fontes (`buscador-citacoes`), **revisão** de texto escrito pelo grupo (apontar problemas; reescrita só pontual e se o aluno pedir).
- Isso substitui a regra geral da skill de "preencher em voz de relatório".

## Como trabalhar aqui (preferências combinadas)

- Idioma **pt-BR**; respostas diretas.
- Fonte de trabalho: `artigo/artigo-poda.md` (roteiro + dados + material). **Não usar Claude Docs.** O texto final é escrito pelo grupo em **LaTeX/Overleaf** com o template SBC da professora.
- Repositório: **o do Rodrigo** (`rodrigo-mello-hw08/unisinos-analise-e-projeto-algoritmos`), usado pela dupla para a faculdade. Não há repositório separado. Commits locais são permitidos; para push, pedir OK. **Não alterar o código existente** em `src/` sem pedido explícito. Plugin `relatorio`: skills `pendencias`, `retomar`, `handoff-para-grupo`, `exportar`, `reproducao`; agentes `mapeador-codigo`, `revisor-relatorio`, `buscador-citacoes`.
- Sessões de esqueleto: preencher o que dá para inferir (respeitando a regra acima), anotar pendência e seguir; parar só em bloqueio real.
- **Propor antes de aplicar** mudanças estruturais; perguntas em lote só para decisões do aluno.
- Ações externas (push, convites, instalar software, mensagens) só com confirmação.
- Sem pseudocódigo (salvo pedido); números em formato brasileiro; referências só as aprovadas, com página impressa.

## Convenção do `artigo-poda.md`

| Formato | Significa |
|---|---|
| texto normal, sem `>` | **vai para o artigo** |
| bloco `> **DEV ...**` | **nunca vai**: pendências, status (✅ 🟡 🔴), material, achados, comandos |
| `{{...}}` no texto | **lacuna** a preencher |

Estrutura: seções mínimas do **enunciado** (Título, Resumo/Abstract, 1 Introdução … 7 Conclusão, Referências). O template de relatório da disciplina **não se aplica** (é para relatórios de aula).

## Grupo

| Integrante | Contato | Responsável por |
|---|---|---|
| Vinicius de Avila Bezerra (Vini) | GitHub `Vini-boat` | dono deste repo |
| Rodrigo Mello (a confirmar, P7) | GitHub `rodrigo-mello-hw08` | código (`Artigo/` no repo dele), planos, provável máquina dos tempos |

Pedidos em aberto: `handoff-*.md`.

## Decisões já tomadas (não reabrir sem o aluno pedir)

- (14/11/2026) Regra anti-IA da professora: Claude não redige a prosa do artigo (ver acima).
- (14/11/2026, Vini) Problemas: **N rainhas + Caixeiro-Viajante (TSP)**. Mochila fora: o título e os objetivos do rascunho precisam ser ajustados.
- (14/11/2026, Vini) PDF em **LaTeX/Overleaf**, com o template SBC da professora (subpasta `template-latex` no Drive).
- (14/11/2026, Vini) Trabalhar dentro de `Artigo/` no repositório do Rodrigo, sem repositório próprio.
- (14/11/2026) Cabeçalho do enunciado diz "Simulação e Modelagem de Sistemas": erro de cópia; o link de entrega é de APA.

## Resultados-chave já verificados (para não rodar de novo)

- Origem: `resources/resultados-*.csv`, commit `03096d0` (Rodrigo, 12/11/2026). **Execução única, sem aquecimento, máquina não registrada** → tempos provisórios; contagens são determinísticas.
- N rainhas (N=4..7): soluções 2, 10, 4, 40 (iguais nas 3 variantes). Nós: FB 341/3906/55987/960800; BT 17/54/153/552; B&B 17/54/131/464.
- TSP (n=1,2,4,6,8,10): custo ótimo igual nas 3 variantes (n=10: 233). Rotas completas avaliadas FB = BT = (n−1)!/2 (n=10: 181440); B&B 11. Nós FB = BT (n=10: 986410); B&B 855.

## Estrutura do repositório

```
Artigo/
  CLAUDE.md               este arquivo
  planos/                 planos de execução do Rodrigo (plano-2 mochila ficou obsoleto)
  src/                    código Java (N rainhas, TSP) — ver Apêndice A do artigo-poda.md
  resources/              CSVs gerados pelo Main
  artigo/
    artigo-poda.md        roteiro + dados + material (convenção DEV/{{lacunas}})
    docs/                 enunciado, rascunho do Rodrigo, template de relatório (não usado), sbc/ (template SBC)
    images/, export/
```

Compilar e rodar (em `Artigo/`): `javac -encoding UTF-8 -d out src/Main.java src/metrics/*.java src/nqueens/*/*.java src/reporting/*.java src/tsp/*/*.java` e `java -Dfile.encoding=UTF-8 -cp out Main`. O `out/` já é ignorado pelo `.gitignore` da raiz.

## Restaurar o ambiente em outro computador

1. `git clone https://github.com/rodrigo-mello-hw08/unisinos-analise-e-projeto-algoritmos.git` e abrir a pasta `Artigo/`.
2. (sem passo extra)
3. Requisitos: JDK 21; Python 3 (conversões); LibreOffice (conferir PDF); LaTeX/Overleaf se P2 = LaTeX.
4. IDs do Drive (rebaixar com `curl -L -o x.docx "https://docs.google.com/document/d/<ID>/export?format=docx"`): enunciado `1GNFWAAwV5FrFjvpafwgLllY4GyHKJ5d1dwSgO-t-krI`; rascunho do Rodrigo `1E1benBOowwc-Cb33BEUcrcd0N34mS_IMY4acxinvTiQ` (pasta `artigo_cientifico` `1YJnefJ5vwjVKL314G72x20M4Bd7RmEqB`); pasta do template SBC da professora `1oKXMfdAKy0_p6HEil9mVq8mN3AMMNwnV` (subpasta `template-latex` `1KhCRXtngBuD7EDClr6d4jwPY_SkGOVVE`; arquivos: `https://drive.google.com/uc?export=download&id=<ID>`).

## Armadilhas conhecidas

- Windows: console em cp1252 — em Python use `sys.stdout.reconfigure(encoding='utf-8')` e rode com `python -I`.
- JVM: `-Dfile.encoding=UTF-8 -Dstdout.encoding=UTF-8`, `javac -encoding UTF-8`.
- `gh` pode não estar no PATH: `C:\Program Files\GitHub CLI\gh.exe`.

## Entrega

PDF no formato SBC (10–15 laudas), com link para o código (repo do Rodrigo, pasta `Artigo/`). Nada da entrega pode depender deste repositório.
