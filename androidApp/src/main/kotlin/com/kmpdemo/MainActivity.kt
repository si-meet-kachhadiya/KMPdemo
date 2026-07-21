package com.kmpdemo

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.kmpdemo.config.BuildConfig
import com.kmpdemo.mvi.listing.model.ListingItem
import com.kmpdemo.mvi.listing.presentation.ListingIntent
import com.kmpdemo.mvi.listing.presentation.ListingViewModel
import com.kmpdemo.mvi.listing.presentation.createListingViewModel
import com.kmpdemo.photo.PhotoDetailScreen
import com.kmpsdk.KmpSdk
import com.kmpsdk.presentation.state.DataState
import com.kmpsdk.presentation.state.toErrorMessage
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            KmpSdk.messageEventBus.events.collect { event ->
                Toast.makeText(this@MainActivity, event.message, Toast.LENGTH_SHORT).show()
            }
        }

        val viewModel = createListingViewModel(lifecycleScope)
        viewModel.dispatch(ListingIntent.Load)

        setContent {
            MaterialTheme {
                var selectedTitleAlias by remember { mutableStateOf<String?>(null) }

                if (selectedTitleAlias != null) {
                    PhotoDetailScreen(
                        titleAlias = selectedTitleAlias!!,
                        onBackClick = { selectedTitleAlias = null },
                    )
                } else {
                    MviDemoScreen(
                        viewModel = viewModel,
                        onItemClick = { item ->
                            val alias = item.titleAlias
                            if (alias.isNullOrBlank()) {
                                Toast.makeText(
                                    this@MainActivity,
                                    "No titleAlias for this item",
                                    Toast.LENGTH_SHORT,
                                ).show()
                            } else {
                                selectedTitleAlias = alias
                            }
                        },
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MviDemoScreen(
    viewModel: ListingViewModel,
    onItemClick: (ListingItem) -> Unit,
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("${BuildConfig.appName} · ${BuildConfig.buildVariant}") },
                actions = {
                    Button(onClick = { viewModel.dispatch(ListingIntent.Refresh) }) {
                        Text("Refresh")
                    }
                },
            )
        },
    ) { innerPadding ->
        ListingContent(
            listingsState = state.listings,
            padding = innerPadding,
            onItemClick = onItemClick,
        )
    }
}

@Composable
private fun ListingContent(
    listingsState: DataState<List<ListingItem>>,
    padding: PaddingValues,
    onItemClick: (ListingItem) -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding),
    ) {
        when (listingsState) {
            is DataState.Idle -> StatusMessage("Tap Refresh to load listings")
            is DataState.Loading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            is DataState.Success -> {
                if (listingsState.data.isEmpty()) {
                    StatusMessage("No listings returned for page 2")
                } else {
                    ListingList(items = listingsState.data, onItemClick = onItemClick)
                }
            }
            is DataState.Failure -> StatusMessage(listingsState.toErrorMessage())
            is DataState.NoNetwork -> StatusMessage("No internet connection")
        }
    }
}

@Composable
private fun ListingList(
    items: List<ListingItem>,
    onItemClick: (ListingItem) -> Unit,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(items, key = { it.id }) { item ->
            ListingCard(item = item, onClick = { onItemClick(item) })
        }
    }
}

@Composable
private fun ListingCard(
    item: ListingItem,
    onClick: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = item.title,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
            item.publishDate?.let { date ->
                Text(
                    text = date,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 4.dp),
                )
            }
            item.assetType?.let { type ->
                Text(
                    text = "Type: $type",
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.padding(top = 4.dp),
                )
            }
            item.likeCount?.let { likes ->
                Text(
                    text = "Likes: $likes",
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.padding(top = 4.dp),
                )
            }
        }
    }
}

@Composable
private fun StatusMessage(message: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(24.dp),
        )
    }
}
