/*
 * Copyright 2026 FormaUI. Licensed under the Apache License, Version 2.0.
 */
@file:OptIn(ExperimentalFormaUiApi::class, ExperimentalMaterial3Api::class)

package dev.formaui.components.carousel

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.carousel.rememberCarouselState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.formaui.core.annotation.ExperimentalFormaUiApi
import dev.formaui.core.theme.FormaTheme

/**
 * Preview of [FormaCarousel]: the multi-browse and uncontained variants, each with a few colored
 * placeholder items.
 */
@Preview
@Composable
private fun FormaCarouselPreview() {
    FormaTheme {
        Surface {
            Column(
                modifier = Modifier.padding(FormaTheme.spacing.md),
                verticalArrangement = Arrangement.spacedBy(FormaTheme.spacing.md),
            ) {
                val colors = listOf(
                    MaterialTheme.colorScheme.primaryContainer,
                    MaterialTheme.colorScheme.secondaryContainer,
                    MaterialTheme.colorScheme.tertiaryContainer,
                    MaterialTheme.colorScheme.errorContainer,
                    MaterialTheme.colorScheme.surfaceVariant,
                )

                Text("Multi-browse")
                val multiState = rememberCarouselState(itemCount = { colors.size })
                FormaCarousel(
                    state = multiState,
                    itemWidth = 200.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp),
                ) { index ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(colors[index]),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text("${index + 1}")
                    }
                }

                Text("Uncontained")
                val uncontainedState = rememberCarouselState(itemCount = { colors.size })
                FormaCarousel(
                    state = uncontainedState,
                    itemWidth = 140.dp,
                    variant = FormaCarouselVariant.Uncontained,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                ) { index ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(colors[index]),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text("${index + 1}")
                    }
                }
            }
        }
    }
}
