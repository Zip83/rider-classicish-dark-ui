param(
  [string]$ConfigDir = "$env:APPDATA\JetBrains\Rider2026.3",
  [string]$VmOptionsPath = "$env:APPDATA\JetBrains\Rider2026.3\Rider64.exe.vmoptions"
)

$ErrorActionPreference = "Stop"

function New-ApplicationXml {
  $doc = New-Object System.Xml.XmlDocument
  [void]$doc.AppendChild($doc.CreateElement("application"))
  return $doc
}

function Read-XmlOrCreate {
  param([string]$Path)
  if (Test-Path -LiteralPath $Path) {
    $doc = New-Object System.Xml.XmlDocument
    $doc.PreserveWhitespace = $true
    $doc.Load($Path)
    return $doc
  }
  return New-ApplicationXml
}

function Save-Xml {
  param(
    [System.Xml.XmlDocument]$Doc,
    [string]$Path
  )
  $dir = Split-Path -Parent $Path
  New-Item -ItemType Directory -Force -Path $dir | Out-Null
  $settings = New-Object System.Xml.XmlWriterSettings
  $settings.Indent = $true
  $settings.OmitXmlDeclaration = $true
  $writer = [System.Xml.XmlWriter]::Create($Path, $settings)
  $Doc.Save($writer)
  $writer.Dispose()
}

function Get-OrCreateComponent {
  param(
    [System.Xml.XmlDocument]$Doc,
    [string]$Name
  )
  $component = $Doc.SelectSingleNode("/application/component[@name='$Name']")
  if (-not $component) {
    $component = $Doc.CreateElement("component")
    $nameAttr = $Doc.CreateAttribute("name")
    $nameAttr.Value = $Name
    [void]$component.Attributes.Append($nameAttr)
    [void]$Doc.DocumentElement.AppendChild($component)
  }
  return $component
}

function Set-Option {
  param(
    [System.Xml.XmlDocument]$Doc,
    [System.Xml.XmlElement]$Component,
    [string]$Name,
    [string]$Value
  )
  $option = $Component.SelectSingleNode("option[@name='$Name']")
  if (-not $option) {
    $option = $Doc.CreateElement("option")
    $nameAttr = $Doc.CreateAttribute("name")
    $nameAttr.Value = $Name
    [void]$option.Attributes.Append($nameAttr)
    [void]$Component.AppendChild($option)
  }
  $valueAttr = $option.Attributes["value"]
  if (-not $valueAttr) {
    $valueAttr = $Doc.CreateAttribute("value")
    [void]$option.Attributes.Append($valueAttr)
  }
  $valueAttr.Value = $Value
}

function Set-RegistryEntry {
  param(
    [System.Xml.XmlDocument]$Doc,
    [System.Xml.XmlElement]$Registry,
    [string]$Key,
    [string]$Value,
    [string]$Source = "SYSTEM"
  )
  $entry = $Registry.SelectSingleNode("entry[@key='$Key']")
  if (-not $entry) {
    $entry = $Doc.CreateElement("entry")
    $keyAttr = $Doc.CreateAttribute("key")
    $keyAttr.Value = $Key
    [void]$entry.Attributes.Append($keyAttr)
    [void]$Registry.AppendChild($entry)
  }
  foreach ($pair in @{"value" = $Value; "source" = $Source}.GetEnumerator()) {
    $attr = $entry.Attributes[$pair.Key]
    if (-not $attr) {
      $attr = $Doc.CreateAttribute($pair.Key)
      [void]$entry.Attributes.Append($attr)
    }
    $attr.Value = $pair.Value
  }
}

function Backup-Path {
  param(
    [string]$Path,
    [string]$BackupRoot
  )
  if (Test-Path -LiteralPath $Path) {
    New-Item -ItemType Directory -Force -Path $BackupRoot | Out-Null
    Copy-Item -LiteralPath $Path -Destination (Join-Path $BackupRoot (Split-Path -Leaf $Path)) -Force
  }
}

