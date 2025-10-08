package ru.contlog.mobile.helper.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.contlog.mobile.helper.databinding.FragmentWorkSitesBinding

class WorkSitesFragment : Fragment() {
    private lateinit var binding: FragmentWorkSitesBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWorkSitesBinding.inflate(inflater)

        bind()

        return binding.root
    }

    private fun bind() {}
}