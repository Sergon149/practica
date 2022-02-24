package com.example.practica

import com.google.gson.Gson
import org.springframework.web.bind.annotation.*

@RestController
class PreguntasController (private val preguntasRepository: PreguntasRepository){

    //curl --request POST --header "Content-type:application/json; charset=utf-8" --data "Mensaje inicio" localhost:8083/publicarTexto
    @PostMapping("publicarTexto")
    fun publicar(@RequestBody texto: String): Preguntas{
        val preg = Preguntas(texto)
        preguntasRepository.save(preg)
        println(preg)
        return preg
    }
    //curl --request GET --header "Content-type:application/json; charset=utf-8" --data "Mensa" localhost:8083/descargarFiltrado
    @GetMapping("descargarFiltrado")
    fun filtrado(@RequestBody texto: String): Any{
        var filtrado = MensajesFiltrados()
        preguntasRepository.findAll().forEach {
            if (it.mensaje.contains(texto)){
                filtrado.listaMensajesFiltrados.add(it)
            }
        }
        return if (filtrado.listaMensajesFiltrados.size>0)
            filtrado
        else
            "ERROR NOT FOUND"
    }

    /*
    fun filtrado(@RequestBody texto: String): String {
        var listaMensajeFiltrado = mutableListOf<Preguntas>()
        preguntasRepository.findAll().forEach {
            if (it.mensaje.contains(texto)){
                listaMensajeFiltrado.add(it)
            }
            return "Preguntas filtradas "+listaMensajeFiltrado
        }
        return "ERROR NOT FOUND"
    }
     */

    @GetMapping("borrar")
    fun delete(): Boolean{
        var borrado= false
        preguntasRepository.findAll().forEach {
            if (it.mensaje == ""){
                borrado = true
                preguntasRepository.delete(it)
            }else{
                borrado=false
            }
        }
        return borrado
    }

    @GetMapping("Ultimos10")
    fun ultimos(): MensajesFiltrados {
        val listaUltimos = MensajesFiltrados()

        listaUltimos.listaMensajesFiltrados.addAll(preguntasRepository.findAll())

        return if (listaUltimos.listaMensajesFiltrados.size>10){
            listaUltimos.listaMensajesFiltrados.filter { it.id > (listaUltimos.listaMensajesFiltrados.size-10) }
            listaUltimos
        }else{
            listaUltimos
        }
    }

    @GetMapping("rango/{inicio}/{fin}")
    fun range(@PathVariable ini:Int, @PathVariable fin: Int): List<Preguntas> {
        val listafiltrada = preguntasRepository.findAll().filter {
            it.id in ini..fin
        }
        return listafiltrada
    }

    @GetMapping("show")
    fun show():MutableList<Preguntas>{
        return preguntasRepository.findAll()
    }
}
    /*
    //curl -v --request GET --data "{\"id\":1, \"nombre\":\"Caterpie\", \"nivel\":5}" http://localhost:8083/insertPokemonBody
    @GetMapping("insertPokemonBody")
    fun insertPokemonBody(@RequestBody texto: String){
        val gson = Gson()
        val pokemon = gson.fromJson(texto, Pokemon::class.java)
        pokemonRepository.save(pokemon)
        pokemonRepository.findAll().forEach { println(it)}
    }
    */
