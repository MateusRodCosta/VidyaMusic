package com.mateusrodcosta.apps.vidyamusic.features.settings.screens

import android.os.Build
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material.icons.filled.FormatColorFill
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.mateusrodcosta.apps.vidyamusic.features.settings.SettingsViewModel
import com.mateusrodcosta.apps.vidyamusic.features.settings.components.ThemeSelectionDialog
import com.mateusrodcosta.apps.vidyamusic.features.shared.components.SectionHeader
import com.mateusrodcosta.apps.vidyamusic.features.shared.utils.uiInfo
import com.mateusrodcosta.apps.vidyamusic.features.settings.R as RSettings
import com.mateusrodcosta.apps.vidyamusic.features.shared.R as RShared

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    onNavigateToAbout: () -> Unit,
    onBackClick: () -> Unit
) {
    val currentTheme by viewModel.currentTheme.collectAsState()
    val useDynamicColor by viewModel.useDynamicColor.collectAsState()
    val skipIntro by viewModel.skipPlaylistIntro.collectAsState()

    val scrollState = rememberScrollState()
    var showThemeDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(RSettings.string.screen_settings_title)) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(RShared.string.description_icon_back)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    scrolledContainerColor = Color.Transparent
                )
            )
        }
    ) { innerPadding ->
        val listItemColors = ListItemDefaults.colors(
            containerColor = Color.Transparent
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                modifier = Modifier
                    .widthIn(max = 800.dp)
                    .verticalScroll(scrollState)
                    .fillMaxWidth()
            ) {
                SectionHeader(text = stringResource(RSettings.string.settings_appearance_section_header))
                ListItem(
                    headlineContent = {
                        Text(stringResource(RSettings.string.settings_appearance_theme_selector_title))
                    },
                    supportingContent = {
                        Text(currentTheme.uiInfo.text)
                    },
                    leadingContent = {
                        Icon(
                            Icons.Default.Palette,
                            contentDescription = stringResource(RSettings.string.description_icon_theme)
                        )
                    },
                    colors = listItemColors,
                    modifier = Modifier.clickable { showThemeDialog = true }
                )
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    ListItem(
                        headlineContent = {
                            Text(stringResource(RSettings.string.settings_appearance_dynamic_colors_title))
                        },
                        supportingContent = {
                            Text(stringResource(RSettings.string.settings_appearance_dynamic_colors_subtitle))
                        },
                        leadingContent = {
                            Icon(
                                Icons.Default.FormatColorFill,
                                contentDescription = stringResource(RSettings.string.description_icon_dynamic_colors)
                            )
                        },
                        trailingContent = {
                            Switch(
                                checked = useDynamicColor,
                                onCheckedChange = null
                            )
                        },
                        colors = listItemColors,
                        modifier = Modifier.toggleable(
                            value = useDynamicColor,
                            onValueChange = { viewModel.updateDynamicColor(it) },
                            role = Role.Switch
                        )
                    )
                }

                SectionHeader(stringResource(RSettings.string.settings_playback_section_header))
                ListItem(
                    headlineContent = {
                        Text(stringResource(RSettings.string.settings_playback_skip_intro_title))
                    },
                    supportingContent = {
                        Text(stringResource(RSettings.string.settings_playback_skip_intro_subtitle))
                    },
                    leadingContent = {
                        Icon(
                            Icons.Default.SkipNext,
                            contentDescription = stringResource(RSettings.string.description_icon_skip_intro)
                        )
                    },
                    trailingContent = {
                        Switch(
                            checked = skipIntro,
                            onCheckedChange = null
                        )
                    },
                    colors = listItemColors,
                    modifier = Modifier.toggleable(
                        value = skipIntro,
                        onValueChange = { viewModel.updateSkipPlaylistIntro(it) },
                        role = Role.Switch
                    )
                )

                SectionHeader(stringResource(RSettings.string.settings_about_section_header))
                ListItem(
                    headlineContent = {
                        Text(stringResource(RSettings.string.settings_about_about_title))
                    },
                    leadingContent = {
                        Icon(
                            Icons.AutoMirrored.Filled.HelpOutline,
                            contentDescription = stringResource(RSettings.string.description_icon_about)
                        )
                    },
                    colors = listItemColors,
                    modifier = Modifier.clickable(onClick = onNavigateToAbout)
                )
            }
        }
    }

    if (showThemeDialog) {
        ThemeSelectionDialog(
            currentTheme = currentTheme,
            onDismiss = { showThemeDialog = false },
            onThemeSelected = { selectedMode ->
                viewModel.updateTheme(selectedMode)
                showThemeDialog = false
            }
        )
    }
}