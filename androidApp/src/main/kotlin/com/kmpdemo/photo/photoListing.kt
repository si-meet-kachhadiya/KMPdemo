package com.kmpdemo.photo

import android.widget.Space
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kmpdemo.utils.ToolbarCommon

private val trendingBgColor = Color(0xFFE1D5E7)


@Preview
@Composable
fun PhotoListing(modifier: Modifier = Modifier) {

    Column(
        modifier = modifier.fillMaxSize(),
    ) {
        ToolbarCommon("Photo")
        BannerImage()
    }
}

@Composable
fun BannerImage(
    modifier: Modifier = Modifier,
    assetTitle: String = "Moments, Memories, Reactions, much more for KKR at the IPL Auction 2024",
    onLikeClick: () -> Unit = {},
    onShareClick: () -> Unit = {}
) {

    Box(modifier = modifier) {

        Image(
            painter = painterResource(com.kmpdemo.R.drawable.ic_launcher_background),
            contentDescription = "",
            modifier = Modifier
                .fillMaxWidth()
                .height(height = 276.dp)
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
                        .background(trendingBgColor)
                ) {
                    Text(
                        modifier = modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                        text = "Trending",
                        color = Color.Black,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    modifier = modifier.fillMaxWidth(),
                    text = assetTitle,
                    color = Color.White,
                    fontSize = 10.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {

                    Text(
                        modifier = modifier.weight(1f),
                        text = "05 Apr 2026",
                        color = Color.White,
                        fontSize = 8.sp
                    )

                    Box(
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                painter = painterResource(com.kmpdemo.R.drawable.ic_launcher_background),
                                contentDescription = "like icon",
                                modifier = Modifier
                                    .size(15.dp)
                                    .clip(CircleShape)
                                    .clickable(onClick = {
                                        onLikeClick
                                    })
                            )

                            Spacer(modifier = Modifier.width(8.dp))

                            Text(
                                modifier = modifier,
                                text = "100K",
                                color = Color.Yellow,
                                fontSize = 10.sp
                            )

                            Spacer(modifier = Modifier.width(8.dp))

                            Box(
                                modifier = Modifier
                                    .height(12.dp)
                                    .width(1.dp)
                                    .background(Color.White)
                            )

                            Spacer(modifier = Modifier.width(8.dp))

                            Icon(
                                painter = painterResource(com.kmpdemo.R.drawable.ic_launcher_background),
                                contentDescription = "share icon",
                                modifier = Modifier
                                    .size(15.dp)
                                    .clickable(onClick = {
                                        onShareClick
                                    })
                            )
                        }
                    }
                }
            }
        }
    }
}
