# Registra a máquina onde os tempos do artigo foram medidos (pendência P6).
# Só lê informações do sistema; não roda o Main nem altera os CSVs.
#
# Uso (no Windows, dentro de Artigo/):
#   powershell -ExecutionPolicy Bypass -File coletar-ambiente.ps1
# Saída: resources/ambiente.txt  (fazer commit + push desse arquivo)
#
# Rodar NA MESMA MÁQUINA que gerou resources/resultados-*.csv.
# Se os CSVs forem regerados, rodar o script de novo.

$ErrorActionPreference = "Stop"
Set-Location -Path $PSScriptRoot

function Saida-Comando([string]$cmd) {
    try { (cmd /c "$cmd 2>&1") -join " | " } catch { "não encontrado" }
}

$cpu = Get-CimInstance Win32_Processor | Select-Object -First 1
$so  = Get-CimInstance Win32_OperatingSystem
$cs  = Get-CimInstance Win32_ComputerSystem
$ram = [math]::Round($cs.TotalPhysicalMemory / 1GB, 1)
$pentes = Get-CimInstance Win32_PhysicalMemory
$velRam = ($pentes | Where-Object { $_.Speed } | Select-Object -ExpandProperty Speed -Unique) -join "/"
$bateria = Get-CimInstance Win32_Battery -ErrorAction SilentlyContinue | Select-Object -First 1
$energia = if (-not $bateria) { "desktop (sem bateria)" }
           elseif ($bateria.BatteryStatus -eq 2) { "notebook na tomada" }
           else { "notebook NA BATERIA (o ideal é medir na tomada)" }
$plano = (Saida-Comando "powercfg /getactivescheme") -replace '^.*\((.*)\).*$', '$1'

$commitHead = Saida-Comando "git rev-parse --short HEAD"
$commitCsv  = Saida-Comando "git log -1 --format=%h_%ad --date=short -- resources/resultados-nqueens.csv resources/resultados-tsp.csv"
$csvAlterado = Saida-Comando "git status --porcelain -- resources/resultados-nqueens.csv resources/resultados-tsp.csv"

$linhas = @(
    "Ambiente da medição de tempos (P6) - gerado por coletar-ambiente.ps1",
    "Data da coleta: $(Get-Date -Format 'dd/MM/yyyy HH:mm')",
    "",
    "CPU: $($cpu.Name.Trim())",
    "Núcleos/threads: $($cpu.NumberOfCores) núcleos / $($cpu.NumberOfLogicalProcessors) threads",
    "Clock máximo informado: $($cpu.MaxClockSpeed) MHz",
    "RAM: $ram GB$(if ($velRam) { " ($velRam MT/s)" })",
    "SO: $($so.Caption) $($so.Version) ($($so.OSArchitecture))",
    "Energia: $energia; plano: $plano",
    "",
    "java -version: $(Saida-Comando 'java -version')",
    "javac -version: $(Saida-Comando 'javac -version')",
    "",
    "Commit atual (HEAD): $commitHead",
    "Último commit dos CSVs: $commitCsv",
    "CSVs com alteração não commitada: $(if ($csvAlterado) { $csvAlterado } else { 'não' })"
)

New-Item -ItemType Directory -Force -Path "resources" | Out-Null
$arquivo = Join-Path $PSScriptRoot "resources\ambiente.txt"
[System.IO.File]::WriteAllLines($arquivo, $linhas, (New-Object System.Text.UTF8Encoding($false)))

$linhas | ForEach-Object { Write-Host $_ }
Write-Host ""
Write-Host "Gravado em resources/ambiente.txt. Confira se esta é a máquina que gerou os CSVs e faça commit + push."
