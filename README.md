# InterfaceFun - Sistema de Cadastro de Funcionários

## 📄 Sobre o Projeto

Este é um sistema de gerenciamento de funcionários desenvolvido em Java com JavaFX, utilizando arquitetura MVC, persistência em arquivos CSV e funcionalidades de relatórios com a API Stream.

## 💡 Funcionalidades Implementadas

- Cadastro de funcionários (com dados pessoais e endereço)
- Validações:
    - Funcionário deve ter 18 anos ou mais
    - Salário deve ser positivo
    - CEP com 8 dígitos numéricos
- Edição e exclusão de funcionários
- Consulta por matrícula
- Listagem geral de todos os funcionários
- Relatórios via API Stream:
    - Filtrar por cargo
    - Filtrar por faixa salarial

## 📁 Estrutura do Projeto

```
InterfaceFun/
├── src/main/java
│   └── com/example/interfacefun/
│       ├── controller/FuncionarioController.java
│       ├── model/Funcionario.java, Endereco.java
│       ├── repository/FuncionarioRepository.java
│       └── Relatorios.java
├── src/main/resources
│   └── hello-view.fxml
├── pom.xml
└── README.md
```

## 🚀 Como Executar o Projeto

### Requisitos

- JDK 17+
- Maven instalado ([https://maven.apache.org/](https://maven.apache.org/))

### Passos

1. Clone o repositório:

```bash
git clone https://github.com/seuusuario/InterfaceFun.git
cd InterfaceFun
```

2. Compile o projeto com Maven:

```bash
mvn clean install
```

3. Execute a aplicação:

```bash
mvn javafx:run
```

## 🤝 Decisões de Design

- Uso de JavaFX para criar uma interface responsiva e modular com FXML
- Controle central em `FuncionarioController`, conectado via `fx:controller`
- `FuncionarioRepository` lida com persistência em CSV
- `Relatorios.java` aplica a API Stream para gerar consultas de dados

##
