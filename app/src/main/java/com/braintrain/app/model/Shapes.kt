package com.braintrain.app.model

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

enum class ShapeName(val fill: Color, val bg: Color, val label: String) {
    SQUARE(Color(0xFFFF6B6B), Color(0xFFFFE8E8), "Square"),
    CIRCLE(Color(0xFFFFD93D), Color(0xFFFFF8D9), "Circle"),
    TRIANGLE(Color(0xFF6BCB77), Color(0xFFE8F8EA), "Triangle"),
    PENTAGON(Color(0xFF4D96FF), Color(0xFFE2EEFF), "Pentagon"),
    STAR(Color(0xFFFF922B), Color(0xFFFFEEDD), "Star"),
}

/**
 * Draws one of the shapes on a Canvas
 */
@Composable
fun ShapeIcon(
    name: ShapeName,
    modifier: Modifier = Modifier,
    size: Dp = 60.dp,
    color: Color? = null,
) {
    val fillColor = color ?: name.fill
    Canvas(modifier = modifier.size(size)) {
        val w = this.size.width
        val h = this.size.height
        val cx = w / 2f
        val cy = h / 2f
        val r = w * 0.43f

        when (name) {
            ShapeName.SQUARE -> {
                val pad = w * 0.11f
                drawRoundRect(
                    color = fillColor,
                    topLeft = Offset(pad, pad),
                    size = Size(w - pad * 2, h - pad * 2),
                    cornerRadius = CornerRadius(w * 0.1f, w * 0.1f),
                )
            }
            ShapeName.CIRCLE -> {
                drawCircle(color = fillColor, radius = r, center = Offset(cx, cy))
            }
            ShapeName.TRIANGLE -> {
                val path = Path().apply {
                    moveTo(cx, h * 0.09f)
                    lineTo(w * 0.91f, h * 0.89f)
                    lineTo(w * 0.09f, h * 0.89f)
                    close()
                }
                drawPath(path, color = fillColor)
            }
            ShapeName.PENTAGON -> {
                drawPath(regularPolygonPath(cx, cy, r, sides = 5), color = fillColor)
            }
            ShapeName.STAR -> {
                drawPath(starPath(cx, cy, outerR = r, innerR = r * 0.42f), color = fillColor)
            }
        }
    }
}

private fun regularPolygonPath(cx: Float, cy: Float, r: Float, sides: Int): Path {
    val path = Path()
    for (i in 0 until sides) {
        val angle = (i * 2.0 * PI / sides - PI / 2.0).toFloat()
        val x = cx + r * cos(angle)
        val y = cy + r * sin(angle)
        if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
    }
    path.close()
    return path
}

private fun starPath(cx: Float, cy: Float, outerR: Float, innerR: Float): Path {
    val path = Path()
    for (i in 0 until 10) {
        val angle = (i * PI / 5.0 - PI / 2.0).toFloat()
        val rad = if (i % 2 == 0) outerR else innerR
        val x = cx + rad * cos(angle)
        val y = cy + rad * sin(angle)
        if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
    }
    path.close()
    return path
}
