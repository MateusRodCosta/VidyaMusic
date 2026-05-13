package com.mateusrodcosta.apps.vidyamusic.features.shared.components.utils

import androidx.window.core.layout.WindowSizeClass

fun shouldShowLandscape(sizeClass: WindowSizeClass): Boolean {

    val showLandscapePhone = !sizeClass.isHeightAtLeastBreakpoint(
        WindowSizeClass.HEIGHT_DP_MEDIUM_LOWER_BOUND
    )
    val showLandscapeTablet = sizeClass.isWidthAtLeastBreakpoint(
        WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND
    )

    return showLandscapePhone || showLandscapeTablet
}