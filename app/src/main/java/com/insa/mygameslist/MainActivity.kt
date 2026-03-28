package com.insa.mygameslist

import Etats_Navigation.Game_Info
import Etats_Navigation.Home
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.animateBounds
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFrom
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontLoadingStrategy
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import coil3.compose.AsyncImage
import com.insa.mygameslist.data.GameComplet
import com.insa.mygameslist.data.IGDB
import com.insa.mygameslist.ui.theme.MyGamesListTheme
import com.insa.mygameslist.ui.theme.Purple80
import com.insa.mygameslist.ui.theme.PurpleGrey80
import kotlinx.coroutines.selects.select

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AfficherListeDesJeux(igdb: IGDB, backStack: MutableList<Any>, jeuxAafficher: MutableState<ArrayList<Long>>, favori_states: MutableMap<Long, MutableState<Boolean>>){
    var champTexte by rememberSaveable { mutableStateOf("")}
    //val resultats = ArrayList<Long>()
    var rechercheEnCours: Boolean by rememberSaveable { mutableStateOf(false) }
    var afficherFavoris: MutableState<Boolean> = rememberSaveable { mutableStateOf(false) }
    Scaffold(topBar = {
        TopAppBar(
            colors = topAppBarColors(
                containerColor = Purple80,
                titleContentColor = Color.Black,
            ),
            title = { if(!rechercheEnCours){
                Text("My Games List")
            }else{
                Recherche(
                    query = champTexte,
                    onQueryChange = { input -> champTexte = input
                        rechercherJeu(igdb, champTexte, jeuxAafficher)
                    },
                    onSearch = { input -> champTexte = input
                        rechercherJeu(igdb,champTexte, jeuxAafficher)
                    },
                    modifier = Modifier)
            } },
            actions ={
                IconButton(onClick = { selectionnerFavoris(IGDB, jeuxAafficher, afficherFavoris)} ){
                    Icon(
                        painter = painterResource(R.drawable.etoile_pleine),
                        contentDescription = "",
                        modifier = Modifier.width(20.dp)
                    )
                }
                IconButton(onClick = { rechercheEnCours=true }){
                    Icon(
                        painter = painterResource(R.drawable.search_icon),
                        modifier = Modifier.offset(y = 4.dp),
                        contentDescription = ""
                    )
                }
            }
        )
    },
        contentWindowInsets = WindowInsets.systemBars,
        modifier = Modifier.fillMaxSize()) { innerPadding ->
        LazyColumn(modifier = Modifier.padding(innerPadding)) {
            if(jeuxAafficher.value.isNotEmpty()) {
                items(jeuxAafficher.value) { idGame ->
                    AfficherPreviewDetails(igdb.gamesMapComplet[idGame], backStack, favori_states)

                }
            }
        }
    }

}

