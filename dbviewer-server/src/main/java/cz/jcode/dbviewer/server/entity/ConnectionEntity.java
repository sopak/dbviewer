package cz.jcode.dbviewer.server.entity;

import cz.jcode.dbviewer.server.converter.PasswordConverter;
import lombok.*;

import javax.persistence.*;
import java.util.UUID;

@SuppressWarnings("JpaDataSourceORMInspection")
@Entity
@Table(name = "connection")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ConnectionEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    private String name;
    private String jdbcDriver;
    private String jdbcUrl;
    private String username;
    @Convert(converter = PasswordConverter.class)
    private String password;

    @Version
    private Long version;
}