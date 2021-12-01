package diary.ui

import diary.ui.spaces.Space

interface Link {
    val id: Space.Id
    fun navigate(workSpace: WorkSpace)
}

abstract class AbstractLink : Link {
    protected fun navigateOrCreate(workSpace: WorkSpace, block: () -> Space) {
        val space = workSpace[id]
        if (space != null) {
            space.navigate(this)
        } else {
            val newSpace = block()
            workSpace.add(Tab(newSpace))
            newSpace.navigate(this)
        }
    }
}

class LinkBuffer(var link: Link? = null)
