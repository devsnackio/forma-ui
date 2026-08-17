/*
 * Copyright 2026 FormaUI. Licensed under the Apache License, Version 2.0.
 */
@file:OptIn(ExperimentalFormaUiApi::class, ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)

package dev.formaui.components.screenshot

import android.graphics.Bitmap
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.toPixelMap
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.unit.dp
import dev.formaui.components.avatar.FormaAvatar
import dev.formaui.components.avatar.FormaAvatarSize
import dev.formaui.components.button.FormaButton
import dev.formaui.components.button.FormaButtonVariant
import dev.formaui.components.card.FormaCard
import dev.formaui.components.card.FormaCardVariant
import dev.formaui.components.chart.FormaBarChart
import dev.formaui.components.chart.FormaChartEntry
import dev.formaui.components.chart.FormaLineChart
import dev.formaui.components.chart.captureNodeToImage
import dev.formaui.components.chip.FormaChip
import dev.formaui.components.chip.FormaChipVariant
import dev.formaui.components.listitem.FormaListItem
import dev.formaui.components.slider.FormaSlider
import dev.formaui.components.switch.FormaSwitch
import dev.formaui.components.textfield.FormaTextField
import dev.formaui.core.annotation.ExperimentalFormaUiApi
import dev.formaui.core.theme.FormaTheme
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode
import java.io.File

/**
 * Renders fixed boards and writes them to `build/screenshots/` as PNG — the marketing screenshots
 * (README hero, social preview, the Material 3 / FormaUI before-and-after) are generated from
 * here rather than photographed off a device.
 *
 * This is a **test**, not just a generator, and the assertions are the point: each capture must
 * be non-blank and carry more than a threshold number of distinct colors. A board that silently
 * stops rendering — the exact failure
 * [ChartPixelRenderTest][dev.formaui.components.chart.ChartPixelRenderTest] was written for —
 * would otherwise produce a plausible, correctly-sized, empty PNG and ship it to the README.
 *
 * Mechanics, both inherited from the chart pixel tests:
 * - [captureNodeToImage] rather than `captureToImage()`, which cannot work under Robolectric.
 * - `@GraphicsMode(NATIVE)` is mandatory; under the project's default LEGACY graphics
 *   `drawPath`/`drawArc`/`drawLine` are raster no-ops and every capture reads blank.
 *
 * `qualifiers` pins a phone-shaped xhdpi viewport so output is deterministic across machines
 * instead of inheriting Robolectric's small default screen.
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34], qualifiers = "w411dp-h838dp-xhdpi")
@GraphicsMode(GraphicsMode.Mode.NATIVE)
class ShowcaseScreenshotTest {

    @get:Rule
    val composeRule = createComposeRule()

    private val outputDir = File("build/screenshots").apply { mkdirs() }

    @Test
    fun showcaseBoard_light_rendersAndIsCaptured() {
        capture("showcase-light") {
            FormaTheme(dynamicColor = false, darkTheme = false) { ShowcaseBoard() }
        }
    }

    @Test
    fun showcaseBoard_dark_rendersAndIsCaptured() {
        capture("showcase-dark") {
            FormaTheme(dynamicColor = false, darkTheme = true) { ShowcaseBoard() }
        }
    }

    /**
     * The "before" half of the comparison — **genuine Material 3 widgets** under a bare
     * [MaterialTheme], not FormaUI components with the theme removed.
     *
     * That distinction matters and is easy to get wrong: FormaUI's per-component defaults read
     * through composition locals that fall back to `FormaShapes()`/`FormaSpacing()` outside
     * [FormaTheme], so an unwrapped `FormaButton` still carries FormaUI's 8dp corners. Capturing
     * that and labelling it "Material 3" would overstate the difference with something a reader
     * could disprove by running the same code.
     */
    @Test
    fun comparisonBoard_stockMaterial3_rendersAndIsCaptured() {
        capture("compare-material3") {
            MaterialTheme { MaterialComparisonBoard() }
        }
    }

    /** The "after" half: the same layout and copy, built from FormaUI under [FormaTheme]. */
    @Test
    fun comparisonBoard_formaUi_rendersAndIsCaptured() {
        capture("compare-formaui") {
            FormaTheme(dynamicColor = false, darkTheme = false) { FormaComparisonBoard() }
        }
    }

    /** Composes [content], captures the tagged root, asserts it painted, and writes the PNG. */
    private fun capture(name: String, content: @Composable () -> Unit) {
        composeRule.setContent { content() }
        composeRule.waitForIdle()

        val image = composeRule.onNodeWithTag(BoardTag).captureNodeToImage()
        assertRendered(name, image)

        val file = File(outputDir, "$name.png")
        file.outputStream().use {
            image.asAndroidBitmap().compress(Bitmap.CompressFormat.PNG, 100, it)
        }
        println("[screenshot] ${file.absolutePath} (${image.width}x${image.height})")
    }

    /**
     * A correctly-sized blank image is the failure mode worth guarding against, so assert on
     * color variety rather than merely on non-zero dimensions: a board this dense cannot
     * legitimately resolve to a handful of colors.
     */
    private fun assertRendered(name: String, image: ImageBitmap) {
        assertTrue("$name: capture has degenerate size", image.width > 100 && image.height > 100)

        val pixels = image.toPixelMap()
        val distinct = buildSet {
            // Sampling on a grid keeps this cheap; a real render trips the threshold immediately.
            for (x in 0 until image.width step 4) {
                for (y in 0 until image.height step 4) add(pixels[x, y].value)
            }
        }
        assertTrue(
            "$name: only ${distinct.size} distinct colors — the board rendered blank or near-blank",
            distinct.size > 20,
        )
    }
}

