package com.example.produceapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ProduceDetailFragment : BottomSheetDialogFragment() {

    companion object {
        private const val ARG_NAME = "name"
        private const val ARG_DESCRIPTION = "description"
        private const val ARG_PRICE = "price"
        private const val ARG_IMAGE_RES_ID = "imageResId"

        fun newInstance(name: String, description: String, price: String, imageResId: Int): ProduceDetailFragment {
            val fragment = ProduceDetailFragment()
            val args = Bundle().apply {
                putString(ARG_NAME, name)
                putString(ARG_DESCRIPTION, description)
                putString(ARG_PRICE, price)
                putInt(ARG_IMAGE_RES_ID, imageResId)
            }
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_produce_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Get passed arguments
        val name = arguments?.getString(ARG_NAME) ?: ""
        val description = arguments?.getString(ARG_DESCRIPTION) ?: ""
        val price = arguments?.getString(ARG_PRICE) ?: ""
        val imageResId = arguments?.getInt(ARG_IMAGE_RES_ID) ?: 0

        // Set up views
        view.findViewById<TextView>(R.id.textDetailName).text = name
        view.findViewById<TextView>(R.id.textDetailDescription).text = description
        view.findViewById<TextView>(R.id.textDetailPrice).text = price
        view.findViewById<ImageView>(R.id.imageProduceDetail).setImageResource(imageResId)
    }
}