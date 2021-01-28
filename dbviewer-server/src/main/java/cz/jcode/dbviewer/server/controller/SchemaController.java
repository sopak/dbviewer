package cz.jcode.dbviewer.server.controller;

import cz.jcode.dbviewer.server.service.DbService;
import cz.jcode.dbviewer.server.vo.Schema;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/connections/{connectionId}/catalogs/{catalogName}/schemas")
public class SchemaController {

    private final DbService dbService;

    public SchemaController(DbService dbService) {
        this.dbService = dbService;
    }

    @GetMapping(
            path = ""
    )
    public List<Schema> schemas(
            @PathVariable("connectionId") UUID connectionId,
            @PathVariable("catalogName") String catalogName
    ) {
        return dbService.getAllSchemas(connectionId, catalogName);
    }
}
