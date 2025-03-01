package cat.copernic.grup4.gamedex.Library.Data

import cat.copernic.grup4.gamedex.Core.Model.Library
import cat.copernic.grup4.gamedex.Core.Model.Videogame
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface LibraryApiRest {
    @POST("library/add")
    suspend fun addGameToLibrary(@Body library: Library): Response<Library>

    @GET("library/get")
    suspend fun getLibrary(@Query("username") username: String): Response<List<Library>>

    @GET("library/comments/{gameId}")
    suspend fun getCommentsForVideogame(@Path("gameId") gameId: String): Response<List<Library>>

    @DELETE("library/delete/{gameId}/{username}")
    suspend fun deleteVideogameFromLibrary(@Path("gameId") gameId: String, @Path("username") username: String): Response<Void>

    @GET("library/averagerating/{gameId}")
    suspend fun getAverageRating(@Path("gameId") gameId: String): Response<Double>
}