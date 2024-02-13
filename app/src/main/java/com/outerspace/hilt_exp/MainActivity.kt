package com.outerspace.hilt_exp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.lifecycleScope
import com.outerspace.hilt_exp.ui.theme.HiltExperimentTheme
import com.outerspace.hiltexp.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

const val LIST_OF_PERSONS = "list-of-persons"

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var peopleVM: PeopleViewModel

    suspend fun getPeople(): List<PersonEntity> {
        return lifecycleScope.async(Dispatchers.IO) {
            peopleVM.getAll()
        }.await()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        peopleVM = ViewModelProvider(this as ViewModelStoreOwner)[PeopleViewModel::class.java]

        lifecycleScope.launch(Dispatchers.IO) {
            if (peopleVM.countPeople() == 0) {
                peopleVM.initializePeopleTable()
            }
        }

        val petSupply: MutableList<PersonEntity> = mutableListOf()
        petSupply.addAll(peopleVM.testPetList())

        setContent {
            HiltExperimentTheme {
                val modifier = Modifier
                val listState: MutableState<List<PersonEntity>> = remember { mutableStateOf(listOf()) }
                val refreshList: MutableState<Boolean> = remember { mutableStateOf(true) }
                val addPetsState: MutableState<Boolean> = remember { mutableStateOf(false)}

                Surface(
                    modifier = modifier.fillMaxSize().padding(32.dp),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    if (refreshList.value) {
                        LaunchedEffect(refreshList) {
                            val list = getPeople()
                            listState.value = list
                            refreshList.value = false
                        }
                    }

                    if (addPetsState.value) {
                        LaunchedEffect(addPetsState) {
                            if (petSupply.isNotEmpty()) {
                                val pet = petSupply.first()
                                petSupply.remove(pet)
                                peopleVM.addPerson(pet)
                            } else {
                                peopleVM.deleteGender(GenderEnum.FURRY)
                                petSupply.addAll(peopleVM.testPetList())
                            }
                            addPetsState.value = false
                            refreshList.value = true
                        }
                    }

                    Column(
                        modifier = modifier.fillMaxSize()
                    ) {
                        LazyColumn (
                            modifier = modifier.testTag(LIST_OF_PERSONS)
                        ) {
                            items(listState.value) { person ->
                                Text(text = person.fullName(), modifier = modifier, fontSize = 24.sp)
                            }
                        }
                        Spacer(modifier = modifier.weight(1.0F))
                        Button(
                            onClick =   {
                                addPetsState.value = true
                                },
                            modifier = modifier.align(Alignment.CenterHorizontally)
                        ) {
                            Text(stringResource(R.string.add_pets))
                        }
                    }
                }
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
        LazyColumn {
            listState.value.forEach { person ->
                item {
                    Text(text = person.fullName(), modifier = Modifier, fontSize = 24.sp)
                }
            }
        }
    }
}


