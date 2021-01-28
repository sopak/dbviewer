package cz.jcode.dbviewer.server.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Catalog {
    @JsonProperty(required = true)
    String name;
}
