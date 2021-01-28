package cz.jcode.dbviewer.server.helper;

public interface BiConsumerThatThrows<I1, I2> {
    void accept(I1 input1, I2 input2) throws Exception;
}