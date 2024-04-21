package com.vasberc.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.vasberc.domain.model.BookItem
import com.vasberc.presentation.R

@Composable
fun BookListItem(item: BookItem?) {
    if (item != null) {
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(15.dp))
                .background(MaterialTheme.colorScheme.primary)
        ) {

            var showShimmer by remember { mutableStateOf(true) }

            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(item.image)
                    .crossfade(true)
                    .build(),
                contentDescription = "Book ${item.title} cover image",
                error = painterResource(id = R.drawable.ic_no_image_placeholder),
                onError = { showShimmer = false },
                onSuccess = { showShimmer = false },
                modifier = Modifier
                    .height(250.dp)
                    .fillMaxWidth()
                    .background(shimmerBrush(showShimmer = showShimmer)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Title",
                modifier = Modifier.padding(horizontal = 10.dp),
                style = TextStyle(
                    fontSize = 18.sp,
                    color = Color.White
                )
            )

            Text(
                text = item.title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(horizontal = 10.dp),
                style = TextStyle(
                    color = Color.White
                )
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Author" + if(item.authors.size != 1) "s" else "",
                modifier = Modifier.padding(horizontal = 10.dp),
                style = TextStyle(
                    fontSize = 18.sp,
                    color = Color.White
                )
            )

            Text(
                text = item.authors.joinToString(" & "),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(horizontal = 10.dp),
                style = TextStyle(
                    color = Color.White
                )
            )

            Spacer(modifier = Modifier.height(10.dp))

        }
    } else {
        ConstraintLayout {
            val (column, box) = createRefs()
            //This column is a copy of the list item to achieve the same size
            Column(
                modifier = Modifier
                    .clip(RoundedCornerShape(15.dp))
                    .background(MaterialTheme.colorScheme.primary)
                    .constrainAs(column) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
            ) {

                Box(
                    modifier = Modifier
                        .height(250.dp)
                        .fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "",
                    modifier = Modifier.padding(horizontal = 10.dp),
                    style = TextStyle(
                        fontSize = 18.sp,
                        color = Color.White
                    )
                )

                Text(
                    text = "",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(horizontal = 10.dp),
                    style = TextStyle(
                        color = Color.White
                    )
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "",
                    modifier = Modifier.padding(horizontal = 10.dp),
                    style = TextStyle(
                        fontSize = 18.sp,
                        color = Color.White
                    )
                )

                Text(
                    text = "",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(horizontal = 10.dp),
                    style = TextStyle(
                        color = Color.White
                    )
                )

                Spacer(modifier = Modifier.height(10.dp))

            }
            //Place on top the shimmer effect
            Box(modifier = Modifier
                .constrainAs(box) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                    height = Dimension.fillToConstraints
                }
                .background(shimmerBrush())) {
            }
        }

    }
}