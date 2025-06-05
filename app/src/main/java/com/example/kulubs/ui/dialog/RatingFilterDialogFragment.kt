package com.example.kulubs.ui.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.kulubs.R
import com.google.android.material.button.MaterialButton

class RatingFilterDialogFragment : DialogFragment() {

    private var onRatingSelected: ((Float?) -> Unit)? = null
    private var selectedRating: Float? = null

    fun setOnRatingSelectedListener(listener: (Float?) -> Unit) {
        onRatingSelected = listener
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_FRAME, R.style.TransparentDialogTheme) // Apply transparent theme
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_rating_filter, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize rating buttons
        val ratingButtons = listOf(
            view.findViewById<MaterialButton>(R.id.rating_5),
            view.findViewById<MaterialButton>(R.id.rating_4),
            view.findViewById<MaterialButton>(R.id.rating_3),
            view.findViewById<MaterialButton>(R.id.rating_2),
            view.findViewById<MaterialButton>(R.id.rating_1)
        )

        // Map buttons to their rating values
        val ratingValues = listOf(5f, 4f, 3f, 2f, 1f)

        // Set toggle behavior for each button
        ratingButtons.forEachIndexed { index, button ->
            button.setOnClickListener {
                val rating = ratingValues[index]
                if (selectedRating == rating) {
                    // Deselect if already selected
                    selectedRating = null
                    button.isSelected = false
                    button.setBackgroundTintList(android.content.res.ColorStateList.valueOf(android.graphics.Color.WHITE))
                } else {
                    // Deselect all other buttons
                    ratingButtons.forEach { btn ->
                        btn.isSelected = false
                        btn.setBackgroundTintList(android.content.res.ColorStateList.valueOf(android.graphics.Color.WHITE))
                    }
                    // Select the clicked button
                    selectedRating = rating
                    button.isSelected = true
                    button.setBackgroundTintList(android.content.res.ColorStateList.valueOf(android.graphics.Color.parseColor("#EAA23F")))
                }
            }
        }

        // Handle Apply and Cancel buttons
        view.findViewById<MaterialButton>(R.id.btnApply).setOnClickListener {
            onRatingSelected?.invoke(selectedRating)
            dismiss()
        }

        view.findViewById<MaterialButton>(R.id.btnCancel).setOnClickListener {
            dismiss()
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            800,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
    }
}