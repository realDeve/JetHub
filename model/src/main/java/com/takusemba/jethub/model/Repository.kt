package com.takusemba.jethub.model

import java.time.LocalDateTime

class Repository(
  val id: Int,
  val owner: com.takusemba.jethub.model.Owner,
  val name: String,
  val description: String,
  val createdAt: LocalDateTime,
  val updatedAt: LocalDateTime,
  val starsCount: Int,
  val watchersCount: Int,
  val forksCount: Int,
  val language: String
)