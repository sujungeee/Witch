package com.ssafy.witch.ui.snack

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ssafy.witch.R
import com.ssafy.witch.base.BaseFragment
import com.ssafy.witch.databinding.FragmentPwdEditBinding
import com.ssafy.witch.databinding.FragmentSnackBinding
import com.ssafy.witch.ui.group.GroupEditFragment


class SnackFragment : BaseFragment<FragmentSnackBinding>(FragmentSnackBinding::bind, R.layout.fragment_snack) {

    private var snackId = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            snackId = it.getString("snackId").toString()
        }

    }

    companion object {
        @JvmStatic
        fun newInstance(key:String, value:String) =
            SnackFragment().apply {
                arguments = Bundle().apply {
                    putString(key, value)
                }
            }
    }
}