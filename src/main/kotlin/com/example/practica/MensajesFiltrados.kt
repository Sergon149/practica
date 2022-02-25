package com.example.practica

import com.google.gson.Gson

class MensajesFiltrados() {
    val listaMensajesFiltrados = mutableListOf<SinRespuesta>()
    override fun toString(): String {
        val gson = Gson()
        return gson.toJson(this)

    }
}