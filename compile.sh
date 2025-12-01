#!/bin/bash

echo "Compilando Sistema de Gerenciamento de Pacientes..."
echo

cd "$(dirname "$0")"

mkdir -p target/classes

echo "[1/3] Compilando classes Java..."
javac -d target/classes -encoding UTF-8 \
    src/main/java/org/model/Paciente.java \
    src/main/java/org/dao/PacienteDAO.java \
    src/main/java/org/ui/PacienteUISwing.java \
    src/main/java/org/Main.java

if [ $? -ne 0 ]; then
    echo "Erro na compilação!"
    exit 1
fi

echo "[2/3] Dependências prontas"
echo "[3/3] Compilação concluída com sucesso!"
echo
echo "Para executar a interface gráfica:"
echo "  java -cp target/classes org.ui.PacienteUISwing"
echo
echo "Para executar o exemplo console:"
echo "  java -cp target/classes org.Main"
echo
