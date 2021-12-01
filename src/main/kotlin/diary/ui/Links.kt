package diary.ui

interface Link {
    fun jump(workSpace: WorkSpace)
}

class LinkBuffer(var link: Link? = null)
