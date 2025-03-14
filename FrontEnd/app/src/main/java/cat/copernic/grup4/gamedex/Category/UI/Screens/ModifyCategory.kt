package cat.copernic.grup4.gamedex.Categories.UI.Screens

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import cat.copernic.grup4.gamedex.Category.Data.CategoryRepository
import cat.copernic.grup4.gamedex.Category.Domain.CategoryCases
import cat.copernic.grup4.gamedex.Category.UI.ViewModel.CategoryViewModel
import cat.copernic.grup4.gamedex.Category.UI.ViewModel.CategoryViewModelFactory
import cat.copernic.grup4.gamedex.Core.Model.Category
import cat.copernic.grup4.gamedex.Core.ui.BottomSection
import cat.copernic.grup4.gamedex.Core.ui.header
import cat.copernic.grup4.gamedex.Core.ui.theme.GameDexTypography
import cat.copernic.grup4.gamedex.R
import cat.copernic.grup4.gamedex.Users.UI.Screens.InputFieldEdit
import cat.copernic.grup4.gamedex.Users.UI.ViewModel.UserViewModel
import coil.compose.AsyncImage

@Composable
fun ModifyCategoryScreen(navController: NavController, userViewModel: UserViewModel) {
    val categoryCases = CategoryCases(CategoryRepository())
    val categoryViewModel: CategoryViewModel =
        viewModel(factory = CategoryViewModelFactory(categoryCases))

    val context = LocalContext.current

    val categoryId = remember {
        navController.currentBackStackEntry?.arguments?.getString("categoryId")
    } ?: return

    val updateSuccess by categoryViewModel.categoryModified.collectAsState()

    var nameCategory by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var categoryPhoto by remember { mutableStateOf("") }
    var oldCategoryPhoto by remember { mutableStateOf("") }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    DisposableEffect(Unit) {
        onDispose {
            categoryViewModel._categoryModified.value = null
        }
    }

    LaunchedEffect(categoryId) {
        val category = categoryViewModel.getCategoryById(categoryId)
        category?.let {
            nameCategory = it.nameCategory
            description = it.description
            oldCategoryPhoto = it.categoryPhoto ?: ""
        }
    }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri -> selectedImageUri = uri }
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(R.color.background))
            .windowInsetsPadding(WindowInsets.systemBars),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        header(navController, userViewModel)

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 100.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = stringResource(R.string.modify_category),
                fontSize = 40.sp,
                color = Color.Black,
                style = GameDexTypography.bodyLarge
            )

            Spacer(modifier = Modifier.height(20.dp))

            Card(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth(),
                shape = MaterialTheme.shapes.medium,
                colors = CardDefaults.cardColors(containerColor = Color.White),
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = nameCategory,
                        fontSize = 40.sp,
                        color = Color.Black,
                        style = GameDexTypography.bodyLarge
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    InputFieldEdit(
                        label = stringResource(R.string.description),
                        value = description
                    ) { description = it }

                    Spacer(modifier = Modifier.height(8.dp))

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Row {
                            if (selectedImageUri == null && oldCategoryPhoto.isNotEmpty()) {
                                val imageBitmap = categoryViewModel.base64ToBitmap(oldCategoryPhoto)
                                imageBitmap?.let {
                                    Image(
                                        bitmap = it,
                                        contentDescription = stringResource(R.string.category_photo),
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier
                                            .size(120.dp)
                                            .clip(CircleShape)
                                    )
                                }
                            } else if (selectedImageUri != null) {
                                categoryPhoto =
                                    categoryViewModel.uriToBase64(context, selectedImageUri!!)
                                        .toString()
                                oldCategoryPhoto = categoryPhoto
                                AsyncImage(
                                    model = selectedImageUri,
                                    contentDescription = stringResource(R.string.category_photo),
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .size(120.dp)
                                        .clip(CircleShape)
                                )
                            } else {
                                Image(
                                    painter = painterResource(id = R.drawable.user),
                                    contentDescription = stringResource(R.string.category_photo),
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .size(120.dp)
                                        .clip(CircleShape)
                                )
                            }
                            Icon(
                                Icons.Default.Add,
                                contentDescription = stringResource(R.string.add_photo),
                                modifier = Modifier
                                    .clickable {
                                        imagePickerLauncher.launch(
                                            PickVisualMediaRequest(
                                                ActivityResultContracts.PickVisualMedia.ImageOnly
                                            )
                                        )
                                    }
                                    .padding(top = 40.dp)
                                    .background(
                                        colorResource(R.color.header),
                                        shape = RoundedCornerShape(50)
                                    )
                                    .clip(RoundedCornerShape(50))
                                    .size(40.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            val updatedCategory = Category(
                                nameCategory = nameCategory,
                                description = description,
                                categoryPhoto = oldCategoryPhoto
                            )
                                categoryViewModel.modifyCategory(updatedCategory)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF69B4)),
                    ) {
                        Text(
                            text = stringResource(id = R.string.confirm),
                            color = Color.White,
                            fontSize = 16.sp,
                        )
                    }
                }
            }
        }
    }

    LaunchedEffect(updateSuccess) {
        updateSuccess?.let { success ->
            if (success) {
                Toast.makeText(
                    context,
                    context.getString(R.string.category_updated),
                    Toast.LENGTH_LONG
                ).show()
                navController.popBackStack()
            } else {
                Toast.makeText(
                    context,
                    context.getString(R.string.error_updating_category),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    BottomSection(navController, userViewModel, 0)
}

@Composable
fun InputFieldEdit(
    label: String,
    value: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    onValueChange: (String) -> Unit
) {
    Column {
        Text(text = label, color = Color.Black, fontSize = 14.sp)
        TextField(
            value = value,
            placeholder = { Text(text = stringResource(R.string.leave_empty)) },
            onValueChange = onValueChange,
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = keyboardType),
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.LightGray),
        )
        Spacer(modifier = Modifier.height(6.dp))
    }
}
