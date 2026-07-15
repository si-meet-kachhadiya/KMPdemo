package com.kmpdemo

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.kmpdemo.mvi.model.ListingItem
import com.kmpdemo.mvi.presentation.ListingIntent
import com.kmpdemo.mvi.presentation.ListingViewModel
import com.kmpdemo.mvi.presentation.createListingViewModel
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

        // 1) Create MVI ViewModel (resolves GetListingsUseCase from SDK)
        val viewModel = createListingViewModel(lifecycleScope)

        // 2) Trigger KKR listing API call from MainActivity
        viewModel.dispatch(ListingIntent.Load)

        setContent {
            MaterialTheme {
                MviDemoScreen(viewModel = viewModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MviDemoScreen(viewModel: ListingViewModel) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("KKR Listing (MVI)") },
                actions = {
                    Button(onClick = { viewModel.dispatch(ListingIntent.Refresh) }) {
                        Text("Refresh")
                    }
                },
            )
        },
    ) { innerPadding ->
        ListingContent(listingsState = state.listings, padding = innerPadding)
    }
}

@Composable
private fun ListingContent(
    listingsState: DataState<List<ListingItem>>,
    padding: PaddingValues,
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
                    ListingList(items = listingsState.data)
                }
            }
            is DataState.Failure -> StatusMessage(listingsState.toErrorMessage())
            is DataState.NoNetwork -> StatusMessage("No internet connection")
        }
    }
}

@Composable
private fun ListingList(items: List<ListingItem>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(items, key = { it.id }) { item ->
            ListingCard(item)
        }
    }
}

@Composable
private fun ListingCard(item: ListingItem) {
    Card(modifier = Modifier.fillMaxWidth()) {
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
