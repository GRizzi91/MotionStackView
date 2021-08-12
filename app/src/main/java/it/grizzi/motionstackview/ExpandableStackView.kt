package it.grizzi.motionstackview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.appcompat.widget.AppCompatTextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.motion.widget.MotionScene
import androidx.constraintlayout.motion.widget.TransitionBuilder
import androidx.constraintlayout.widget.ConstraintSet

class ExpandableStackView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : MotionLayout(context, attrs, defStyleAttr) {

    fun setAdapter(adapter: BaseAdapter) {

        val scene = MotionScene(this)

        val startSetId = View.generateViewId()
        val startSet = ConstraintSet()
        startSet.clone(this)

        val endSetId = View.generateViewId()
        val endSet = ConstraintSet()
        endSet.clone(this)

        val views = mutableListOf<View>()

        for (i in 0 until adapter.count) {

            val view = adapter.getView(i, null, this)
            val cardView = view.findViewById<CardView>(R.id.cardContainer)
            view.id = View.generateViewId()

            startSet.constrainHeight(view.id, ConstraintSet.WRAP_CONTENT)
            endSet.constrainHeight(view.id, ConstraintSet.WRAP_CONTENT)

            connectViewToParent(startSet, view)
            connectViewToParent(endSet, view)

            if (i == 0) {
                startSet.setCardElevation(cardView, 8f)
                endSet.setCardElevation(cardView, 4f)
                startSet.connect(
                    view.id,
                    ConstraintSet.TOP,
                    ConstraintSet.PARENT_ID,
                    ConstraintSet.TOP
                )
                endSet.connect(
                    view.id,
                    ConstraintSet.TOP,
                    ConstraintSet.PARENT_ID,
                    ConstraintSet.TOP
                )
            } else {

                if (i == 1) {
                    startSet.setScaleX(
                        view.id,
                        0.95f
                    )
                    startSet.setCardElevation(cardView, 6f)
                    endSet.setCardElevation(cardView, 4f)
                } else {
                    startSet.setScaleX(
                        view.id,
                        0.9f
                    )
                    if (i == 2) {
                        startSet.setCardElevation(cardView, 4f)
                        endSet.setCardElevation(cardView, 4f)
                    } else {
                        startSet.setCardElevation(cardView, 0f)
                        endSet.setCardElevation(cardView, 4f)
                    }
                }

                views[i - 1].let {
                    boundTwoViewEnd(endSet, it, view)
                    if (i == adapter.count - 1) {
                        endSet.connect(
                            view.id,
                            ConstraintSet.BOTTOM,
                            ConstraintSet.PARENT_ID,
                            ConstraintSet.BOTTOM,
                            fromDpToPx(16).toInt()
                        )
                    }

                    boundTwoViewStart(
                        startSet, it, view,
                        if (i < 3)
                            fromDpToPx(8).toInt()
                        else
                            0
                    )
                }
            }

            views.add(view)
        }

        views.asReversed().forEach {
            addView(it)
        }

        val transitionId = View.generateViewId()
        val transaction = TransitionBuilder.buildTransition(
            scene,
            transitionId,
            startSetId, startSet,
            endSetId, endSet
        )

        transaction.duration = 1000

        scene.addTransition(transaction)
        scene.setTransition(transaction)
        setScene(scene)

        setTransition(transitionId)
    }

    private fun boundTwoViewEnd(
        constraintSet: ConstraintSet,
        firstView: View,
        secondView: View
    ) {
        constraintSet.connect(
            secondView.id,
            ConstraintSet.TOP,
            firstView.id,
            ConstraintSet.BOTTOM,
            fromDpToPx(16).toInt()
        )
    }

    private fun boundTwoViewStart(
        constraintSet: ConstraintSet,
        firstView: View,
        secondView: View,
        marginBottom: Int
    ) {
        constraintSet.connect(
            secondView.id,
            ConstraintSet.TOP,
            firstView.id,
            ConstraintSet.TOP,
            marginBottom
        )
    }

    private fun connectViewToParent(
        constraintSet: ConstraintSet,
        firstView: View
    ) {
        constraintSet.connect(
            firstView.id,
            ConstraintSet.START,
            ConstraintSet.PARENT_ID,
            ConstraintSet.START
        )

        constraintSet.connect(
            firstView.id,
            ConstraintSet.END,
            ConstraintSet.PARENT_ID,
            ConstraintSet.END
        )
    }

}

class ExpandableStackViewAdapter(
    private val models: List<String>,
    private val context: Context
) : BaseAdapter() {

    override fun getCount(): Int = models.size

    override fun getItem(position: Int): String = models[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = LayoutInflater.from(context).inflate(R.layout.card, parent, false)
        view.findViewById<AppCompatTextView>(R.id.text).text = position.toString()
        return view
    }
}
