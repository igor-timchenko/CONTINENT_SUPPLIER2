package ru.contlog.mobile.helper.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.contlog.mobile.helper.databinding.FragmentProductInfoBinding

class ProductInfoFragment : Fragment() {
    private lateinit var binding: FragmentProductInfoBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProductInfoBinding.inflate(inflater)

        bind()

        return binding.root
    }

    private fun bind() {}
}