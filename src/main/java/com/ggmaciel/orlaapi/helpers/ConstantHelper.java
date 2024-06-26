package com.ggmaciel.orlaapi.helpers;

public class ConstantHelper {
    public static final String INVALID_NAME_SIZE = "O campo 'nome' deve ter entre 1 e 255 caracteres.";
    public static final String PROJECT_ALREADY_EXISTS = "Projeto já existe, tente novamente com outro nome.";
    public static final String EMPLOYEE_WITH_CPF_ALREADY_EXISTS = "Funcionário já cadastrado, tente novamente com outro CPF.";
    public static final String EMPLOYEE_WITH_EMAIL_ALREADY_EXISTS = "Funcionário já cadastrado, tente novamente com outro email.";
    public static final String INVALID_CPF_SIZE = "O campo 'cpf' deve ter 11 caracteres.";
    public static final String INVALID_EMAIL = "O campo 'email' deve ser um endereço de e-mail válido. Por exemplo: 'exemplo@dominio.com'.";
    public static final String INVALID_SALARY = "O campo 'salário' deve ser um número positivo ou zero.";
    public static final String ENTITY_NOT_FOUND = "Entidade não encontrada. Verifique o ID usado.";
    public static final String ID_MUST_BE_A_VALID_NUMBER = "OS campos de 'id' devem ser um número positivo válido.";

    private ConstantHelper() {
        throw new IllegalStateException("Utility class");
    }
}
