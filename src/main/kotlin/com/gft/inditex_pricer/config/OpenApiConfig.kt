package com.gft.inditex_pricer.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.KotlinModule
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import org.springdoc.core.customizers.OpenApiCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.core.io.ClassPathResource

@Configuration
class OpenApiConfig {

    @Bean
    @Primary
    fun customOpenAPI(): OpenAPI {
        val yamlFile = ClassPathResource("openapi-public.yaml")

        return try {
            val yamlContent = yamlFile.inputStream.use {
                it.readBytes().toString(Charsets.UTF_8)
            }

            val mapper = ObjectMapper(YAMLFactory())
                .registerModule(KotlinModule.Builder().build())

            mapper.readValue(yamlContent, OpenAPI::class.java)

        } catch (e: Exception) {
            println("Error loading OpenAPI file: ${e.message}")
            e.printStackTrace()

            // Fallback básico
            OpenAPI().info(
                Info()
                    .title("Inditex Pricing API")
                    .description("Error loading custom OpenAPI file")
                    .version("1.0")
            )
        }
    }

    // Opcional: Customizer para sobrescribir completamente la documentación
    @Bean
    fun openApiCustomizer(): OpenApiCustomizer {
        return OpenApiCustomizer { openApi ->
            try {
                val yamlFile = ClassPathResource("openapi-public.yaml")
                val yamlContent = yamlFile.inputStream.use {
                    it.readBytes().toString(Charsets.UTF_8)
                }

                val mapper = ObjectMapper(YAMLFactory())
                    .registerModule(KotlinModule.Builder().build())

                val customOpenApi = mapper.readValue(yamlContent, OpenAPI::class.java)

                // Reemplazar completamente con el contenido del archivo
                openApi.info = customOpenApi.info
                openApi.servers = customOpenApi.servers
                openApi.tags = customOpenApi.tags
                openApi.paths = customOpenApi.paths
                openApi.components = customOpenApi.components
                openApi.security = customOpenApi.security
                openApi.externalDocs = customOpenApi.externalDocs

                println("Successfully loaded custom OpenAPI from YAML file")

            } catch (e: Exception) {
                println("Error in OpenApiCustomizer: ${e.message}")
                e.printStackTrace()
            }
        }
    }
}