private const val BoardTag = "showcase-board"

private val BarData = listOf(
    FormaChartEntry("Jan", 12f),
    FormaChartEntry("Feb", 32f),
    FormaChartEntry("Mar", 21f),
    FormaChartEntry("Apr", 45f),
)

/** The full-fat FormaUI board: what the library looks like with nothing configured. */
@Composable
private fun ShowcaseBoard() {
    Board {
        Text("Overview", style = MaterialTheme.typography.headlineMedium)

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            FormaButton(onClick = {}, variant = FormaButtonVariant.Filled) { Text("Filled") }
            FormaButton(onClick = {}, variant = FormaButtonVariant.Tonal) { Text("Tonal") }
            FormaButton(onClick = {}, variant = FormaButtonVariant.Outlined) { Text("Outlined") }
        }

        FormaTextField(
            value = "olive@formaui.dev",
            onValueChange = {},
            label = "Email",
            modifier = Modifier.fillMaxWidth(),
        )

        FormaCard(variant = FormaCardVariant.Elevated, modifier = Modifier.fillMaxWidth()) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Revenue", style = MaterialTheme.typography.titleMedium)
                // animationSpec = null pins the final frame; an in-flight entry animation would
                // make the captured pixels depend on clock timing.
                FormaBarChart(
                    entries = BarData,
                    animationSpec = null,
                    showValueLabels = true,
                    modifier = Modifier.fillMaxWidth().height(132.dp),
                )
            }
        }

        FormaCard(variant = FormaCardVariant.Filled, modifier = Modifier.fillMaxWidth()) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Sessions", style = MaterialTheme.typography.titleMedium)
                FormaLineChart(
                    values = listOf(4f, 9f, 6f, 14f, 11f, 17f),
                    animationSpec = null,
                    modifier = Modifier.fillMaxWidth().height(104.dp),
                )
            }
        }

        FormaListItem(
            headline = "Olive Kerr",
            supporting = "Product design",
            leading = {
                FormaAvatar(size = FormaAvatarSize.Medium) {
                    Text("OK", modifier = Modifier.align(Alignment.Center))
                }
            },
            trailing = { FormaSwitch(checked = true, onCheckedChange = {}) },
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

/**
 * The comparison layout, in FormaUI components. Deliberately restricted to widgets Material 3
 * also has, so [MaterialComparisonBoard] can mirror it one-for-one — the charts are a FormaUI
 * differentiator, but putting them in a side-by-side would compare a component against nothing.
 */
@Composable
private fun FormaComparisonBoard() {
    Board {
        Text("Overview", style = MaterialTheme.typography.headlineMedium)

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            FormaButton(onClick = {}, variant = FormaButtonVariant.Filled) { Text("Filled") }
            FormaButton(onClick = {}, variant = FormaButtonVariant.Tonal) { Text("Tonal") }
            FormaButton(onClick = {}, variant = FormaButtonVariant.Outlined) { Text("Outlined") }
        }

        FormaTextField(
            value = "olive@formaui.dev",
            onValueChange = {},
            label = "Email",
            modifier = Modifier.fillMaxWidth(),
        )

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            FormaChip("Design", onClick = {}, variant = FormaChipVariant.Filter, selected = true)
            FormaChip("Mobile", onClick = {}, variant = FormaChipVariant.Filter)
            FormaChip("Archive", onClick = {}, variant = FormaChipVariant.Assist)
        }

        FormaCard(variant = FormaCardVariant.Elevated, modifier = Modifier.fillMaxWidth()) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text("Revenue", style = MaterialTheme.typography.titleMedium)
                Text(CardBody, style = MaterialTheme.typography.bodyMedium)
            }
        }

        FormaListItem(
            headline = "Olive Kerr",
            supporting = "Product design",
            leading = {
                FormaAvatar(size = FormaAvatarSize.Medium) {
                    Text("OK", modifier = Modifier.align(Alignment.Center))
                }
            },
            trailing = { FormaSwitch(checked = true, onCheckedChange = {}) },
            modifier = Modifier.fillMaxWidth(),
        )

        FormaSlider(value = 0.62f, onValueChange = {}, modifier = Modifier.fillMaxWidth())
    }
}

