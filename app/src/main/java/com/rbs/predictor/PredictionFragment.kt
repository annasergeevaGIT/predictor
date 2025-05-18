package com.rbs.predictor.ui.prediction // Use your UI prediction package name

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.rbs.predictor.databinding.FragmentPredictionBinding // Use your root package for binding
import com.rbs.predictor.data.PredictionRepository // Import PredictionRepository
import com.rbs.predictor.viewmodel.PredictionViewModel // Import PredictionViewModel
import com.rbs.predictor.viewmodel.PredictionViewModelFactory // Import PredictionViewModelFactory

class PredictionFragment : Fragment() {

    private var _binding: FragmentPredictionBinding? = null
    private val binding get() = _binding!!

    // Use viewModels delegate with a custom factory
    private val viewModel: PredictionViewModel by viewModels {
        PredictionViewModelFactory(PredictionRepository())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPredictionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Observe the prediction LiveData from the ViewModel
        viewModel.prediction.observe(viewLifecycleOwner) { result ->
            when (result) {
                // *** CORRECTED REFERENCES HERE ***
                // Referencing the states of the INNER sealed class PredictionResultState
                is PredictionViewModel.PredictionResultState.Loading -> {
                    binding.progressBarLoading.visibility = View.VISIBLE
                    binding.textViewLoadingError.visibility = View.GONE
                    binding.textViewPredictionCoin.visibility = View.GONE
                    binding.textViewPredictionDate.visibility = View.GONE
                }
                is PredictionViewModel.PredictionResultState.Success -> {
                    binding.progressBarLoading.visibility = View.GONE
                    binding.textViewLoadingError.visibility = View.GONE
                    binding.textViewPredictionCoin.visibility = View.VISIBLE
                    binding.textViewPredictionDate.visibility = View.VISIBLE

                    binding.textViewPredictionDate.text = "Date: ${result.date}"
                    binding.textViewPredictionCoin.text = result.coin
                }
                is PredictionViewModel.PredictionResultState.Error -> {
                    binding.progressBarLoading.visibility = View.GONE
                    binding.textViewPredictionCoin.visibility = View.GONE
                    binding.textViewPredictionDate.visibility = View.GONE
                    binding.textViewLoadingError.visibility = View.VISIBLE
                    binding.textViewLoadingError.text = result.message
                }
                is PredictionViewModel.PredictionResultState.NotFound -> {
                    binding.progressBarLoading.visibility = View.GONE
                    binding.textViewPredictionCoin.visibility = View.GONE
                    binding.textViewPredictionDate.visibility = View.GONE
                    binding.textViewLoadingError.visibility = View.VISIBLE
                    binding.textViewLoadingError.text = result.message
                }
            }
        }

        // Fetch the prediction when the fragment is created
        viewModel.fetchDailyPrediction()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}