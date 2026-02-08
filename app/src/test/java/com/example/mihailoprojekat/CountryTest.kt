package com.example.mihailoprojekat

import com.example.mihailoprojekat.modules.Country
import org.junit.Test
import org.junit.Assert.*

class CountryTest {

    @Test
    fun country_has_valid_code() {
        val country = Country("United States", "us", R.drawable.earth)
        assertEquals("us", country.code)
        assertTrue(country.code.length == 2)
    }

    @Test
    fun country_name_not_empty() {
        val country = Country("Canada", "ca", R.drawable.earth)
        assertTrue(country.name.isNotEmpty())
    }

    @Test
    fun country_code_is_lowercase() {
        val countries = listOf(
            Country("United States", "us", R.drawable.earth),
            Country("Canada", "ca", R.drawable.earth),
            Country("Serbia", "rs", R.drawable.earth)
        )

        countries.forEach { country ->
            assertEquals(country.code, country.code.lowercase())
        }
    }

    @Test
    fun search_query_can_be_null() {
        val country = Country("Worldwide", "us", R.drawable.earth, null)
        assertNull(country.searchQuery)
    }

    @Test
    fun search_query_exists_for_specific_countries() {
        val country = Country("Serbia", "rs", R.drawable.serbia, "Serbia")
        assertNotNull(country.searchQuery)
        assertEquals("Serbia", country.searchQuery)
    }
}