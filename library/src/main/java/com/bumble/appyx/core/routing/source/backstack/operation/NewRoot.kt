package com.bumble.appyx.core.routing.source.backstack.operation

import com.bumble.appyx.core.routing.RoutingKey
import com.bumble.appyx.core.routing.source.backstack.BackStack
import com.bumble.appyx.core.routing.source.backstack.BackStackElement
import com.bumble.appyx.core.routing.source.backstack.BackStackElements
import com.bumble.appyx.core.routing.source.backstack.active
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

/**
 * Operation:
 *
 * [A, B, C] + NewRoot(D) = [ D ]
 */
@Parcelize
data class NewRoot<T : Any>(
    private val element: @RawValue T
) : BackStackOperation<T> {

    override fun isApplicable(elements: BackStackElements<T>): Boolean = true

    override fun invoke(
        elements: BackStackElements<T>,
    ): BackStackElements<T> {

        val current = elements.active
        requireNotNull(current) { "No previous elements, state=$elements" }

        return if (current.key.routing == element) {
            listOf(current)
        } else {
            listOf(
                current.transitionTo(
                    targetState = BackStack.TransitionState.DESTROYED,
                    operation = this
                ),
                BackStackElement(
                    key = RoutingKey(element),
                    fromState = BackStack.TransitionState.CREATED,
                    targetState = BackStack.TransitionState.ACTIVE,
                    operation = this
                )
            )
        }
    }
}

fun <T : Any> BackStack<T>.newRoot(element: T) {
    accept(NewRoot(element))
}