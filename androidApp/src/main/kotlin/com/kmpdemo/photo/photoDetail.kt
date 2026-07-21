@file:JvmName("PhotoDetailKt")

package com.kmpdemo.photo

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import coil.compose.AsyncImage
import com.kmpdemo.mvi.detail.model.PhotoDetail
import com.kmpdemo.mvi.detail.model.PhotoImage
import com.kmpdemo.mvi.detail.presentation.PhotoDetailIntent
import com.kmpdemo.mvi.detail.presentation.createPhotoDetailViewModel
import com.kmpdemo.utils.ToolbarCommon
import com.kmpsdk.presentation.state.DataState
import com.kmpsdk.presentation.state.toErrorMessage

private val trendingBgColor = Color(0xFFE1D5E7)

/**
 * Photo detail screen.
 * Existing banner UI is left as-is (not wired to API).
 * Photo images from API are listed below that design.
 */
@Composable
fun PhotoDetailScreen(
    titleAlias: String,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val viewModel = remember(titleAlias) {
        createPhotoDetailViewModel(lifecycleOwner.lifecycleScope)
    }
    val state by viewModel.state.collectAsState()

    LaunchedEffect(titleAlias) {
        viewModel.dispatch(PhotoDetailIntent.Load(titleAlias))
    }

    Column(modifier = modifier.fillMaxSize()) {
        // Existing design — not wired to API data
        ToolbarCommon(title = "Photo", onBackClick = onBackClick)

        when (val detail = state.detail) {
            is DataState.Idle,
            is DataState.Loading,
            -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator()
                }
            }

            is DataState.Success -> {
                BannerImage(data = detail.data)
                PhotoImagesList(images = detail.data.images)
            }

            is DataState.Failure -> {
                Text(
                    text = detail.toErrorMessage(),
                    modifier = Modifier.padding(16.dp),
                    color = MaterialTheme.colorScheme.error,
                )
            }

            is DataState.NoNetwork -> {
                Text(
                    text = "No internet connection",
                    modifier = Modifier.padding(16.dp),
                )
            }
        }
    }
}

@Composable
private fun PhotoImagesList(images: List<PhotoImage>) {
    if (images.isEmpty()) {
        Text(
            text = "No photos in this album",
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.bodyMedium,
        )
        return
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        items(images, key = { it.id }) { image ->
            PhotoImageRow(image)
        }
    }
}

@Composable
private fun PhotoImageRow(image: PhotoImage) {
    Column(modifier = Modifier.fillMaxWidth()) {
        AsyncImage(
            model = image.imageUrl,
            contentDescription = image.caption,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16f / 10f)
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0xFFE8E8E8)),
            contentScale = ContentScale.Crop,
        )
        image.caption?.takeIf { it.isNotBlank() }?.let { caption ->
            Text(
                text = caption,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 6.dp),
            )
        }
    }
}

@Preview
@Composable
fun PhotoListing(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
    ) {
        ToolbarCommon("Photo")
    }
}

@Composable
fun BannerImage(
    modifier: Modifier = Modifier,
    data: PhotoDetail ?= null,
    onLikeClick: () -> Unit = {},
    onShareClick: () -> Unit = {},
) {
    Box(modifier = modifier) {

        AsyncImage(
            model = data?.images?.first()?.imageUrl,
            contentDescription = data?.images?.first()?.caption,
            modifier = Modifier
                .fillMaxWidth()
                .height(height = 276.dp)
                .aspectRatio(16f / 10f)
                .background(Color(0xFFE8E8E8)),
            contentScale = ContentScale.Crop,
        )

        Box(
            modifier = modifier
                .padding(start = 20.dp, end = 20.dp, bottom = 20.dp)
                .fillMaxWidth()
                .align(alignment = Alignment.BottomCenter),
        ) {
            Column(modifier = modifier.fillMaxWidth()) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(trendingBgColor),
                ) {
                    Text(
                        modifier = modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                        text = "Trending",
                        color = Color.Black,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    modifier = modifier.fillMaxWidth(),
                    text = data?.title?:"",
                    color = Color.White,
                    fontSize = 12.sp,
                    maxLines = 1
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        modifier = modifier.weight(1f),
                        text = data?.publishedDate?:"",
                        color = Color.White,
                        fontSize = 8.sp,
                    )

                    Box {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                painter = painterResource(com.kmpdemo.R.drawable.ic_launcher_background),
                                contentDescription = "like icon",
                                modifier = Modifier
                                    .size(15.dp)
                                    .clip(CircleShape)
                                    .clickable(onClick = {
                                        onLikeClick
                                    }),
                            )

                            Spacer(modifier = Modifier.width(8.dp))

                            Text(
                                modifier = modifier,
                                text = "100K",
                                color = Color.Yellow,
                                fontSize = 10.sp,
                            )

                            Spacer(modifier = Modifier.width(8.dp))

                            Box(
                                modifier = Modifier
                                    .height(12.dp)
                                    .width(1.dp)
                                    .background(Color.White),
                            )

                            Spacer(modifier = Modifier.width(8.dp))

                            Icon(
                                painter = painterResource(com.kmpdemo.R.drawable.ic_launcher_background),
                                contentDescription = "share icon",
                                modifier = Modifier
                                    .size(15.dp)
                                    .clickable(onClick = {
                                        onShareClick
                                    }),
                            )
                        }
                    }
                }
            }
        }
    }
}
