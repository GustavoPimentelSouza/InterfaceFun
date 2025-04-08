package com.example.interfacefun.util;

public class Validador {
    public static boolean validarMatricula(String matricula) {
        return matricula.matches("\\d{6}");
    }

    public static boolean validarNome(String nome) {
        return nome != null && nome.trim().length() >= 3;
    }

    public static boolean validarCpf(String cpf) {
        return cpf.matches("\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}");
    }

    public static boolean validarCep(String cep) {
        return cep.matches("\\d{5}-\\d{3}");
    }
}
