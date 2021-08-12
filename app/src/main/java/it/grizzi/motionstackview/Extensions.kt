package it.grizzi.motionstackview

import android.view.View
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintAttribute
import androidx.constraintlayout.widget.ConstraintSet


fun View.fromDpToPx(dp: Int) = context.resources.displayMetrics.density * dp

fun ConstraintSet.setCardRadius(cardView: CardView, radiusDp: Float) {
    getConstraint(cardView.id)?.let {
        it.mCustomConstraints["Radius"] = ConstraintAttribute(
            "Radius",
            ConstraintAttribute.AttributeType.DIMENSION_TYPE,
            radiusDp
        )
    }
}

fun ConstraintSet.setCardElevation(cardView: CardView, elevation: Float) {
    getConstraint(cardView.id)?.let {
        it.mCustomConstraints["CardElevation"] = ConstraintAttribute(
            "CardElevation",
            ConstraintAttribute.AttributeType.DIMENSION_TYPE,
            elevation
        )
    }
}
