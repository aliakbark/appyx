package com.bumble.appyx.v2.core.routing.source.spotlight.backpresshandler

import com.bumble.appyx.v2.core.routing.backpresshandlerstrategies.BaseBackPressHandlerStrategy
import com.bumble.appyx.v2.core.routing.source.spotlight.Spotlight
import com.bumble.appyx.v2.core.routing.source.spotlight.Spotlight.TransitionState.ACTIVE
import com.bumble.appyx.v2.core.routing.source.spotlight.SpotlightElements
import com.bumble.appyx.v2.core.routing.source.spotlight.operations.Activate
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GoToDefault<Routing : Any>(
    private val defaultElementIndex: Int = 0
) : BaseBackPressHandlerStrategy<Routing, Spotlight.TransitionState>() {

    override val canHandleBackPressFlow: Flow<Boolean> by lazy {
        routingSource.elements.map(::defaultElementIsNotActive)
    }

    override fun onBackPressed() {
        routingSource.accept(Activate(defaultElementIndex))
    }

    private fun defaultElementIsNotActive(elements: SpotlightElements<Routing>) =
        elements.getOrNull(defaultElementIndex)?.targetState != ACTIVE
}