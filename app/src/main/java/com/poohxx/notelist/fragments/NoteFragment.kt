package com.poohxx.notelist.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.poohxx.notelist.R
import com.poohxx.notelist.activities.MainApp
import com.poohxx.notelist.databinding.FragmentNoteBinding
import com.poohxx.notelist.db.MainViewModel

class NoteFragment : BaseFragment() {
    private lateinit var binding: FragmentNoteBinding
    private val mainViewModel: MainViewModel by activityViewModels {
        MainViewModel.MainViewModelFactory((context?.applicationContext as MainApp).dataBase)
    }

    override fun onClickNew() {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
     
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {

        @JvmStatic
        fun newInstance() = NoteFragment()
    }
}