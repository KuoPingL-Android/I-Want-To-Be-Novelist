package studio.saladjam.iwanttobenovelist.profilescene

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import studio.saladjam.iwanttobenovelist.databinding.DialogImageSelectorBinding

class ImageSelectorDialog(private val viewModel: ProfileCreateBookViewModel) : BottomSheetDialogFragment() {
    private lateinit var binding: DialogImageSelectorBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogImageSelectorBinding.inflate(inflater)

        binding.buttonImageSelectorCamera.setOnClickListener {
            viewModel.displayCamera()
        }

        binding.buttonImageSelectorFolder.requestLayout()

        binding.buttonImageSelectorFolder.setOnClickListener {
            viewModel.displayImageSelector()
        }

        return binding.root
    }
}