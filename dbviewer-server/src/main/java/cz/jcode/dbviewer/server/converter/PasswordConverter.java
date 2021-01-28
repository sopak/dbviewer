package cz.jcode.dbviewer.server.converter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.encrypt.BytesEncryptor;
import org.springframework.stereotype.Component;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component
@Converter
@Slf4j
public class PasswordConverter implements AttributeConverter<String, String> {

    private final BytesEncryptor encryptor;

    public PasswordConverter(BytesEncryptor encryptor) {
        this.encryptor = encryptor;
    }

    @Override
    public String convertToDatabaseColumn(String plainPassword) {
        if (plainPassword == null) {
            throw new IllegalArgumentException("PlainPassword can't be null");
        }
        return Base64.getEncoder()
                .encodeToString(
                        encryptor.encrypt(
                                plainPassword.getBytes(StandardCharsets.UTF_8)
                        )
                );
    }

    @Override
    public String convertToEntityAttribute(String encryptedPassword) {
        if (encryptedPassword == null) {
            throw new IllegalArgumentException("EncryptedPassword can't be null");
        }
        try {
            return new String(
                    encryptor.decrypt(
                            Base64.getDecoder().decode(encryptedPassword.getBytes(StandardCharsets.UTF_8))
                    ), StandardCharsets.UTF_8
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encryptedPassword;

    }
}
