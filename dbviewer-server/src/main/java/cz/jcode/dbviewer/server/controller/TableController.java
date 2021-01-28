package cz.jcode.dbviewer.server.controller;

import cz.jcode.dbviewer.server.vo.Table;
import cz.jcode.dbviewer.server.service.DbService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/connections/{connectionId}/catalogs/{catalogName}/schemas/{schemaName}/tables")
public class TableController {

    private final DbService dbService;

    public TableController(DbService dbService) {
        this.dbService = dbService;
    }

    @GetMapping(
            path = ""
    )
    public List<Table> schemas(
            @PathVariable("connectionId") UUID connectionId,
            @PathVariable("catalogName") String catalogName,
            @PathVariable("schemaName") String schemaName
    ) {
        return dbService.getAllTables(connectionId, catalogName, schemaName);
    }
}
