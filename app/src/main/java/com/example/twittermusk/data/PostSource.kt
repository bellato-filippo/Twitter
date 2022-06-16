package com.example.twittermusk.data

import com.example.twittermusk.models.Post

class PostSource {
    fun loadPost(): List<Post> {
        val post1: Post = Post(1, "prova1", "prova1", "prova1")
        val post2: Post = Post(1, "prova2", "prova2", "prova2")
        val post3: Post = Post(1, "prova3", "prova3", "prova3")


        return listOf<Post>(post1, post2, post3)
    }
}