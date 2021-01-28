package cz.jcode.dbviewer.server.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;
import lombok.With;

import java.util.UUID;

@Value
@Builder
public class Connection {
    @JsonProperty
    UUID id;
    @JsonProperty(required = true)
    String name;
    @JsonProperty(required = true)
    String jdbcDriver;
    @JsonProperty(required = true)
    String jdbcUrl;
    @JsonProperty(required = true)
    String username;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @With
    String password;
}
