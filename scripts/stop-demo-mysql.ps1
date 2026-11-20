[CmdletBinding()]
param(
    [int]$Port = 3307,
    [string]$MysqlHome = 'C:\Program Files\MySQL\MySQL Server 9.4'
)

$ErrorActionPreference = 'Stop'
$mysqlAdminExe = Join-Path $MysqlHome 'bin\mysqladmin.exe'
if (-not (Test-Path -LiteralPath $mysqlAdminExe -PathType Leaf)) {
    throw "Không tìm thấy: $mysqlAdminExe"
}

try {
    & $mysqlAdminExe --protocol=TCP --host=127.0.0.1 "--port=$Port" --user=root shutdown
    if ($LASTEXITCODE -ne 0) {
        throw "mysqladmin trả mã $LASTEXITCODE"
    }
    Write-Host "Đã dừng MySQL demo trên cổng $Port. Dữ liệu vẫn được giữ trong .mysql-data."
} catch {
    throw "Không dừng được MySQL demo: $($_.Exception.Message)"
}

