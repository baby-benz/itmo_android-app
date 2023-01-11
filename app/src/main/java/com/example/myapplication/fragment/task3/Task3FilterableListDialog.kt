package com.example.myapplication.fragment.task3

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.myapplication.R
import com.example.myapplication.databinding.DialogTask3FilterableListBinding

class Task3FilterableListDialog : DialogFragment() {
    private val args: Task3FilterableListDialogArgs by navArgs()
    private lateinit var binding: DialogTask3FilterableListBinding
    private lateinit var listView: ListView
    private lateinit var listAdapter: ArrayAdapter<String>

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            binding = DialogTask3FilterableListBinding.inflate(layoutInflater)
            AlertDialog.Builder(it).setView(binding.root).create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dialog?.window?.setBackgroundDrawableResource(R.drawable.dialog_fragment_background)
        listView = binding.list
        listAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, args.entityList)
        listView.adapter = listAdapter
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.editText.doAfterTextChanged {
            listAdapter.filter.filter(it)
        }

        listView.setOnItemClickListener { _, _, i, _ ->
            findNavController().previousBackStackEntry?.savedStateHandle?.set(args.stateKey, i)
            dismiss()
        }
    }
}