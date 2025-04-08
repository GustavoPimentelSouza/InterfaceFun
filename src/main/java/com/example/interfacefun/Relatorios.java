package com.example.interfacefun;

import com.example.interfacefun.model.Funcionario;
import com.example.interfacefun.repository.FuncionarioRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class Relatorios {

    private static final FuncionarioRepository repository = new FuncionarioRepository();

    public static List<Funcionario> filtrarPorCargo(String cargo) {
        return repository.listarTodos().stream()
                .filter(f -> f.getCargo().equalsIgnoreCase(cargo))
                .collect(Collectors.toList());
    }

    public static List<Funcionario> filtrarPorFaixaSalarial(double salarioMin, double salarioMax) {
        return repository.listarTodos().stream()
                .filter(f -> {
                    BigDecimal salario = f.getSalario();
                    return salario.compareTo(BigDecimal.valueOf(salarioMin)) >= 0 &&
                            salario.compareTo(BigDecimal.valueOf(salarioMax)) <= 0;
                })
                .collect(Collectors.toList());
    }

    // Adicione aqui outros métodos de relatório conforme necessário
}
