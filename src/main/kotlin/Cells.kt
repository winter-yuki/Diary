import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import java.nio.file.Path

interface Cell {
    fun save(path: Path)

    fun load(path: Path): Cell

    @Composable
    operator fun invoke(notes: Notes)

    @Composable
    fun cell(notes: Notes, block: @Composable () -> Unit) {
        // TODO border rectangle #5
        // TODO remove cell button #2
        println("width")
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Surface(shape = MaterialTheme.shapes.large, elevation = 2.dp) {
                block()
            }
        }
    }

    companion object {
        fun of(path: Path): Cell {
            TODO()
        }
    }
}

class TextCell : Cell {
    override fun save(path: Path) {
        TODO("Not yet implemented") // #7
    }

    override fun load(path: Path): TextCell {
        TODO("Not yet implemented") // #7
    }

    @Composable
    override operator fun invoke(notes: Notes) = cell(notes) {
        val text = remember { mutableStateOf("") }
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .padding(vertical = 10.dp),
            value = text.value,
            singleLine = false,
            onValueChange = { text.value = it }
        )
    }
}

class SketchCell : Cell {
    override fun save(path: Path) {
        TODO("Not yet implemented") // #8
    }

    override fun load(path: Path): SketchCell {
        TODO("Not yet implemented") // #8
    }

    @Composable
    override operator fun invoke(notes: Notes) = cell(notes) {
        val offsetX = remember { mutableStateOf(0f) }
        val offsetY = remember { mutableStateOf(0f) }
        val action = mutableStateOf<Offset?>(null)
        val path = androidx.compose.ui.graphics.Path()
        Canvas(
            modifier = Modifier
                .height(250.dp)
                .fillMaxWidth()
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragStart = {
                            offsetX.value = it.x
                            offsetY.value = it.y
                            path.moveTo(it.x, it.y)
                        }
                    ) { _, dragAmount ->
                        offsetX.value += dragAmount.x
                        offsetY.value += dragAmount.y
                        path.lineTo(offsetX.value, offsetY.value)
                        action.value = Offset(offsetX.value, offsetY.value)
                    }
                }
        ) {
            action.value?.let {
                drawPath(
                    path = path,
                    color = Color.Magenta,
                    alpha = 1f,
                    style = Stroke(3f)
                )
            }
        }

        // TODO resize cell bar appearing on focus #9
        // TODO clickable link: click -> jump #11
    }
}
