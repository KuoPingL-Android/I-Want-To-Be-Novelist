package studio.saladjam.iwanttobenovelist.factories.viewmodelfactories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import studio.saladjam.iwanttobenovelist.bookdetailscene.BookDetailViewModel
import studio.saladjam.iwanttobenovelist.bookdetailscene.ChapterDetailViewModel
import studio.saladjam.iwanttobenovelist.categoryscene.CategoryListViewModel
import studio.saladjam.iwanttobenovelist.categoryscene.CategoryViewModel
import studio.saladjam.iwanttobenovelist.editorscene.EditorMixerV1ViewModel
import studio.saladjam.iwanttobenovelist.editorscene.EditorTextViewModel
import studio.saladjam.iwanttobenovelist.editorscene.EditorMixerViewModel
import studio.saladjam.iwanttobenovelist.editorscene.EditorViewModel
import studio.saladjam.iwanttobenovelist.homescene.HomeViewModel
import studio.saladjam.iwanttobenovelist.homescene.HomeWorkInProgressViewModel
import studio.saladjam.iwanttobenovelist.launchscene.LaunchViewModel
import studio.saladjam.iwanttobenovelist.loginscene.*
import studio.saladjam.iwanttobenovelist.profilescene.*
import studio.saladjam.iwanttobenovelist.repository.Repository
import studio.saladjam.iwanttobenovelist.searchscene.SearchViewModel
import java.lang.IllegalArgumentException

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(private val repository: Repository)
    : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return with(modelClass) {
            when {

                isAssignableFrom(LaunchViewModel::class.java)
                                    -> LaunchViewModel(repository)

                isAssignableFrom(LoginSigninupViewModel::class.java)
                                    -> LoginSigninupViewModel(repository)

                isAssignableFrom(LoginInterestViewModel::class.java)
                                    -> LoginInterestViewModel(repository)

                isAssignableFrom(HomeViewModel::class.java)
                                    -> HomeViewModel(repository)

                isAssignableFrom(CategoryViewModel::class.java)
                                    -> CategoryViewModel(repository)

                isAssignableFrom(CategoryListViewModel::class.java)
                                    -> CategoryListViewModel(repository)

                isAssignableFrom(ProfileViewModel::class.java)
                                    -> ProfileViewModel(repository)

                isAssignableFrom(EditorMixerViewModel::class.java)
                                    -> EditorMixerViewModel(repository)

                isAssignableFrom(ProfileBookReadingViewModel::class.java)
                                    -> ProfileBookReadingViewModel(repository)

                isAssignableFrom(ProfileWorkViewModel::class.java)
                                    -> ProfileWorkViewModel(repository)

                isAssignableFrom(ProfileCreateBookViewModel::class.java)
                                    -> ProfileCreateBookViewModel(repository)

                isAssignableFrom(BookDetailViewModel::class.java)
                                    -> BookDetailViewModel(repository)

                isAssignableFrom(ChapterDetailViewModel::class.java)
                                    -> ChapterDetailViewModel(repository)

                isAssignableFrom(EditorTextViewModel::class.java)
                                    -> EditorTextViewModel(repository)

                isAssignableFrom(EditorMixerV1ViewModel::class.java)
                                    -> EditorMixerV1ViewModel(repository)

                isAssignableFrom(EditorViewModel::class.java)
                                    -> EditorViewModel(repository)

                isAssignableFrom(HomeWorkInProgressViewModel::class.java)
                                    -> HomeWorkInProgressViewModel(repository)

                isAssignableFrom(SearchViewModel::class.java)
                                    -> SearchViewModel(repository)

                else -> {throw IllegalArgumentException("${modelClass} is not supported")}
            }
        } as T
    }
}