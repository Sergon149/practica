package com.example.practica

import org.springframework.web.bind.annotation.*
import java.security.MessageDigest
import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

@RestController
class MensajesController (private val mensajesRepository: MensajesRepository){

    val type = "AES/ECB/PKCS5Padding"
    val respuestas = arrayListOf<String>()

    //curl --request POST --header "Content-type:application/json; charset=utf-8" --data "Mensaje inicio" localhost:8083/publicarTexto
    @PostMapping("publicarTexto")
    fun publicar(@RequestBody texto: String): SinRespuesta {

        val mensa = Mensajes(texto, respuestas, generarkey())
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
    fun delete() : Boolean{
        var cont = 0
        var bool = false
        mensajesRepository.findAll().forEach {
            if (it.mensaje == ""){
                bool=true
                cont+=1
                mensajesRepository.delete(it)
            }
        }
        println("$bool - Se han borrado $cont registros")
        return bool
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
    //http://localhost:8083/responder/1/que
    //http://localhost:8083/responder/1/tal
    //http://localhost:8083/responder/1/estas
    @GetMapping("responder/{idmensa}/{res}")
    fun responder(@PathVariable idmensa: Int, @PathVariable res: String): Any{
        val todo = mensajesRepository.getById(idmensa)

        todo.respuesta.add(res)
        mensajesRepository.save(todo)
        return todo
    }

    //http://localhost:8083/respuestas/1
    @GetMapping("respuestas/{id}")
    fun respondidos(@PathVariable id: Int): Any{
        val todo = mensajesRepository.getById(id)
        return if (todo.respuesta.isEmpty()) {
            "Esta vacia la lista del id $id"
        }else{
            todo.respuesta
        }
    }

    @GetMapping("coincide/{id}")
    fun coincide(@PathVariable id: Int): Any{
        val igual = MensajesFiltrados()
        val todo = mensajesRepository.getById(id)
        mensajesRepository.findAll().forEach {
            if (it.respuesta.contains(todo.mensaje)){
                val buscar = SinRespuesta(it.id, it.mensaje)
                igual.listaMensajesFiltrados.add(buscar)
            }
        }
        return igual
    }

    @GetMapping("add/{mensaje}")
    fun add(@PathVariable mensaje: String): SinRespuesta{
        val nuevo = Mensajes(mensaje, respuestas, generarkey())
        val cif = cifrar(nuevo.mensaje, nuevo.key)
        nuevo.mensaje = cif
        mensajesRepository.save(nuevo)
        println("Hola nuevo:    $nuevo")
        return SinRespuesta(nuevo.id, nuevo.mensaje)
    }

    @GetMapping("cifrar/{id}")
    fun cifrar(@PathVariable id: Int): Mensajes {
        val cif = mensajesRepository.getById(id)
        val vuelta = cifrar(cif.mensaje, cif.key)
        cif.mensaje = vuelta
        mensajesRepository.save(cif)
        return cif
    }

    @GetMapping("descifrar/{id}")
    fun descifrar(@PathVariable id: Int): Mensajes {
        val descif = mensajesRepository.getById(id)
        val vuelta = descifrar(descif.mensaje, descif.key)
        descif.mensaje = vuelta
        mensajesRepository.save(descif)
        return descif
    }

    //http://localhost:8083/show
    @GetMapping("show")
    fun show():MutableList<Mensajes>{
        return mensajesRepository.findAll()
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

    private fun cifrar(textoEnString : String, llaveEnString : String) : String {
        println("Voy a cifrar: $textoEnString")
        val cipher = Cipher.getInstance(type)
        cipher.init(Cipher.ENCRYPT_MODE, getKey(llaveEnString))
        val textCifrado = cipher.doFinal(textoEnString.toByteArray(Charsets.UTF_8))
        println("Texto cifrado $textCifrado")
        val textCifradoYEncodado = Base64.getUrlEncoder().encodeToString(textCifrado)
        println("Texto cifrado y encodado $textCifradoYEncodado")
        return textCifradoYEncodado
        //return textCifrado.toString()
    }

    private fun descifrar(textoCifradoYEncodado : String, llaveEnString : String) : String {
        println("Voy a descifrar $textoCifradoYEncodado")
        val cipher = Cipher.getInstance(type)
        cipher.init(Cipher.DECRYPT_MODE, getKey(llaveEnString))
        val textCifradoYDencodado = Base64.getUrlDecoder().decode(textoCifradoYEncodado)
        println("Texto cifrado $textCifradoYDencodado")
        val textDescifradoYDesencodado = String(cipher.doFinal(textCifradoYDencodado))
        println("Texto cifrado y desencodado $textDescifradoYDesencodado")
        return textDescifradoYDesencodado
    }

    private fun getKey(llaveEnString : String): SecretKeySpec {
        var llaveUtf8 = llaveEnString.toByteArray(Charsets.UTF_8)
        val sha = MessageDigest.getInstance("SHA-1")
        llaveUtf8 = sha.digest(llaveUtf8)
        llaveUtf8 = llaveUtf8.copyOf(16)
        return SecretKeySpec(llaveUtf8, "AES")
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
