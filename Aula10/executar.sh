#!/usr/bin/env bash
# Reproduz todos os resultados do relatorio da Aula 10 (PD x guloso) (Linux / macOS / Git Bash).
# Requisito: JDK 21+ (javac e java no PATH). Nenhuma outra dependencia.
set -euo pipefail
cd "$(dirname "$0")"

OUT=resultados
mkdir -p out "$OUT"
JFLAGS="-Dfile.encoding=UTF-8 -Dstdout.encoding=UTF-8 -Djava.awt.headless=true"

echo "[1/6] Registrando ambiente -> $OUT/ambiente.txt"
{
  echo "Data: $(date)"
  echo "SO: $(uname -srv 2>/dev/null || echo n/d)"
  if command -v lscpu >/dev/null 2>&1; then echo "CPU: $(lscpu | sed -n 's/^Model name:[[:space:]]*//p' | head -1)"
  elif command -v sysctl >/dev/null 2>&1; then echo "CPU: $(sysctl -n machdep.cpu.brand_string 2>/dev/null || echo n/d)"
  else echo "CPU: n/d"; fi
  if [ -r /proc/meminfo ]; then echo "RAM: $(awk '/MemTotal/ {printf "%.1f GB", $2/1024/1024}' /proc/meminfo)"
  elif command -v sysctl >/dev/null 2>&1; then echo "RAM: $(sysctl -n hw.memsize 2>/dev/null | awk '{printf "%.1f GB", $1/1024/1024/1024}')"
  else echo "RAM: n/d"; fi
  echo "--- java -version ---"
  java -version 2>&1
} > "$OUT/ambiente.txt"

echo "[2/6] Compilando (src, incluindo src/analysis) -> out"
find src -name '*.java' > out/sources.txt
javac -encoding UTF-8 -d out @out/sources.txt

echo "[3/6] Testes do projeto (Main: guloso, PD e comparacao) -> $OUT/saida-main.txt"
java $JFLAGS -cp out Main > "$OUT/saida-main.txt"

echo "[4/6] Tabela DP da instancia pequena -> $OUT/tabela-dp-pequena.md"
java $JFLAGS -cp out analysis.DpTable > "$OUT/tabela-dp-pequena.md"

echo "[5/6] Benchmark de acerto do guloso (2000 instancias/ponto, semente fixa) -> $OUT/acerto-guloso.csv"
java $JFLAGS -cp out analysis.Benchmark 2000 "$OUT/acerto-guloso.csv"

echo "[6/6] Grafico -> $OUT/acerto-guloso.png"
java $JFLAGS -cp out analysis.ChartPng "$OUT/acerto-guloso.csv" "$OUT/acerto-guloso.png"

echo "Concluido. Resultados em: $(pwd)/$OUT"
