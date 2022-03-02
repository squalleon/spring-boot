package hello.hellospring.repository;

import org.h2.tools.Server;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;
import java.sql.SQLException;

@Configuration
@Profile("local")
public class H2ServerConfig {

//    @Bean
//    public Server h2TcpServer() throws SQLException {
//        return Server.createTcpServer().start();
//    }

    @Bean
    @ConfigurationProperties("spring.datasource.hikari")
    public DataSource dataSource() throws SQLException {
        // 이 구문 타지 않음. 이유는?
        Server.createTcpServer("-tcp", "-tcpAllowOthers", "-tcpPort", "9092").start();

        return new com.zaxxer.hikari.HikariDataSource();
    }
}
