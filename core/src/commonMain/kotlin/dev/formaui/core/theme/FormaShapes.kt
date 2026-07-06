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
 * Five named tiers give components a predictable, opinionated roundness. The tiers also feed
 * Material 3 via [material], so any component that reads shapes from [MaterialTheme][androidx.compose.material3.MaterialTheme]
 * inherits FormaUI's corners with no extra wiring.
 *
 * Default scale:
 *
 * | Token    | Radius       | Typical use                          |
 * |----------|--------------|--------------------------------------|
 * | [none]   | 0dp          | edge-to-edge surfaces, dividers      |
 * | [small]  | 8dp          | chips, text fields, small buttons    |
 * | [medium] | 12dp         | cards, standard buttons              |
 * | [large]  | 16dp         | dialogs, bottom sheets, large cards  |
 * | [full]   | fully round  | avatars, FABs, pill-shaped controls  |
 *
 * @property none square corners (default 0dp).
 * @property small slightly rounded corners (default 8dp).
 * @property medium moderately rounded corners (default 12dp).
 * @property large generously rounded corners (default 16dp).
 * @property full fully rounded / pill corners ([CircleShape]).
 */
@ExperimentalFormaUiApi
@Immutable
class FormaShapes(
    val none: CornerBasedShape = RoundedCornerShape(0.dp),
    val small: CornerBasedShape = RoundedCornerShape(8.dp),
    val medium: CornerBasedShape = RoundedCornerShape(12.dp),
    val large: CornerBasedShape = RoundedCornerShape(16.dp),
    val full: CornerBasedShape = CircleShape,
) {
    /**
     * The equivalent Material 3 [Shapes], applied by [FormaTheme] so M3 components pick up
     * FormaUI's corners. FormaUI's five tiers map onto M3's five slots as
     * `extraSmall→small`, `small→small`, `medium→medium`, `large→large`, `extraLarge→large`.
     */
    val material: Shapes = Shapes(
        extraSmall = small,
        small = small,
        medium = medium,
        large = large,
        extraLarge = large,
    )
}
