package diary.ui.tabs

import androidx.compose.runtime.Composable
import diary.ui.Link
import java.nio.file.Path

interface Tab {

    // TODO fix
    data class Id(val id: Path)

    val id: Id

    fun navigate(link: Link)

    @Composable
    operator fun invoke()
}
