package com.example.notefun2.ui.screen.shared

import androidx.compose.animation.core.SpringSpec
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.notefun2.R
import com.example.gamecore.domain.model.Game
import com.example.gamecore.domain.model.Release
import com.example.gamecore.domain.model.enum_class.PlatformType
import com.example.notefun2.ui.theme.*
import com.example.notefun2.ui.utils.bannerUrl
import com.example.notefun2.ui.utils.logo
import com.google.accompanist.coil.rememberCoilPainter
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

const val TopAppBarHeight = 50
const val TopAppBarVerticalPadding = 8

@Composable
fun NoteFunLogo(modifier: Modifier){
    Image(painterResource(id = R.drawable.logo), contentDescription = "logo",
    modifier = Modifier.size(120.dp))
}

@Composable
fun PlatformLogo(
    platformTypeList: List<PlatformType>,
    modifier: Modifier = Modifier,
    singleLogoModifier: Modifier = Modifier,
){
    Row(modifier = modifier) {
        for (platform in platformTypeList){
            Icon(painter = painterResource(platform.logo), contentDescription = null,
                modifier = singleLogoModifier
                    .height(12.dp)
                    .padding(end = 8.dp))
        }

    }
}

@Composable
fun GameCard(
    game: Game,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
){
    Surface(modifier = modifier.clickable { onClick() },
        color = Color.Gray.copy(0.05f),
        shape = MaterialTheme.shapes.small){
        Column{
            Image(painter = rememberCoilPainter(
                request = game.bannerUrl,
                previewPlaceholder = R.drawable.apex_legends_cover,
                fadeIn = true,
            ),
                contentDescription = game.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp),
                contentScale = ContentScale.Crop,
                alignment = Alignment.TopCenter
            )
            Text(text = game.name,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .padding(start = 8.dp, top = 4.dp, bottom = 0.dp)
            )
            Row(horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .padding(bottom = 8.dp, top = 4.dp)
                    .fillMaxWidth()) {
                PlatformLogo(platformTypeList = remember(game.id) {
                    game.platformList.groupBy { it.platformType }.keys.toList()
                },
                )
                GameScore(score = game.rating.toInt())
            }
        }
    }
}

@Composable
fun GameScore(
    score: Int,
    modifier: Modifier = Modifier,
    style: TextStyle = TextStyle.Default.copy(fontSize = 11.sp),
    borderWidth: Dp = (1.5).dp
){
    val scoreColor = remember {
        when {
            score > 70 -> HighScoreColor
            score > 50 -> MediumScoreColor
            score > 30 -> BadScoreColor
            else -> WorseScoreColor
        }
    }
    Text(text = score.toString(),
        style = style,
        color = scoreColor,
        modifier = modifier
            .border(width = borderWidth, color = scoreColor, shape = MaterialTheme.shapes.small)
            .padding(horizontal = (3.5).dp, vertical = 2.dp))
}

@Composable
fun NoteFunTopAppBar(
    title: @Composable () -> Unit,
    drawerState: DrawerState,
    modifier: Modifier = Modifier,
    actions: @Composable RowScope.()-> Unit = {},
){

    val coroutineScope = rememberCoroutineScope()

    TopAppBar(
        title = title,
        actions = actions,
        modifier = modifier
            .padding(
                horizontal = 16.dp,
                vertical = TopAppBarVerticalPadding.dp
            )
            .height(TopAppBarHeight.dp)
            .clip(MaterialTheme.shapes.medium),
        elevation = 1.dp
    )
}

