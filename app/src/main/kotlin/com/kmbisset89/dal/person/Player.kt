package com.kmbisset89.dal.person

import kotlinx.serialization.Serializable

@Serializable
class Player(
    val firstName: String,
    val lastName: String,
    val areas : Set<Area>
)
