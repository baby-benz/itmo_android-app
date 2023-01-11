package com.example.myapplication.contract.task1.presenter

import com.example.myapplication.contract.task1.Task1Contract

class Task1Presenter(
    private var view: Task1Contract.View?,
    private val model: Task1Contract.Model,
    override var isPictureZoomed: Boolean = false,
    override var isListVisible: Boolean = true
) : Task1Contract.Presenter {
    override fun syncState() {
        if (isPictureZoomed) {
            view?.zoomInPicture()
        }
        if (!isListVisible) {
            view?.hideList()
        }
    }

    override fun onToastButtonPressed() {
        view?.showAndLogToast()
    }

    override fun onUpdateLabelButtonPressed(editTextText: String) {
        if (editTextText.trim().isNotEmpty()) {
            view?.updateLabel(editTextText)
        } else {
            view?.showUpdateLabelError()
        }
    }

    override fun onFabPressed() {
        view?.showDummySnackbar()
    }

    override fun onZoomButtonPressed() {
        isPictureZoomed = !isPictureZoomed
        if (isPictureZoomed) {
            view?.zoomInPicture()
        } else {
            view?.zoomOutPicture()
        }
    }

    override fun onListItemClicked(position: Int) {
        val detailedScreenData = model.detailedScreenData(position)
        view?.navigateToDetailsFragment(
            position,
            detailedScreenData.title,
            detailedScreenData.description
        )
    }

    override fun onSwitchToggled(isChecked: Boolean) {
        if (isChecked) {
            view?.turnBackgroundColorBlack()
        } else {
            view?.turnBackgroundColorWhite()
        }
    }

    override fun onHideShowListButtonPressed() {
        isListVisible = !isListVisible
        if (isListVisible) {
            view?.showList()
        } else {
            view?.hideList()
        }
    }

    override fun onDestroy() {
        this.view = null
    }
}