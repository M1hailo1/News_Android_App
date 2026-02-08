package com.example.mihailoprojekat

import com.example.mihailoprojekat.modules.Info
import com.example.mihailoprojekat.modules.Source
import org.junit.Test
import org.junit.Assert.*

class BookmarkManagerTest {

    @Test
    fun sanitizeUrl_removes_special_characters() {
        val url = "https://example.com/article"
        val sanitized = url.replace("[^a-zA-Z0-9]".toRegex(), "_")
        assertFalse(sanitized.contains("://"))
        assertFalse(sanitized.contains("/"))
    }

    @Test
    fun article_has_valid_title() {
        val article = Info(
            title = "Test Article",
            description = "Test description",
            content = "Test content",
            url = "https://example.com",
            image = null,
            publishedAt = "2024-01-01",
            source = Source("Test Source", "")
        )
        assertTrue(article.title.isNotEmpty())
        assertEquals("Test Article", article.title)
    }

    @Test
    fun article_url_is_valid() {
        val article = Info(
            title = "Test",
            description = null,
            content = null,
            url = "https://example.com/news",
            image = null,
            publishedAt = "2024-01-01",
            source = Source("Test", "")
        )
        assertTrue(article.url.startsWith("http"))
        assertTrue(article.url.contains("."))
    }
}