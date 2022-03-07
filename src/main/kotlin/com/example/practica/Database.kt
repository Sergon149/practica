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
                Mensajes ("hola", respuestas, generarkey()),
                Mensajes ("", respuestas, generarkey()),
                Mensajes ("que", respuestas, generarkey()),
                Mensajes ("", respuestas, generarkey()),
                Mensajes ("tal", respuestas, generarkey()),
                Mensajes ("", respuestas, generarkey()),
                Mensajes ("estas", respuestas, generarkey()),
                Mensajes ("", respuestas, generarkey()),
                Mensajes ("?", respuestas, generarkey())
            )

            listaMensajes.forEach { mensajesRepository.save(it) }
            listaMensajes.forEach { println(it) }

            println("BASE DE DATOS CREADA CON EXITO ------------------------------")
        }
    }
    fun generarkey(): String {
        val lista = 'a'..'z'
        var aux=""
        var cont=0
        do {
            val generar = lista.random()
            aux += generar
            cont++
        }while (cont < 5)
        return aux
    }
}