package cz.jcode.dbviewer.server.controller;

import cz.jcode.dbviewer.server.vo.Record;
import cz.jcode.dbviewer.server.service.DbService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/connections/{connectionId}/catalogs/{catalogName}/schemas/{schemaName}/tables/{tableName}/records")
public class RecordController {

    private final DbService dbService;

    public RecordController(DbService dbService) {
        this.dbService = dbService;
    }

    @GetMapping(
            path = ""
    )
    public List<Record> schemas(
            @PathVariable("connectionId") UUID connectionId,
            @PathVariable("catalogName") String catalogName,
            @PathVariable("schemaName") String schemaName,
            @PathVariable("tableName") String tableName,
            @RequestParam(name = "offset", defaultValue = "0") int offset,
            @RequestParam(name = "limit", defaultValue = "5") int limit
    ) {
        return dbService.getRecords(connectionId, catalogName, schemaName, tableName, offset, limit);
    }
}
