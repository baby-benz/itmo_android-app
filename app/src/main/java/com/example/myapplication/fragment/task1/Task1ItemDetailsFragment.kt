package com.example.myapplication.fragment.task1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.myapplication.*
import com.example.myapplication.contract.task1.Task1ItemDetailsContract
import com.example.myapplication.contract.task1.data.Task1DetailedModel
import com.example.myapplication.contract.task1.presenter.Task1ItemDetailsPresenter
import com.example.myapplication.databinding.FragmentTask1DetailedScreenBinding

private const val NATURAL_BUNDLE_KEY = "natural"
private const val FIBONACCI_BUNDLE_KEY = "fibonacci"
private const val NEXT_FIBONACCI_BUNDLE_KEY = "nextFibonacci"
private const val COLLATZ_BUNDLE_KEY = "collatz"

class ListItemDetailsFragment: Fragment(), Task1ItemDetailsContract.View {
    private val args: ListItemDetailsFragmentArgs by navArgs()

    private lateinit var binding: FragmentTask1DetailedScreenBinding
    private lateinit var presenter: Task1ItemDetailsContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initPresenter(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTask1DetailedScreenBinding.inflate(inflater)
        binding.icon.setImageResource(args.iconId)
        binding.title.text = args.title
        binding.description.text = args.description
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonNatural.setOnClickListener {
            presenter.onNaturalButtonPressed()
        }
        binding.buttonFibonacci.setOnClickListener {
            presenter.onFibonacciButtonPressed()
        }
        binding.buttonCollatz.setOnClickListener {
            presenter.onCollatzButtonPressed()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val curState = presenter.curState()
        outState.putInt(NATURAL_BUNDLE_KEY, curState.natural)
        outState.putInt(FIBONACCI_BUNDLE_KEY, curState.fibonacci)
        outState.putInt(NEXT_FIBONACCI_BUNDLE_KEY, curState.nextFibonacci)
        outState.putInt(COLLATZ_BUNDLE_KEY, curState.collatz)
    }

    override fun displayNatural(value: Int) {
        binding.textViewNatural.text = value.toString()
    }

    override fun displayFibonacci(value: Int) {
        binding.textViewFibonacci.text = value.toString()
    }

    override fun displayCollatz(value: Int) {
        binding.textViewCollatz.text = value.toString()
    }

    override fun showMaxNumberErrorToast() {
        Toast.makeText(
            requireContext(),
            getString(R.string.task_1_detailed_text_view_max_number_error),
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun showLastCollatzMemberErrorToast() {
        Toast.makeText(
            requireContext(),
            getString(R.string.task_1_detailed_text_view_collatz_error),
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onDestroyView() {
        presenter.onDestroy()
        super.onDestroyView()
    }

    private fun initPresenter(state: Bundle?) {
        if (state != null) {
            val savedNaturalValue = state.getInt(NATURAL_BUNDLE_KEY)
            val savedFibonacciValue = state.getInt(FIBONACCI_BUNDLE_KEY)
            val savedNextFibonacciValue = state.getInt(NEXT_FIBONACCI_BUNDLE_KEY)
            val savedCollatzValue = state.getInt(COLLATZ_BUNDLE_KEY)

            presenter = Task1ItemDetailsPresenter(
                this,
                Task1DetailedModel(
                    Task1ItemDetailsContract.State(
                        savedNaturalValue,
                        savedFibonacciValue,
                        savedNextFibonacciValue,
                        savedCollatzValue
                    )
                )
            )
        } else {
            val resourceNaturalValue = resources.getInteger(R.integer.natural)
            val resourceFibonacciValue = resources.getInteger(R.integer.fibonacci)
            val resourceNextFibonacciValue = resources.getInteger(R.integer.next_fibonacci)
            val resourceCollatzValue = resources.getInteger(R.integer.collatz)

            presenter = Task1ItemDetailsPresenter(
                this,
                Task1DetailedModel(
                    Task1ItemDetailsContract.State(
                        resourceNaturalValue,
                        resourceFibonacciValue,
                        resourceNextFibonacciValue,
                        resourceCollatzValue
                    )
                )
            )
        }
    }
}