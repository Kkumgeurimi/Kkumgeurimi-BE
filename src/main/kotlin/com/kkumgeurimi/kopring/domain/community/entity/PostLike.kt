package com.kkumgeurimi.kopring.domain.community.entity

import com.kkumgeurimi.kopring.domain.common.BaseTime
import com.kkumgeurimi.kopring.domain.student.entity.Student
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.persistence.ManyToOne
import jakarta.persistence.JoinColumn
import jakarta.persistence.FetchType
import jakarta.persistence.UniqueConstraint

@Entity
@Table(
    name = "post_like",
    uniqueConstraints = [UniqueConstraint(columnNames = ["post_id", "author_id"])]
)
class PostLike(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_like_id")
    val id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    var post: Post,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    val student: Student
) : BaseTime() {}