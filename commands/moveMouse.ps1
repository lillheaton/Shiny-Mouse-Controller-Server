param(
    [Int32]$Left=50,
    [Int32]$Top=50
)

Add-Type -AssemblyName System.Windows.Forms

# Get parameters

# Get current Position
$CurrentPos = [System.Windows.Forms.Cursor]::Position

echo $Left
echo $Top

# Move mouse
[System.Windows.Forms.Cursor]::Position = New-Object System.Drawing.Point(($CurrentPos.X + $Left) , ($CurrentPos.Y + $Top))