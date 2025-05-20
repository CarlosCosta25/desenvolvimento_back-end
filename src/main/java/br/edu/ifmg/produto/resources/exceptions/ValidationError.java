package br.edu.ifmg.produto.resources.exceptions;
import java.util.List;
import java.util.ArrayList;

public class ValidationError extends StandartError{
    private List<FieldMessage> erros = new ArrayList<>();

    public ValidationError(){}
    public List<FieldMessage> getFieldMenssage() {
        return erros;
    }
    public void setFieldMessage(List<FieldMessage>fieldMenssages) {
        this.erros = fieldMenssages;
    }

    public void addFieldMessage(String fieldName, String message) {
        this.erros.add(new FieldMessage(fieldName, message));
    }

}
