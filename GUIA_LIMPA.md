# Sistema de Gerenciamento de Pacientes - Versão Limpa

## 📋 O que foi removido?

Da pasta original (`Clinica`), foram removidas as seguintes classes desnecessárias:

- `ExemplosAvancados.java` - Exemplos do projeto do professor
- `TestPacienteDAO.java` - Testes do projeto original
- `TestPacienteSimplificado.java` - Testes alternativos
- `PacienteUI.java` - Interface JavaFX (dependências externas)
- `ProfessorDAO.java` - Não utilizado
- `Professor.java` - Não utilizado
- `HibernateUtil.java` - Config do Hibernate (usando CSV)
- Documentação técnica extensa (ARCHITECTURE.md, etc)

## ✅ O que permaneceu?

Apenas o **essencial e funcional**:

### Classes Java
- **`Paciente.java`** - Entidade JPA simples com 5 atributos
- **`PacienteDAO.java`** - Acesso a dados com persistência em CSV
- **`PacienteUISwing.java`** - Interface gráfica nativa (Swing)
- **`Main.java`** - Exemplo de operações CRUD em console

### Configuração
- **`pom.xml`** - Apenas dependências necessárias (Lombok + Jakarta Persistence API)

### Documentação
- **`README.md`** - Instruções básicas

## 🚀 Como usar?

### Windows
```bash
# Compilar
compile.bat

# Executar Interface Gráfica
run_ui.bat

# Executar Exemplo Console
run_console.bat
```

### Linux/Mac
```bash
# Compilar
bash compile.sh

# Executar Interface Gráfica
java -cp target/classes org.ui.PacienteUISwing

# Executar Exemplo Console
java -cp target/classes org.Main
```

## 📁 Estrutura

```
Clinica_Limpa/
├── src/main/java/org/
│   ├── model/
│   │   └── Paciente.java
│   ├── dao/
│   │   └── PacienteDAO.java
│   ├── ui/
│   │   └── PacienteUISwing.java
│   └── Main.java
├── pom.xml
├── compile.bat
├── compile.sh
├── run_ui.bat
├── run_console.bat
└── README.md
```

## 💾 Dados

Os dados são persistidos em `pacientes.csv`:
```
id,nome,cpf,dataNascimento,telefone
1,Maria Silva,123.456.789-00,1990-05-15,(62) 99876-5432
```

## 🎯 Requisitos

- Java 24+
- Nenhuma dependência em runtime (Lombok é apenas para compilação)

Projeto pronto para uso e customização!
