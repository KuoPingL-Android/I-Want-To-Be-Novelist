package studio.saladjam.iwanttobenovelist.profilescene

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import studio.saladjam.iwanttobenovelist.IWBNApplication
import studio.saladjam.iwanttobenovelist.R
import studio.saladjam.iwanttobenovelist.databinding.DialogProfileBookCreationBinding
import studio.saladjam.iwanttobenovelist.editorscene.EditorFragment
import studio.saladjam.iwanttobenovelist.extensions.getBitmap
import studio.saladjam.iwanttobenovelist.extensions.getSimpleBitmap
import studio.saladjam.iwanttobenovelist.extensions.getVMFactory

class ProfileCreateBookDialog(private val workViewModel: ProfileWorkViewModel) : AppCompatDialogFragment() {

    private lateinit var binding: DialogProfileBookCreationBinding
    private val viewModel by viewModels<ProfileCreateBookViewModel> { getVMFactory() }
    private lateinit var imageSelection: ImageSelectorDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.CreateBookDialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogProfileBookCreationBinding.inflate(inflater)

        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        binding.buttonProfileBookCreateClose.setOnClickListener {
            dismiss()
        }

        imageSelection = ImageSelectorDialog(viewModel)

        viewModel.shouldShowImageSelection.observe(this, Observer {
            it?.let {

                fragmentManager?.let {fm ->
                    if(!imageSelection.isInLayout) {
                        imageSelection.show(fm, "Image Selection")
                    }
                }
                viewModel.doneDisplayingImageSelector()
            }
        })

        viewModel.shouldSelectFromFolder.observe(this, Observer {
            it?.let {
                val intent = Intent()
                intent.type = "image/*"
                intent.action = Intent.ACTION_GET_CONTENT
                startActivityForResult(
                    Intent.createChooser(intent, getString(R.string.select_picture)),
                    IWBNApplication.PICK_IMAGE_REQUEST
                )
                viewModel.doneDisplayingImageSelector()
            }
        })

        viewModel.shouldSelectFromCamera.observe(this, Observer {
            it?.let {
                viewModel.doneDisplayingCamera()
            }
        })

        viewModel.isDataPrepared.observe(this, Observer {})

        viewModel.createdBook.observe(this, Observer {
            it?.let {
                workViewModel.notifyBookCreated(it)
                dismiss()
            }
        })

        return binding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == IWBNApplication.PICK_IMAGE_REQUEST &&
            resultCode == Activity.RESULT_OK &&
            data != null && data.data != null) {

            val bitmap = data.data!!.getBitmap(binding.imageProfileBookCreationCover.width, binding.imageProfileBookCreationCover.height)

            if(bitmap != null) {
                viewModel.setImage(bitmap)
                binding.imageProfileBookCreationCover.setImageBitmap(bitmap)
                imageSelection.dismiss()
            }

        }

    }

}