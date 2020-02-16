package studio.saladjam.iwanttobenovelist.editorscene

import android.content.Context
import android.view.View

class EditorMixerV1Canvas (context: Context, text: String): View(context) {
    private var mText: String = text

    fun setText(text: String) {
        mText = text
    }



}