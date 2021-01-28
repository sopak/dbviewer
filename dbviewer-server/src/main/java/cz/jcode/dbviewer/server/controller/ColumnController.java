package cz.jcode.dbviewer.server.controller;

import cz.jcode.dbviewer.server.vo.Column;
import cz.jcode.dbviewer.server.service.DbService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/connections/{connectionId}/catalogs/{catalogName}/schemas/{schemaName}/tables/{tableName}/columns")
public class ColumnController {

    private final DbService dbService;

    public ColumnController(DbService dbService) {
        this.dbService = dbService;
    }

    @GetMapping(
            path = ""
    )
    public List<Column> schemas(
            @PathVariable("connectionId") UUID connectionId,
            @PathVariable("catalogName") String catalogName,
            @PathVariable("schemaName") String schemaName,
            @PathVariable("tableName") String tableName
    ) {
        return dbService.getAllColumns(connectionId, catalogName, schemaName, tableName);
    }
}
