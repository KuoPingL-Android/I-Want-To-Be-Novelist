package studio.saladjam.iwanttobenovelist.profilescene.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import studio.saladjam.iwanttobenovelist.profilescene.ProfileViewModel

class ProfileFragmentStateAdapter (frag: Fragment, private val viewModel: ProfileViewModel): FragmentStateAdapter(frag) {

    private var _fragments = listOf<Fragment>()

    fun setFragments(fragments: List<Fragment>) {
        _fragments = fragments
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return _fragments.count()
    }

    override fun createFragment(position: Int): Fragment {
        return _fragments[position]
    }

}