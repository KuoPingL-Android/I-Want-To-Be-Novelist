package studio.saladjam.iwanttobenovelist.binding

import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import studio.saladjam.iwanttobenovelist.IWBNApplication
import studio.saladjam.iwanttobenovelist.R
import studio.saladjam.iwanttobenovelist.repository.dataclass.Roles
import java.util.*
import java.util.concurrent.TimeUnit


/** IMAGE URL */
@BindingAdapter("fireStorageImageUrl")
fun bind(imageView: ImageView, imageUrl: String?) {
    imageUrl?.let {url ->
        val storage = IWBNApplication.container.getStorageInstance(url)
        Glide.with(imageView.context)
            .load(storage)
            .placeholder(R.drawable.placeholder_icon)
            .into(imageView)
    }
}

/** DATE */
@BindingAdapter("daysSinceLastUpdate")
fun bindDates(textView: TextView, lastUpdatedTime: Long) {
    val currentTime = Calendar.getInstance().timeInMillis
    val (value, unit) = getLastUpdatedTime(currentTime - lastUpdatedTime)

    textView.text = IWBNApplication.context
        .getString(
            R.string.time_before,
            value, unit, IWBNApplication.context.getString(
                R.string.before
            ))
}

fun getLastUpdatedTime(diff: Long): Pair<Int, String> {
    val seconds = TimeUnit.SECONDS.convert(diff, TimeUnit.MILLISECONDS)
    val minutes = TimeUnit.MINUTES.convert(diff, TimeUnit.MILLISECONDS)
    val hours   = TimeUnit.HOURS.convert(diff, TimeUnit.MILLISECONDS)
    var days    = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)
    val years   = days / 365
    days -= years * 365

    val unit: String
    val value: Int

    when {
        years.toInt() > 0 -> {
            value = years.toInt()
            unit = IWBNApplication.context.getString(
                R.string.unit_year
            )
        }
        days.toInt() > 0 -> {
            value = days.toInt()
            unit = IWBNApplication.context.getString(
                R.string.unit_day
            )
        }
        hours.toInt() > 0 -> {
            value = hours.toInt()
            unit = IWBNApplication.context.getString(
                R.string.unit_hour
            )
        }
        minutes.toInt() > 0 -> {
            value = minutes.toInt()
            unit = IWBNApplication.context.getString(
                R.string.unit_minutes
            )
        }
        else -> {
            value = seconds.toInt()
            unit = IWBNApplication.context.getString(
                R.string.unit_second
            )
        }
    }

    return Pair(value, unit)
}



/** HOME BOOK ITEM LAYOUTPARAMS */
@BindingAdapter("layoutHomeMainLayout")
fun bindLayoutParams(viewGroup: ViewGroup, isLinearLayout: Boolean) {
    viewGroup.layoutParams =
        if (isLinearLayout) {
            ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT)
        } else {
            ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        }
}

@BindingAdapter("layoutHomeInnerLayout")
fun bindInnerLayoutParams(viewGroup: ViewGroup, isLinearLayout: Boolean) {
    viewGroup.layoutParams =
        if (isLinearLayout) {
            viewGroup.layoutParams
        } else {
            ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        }
}

/** PROFILE */
@BindingAdapter("userRole")
fun bindTextView(textView: TextView, role: Int?) {
    role?.let {
        when(it) {
            Roles.REVIEWER.value -> {
                textView.text = IWBNApplication.context.resources.getText(
                    R.string.user_role_reviewer
                )
            }

            Roles.WRITER.value -> {
                textView.text = IWBNApplication.context.resources.getText(
                    R.string.user_role_writer
                )
            }
        }
    }
}

@BindingAdapter("booksNumber")
fun bindTextViewToBookCount(textView: TextView, books: Int?) {
    textView.text = if (books == null) "0" else "${books}"
}

@BindingAdapter("word", "targetCount")
fun bindTextViewToWordCount(textView: TextView, word: String?, targetCount: Int) {
    var wordCount = word?.length ?: 0

    if (wordCount >= targetCount) wordCount = targetCount

    textView.text = IWBNApplication.context.getString(
        R.string.editor_manage_word_count, wordCount, targetCount)
}