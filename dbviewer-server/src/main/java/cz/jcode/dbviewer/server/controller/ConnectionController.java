package cz.jcode.dbviewer.server.controller;

import cz.jcode.dbviewer.server.service.ConnectionService;
import cz.jcode.dbviewer.server.vo.Connection;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/connections")
public class ConnectionController {

    private final ConnectionService connectionService;

    public ConnectionController(ConnectionService connectionService) {
        this.connectionService = connectionService;
    }

    @GetMapping(
            path = ""
    )
    public List<Connection> connections() {
        return connectionService.findAll();
    }

    @GetMapping(
            path = "/names"

    )
    public List<String> connectionNames() {
        return connectionService.findAllNames();
    }

    @PutMapping(
            path = "/{id}"
    )
    public Connection connections(
            @PathVariable("id") UUID id,
            @RequestBody @Validated Connection connection
    ) {
        return connectionService.update(connection);
    }

    @PostMapping(
            path = ""
    )
    public Connection connections(
            @RequestBody @Validated Connection connection
    ) {
        return connectionService.create(connection);
    }

    @DeleteMapping(
            path = "/{id}"
    )
    public void connections(
            @PathVariable("id") UUID id
    ) {
        connectionService.deleteById(id);
    }
}
