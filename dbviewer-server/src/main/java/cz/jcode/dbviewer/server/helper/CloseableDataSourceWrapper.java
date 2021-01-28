package cz.jcode.dbviewer.server.helper;

import javax.sql.DataSource;
import java.io.Closeable;
import java.io.IOException;

public class CloseableDataSourceWrapper implements Closeable {

    private final DataSource dataSource;

    public CloseableDataSourceWrapper(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    @Override
    public void close() throws IOException {
        if (dataSource instanceof Closeable) {
            ((Closeable) dataSource).close();
        }
    }
}
