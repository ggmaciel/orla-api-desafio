package com.ggmaciel.orlaapi.helpers;

public class ConstantHelper {
    public static final String INVALID_NAME_SIZE = "O campo 'nome' deve ter entre 1 e 255 caracteres.";
    public static final String PROJECT_ALREADY_EXISTS = "Projeto já existe, tente novamente com outro nome.";

    private ConstantHelper() {
        throw new IllegalStateException("Utility class");
    }
}
