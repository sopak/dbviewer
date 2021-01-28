package cz.jcode.dbviewer.server.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@EnableConfigurationProperties(DbViewerProperties.class)
@ConfigurationProperties(prefix="cz.jcode.dbviewer.server")
public class DbViewerProperties {

    String encryptionHexSalt16bytes;

    String encryptionKey;

    ControllerLoggingAspectProperties ControllerLoggingAspect = new ControllerLoggingAspectProperties();

    @Getter
    @Setter
    private static class ControllerLoggingAspectProperties {
        private boolean enabled;
    }
}
