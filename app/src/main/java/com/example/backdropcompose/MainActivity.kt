package com.example.backdropcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.backdropcompose.ui.theme.BackDropComposeExampleTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BackDropComposeExampleTheme {

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    HomeScreen()
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
@OptIn(ExperimentalMaterialApi::class)
fun HomeScreen() {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val selection = remember { mutableStateOf(0) }
    val scaffoldState = rememberBackdropScaffoldState(BackdropValue.Concealed)
    LaunchedEffect(scaffoldState) {
        scaffoldState.conceal()
    }

    BackdropScaffold(
        scaffoldState = scaffoldState,
        appBar = {
            TopAppBar(
                title = { Text(context.getString(R.string.app_name)) },
                navigationIcon = {


                     if (scaffoldState.isConcealed) {
                        IconButton(onClick = {
                            scope.launch { scaffoldState.reveal() }
                        }) {
                            Icon(
                                Icons.Filled.Menu,
                                contentDescription = "menu"
                            )
                        }
                    } else {
                        IconButton(onClick = { scope.launch { scaffoldState.conceal() } }) {
                            Icon(
                                Icons.Filled.Close,
                                contentDescription = "Close"
                            )
                        }
                    }
                },
                actions = {
                    var clickCount by remember { mutableStateOf(0) }
                    IconButton(
                        onClick = {
                            scope.launch {
                                scaffoldState.snackbarHostState
                                    .showSnackbar(
                                        context.getString(
                                            R.string.button_count,
                                            ++clickCount
                                        )
                                    )
                            }
                        }
                    ) {
                        Icon(
                            Icons.Filled.Favorite,
                            contentDescription = "Favorite"
                        )
                    }
                },
                elevation = 0.dp,
                backgroundColor = Color.Transparent
            )
        },
        backLayerContent = {
            LazyColumn {
                items(if (selection.value > 0) 30 else 30) {
                    ListItem(
                        Modifier.clickable {
                            selection.value = it
                            scope.launch { scaffoldState.conceal() }
                        },
                        text = { Text(context.getString(R.string.select_args, it)) }
                    )
                }
            }
        },
        frontLayerContent = {
            LazyColumn {
                item {
                    Text(
                        context.getString(R.string.selection_args, selection.value),
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }
                items(50) {
                    ListItem(
                        text = { Text(context.getString(R.string.item, it)) },
                        icon = {
                            Icon(
                                Icons.Filled.Favorite,
                                contentDescription = "Favorite"
                            )
                        }
                    )
                }
            }
        },
        // Defaults to BackdropScaffoldDefaults.PeekHeight
        peekHeight = 60.dp,
        // Defaults to BackdropScaffoldDefaults.HeaderHeight
        headerHeight = 60.dp,
        // Defaults to true
        gesturesEnabled = true
    ) {

    }
}

