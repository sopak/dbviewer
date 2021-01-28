package cz.jcode.dbviewer.server.converter;

import cz.jcode.dbviewer.server.entity.ConnectionEntity;
import cz.jcode.dbviewer.server.vo.Connection;
import org.springframework.stereotype.Component;

@Component
public class ConnectionConverter {

    public Connection convertFromEntity(ConnectionEntity connectionEntity) {
        return Connection.builder()
                .id(connectionEntity.getId())
                .name(connectionEntity.getName())
                .jdbcDriver(connectionEntity.getJdbcDriver())
                .jdbcUrl(connectionEntity.getJdbcUrl())
                .username(connectionEntity.getUsername())
                .password(connectionEntity.getPassword())
                .build();
    }

    public ConnectionEntity convertToEntity(Connection connection) {
        return ConnectionEntity.builder()
                .id(connection.getId())
                .name(connection.getName())
                .jdbcDriver(connection.getJdbcDriver())
                .jdbcUrl(connection.getJdbcUrl())
                .username(connection.getUsername())
                .password(connection.getPassword())
                .build();
    }

    public void updateToEntity(ConnectionEntity connectionEntity, Connection connection) {
        connectionEntity.setName(connection.getName());
        connectionEntity.setJdbcDriver(connection.getJdbcDriver());
        connectionEntity.setJdbcUrl(connection.getJdbcUrl());
        connectionEntity.setUsername(connection.getUsername());
        if (connection.getPassword() != null) {
            connectionEntity.setPassword(connection.getPassword());
        }
    }
}
