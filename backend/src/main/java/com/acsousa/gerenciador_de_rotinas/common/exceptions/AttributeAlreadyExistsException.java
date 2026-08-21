package com.acsousa.gerenciador_de_rotinas.common.exceptions;

import java.io.Serial;

public class AttributeAlreadyExistsException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public AttributeAlreadyExistsException(String msg) {
        super(msg);
    }
}
