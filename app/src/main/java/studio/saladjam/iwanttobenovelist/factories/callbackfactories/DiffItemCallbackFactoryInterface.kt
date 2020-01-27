package studio.saladjam.iwanttobenovelist.factories.callbackfactories

import androidx.recyclerview.widget.DiffUtil

interface DiffItemCallbackFactoryInterface {
    fun <T : DiffUtil.ItemCallback<R>?, R:Any> create(modelClass: Class<R>): T
}