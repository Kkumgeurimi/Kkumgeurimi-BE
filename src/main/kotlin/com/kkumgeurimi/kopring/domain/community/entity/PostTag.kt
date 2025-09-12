package com.kkumgeurimi.kopring.domain.hashtag.entity

import com.kkumgeurimi.kopring.domain.common.BaseTime
import com.kkumgeurimi.kopring.domain.community.entity.Post
import jakarta.persistence.*

@Entity
@Table(name = "post_tag")
class PostTag(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_tag_id")
    val id: Long = 0,

    @Column(nullable = false, unique = true, length = 50)
    val name: String,

    @ManyToMany(mappedBy = "post_tags")
    val posts: MutableList<Post> = mutableListOf()
) : BaseTime()
