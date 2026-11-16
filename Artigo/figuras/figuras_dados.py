"""Figuras que dependem dos CSVs gerados pelo Main (resources/resultados-*.csv).

Rodar de novo sempre que os CSVs forem regerados. Gera PDF (Overleaf) + PNG (pré-visualização):
  nos-rainhas       nós explorados × N (escala log), 3 variantes
  nos-tsp           nós explorados × n (escala log), 3 variantes
  podas-rainhas     composição das podas por causa (coluna/diagonal/limite), BT e B&B
  tempo-rainhas     tempo com aquecimento (mediana) × N (escala log)
  tempo-tsp         tempo com aquecimento (mediana) × n (escala log)
  frio-aquecido     tempo sem aquecimento × com aquecimento, rainhas e TSP lado a lado

Uso (em Artigo/):
  py -3.12 figuras/figuras_dados.py
  py -3.12 figuras/figuras_dados.py --nqueens resources/resultados-nqueens.csv --tsp resources/resultados-tsp.csv
Colunas de tempo (desde o commit 6041c18 do Rodrigo):
  warmup_elapsed_ns   = sem aquecimento (1 execução, a primeira daquele solver naquele tamanho)
  tempo_decorrido_ns  = com aquecimento (mediana de 5 execuções feitas logo depois)
Sem a coluna warmup_elapsed_ns (CSVs antigos), frio-aquecido é pulado.
"""
import argparse
import csv
import math
import os
import sys
from collections import defaultdict

import matplotlib

matplotlib.use("Agg")
import matplotlib.pyplot as plt
from matplotlib.ticker import FuncFormatter, LogLocator, NullFormatter

AQUI = os.path.dirname(os.path.abspath(__file__))
RAIZ = os.path.dirname(AQUI)

TINTA = "#222222"
TINTA_2 = "#666666"
GRADE = "#e4e2dc"

# cor + marcador por variante (identidade nunca só pela cor; legível em P&B)
VARIANTES = [
    ("brute_force", "Força bruta", "#2a78d6", "o"),
    ("backtracking", "Backtracking", "#eb6834", "s"),
    ("branch_and_bound", "Branch and bound", "#1baf7a", "^"),
]
CAUSAS = [
    ("podas_por_coluna", "coluna", "#2a78d6"),
    ("podas_por_diagonal", "diagonal", "#eb6834"),
    ("podas_por_limite", "limite", "#1baf7a"),
]

plt.rcParams.update({
    "font.family": "DejaVu Sans",
    "font.size": 9,
    "pdf.fonttype": 42,
    "axes.edgecolor": TINTA_2,
    "axes.labelcolor": TINTA,
    "xtick.color": TINTA_2,
    "ytick.color": TINTA_2,
    "axes.spines.top": False,
    "axes.spines.right": False,
})


# ------------------------------------------------------------- utilidades
def br(x, casas=0):
    """Número no formato brasileiro (milhar com ponto, decimal com vírgula)."""
    s = f"{x:,.{casas}f}"
    return s.replace(",", "§").replace(".", ",").replace("§", ".")


def fmt_log(v, _pos=None):
    if v >= 1:
        return br(v)
    casas = max(0, -int(round(math.log10(v))))
    return br(v, casas)


def ler(caminho):
    """Lê o CSV e devolve {algoritmo: [linhas ordenadas por tamanho]} (tamanho em 'tam')."""
    por_alg = defaultdict(list)
    with open(caminho, encoding="utf-8") as f:
        for linha in csv.DictReader(f):
            tam = linha.get("n") or linha.get("quantidade_de_cidades")
            linha["tam"] = int(tam)
            por_alg[linha["algoritmo"]].append(linha)
    for v in por_alg.values():
        v.sort(key=lambda r: r["tam"])
    return por_alg


def eixo_log(ax):
    ax.set_yscale("log")
    ax.yaxis.set_major_locator(LogLocator(base=10))
    ax.yaxis.set_major_formatter(FuncFormatter(fmt_log))
    ax.yaxis.set_minor_formatter(NullFormatter())
    ax.grid(axis="y", color=GRADE, linewidth=0.6)
    ax.set_axisbelow(True)


def salvar(fig, saida, nome):
    for ext in ("pdf", "png"):
        fig.savefig(os.path.join(saida, f"{nome}.{ext}"), dpi=200, bbox_inches="tight")
    plt.close(fig)
    print("gerado:", nome)