/** [FormaComparisonBoard]'s mirror in stock `androidx.compose.material3` widgets. */
@Composable
private fun MaterialComparisonBoard() {
    Board {
        Text("Overview", style = MaterialTheme.typography.headlineMedium)

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Button(onClick = {}) { Text("Filled") }
            FilledTonalButton(onClick = {}) { Text("Tonal") }
            OutlinedButton(onClick = {}) { Text("Outlined") }
        }

        OutlinedTextField(
            value = "olive@formaui.dev",
            onValueChange = {},
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
        )

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            FilterChip(selected = true, onClick = {}, label = { Text("Design") })
            FilterChip(selected = false, onClick = {}, label = { Text("Mobile") })
            AssistChip(onClick = {}, label = { Text("Archive") })
        }

        ElevatedCard(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Text("Revenue", style = MaterialTheme.typography.titleMedium)
                Text(CardBody, style = MaterialTheme.typography.bodyMedium)
            }
        }

        ListItem(
            headlineContent = { Text("Olive Kerr") },
            supportingContent = { Text("Product design") },
            leadingContent = {
                Surface(
                    modifier = Modifier.size(40.dp),
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                ) {
                    Box(contentAlignment = Alignment.Center) { Text("OK") }
                }
            },
            trailingContent = { Switch(checked = true, onCheckedChange = {}) },
            modifier = Modifier.fillMaxWidth(),
        )

        Slider(value = 0.62f, onValueChange = {}, modifier = Modifier.fillMaxWidth())
    }
}

private const val CardBody = "Up 18% against the same period last quarter."

/**
 * Shared chrome so every board is captured at identical width, padding and spacing.
 *
 * Height wraps the content rather than filling the viewport: [captureNodeToImage] crops to the
 * node's bounds, so a `fillMaxSize` board would bake a tall strip of empty background into every
 * PNG and leave the README hero mostly blank.
 */
@Composable
private fun Board(content: @Composable () -> Unit) {
    Surface(modifier = Modifier.fillMaxWidth().wrapContentHeight().testTag(BoardTag)) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            content()
        }
    }
}
