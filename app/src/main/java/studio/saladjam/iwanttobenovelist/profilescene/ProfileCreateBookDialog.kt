package studio.saladjam.iwanttobenovelist.profilescene

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import studio.saladjam.iwanttobenovelist.IWBNApplication
import studio.saladjam.iwanttobenovelist.Logger
import studio.saladjam.iwanttobenovelist.R
import studio.saladjam.iwanttobenovelist.databinding.DialogProfileBookCreationBinding
import studio.saladjam.iwanttobenovelist.extensions.getBitmap
import studio.saladjam.iwanttobenovelist.extensions.getVMFactory
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class ProfileCreateBookDialog(private val workViewModel: ProfileWorkViewModel) : AppCompatDialogFragment() {

    private lateinit var binding: DialogProfileBookCreationBinding
    private val viewModel by viewModels<ProfileCreateBookViewModel> { getVMFactory() }
    private lateinit var imageSelection: ImageSelectorDialog
    private var photoUri: Uri? = null

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

                when(ContextCompat.checkSelfPermission(context!!, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    PackageManager.PERMISSION_GRANTED -> {
                        showGallery()
                    }

                    PackageManager.PERMISSION_DENIED -> {
                        when (ActivityCompat.shouldShowRequestPermissionRationale(activity!!, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                            true -> {
                                showGalleryToastAndRequestPermission()
                            }

                            false -> {
                                requestGalleryPermissions()
                            }
                        }
                    }
                }


                viewModel.doneDisplayingImageSelector()
            }
        })


        viewModel.shouldSelectFromCamera.observe(this, Observer {
            it?.let {

                when(ContextCompat.checkSelfPermission(context!!, Manifest.permission.CAMERA)) {
                    PackageManager.PERMISSION_GRANTED -> {
                        showCamera()
                    }

                    PackageManager.PERMISSION_DENIED -> {
                        when (ActivityCompat.shouldShowRequestPermissionRationale(activity!!, Manifest.permission.CAMERA)) {
                            true -> {
                                // Show an explanation to the user *asynchronously* -- don't block
                                // this thread waiting for the user's response! After the user
                                // sees the explanation, try again to request the permission.
                                showCameraToastAndRequestPermission()
                            }

                            false -> {
                                // No explanation needed, we can request the permission
                                requestCameraPermissions()
                            }
                        }
                    }
                }
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

    /** GALLERY PERMISSION and INTENT */
    private fun showGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(
            Intent.createChooser(intent, getString(R.string.select_picture)),
            IWBNApplication.PICK_IMAGE_REQUEST
        )
    }

    private fun requestGalleryPermissions() {
            requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), IWBNApplication.PICK_IMAGE_REQUEST)
    }

    private fun showGalleryToastAndRequestPermission() {
        Toast.makeText(context!!, "Access to Gallery Denied", Toast.LENGTH_LONG).show()
        requestGalleryPermissions()
    }

    private fun showGalleryToastAndDismissDialog() {
        Toast.makeText(context!!, "Access to Gallery Denied", Toast.LENGTH_LONG).show()
        imageSelection.dismiss()
    }

    /** CAMERA PERMISSION and INTENT */
    private fun showCamera() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            takePictureIntent.resolveActivity(context!!.packageManager)?.also {
                // Create the File where the photo should go
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    // Error occurred while creating the File
                    Logger.e("showCamera: Error occurred while creating the File")
                    null
                }
                // Continue only if the File was successfully created
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        context!!,
                        "studio.saladjam.iwanttobenovelist.fileprovider",
                        it
                    )

//                    val bundle = Bundle()
//                    bundle.putParcelable("URI", photoURI)

                    this.photoUri = photoURI

                    takePictureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    // this will cause the app to crash ... not really sure why
//                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, bundle)
                    startActivityForResult(takePictureIntent,IWBNApplication.CAMERA_IMAGE_REQUEST)
                }
            }
        }
//        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
//            takePictureIntent.resolveActivity(activity!!.packageManager)?.also {
//                startActivityForResult(takePictureIntent, IWBNApplication.CAMERA_IMAGE_REQUEST)
//            }
//        }
    }

    private fun requestCameraPermissions() {
        requestPermissions(arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE), IWBNApplication.CAMERA_IMAGE_REQUEST)
    }

    private fun showCameraToastAndRequestPermission() {
        Toast.makeText(context!!, "Access to Camera Denied", Toast.LENGTH_LONG).show()
        requestGalleryPermissions()
    }

    private fun showCameraToastAndDismissDialog() {
        Toast.makeText(context!!, "Access to Camera Denied", Toast.LENGTH_LONG).show()
        imageSelection.dismiss()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when(requestCode) {
            IWBNApplication.PICK_IMAGE_REQUEST -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // PERMISSION GRANTED
                    showGallery()
                } else {
                    showGalleryToastAndDismissDialog()
                }
                return
            }

            IWBNApplication.CAMERA_IMAGE_REQUEST -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // PERMISSION GRANTED
                    showCamera()
                } else {
                    showCameraToastAndDismissDialog()
                }
                return
            }

//            else ->
        }

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

        } else if (requestCode == IWBNApplication.CAMERA_IMAGE_REQUEST &&
                resultCode == Activity.RESULT_OK &&
                data != null) {
            val bitmap = photoUri?.getBitmap(binding.imageProfileBookCreationCover.width, binding.imageProfileBookCreationCover.height)
            binding.imageProfileBookCreationCover.setImageBitmap(bitmap)

            viewModel.setImage(bitmap)
            binding.imageProfileBookCreationCover.setImageBitmap(bitmap)
            imageSelection.dismiss()

            photoUri = null
        }
    }

    lateinit var currentPhotoPath: String

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File = context!!.getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
        return File.createTempFile(
            "JPEG_cover_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }
}