package com.example.interfacefun.repository;

import com.example.interfacefun.model.Endereco;
import com.example.interfacefun.model.Funcionario;

import java.io.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FuncionarioRepository {

    private static final String CAMINHO_ARQUIVO = "funcionarios.csv";

    public void salvar(Funcionario funcionario) {
        List<Funcionario> funcionarios = listarTodos();
        funcionarios.removeIf(f -> f.getMatricula().equals(funcionario.getMatricula()));
        funcionarios.add(funcionario);
        salvarTodos(funcionarios);
    }

    public void excluir(String matricula) {
        List<Funcionario> funcionarios = listarTodos();
        funcionarios.removeIf(f -> f.getMatricula().equals(matricula));
        salvarTodos(funcionarios);
    }

    public Funcionario consultar(String matricula) {
        return listarTodos().stream()
                .filter(f -> f.getMatricula().equals(matricula))
                .findFirst()
                .orElse(null);
    }

    public List<Funcionario> listarTodos() {
        List<Funcionario> lista = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(CAMINHO_ARQUIVO))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                String[] partes = linha.split(";");
                if (partes.length == 14) {
                    Endereco endereco = new Endereco(
                            partes[7], partes[8], partes[9], partes[10],
                            partes[11], partes[12], partes[13]
                    );

                    Funcionario funcionario = new Funcionario(
                            partes[0],                       // matrícula
                            partes[1],                       // nome
                            partes[2],                       // cpf
                            LocalDate.parse(partes[3]),      // dataNascimento
                            partes[4],                       // cargo
                            new BigDecimal(partes[5]),       // salário
                            LocalDate.parse(partes[6]),      // dataContratacao
                            endereco                         // endereço
                    );
                    lista.add(funcionario);
                }
            }
        } catch (IOException e) {
            System.err.println("Erro ao ler o arquivo: " + e.getMessage());
        }
        return lista;
    }

    private void salvarTodos(List<Funcionario> funcionarios) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CAMINHO_ARQUIVO))) {
            for (Funcionario f : funcionarios) {
                writer.write(String.join(";",
                        f.getMatricula(),
                        f.getNome(),
                        f.getCpf(),
                        f.getDataNascimento().toString(),
                        f.getCargo(),
                        f.getSalario().toString(),
                        f.getDataContratacao().toString(),
                        f.getEndereco().getLogradouro(),
                        f.getEndereco().getNumero(),
                        f.getEndereco().getComplemento(),
                        f.getEndereco().getBairro(),
                        f.getEndereco().getCidade(),
                        f.getEndereco().getEstado(),
                        f.getEndereco().getCep()
                ));
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Erro ao salvar o arquivo: " + e.getMessage());
        }
    }
}
