package cz.jcode.dbviewer.server.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;

import java.util.Map;

@Value
@Builder
public class Record {
    @JsonProperty(required = true)
    Map<String, Object> values;
}
