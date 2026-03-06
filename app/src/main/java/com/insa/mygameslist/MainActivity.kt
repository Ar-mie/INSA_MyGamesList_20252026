package com.insa.mygameslist

import Etats_Navigation.Game_Info
import Etats_Navigation.Home
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fitInside
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import coil3.compose.AsyncImage
import com.insa.mygameslist.data.GameComplet
import com.insa.mygameslist.data.IGDB
import com.insa.mygameslist.ui.theme.MyGamesListTheme


//@Preview(showBackground = true)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AfficherListeJeux(igdb: IGDB, backStack: MutableList<Any>){
    Scaffold(topBar = {
        TopAppBar(
            colors = topAppBarColors(
                containerColor = Color.Cyan,
                titleContentColor = Color.Black,
            ),
            title = { Text("My Games List") })
    },
        contentWindowInsets = WindowInsets.systemBars,
        modifier = Modifier.fillMaxSize()) { innerPadding ->
            LazyColumn(modifier = Modifier.padding(innerPadding)) {
                items(igdb.games) { game ->
                    AfficheDonneesJeu(igdb.gamesMapComplet[game.id],backStack)

                }
            }
    }

}

@Composable
fun AfficheDonneesJeu(game: GameComplet?, backStack: MutableList<Any>){
    Row(horizontalArrangement = Arrangement.spacedBy(4.dp), modifier = Modifier.clickable(onClick = { game?.let { backStack.add(Game_Info(it.id)) } })) {
        AsyncImage(
            model = "https:${game?.cover?.url}",
            contentDescription = null
        )
        Column(){
                game?.name?.let { Text(it) }
                Text("Genres :")
                Row(){
                    if (game != null) {
                        Text(game.genres.joinToString(", ") { it.name }, overflow = TextOverflow.Ellipsis, maxLines = 1)
                    }

                }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AfficherDetailsJeu(igdb: IGDB, backStack: MutableList<Any>, id:Long){
    Scaffold(topBar = {
        TopAppBar(
            colors = topAppBarColors(
                containerColor = Color.Cyan,
                titleContentColor = Color.Black,
            ),
            title = {
                IGDB.gamesMapComplet[id]?.let { it1 -> Text(it1.name) }
            },
            navigationIcon = {
                IconButton(onClick = { backStack.removeLastOrNull() }) {
                    Icon(
                        painter = painterResource(R.drawable.baseline_arrow_back_24),
                        contentDescription = ""
                    )
                }
                IconButton(onClick = {}){
                    Icon(
                        painter = painterResource(R.drawable.search_icon),
                        contentDescription = ""
                    )
                }
            }
        )
    },
        contentWindowInsets = WindowInsets.systemBars,
        modifier = Modifier.fillMaxSize()) { innerPadding ->
            Column(modifier = Modifier.padding(innerPadding), Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally){
                IGDB.gamesMapComplet[id]?.let { it1 ->
                    Text(it1.name,
                        modifier = Modifier.paddingFromBaseline(top = 30.dp),
                        fontWeight = FontWeight.Bold,
                        textDecoration = TextDecoration.Underline,
                        fontSize = 24.sp
                    )
                }
                val currentgame: GameComplet? = IGDB.gamesMapComplet[id]

                AsyncImage(
                    model = "https:${currentgame?.cover?.url}",
                    contentDescription = null,
                    modifier = Modifier.paddingFromBaseline(top = 30.dp)
                )


                if (currentgame != null) {
                    Text(currentgame.genres.joinToString(", ") { it.name }, overflow = TextOverflow.Ellipsis, maxLines = 1,
                        fontStyle = FontStyle.Italic,
                        fontSize = 12.sp,
                        modifier = Modifier.paddingFromBaseline(top = 20.dp)
                    )
                }

                LazyRow(modifier = Modifier.paddingFromBaseline(top = 30.dp)) {
                    if(currentgame != null){
                        items(currentgame.platforms){ platf ->
                            if(igdb.platformLogoMap[platf.platLogo]?.url != null){
                                AsyncImage(
                                    model = "https:${igdb.platformLogoMap[platf.platLogo]?.url}",
                                    contentDescription = null,
                                    modifier = Modifier.height(60.dp).width(80.dp).padding(horizontal = 10.dp),
                                    contentScale = ContentScale.FillBounds,

                                    error = painterResource(R.drawable.baseline_no_photography_24)
                                )

                            }else{
                                Icon(
                                    painter = painterResource(R.drawable.baseline_no_photography_24),
                                    contentDescription = "",
                                    modifier = Modifier.height(60.dp).width(80.dp)
                                )
                            }
                        }

                    }
                }


                Text("Description : ${currentgame?.summary}", modifier = Modifier.paddingFromBaseline(top = 40.dp).padding(15.dp))


            }
//        Text(
//            "ID du jeu : ${id}",
//            modifier = Modifier.padding(innerPadding)
//        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        IGDB.load(this)


        enableEdgeToEdge()
        setContent {
            val backStack = remember{ mutableStateListOf<Any>(Home) }

            MyGamesListTheme {
                NavDisplay(
                    backStack = backStack,
                    onBack = { backStack.removeLastOrNull() },
                    entryProvider = { key ->
                        when(key){
                            is Home -> NavEntry(key){
                                AfficherListeJeux( IGDB,backStack)
                            }
                            is Game_Info -> NavEntry(key){
                                AfficherDetailsJeu(IGDB,backStack,key.id)
                            }
                            else -> NavEntry(Unit) { Text("Unknown route") }
                        }
                    }
                )

            }
        }
    }
}