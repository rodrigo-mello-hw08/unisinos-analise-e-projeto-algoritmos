"""Figuras que NÃO dependem dos CSVs de resultados (dependem só do algoritmo/instância).

Gera, em PDF (para o Overleaf) e PNG (pré-visualização), em ../artigo/images/:
  - arvore-rainhas-4.{pdf,png}   árvore de estados do backtracking para N=4
  - solucoes-rainhas-4.{pdf,png} as soluções de N=4 em tabuleiro
  - tsp-instancia-<n>.{pdf,png}  instância do TSP (mesmo gerador do Main) com a rota ótima

Uso (em Artigo/):
  py -3.12 figuras/figuras_fixas.py [--tsp-n 6] [--saida artigo/images]
O grafo do TSP chama o auxiliar Java (figuras/ExportarInstanciaTsp.java); o projeto
precisa estar compilado em out/ — o script compila o auxiliar se faltar.
"""
import argparse
import json
import math
import os
import subprocess
import sys

import matplotlib

matplotlib.use("Agg")
import matplotlib.pyplot as plt
from matplotlib.patches import Rectangle

AQUI = os.path.dirname(os.path.abspath(__file__))
RAIZ = os.path.dirname(AQUI)  # Artigo/

TINTA = "#222222"
TINTA_2 = "#666666"
GRADE = "#bbbbbb"
AZUL = "#2a78d6"
VERDE = "#1baf7a"
LARANJA = "#eb6834"
CASA_CLARA = "#f3f1ea"
CASA_ESCURA = "#d9d5c7"
RAINHA = "♛"

plt.rcParams.update({
    "font.family": "DejaVu Sans",
    "font.size": 9,
    "pdf.fonttype": 42,
})


def salvar(fig, saida, nome):
    for ext in ("pdf", "png"):
        fig.savefig(os.path.join(saida, f"{nome}.{ext}"), dpi=200, bbox_inches="tight")
    plt.close(fig)
    print("gerado:", nome)


# ---------------------------------------------------------------- N rainhas
def arvore_backtracking(n):
    """Reproduz NQueensBacktracking.search e devolve a árvore de nós explorados.

    Cada nó: dict(rainhas=[colunas], filhos=[...], podas_coluna, podas_diagonal).
    """
    cols, down, up = [False] * n, [False] * (2 * n - 1), [False] * (2 * n - 1)

    def busca(linha, rainhas):
        no = {"rainhas": list(rainhas), "filhos": [], "pc": 0, "pd": 0}
        if linha == n:
            return no
        for c in range(n):
            d, u = linha - c + n - 1, linha + c
            if cols[c]:
                no["pc"] += 1
                continue
            if down[d] or up[u]:
                no["pd"] += 1
                continue
            cols[c] = down[d] = up[u] = True
            no["filhos"].append(busca(linha + 1, rainhas + [c]))
            cols[c] = down[d] = up[u] = False
        return no

    return busca(0, [])


def percorrer(no):
    yield no
    for f in no["filhos"]:
        yield from percorrer(f)


def mini_tabuleiro(ax, x0, y0, lado, n, rainhas, borda, largura_borda):
    casa = lado / n
    for r in range(n):
        for c in range(n):
            cor = CASA_CLARA if (r + c) % 2 == 0 else CASA_ESCURA
            ax.add_patch(Rectangle((x0 + c * casa, y0 + (n - 1 - r) * casa), casa, casa,
                                   facecolor=cor, edgecolor="none"))
    for r, c in enumerate(rainhas):
        ax.add_patch(Rectangle((x0 + c * casa, y0 + (n - 1 - r) * casa), casa, casa,
                               facecolor=TINTA, edgecolor="none"))
    ax.add_patch(Rectangle((x0, y0), lado, lado, facecolor="none",
                           edgecolor=borda, linewidth=largura_borda))


