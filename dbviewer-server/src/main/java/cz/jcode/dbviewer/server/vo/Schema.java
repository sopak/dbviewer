package cz.jcode.dbviewer.server.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Schema {
    @JsonProperty(required = true)
    String name;
}
