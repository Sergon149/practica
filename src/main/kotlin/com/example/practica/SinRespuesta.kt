package com.example.practica

import com.google.gson.Gson

class SinRespuesta(var id: Int, var mensaje: String) {


    override fun toString(): String {
        val gson= Gson()
        return gson.toJson(this)
    }
}