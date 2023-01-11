package com.example.myapplication.fragment.task1

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.myapplication.contract.task1.Task1IncrementerContract
import com.example.myapplication.contract.task1.data.IncrementerPersistentStorageModel
import com.example.myapplication.contract.task1.presenter.Task1IncrementerPresenter
import com.example.myapplication.databinding.FragmentIncrementerBinding

private const val STORAGE_NAME = "NumStorage"

class IncrementerFragment : Fragment(), Task1IncrementerContract.View {
    private lateinit var binding: FragmentIncrementerBinding
    private lateinit var presenter: Task1IncrementerContract.Presenter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonIncrement.setOnClickListener {
            presenter.onIncrementButtonPressed()
        }

        binding.buttonDecrement.setOnClickListener {
            presenter.onDecrementButtonPressed()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentIncrementerBinding.inflate(inflater, container, false)
        presenter = Task1IncrementerPresenter(
            this,
            IncrementerPersistentStorageModel(
                requireContext().getSharedPreferences(
                    STORAGE_NAME,
                    Context.MODE_PRIVATE
                )
            )
        )
        return binding.root
    }

    override fun onDestroyView() {
        presenter.onDestroy()
        super.onDestroyView()
    }

    override fun updateIncrementerText(text: String) {
        binding.textViewIncrementer.text = text
    }
}