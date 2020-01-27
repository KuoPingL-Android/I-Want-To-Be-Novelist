package studio.saladjam.iwanttobenovelist.factories.callbackfactories

import androidx.recyclerview.widget.DiffUtil

@Suppress("UNCHECKED_CAST")
class CallbackFactory : DiffItemCallbackFactoryInterface {

    override fun <T : DiffUtil.ItemCallback<R>?, R:Any> create(modelClass: Class<R>): T {
        return with(modelClass) {
            when {
                isAssignableFrom(String::class.java) -> StringCallback()
                else -> IllegalArgumentException("UNKNOWN CLASS")
            }
        } as T
    }
}