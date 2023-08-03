package com.example.note_taking_app.fragments

import android.os.Bundle
import android.text.Editable
import android.text.SpannableStringBuilder
import android.text.TextWatcher
import android.view.*
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.note_taking_app.MainActivity
import com.example.note_taking_app.R
import com.example.note_taking_app.adapter.NoteAdapter
import com.example.note_taking_app.databinding.FragmentNewNoteBinding
import com.example.note_taking_app.model.Note
import com.example.note_taking_app.viewmodel.NoteViewModel
import java.text.SimpleDateFormat
import java.util.*

class NewNoteFragment : Fragment(R.layout.fragment_new_note) {

    private var _binding: FragmentNewNoteBinding? = null
    private val binding get() = _binding!!

    private lateinit var noteAdapter: NoteAdapter
    private lateinit var notesViewModel: NoteViewModel
    private lateinit var mView: View


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentNewNoteBinding.inflate(inflater, container, false)
        val actionBar = (activity as AppCompatActivity).supportActionBar
        actionBar?.title = "Add New Note"

        // Create a SimpleDateFormat object
        val dateFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        val formattedDate = dateFormat.format(Date())

        // Set the text of the TextView
        binding.date.text = formattedDate

        return binding.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        notesViewModel = (activity as MainActivity).noteViewModel
        mView = view

    }

    // Call these functions when you want to perform undo or redo actions


    private fun saveNote(view: View) {

        val noteTitle = binding.etNoteTitle.text.toString().trim()
        val noteBody = binding.etNoteBody.text.toString().trim()
        val date = binding.date.text.toString()

        if (noteTitle.isNotEmpty()) {
            val note = Note(0, noteTitle, noteBody,date)
            notesViewModel.addNote(note)
            Toast.makeText(
                mView.context,
                "Note Saved Successfully",
                Toast.LENGTH_LONG
            ).show()


            view.findNavController().navigate(R.id.action_newNoteFragment_to_homeFragment)

        } else {
            Toast.makeText(
                mView.context,
                "Please Enter Note Title",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        menu.clear()
        inflater.inflate(R.menu.menu_new_note, menu)
        super.onCreateOptionsMenu(menu, inflater)

    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.saveMenu -> {
                saveNote(mView)
            }
            R.id.undoMenu ->{
            }
            R.id.redoMenu ->{
            }
        }

        return super.onOptionsItemSelected(item)
    }

}