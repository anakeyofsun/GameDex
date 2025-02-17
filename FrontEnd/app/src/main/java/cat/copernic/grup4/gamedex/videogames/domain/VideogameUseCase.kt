package cat.copernic.grup4.gamedex.videogames.domain

import cat.copernic.grup4.gamedex.Core.Model.Videogame
import cat.copernic.grup4.gamedex.videogames.data.VideogameRepository

class VideogameUseCase (private val repository: VideogameRepository) {
    suspend fun createVideogame(videogame: Videogame) = repository.createVideogame(videogame)
}