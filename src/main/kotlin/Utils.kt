import androidx.compose.runtime.Composable
import java.awt.FileDialog
import java.awt.Frame
import java.nio.file.Path
import kotlin.io.path.extension

interface UIElem {
    @Composable
    operator fun invoke()
}

@Composable
fun <T> Iterable<T>.forEachIndexedCo(block: @Composable (Int, T) -> Unit) {
    for ((i, t) in this.withIndex()) {
        block(i, t)
    }
}

@Composable
fun <T> Iterable<T>.forEachCo(block: @Composable (T) -> Unit) {
    forEachIndexedCo { _, t ->
        block(t)
    }
}

@Composable
fun <T> forEachCo(vararg ts: T, block: @Composable (T) -> Unit) {
    for (t in ts) {
        block(t)
    }
}

typealias FileDialogMode = Int

fun callFileExplorer(title: String, mode: FileDialogMode = FileDialog.LOAD): Path =
    FileDialog(Frame(), title).apply {
        this.mode = mode
        isVisible = true
    }.run {
        Path.of(directory, file)
    }

enum class FileType {
    Unknown, Diary, Pdf;

    companion object {
        fun of(path: Path): FileType = when (path.extension) {
            "diary" -> Diary
            "pdf" -> Pdf
            else -> Unknown
        }
    }
}
