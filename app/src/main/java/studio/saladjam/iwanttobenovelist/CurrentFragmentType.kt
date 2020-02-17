package studio.saladjam.iwanttobenovelist

import studio.saladjam.iwanttobenovelist.util.Util.getString

/**
 * <string name="page_profile">個人</string>
 * <string name="page_editor">編輯</string>
 * <string name="page_editor_modifier">最後編輯</string>
 * */

enum class CurrentFragmentType (val value: String) {
    LOGIN(getString(R.string.page_login)),
    HOME(getString(R.string.page_home)),
    CATEGORY(getString(R.string.page_category)),
    PROFILE(getString(R.string.page_profile)),
    EDITOR(getString(R.string.page_editor)),
    EDITORMODIFIER(getString(R.string.page_editor_modifier)),
    BOOKDETAIL(getString(R.string.page_book_detail)),
    READER(getString(R.string.page_reader))
}

