package org.ecommerce.project.exception;


public class APIException extends RuntimeException {
    String fieldName;

    public APIException( String fieldName) {
        super(String.format("%s already exist",fieldName));
        this.fieldName = fieldName;

    }
}
