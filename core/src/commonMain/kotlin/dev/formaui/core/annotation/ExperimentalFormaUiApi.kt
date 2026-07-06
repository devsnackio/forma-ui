/*
 * Copyright 2026 FormaUI. Licensed under the Apache License, Version 2.0.
 */
package dev.formaui.core.annotation

/**
 * Marks a FormaUI API as experimental.
 *
 * FormaUI is pre-1.0 (see [semver][https://semver.org] `0.x`): the public surface is not yet
 * frozen and may change in a breaking way in any minor release without that counting as a
 * semver violation. Opt in on a per-use-site basis with `@OptIn(ExperimentalFormaUiApi::class)`,
 * or module-wide via the Kotlin compiler flag
 * `-opt-in=dev.formaui.core.annotation.ExperimentalFormaUiApi`.
 *
 * Every public API in `:core` and `:components` carries this annotation until the surface is
 * proven by real external usage on the road to `1.0.0`.
 */
@RequiresOptIn(
    message = "This FormaUI API is experimental and may change or be removed in a future " +
        "pre-1.0 release. Opt in with @OptIn(ExperimentalFormaUiApi::class).",
    level = RequiresOptIn.Level.WARNING,
)
@Retention(AnnotationRetention.BINARY)
@Target(
    AnnotationTarget.CLASS,
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.CONSTRUCTOR,
    AnnotationTarget.TYPEALIAS,
)
annotation class ExperimentalFormaUiApi
