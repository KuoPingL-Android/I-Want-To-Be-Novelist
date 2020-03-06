package studio.saladjam.iwanttobenovelist.homescene.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import studio.saladjam.iwanttobenovelist.IWBNApplication
import studio.saladjam.iwanttobenovelist.R
import studio.saladjam.iwanttobenovelist.databinding.ItemHomeV1Binding
import studio.saladjam.iwanttobenovelist.databinding.ItemHomeV1RecommendlistBinding
import studio.saladjam.iwanttobenovelist.factories.callbackfactories.CallbackFactory
import studio.saladjam.iwanttobenovelist.homescene.HomeSections
import studio.saladjam.iwanttobenovelist.homescene.HomeViewModel
import studio.saladjam.iwanttobenovelist.homescene.HomeWorkInProgressViewModel
import studio.saladjam.iwanttobenovelist.homescene.sealitems.HomeSealItems
import studio.saladjam.iwanttobenovelist.homescene.viewholders.HomeCurrentReadViewHolder
import studio.saladjam.iwanttobenovelist.homescene.viewholders.HomeGeneralViewHolder
import studio.saladjam.iwanttobenovelist.homescene.viewholders.HomeWorkInProgressViewHolder
import java.lang.IllegalArgumentException

/** THIS IS the MAIN ADAPTER for HOME PAGE */
class HomeRecyclerAdapterV1(val viewModel: HomeViewModel,
                            private  val workInProgressViewModel: HomeWorkInProgressViewModel) :
    ListAdapter<HomeSealItems, RecyclerView.ViewHolder>(
        CallbackFactory().create(HomeSealItems::class.java)) {

    companion object {
        private val GENERAL             = HomeSections.GENERAL.value
        private val RECOMMEND           = HomeSections.RECOMMEND.value
        private val CURRENT_READ        = HomeSections.CURRENT_READING.value
        private val WORK_IN_PROGRESS    = HomeSections.WORK_IN_PROGRESS.value
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            GENERAL -> {
                HomeGeneralViewHolder(
                    ItemHomeV1Binding
                        .inflate(LayoutInflater.from(parent.context), parent, false)
                )
            }

            CURRENT_READ -> {
                HomeCurrentReadViewHolder(
                    ItemHomeV1Binding
                        .inflate(LayoutInflater.from(parent.context), parent, false)
                )
            }

            WORK_IN_PROGRESS -> {
                HomeWorkInProgressViewHolder(
                    ItemHomeV1Binding
                        .inflate(LayoutInflater.from(parent.context), parent, false),
                    workInProgressViewModel)
            }

            RECOMMEND -> {
                HomeRecommendViewHolder(ItemHomeV1RecommendlistBinding
                    .inflate(LayoutInflater.from(parent.context), parent, false))
            }

            else -> {
                throw IllegalArgumentException(
                    IWBNApplication.instance.
                        getString(R.string.exception_unrecognized_viewtype,
                            this::class.java, viewType))
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder) {
            is HomeGeneralViewHolder -> {
                holder.bind(
                    getItem(position) as HomeSealItems.General,
                    viewModel,
                    HomeSections.POPULAR)}
            is HomeCurrentReadViewHolder -> {
                holder.bind(
                    getItem(position) as HomeSealItems.CurrentReading,
                    viewModel,
                    HomeSections.CURRENT_READING)}
            is HomeWorkInProgressViewHolder -> {
                holder.bind(
                    getItem(position) as HomeSealItems.WorkInProgress,
                    viewModel,
                    HomeSections.WORK_IN_PROGRESS)}
            is HomeRecommendViewHolder -> {
                holder.bind(
                    getItem(position) as HomeSealItems.Recommend,
                    viewModel,
                    HomeSections.RECOMMEND)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when(getItem(position)) {
            is HomeSealItems.WorkInProgress -> WORK_IN_PROGRESS

            is HomeSealItems.CurrentReading -> CURRENT_READ

            is HomeSealItems.Recommend -> RECOMMEND

            is HomeSealItems.General -> GENERAL
        }
    }



}