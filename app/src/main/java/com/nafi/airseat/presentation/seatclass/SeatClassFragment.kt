package com.nafi.airseat.presentation.seatclass

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.nafi.airseat.data.datasource.seatclass.SeatClassDummyDataSourceImpl
import com.nafi.airseat.data.model.SeatClass
import com.nafi.airseat.data.repository.SeatClassRepositoryImpl
import com.nafi.airseat.databinding.FragmentSeatClassBinding
import com.nafi.airseat.presentation.seatclass.adapter.SeatClassAdapter

class SeatClassFragment : BottomSheetDialogFragment() {
    private lateinit var viewModel: SeatClassViewModel
    private lateinit var binding: FragmentSeatClassBinding
    private val seatClassAdapter: SeatClassAdapter by lazy {
        SeatClassAdapter {
            // Do nothing on item click, handle on save button click
        }
    }
    private var listener: OnSeatClassSelectedListener? = null

    interface OnSeatClassSelectedListener {
        fun onSeatClassSelected(seatClass: SeatClass)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentSeatClassBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)

        val seatClassRepository = SeatClassRepositoryImpl(SeatClassDummyDataSourceImpl())
        viewModel = SeatClassViewModel(seatClassRepository)

        setupSeatClass()
        proceedSeatClass()
        binding.btnSaveSeatClass.setOnClickListener {
            seatClassAdapter.getSelectedSeatClass()?.let {
                listener?.onSeatClassSelected(it)
            }
            dismiss()
        }
    }

    override fun onStart() {
        super.onStart()
        (dialog as? BottomSheetDialog)?.behavior?.state = BottomSheetBehavior.STATE_EXPANDED
    }

    private fun setupSeatClass() {
        binding.rvSeatClass.adapter = seatClassAdapter
    }

    private fun proceedSeatClass() {
        viewModel.getSeatClass().observe(viewLifecycleOwner) { data ->
            data?.let {
                bindSeatClassList(it)
            }
        }
    }

    private fun bindSeatClassList(data: List<SeatClass>) {
        seatClassAdapter.submitData(data)
    }

    fun setOnSeatClassSelectedListener(listener: OnSeatClassSelectedListener) {
        this.listener = listener
    }
}
