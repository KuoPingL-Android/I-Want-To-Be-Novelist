package studio.saladjam.iwanttobenovelist.editorscene

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.provider.MediaStore
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toDrawable
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import studio.saladjam.iwanttobenovelist.IWBNApplication
import studio.saladjam.iwanttobenovelist.Logger
import studio.saladjam.iwanttobenovelist.R
import studio.saladjam.iwanttobenovelist.bind
import studio.saladjam.iwanttobenovelist.databinding.FragmentEditorMixerBinding
import studio.saladjam.iwanttobenovelist.databinding.FragmentEditorMixerV1Binding
import studio.saladjam.iwanttobenovelist.editorscene.utils.EditorImageBlockTouchListenerImpl
import studio.saladjam.iwanttobenovelist.editorscene.utils.TouchListenerImpl
import studio.saladjam.iwanttobenovelist.extensions.getBitmap
import studio.saladjam.iwanttobenovelist.extensions.getVMFactory
import studio.saladjam.iwanttobenovelist.extensions.toPx
import studio.saladjam.iwanttobenovelist.repository.dataclass.Chapter

class EditorMixerFragment: Fragment() {

    private lateinit var binding: FragmentEditorMixerBinding

    private val viewModel by viewModels<EditorMixerViewModel> { getVMFactory() }

    private var chapter: Chapter? = null

    companion object {
        private const val PICK_IMAGE_REQUEST = 1
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEditorMixerBinding.inflate(inflater)
        binding.lifecycleOwner = this

        chapter = requireArguments().get("chapter") as Chapter

        chapter?.let {
            binding.simpleContainer.setContentWithPaint(it, binding.editEditorMix.paint)
        }

        binding.viewModel = viewModel


        viewModel.shouldAddImage.observe(viewLifecycleOwner, Observer {
            it?.let {
                displayImagePicker()
                viewModel.doneAddingImage()
            }
        })

        viewModel.shouldGoBackToPreviousPage.observe(viewLifecycleOwner, Observer {
            it?.let {
                findNavController().navigateUp()
                viewModel.doneNavigatingToPreviousPage()
            }
        })

        viewModel.imageDeleteEnabled.observe(viewLifecycleOwner, Observer {
            it?.let {
                if(it) {
                    binding.simpleContainer.enableDeletion()
                    binding.textEditorMixerDelete.setTextColor(Color.parseColor("#90ee90"))
                    binding.textEditorMixerDelete.setText("Done")
                } else {
                    binding.simpleContainer.disableDeletion()
                    binding.textEditorMixerDelete.setTextColor(IWBNApplication.context.getColor(android.R.color.holo_red_light))
                    binding.textEditorMixerDelete.setText("Delete Image")
                }
                viewModel.doneTriggeringImageDeletion()
            }
        })


        viewModel.shouldSaveChapter.observe(viewLifecycleOwner, Observer {
            it?.let {
                val (bitmapsMap, coordinators) = binding.simpleContainer.getDetails()
                viewModel.doneSaveChapter()
                chapter?.let {chapter ->
                    viewModel.saveChapterDetails(chapter, bitmapsMap, coordinators)
                }
            }
        })


        return binding.root
    }

    private fun displayImagePicker() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, getString(R.string.select_picture)), PICK_IMAGE_REQUEST)
    }

    private fun buildEditText(string: String) : EditText {
        val txtColor = ContextCompat.getColor(context!!, android.R.color.white)
        val bgColor = ContextCompat.getColor(context!!, R.color.colorPrimaryDark)
        val padding = 10.toPx()
        val height = 50.toPx()

        return EditText(context!!).apply {
            setText(string)
            val params = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                height
            )
            setPadding(padding, padding, padding, padding)
            hint = "Please Enter Text Here"
            layoutParams = params
            background = IWBNApplication.context.resources.getColor(android.R.color.transparent).toDrawable()
            gravity = Gravity.CENTER_VERTICAL
            setTextColor(txtColor)
            setBackgroundColor(bgColor)
            setOnTouchListener(getTouchListener())
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 24f)
        }
    }



    fun getTouchListener(): TouchListenerImpl {
        return TouchListenerImpl(50.toPx(), 50.toPx())
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.data != null) {

            val bitmap = data.data!!.getBitmap(binding.simpleContainer.width / 3, binding.simpleContainer.width / 3)

            val imageView = EditorImageBlock(context!!)
            val layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            imageView.layoutParams = layoutParams
            imageView.setImageBitmap(bitmap)
            imageView.isClickable = true
            binding.simpleContainer.addView(imageView)
        }
    }
}