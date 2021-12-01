package diary.spaces

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import diary.ui.UIElem
import java.nio.file.Path

class Pdf : UIElem {
    @Composable
    override fun invoke() {
        Box(modifier = Modifier.fillMaxSize()) {
            Text("I'm PDF!", modifier = Modifier.align(Alignment.Center))
        }
        // TODO
    }

    companion object {
        fun from(path: Path): Pdf = Pdf() // TODO
    }
}