def figura_arvore(saida, n=4):
    raiz = arvore_backtracking(n)
    nos = list(percorrer(raiz))
    pc = sum(x["pc"] for x in nos)
    pd = sum(x["pd"] for x in nos)
    # Confere com o CSV do código (N=4): 17 nós, 28 podas por coluna, 16 por diagonal.
    print(f"árvore N={n}: {len(nos)} nós, podas coluna={pc}, diagonal={pd}")

    # layout: folhas em x consecutivos, pai no meio dos filhos
    proximo = [0]

    def posicionar(no, prof):
        no["y"] = -prof
        if not no["filhos"]:
            no["x"] = proximo[0]
            proximo[0] += 1
        else:
            for f in no["filhos"]:
                posicionar(f, prof + 1)
            no["x"] = sum(f["x"] for f in no["filhos"]) / len(no["filhos"])

    posicionar(raiz, 0)
    folhas = proximo[0]
    esp_x, esp_y, lado = 1.25, 1.55, 0.78

    fig, ax = plt.subplots(figsize=(max(6.0, folhas * 0.95), (n + 1) * 1.05))
    for no in nos:
        for f in no["filhos"]:
            ax.plot([no["x"] * esp_x, f["x"] * esp_x],
                    [no["y"] * esp_y - lado / 2, f["y"] * esp_y + lado / 2],
                    color=GRADE, linewidth=1, zorder=0)
    for no in nos:
        x, y = no["x"] * esp_x, no["y"] * esp_y
        solucao = len(no["rainhas"]) == n
        beco = not solucao and not no["filhos"]
        borda, lb = (VERDE, 2.4) if solucao else ((LARANJA, 2.4) if beco else (TINTA_2, 0.8))
        mini_tabuleiro(ax, x - lado / 2, y - lado / 2, lado, n, no["rainhas"], borda, lb)
        if no["pc"] or no["pd"]:
            ax.text(x + lado / 2 + 0.05, y - lado / 2, f"c{no['pc']}\nd{no['pd']}",
                    fontsize=6.5, color=TINTA_2, va="bottom", ha="left", linespacing=1.1)
    for prof in range(n + 1):
        ax.text(-0.9, -prof * esp_y, f"linha {prof}" if prof else "raiz",
                fontsize=7.5, color=TINTA_2, ha="right", va="center")

    from matplotlib.lines import Line2D
    legenda = [
        Line2D([], [], marker="s", linestyle="none", markerfacecolor="none",
               markeredgecolor=VERDE, markeredgewidth=2, markersize=9, label="solução"),
        Line2D([], [], marker="s", linestyle="none", markerfacecolor="none",
               markeredgecolor=LARANJA, markeredgewidth=2, markersize=9,
               label="sem continuação (todas as colunas podadas)"),
        Line2D([], [], linestyle="none", label="c / d = tentativas podadas por coluna / diagonal"),
    ]
    ax.legend(handles=legenda, loc="upper center", bbox_to_anchor=(0.5, -0.02),
              ncol=3, frameon=False, fontsize=7.5, handletextpad=0.4, columnspacing=1.2)
    ax.set_xlim(-2.0, (folhas - 1) * esp_x + 1.1)
    ax.set_ylim(-n * esp_y - 0.6, 0.6)
    ax.set_aspect("equal")
    ax.axis("off")
    salvar(fig, saida, f"arvore-rainhas-{n}")
    return len(nos), pc, pd


def figura_solucoes(saida, n=4):
    sols = [x["rainhas"] for x in percorrer(arvore_backtracking(n)) if len(x["rainhas"]) == n]
    fig, axs = plt.subplots(1, len(sols), figsize=(2.0 * len(sols), 2.2))
    for k, (ax, s) in enumerate(zip(axs, sols)):
        for r in range(n):
            for c in range(n):
                ax.add_patch(Rectangle((c, n - 1 - r), 1, 1,
                                       facecolor=CASA_CLARA if (r + c) % 2 == 0 else CASA_ESCURA))
        for r, c in enumerate(s):
            ax.text(c + 0.5, n - 1 - r + 0.47, RAINHA, ha="center", va="center",
                    fontsize=22, color=TINTA)
        ax.add_patch(Rectangle((0, 0), n, n, facecolor="none", edgecolor=TINTA, linewidth=1))
        ax.set_xlim(0, n)
        ax.set_ylim(0, n)
        ax.set_aspect("equal")
        ax.set_xticks([i + 0.5 for i in range(n)], [str(i + 1) for i in range(n)])
        ax.set_yticks([n - 1 - i + 0.5 for i in range(n)], [str(i + 1) for i in range(n)])
        ax.tick_params(length=0, labelsize=7, colors=TINTA_2)
        for e in ax.spines.values():
            e.set_visible(False)
        ax.set_title("(" + "abcdefgh"[k] + ")  colunas " + ", ".join(str(c + 1) for c in s),
                     fontsize=8, color=TINTA)
    fig.tight_layout()
    salvar(fig, saida, f"solucoes-rainhas-{n}")


# ---------------------------------------------------------------- TSP
def instancia_tsp(n):
    cp_aux = os.path.join(AQUI, "out")
    if not os.path.exists(os.path.join(cp_aux, "ExportarInstanciaTsp.class")):
        subprocess.run(["javac", "-encoding", "UTF-8", "-cp", os.path.join(RAIZ, "out"), "-d", cp_aux,
                        os.path.join(AQUI, "ExportarInstanciaTsp.java")], check=True)
    cp = os.pathsep.join([os.path.join(RAIZ, "out"), cp_aux])
    saida = subprocess.run(["java", "-cp", cp, "ExportarInstanciaTsp", str(n)],
                           check=True, capture_output=True, text=True).stdout
    return json.loads(saida)


