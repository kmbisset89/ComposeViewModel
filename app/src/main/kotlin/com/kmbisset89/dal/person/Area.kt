package com.kmbisset89.dal.person

import kotlinx.serialization.Serializable

/**
 * Represents the area of the game a player is involved in.
 */
@Serializable
enum class Area {
    Offense,
    Defense,
    SpecialTeams,
}