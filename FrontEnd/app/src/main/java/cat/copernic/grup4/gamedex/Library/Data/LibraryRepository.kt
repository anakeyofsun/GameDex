package cat.copernic.grup4.gamedex.Library.Data

import cat.copernic.grup4.gamedex.Core.Model.Library
import retrofit2.Response

/**
 * Repositori per gestionar les operacions de la API REST de biblioteques de videojocs.
 */
class LibraryRepository {

    /**
     * Afegeix un joc a la biblioteca d'un usuari.
     *
     * @param library La biblioteca que conté el joc i l'usuari.
     * @return La resposta de la API amb la biblioteca actualitzada.
     */
    suspend fun addGameToLibrary(library: Library): Response<Library> {
        return RetrofitInstance.api.addGameToLibrary(library)
    }

    /**
     * Obté la biblioteca d'un usuari pel seu nom d'usuari.
     *
     * @param username El nom d'usuari.
     * @return La resposta de la API amb la llista de biblioteques de l'usuari.
     */
    suspend fun getLibrary(username: String): Response<List<Library>> {
        return RetrofitInstance.api.getLibrary(username) // Aquesta funció crida a l'API o base de dades
    }

    /**
     * Obté els comentaris per a un videojoc específic.
     *
     * @param gameId L'ID del videojoc.
     * @return La resposta de la API amb la llista de comentaris.
     */
    suspend fun getCommentsForVideogame(gameId: String): Response<List<Library>> {
        val response = RetrofitInstance.api.getCommentsForVideogame(gameId)
        return response
    }

    /**
     * Elimina un joc de la biblioteca d'un usuari.
     *
     * @param gameId   L'ID del joc.
     * @param username El nom d'usuari.
     * @return La resposta de la API.
     */
    suspend fun deleteVideogameFromLibrary(gameId: String, username: String): Response<Void> {
        return RetrofitInstance.api.deleteVideogameFromLibrary(gameId, username)
    }

    /**
     * Obté la puntuació mitjana d'un videojoc.
     *
     * @param gameId L'ID del videojoc.
     * @return La resposta de la API amb la puntuació mitjana.
     */
    suspend fun getAverageRating(gameId: String): Response<Double> {
        return RetrofitInstance.api.getAverageRating(gameId)
    }

    /**
     * Obté una entrada de la biblioteca d'un usuari per a un videojoc específic fent una crida a l'API.
     *
     * @param gameId Identificador del videojoc.
     * @param username Nom d'usuari del propietari de la biblioteca.
     * @return Una resposta amb l'objecte Library si existeix.
     */
    suspend fun getLibraryEntry(gameId: String, username: String): Response<Library> {
        return RetrofitInstance.api.getLibraryEntry(gameId, username)
    }

    /**
     * Actualitza la informació d'un videojoc dins la biblioteca d'un usuari mitjançant una crida a l'API.
     *
     * @param library L'objecte Library amb la informació actualitzada.
     * @return Una resposta amb l'objecte Library actualitzat.
     */
    suspend fun updateGameInLibrary(library: Library): Response<Library> {
        return RetrofitInstance.api.updateGameInLibrary(library)
    }
}