@Composable
fun ReleaseCard(
    release: Release,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier.clickable { onClick() },
        shape = RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp),
        elevation = 5.dp
    ) {
        Image(
            painter = rememberCoilPainter(
                request = release.artWorkUrl,
                previewPlaceholder = R.drawable.apex_legends_artwork,
                fadeIn = true,
            ),
            contentDescription = null,
            contentScale = ContentScale.Crop
        )
        Row(
            verticalAlignment = Alignment.Bottom,
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Color.Transparent,
                            MaterialTheme.colors.surface
                        )
                    )
                )
                .padding(vertical = 8.dp),
        ) {
            Image(
                painter = rememberCoilPainter(
                    request = release.gameCoverUrl,
                    previewPlaceholder = R.drawable.apex_legends_cover,
                    fadeIn = true,
                ),
                contentDescription = null,
                modifier = Modifier
                    .width(80.dp)
                    .padding(horizontal = 8.dp)
                    .clip(MaterialTheme.shapes.small),
            )
            Column {
                Text(
                    text = release.gameName,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )

                Text(
                    release.formattedDate, color = MaterialTheme.colors.primary,
                    fontSize = 14.sp,
                )
                ProvideTextStyle(
                    value = TextStyle(fontSize = 12.sp),
                ) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text(release.region)
                        Text(
                            release.platform.name,
                            modifier = Modifier.padding(end = 4.dp),
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun Tabs(titles: List<String>, onTabSelected: (selectedTabTitle: String) -> Unit) {
    var selectedTabIndex by remember { mutableStateOf(0) }

    ScrollableTabRow(
        selectedTabIndex = selectedTabIndex,
        edgePadding = 0.dp,
        indicator = { tabPositions ->
            TabRowDefaults.Indicator(
                height = 2.dp,
                modifier = Modifier
                    .tabIndicatorOffset((tabPositions[selectedTabIndex]))
                    .width(3.dp),
                color = MaterialTheme.colors.primary,
            )
        },
    ) {
        titles.forEachIndexed { index, title ->
            Tab(
                selected = index == selectedTabIndex,
                selectedContentColor = MaterialTheme.colors.primary,
                unselectedContentColor = MaterialTheme.colors.onSurface.copy(0.87F),
                text = { Text(text = title, fontSize = 13.sp) },
                onClick = {
                    if (selectedTabIndex != index) {
                        selectedTabIndex = index
                        onTabSelected(title)
                    }
                },
            )
        }
    }
}

@Composable
fun CollapsingTabBarScaffold(
    topAppBar: @Composable () -> Unit,
    drawerState: DrawerState,
    drawerContent: @Composable ColumnScope.() -> Unit,
    drawerBackgroundColor: Color = Color.Transparent,
    drawerScrimColor: Color = MaterialTheme.colors.surface.copy(0.63f),
    drawerElevation: Dp = 0.dp,
    scaffoldContent: @Composable () -> Unit,
) {
    // TODO Refactor
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()
    val totalTopAppBarHeight =
        remember { (TopAppBarHeight + (TopAppBarVerticalPadding * 2)).toFloat() }
    val localDensity = LocalDensity.current

    ModalDrawer(
        drawerState = drawerState,
        drawerBackgroundColor = drawerBackgroundColor,
        scrimColor = drawerScrimColor,
        drawerElevation = drawerElevation,
        drawerContent = drawerContent,
        content = {
            Box(
                Modifier
                    .verticalScroll(scrollState)
                    .nestedScroll(object : NestedScrollConnection {
                        var yOffset = 0f
                        override fun onPreScroll(
                            available: Offset,
                            source: NestedScrollSource
                        ): Offset {
                            if (available.y == 0f || source != NestedScrollSource.Drag) return Offset.Zero

                            return if (available.y > 0) {
                                if (yOffset == 0f) return Offset.Zero
                                val tempOffset = available.y - yOffset
                                val dragConsumed = yOffset + tempOffset
                                yOffset = (yOffset + dragConsumed).coerceAtMost(0f)

                                coroutineScope.launch {
                                    scrollState.animateScrollTo(-localDensity.run {
                                        yOffset.toInt().dp
                                            .toPx()
                                            .toInt()
                                    }, animationSpec = SpringSpec(stiffness = 500f))
                                }

                                Offset(0f, dragConsumed)

                            } else {
                                if (yOffset <= -totalTopAppBarHeight) return Offset.Zero
                                val previousOffset = yOffset
                                yOffset =
                                    -((previousOffset.absoluteValue + available.y.absoluteValue).coerceAtMost(
                                        totalTopAppBarHeight
                                    ))

                                coroutineScope.launch {
                                    scrollState.animateScrollTo(localDensity.run {
                                        yOffset.toInt().dp
                                            .toPx()
                                            .toInt()
                                    }.absoluteValue, animationSpec = SpringSpec(stiffness = 500f))
                                }
                                val dragConsumed =
                                    (yOffset.absoluteValue - previousOffset.absoluteValue)
                                Offset(0f, -dragConsumed)
                            }
                        }
                    })
                    .height((LocalConfiguration.current.screenHeightDp + totalTopAppBarHeight).dp),
//            .offset(y = yOffsetDp.dp),
                contentAlignment = Alignment.TopCenter,
            ) {
                Column {
                    topAppBar()
                    scaffoldContent()
                }
            }
        }
    )
}