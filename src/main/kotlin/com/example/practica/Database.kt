package com.example.practica

import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class Database {
    @Bean
    fun initDatabase(mensajesRepository: MensajesRepository):CommandLineRunner{
        return CommandLineRunner{
            println("BASE DE DATOS CREANDOSE --------------------------")
            val listaMensajes = listOf(
                Mensajes ("hola", ""),
                Mensajes ("", ""),
                Mensajes ("que", ""),
                Mensajes ("", ""),
                Mensajes ("tal", ""),
                Mensajes ("", ""),
                Mensajes ("estas", ""),
                Mensajes ("", ""),
                Mensajes ("?", "")
            )

            listaMensajes.forEach { mensajesRepository.save(it) }
            listaMensajes.forEach { println(it) }

            println("BASE DE DATOS CREADA CON EXITO ------------------------------")
        }
    }
}