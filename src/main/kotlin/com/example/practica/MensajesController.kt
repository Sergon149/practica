package com.example.practica

import org.springframework.web.bind.annotation.*

@RestController
class MensajesController (private val mensajesRepository: MensajesRepository){

    //curl --request POST --header "Content-type:application/json; charset=utf-8" --data "Mensaje inicio" localhost:8083/publicarTexto
    @PostMapping("publicarTexto")
    fun publicar(@RequestBody texto: String): SinRespuesta{
        val mensa = Mensajes(texto, "")
        mensajesRepository.save(mensa)
        println(mensa)
        var añadir = SinRespuesta(mensa.id,texto)
        return añadir
    }



    //curl --request GET --header "Content-type:application/json; charset=utf-8" --data "Mensa" localhost:8083/descargarFiltrado
    @GetMapping("descargarFiltrado")
    fun filtrado(@RequestBody texto: String): Any{
        var filtrado = MensajesFiltrados()
        mensajesRepository.findAll().forEach {
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
        mensajesRepository.findAll().forEach {
            if (it.mensaje == ""){
                borrado = true
                mensajesRepository.delete(it)
            }else{
                borrado=false
            }
        }
        return borrado
    }

    @GetMapping("Ultimos10")
    fun ultimos(): MensajesFiltrados {
        var listaUltimos = MensajesFiltrados()
        var cont = 0

        mensajesRepository.findAll().asReversed().forEach {
            if (cont < 11){
                listaUltimos.listaMensajesFiltrados.add(it)
            }
            cont++
        }
        return listaUltimos
    }

    @GetMapping("rango/{inicio}/{fin}")
    fun range(@PathVariable ini:Int, @PathVariable fin: Int): MensajesFiltrados {
        var rango = MensajesFiltrados()
        mensajesRepository.findAll().forEach {
            if(it.id in ini..fin){
                rango.listaMensajesFiltrados.add(it)
            }
        }
        return rango
    }

    @GetMapping("responder/{idmensa}/{res}")
    fun responder(@PathVariable idmensa: Int, @PathVariable res: String): Any{
        var todo = mensajesRepository.getById(idmensa)

        return if(todo.respuesta==""){
            todo.respuesta=res
            mensajesRepository.save(todo)
            todo
        }else{
            "Ya existe una respuesta para este id, que contiene "+todo.respuesta
        }
    }

    @GetMapping("show")
    fun show():MutableList<Mensajes>{
        return mensajesRepository.findAll()
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
