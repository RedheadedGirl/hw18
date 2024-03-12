package ru.sbrf.config.exceptions;

public class SqlMappingException extends RuntimeException {

    public SqlMappingException(Exception exception) {
        super(exception);
    }
}
