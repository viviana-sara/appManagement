package validator;

public interface Validator<T> {

    public int validate(T t);
    public  int validateString(String command);
}

