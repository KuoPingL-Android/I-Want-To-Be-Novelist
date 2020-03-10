package studio.saladjam.iwanttobenovelist.editorscene

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import studio.saladjam.iwanttobenovelist.IWBNApplication
import studio.saladjam.iwanttobenovelist.Logger
import studio.saladjam.iwanttobenovelist.MainViewModel
import studio.saladjam.iwanttobenovelist.R
import studio.saladjam.iwanttobenovelist.constants.IntentConstants
import studio.saladjam.iwanttobenovelist.constants.NavArgKeys
import studio.saladjam.iwanttobenovelist.databinding.FragmentScrollableEditorBinding
import studio.saladjam.iwanttobenovelist.editorscene.viewmodel.ScrollableEditorViewModel
import studio.saladjam.iwanttobenovelist.extensions.getBitmap
import studio.saladjam.iwanttobenovelist.extensions.getVMFactory
import studio.saladjam.iwanttobenovelist.repository.dataclass.Chapter

class ScrollableEditorFragment : Fragment() {

    private lateinit var binding: FragmentScrollableEditorBinding
    private var chapter: Chapter? = null
    private var mainViewModel: MainViewModel? = null
    private val viewModel by viewModels<ScrollableEditorViewModel> { getVMFactory() }

    companion object {
        private const val PICK_IMAGE_REQUEST = 1
        private const val BITMAP_LIMIT_RATIO = 3
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentScrollableEditorBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        mainViewModel = ViewModelProviders.of(activity!!).get(MainViewModel::class.java)

        binding.simpleContainer.parentScrollView = binding.scrollviewEditor
//        binding.simpleContainer.parentScrollView?.setOnTouchListener { v, event ->
//            Logger.d("OnDragListener event=$event")
//            false
//        }

        chapter = requireArguments().get(NavArgKeys.CHAPTER) as Chapter
        chapter?.let {
            viewModel.prepareChapter(it)
            if (it.chapterID.isNotEmpty()) {
                binding.simpleContainer.setContentWithPaint(it, binding.editEditorMix.paint)
            }
        }

        /** SAVE CHAPTERS */
        viewModel.shouldSaveChapter.observe(viewLifecycleOwner, Observer {
            it?.let {
                val (bitmapsMap, coordinators) = binding.simpleContainer.getDetails()
                viewModel.doneSaveChapter()
                chapter?.let {chapter ->
                    viewModel.saveChapterDetails(chapter, bitmapsMap, coordinators)
                }
            }
        })


        /** IMAGES */
        viewModel.imageDeleteEnabled.observe(viewLifecycleOwner, Observer {
            it?.let {
                if(it) {
                    binding.simpleContainer.enableDeletion()
                    binding.textEditorDelete.setTextColor(
                        IWBNApplication.context.getColor(R.color.editor_btn_add_image))
                    binding.textEditorDelete.text =
                        IWBNApplication.context.getText(R.string.editor_btn_add_images)
                } else {
                    binding.simpleContainer.disableDeletion()
                    binding.textEditorDelete.setTextColor(
                        IWBNApplication.context.getColor(android.R.color.holo_red_light))
                    binding.textEditorDelete.text =
                        IWBNApplication.context.getText(R.string.editor_btn_remove_image)
                }
                viewModel.doneTriggeringImageDeletion()
            }
        })

        viewModel.shouldAddImage.observe(viewLifecycleOwner, Observer {
            it?.let {
                displayImagePicker()
                viewModel.doneAddingImage()
            }
        })


        viewModel.isCurrentlyEditText.observe(viewLifecycleOwner, Observer {
            it?.let {currentlyEditingText ->
                if (currentlyEditingText) {
                    chapter?.let {
                        binding.editEditor.setText(it.text)
                    }
                    binding.fabEditorImage.hide()
                } else {
                    chapter?.let {
                        binding.simpleContainer.setContentWithPaint(it, binding.editEditorMix.paint)
                    }

                    binding.fabEditorImage.show()
                }
            }
        })

        viewModel.dialogInfo.observe(viewLifecycleOwner, Observer {
            it?.let {

                mainViewModel?.displayLoadingDialog(it.first, it.second)
                viewModel.doneDisplayingDialog()
            }
        })

        viewModel.isDone.observe(viewLifecycleOwner, Observer {
            it?.let {
                findNavController().navigateUp()
                viewModel.donePressingIsDone()
            }
        })

        viewModel.currentChapterBlock.observe(viewLifecycleOwner, Observer {
            it?.let {
                binding.simpleContainer.addBlocks(it, true)
            }
        })

        return binding.root
    }

    private fun displayImagePicker() {
        val intent = Intent()
        intent.type = IntentConstants.IMAGE_TYPE
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(
            Intent.createChooser(intent, getString(R.string.select_picture)),
            PICK_IMAGE_REQUEST
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST &&
            resultCode == Activity.RESULT_OK &&
            data != null && data.data != null) {

            val bitmap = data.data!!
                .getBitmap(
                    binding.simpleContainer.width / BITMAP_LIMIT_RATIO,
                    binding.simpleContainer.width / BITMAP_LIMIT_RATIO
                )

            val imageView = EditorImageBlock(context!!)
            val layoutParams = ViewGroup.
                LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT)
            imageView.layoutParams = layoutParams
            imageView.setImageBitmap(bitmap)
            imageView.isClickable = true
            binding.simpleContainer.addView(imageView)
        }
    }
}