package cz.jcode.dbviewer.server.helper;

public interface FunctionThatThrows<T, R> {
    R apply(T t) throws Exception;
}