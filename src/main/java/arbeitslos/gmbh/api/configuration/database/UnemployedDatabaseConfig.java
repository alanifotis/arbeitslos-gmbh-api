package arbeitslos.gmbh.api.configuration.database;

import arbeitslos.gmbh.api.converter.UnemployedEntityReadingConverter;
import arbeitslos.gmbh.api.converter.UnemployedEntityWritingConverter;
import arbeitslos.gmbh.api.model.EmploymentStatus;
import org.springframework.context.annotation.Bean;

import java.util.List;


import io.r2dbc.postgresql.PostgresqlConnectionConfiguration;
import io.r2dbc.postgresql.PostgresqlConnectionFactory;
import io.r2dbc.postgresql.codec.EnumCodec;
import io.r2dbc.spi.ConnectionFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.r2dbc.connection.R2dbcTransactionManager;
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer;
import org.springframework.transaction.ReactiveTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@Configuration
@EnableTransactionManagement
@EnableR2dbcRepositories
@EnableR2dbcAuditing
public class UnemployedDatabaseConfig extends AbstractR2dbcConfiguration {

    @Bean
    public ConnectionFactory connectionFactory() {
        return new PostgresqlConnectionFactory(
                PostgresqlConnectionConfiguration.builder()
                        .addHost("192.168.0.149", 5432)
                        .database("reactive")
                        .username("unemployed")
                        .password("test")
                        .codecRegistrar(EnumCodec.builder().withEnum("employmentstatus", EmploymentStatus.class).build())
                        .build()
        );
    }

    @Override
    protected List<Object> getCustomConverters() {
        return List.of(
                new UnemployedEntityReadingConverter(),
                new UnemployedEntityWritingConverter()
        );
    }

    @Bean
    ReactiveTransactionManager transactionManager(ConnectionFactory connectionFactory) {
        return new R2dbcTransactionManager(connectionFactory);
    }

    @Bean
    public ConnectionFactoryInitializer initializer(ConnectionFactory connectionFactory) {

        ConnectionFactoryInitializer initializer = new ConnectionFactoryInitializer();
        initializer.setConnectionFactory(connectionFactory);

        return initializer;
    }

}