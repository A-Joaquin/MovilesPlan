package com.lainus.examen.plans

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.lainus.domain.Plan
import com.lainus.examen.R
import kotlinx.coroutines.launch
import com.lainus.examen.utils.WhatsAppHelper

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PlansUI(
    viewModel: PlansViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val currentIndex by viewModel.currentPlanIndex.collectAsState()
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        when (val currentState = state) {
            is PlansViewModel.PlansState.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            is PlansViewModel.PlansState.Success -> {
                PlansContent(
                    plans = currentState.plans,
                    currentIndex = currentIndex,
                    onPlanIndexChanged = viewModel::onPlanIndexChanged,
                    onPlanSelected = viewModel::onPlanSelected,
                    viewModel = viewModel,
                    context = context
                )
            }
            is PlansViewModel.PlansState.Error -> {
                Text(
                    text = "Error: ${currentState.message}",
                    modifier = Modifier.align(Alignment.Center),
                    color = Color.Red
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun PlansContent(
    plans: List<Plan>,
    currentIndex: Int,
    onPlanIndexChanged: (Int) -> Unit,
    onPlanSelected: (Plan) -> Unit,
    viewModel: PlansViewModel,
    context: android.content.Context
) {
    val pagerState = rememberPagerState(pageCount = { plans.size })
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(pagerState.currentPage) {
        onPlanIndexChanged(pagerState.currentPage)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Nuestros planes móviles",
                color = Color(0xFFFF5252),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 32.dp)
            )

            Text(
                text = "La mejor cobertura, increíbles beneficios y un compromiso con el planeta.",
                color = Color.Gray,
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(horizontal = 32.dp, vertical = 8.dp)
            )

            HorizontalPager(
                state = pagerState,
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(horizontal = 32.dp),
                pageSpacing = 16.dp
            ) { page ->
                PlanCard(
                    plan = plans[page],
                    onSelectPlan = { onPlanSelected(plans[page]) }
                )
            }

            Row(
                modifier = Modifier
                    .padding(bottom = 24.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                plans.indices.forEach { index ->
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .size(if (index == currentIndex) 24.dp else 8.dp, 8.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(
                                if (index == currentIndex) Color(0xFFFF5252) else Color(0xFFCCCCCC)
                            )
                    )
                }
            }
        }

        if (currentIndex > 0) {
            IconButton(
                onClick = {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(currentIndex - 1)
                    }
                },
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 8.dp)
                    .size(48.dp)
                    .background(Color.White, CircleShape)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_chevron_left),
                    contentDescription = "Previous",
                    tint = Color(0xFFFF5252)
                )
            }
        }

        if (currentIndex < plans.size - 1) {
            IconButton(
                onClick = {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(currentIndex + 1)
                    }
                },
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 8.dp)
                    .size(48.dp)
                    .background(Color.White, CircleShape)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_chevron_right),
                    contentDescription = "Next",
                    tint = Color(0xFFFF5252)
                )
            }
        }

        FloatingActionButton(
            onClick = {
                // Quitar la verificación del plan ya que no lo necesitas
                WhatsAppHelper.openWhatsApp(context)
            },
            containerColor = Color(0xFF25D366),
            contentColor = Color.White,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_whatsapp),
                contentDescription = "WhatsApp"
            )
        }
    }
}

@Composable
private fun PlanCard(
    plan: Plan,
    onSelectPlan: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.75f),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = plan.name,
                color = Color(0xFFFF5252),
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Antes ",
                        color = Color.Gray,
                        fontSize = 16.sp
                    )
                    Text(
                        text = "${plan.originalPrice}",
                        color = Color.Gray,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        textDecoration = TextDecoration.LineThrough
                    )
                    Text(
                        text = "/mes",
                        color = Color.Gray,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    Text(
                        text = "Ahora ",
                        color = Color.Gray,
                        fontSize = 16.sp
                    )
                    Text(
                        text = "${plan.discountedPrice}",
                        color = Color.Black,
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "/mes",
                        color = Color.Gray,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }

                Text(
                    text = plan.dataAmount,
                    color = Color.Black,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            Column(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .weight(1f)
            ) {
                plan.features.forEach { feature ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(vertical = 2.dp)
                    ) {
                        Icon(
                            Icons.Default.Check,
                            contentDescription = null,
                            tint = Color(0xFF4CAF50),
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = feature,
                            color = Color.Gray,
                            fontSize = 13.sp,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                }
            }

            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                plan.socialNetworks.forEach { network ->
                    SocialNetworkIcon(network)
                }
            }

            Button(
                onClick = onSelectPlan,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFF5252)
                )
            ) {
                Text(
                    text = "Quiero este plan",
                    fontSize = 14.sp,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )
                Icon(
                    painter = painterResource(id = R.drawable.ic_whatsapp),
                    contentDescription = "WhatsApp",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
private fun SocialNetworkIcon(network: String) {
    val iconRes = when (network) {
        "whatsapp" -> R.drawable.ic_whatsapp
        "facebook" -> R.drawable.ic_facebook
        "twitter" -> R.drawable.ic_twitter
        "instagram" -> R.drawable.ic_instagram
        "snapchat" -> R.drawable.ic_snapchat
        "telegram" -> R.drawable.ic_telegram
        else -> null
    }

    iconRes?.let {
        Icon(
            painter = painterResource(id = it),
            contentDescription = network,
            modifier = Modifier
                .padding(horizontal = 4.dp)
                .size(24.dp),
            tint = Color.Unspecified
        )
    }
}
