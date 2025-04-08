package com.example.interfacefun.controller;

import com.example.interfacefun.Relatorios;
import com.example.interfacefun.model.Endereco;
import com.example.interfacefun.model.Funcionario;
import com.example.interfacefun.repository.FuncionarioRepository;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;

public class FuncionarioController {

    @FXML private TextField matriculaField, nomeField, cpfField, cargoField, salarioField;
    @FXML private DatePicker dataNascimentoPicker, dataContratacaoPicker;

    @FXML private TextField logradouroField, numeroField, complementoField, bairroField, cidadeField, estadoField, cepField;

    @FXML private TextField salarioMinField, salarioMaxField;

    @FXML private TableView<Funcionario> tabelaFuncionarios;
    @FXML private TableColumn<Funcionario, String> colMatricula, colNome, colCargo;
    @FXML private TableColumn<Funcionario, BigDecimal> colSalario;

    private final FuncionarioRepository repository = new FuncionarioRepository();

    @FXML
    public void initialize() {
        colMatricula.setCellValueFactory(new PropertyValueFactory<>("matricula"));
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colCargo.setCellValueFactory(new PropertyValueFactory<>("cargo"));
        colSalario.setCellValueFactory(new PropertyValueFactory<>("salario"));
        atualizarTabela();
    }

    @FXML
    public void onCadastrar() {
        try {
            LocalDate nascimento = dataNascimentoPicker.getValue();
            if (nascimento == null || Period.between(nascimento, LocalDate.now()).getYears() < 18) {
                mostrarAlerta("Funcionário deve ter no mínimo 18 anos.");
                return;
            }

            BigDecimal salario;
            try {
                salario = new BigDecimal(salarioField.getText());
                if (salario.compareTo(BigDecimal.ZERO) <= 0) throw new NumberFormatException();
            } catch (NumberFormatException e) {
                mostrarAlerta("Salário inválido. Digite um valor positivo.");
                return;
            }

            if (!cepField.getText().matches("\\d{8}")) {
                mostrarAlerta("CEP deve conter exatamente 8 dígitos numéricos.");
                return;
            }

            Funcionario funcionario = new Funcionario();
            funcionario.setMatricula(matriculaField.getText());
            funcionario.setNome(nomeField.getText());
            funcionario.setCpf(cpfField.getText());
            funcionario.setDataNascimento(nascimento);
            funcionario.setCargo(cargoField.getText());
            funcionario.setSalario(salario);
            funcionario.setDataContratacao(dataContratacaoPicker.getValue());

            Endereco endereco = new Endereco();
            endereco.setLogradouro(logradouroField.getText());
            endereco.setNumero(numeroField.getText());
            endereco.setComplemento(complementoField.getText());
            endereco.setBairro(bairroField.getText());
            endereco.setCidade(cidadeField.getText());
            endereco.setEstado(estadoField.getText());
            endereco.setCep(cepField.getText());

            funcionario.setEndereco(endereco);

            repository.salvar(funcionario);
            limparCampos();
            atualizarTabela();
        } catch (Exception e) {
            mostrarAlerta("Erro ao cadastrar funcionário: " + e.getMessage());
        }
    }

    @FXML
    public void onExcluir() {
        String matricula = matriculaField.getText();
        if (matricula.isEmpty()) {
            mostrarAlerta("Informe a matrícula para exclusão.");
            return;
        }
        repository.excluir(matricula);
        limparCampos();
        atualizarTabela();
    }

    @FXML
    public void onConsultar() {
        String matricula = matriculaField.getText();
        if (matricula.isEmpty()) {
            mostrarAlerta("Informe a matrícula para consulta.");
            return;
        }

        Funcionario funcionario = repository.consultar(matricula);
        if (funcionario == null) {
            mostrarAlerta("Funcionário não encontrado.");
            return;
        }

        nomeField.setText(funcionario.getNome());
        cpfField.setText(funcionario.getCpf());
        dataNascimentoPicker.setValue(funcionario.getDataNascimento());
        cargoField.setText(funcionario.getCargo());
        salarioField.setText(funcionario.getSalario().toString());
        dataContratacaoPicker.setValue(funcionario.getDataContratacao());

        Endereco e = funcionario.getEndereco();
        if (e != null) {
            logradouroField.setText(e.getLogradouro());
            numeroField.setText(e.getNumero());
            complementoField.setText(e.getComplemento());
            bairroField.setText(e.getBairro());
            cidadeField.setText(e.getCidade());
            estadoField.setText(e.getEstado());
            cepField.setText(e.getCep());
        }
    }

    @FXML
    public void onListarTodos() {
        atualizarTabela();
    }

    @FXML
    public void onFiltrarPorCargo() {
        String cargo = cargoField.getText();
        if (cargo == null || cargo.isBlank()) {
            mostrarAlerta("Informe o cargo para filtrar.");
            return;
        }

        List<Funcionario> filtrados = Relatorios.filtrarPorCargo(cargo);
        tabelaFuncionarios.getItems().setAll(filtrados);
    }

    @FXML
    public void onFiltrarPorSalario() {
        try {
            double min = Double.parseDouble(salarioMinField.getText());
            double max = Double.parseDouble(salarioMaxField.getText());

            if (min < 0 || max < 0 || min > max) {
                mostrarAlerta("Verifique os valores mínimo e máximo corretamente.");
                return;
            }

            List<Funcionario> filtrados = Relatorios.filtrarPorFaixaSalarial(min, max);
            tabelaFuncionarios.getItems().setAll(filtrados);
        } catch (NumberFormatException e) {
            mostrarAlerta("Digite valores válidos nos campos de salário mínimo e máximo.");
        }
    }

    private void atualizarTabela() {
        tabelaFuncionarios.getItems().setAll(repository.listarTodos());
    }

    private void limparCampos() {
        matriculaField.clear(); nomeField.clear(); cpfField.clear(); cargoField.clear(); salarioField.clear();
        dataNascimentoPicker.setValue(null); dataContratacaoPicker.setValue(null);
        logradouroField.clear(); numeroField.clear(); complementoField.clear();
        bairroField.clear(); cidadeField.clear(); estadoField.clear(); cepField.clear();
        if (salarioMinField != null) salarioMinField.clear();
        if (salarioMaxField != null) salarioMaxField.clear();
    }

    private void mostrarAlerta(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Atenção");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}
