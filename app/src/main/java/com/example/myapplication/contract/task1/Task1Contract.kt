package com.example.myapplication.contract.task1

interface Task1Contract {
    interface View {
        fun navigateToDetailsFragment(position: Int, title: String, description: String)
        fun showAndLogToast()
        fun updateLabel(text: String)
        fun showDummySnackbar()
        fun showUpdateLabelError()
        fun zoomInPicture()
        fun zoomOutPicture()
        fun turnBackgroundColorBlack()
        fun turnBackgroundColorWhite()
        fun hideList()
        fun showList()
    }

    interface Presenter {
        var isPictureZoomed: Boolean
        var isListVisible: Boolean

        fun syncState()
        fun onToastButtonPressed()
        fun onUpdateLabelButtonPressed(editTextText: String)
        fun onFabPressed()
        fun onZoomButtonPressed()
        fun onListItemClicked(position: Int)
        fun onSwitchToggled(isChecked: Boolean)
        fun onHideShowListButtonPressed()
        fun onDestroy()
    }

    interface Model {
        data class DetailedScreenData(val title: String, val description: String)

        fun detailedScreenData(detailedScreenNum: Int): DetailedScreenData
    }
}