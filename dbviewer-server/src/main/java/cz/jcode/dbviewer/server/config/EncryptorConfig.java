package cz.jcode.dbviewer.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.encrypt.BytesEncryptor;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.stereotype.Component;

@Component
public class EncryptorConfig {

    private final DbViewerProperties dbViewerProperties;

    public EncryptorConfig(DbViewerProperties dbViewerProperties) {
        this.dbViewerProperties = dbViewerProperties;
    }

    @Bean
    public BytesEncryptor getEncryptor(){
        return Encryptors.standard(dbViewerProperties.getEncryptionKey(), dbViewerProperties.getEncryptionHexSalt16bytes());
    }
}
