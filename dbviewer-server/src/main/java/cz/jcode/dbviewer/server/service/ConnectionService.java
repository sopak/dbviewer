package cz.jcode.dbviewer.server.service;

import cz.jcode.dbviewer.server.converter.ConnectionConverter;
import cz.jcode.dbviewer.server.entity.ConnectionEntity;
import cz.jcode.dbviewer.server.repository.ConnectionRepository;
import cz.jcode.dbviewer.server.vo.Connection;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import javax.sql.DataSource;
import java.util.List;
import java.util.UUID;

import static java.util.stream.Collectors.toList;

@Service
@Transactional
public class ConnectionService {

    private final ConnectionRepository connectionRepository;

    private final ConnectionConverter connectionConverter;

    public ConnectionService(ConnectionRepository connectionRepository, ConnectionConverter connectionConverter) {
        this.connectionRepository = connectionRepository;
        this.connectionConverter = connectionConverter;
    }

    public List<Connection> findAll() {
        return connectionRepository.findAll().stream()
                .map(connectionConverter::convertFromEntity)
                .collect(toList());
    }

    public List<String> findAllNames() {
        return findAll().stream().map(Connection::getName).collect(toList());
    }

    public Connection create(Connection connection) {
        ConnectionEntity connectionEntity = connectionRepository.save(
                connectionConverter.convertToEntity(connection)
        );
        return connectionConverter.convertFromEntity(connectionEntity);
    }

    public Connection update(Connection connection) {
        ConnectionEntity connectionEntity = getConnectionEntity(connection.getId());
        connectionConverter.updateToEntity(connectionEntity, connection);
        ConnectionEntity updatedConnectionEntity = connectionRepository.save(connectionEntity);
        return connectionConverter.convertFromEntity(updatedConnectionEntity);
    }

    private ConnectionEntity getConnectionEntity(UUID connectionId) {
        ConnectionEntity connectionEntity = connectionRepository.findOneById(connectionId);
        if (connectionEntity == null) {
            throw new EntityNotFoundException(String.format("Connection entity with id '%s' not found", connectionId));
        }
        return connectionEntity;
    }

    public void deleteById(UUID connectionId) {
        ConnectionEntity connectionEntity = getConnectionEntity(connectionId);
        connectionRepository.deleteById(connectionEntity.getId());
    }

    public DataSource getDataSource(UUID connectionId) {
        ConnectionEntity connectionEntity = getConnectionEntity(connectionId);
        DataSourceBuilder<?> dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.driverClassName(connectionEntity.getJdbcDriver());
        dataSourceBuilder.url(connectionEntity.getJdbcUrl());
        dataSourceBuilder.username(connectionEntity.getUsername());
        dataSourceBuilder.password(connectionEntity.getPassword());
        return dataSourceBuilder.build();
    }
}
