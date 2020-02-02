package studio.saladjam.iwanttobenovelist.categoryscene.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import studio.saladjam.iwanttobenovelist.categoryscene.CategoryListFragment
import studio.saladjam.iwanttobenovelist.repository.dataclass.Genre

class CategoryGenreViewPagerAdapter(val fragmentManager: FragmentManager) :
    FragmentStatePagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private var genres: List<Genre> = listOf()

    fun setGenres(genres: List<Genre>) {
        if (this.genres.isEmpty()) {
            this.genres = genres
            notifyDataSetChanged()
        }
    }
    //TODO: IMPLEMENT FILTER
    fun setFilter() {

    }

    override fun getItem(position: Int): Fragment {
        val genre = genres.get(position)
        return CategoryListFragment(genre)
    }

    override fun getCount(): Int {
        return genres.count()
    }

    override fun getPageTitle(position: Int): CharSequence? {
        //TODO: RETURN TITLE BASED ON LANGUAGE
        return genres.get(position).zh
    }
}