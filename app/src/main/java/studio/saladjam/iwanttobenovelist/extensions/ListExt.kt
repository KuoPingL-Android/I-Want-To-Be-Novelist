package studio.saladjam.iwanttobenovelist.extensions

fun <E> MutableList<E>.moveToLast(element: E): Boolean {
    return if (this.contains(element)) {
        this.remove(element)
        this.add(this.size - 1, element)
        true
    } else {
        false
    }
}