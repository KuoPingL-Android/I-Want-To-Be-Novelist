package studio.saladjam.iwanttobenovelist.editorscene

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import studio.saladjam.iwanttobenovelist.databinding.FragmentEditorMixerV1Binding
import studio.saladjam.iwanttobenovelist.repository.dataclass.Chapter

class EditorMixerV1Fragment : Fragment() {

    private lateinit var binding: FragmentEditorMixerV1Binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEditorMixerV1Binding.inflate(inflater)

        val chapter = requireArguments().get(EditorKeys.NAV_CHAPTER_KEY) as Chapter

        binding.mixerEditorMixerV1Editor.setText(chapter.content)

        return binding.root
    }

}