@Composable
fun AfficherPreviewDetails(game: GameComplet?, backStack: MutableList<Any>, favoriStates: MutableMap<Long, MutableState<Boolean>>){
    Row(horizontalArrangement = Arrangement.spacedBy(5.dp), modifier = Modifier.clickable(onClick = { game?.let { backStack.add(Game_Info(it.id)) } }).absolutePadding(top = 15.dp, right = 10.dp) ){
        Box(
            modifier = Modifier.fillMaxSize()
                                .padding(start = 7.dp)
                                .clip(RoundedCornerShape(topStart = 20.dp, bottomStart = 20.dp, topEnd = 20.dp, bottomEnd = 20.dp))
                                .background(PurpleGrey80)
        ) {
            IconButton(onClick = { miseEnFavori(game, favoriStates) }, modifier = Modifier.offset(x=345.dp, y = 4.dp)) {
                if (game != null) {
                    if (favoriStates[game.id]?.value == true) {
                        Icon(
                            painter = painterResource(R.drawable.etoile_pleine),
                            contentDescription = "",
                            modifier = Modifier.width(20.dp)
                        )
                    } else {
                        Icon(
                            painter = painterResource(R.drawable.etoile_vide),
                            contentDescription = "",
                            modifier = Modifier.width(20.dp)
                        )
                    }
                }
            }
            AsyncImage(
                model = "https:${game?.cover?.url}",
                modifier = Modifier.size(100.dp, 140.dp)
                                    .padding(start = 14.dp),
                contentDescription = null,
            )
            Column(modifier = Modifier.padding(start = 111.dp)) {
                game?.name?.let {
                    Text(
                        it,
                        modifier = Modifier.padding(top = 17.dp, bottom = 5.dp, end = 30.dp),
                        fontWeight = FontWeight.Bold,
                        textDecoration = TextDecoration.Underline,
                        fontSize = 20.sp
                    )
                }
                Text("Genres :")
                Row(modifier = Modifier.padding(end = 5.dp)) {
                    if (game != null) {
                        Text(
                            game.genres.joinToString(", ") { it.name },
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1
                        )
                    }

                }

            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AfficherPagedeDetails(igdb: IGDB, backStack: MutableList<Any>, id: Long, favoriStates: MutableMap<Long, MutableState<Boolean>>) { //, viewModelJeu : ViewModelJeu = viewModel() { }
    Scaffold(
        topBar = {
            TopAppBar(
                colors = topAppBarColors(
                    containerColor = Purple80,
                    titleContentColor = Color.Black,
                ),
                title = { //Viewmodel.gameTitle
                    IGDB.gamesMapComplet[id]?.let { it1 -> Text(it1.name) }
                },
                navigationIcon = {
                    IconButton(onClick = { backStack.removeLastOrNull() }) {
                        Icon(
                            painter = painterResource(R.drawable.baseline_arrow_back_24),
                            contentDescription = ""
                        )
                    }
                },
                actions = {
                    if (favoriStates[id]?.value == true) {
                        IconButton(onClick = {
                            miseEnFavori(
                                IGDB.gamesMapComplet[id],
                                favoriStates
                            )
                        }) {
                            Icon(
                                painter = painterResource(R.drawable.etoile_pleine),
                                contentDescription = "",
                                modifier = Modifier.width(20.dp)
                            )
                        }
                    } else {
                        IconButton(onClick = {
                            miseEnFavori(
                                IGDB.gamesMapComplet[id],
                                favoriStates
                            )
                        }) {
                            Icon(
                                painter = painterResource(R.drawable.etoile_vide),
                                contentDescription = "",
                                modifier = Modifier.width(20.dp)

                            )
                        }

                    }
                }
            )
        },
        contentWindowInsets = WindowInsets.systemBars,
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding),
            Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            IGDB.gamesMapComplet[id]?.let { it1 ->
                Text(
                    it1.name,
                    modifier = Modifier.paddingFromBaseline(top = 35.dp),
                    fontWeight = FontWeight.Bold,
                    textDecoration = TextDecoration.Underline,
                    fontSize = 24.sp
                )
            }
            val currentGame: GameComplet? = IGDB.gamesMapComplet[id]

            AsyncImage(
                model = "https:${currentGame?.cover?.url}",
                contentDescription = null,
                modifier = Modifier.paddingFromBaseline(top = 25.dp)
            )


            if (currentGame != null) {
                Text(
                    currentGame.genres.joinToString(", ") { it.name },
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    fontStyle = FontStyle.Italic,
                    fontSize = 12.sp,
                    modifier = Modifier.paddingFromBaseline(top = 20.dp)
                )
            }

            LazyRow(modifier = Modifier.paddingFromBaseline(top = 30.dp)) {
                if (currentGame != null) {
                    items(currentGame.platforms) { platf ->
                        if (igdb.platformLogoMap[platf.platLogo]?.url != null) {
                            AsyncImage(
                                model = "https:${igdb.platformLogoMap[platf.platLogo]?.url}",
                                contentDescription = null,
                                modifier = Modifier.height(60.dp).width(80.dp)
                                    .padding(horizontal = 10.dp),
                                contentScale = ContentScale.FillBounds,

                                error = painterResource(R.drawable.baseline_no_photography_24)
                            )

                        } else {
                            Icon(
                                painter = painterResource(R.drawable.baseline_no_photography_24),
                                contentDescription = "",
                                modifier = Modifier.height(60.dp).width(80.dp)
                            )
                        }
                    }

                }
            }
            Text(
                "Description : ${currentGame?.summary}",
                modifier = Modifier.paddingFromBaseline(top = 40.dp).padding(15.dp)
            )
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
            val backStack = remember{ mutableStateListOf<Any>(Home) }
            val listeTotale = ArrayList<Long>()
            val favoriStatesMap = mutableMapOf<Long, MutableState<Boolean>>()
            for(game in IGDB.gamesMapComplet.values){
                listeTotale.add(game.id)
            }
            for(game in IGDB.gamesMapComplet.values){
                val favState = rememberSaveable { mutableStateOf(game.favori) }
                favoriStatesMap[game.id] = favState
            }
            val jeuAafficher = rememberSaveable { mutableStateOf(listeTotale) }

            MyGamesListTheme {
                NavDisplay(
                    backStack = backStack,
                    onBack = { backStack.removeLastOrNull() },
                    entryProvider = { key ->
                        when(key){
                            is Home -> NavEntry(key){
                                AfficherListeDesJeux( IGDB, backStack, jeuAafficher, favoriStatesMap)
                            }
                            is Game_Info -> NavEntry(key){
                                AfficherPagedeDetails(IGDB,backStack,key.id,favoriStatesMap)
                            }
                            else -> NavEntry(Unit) { Text("Unknown route") }
                        }
                    }
                )

            }
        }
    }
}