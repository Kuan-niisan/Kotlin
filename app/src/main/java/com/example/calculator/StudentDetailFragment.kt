package com.example.calculator

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.calculator.databinding.FragmentStudentDetailBinding

class StudentDetailFragment : Fragment() {
    private val viewModel: StudentViewModel by activityViewModels()
    private lateinit var binding: FragmentStudentDetailBinding
    private val args: StudentDetailFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentStudentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.onStudentSelected(args.studentPosition)

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        val btnUpdate: Button = view.findViewById(R.id.btnUpdate)
        btnUpdate.setOnClickListener {
            val name = binding.etStudentName.text.toString()
            val phone = binding.etPhoneNumber.text.toString()
            val address = binding.etAddress.text.toString()

            if (name.isNotEmpty()) {
                viewModel.updateStudent(name, phone, address)
                findNavController().popBackStack()
            } else {
                Toast.makeText(requireContext(), "Họ tên không được để trống", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.doneNavigating()
    }
}