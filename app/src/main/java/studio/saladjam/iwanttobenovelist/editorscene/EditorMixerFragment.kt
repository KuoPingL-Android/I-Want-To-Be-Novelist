package studio.saladjam.iwanttobenovelist.editorscene

import android.app.Activity.RESULT_OK
import android.content.Intent
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
import studio.saladjam.iwanttobenovelist.IWBNApplication
import studio.saladjam.iwanttobenovelist.R
import studio.saladjam.iwanttobenovelist.databinding.FragmentEditorMixerBinding
import studio.saladjam.iwanttobenovelist.databinding.FragmentEditorMixerV1Binding
import studio.saladjam.iwanttobenovelist.editorscene.utils.TouchListenerImpl
import studio.saladjam.iwanttobenovelist.extensions.getVMFactory
import studio.saladjam.iwanttobenovelist.extensions.toPx
import studio.saladjam.iwanttobenovelist.repository.dataclass.Chapter

class EditorMixerFragment: Fragment() {

    private lateinit var binding: FragmentEditorMixerBinding

    private val viewModel by viewModels<EditorMixerViewModel> { getVMFactory() }

//    private val args by navArgs<EditorFragmentArgs>()

    companion object {
        private const val PICK_IMAGE_REQUEST = 1
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEditorMixerBinding.inflate(inflater)



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
            val uri = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(context!!.contentResolver, uri)

            val imageView = ImageView(context!!)
            imageView.setImageBitmap(bitmap)

//            imageView.setOnTouchListener(TouchListenerImpl(50.toPx(), 50.toPx()){
//                binding.editEditor.updateOrAdd(imageView,  imageView.positionIn(binding.editEditor))
//            })

            imageView.width

//            binding.layerEditor.addView(imageView)


//            binding.layoutEditorPalette.addView(imageView)
//            binding.scrollviewEditor.addView(imageView)
        }
    }
}