package com.example.note_taking_app.fragments

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.note_taking_app.MainActivity
import com.example.note_taking_app.R
import com.example.note_taking_app.adapter.NoteAdapter
import com.example.note_taking_app.databinding.FragmentHomeBinding
import com.example.note_taking_app.model.Note
import com.example.note_taking_app.viewmodel.NoteViewModel


class HomeFragment : Fragment(R.layout.fragment_home), SearchView.OnQueryTextListener {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var notesViewModel: NoteViewModel
    private lateinit var noteAdapter: NoteAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val actionBar = (activity as AppCompatActivity).supportActionBar
        actionBar?.title = "My Notes"
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        notesViewModel = (activity as MainActivity).noteViewModel

        setUpRecyclerView()

        binding.fabAddNote.setOnClickListener {
            it.findNavController().navigate(R.id.action_homeFragment_to_newNoteFragment)
        }
    }

    private fun setUpRecyclerView() {

        noteAdapter = NoteAdapter()

        binding.recyclerView.apply {
            layoutManager = StaggeredGridLayoutManager(
                2,
                StaggeredGridLayoutManager.VERTICAL
            )
            setHasFixedSize(true)
            adapter = noteAdapter
        }

        activity?.let {
            notesViewModel.getAllNotes().observe(
                viewLifecycleOwner
            ) { note ->
                noteAdapter.differ.submitList(note)
                updateUI(note)
            }
        }

    }

    private fun updateUI(note: List<Note>?) {
        if (note != null) {
            if (note.isNotEmpty()) {
                binding.cardView.visibility = View.GONE
                binding.recyclerView.visibility = View.VISIBLE
            } else {
                binding.cardView.visibility = View.VISIBLE
                binding.recyclerView.visibility = View.GONE
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        menu.clear()
        inflater.inflate(R.menu.home_menu, menu)
        val searchItem = menu.findItem(R.id.searchMenu)
        if (searchItem != null) {
            val mMenuSearch = searchItem.actionView as SearchView
            mMenuSearch.isSubmitButtonEnabled = false
            mMenuSearch.setOnQueryTextListener(this)
        }

    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        // searchNote(query)
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        if (newText != null) {
            searchNote(newText)
        }
        return true
    }

    private fun searchNote(query: String?) {
        val searchQuery = "%$query"
        notesViewModel.searchNote(searchQuery).observe(
            this,
            { list -> noteAdapter.differ.submitList(list) }
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}