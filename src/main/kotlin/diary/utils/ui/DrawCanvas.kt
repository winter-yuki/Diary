package diary.utils.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp

@Composable
fun DrawCanvas(
    pathState: MutableState<Path>,
    modifier: Modifier = Modifier,
    bitmap: ImageBitmap? = null
) {
    val path by pathState
    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }
    // It is better to replace with path mutable state
    var action by mutableStateOf<Offset?>(null)
    Canvas(
        modifier = modifier
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = {
                        offsetX = it.x
                        offsetY = it.y
                        path.moveTo(it.x, it.y)
                    }
                ) { _, dragAmount ->
                    offsetX += dragAmount.x
                    offsetY += dragAmount.y
                    path.lineTo(offsetX, offsetY)
                    action = Offset(offsetX, offsetY)
                }
            }
    ) {
        bitmap?.let { drawImage(it) }
        drawPath(path)
        action?.let {
            drawPath(path)
        }
    }
}

private fun DrawScope.drawPath(path: Path) {
    drawPath(
        path = path,
        color = Color(0xFF37596D),
        alpha = 1f,
        style = Stroke(3f)
    )
}

fun Modifier.chooseSize(bitmap: ImageBitmap?): Modifier =
    bitmap?.run { size(width = width.dp, height = height.dp) } ?: this
