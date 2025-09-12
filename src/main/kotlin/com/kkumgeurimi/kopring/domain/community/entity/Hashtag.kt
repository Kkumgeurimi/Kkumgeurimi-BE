package com.kkumgeurimi.kopring.domain.hashtag.entity

import com.kkumgeurimi.kopring.domain.common.BaseTime
import jakarta.persistence.*

@Entity
@Table(name = "hashtag")
class Hashtag(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hashtag_id")
    val id: Long = 0,

    @Column(nullable = false, unique = true, length = 50)
    val name: String
) : BaseTime()
