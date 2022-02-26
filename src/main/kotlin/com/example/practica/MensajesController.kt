package com.example.practica

import org.springframework.web.bind.annotation.*

@RestController
class MensajesController (private val mensajesRepository: MensajesRepository){

    //curl --request POST --header "Content-type:application/json; charset=utf-8" --data "Mensaje inicio" localhost:8083/publicarTexto
    @PostMapping("publicarTexto")
    fun publicar(@RequestBody texto: String): SinRespuesta {
        val respuestas = arrayListOf<String>()
        val mensa = Mensajes(texto, respuestas)
        mensajesRepository.save(mensa)
        println(mensa)
        return SinRespuesta(mensa.id, texto)
    }



    //curl --request GET --header "Content-type:application/json; charset=utf-8" --data "Mensa" localhost:8083/descargarFiltrado
    @GetMapping("descargarFiltrado")
    fun filtrado(@RequestBody texto: String): Any{
        val filtrado = MensajesFiltrados()

        mensajesRepository.findAll().forEach {
            if (it.mensaje.contains(texto)){
                val buscar = SinRespuesta(it.id, it.mensaje)
                filtrado.listaMensajesFiltrados.add(buscar)
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

    //http://localhost:8083/borrar
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

    //http://localhost:8083/Ultimos10
    @GetMapping("Ultimos10")
    fun ultimos(): MensajesFiltrados {
        val listaUltimos = MensajesFiltrados()
        var cont = 0

        mensajesRepository.findAll().asReversed().forEach {
            if (cont < 11){
                val buscar = SinRespuesta(it.id, it.mensaje)
                listaUltimos.listaMensajesFiltrados.add(buscar)
            }
            cont++
        }
        return listaUltimos
    }

    //http://localhost:8083/rango/2/8
    @GetMapping("rango/{ini}/{fin}")
    fun range(@PathVariable ini:Int, @PathVariable fin: Int): MensajesFiltrados {
        val rango = MensajesFiltrados()
        mensajesRepository.findAll().forEach {
            if(it.id in ini..fin){
                val buscar = SinRespuesta(it.id, it.mensaje)
                rango.listaMensajesFiltrados.add(buscar)
            }
        }
        return rango
    }

    //http://localhost:8083/responder/1/hola
    @GetMapping("responder/{idmensa}/{res}")
    fun responder(@PathVariable idmensa: Int, @PathVariable res: String): Any{
        val todo = mensajesRepository.getById(idmensa)

        todo.respuesta.add(res)
        mensajesRepository.save(todo)
        return todo
    }

    //http://localhost:8083/responder/1
    @GetMapping("responder/{id}")
    fun respondidos(@PathVariable id: Int): Any{
        val todo = mensajesRepository.getById(id)
        return if (todo.respuesta.isEmpty()) {
            "Esta vacia la lista del id $id"
        }else{
            todo.respuesta
        }
    }

    //http://localhost:8083/show
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
