package ru.tech.imageresizershrinker.presentation.root.widget.color_picker

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import com.smarttoolfactory.colordetector.util.ColorUtil
import com.smarttoolfactory.colorpicker.selector.SelectorRectSaturationValueHSV
import com.smarttoolfactory.colorpicker.slider.SliderHueHSV
import ru.tech.imageresizershrinker.presentation.root.widget.modifier.container

@Composable
fun ColorSelection(
    color: Int,
    onColorChange: (Int) -> Unit,
) {
    val color1 = Color(color)
    val hsv = ColorUtil.colorToHSV(color1)
    var hue by remember { mutableFloatStateOf(hsv[0]) }
    val saturation = hsv[1]
    val value = hsv[2]

    Column {
        ColorInfo(
            color = color1.copy(1f).toArgb(),
            onColorChange = onColorChange,
        )
        Spacer(Modifier.height(16.dp))
        SelectorRectSaturationValueHSV(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(4 / 3f)
                .container(RoundedCornerShape(2.dp), resultPadding = 0.dp)
                .clip(RoundedCornerShape(3.dp)),
            hue = hue,
            saturation = saturation,
            value = value
        ) { s, v ->
            onColorChange(
                Color.hsv(hue, s, v).toArgb()
            )
        }
        Spacer(Modifier.height(16.dp))
        SliderHueHSV(
            hue = hue,
            saturation = saturation,
            value = value,
            onValueChange = { h ->
                hue = h
                onColorChange(
                    Color.hsv(hue, saturation, value).toArgb()
                )
            },
            trackHeight = 16.dp,
            modifier = Modifier
                .container(
                    shape = CircleShape,
                    resultPadding = 0.dp,
                    color = Color.Transparent,
                    composeColorOnTopOfBackground = false,
                    clip = false,
                    isShadowClip = true
                )
                .padding(horizontal = 10.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        LaunchedEffect(color1) {
            if (hue != hsv[0]) hue = hsv[0]
        }
    }
}