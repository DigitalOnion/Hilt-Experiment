package com.outerspace.hilt_exp

import android.app.Person
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.lifecycleScope
import com.outerspace.hilt_exp.ui.theme.HiltExperimentTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var peopleVM: PeopleViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        peopleVM = ViewModelProvider(this as ViewModelStoreOwner)[PeopleViewModel::class.java]

        suspend fun getPeople(): List<PersonEntity> {
            val n = peopleVM.countPeople()
            if (n == 0) {
                for(person in peopleVM.testPeopleList()) {
                    peopleVM.addPerson(person)
                }
            }

            return lifecycleScope.async(Dispatchers.IO) {
                peopleVM.getAll()
            }.await()
        }

        setContent {
            HiltExperimentTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize().padding(32.dp),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    var listState: MutableState<List<PersonEntity>> = remember {
                        mutableStateOf(listOf())
                    }
                    LaunchedEffect(key1 = null) {
                        val list = getPeople()
                        listState.value = list
                    }
                    peopleList(listState, modifier = Modifier)
                }
            }
        }
    }
}

@Composable
fun peopleList(listState: MutableState<List<PersonEntity>>, modifier: Modifier = Modifier) {
    LazyColumn {
        listState.value.forEach { person ->
            item {
                Text(text = person.fullName(), modifier = modifier, fontSize = 24.sp)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun greetingPreview() {
    val listState: MutableState<List<PersonEntity>> = remember {
        mutableStateOf(
            listOf(PersonEntity(null, "Bimbo", "Virueña", GenderEnum.FURRY))
        )
    }

    HiltExperimentTheme {
        peopleList(listState)
    }
}


