package com.example.mihailoprojekat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mihailoprojekat.modules.recycler.itemrv.Adapter

class BookmarksFragment : Fragment() {

    private lateinit var bookmarksRecycler: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_bookmarks, container, false)

        bookmarksRecycler = view.findViewById(R.id.bookmarks_recycler)
        bookmarksRecycler.layoutManager = LinearLayoutManager(requireContext())

        loadBookmarks()

        return view
    }

    override fun onResume() {
        super.onResume()
        loadBookmarks()
    }

    private fun loadBookmarks() {
        BookmarkManager.getBookmarks { bookmarks ->
            if (bookmarks.isEmpty()) {
                Toast.makeText(requireContext(), "No bookmarks yet!", Toast.LENGTH_SHORT).show()
            } else {
                bookmarksRecycler.adapter = Adapter(bookmarks)
                Toast.makeText(requireContext(), "Loaded ${bookmarks.size} bookmarks", Toast.LENGTH_SHORT).show()
            }
        }
    }
}