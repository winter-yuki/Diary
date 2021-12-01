package diary.ui.tabs

import diary.ui.Link
import diary.ui.UIComponent
import java.nio.file.Path

interface Tab : UIComponent {

    data class Id(val id: Path)

    val id: Id

    fun navigate(link: Link)
}
