package cz.jcode.dbviewer.server.converter;

import cz.jcode.dbviewer.server.config.DbViewerProperties;
import cz.jcode.dbviewer.server.config.EncryptorConfig;
import net.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Random;
import java.util.stream.Stream;

@SuppressWarnings("SpellCheckingInspection")
@SpringBootTest(
        classes = {PasswordConverter.class, EncryptorConfig.class, DbViewerProperties.class},
        properties = {
                "cz.jcode.dbviewer.server.encryption-key=someKefdsSADASD35fdsfds%",
                "cz.jcode.dbviewer.server.encryption-hex-salt-16bytes=abcdadcfadcd1234"
        }
)
class PasswordConverterTest {

    @Autowired
    private PasswordConverter passwordConverter;

    @ParameterizedTest
    @ArgumentsSource(RandomStrings.class)
    void encryptAndDecrypt(String original) {
        String encrypted = passwordConverter.convertToDatabaseColumn(original);
        String plain = passwordConverter.convertToEntityAttribute(encrypted);
        Assertions.assertEquals(original, plain);
    }

    @Test
    void encryptAndDecryptEmpty() {
        String original = "";
        String encrypted = passwordConverter.convertToDatabaseColumn(original);
        String plain = passwordConverter.convertToEntityAttribute(encrypted);
        Assertions.assertEquals(original, plain);
    }

    @Test
    void encryptNull() {
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> passwordConverter.convertToDatabaseColumn(null)
        );
    }

    @Test
    void decryptNull() {
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> passwordConverter.convertToEntityAttribute(null)
        );
    }

    public static class RandomStrings implements ArgumentsProvider {

        final Random random = new Random();

        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
            return Stream.generate(
                    () -> Arguments.of(
                            RandomString.make(random.nextInt(63) + 1)
                    )).limit(10);
        }
    }
}