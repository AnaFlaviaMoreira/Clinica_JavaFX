# Sistema de Gerenciamento de Pacientes - Versão Limpa

Implementação limpa e essencial do Sistema de Gerenciamento de Pacientes para clínicas, com apenas as classes necessárias.

## Estrutura do Projeto

```
src/main/
├── java/org/
│   ├── model/
│   │   └── Paciente.java          # Entidade JPA
│   ├── dao/
│   │   └── PacienteDAO.java       # Acesso a dados (CSV)
│   ├── ui/
│   │   └── PacienteUISwing.java   # Interface gráfica Swing
│   └── Main.java                   # Exemplo console
└── resources/
```

## Funcionalidades

- **CRUD Completo**: Criar, ler, atualizar e deletar pacientes
- **Interface Gráfica**: Swing com masks para entrada de dados
- **Persistência**: Arquivo CSV automático
- **Formato Brasileiro**: Data DD/MM/YYYY e máscara para CPF/Telefone

## Como Compilar

```bash
mvn clean compile
```

## Como Executar

### Interface Gráfica (Recomendado)
```bash
mvn exec:java -Dexec.mainClass="org.ui.PacienteUISwing"
```

### Exemplo Console
```bash
mvn exec:java -Dexec.mainClass="org.Main"
```

## Dependências Mínimas

- Java 24+
- Lombok (provided scope - apenas compilação)
- Jakarta Persistence API

## Dados Persistidos

Os dados são salvos em `pacientes.csv` com formato:
```
id,nome,cpf,dataNascimento,telefone
1,Maria Silva,123.456.789-00,1990-05-15,62998765432
```

## Autor

Desenvolvido como sistema de gerenciamento de pacientes para clínica.
