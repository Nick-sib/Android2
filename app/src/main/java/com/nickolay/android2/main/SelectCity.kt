package com.nickolay.android2.main


import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.nickolay.android2.R
import com.nickolay.android2.adapters.CityListAdapter
import com.nickolay.android2.model.WeatherViewModel
import kotlinx.android.synthetic.main.city_select.view.*
import java.io.File.separator
import java.util.*


class SelectCity: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.city_select, container, false)

        root.tiet_Filter.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable) { }

            override fun beforeTextChanged(s: CharSequence, p1: Int, p2: Int, p3: Int) { }

            override fun onTextChanged(s: CharSequence, p1: Int, p2: Int, p3: Int) {
                (root.rv_citys.adapter as CityListAdapter).applyFilter(
                    s.toString().toLowerCase(Locale.ROOT))
            }
        })

        setupAdapter(root)

        return root
    }

    private fun setupAdapter(root: View) {
        root.rv_citys.setHasFixedSize(true)

        root.rv_citys.addItemDecoration(
            DividerItemDecoration(context, LinearLayoutManager.VERTICAL)
                .apply {setDrawable(context!!.getDrawable(R.drawable.separator)!!)})

        root.rv_citys.itemAnimator = DefaultItemAnimator().apply {
            addDuration = ANIMATION_DURATION
            removeDuration = ANIMATION_DURATION
        }


        val wordViewModel = ViewModelProvider(activity!!).get(WeatherViewModel::class.java)

        root.rv_citys.adapter = wordViewModel.adapter

        wordViewModel.allCitys.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            (root.rv_citys.adapter as CityListAdapter).setCitys(it)
        })

//            .apply {
//                onItemListClickListener = parentFragmentManager.fragments[0] as CityData
//            }
    }


    companion object {

        private const val ANIMATION_DURATION = 200L

        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private const val ARG_SECTION_NUMBER = "section_number"

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        @JvmStatic
        fun newInstance(sectionNumber: Int): SelectCity {
            return SelectCity().apply {
                arguments = Bundle().apply {
                    putInt(ARG_SECTION_NUMBER, sectionNumber)
                }
            }
        }
    }



}