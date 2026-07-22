/*
 * Copyright 2026 FormaUI. Licensed under the Apache License, Version 2.0.
 */
package dev.formaui.core.theme

import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.dp
import dev.formaui.core.annotation.ExperimentalFormaUiApi

/**
 * FormaUI shape scale — corner-radius tiers consistent with Material You's shape system.
 *
 * Eight named tiers give components a predictable, opinionated roundness. The tiers also feed
 * Material 3 via [material], so any component that reads shapes from [MaterialTheme][androidx.compose.material3.MaterialTheme]
 * inherits FormaUI's corners with no extra wiring.
 *
 * Default scale:
 *
 * | Token  | Radius       | Typical use                                    |
 * |--------|--------------|-------------------------------------------------|
 * | [none] | 0dp          | edge-to-edge surfaces, dividers                 |
 * | [xs]   | 4dp          | badge accents, tiny dropdowns                   |
 * | [sm]   | 6dp          | small inline buttons, dropdown items            |
 * | [md]   | 8dp          | buttons, text inputs, tabs                      |
 * | [lg]   | 12dp         | content cards                                   |
 * | [xl]   | 16dp         | hero/marquee containers, dialogs                |
 * | [pill] | fully round  | stadium controls, badges                        |
 * | [full] | fully round  | avatars, icon buttons                           |
 *
 * [pill] and [full] resolve to the same [CircleShape] — they're distinct semantic tokens (a
 * stadium-shaped control vs. a fully circular one) that happen to share a value at every current
 * use site.
 *
 * M3-default components (e.g. menus) that read shapes off [material] pick up crisper corners via
 * the tier remap below — that's intended, not a gap.
 *
 * @property none square corners (default 0dp).
 * @property xs barely-rounded corners (default 4dp).
 * @property sm small inline corners (default 6dp).
 * @property md standard control corners — buttons, inputs, tabs (default 8dp).
 * @property lg content-card corners (default 12dp).
 * @property xl hero-container / dialog corners (default 16dp).
 * @property pill stadium-shaped corners ([CircleShape]).
 * @property full fully rounded / circular corners ([CircleShape]).
 */
@ExperimentalFormaUiApi
@Immutable
class FormaShapes(
    val none: CornerBasedShape = RoundedCornerShape(0.dp),
    val xs: CornerBasedShape = RoundedCornerShape(4.dp),
    val sm: CornerBasedShape = RoundedCornerShape(6.dp),
    val md: CornerBasedShape = RoundedCornerShape(8.dp),
    val lg: CornerBasedShape = RoundedCornerShape(12.dp),
    val xl: CornerBasedShape = RoundedCornerShape(16.dp),
    val pill: CornerBasedShape = CircleShape,
    val full: CornerBasedShape = CircleShape,
) {
    /**
     * The equivalent Material 3 [Shapes], applied by [FormaTheme] so M3 components pick up
     * FormaUI's corners. FormaUI's tiers map onto M3's five slots as
     * `extraSmall→xs`, `small→sm`, `medium→md`, `large→lg`, `extraLarge→xl`.
     */
    val material: Shapes = Shapes(
        extraSmall = xs,
        small = sm,
        medium = md,
        large = lg,
        extraLarge = xl,
    )
}
