package diary.utils.ui

import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.asSkiaPath
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.key.*
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalComposeUiApi::class, ExperimentalFoundationApi::class)
@Composable
fun DrawCanvas(
    pathState: MutableState<Path>,
    modifier: Modifier = Modifier,
    bitmap: ImageBitmap? = null
) {
    var path by pathState
    var iterablePath: MutableList<MutableList<Offset>> = mutableListOf()
    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }
    // It is better to replace with path mutable state
    var action by mutableStateOf<Int?>(null)

    Canvas(
        modifier = modifier
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = {
                        offsetX = it.x
                        offsetY = it.y
                        path.moveTo(it.x, it.y)
                        iterablePath.add(mutableListOf())
                    }
                ) { _, dragAmount ->
                    offsetX += dragAmount.x
                    offsetY += dragAmount.y
                    path.lineTo(offsetX, offsetY)

                    if (iterablePath.isEmpty() or iterablePath.last().isEmpty()){
                        iterablePath.last().add( dragAmount )
                    } else {
                        iterablePath.last().add(
                            Offset(
                                iterablePath.last().last().x + dragAmount.x,
                                iterablePath.last().last().y + dragAmount.y,
                            )
                        )
                    }

                    action = (action ?: 0) + 1 //Offset(offsetX, offsetY)
                }
            }
            .focusable()
            .onKeyEvent {
                when {
                    (it.isCtrlPressed && it.key == Key.Z && iterablePath.isNotEmpty()) -> { //} && it.key == Key.Z) -> {
                        if (iterablePath.isNotEmpty()) {
                            path = Path()
                            for (offsetList in iterablePath) {
                                path.moveTo(offsetList.first().x, offsetList.first().y)
                                for (offset in offsetList.subList(1, offsetList.lastIndex)) {
                                    path.lineTo(offset.x, offset.y)
                                }
                            }
                            action = (action ?: 0) + 1
                        }
                        true
                    }
                    else -> false
                }

            }
    ) {
        bitmap?.let { drawImage(it) }
        drawPath(path)
        action?.let {
            println(path.getBounds())
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
