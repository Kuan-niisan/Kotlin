package com.example.calculator

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.calculator.databinding.FragmentAddStudentBinding

class AddStudentFragment : Fragment() {
    private val viewModel: StudentViewModel by activityViewModels()
    private lateinit var binding: FragmentAddStudentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentAddStudentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        val btnSave: Button = view.findViewById(R.id.btnSave)
        btnSave.setOnClickListener {
            val id = binding.etStudentId.text.toString()
            val name = binding.etStudentName.text.toString()
            val phone = binding.etPhoneNumber.text.toString()
            val address = binding.etAddress.text.toString()

            if (id.isNotEmpty() && name.isNotEmpty()) {
                viewModel.addStudent(Student(id, name, phone, address))
                findNavController().popBackStack()
            } else {
                Toast.makeText(requireContext(), "Vui lòng nhập đủ MSSV và Họ tên", Toast.LENGTH_SHORT).show()
            }
        }
    }
}