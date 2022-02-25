package com.example.practica

import com.google.gson.Gson

class MensajesFiltrados() {
    val listaMensajesFiltrados = mutableListOf<Mensajes>()
    override fun toString(): String {
        val gson = Gson()
        return gson.toJson(this)

    }
}