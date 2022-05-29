package com.example.alias.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.alias.R
import com.example.alias.databinding.CustomSnakeButtonBinding

class CustomSnakeBtn @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    private val binding =
        CustomSnakeButtonBinding.inflate(LayoutInflater.from(context), this, true)

    fun setColor(isMarked: Boolean) {
        with(binding) {
            if (isMarked) {
                leftPart.setBackgroundResource(R.drawable.half_circle_right_green)
                rightPart.setBackgroundResource(R.drawable.half_circle_left_green)
                center.setBackgroundResource(R.color.subtle_green)
            } else {
                leftPart.setBackgroundResource(R.drawable.half_circle_right)
                rightPart.setBackgroundResource(R.drawable.half_circle_left)
                center.setBackgroundResource(R.color.dark_blue)
            }
        }
    }

    fun setOnClickListsner(listener: () -> Unit) = with(binding) {
        leftPart.setOnClickListener { listener() }
        rightPart.setOnClickListener { listener() }
        center.setOnClickListener { listener() }
    }

}