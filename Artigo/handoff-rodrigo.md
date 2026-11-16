# Pedidos para o Rodrigo

## Aberto

- [ ] **P6 — Registrar a máquina dos tempos (16/11).** Os tempos do artigo (Tabelas 4–6, Figuras 7–9) vêm dos CSVs do commit `6041c18`. Falta dizer em que máquina eles foram gerados.
  1. `git pull`
  2. Em `Artigo/`, na **mesma máquina** que gerou os CSVs: `powershell -ExecutionPolicy Bypass -File coletar-ambiente.ps1`
  3. Conferir `resources/ambiente.txt` (CPU, RAM, SO, JDK, commit dos CSVs) e fazer commit + push dele.
  - Se os CSVs foram gerados em outra máquina, rodar o `Main` de novo nesta, depois o script, e subir os três arquivos juntos.
  - Esperado no arquivo: "Último commit dos CSVs: 6041c18_2026-11-16" e "CSVs com alteração não commitada: não".

**Mensagem para colar:**

> Rodrigo, falta só registrar a máquina dos tempos do artigo. Dá um pull e, dentro de `Artigo/`, roda `powershell -ExecutionPolicy Bypass -File coletar-ambiente.ps1` no mesmo PC em que tu gerou os CSVs do warmup. Ele cria `resources/ambiente.txt` com CPU, RAM, SO e JDK. Depois é só commit + push desse arquivo. Se os CSVs não foram gerados nesse PC, roda o Main de novo antes e sobe tudo junto.

## Respondido

- (16/11) P4 — medição com aquecimento: feita (`84478e2`, `6041c18`).
- (16/11) P11 — JUnit: desconsiderado.
