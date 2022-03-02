package hello.hellospring.repository;

import org.h2.tools.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Statement;

@Component
public class DatabaseConfig implements ApplicationRunner {

    @Autowired
    DataSource dataSource;

    @Override
    public void run(ApplicationArguments args) throws Exception {

//        Server server = Server.createTcpServer("-tcp", "-tcpAllowOthers", "-tcpPort", "9099").start();
//        System.out.println(server.getURL());
//        System.out.println(server.getPort());

        try(Connection connection = dataSource.getConnection()){
            System.out.println(connection.getMetaData().getURL());
            System.out.println(connection.getMetaData().getUserName());

            Statement statement = connection.createStatement();
            String sql = "DROP TABLE TESTTABLE CASCADE";
            statement.executeUpdate(sql);

//            Statement statement = connection.createStatement();
            sql = "CREATE TABLE TESTTABLE(ID INTEGER NOT NULL, VALUE VARCHAR(255), PRIMARY KEY (ID) )";
            statement.executeUpdate(sql);

            String sql2 = "INSERT INTO TESTTABLE VALUES(1, 'value')";
            statement.execute(sql2);
        } catch (Exception e){
            System.out.println(e);
        }
    }
}
