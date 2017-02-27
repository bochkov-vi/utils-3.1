package web; /**
 * Created by home on 27.02.17.
 */

import org.h2.tools.Server;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.sql.SQLException;

public class H2StarterListener implements ServletContextListener {
    Server server;


    public void contextInitialized(ServletContextEvent sce) {
        try {
            Server server = Server.createTcpServer().start();
        } catch (SQLException e) {
            throw new RuntimeException(e);

        }
    }

    public void contextDestroyed(ServletContextEvent sce) {
        server.stop();
    }
}
