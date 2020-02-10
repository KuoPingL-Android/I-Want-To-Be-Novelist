package studio.saladjam.iwanttobenovelist.editorscene

import android.app.Activity.RESULT_OK
import android.content.Intent
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
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toDrawable
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import studio.saladjam.iwanttobenovelist.IWBNApplication
import studio.saladjam.iwanttobenovelist.R
import studio.saladjam.iwanttobenovelist.databinding.FragmentEditorBinding
import studio.saladjam.iwanttobenovelist.editorscene.utils.TouchListenerImpl
import studio.saladjam.iwanttobenovelist.extensions.getVMFactory
import studio.saladjam.iwanttobenovelist.extensions.toPx
import studio.saladjam.iwanttobenovelist.repository.dataclass.Chapter

class EditorFragment : Fragment() {
    private lateinit var binding: FragmentEditorBinding
    private val viewModel by viewModels<EditorViewModel> { getVMFactory() }

//    private val args by navArgs<EditorFragmentArgs>()

    companion object {
        private const val PICK_IMAGE_REQUEST = 1
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEditorBinding.inflate(inflater)

        binding.viewModel = viewModel

        val chapter = arguments?.getParcelable<Chapter>("chapter")

        viewModel.shouldAddEditText.observe(this, Observer {
            it?.let {
                binding.layoutEditorPalette.addView(buildEditText("TESTING"))
            }
        })

        viewModel.shouldAddImage.observe(this, Observer {
            it?.let {
                val intent = Intent()
                intent.type = "image/*"
                intent.action = Intent.ACTION_GET_CONTENT
                startActivityForResult(Intent.createChooser(intent, getString(R.string.select_picture)), PICK_IMAGE_REQUEST)
            }
        })

        return binding.root
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

            imageView.setOnTouchListener(getTouchListener())

            binding.layoutEditorPalette.addView(imageView)
        }
    }
}