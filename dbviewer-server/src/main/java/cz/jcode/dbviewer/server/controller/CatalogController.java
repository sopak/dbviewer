package cz.jcode.dbviewer.server.controller;

import cz.jcode.dbviewer.server.service.DbService;
import cz.jcode.dbviewer.server.vo.Catalog;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/connections/{connectionId}/catalogs")
public class CatalogController {

    private final DbService dbService;

    public CatalogController(DbService dbService) {
        this.dbService = dbService;
    }

    @GetMapping(
            path = ""
    )
    public List<Catalog> catalogs(@PathVariable("connectionId") UUID connectionId) {
        return dbService.getAllCatalogs(connectionId);
    }
}
