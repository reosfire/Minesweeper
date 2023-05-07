package ru.reosfire.minesweeper.fragments.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.DialogFragment
import ru.reosfire.minesweeper.databinding.FragmentGameSettingsDialogBinding
import ru.reosfire.minesweeper.game.GameSettings

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

typealias GameSettingsDialogResultListener = (GameSettings) -> Unit

class GameSettingsDialog: DialogFragment() {
    companion object {
        private const val INITIAL_SETTING_KEY = "initial_settings"

        fun create(settings: GameSettings): GameSettingsDialog {
            return GameSettingsDialog().apply {
                arguments = Bundle().apply {
                    putParcelable(INITIAL_SETTING_KEY, settings)
                }
            }
        }

        private const val MIN_HEIGHT = 6
        private const val DEFAULT_HEIGHT = 15
        private const val MIN_WIDTH = 6
        private const val DEFAULT_WIDTH = 10
        private const val MIN_MINES = 6
        private const val DEFAULT_MINES = 10
    }


    private lateinit var binding: FragmentGameSettingsDialogBinding
    private var resultListener: GameSettingsDialogResultListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentGameSettingsDialogBinding.inflate(inflater, container, false)

        binding.heightSeekBar.setProgressChangedListener { _, progress, _ ->
            val height = MIN_HEIGHT + progress
            binding.heightValue.text = height.toString()
        }
        binding.widthSeekBar.setProgressChangedListener { _, progress, _ ->
            val width = MIN_WIDTH + progress
            binding.widthValue.text = width.toString()
        }
        binding.minesSeekBar.setProgressChangedListener { _, progress, _ ->
            val mines = MIN_MINES + progress
            binding.minesValue.text = mines.toString()
        }

        binding.okButton.setOnClickListener {
            resultListener?.invoke(GameSettings(getWidth(), getHeight(), getMines()))
            dismiss()
        }
        binding.cancelButton.setOnClickListener {
            dismiss()
        }


        val settings = arguments?.getParcelable<GameSettings>(INITIAL_SETTING_KEY)
        val initialHeight = settings?.height ?: DEFAULT_HEIGHT
        val initialWidth = settings?.width ?: DEFAULT_WIDTH
        val initialMines = settings?.minesCount ?: DEFAULT_MINES

        binding.heightValue.text = initialHeight.toString()
        binding.widthValue.text = initialWidth.toString()
        binding.minesValue.text = initialMines.toString()

        binding.heightSeekBar.progress = initialHeight - MIN_HEIGHT
        binding.widthSeekBar.progress = initialWidth - MIN_WIDTH
        binding.minesSeekBar.progress = initialMines - MIN_MINES

        return binding.root
    }

    fun setResultListener(listener: GameSettingsDialogResultListener) {
        resultListener = listener
    }

    private fun getHeight(): Int {
        return binding.heightSeekBar.progress + MIN_HEIGHT
    }
    private fun getWidth(): Int {
        return binding.widthSeekBar.progress + MIN_WIDTH
    }
    private fun getMines(): Int {
        return binding.minesSeekBar.progress + MIN_MINES
    }
}