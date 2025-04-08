package com.example.interfacefun.controller;

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

    @FXML
    private TextField matriculaField;
    @FXML
    private TextField nomeField;
    @FXML
    private TextField cpfField;
    @FXML
    private DatePicker dataNascimentoPicker;
    @FXML
    private TextField cargoField;
    @FXML
    private TextField salarioField;
    @FXML
    private DatePicker dataContratacaoPicker;

    @FXML
    private TextField logradouroField;
    @FXML
    private TextField numeroField;
    @FXML
    private TextField complementoField;
    @FXML
    private TextField bairroField;
    @FXML
    private TextField cidadeField;
    @FXML
    private TextField estadoField;
    @FXML
    private TextField cepField;

    @FXML
    private TableView<Funcionario> tabelaFuncionarios;
    @FXML
    private TableColumn<Funcionario, String> colMatricula;
    @FXML
    private TableColumn<Funcionario, String> colNome;
    @FXML
    private TableColumn<Funcionario, String> colCargo;
    @FXML
    private TableColumn<Funcionario, BigDecimal> colSalario;

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
            LocalDate dataNascimento = dataNascimentoPicker.getValue();
            if (dataNascimento == null) {
                mostrarAlerta("Informe a data de nascimento.");
                return;
            }

            int idade = Period.between(dataNascimento, LocalDate.now()).getYears();
            if (idade < 18) {
                mostrarAlerta("O funcionário deve ter no mínimo 18 anos.");
                return;
            }

            BigDecimal salario;
            try {
                salario = new BigDecimal(salarioField.getText());
                if (salario.compareTo(BigDecimal.ZERO) <= 0) {
                    mostrarAlerta("O salário deve ser um valor positivo.");
                    return;
                }
            } catch (NumberFormatException e) {
                mostrarAlerta("Informe um valor numérico válido para o salário.");
                return;
            }

            String cep = cepField.getText();
            if (!cep.matches("\\d{8}")) {
                mostrarAlerta("O CEP deve conter exatamente 8 dígitos numéricos.");
                return;
            }

            Funcionario funcionario = new Funcionario();
            funcionario.setMatricula(matriculaField.getText());
            funcionario.setNome(nomeField.getText());
            funcionario.setCpf(cpfField.getText());
            funcionario.setDataNascimento(dataNascimento);
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
            endereco.setCep(cep);

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
            mostrarAlerta("Informe a matrícula do funcionário a ser excluído.");
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
            mostrarAlerta("Informe a matrícula do funcionário para consulta.");
            return;
        }

        Funcionario funcionario = repository.consultar(matricula);

        if (funcionario != null) {
            nomeField.setText(funcionario.getNome());
            cpfField.setText(funcionario.getCpf());
            dataNascimentoPicker.setValue(funcionario.getDataNascimento());
            cargoField.setText(funcionario.getCargo());
            salarioField.setText(funcionario.getSalario().toString());
            dataContratacaoPicker.setValue(funcionario.getDataContratacao());

            Endereco endereco = funcionario.getEndereco();
            if (endereco != null) {
                logradouroField.setText(endereco.getLogradouro());
                numeroField.setText(endereco.getNumero());
                complementoField.setText(endereco.getComplemento());
                bairroField.setText(endereco.getBairro());
                cidadeField.setText(endereco.getCidade());
                estadoField.setText(endereco.getEstado());
                cepField.setText(endereco.getCep());
            }
        } else {
            mostrarAlerta("Funcionário não encontrado.");
        }
    }

    @FXML
    public void onListarTodos() {
        atualizarTabela();
    }

    private void atualizarTabela() {
        List<Funcionario> funcionarios = repository.listarTodos();
        tabelaFuncionarios.getItems().setAll(funcionarios);
    }

    private void limparCampos() {
        matriculaField.clear();
        nomeField.clear();
        cpfField.clear();
        dataNascimentoPicker.setValue(null);
        cargoField.clear();
        salarioField.clear();
        dataContratacaoPicker.setValue(null);

        logradouroField.clear();
        numeroField.clear();
        complementoField.clear();
        bairroField.clear();
        cidadeField.clear();
        estadoField.clear();
        cepField.clear();
    }

    private void mostrarAlerta(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Atenção");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}
