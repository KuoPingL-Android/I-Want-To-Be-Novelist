package studio.saladjam.iwanttobenovelist.extensions

import android.icu.lang.UCharacter
import android.util.Log
import studio.saladjam.iwanttobenovelist.Logger


fun Char.isBasicLatin(): Boolean {
    return Character.UnicodeBlock.of(this) == Character.UnicodeBlock.BASIC_LATIN
}
