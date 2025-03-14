package cat.copernic.grup4.gamedex.videogames.ui.screens


import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import cat.copernic.grup4.gamedex.Core.ui.header
import androidx.lifecycle.viewmodel.compose.viewModel
import cat.copernic.grup4.gamedex.Core.Model.Category
import cat.copernic.grup4.gamedex.Core.Model.Videogame
import cat.copernic.grup4.gamedex.Core.ui.BottomSection
import cat.copernic.grup4.gamedex.Core.ui.theme.GameDexTypography
import cat.copernic.grup4.gamedex.R
import cat.copernic.grup4.gamedex.Users.Data.UserRepository
import cat.copernic.grup4.gamedex.Users.Domain.UseCases
import cat.copernic.grup4.gamedex.Users.UI.Screens.InputField
import cat.copernic.grup4.gamedex.Users.UI.ViewModel.UserViewModel
import cat.copernic.grup4.gamedex.Users.UI.ViewModel.UserViewModelFactory
import cat.copernic.grup4.gamedex.videogames.data.VideogameRepository
import cat.copernic.grup4.gamedex.videogames.domain.VideogameUseCase
import cat.copernic.grup4.gamedex.videogames.ui.viewmodel.GameViewModel
import cat.copernic.grup4.gamedex.videogames.ui.viewmodel.GameViewModelFactory
import coil.compose.AsyncImage

