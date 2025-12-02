package com.agilit;

import org.glassfish.jersey.server.ResourceConfig;

/**
 * Configuração principal da aplicação JAX-RS usando Jersey.
 *
 * IMPORTANTE: Não usamos @ApplicationPath aqui porque o path base "/api"
 * já está definido no web.xml através do <url-pattern>/api/*</url-pattern>
 *
 * Usar ambos causaria duplicação de path: /api/api/endpoint
 *
 * Esta classe:
 * - Escaneia automaticamente todos os controllers em com.agilit.controller
 * - Registra o Jackson para serialização/deserialização JSON
 * - É referenciada no web.xml como jakarta.ws.rs.Application
 */
public class App extends ResourceConfig {
    
    public App() {
        // Escaneia pacotes para encontrar controllers (@Path) e providers
        packages(
            "com.agilit.controller",           // Controllers REST
            "com.agilit.config",                // Exception mappers e outros providers
            "org.glassfish.jersey.jackson"     // Suporte JSON via Jackson
        );
    }
}
