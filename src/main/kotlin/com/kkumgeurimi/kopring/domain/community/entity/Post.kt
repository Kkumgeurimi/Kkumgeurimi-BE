package com.kkumgeurimi.kopring.domain.community.entity

import com.kkumgeurimi.kopring.domain.common.BaseTime
import com.kkumgeurimi.kopring.domain.hashtag.entity.PostHashtag
import com.kkumgeurimi.kopring.domain.student.entity.Student
import jakarta.persistence.*

@Entity
@Table(name = "post")
class Post(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    val postId: Long = 0,

    @Column(nullable = true)
    var title: String? = null,

    @Column(nullable = false, columnDefinition = "TEXT")
    var content: String,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var category: PostCategory,

    @Column(name = "view_count", nullable = false)
    var viewCount: Int = 0,

    @Column(name = "like_count", nullable = false)
    var likeCount: Int = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    var author: Student,

    @OneToMany(mappedBy = "post", orphanRemoval = true)
    val comments: MutableList<Comment> = mutableListOf(),

    @OneToMany(mappedBy = "post", orphanRemoval = true)
    val likes: MutableList<PostLike> = mutableListOf(),

    @OneToMany(mappedBy = "post", cascade = [CascadeType.ALL], orphanRemoval = true)
    val postHashtags: MutableList<PostHashtag> = mutableListOf()
) : BaseTime()