package cz.jcode.dbviewer.server.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Column {
    @JsonProperty(required = true)
    String name;
    @JsonProperty(required = true)
    String sqlDataTypeName;
    @JsonProperty(required = true)
    String sqlDataType;
    @JsonProperty
    String defaultValue;
    @JsonProperty(required = true)
    Boolean isNullable;
    @JsonProperty
    int ordinalPosition;
}
