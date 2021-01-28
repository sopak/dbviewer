package cz.jcode.dbviewer.derby.instance;

import org.apache.derby.drda.NetworkServerControl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.PrintWriter;
import java.net.InetAddress;

@SpringBootApplication
public class DbViewerDerbyInstanceApplication {

    public static final String USER = "sa";
    public static final String PASSWORD = "sa";

    public static void main(String[] args) throws Exception {
        InetAddress address = InetAddress.getLoopbackAddress();
        final NetworkServerControl server = new NetworkServerControl(
                address,
                NetworkServerControl.DEFAULT_PORTNUMBER,
                USER,
                PASSWORD
        );
        server.start(new PrintWriter(System.out));
        Runtime.getRuntime().addShutdownHook(shutdownHook(server));

        SpringApplication.run(DbViewerDerbyInstanceApplication.class, args);
    }

    private static Thread shutdownHook(final NetworkServerControl server) {
        return new Thread(() -> {
            try {
                server.shutdown();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
