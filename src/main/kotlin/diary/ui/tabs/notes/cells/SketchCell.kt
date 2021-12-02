package diary.ui.tabs.notes.cells

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import java.nio.file.Path

class SketchCell(
    override var name: CellName = CellName(""),
    path: MutableState<androidx.compose.ui.graphics.Path> =
        mutableStateOf(androidx.compose.ui.graphics.Path())
) : AbstractCell() {

    private val pathState = path
    private val path by path

    override fun save(path: Path) {
        println("Save sketch $path") // TODO
    }

    @Composable
    override operator fun invoke() = cell {
        var offsetX by remember { mutableStateOf(0f) }
        var offsetY by remember { mutableStateOf(0f) }
        // It is better to replace with path mutable state
        var action by mutableStateOf<Offset?>(null)
        Canvas(
            modifier = Modifier
                .height(250.dp)
                .fillMaxWidth()
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
            drawPath()
            action?.let {
                drawPath()
            }
        }
    }

    fun DrawScope.drawPath() {
        drawPath(
            path = path,
            color = Color(0xFF37596D),
            alpha = 1f,
            style = Stroke(3f)
        )
    }
}