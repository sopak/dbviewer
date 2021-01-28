package cz.jcode.dbviewer.server.repository;

import cz.jcode.dbviewer.server.entity.ConnectionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.annotation.Nullable;
import java.util.UUID;

public interface ConnectionRepository extends JpaRepository<ConnectionEntity, UUID> {

    @Nullable
    ConnectionEntity findOneById(UUID id);
}