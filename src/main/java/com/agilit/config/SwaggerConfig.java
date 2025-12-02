package com.agilit.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Configuração do Swagger/OpenAPI para documentação da API REST.
 * 
 * Acesso à documentação:
 * - Swagger UI: http://localhost:8080/openapi.json
 * - OpenAPI JSON: http://localhost:8080/openapi.json
 */
@OpenAPIDefinition(
    info = @Info(
        title = "AGILIT LOAN API",
        version = "1.0.0",
        description = "API REST para sistema de empréstimos peer-to-peer entre Credores e Devedores. " +
                     "Sistema completo de gestão de ofertas, propostas, interesses, empréstimos, parcelas e notificações.",
        contact = @Contact(
            name = "Equipe AGILIT",
            email = "contato@agilit.com"
        ),
        license = @License(
            name = "MIT License",
            url = "https://opensource.org/licenses/MIT"
        )
    ),
    servers = {
        @Server(
            description = "Servidor Local",
            url = "http://localhost:8080"
        ),
        @Server(
            description = "Servidor de Desenvolvimento",
            url = "http://dev.agilit.com"
        )
    },
    tags = {
        @Tag(name = "Credor", description = "Operações relacionadas a Credores (quem empresta dinheiro)"),
        @Tag(name = "Devedor", description = "Operações relacionadas a Devedores (quem toma empréstimo)"),
        @Tag(name = "Oferta", description = "Ofertas privadas de empréstimo criadas por Credores"),
        @Tag(name = "Proposta", description = "Propostas públicas de empréstimo visíveis para Devedores"),
        @Tag(name = "Interesse", description = "Manifestação de interesse de Devedores em Propostas"),
        @Tag(name = "Empréstimo", description = "Empréstimos ativos, pagos ou atrasados"),
        @Tag(name = "Parcela", description = "Parcelas de empréstimos e controle de pagamentos"),
        @Tag(name = "Notificação", description = "Sistema de notificações para Credores e Devedores")
    }
)
public class SwaggerConfig {
    // Esta classe serve apenas para configuração via anotações
    // Não precisa de métodos ou lógica adicional
}

// Made with Bob