// Pantalla de modificar jocs
@Composable
fun ModifyGamesScreen(navController : NavController, userViewModel: UserViewModel) {
    val videogameUseCase = VideogameUseCase(VideogameRepository())
    val gameViewModel: GameViewModel = viewModel(factory = GameViewModelFactory(videogameUseCase))

    val context = LocalContext.current
    val gameId = remember {
        // Obté la ID del joc dels paràmetres de navegació
        navController.currentBackStackEntry?.arguments?.getString("gameId")
    } ?: return // Si es null, surt

    val categories by gameViewModel.categories.collectAsState()
    val updateSuccess by gameViewModel.videogameUpdated.collectAsState()

    var nameGame by remember { mutableStateOf("") }
    var releaseYear by remember { mutableStateOf("") }
    var ageRecommendation by remember { mutableStateOf("") }
    var developer by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf<Category?>(null) }
    var descriptionGame by remember { mutableStateOf("") }
    var gamePhoto by remember { mutableStateOf("") }
    var oldGamePhoto by remember { mutableStateOf("") }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    DisposableEffect(Unit) {
        onDispose {
            gameViewModel._videogameUpdated.value = null
        }
    }
    LaunchedEffect(Unit) {
        gameViewModel.getAllCategories()
    }
    LaunchedEffect(gameId, categories) {
        val game = gameViewModel.videogamesById(gameId)
        game?.let {
            nameGame = it.nameGame
            releaseYear = it.releaseYear
            ageRecommendation = it.ageRecommendation
            developer = it.developer
            selectedCategory = categories.find { category -> category.nameCategory == it.category }
            descriptionGame = it.descriptionGame
            oldGamePhoto = it.gamePhoto ?: ""
        }
    }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri -> selectedImageUri = uri }
    )

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(colorResource(R.color.background))
                .windowInsetsPadding(WindowInsets.systemBars),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            header(navController, userViewModel)
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 80.dp)
                    .background(colorResource(R.color.background))
                    .windowInsetsPadding(WindowInsets.systemBars)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = stringResource(R.string.modify_game),
                    fontSize = 50.sp,
                    color = Color.Black,
                    style = GameDexTypography.bodyLarge
                )
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    modifier = Modifier.padding(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = nameGame,
                            fontSize = 40.sp,
                            color = Color.Black,
                            style = GameDexTypography.bodyLarge
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        InputField(
                            label = stringResource(R.string.game_name),
                            value = nameGame
                        ) { nameGame = it }
                        InputField(
                            label = stringResource(R.string.release_year),
                            value = releaseYear,
                            keyboardType = KeyboardType.Number
                        ) { releaseYear = it }
                        InputField(
                            label = stringResource(R.string.age_recommendation),
                            value = ageRecommendation
                        ) { ageRecommendation = it }
                        InputField(
                            label = stringResource(R.string.developer),
                            value = developer
                        ) { developer = it }
                        CategoryDropdown(
                            categories = categories,
                            selectedCategory = selectedCategory,
                            onCategorySelected = {
                                selectedCategory = it
                                Log.d(
                                    "ModifyGamesScreen",
                                    "Categoría seleccionada: ${it.nameCategory}"
                                )
                            }
                        )
                        InputField(
                            label = stringResource(R.string.description),
                            value = descriptionGame
                        ) { descriptionGame = it }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = stringResource(R.string.cover) + ":",
                            color = Color.Black,
                            //style = GameDexTypography.bodyLarge,
                            fontSize = 18.sp,
                            modifier = Modifier.padding(end = 100.dp, bottom = 4.dp)
                        )

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier.padding(start = 50.dp)
                        ) {
                            Row(horizontalArrangement = Arrangement.Center) {
                                Spacer(modifier = Modifier.height(4.dp))
                                if (selectedImageUri == null && oldGamePhoto.isNotEmpty()) {
                                    val imageBitmap = gameViewModel.base64ToBitmap(oldGamePhoto)
                                    imageBitmap?.let {
                                        Image(
                                            bitmap = it,
                                            contentDescription = stringResource(R.string.category_photo),
                                            contentScale = ContentScale.Crop,
                                            modifier = Modifier.size(height = 170.dp, width = 110.dp)
                                        )
                                    }
                                } else if (selectedImageUri != null) {
                                    gamePhoto =
                                        gameViewModel.uriToBase64(context, selectedImageUri!!)
                                            .toString()
                                    oldGamePhoto = gamePhoto
                                    AsyncImage(
                                        model = selectedImageUri,
                                        contentDescription = stringResource(R.string.gamePicture),
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.size(height = 170.dp, width = 110.dp)
                                    )
                                } else {
                                    Image(
                                        painter = painterResource(id = R.drawable.defaultimage),
                                        contentDescription = stringResource(R.string.gamePicture),
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.size(height = 170.dp, width = 110.dp)
                                    )
                                }
                                Icon(
                                    Icons.Default.Add,
                                    contentDescription = stringResource(R.string.add_cover),
                                    modifier = Modifier
                                        .clickable {
                                            imagePickerLauncher
                                                .launch(
                                                    PickVisualMediaRequest(
                                                        ActivityResultContracts
                                                            .PickVisualMedia.ImageOnly
                                                    )
                                                )
                                        }
                                        .padding(start = 10.dp, top = 100.dp)
                                        .background(
                                            colorResource(R.color.header),
                                            shape = RoundedCornerShape(50)
                                        )
                                        .clip(RoundedCornerShape(50))
                                        .size(40.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(20.dp))
                        }
                        Button(
                            onClick = {
                                if (selectedCategory == null) {
                                    Toast.makeText(
                                        context,
                                        context.getString(R.string.please_select_a_category),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } else {
                                    val updatedGame = Videogame(
                                        gameId = gameId,
                                        nameGame = nameGame,
                                        releaseYear = releaseYear,
                                        ageRecommendation = ageRecommendation,
                                        developer = developer,
                                        category = selectedCategory!!.nameCategory,
                                        descriptionGame = descriptionGame,
                                        gamePhoto = oldGamePhoto
                                    )
                                    gameViewModel.updateVideogame(updatedGame)
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF69B4)),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                                .clip(RoundedCornerShape(16.dp))
                        ) {
                            Text(
                                text = stringResource(R.string.confirm),
                                color = Color.White,
                                style = GameDexTypography.bodyLarge,
                                fontSize = 22.sp
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                }
            }
            LaunchedEffect(updateSuccess) {
                updateSuccess?.let { success ->
                    if (success) {
                        Toast.makeText(
                            context,
                            context.getString(R.string.game_updated),
                            Toast.LENGTH_LONG
                        ).show()
                        navController.popBackStack()
                    } else {
                        Toast.makeText(
                            context,
                            context.getString(R.string.error_updating_game),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }
        BottomSection(navController, userViewModel, 1)
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewModifyGamesScreen() {
    val fakeNavController = rememberNavController()
    val useCases = UseCases(UserRepository())
    val userViewModel: UserViewModel = viewModel(factory = UserViewModelFactory(useCases))
    ModifyGamesScreen(navController = fakeNavController, userViewModel)
}