def linhas_por_variante(ax, dados, coluna, escala=1.0, tam_min=1, estilo="-", rotular=True, alfa=1.0):
    tams_todos = set()
    rotulos = []
    # a primeira variante é desenhada mais grossa e por baixo: se outra tiver os mesmos valores
    # (ex.: nós da força bruta = nós do backtracking no TSP), ela continua visível como contorno
    for k, (chave, nome, cor, marc) in enumerate(VARIANTES):
        linhas = [r for r in dados.get(chave, []) if r["tam"] >= tam_min]
        if not linhas:
            continue
        xs = [r["tam"] for r in linhas]
        ys = [float(r[coluna]) * escala for r in linhas]
        tams_todos.update(xs)
        ax.plot(xs, ys, estilo, color=cor, marker=marc, markersize=8 if k == 0 else 6,
                linewidth=3.6 if k == 0 else 2, label=nome, alpha=alfa, zorder=2 + k,
                markeredgecolor="white", markeredgewidth=0.8)
        rotulos.append((xs[-1], ys[-1]))
    ax.set_xticks(sorted(tams_todos))
    if rotular:
        rotulos_diretos(ax, rotulos)
    return sorted(tams_todos)


def rotulos_diretos(ax, pontos, folga_pt=9):
    """Rótulo do valor no último ponto de cada série, afastando rótulos que colidiriam.

    Valores iguais viram um único rótulo.
    """
    ax.autoscale_view()
    dpi = ax.figure.dpi
    unicos = {}
    for x, y in pontos:
        unicos.setdefault(round(y, 6), (x, y))
    itens = []
    for x, y in unicos.values():
        _, py = ax.transData.transform((x, y))
        itens.append([py, py, x, y])  # [y ajustado, y original, x, valor]
    itens.sort(key=lambda t: t[0])
    folga = folga_pt * dpi / 72
    for i in range(1, len(itens)):
        if itens[i][0] - itens[i - 1][0] < folga:
            itens[i][0] = itens[i - 1][0] + folga
    for aj, orig, x, y in itens:
        ax.annotate(br(y, 0 if y >= 10 else 2), (x, y), xytext=(7, (aj - orig) * 72 / dpi),
                    textcoords="offset points", va="center", fontsize=7.5, color=TINTA)


# ------------------------------------------------------------- figuras
def fig_nos(dados, saida, nome, rotulo_x, tam_min):
    fig, ax = plt.subplots(figsize=(5.2, 3.3))
    eixo_log(ax)
    tams = linhas_por_variante(ax, dados, "nos_explorados", tam_min=tam_min)
    ax.set_xlim(min(tams) - 0.3, max(tams) + 0.9)
    ax.set_xlabel(rotulo_x)
    ax.set_ylabel("nós explorados (escala log)")
    ax.legend(frameon=False, fontsize=8, loc="upper left")
    salvar(fig, saida, nome)


def fig_tempo(dados, saida, nome, rotulo_x, tam_min):
    fig, ax = plt.subplots(figsize=(5.2, 3.3))
    eixo_log(ax)
    tams = linhas_por_variante(ax, dados, "tempo_decorrido_ns", escala=1e-6, tam_min=tam_min)
    ax.set_xlim(min(tams) - 0.3, max(tams) + 0.9)
    ax.set_xlabel(rotulo_x)
    ax.set_ylabel("tempo com aquecimento (ms, escala log)")
    ax.legend(frameon=False, fontsize=8, loc="upper left")
    salvar(fig, saida, nome)


def fig_podas(dados, saida):
    algs = [("backtracking", "Backtracking"), ("branch_and_bound", "Branch and bound")]
    fig, axs = plt.subplots(1, 2, figsize=(6.4, 3.0), sharey=True)
    for ax, (chave, titulo) in zip(axs, algs):
        linhas = dados.get(chave, [])
        xs = [r["tam"] for r in linhas]
        base = [0.0] * len(linhas)
        totais = [sum(int(r[c]) for c, _, _ in CAUSAS) for r in linhas]
        for col, nome, cor in CAUSAS:
            vals = [int(r[col]) for r in linhas]
            if not any(vals):
                continue
            frac = [100 * v / t if t else 0 for v, t in zip(vals, totais)]
            ax.bar(xs, frac, bottom=base, width=0.62, color=cor, label=nome,
                   edgecolor="white", linewidth=1)
            base = [b + f for b, f in zip(base, frac)]
        for x, t in zip(xs, totais):
            ax.text(x, 101.5, br(t), ha="center", va="bottom", fontsize=7.5, color=TINTA)
        ax.set_title(titulo, fontsize=9, color=TINTA, pad=14)
        ax.set_xticks(xs)
        ax.set_xlabel("N")
        ax.set_ylim(0, 100)
        ax.grid(axis="y", color=GRADE, linewidth=0.6)
        ax.set_axisbelow(True)
    axs[0].set_ylabel("% das tentativas podadas")
    alcas, nomes = {}, []
    for ax in axs:
        for h, l in zip(*ax.get_legend_handles_labels()):
            alcas.setdefault(l, h)
    fig.legend(list(alcas.values()), list(alcas.keys()), loc="lower center", ncol=3,
               frameon=False, fontsize=8, bbox_to_anchor=(0.5, -0.06))
    fig.text(0.5, 0.955, "acima de cada barra: total de tentativas podadas", ha="center",
             fontsize=7, color=TINTA_2)
    fig.tight_layout(rect=(0, 0.04, 1, 0.95))
    salvar(fig, saida, "podas-rainhas")


