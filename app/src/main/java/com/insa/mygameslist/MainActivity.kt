package com.insa.mygameslist

import Etats_Navigation.Home
import Etats_Navigation.Game_Info
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import coil3.ImageLoader
import coil3.compose.AsyncImage
import coil3.compose.setSingletonImageLoaderFactory
import coil3.request.crossfade
import com.insa.mygameslist.data.Game
import com.insa.mygameslist.data.IGDB
import com.insa.mygameslist.ui.theme.MyGamesListTheme
import kotlinx.datetime.format.Padding

//@Preview(showBackground = true)
@Composable
fun AfficherJeux(innerPadding: PaddingValues, igdb: IGDB){

    LazyColumn(modifier = Modifier.padding(innerPadding)) {
        items(igdb.games) { game ->
            AfficheDonneesJeu(game)

        }
    }
}

@Composable
fun AfficheDonneesJeu(game: Game){
    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        val coverId: Long = game.cover
        AsyncImage(
            model = "https:${IGDB.coversMap.get(coverId)}",
            contentDescription = null
        )
        Column(){
                Text(game.name)
                Text("Genres :")
                Row(){
                    Text(game.genres.filter { IGDB.genresMap.containsKey(it) }
                        .joinToString(", ") { IGDB.genresMap.get(it)!! }, overflow = TextOverflow.Ellipsis, maxLines = 1)

                }
            }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        IGDB.load(this)

        enableEdgeToEdge()
        setContent {
            val backStack = remember{ mutableListOf<Any>(Home) }

            MyGamesListTheme {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            colors = topAppBarColors(
                                containerColor = Color.Cyan,
                                titleContentColor = Color.Black,
                            ),
                            title = { Text("My Games List") })
                    },
                    contentWindowInsets = WindowInsets.systemBars,
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    //Text("À remplir", modifier = Modifier.padding(innerPadding))
                    NavDisplay(
                        backStack = backStack,
                        onBack = { backStack.removeLastOrNull() },
                        entryProvider = { key ->
                            when(key){
                                is Home -> NavEntry(key){
                                    AfficherJeux(innerPadding, IGDB)
                                }
                                is Game_Info -> NavEntry(key){

                                }
                                else -> NavEntry(Unit) { Text("Unknown route") }
                            }
                        }
                    )

                }
            }
        }
    }
}