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
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class StudentListFragment : Fragment() {

    private val viewModel: StudentViewModel by activityViewModels()
    private lateinit var adapter: StudentAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_student_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rvStudents: RecyclerView = view.findViewById(R.id.rvStudents)
        val fabAdd: FloatingActionButton = view.findViewById(R.id.fabAdd)

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

        rvStudents.layoutManager = LinearLayoutManager(requireContext())
        rvStudents.adapter = adapter

        viewModel.studentList.observe(viewLifecycleOwner) { list ->
            adapter.studentList = list
            adapter.notifyDataSetChanged()
        }

        fabAdd.setOnClickListener {
            findNavController().navigate(R.id.action_studentListFragment_to_addStudentFragment)
        }
    }
}