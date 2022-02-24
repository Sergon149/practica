package com.example.practica

import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class Database {
    @Bean
    fun initDatabase(preguntasRepository: PreguntasRepository):CommandLineRunner{
        return CommandLineRunner{
            println("BASE DE DATOS CREANDOSE --------------------------")
            val listaMensajes = listOf(
                Preguntas ("hola"),
                Preguntas (""),
                Preguntas ("que"),
                Preguntas (""),
                Preguntas ("tal"),
                Preguntas (""),
                Preguntas ("estas"),
                Preguntas (""),
                Preguntas ("?")
            )

            listaMensajes.forEach { preguntasRepository.save(it) }
            listaMensajes.forEach { println(it) }

            println("BASE DE DATOS CREADA CON EXITO ------------------------------")
        }
    }
}