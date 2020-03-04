package studio.saladjam.iwanttobenovelist.homescene.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
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
class HomeRecyclerAdpaterV1(val viewModel: HomeViewModel, val workInProgressViewModel: HomeWorkInProgressViewModel) : ListAdapter<HomeSealItems, RecyclerView.ViewHolder>
    (CallbackFactory().create(HomeSealItems::class.java)) {

    companion object {
        const val GENERAL = 0
        const val CURRENT_READ = 1
        const val WORK_IN_PROGRESS = 2
        const val RECOMMEND = 3
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
                    workInProgressViewModel
                )
            }

            RECOMMEND -> {
                HomeRecommendViewHolder(ItemHomeV1RecommendlistBinding
                    .inflate(LayoutInflater.from(parent.context), parent, false))
            }

            else -> {
                throw IllegalArgumentException("HomeRecyclerAdpaterV1 : UNKNOWN VIEW TYPE")
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder) {
            is HomeGeneralViewHolder ->
                    {holder.bind(getItem(position) as HomeSealItems.General,
                        viewModel, HomeSections.POPULAR)}
            is HomeCurrentReadViewHolder ->
                    {holder.bind(getItem(position) as HomeSealItems.CurrentReading,
                        viewModel,
                        HomeSections.CURRENTREAD)}
            is HomeWorkInProgressViewHolder ->
                    {holder.bind(getItem(position) as HomeSealItems.WorkInProgress,
                        viewModel,
                        HomeSections.WORKINPROGRESS)}
            is HomeRecommendViewHolder -> {
                holder.bind(getItem(position) as HomeSealItems.Recommend,
                    viewModel,HomeSections.RECOMMEND)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when(getItem(position)) {
            is HomeSealItems.WorkInProgress -> WORK_IN_PROGRESS

            is HomeSealItems.CurrentReading -> CURRENT_READ

            is HomeSealItems.General -> GENERAL

            is HomeSealItems.Recommend -> RECOMMEND
        }
    }



}