package com.example.calculator

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.calculator.databinding.FragmentStudentListBinding

class StudentListFragment : Fragment() {

    private val viewModel: StudentViewModel by activityViewModels()
    private lateinit var binding: FragmentStudentListBinding
    private lateinit var adapter: StudentAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentStudentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        adapter = StudentAdapter(
            mutableListOf(),
            onItemClick = { position ->
                viewModel.onStudentSelected(position)
                val action = StudentListFragmentDirections.actionStudentListFragmentToStudentDetailFragment(position)
                findNavController().navigate(action)
            },
            onDeleteClick = { position ->
                viewModel.deleteStudent(position)
            }
        )

        binding.rvStudents.layoutManager = LinearLayoutManager(requireContext())
        binding.rvStudents.adapter = adapter

        viewModel.studentList.observe(viewLifecycleOwner) { list ->
            adapter.studentList = list
            adapter.notifyDataSetChanged()
        }

        binding.fabAdd.setOnClickListener {
            findNavController().navigate(R.id.action_studentListFragment_to_addStudentFragment)
        }
    }
}