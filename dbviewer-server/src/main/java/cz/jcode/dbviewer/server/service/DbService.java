package cz.jcode.dbviewer.server.service;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import cz.jcode.dbviewer.server.helper.BiConsumerThatThrows;
import cz.jcode.dbviewer.server.helper.CloseableDataSourceWrapper;
import cz.jcode.dbviewer.server.helper.FunctionThatThrows;
import cz.jcode.dbviewer.server.vo.Catalog;
import cz.jcode.dbviewer.server.vo.Column;
import cz.jcode.dbviewer.server.vo.Schema;
import cz.jcode.dbviewer.server.vo.Table;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;
import java.util.UUID;

@Service
public class DbService {
    private static final String TABLE_CAT = "TABLE_CAT";
    @SuppressWarnings("SpellCheckingInspection")
    private static final String TABLE_SCHEM = "TABLE_SCHEM";
    private static final String TABLE_NAME = "TABLE_NAME";
    private static final String TABLE_TYPE = "TABLE_TYPE";
    private static final String COLUMN_NAME = "COLUMN_NAME";
    private static final String TYPE_NAME = "TYPE_NAME";
    private static final String SQL_DATA_TYPE = "SQL_DATA_TYPE";
    private static final String COLUMN_DEF = "COLUMN_DEF";
    private static final String IS_NULLABLE = "IS_NULLABLE";
    private static final String ORDINAL_POSITION = "ORDINAL_POSITION";

    private final ConnectionService connectionService;

    public DbService(ConnectionService connectionService) {
        this.connectionService = connectionService;
    }

    public List<Catalog> getAllCatalogs(UUID connectionId) {
        return extractRecords(
                connectionId,
                connection -> connection.getMetaData().getCatalogs(),
                this::addCatalog
        );
    }

    public List<Schema> getAllSchemas(UUID connectionId, String catalogName) {
        return extractRecords(
                connectionId,
                connection -> connection.getMetaData().getSchemas(catalogName, null),
                this::addSchema
        );
    }

    public List<Table> getAllTables(UUID connectionId, String catalogName, String schemaName) {
        return extractRecords(
                connectionId,
                connection -> connection.getMetaData().getTables(catalogName, schemaName, null, null),
                this::addTable
        );
    }

    public List<Column> getAllColumns(UUID connectionId, String catalogName, String schemaName, String tableName) {
        return extractRecords(
                connectionId,
                connection -> connection.getMetaData().getColumns(catalogName, schemaName, tableName, null),
                this::addColumn
        );
    }

    public List<cz.jcode.dbviewer.server.vo.Record> getRecords(UUID connectionId, String catalogName, String schemaName, String tableName, int offset, int limit) {
        return extractRecords(
                connectionId,
                connection -> getRecordsWithOffsetAndLimit(
                        catalogName,
                        schemaName,
                        tableName,
                        offset,
                        limit,
                        connection
                ),
                this::addRecord,
                offset
        );
    }

    /**
     * Naive way howto get the data and it depends on Database dialect mainly offset and limit.
     * Offset and limit are handled in code for better compatibility.
     * A native solution for each driver will be much better.
     */
    private ResultSet getRecordsWithOffsetAndLimit(String catalogName, String schemaName, String tableName, int offset, int limit, Connection connection) throws SQLException {
        final Statement statement = connection.createStatement();
        statement.setMaxRows(offset + limit);

        connection.setCatalog(catalogName);
        connection.setSchema(schemaName);
        return statement.executeQuery(String.format(
                "SELECT * FROM %s",
                sanitizeName(connection, tableName)
        ));
    }

    /**
     * dynamic tables in Statements are not safe against SQL injections, so this may prevent it a bit
     */
    private String sanitizeName(Connection connection, String name) throws SQLException {
        String quoteString = connection.getMetaData().getIdentifierQuoteString();
        return quoteString + name.replaceAll(quoteString, "") + quoteString;
    }

    private void addRecord(ImmutableList.Builder<cz.jcode.dbviewer.server.vo.Record> records, ResultSet rs) throws SQLException {
        ImmutableMap.Builder<String, Object> values = ImmutableMap.builder();

        for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
            int index = i + 1;
            values.put(
                    rs.getMetaData().getColumnName(index),
                    rs.getObject(index)
            );
        }

        records.add(
                cz.jcode.dbviewer.server.vo.Record.builder()
                        .values(values.build())
                        .build()
        );
    }

    private void addCatalog(ImmutableList.Builder<Catalog> catalogs, ResultSet rs) throws SQLException {
        catalogs.add(
                Catalog.builder()
                        .name(rs.getString(TABLE_CAT))
                        .build()
        );
    }

    private void addSchema(ImmutableList.Builder<Schema> schemas, ResultSet rs) throws SQLException {
        schemas.add(
                Schema.builder()
                        .name(rs.getString(TABLE_SCHEM))
                        .build()
        );
    }

    private void addTable(ImmutableList.Builder<Table> tables, ResultSet rs) throws SQLException {
        tables.add(
                Table.builder()
                        .name(rs.getString(TABLE_NAME))
                        .type(rs.getString(TABLE_TYPE))
                        .build()
        );
    }

    private void addColumn(ImmutableList.Builder<Column> columns, ResultSet rs) throws SQLException {
        columns.add(
                Column.builder()
                        .name(rs.getString(COLUMN_NAME))
                        .sqlDataTypeName(rs.getString(TYPE_NAME))
                        .sqlDataType(JDBCType.valueOf(rs.getInt(SQL_DATA_TYPE)).getName())
                        .defaultValue(rs.getString(COLUMN_DEF))
                        .isNullable(rs.getBoolean(IS_NULLABLE))
                        .ordinalPosition(rs.getInt(ORDINAL_POSITION))
                        .build()
        );
    }

    private <T> ImmutableList<T> extractRecords(
            UUID connectionId,
            FunctionThatThrows<Connection, ResultSet> recordSupplier,
            BiConsumerThatThrows<ImmutableList.Builder<T>, ResultSet> recordConsumer
    ) {
        return extractRecords(connectionId, recordSupplier, recordConsumer, 0);
    }

    private <T> ImmutableList<T> extractRecords(
            UUID connectionId,
            FunctionThatThrows<Connection, ResultSet> recordSupplier,
            BiConsumerThatThrows<ImmutableList.Builder<T>, ResultSet> recordConsumer,
            int offset
    ) {
        try (CloseableDataSourceWrapper dataSourceWrapper = new CloseableDataSourceWrapper(
                connectionService.getDataSource(connectionId)
        )) {
            return extractRecordsFromDataSourceAndSkipToOffset(
                    recordSupplier,
                    recordConsumer,
                    dataSourceWrapper.getDataSource(),
                    offset
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private <T> ImmutableList<T> extractRecordsFromDataSourceAndSkipToOffset(
            FunctionThatThrows<Connection, ResultSet> recordSupplier,
            BiConsumerThatThrows<ImmutableList.Builder<T>, ResultSet> recordConsumer,
            DataSource dataSource,
            int offset
    ) throws Exception {
        ImmutableList.Builder<T> entries = ImmutableList.builder();
        try (Connection connection = dataSource.getConnection()) {
            try (ResultSet rs = recordSupplier.apply(connection)) {
                while (rs.next()) {
                    if (offset > 0) {
                        offset--;
                    } else {
                        recordConsumer.accept(entries, rs);
                    }
                }
            }
        }
        return entries.build();
    }
}
