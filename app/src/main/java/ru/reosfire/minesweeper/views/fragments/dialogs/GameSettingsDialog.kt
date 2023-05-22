package ru.reosfire.minesweeper.views.fragments.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import ru.reosfire.minesweeper.databinding.FragmentGameSettingsDialogBinding
import ru.reosfire.minesweeper.viewmodels.GameSettingsViewModel

typealias SeekBarProgressChangedListener = (SeekBar?, Int, Boolean) -> Unit
private fun SeekBar.setProgressChangedListener(listener: SeekBarProgressChangedListener) {
    setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
        override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            listener(seekBar, progress, fromUser)
        }

        override fun onStartTrackingTouch(seekBar: SeekBar?) {}
        override fun onStopTrackingTouch(seekBar: SeekBar?) {}
    })
}

class GameSettingsDialog: DialogFragment() {
    companion object {
        private const val MIN_HEIGHT = 6
        private const val MIN_WIDTH = 6
        private const val MIN_MINES = 1
    }

    private val viewModel: GameSettingsViewModel by activityViewModels<GameSettingsViewModel>()
    private lateinit var binding: FragmentGameSettingsDialogBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentGameSettingsDialogBinding.inflate(inflater, container, false)
        val initSettings = viewModel.getSettings()

        binding.minesSeekBar.max = viewModel.maxMines.value
        binding.minesSeekBar.progress = MIN_MINES - viewModel.mines.value
        binding.heightSeekBar.progress = MIN_HEIGHT - viewModel.height.value
        binding.widthSeekBar.progress = MIN_WIDTH - viewModel.width.value

        binding.okButton.setOnClickListener {
            dismiss()
        }
        binding.cancelButton.setOnClickListener {
            viewModel.setSettings(initSettings)
            dismiss()
        }

        lifecycleScope.launchWhenCreated {
            viewModel.height.collect{
                binding.heightValue.text = it.toString()
                binding.heightSeekBar.progress = it - MIN_HEIGHT
            }
        }
        lifecycleScope.launchWhenCreated {
            viewModel.width.collect{
                binding.widthValue.text = it.toString()
                binding.widthSeekBar.progress = it - MIN_WIDTH
            }
        }
        lifecycleScope.launchWhenCreated {
            viewModel.mines.collect{
                binding.minesValue.text = it.toString()
                binding.minesSeekBar.progress = it - MIN_MINES
            }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.maxMines.collect{
                binding.minesSeekBar.max = it - MIN_MINES
            }
        }

        binding.heightSeekBar.setProgressChangedListener { _, progress, _ ->
            viewModel.setHeight(MIN_HEIGHT + progress)
        }
        binding.widthSeekBar.setProgressChangedListener { _, progress, _ ->
            viewModel.setWidth(MIN_WIDTH + progress)
        }
        binding.minesSeekBar.setProgressChangedListener { _, progress, _ ->
            viewModel.setMines(MIN_MINES + progress)
        }

        return binding.root
    }
}