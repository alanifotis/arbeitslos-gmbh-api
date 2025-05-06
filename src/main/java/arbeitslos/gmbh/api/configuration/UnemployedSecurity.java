package arbeitslos.gmbh.api.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebFluxSecurity
public class UnemployedSecurity {


    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http
                .cors(withDefaults())
                .authorizeExchange(exchanges -> {
                            exchanges.pathMatchers("/api/v1/unemployed/**").permitAll();
                            exchanges.anyExchange().authenticated();
                        }
                )
                .httpBasic(withDefaults())
                .formLogin(withDefaults());
        return http.build();
    }
}

