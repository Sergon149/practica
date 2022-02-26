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
            val respuestas = arrayListOf<String>()
            val listaMensajes = listOf(
                Mensajes ("hola", respuestas),
                Mensajes ("", respuestas),
                Mensajes ("que", respuestas),
                Mensajes ("", respuestas),
                Mensajes ("tal", respuestas),
                Mensajes ("", respuestas),
                Mensajes ("estas", respuestas),
                Mensajes ("", respuestas),
                Mensajes ("?", respuestas)
            )

            listaMensajes.forEach { mensajesRepository.save(it) }
            listaMensajes.forEach { println(it) }

            println("BASE DE DATOS CREADA CON EXITO ------------------------------")
        }
    }
}