function Set-JsonProperty {
  param(
    [object]$Object,
    [string]$Name,
    [string]$Value
  )
  if ($Object.PSObject.Properties[$Name]) {
    $Object.$Name = $Value
  }
  else {
    Add-Member -InputObject $Object -MemberType NoteProperty -Name $Name -Value $Value
  }
}

$running = Get-Process -Name "rider64", "Rider.Backend" -ErrorAction SilentlyContinue
if ($running) {
  Write-Host "Close Rider 2026.3 EAP first. Running processes:"
  $running | Select-Object Name, Id | Format-Table -AutoSize
  exit 2
}

$scriptRoot = Split-Path -Parent $MyInvocation.MyCommand.Path
$repoRoot = Split-Path -Parent $scriptRoot
$pluginSourceCandidates = @(
  (Join-Path $scriptRoot "RiderClassicishDarkUi"),
  (Join-Path $repoRoot "RiderClassicishDarkUi"),
  (Join-Path $repoRoot "dist\RiderClassicishDarkUi")
)
$pluginSource = $pluginSourceCandidates | Where-Object { Test-Path -LiteralPath $_ } | Select-Object -First 1
$pluginTarget = Join-Path $ConfigDir "plugins\RiderClassicishDarkUi"
$legacyPluginTargets = @(
  (Join-Path $ConfigDir "plugins\OldUiIconsOnly")
)
$optionsDir = Join-Path $ConfigDir "options"
$backupRoot = Join-Path $ConfigDir ("_codex-classicish-backups\" + (Get-Date -Format "yyyyMMdd-HHmmss"))

New-Item -ItemType Directory -Force -Path $optionsDir | Out-Null

$pathsToBackup = @(
  (Join-Path $optionsDir "ui.lnf.xml"),
  (Join-Path $optionsDir "laf.xml"),
  (Join-Path $optionsDir "colors.scheme.xml"),
  (Join-Path $optionsDir "ide.general.xml"),
  (Join-Path $optionsDir "other.xml"),
  (Join-Path $ConfigDir "disabled_plugins.txt"),
  $VmOptionsPath
)

foreach ($path in $pathsToBackup) {
  Backup-Path -Path $path -BackupRoot $backupRoot
}

if (-not $pluginSource) {
  throw "Missing plugin payload. Run scripts\build.ps1 first, or place RiderClassicishDarkUi next to the install script."
}

New-Item -ItemType Directory -Force -Path (Split-Path -Parent $pluginTarget) | Out-Null
foreach ($legacyPluginTarget in $legacyPluginTargets) {
  if (Test-Path -LiteralPath $legacyPluginTarget) {
    Remove-Item -LiteralPath $legacyPluginTarget -Recurse -Force
  }
}
if (Test-Path -LiteralPath $pluginTarget) {
  Remove-Item -LiteralPath $pluginTarget -Recurse -Force
}
Copy-Item -LiteralPath $pluginSource -Destination $pluginTarget -Recurse -Force

$disabledPath = Join-Path $ConfigDir "disabled_plugins.txt"
if (Test-Path -LiteralPath $disabledPath) {
  $disabled = Get-Content -LiteralPath $disabledPath
  $disabled = $disabled | Where-Object {
    $_ -ne "local.codex.rider.classicish.dark.ui" -and
    $_ -ne "local.codex.rider.classicish.dark.icons" -and
    $_ -ne "local.codex.old.ui.icons.only" -and
    $_ -ne "io.github.zip83.rider.classicish.dark.ui"
  }
  Set-Content -LiteralPath $disabledPath -Value $disabled -Encoding UTF8
}

$uiPath = Join-Path $optionsDir "ui.lnf.xml"
$ui = Read-XmlOrCreate -Path $uiPath
$uiSettings = Get-OrCreateComponent -Doc $ui -Name "UISettings"
Set-Option -Doc $ui -Component $uiSettings -Name "compactTreeIndents" -Value "true"
Set-Option -Doc $ui -Component $uiSettings -Name "DIFFERENTIATE_PROJECTS" -Value "false"
Set-Option -Doc $ui -Component $uiSettings -Name "SHOW_MAIN_MENU_MODE" -Value "SEPARATE_TOOLBAR"
Set-Option -Doc $ui -Component $uiSettings -Name "SHOW_MAIN_TOOLBAR" -Value "true"
Set-Option -Doc $ui -Component $uiSettings -Name "SHOW_PREVIEW_IN_SEARCH_EVERYWHERE" -Value "true"
Set-Option -Doc $ui -Component $uiSettings -Name "UI_DENSITY" -Value "COMPACT"
Save-Xml -Doc $ui -Path $uiPath

$lafPath = Join-Path $optionsDir "laf.xml"
$laf = New-ApplicationXml
$lafManager = Get-OrCreateComponent -Doc $laf -Name "LafManager"
$lafNode = $laf.CreateElement("laf")
$themeAttr = $laf.CreateAttribute("themeId")
$themeAttr.Value = "RiderDark"
[void]$lafNode.Attributes.Append($themeAttr)
[void]$lafManager.AppendChild($lafNode)
$preferred = $laf.CreateElement("preferred-dark-laf")
$preferredAttr = $laf.CreateAttribute("themeId")
$preferredAttr.Value = "RiderDark"
[void]$preferred.Attributes.Append($preferredAttr)
[void]$lafManager.AppendChild($preferred)
$previous = $laf.CreateElement("lafs-to-previous-schemes")
foreach ($mapping in @(
  @("Darcula", "_@user_Dark"),
  @("Islands Darcula", "_@user_Rider Dark"),
  @("RiderDark", "Rider Dark")
)) {
  $item = $laf.CreateElement("laf-to-scheme")
  $lafAttr = $laf.CreateAttribute("laf")
  $lafAttr.Value = $mapping[0]
  $schemeAttr = $laf.CreateAttribute("scheme")
  $schemeAttr.Value = $mapping[1]
  [void]$item.Attributes.Append($lafAttr)
  [void]$item.Attributes.Append($schemeAttr)
  [void]$previous.AppendChild($item)
}
[void]$lafManager.AppendChild($previous)
Save-Xml -Doc $laf -Path $lafPath

$schemePath = Join-Path $optionsDir "colors.scheme.xml"
$scheme = New-ApplicationXml
$schemeManager = Get-OrCreateComponent -Doc $scheme -Name "EditorColorsManagerImpl"
$global = $scheme.CreateElement("global_color_scheme")
$schemeName = $scheme.CreateAttribute("name")
$schemeName.Value = "_@user_Rider Dark"
[void]$global.Attributes.Append($schemeName)
[void]$schemeManager.AppendChild($global)
Save-Xml -Doc $scheme -Path $schemePath

$colorsDir = Join-Path $ConfigDir "colors"
New-Item -ItemType Directory -Force -Path $colorsDir | Out-Null
$classicSchemePath = Join-Path $colorsDir "_@user_Rider Dark.icls"
$classicScheme = @"
<scheme name="_@user_Rider Dark" version="142" parent_scheme="Rider Dark">
  <metaInfo>
    <property name="ide">Rider</property>
    <property name="ideVersion">2026.3.0.0</property>
    <property name="originalScheme">Rider Dark</property>
    <property name="partialSave">true</property>
    <property name="pluginId">Rider UI Theme Pack</property>
  </metaInfo>
  <colors>
    <option name="CARET_ROW_COLOR" value="202424" />
    <option name="EDITOR_GUTTER_BACKGROUND" value="282828" />
    <option name="GUTTER_BACKGROUND" value="282828" />
    <option name="LINE_NUMBERS_COLOR" value="#808080" />
    <option name="LINE_NUMBER_ON_CARET_ROW_COLOR" value="#808080" />
    <option name="NOTIFICATION_BACKGROUND" value="3D3020" />
    <option name="SELECTION_BACKGROUND" value="08335e" />
  </colors>
</scheme>
"@
Set-Content -LiteralPath $classicSchemePath -Value $classicScheme -Encoding UTF8

$generalPath = Join-Path $optionsDir "ide.general.xml"
$general = Read-XmlOrCreate -Path $generalPath
$registry = Get-OrCreateComponent -Doc $general -Name "Registry"
Set-RegistryEntry -Doc $general -Registry $registry -Key "switched.from.classic.to.islands" -Value "false"
Set-RegistryEntry -Doc $general -Registry $registry -Key "ide.experimental.ui" -Value "true"
Set-RegistryEntry -Doc $general -Registry $registry -Key "ide.ui.tree.indent" -Value "8"
Set-RegistryEntry -Doc $general -Registry $registry -Key "ide.project.icon.size" -Value "16"
Save-Xml -Doc $general -Path $generalPath

$otherPath = Join-Path $optionsDir "other.xml"
$other = Read-XmlOrCreate -Path $otherPath
$fontSettings = Get-OrCreateComponent -Doc $other -Name "NotRoamableUiSettings"
Set-Option -Doc $other -Component $fontSettings -Name "fontFace" -Value "Segoe UI"
Set-Option -Doc $other -Component $fontSettings -Name "fontSize" -Value "12.0"
Set-Option -Doc $other -Component $fontSettings -Name "presentationModeIdeScale" -Value "1.75"
Set-Option -Doc $other -Component $fontSettings -Name "overrideLafFonts" -Value "true"

$propertyService = $other.SelectSingleNode("/application/component[@name='PropertyService']")
if ($propertyService -and $propertyService.InnerText.Trim()) {
  $state = $propertyService.InnerText | ConvertFrom-Json
  if (-not $state.keyToString) {
    $state | Add-Member -MemberType NoteProperty -Name "keyToString" -Value ([pscustomobject]@{})
  }
  $state.keyToString.PSObject.Properties.Remove("ide.islands.ab3")
  $state.keyToString.PSObject.Properties.Remove("ide.islands.new.darcula")
  Set-JsonProperty -Object $state.keyToString -Name "selected.color.option.type" -Value "TAB_UNDERLINE"
  $json = $state | ConvertTo-Json -Depth 32
  $propertyService.RemoveAll()
  $nameAttr = $other.CreateAttribute("name")
  $nameAttr.Value = "PropertyService"
  [void]$propertyService.Attributes.Append($nameAttr)
  [void]$propertyService.AppendChild($other.CreateCDataSection($json))
}
Save-Xml -Doc $other -Path $otherPath

if (Test-Path -LiteralPath $VmOptionsPath) {
  $vmOptions = Get-Content -LiteralPath $VmOptionsPath
}
else {
  $vmOptions = @()
}
$vmOptions = $vmOptions | Where-Object { $_ -notmatch '^-Dide\.ui\.scale=' }
$vmOptions += "-Dide.ui.scale=0.85"
Set-Content -LiteralPath $VmOptionsPath -Value $vmOptions -Encoding UTF8

$extendedPlugin = Join-Path $ConfigDir "plugins\extendedToolWindowsUi"
if (-not (Test-Path -LiteralPath $extendedPlugin)) {
  Write-Warning "Extended Tool Windows UI is optional but recommended for the closest classic-style layout."
  Write-Warning "Install it from: https://plugins.jetbrains.com/plugin/34198-extended-tool-windows-ui"
}

Write-Host "Rider Classic-ish Dark UI profile applied."
Write-Host "Backup: $backupRoot"
Write-Host "Restart Rider 2026.3 EAP."
