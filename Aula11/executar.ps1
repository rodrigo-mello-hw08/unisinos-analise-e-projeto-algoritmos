# Reproduz todos os resultados do relatorio da Aula 11 (Divisao e Conquista x PD x guloso) (Windows / PowerShell).
# Requisito: JDK 21+ (javac e java no PATH). Nenhuma outra dependencia.
# Uso:  powershell -ExecutionPolicy Bypass -File .\executar.ps1
$ErrorActionPreference = 'Stop'
Set-Location -LiteralPath $PSScriptRoot

$out = 'resultados'
New-Item -ItemType Directory -Force -Path 'out', $out | Out-Null
$jflags = @('-Dfile.encoding=UTF-8', '-Dstdout.encoding=UTF-8', '-Djava.awt.headless=true')

function Invoke-Java([string[]]$JavaArgs, [string]$OutFile = $null) {
    # Redireciona via cmd para gravar os bytes UTF-8 da JVM sem re-codificar (PowerShell 5.1 re-codificaria).
    $argLine = ($jflags + $JavaArgs) -join ' '
    if ($OutFile) { cmd /c "java $argLine > `"$OutFile`"" } else { cmd /c "java $argLine" }
    if ($LASTEXITCODE -ne 0) { throw "Falha ao executar: java $argLine" }
}

Write-Host "[1/5] Registrando ambiente -> $out\ambiente.txt"
$os  = Get-CimInstance Win32_OperatingSystem
$cpu = Get-CimInstance Win32_Processor | Select-Object -First 1
$ram = (Get-CimInstance Win32_ComputerSystem).TotalPhysicalMemory
$info = @(
    "Data: $(Get-Date -Format 'yyyy-MM-dd HH:mm:ss')",
    "SO: $($os.Caption) (versao $($os.Version), build $($os.BuildNumber))",
    "CPU: $($cpu.Name.Trim()) ($($cpu.NumberOfCores) nucleos / $($cpu.NumberOfLogicalProcessors) threads)",
    ("RAM: {0:N1} GB" -f ($ram / 1GB)),
    '--- java -version ---'
)
$info | Set-Content -Encoding UTF8 "$out\ambiente.txt"
cmd /c "java -version 2>&1" | Add-Content -Encoding UTF8 "$out\ambiente.txt"

Write-Host '[2/5] Compilando (src, incluindo src/analysis) -> out'
Get-ChildItem -Recurse -Filter *.java -Path 'src' | ForEach-Object { $_.FullName } |
    Set-Content -Encoding ASCII 'out\sources.txt'
javac -encoding UTF-8 -d 'out' '@out\sources.txt'
if ($LASTEXITCODE -ne 0) { throw 'Falha na compilacao' }

Write-Host "[3/5] Testes do projeto (Main: guloso, PD, divisao e conquista e comparacao) -> $out\saida-main.txt"
Invoke-Java @('-cp', 'out', 'Main') "$out\saida-main.txt"

Write-Host "[4/5] Contadores da divisao e conquista (chamadas, divisoes, podas, profundidade) -> $out\dc-contadores.txt"
Invoke-Java @('-cp', 'out', 'analysis.DcStats') "$out\dc-contadores.txt"

Write-Host "[5/5] Figuras (fluxograma e arvore de recursao da instancia pequena) -> $out\*.png"
Invoke-Java @('-cp', 'out', 'analysis.DcFigures', $out)

Write-Host "Concluido. Resultados em: $(Join-Path $PSScriptRoot $out)"
