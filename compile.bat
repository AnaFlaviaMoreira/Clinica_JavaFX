@echo off
echo Compilando Sistema de Gerenciamento de Pacientes...
echo.

cd /d "%~dp0"

if not exist "target" mkdir target
if not exist "target\classes" mkdir target\classes

echo [1/3] Compilando classes Java...
javac -d target/classes -encoding UTF-8 ^
    src/main/java/org/model/Paciente.java ^
    src/main/java/org/dao/PacienteDAO.java ^
    src/main/java/org/ui/PacienteUISwing.java ^
    src/main/java/org/Main.java

if %errorlevel% neq 0 (
    echo Erro na compilacao!
    pause
    exit /b 1
)

echo [2/3] Download de dependencias (Lombok)...
if not exist "%USERPROFILE%\.m2\repository\org\projectlombok" (
    echo Baixando Lombok...
    powershell -Command "iwr -Uri https://repo1.maven.org/maven2/org/projectlombok/lombok/1.18.42/lombok-1.18.42.jar -OutFile target\lombok.jar"
)

echo [3/3] Compilacao concluida com sucesso!
echo.
echo Para executar a interface grafica:
echo   java -cp target/classes org.ui.PacienteUISwing
echo.
echo Para executar o exemplo console:
echo   java -cp target/classes org.Main
echo.
pause
