@echo off
cd /d "%~dp0"

if not exist "target\classes" (
    echo Compilando primeiro...
    call compile.bat
    if %errorlevel% neq 0 exit /b 1
)

echo.
echo ======================================
echo Interface de Gerenciamento de Pacientes
echo ======================================
echo.

java -cp target/classes org.ui.PacienteUISwing

pause
