package com.example.myapplication.fragment.task1

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.myapplication.*
import com.example.myapplication.contract.task1.Task1Contract
import com.example.myapplication.contract.task1.data.Task1Repository
import com.example.myapplication.contract.task1.presenter.Task1Presenter
import com.example.myapplication.databinding.FragmentTask1Binding
import com.google.android.material.snackbar.Snackbar
import java.util.*

private const val ZOOM_STATE_BUNDLE_KEY = "zoomState"
private const val LIST_VISIBILITY_BUNDLE_KEY = "listVisibility"

class Task1Fragment : Fragment(), Task1Contract.View {
    private lateinit var binding: FragmentTask1Binding
    private lateinit var presenter: Task1Contract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initPresenter(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTask1Binding.inflate(inflater, container, false)
        presenter.syncState()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.listView.setOnItemClickListener { _: AdapterView<*>, _: View, position: Int, _: Long ->
            presenter.onListItemClicked(position)
        }

        binding.switchChangeColor.setOnCheckedChangeListener { _, isChecked ->
            presenter.onSwitchToggled(isChecked)
        }

        binding.buttonZoomInOut.setOnClickListener {
            presenter.onZoomButtonPressed()
        }

        binding.buttonHideShowList.setOnClickListener {
            presenter.onHideShowListButtonPressed()
        }

        binding.buttonToast.setOnClickListener {
            presenter.onToastButtonPressed()
        }

        binding.buttonUpdateLabel.setOnClickListener {
            presenter.onUpdateLabelButtonPressed(binding.editTextLabel.text.toString())
        }

        binding.fab.setOnClickListener {
            presenter.onFabPressed()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(ZOOM_STATE_BUNDLE_KEY, presenter.isPictureZoomed)
        outState.putBoolean(LIST_VISIBILITY_BUNDLE_KEY, presenter.isListVisible)
    }

    override fun navigateToDetailsFragment(position: Int, title: String, description: String) {
        resources.obtainTypedArray(R.array.task1_detailed_icons).use { icons ->
            val action = Task1FragmentDirections.detailsData(
                icons.getResourceId(position, -1),
                title,
                description
            )
            findNavController().navigate(action)
        }
    }

    override fun showAndLogToast() {
        val toastMsg = getString(R.string.task_1_button_toast) + " is working!"
        Toast.makeText(context, toastMsg, Toast.LENGTH_SHORT).show()
        Log.d("Task 1", toastMsg)
    }

    override fun updateLabel(text: String) {
        binding.textViewLabel.text = text
        binding.editTextLabel.setText("")
    }

    override fun showDummySnackbar() {
        Snackbar.make(binding.coordinator, "It's working!", Snackbar.LENGTH_SHORT).show()
    }

    override fun showUpdateLabelError() {
        binding.editTextLabel.error = getString(R.string.task_1_edit_text_label_error)
    }

    override fun zoomInPicture() {
        binding.image.layoutParams.width *= 2
        binding.image.layoutParams.height *= 2
        binding.buttonZoomInOut.text = getString(R.string.task_1_button_zoom_out)
        binding.image.requestLayout()
    }

    override fun zoomOutPicture() {
        binding.image.layoutParams.width /= 2
        binding.image.layoutParams.height /= 2
        binding.buttonZoomInOut.text = getString(R.string.task_1_button_zoom_in)
        binding.image.requestLayout()
    }

    override fun turnBackgroundColorBlack() {
        binding.coordinator.setBackgroundColor(Color.BLACK)
        binding.textViewChangeColor.setTextColor(Color.WHITE)
    }

    override fun turnBackgroundColorWhite() {
        binding.coordinator.setBackgroundColor(Color.WHITE)
        binding.textViewChangeColor.setTextColor(Color.BLACK)
    }

    override fun hideList() {
        binding.listView.visibility = View.INVISIBLE
        binding.buttonHideShowList.text = getString(R.string.task_1_button_show_list)
    }

    override fun showList() {
        binding.listView.visibility = View.VISIBLE
        binding.buttonHideShowList.text = getString(R.string.task_1_button_hide_list)
    }

    override fun onDestroy() {
        presenter.onDestroy()
        super.onDestroy()
    }

    private fun initPresenter(state: Bundle?) {
        val language = Locale.getDefault().displayLanguage
        presenter = when (state) {
            null -> Task1Presenter(this, Task1Repository(language))
            else -> Task1Presenter(
                this,
                Task1Repository(language),
                state.getBoolean(ZOOM_STATE_BUNDLE_KEY),
                state.getBoolean(LIST_VISIBILITY_BUNDLE_KEY)
            )
        }
    }
}