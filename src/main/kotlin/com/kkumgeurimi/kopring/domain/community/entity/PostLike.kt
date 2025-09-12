package com.kkumgeurimi.kopring.domain.community.entity

import com.kkumgeurimi.kopring.domain.common.BaseTime
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.persistence.ManyToOne
import jakarta.persistence.JoinColumn
import jakarta.persistence.FetchType


@Entity
@Table(name = "post_like")
class PostLike(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_like_id")
    val id: Long = 0,

    @Column(name = "author_id", nullable = false)
    var authorId: Long,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    var post: Post
) : BaseTime() {}