FRIO, AQUECIDO = "warmup_elapsed_ns", "tempo_decorrido_ns"


def tem_coluna(dados, coluna):
    return any(coluna in r for linhas in dados.values() for r in linhas)


def fig_frio_aquecido(paineis, saida):
    """Painéis lado a lado (um por problema, eixos independentes): tracejado = sem aquecimento.

    paineis: lista de (título, dados, rótulo_x, tamanho_mínimo); as duas medições vêm do mesmo CSV.
    """
    fig, axs = plt.subplots(1, len(paineis), figsize=(3.4 * len(paineis), 3.2), squeeze=False)
    for ax, (titulo, d, rx, tm) in zip(axs[0], paineis):
        eixo_log(ax)
        linhas_por_variante(ax, d, FRIO, 1e-6, tm, estilo="--", rotular=False, alfa=0.55)
        linhas_por_variante(ax, d, AQUECIDO, 1e-6, tm, estilo="-", rotular=False)
        ax.set_title(titulo, fontsize=9, color=TINTA)
        ax.set_xlabel(rx)
    axs[0][0].set_ylabel("tempo (ms, escala log)")
    from matplotlib.lines import Line2D
    alcas = [Line2D([], [], color=c, marker=m, linewidth=2, label=n) for _, n, c, m in VARIANTES]
    alcas += [Line2D([], [], color=TINTA_2, linestyle="--", label="sem aquecimento"),
              Line2D([], [], color=TINTA_2, linestyle="-", label="com aquecimento")]
    fig.legend(handles=alcas, loc="lower center", ncol=5, frameon=False, fontsize=7.5,
               bbox_to_anchor=(0.5, -0.08))
    fig.tight_layout(rect=(0, 0.05, 1, 1))
    salvar(fig, saida, "frio-aquecido")


def main():
    sys.stdout.reconfigure(encoding="utf-8")
    p = argparse.ArgumentParser(description=__doc__, formatter_class=argparse.RawDescriptionHelpFormatter)
    p.add_argument("--nqueens", default=os.path.join(RAIZ, "resources", "resultados-nqueens.csv"))
    p.add_argument("--tsp", default=os.path.join(RAIZ, "resources", "resultados-tsp.csv"))
    p.add_argument("--tsp-min-n", type=int, default=4,
                   help="menor n do TSP nos gráficos (padrão 4: n=1 e 2 são triviais)")
    p.add_argument("--saida", default=os.path.join(RAIZ, "artigo", "images"))
    a = p.parse_args()
    os.makedirs(a.saida, exist_ok=True)

    rainhas, tsp = ler(a.nqueens), ler(a.tsp)
    fig_nos(rainhas, a.saida, "nos-rainhas", "N (tamanho do tabuleiro)", 1)
    fig_nos(tsp, a.saida, "nos-tsp", "n (número de cidades)", a.tsp_min_n)
    fig_podas(rainhas, a.saida)
    fig_tempo(rainhas, a.saida, "tempo-rainhas", "N (tamanho do tabuleiro)", 1)
    fig_tempo(tsp, a.saida, "tempo-tsp", "n (número de cidades)", a.tsp_min_n)

    paineis = []
    if tem_coluna(rainhas, FRIO):
        paineis.append(("N rainhas", rainhas, "N", 1))
    if tem_coluna(tsp, FRIO):
        paineis.append(("Caixeiro-viajante", tsp, "n", a.tsp_min_n))
    if paineis:
        fig_frio_aquecido(paineis, a.saida)
    else:
        print(f"frio-aquecido: pulado (os CSVs não têm a coluna {FRIO})")


if __name__ == "__main__":
    main()
