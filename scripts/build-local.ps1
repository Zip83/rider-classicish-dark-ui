param(
  [string]$RiderHome = "$env:LOCALAPPDATA\Programs\Rider 2",
  [string]$OutDir = ""
)

$ErrorActionPreference = "Stop"

$repoRoot = Split-Path -Parent (Split-Path -Parent $MyInvocation.MyCommand.Path)
if (-not $OutDir) {
  $OutDir = Join-Path $repoRoot "dist"
}

function Assert-ChildPath {
  param(
    [string]$Path,
    [string]$Parent
  )

  $resolvedPath = [System.IO.Path]::GetFullPath($Path)
  $resolvedParent = [System.IO.Path]::GetFullPath($Parent)

  if (-not $resolvedPath.StartsWith($resolvedParent, [System.StringComparison]::OrdinalIgnoreCase)) {
    throw "Refusing to modify path outside repository: $resolvedPath"
  }
}

function New-ZipArchiveFromDirectory {
  param(
    [string]$SourceDir,
    [string]$DestinationPath
  )

  Add-Type -AssemblyName System.IO.Compression
  Add-Type -AssemblyName System.IO.Compression.FileSystem

  if (Test-Path -LiteralPath $DestinationPath) {
    Remove-Item -LiteralPath $DestinationPath -Force
  }

  $sourceRoot = [System.IO.Path]::GetFullPath($SourceDir).TrimEnd("\", "/")
  $archive = [System.IO.Compression.ZipFile]::Open($DestinationPath, [System.IO.Compression.ZipArchiveMode]::Create)

  try {
    $files = Get-ChildItem -LiteralPath $SourceDir -File -Recurse

    foreach ($file in $files) {
      $filePath = [System.IO.Path]::GetFullPath($file.FullName)
      $entryName = $filePath.Substring($sourceRoot.Length).TrimStart("\", "/").Replace("\", "/")
      [System.IO.Compression.ZipFileExtensions]::CreateEntryFromFile($archive, $file.FullName, $entryName) | Out-Null
    }
  }
  finally {
    $archive.Dispose()
  }
}

$javac = Join-Path $RiderHome "jbr\bin\javac.exe"
if (-not (Test-Path -LiteralPath $javac)) {
  throw "Cannot find javac.exe at: $javac"
}

$classes = Join-Path $OutDir "classes"
$pluginDir = Join-Path $OutDir "RiderClassicishDarkUi"
$pluginLib = Join-Path $pluginDir "lib"
$jarPath = Join-Path $pluginLib "rider-classicish-dark-ui.jar"
$zipPath = Join-Path $OutDir "rider-classicish-dark-ui.zip"
$stage = Join-Path $OutDir "jar-stage"
$sourceRoot = Join-Path $repoRoot "src\main\java"
$sourceList = Join-Path $OutDir "java-sources.txt"

foreach ($path in @($classes, $stage)) {
  Assert-ChildPath -Path $path -Parent $repoRoot

  if (Test-Path -LiteralPath $path) {
    Remove-Item -LiteralPath $path -Recurse -Force
  }
}

New-Item -ItemType Directory -Force -Path $classes | Out-Null
New-Item -ItemType Directory -Force -Path $pluginLib | Out-Null

$sourceFiles = Get-ChildItem -LiteralPath $sourceRoot -Filter "*.java" -Recurse | Sort-Object FullName
if (-not $sourceFiles) {
  throw "No Java source files found under: $sourceRoot"
}

$sourceFileArguments = $sourceFiles | ForEach-Object {
  $normalizedPath = $_.FullName.Replace("\", "/")
  "`"$normalizedPath`""
}
$utf8NoBom = New-Object System.Text.UTF8Encoding($false)
[System.IO.File]::WriteAllLines($sourceList, [string[]]$sourceFileArguments, $utf8NoBom)

$classpath = @(
  (Join-Path $RiderHome "lib\*"),
  (Join-Path $RiderHome "lib\frontend\*")
) -join ";"

& $javac -cp $classpath -d $classes "@$sourceList"
if ($LASTEXITCODE -ne 0) {
  throw "javac failed with exit code $LASTEXITCODE"
}

New-Item -ItemType Directory -Force -Path (Join-Path $stage "META-INF") | Out-Null

Copy-Item -LiteralPath (Join-Path $repoRoot "src\main\resources\META-INF\plugin.xml") -Destination (Join-Path $stage "META-INF\plugin.xml") -Force
Copy-Item -Path (Join-Path $classes "*") -Destination $stage -Recurse -Force

New-ZipArchiveFromDirectory -SourceDir $stage -DestinationPath $jarPath
New-ZipArchiveFromDirectory -SourceDir $pluginDir -DestinationPath $zipPath

Write-Host "Built plugin:"
Write-Host "  $pluginDir"
Write-Host "  $zipPath"
