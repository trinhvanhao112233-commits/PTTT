[CmdletBinding()]
param(
    [int]$Port = 3307,
    [string]$MysqlHome = 'C:\Program Files\MySQL\MySQL Server 9.4',
    [string]$DataDir,
    [switch]$ResetDatabase
)

$ErrorActionPreference = 'Stop'
$workspaceRoot = [System.IO.Path]::GetFullPath((Split-Path -Parent $PSScriptRoot))
$userProfile = [System.Environment]::GetFolderPath([System.Environment+SpecialFolder]::UserProfile)
$tempRoot = [System.IO.Path]::GetFullPath((Join-Path $userProfile 'AppData\Local\Temp'))
if ([string]::IsNullOrWhiteSpace($DataDir)) {
    # mysqld 9.4 on Windows cannot initialize a datadir whose path contains
    # Vietnamese characters, so the demo uses an ASCII name in the temp root.
    $DataDir = Join-Path $tempRoot 'RestMan-De20-MySQL\data'
}
$resolvedDataDir = [System.IO.Path]::GetFullPath($DataDir)

$isWorkspacePath = $resolvedDataDir.StartsWith($workspaceRoot, [System.StringComparison]::OrdinalIgnoreCase)
$isTempPath = $resolvedDataDir.StartsWith($tempRoot, [System.StringComparison]::OrdinalIgnoreCase)
if (-not ($isWorkspacePath -or $isTempPath)) {
    throw "DataDir phải nằm trong workspace hoặc thư mục tạm: $tempRoot"
}

$mysqldExe = Join-Path $MysqlHome 'bin\mysqld.exe'
$mysqlExe = Join-Path $MysqlHome 'bin\mysql.exe'
$mysqlAdminExe = Join-Path $MysqlHome 'bin\mysqladmin.exe'
foreach ($executable in @($mysqldExe, $mysqlExe, $mysqlAdminExe)) {
    if (-not (Test-Path -LiteralPath $executable -PathType Leaf)) {
        throw "Không tìm thấy: $executable"
    }
}

$runDir = Join-Path (Split-Path -Parent $resolvedDataDir) 'run'
New-Item -ItemType Directory -Path $runDir -Force | Out-Null
New-Item -ItemType Directory -Path $resolvedDataDir -Force | Out-Null
$pidFile = Join-Path $runDir 'mysqld.pid'
$errorLog = Join-Path $runDir 'mysqld-error.log'
$isNewDataDirectory = -not (Test-Path -LiteralPath (Join-Path $resolvedDataDir 'auto.cnf'))

if ($isNewDataDirectory) {
    Write-Host 'Đang khởi tạo MySQL data directory cho bản demo...'
    & $mysqldExe --initialize-insecure "--basedir=$MysqlHome" "--datadir=$resolvedDataDir"
    if ($LASTEXITCODE -ne 0) {
        throw "mysqld --initialize-insecure thất bại với mã $LASTEXITCODE"
    }
}

$alreadyRunning = $false
try {
    & $mysqlAdminExe --protocol=TCP --host=127.0.0.1 "--port=$Port" --user=root ping 2>$null | Out-Null
    $alreadyRunning = $LASTEXITCODE -eq 0
} catch {
    $alreadyRunning = $false
}

if (-not $alreadyRunning) {
    $arguments = @(
        "--basedir=`"$MysqlHome`"",
        "--datadir=`"$resolvedDataDir`"",
        "--port=$Port",
        '--bind-address=127.0.0.1',
        '--mysqlx=0',
        "--pid-file=`"$pidFile`"",
        "--log-error=`"$errorLog`""
    )
    $process = Start-Process -FilePath $mysqldExe -ArgumentList $arguments -WorkingDirectory (Split-Path -Parent $resolvedDataDir) -WindowStyle Hidden -PassThru
    Write-Host "Đã khởi động mysqld (PID $($process.Id)); đang chờ sẵn sàng..."

    $ready = $false
    for ($attempt = 0; $attempt -lt 60; $attempt++) {
        Start-Sleep -Milliseconds 500
        try {
            & $mysqlAdminExe --protocol=TCP --host=127.0.0.1 "--port=$Port" --user=root ping 2>$null | Out-Null
            if ($LASTEXITCODE -eq 0) {
                $ready = $true
                break
            }
        } catch {
            # MySQL vẫn đang khởi động.
        }
    }
    if (-not $ready) {
        throw "MySQL không sẵn sàng sau 30 giây. Xem log: $errorLog"
    }
}

$databaseExists = $false
try {
    $databaseExists = (& $mysqlExe --protocol=TCP --host=127.0.0.1 "--port=$Port" --user=root --batch --skip-column-names --execute="SELECT SCHEMA_NAME FROM INFORMATION_SCHEMA.SCHEMATA WHERE SCHEMA_NAME='restman_db';") -eq 'restman_db'
} catch {
    $databaseExists = $false
}

if ($isNewDataDirectory -or $ResetDatabase -or -not $databaseExists) {
    $sqlFile = Join-Path $workspaceRoot 'database\restman_db.sql'
    Write-Host "Đang nạp dữ liệu mẫu từ $sqlFile"
    Get-Content -LiteralPath $sqlFile -Raw -Encoding UTF8 |
        & $mysqlExe --protocol=TCP --host=127.0.0.1 "--port=$Port" --user=root --default-character-set=utf8mb4
    if ($LASTEXITCODE -ne 0) {
        throw "Nạp restman_db.sql thất bại với mã $LASTEXITCODE"
    }
}

Write-Host "MySQL demo sẵn sàng tại 127.0.0.1:$Port"
Write-Host "Database: restman_db | User: restman | Password: restman123"

