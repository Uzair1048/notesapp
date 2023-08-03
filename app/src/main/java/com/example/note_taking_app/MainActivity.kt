package com.example.note_taking_app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.note_taking_app.database.NoteDatabase
import com.example.note_taking_app.databinding.ActivityMainBinding
import com.example.note_taking_app.repository.NoteRepository
import com.example.note_taking_app.viewmodel.NoteViewModel
import com.example.note_taking_app.viewmodel.NoteViewModelFactory

class MainActivity : AppCompatActivity() {

    lateinit var binding : ActivityMainBinding

    lateinit var noteViewModel : NoteViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpViewModel()
    }

    private fun setUpViewModel() {
        val noteRepository = NoteRepository(NoteDatabase(this))

        val noteViewModelFactory = NoteViewModelFactory(
            application, noteRepository
        )
        noteViewModel = ViewModelProvider(
            this,
            noteViewModelFactory
        ).get(NoteViewModel::class.java)
    }
}