def figura_tsp(saida, n):
    dados = instancia_tsp(n)
    dist, rota = dados["distancias"], dados["rota"]
    print(f"TSP n={n}: rota ótima {rota}, custo {dados['custo']}")
    arestas_rota = {frozenset((rota[i], rota[i + 1])) for i in range(len(rota) - 1)}
    # cidades dispostas no círculo na ordem da rota ótima: a rota vira o contorno (sem cruzamentos)
    ordem = {cidade: k for k, cidade in enumerate(rota[:-1])}
    pos = {i: (math.cos(math.pi / 2 - 2 * math.pi * ordem[i] / n),
               math.sin(math.pi / 2 - 2 * math.pi * ordem[i] / n)) for i in range(n)}
    fig, (ax, axm) = plt.subplots(1, 2, figsize=(8.6, 4.2), gridspec_kw={"width_ratios": [1, 1.05]})
    # grafo: só as arestas da rota levam peso; as demais ficam como contexto (pesos na matriz)
    for i in range(n):
        for j in range(i + 1, n):
            na_rota = frozenset((i, j)) in arestas_rota
            (x1, y1), (x2, y2) = pos[i], pos[j]
            ax.plot([x1, x2], [y1, y2], color=AZUL if na_rota else "#dddddd",
                    linewidth=2.6 if na_rota else 0.7, zorder=2 if na_rota else 1)
            if na_rota:
                ax.text((x1 + x2) / 2, (y1 + y2) / 2, str(dist[i][j]), ha="center", va="center",
                        zorder=3, fontsize=9, fontweight="bold", color=TINTA,
                        bbox=dict(boxstyle="round,pad=0.15", facecolor="white", edgecolor="none"))
    for i, (x, y) in pos.items():
        ax.scatter([x], [y], s=430, color="white", edgecolors=TINTA, linewidths=1.2, zorder=4)
        ax.text(x, y, str(i), ha="center", va="center", fontsize=10, color=TINTA, zorder=5)
    ax.text(0, -1.32, "rota ótima: " + " → ".join(map(str, rota)) + f"   (custo {dados['custo']})",
            ha="center", fontsize=8.5, color=TINTA)
    ax.set_xlim(-1.25, 1.25)
    ax.set_ylim(-1.42, 1.2)
    ax.set_aspect("equal")
    ax.axis("off")

    # matriz de distâncias (simétrica); entradas da rota em negrito sobre fundo azul claro
    for i in range(n):
        axm.text(i + 0.5, n + 0.45, str(i), ha="center", va="center", fontsize=9, color=TINTA_2)
        axm.text(-0.45, n - 1 - i + 0.5, str(i), ha="center", va="center", fontsize=9, color=TINTA_2)
        for j in range(n):
            na_rota = frozenset((i, j)) in arestas_rota
            y = n - 1 - i
            if na_rota:
                axm.add_patch(Rectangle((j, y), 1, 1, facecolor="#cde2fb", edgecolor="none"))
            axm.add_patch(Rectangle((j, y), 1, 1, facecolor="none", edgecolor=GRADE, linewidth=0.5))
            axm.text(j + 0.5, y + 0.5, "–" if i == j else str(dist[i][j]), ha="center", va="center",
                     fontsize=9, color=TINTA if na_rota else TINTA_2,
                     fontweight="bold" if na_rota else "normal")
    axm.set_xlim(-0.9, n + 0.1)
    axm.set_ylim(-0.6, n + 0.9)
    axm.set_aspect("equal")
    axm.axis("off")
    axm.set_title("distâncias", fontsize=9, color=TINTA, pad=2)
    fig.tight_layout()
    salvar(fig, saida, f"tsp-instancia-{n}")


def main():
    sys.stdout.reconfigure(encoding="utf-8")
    p = argparse.ArgumentParser(description=__doc__, formatter_class=argparse.RawDescriptionHelpFormatter)
    p.add_argument("--tsp-n", type=int, default=6, help="tamanho da instância do TSP desenhada (padrão 6)")
    p.add_argument("--saida", default=os.path.join(RAIZ, "artigo", "images"))
    a = p.parse_args()
    os.makedirs(a.saida, exist_ok=True)
    figura_arvore(a.saida)
    figura_solucoes(a.saida)
    figura_tsp(a.saida, a.tsp_n)


if __name__ == "__main__":
    main()
