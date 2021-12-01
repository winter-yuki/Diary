package diary.spaces

import androidx.compose.runtime.Composable
import diary.spaces.notes.TextCell
import diary.ui.UIElem
import java.nio.file.Path

class Pdf : UIElem {
    @Composable
    override fun invoke() {
        TextCell("I'm PDF!")
        // TODO
    }

    companion object {
        fun from(path: Path): Pdf = TODO()
    }
}
