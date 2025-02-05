package ru.tech.imageresizershrinker.presentation.main_screen.components.settings

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Brightness4
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.tech.imageresizershrinker.R
import ru.tech.imageresizershrinker.presentation.root.widget.preferences.PreferenceRowSwitch
import ru.tech.imageresizershrinker.presentation.root.widget.utils.LocalSettingsState

@Composable
fun AmoledModeSettingItem(
    onClick: (Boolean) -> Unit,
    modifier: Modifier = Modifier.padding(
        start = 8.dp,
        end = 8.dp
    ),
    shape: Shape = SettingsShapeDefaults.centerShape
) {
    val settingsState = LocalSettingsState.current
    PreferenceRowSwitch(
        modifier = modifier,
        shape = shape,
        startContent = {
            Icon(
                imageVector = Icons.Outlined.Brightness4,
                contentDescription = null,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        },
        resultModifier = Modifier.padding(
            end = 16.dp,
            top = 8.dp,
            bottom = 8.dp
        ),
        applyHorPadding = false,
        title = stringResource(R.string.amoled_mode),
        subtitle = stringResource(R.string.amoled_mode_sub),
        checked = settingsState.isAmoledMode,
        onClick = onClick
    )
}