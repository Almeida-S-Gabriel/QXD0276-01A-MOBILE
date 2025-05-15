package com.example.zooapp.models
import com.example.zooapp.R
data class Animal (
    val id: Int,
    val name: String,
    val species: String,
    val imageRes: Int,
    val description: String,
    val curiosities: String,
    var isFavorite: Boolean = false
)

val animalList = listOf(
    Animal(
        id = 1,
        name = "Dog",
        species = "Canis lupus familiaris",
        imageRes = R.drawable.dog,
        description = "O cão é um dos animais mais antigos domesticados pelo homem.",
        curiosities = "Os cães têm um olfato cerca de 40 vezes mais potente que o dos humanos."
    ),
    Animal(
        id = 2,
        name = "Cat",
        species = "Felis catus",
        imageRes = R.drawable.cat,
        description = "O gato doméstico é conhecido por sua agilidade e independência.",
        curiosities = "Gatos passam cerca de 70% do dia dormindo."
    ),

    Animal(
        id = 3,
        name = "Elephant",
        species = "Elephas maximus",
        imageRes = R.drawable.elephant,
        description = "O Elefante Asiatic é conhecido por sua Força e inteligência.",
        curiosities = "De todos os animais terrestres, os elefantes são os que têm o cérebro maior, sendo três a quatro vezes maior do que o de um humano;"
    ),

    Animal(
        id = 4,
        name = "Bear",
        species = "Urso",
        imageRes = R.drawable.urso,
        description = "O Urso é conhecido por sua Força.",
        curiosities = "Os ursos vivem em tocas ou cavernas. Eles hibernam seis meses, sem se alimentar. Eles consomem toda a energia possível antes da hibernação. Sobrevivem da gordura acumulada"
    ),
)