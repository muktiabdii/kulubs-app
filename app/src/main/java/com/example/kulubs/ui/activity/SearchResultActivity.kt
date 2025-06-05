package com.example.kulubs.ui.activity

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kulubs.R
import com.example.kulubs.adapter.FilterAdapter
import com.example.kulubs.adapter.ResultsAdapter
import com.example.kulubs.model.FilterOption
import com.example.kulubs.model.WarungResult
import com.example.kulubs.ui.dialog.RatingFilterDialogFragment
import com.google.android.material.card.MaterialCardView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class SearchResultActivity : AppCompatActivity() {

    private lateinit var rvFilter: RecyclerView
    private lateinit var rvResults: RecyclerView
    private lateinit var filterAdapter: FilterAdapter
    private lateinit var resultsAdapter: ResultsAdapter
    private lateinit var searchEditText: EditText
    private lateinit var btnBack: ImageView
    private lateinit var filterContainer: ConstraintLayout
    private lateinit var floatingSearchCard: MaterialCardView
    private lateinit var fabFilter: FloatingActionButton

    private val filterOptions = mutableListOf(
        FilterOption("Semua", true),
        FilterOption("Chinese"),
        FilterOption("Nusantara"),
        FilterOption("Western"),
        FilterOption("Dessert")
    )

    private val warungResults = mutableListOf<WarungResult>()
    private var originalList = mutableListOf<WarungResult>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_search_result)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initializeViews()
        setupRecyclerViews()
        loadSampleData()

        val initialQuery = intent.getStringExtra("QUERY")
        if (!initialQuery.isNullOrEmpty()) {
            searchEditText.setText(initialQuery)
            filterResultsByQuery(initialQuery)
        }

        setupListeners()
    }

    private fun initializeViews() {
        searchEditText = findViewById(R.id.searchEditText)
        btnBack = findViewById(R.id.btnBack)
        filterContainer = findViewById(R.id.filterContainer)
        floatingSearchCard = findViewById(R.id.floatingSearchCard)
        fabFilter = findViewById(R.id.fabFilter)
    }

    private fun setupRecyclerViews() {
        setupFilterRecyclerView()
        setupResultsRecyclerView()
    }

    private fun setupListeners() {
        btnBack.setOnClickListener {
            finish()
        }

        fabFilter.setOnClickListener {
            val ratingFilterDialog = RatingFilterDialogFragment().apply {
                setOnRatingSelectedListener { selectedRating ->
                    if (selectedRating != null) {
                        filterResultsByRating(selectedRating)
                    } else {
                        resetFilters() // Reset if no rating is selected
                    }
                    dismiss()
                }
            }
            ratingFilterDialog.show(supportFragmentManager, "RatingFilterDialog")
        }

        setupSearch()
    }

    private fun setupFilterRecyclerView() {
        rvFilter = findViewById(R.id.rvFilter)
        rvFilter.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        filterAdapter = FilterAdapter(filterOptions) { selected ->
            filterOptions.forEach { it.isSelected = false }
            selected.isSelected = true
            filterAdapter.notifyDataSetChanged()

            if (selected.name == "Semua") {
                resetFilters()
            } else {
                filterResultsByCategory(selected.name)
            }

            Toast.makeText(this, "Filter: ${selected.name}", Toast.LENGTH_SHORT).show()
        }

        rvFilter.adapter = filterAdapter
    }

    private fun setupResultsRecyclerView() {
        rvResults = findViewById(R.id.rvResults)
        rvResults.layoutManager = LinearLayoutManager(this)

        resultsAdapter = ResultsAdapter(
            warungResults,
            onWhatsappClick = { warung ->
                Toast.makeText(this, "WhatsApp to: ${warung.name}", Toast.LENGTH_SHORT).show()
            },
            onReviewClick = { warung ->
                Toast.makeText(this, "Write review for: ${warung.name}", Toast.LENGTH_SHORT).show()
            }
        )

        rvResults.adapter = resultsAdapter
    }

    private fun setupSearch() {
        searchEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val searchQuery = searchEditText.text.toString().trim()
                if (searchQuery.isNotEmpty()) {
                    filterResultsByQuery(searchQuery)
                } else {
                    resetFilters()
                }
                true
            } else {
                false
            }
        }
    }

    private fun loadSampleData() {
        warungResults.addAll(
            listOf(
                WarungResult(
                    id = "1",
                    name = "Lalapan Mbak L",
                    rating = 4.9f,
                    likes = 200,
                    location = "Kantin FILKOM di belakang GKM",
                    categories = listOf("Mie", "Nusantara", "Nasi"),
                    imageResId = R.drawable.sample_warung,
                    phoneNumber = "+628123456789"
                ),
                WarungResult(
                    id = "2",
                    name = "Chinese Food Pak Eko",
                    rating = 4.7f,
                    likes = 150,
                    location = "Gedung Teknik Sipil Lantai 1",
                    categories = listOf("Chinese", "Nasi"),
                    imageResId = R.drawable.sample_warung,
                    phoneNumber = "+628987654321"
                ),
                WarungResult(
                    id = "3",
                    name = "Burger & Steak Pak Doni",
                    rating = 4.5f,
                    likes = 120,
                    location = "Taman Depan Perpustakaan Pusat",
                    categories = listOf("Western", "Burger"),
                    imageResId = R.drawable.sample_warung,
                    phoneNumber = "+628567891234"
                ),
                WarungResult(
                    id = "4",
                    name = "Ice Cream & Dessert Mbak Sari",
                    rating = 4.8f,
                    likes = 180,
                    location = "Food Court Rektorat",
                    categories = listOf("Dessert", "Ice Cream"),
                    imageResId = R.drawable.sample_warung,
                    phoneNumber = "+628765432109"
                ),
                WarungResult(
                    id = "5",
                    name = "Soto Ayam Pak Joko",
                    rating = 4.6f,
                    likes = 95,
                    location = "Kantin Gedung H",
                    categories = listOf("Nusantara", "Soto"),
                    imageResId = R.drawable.sample_warung,
                    phoneNumber = "+628111222333"
                ),
                WarungResult(
                    id = "6",
                    name = "Pizza Corner",
                    rating = 4.3f,
                    likes = 88,
                    location = "Food Court Perpus",
                    categories = listOf("Western", "Pizza"),
                    imageResId = R.drawable.sample_warung,
                    phoneNumber = "+628444555666"
                )
            )
        )

        originalList.addAll(warungResults)
        updateResultCount()
    }

    private fun resetFilters() {
        warungResults.clear()
        warungResults.addAll(originalList)
        resultsAdapter.notifyDataSetChanged()
        updateResultCount()
        toggleEmptyState(false)
    }

    private fun filterResultsByCategory(category: String) {
        val filteredList = originalList.filter { warung ->
            warung.categories.any { it.equals(category, ignoreCase = true) }
        }

        warungResults.clear()
        warungResults.addAll(filteredList)
        resultsAdapter.notifyDataSetChanged()
        updateResultCount()
        toggleEmptyState(filteredList.isEmpty())
    }

    private fun filterResultsByRating(minRating: Float) {
        val filteredList = originalList.filter { it.rating >= minRating }

        warungResults.clear()
        warungResults.addAll(filteredList)
        resultsAdapter.notifyDataSetChanged()
        updateResultCount()
        toggleEmptyState(filteredList.isEmpty())
        Toast.makeText(this, "Filtered by rating: $minRating+", Toast.LENGTH_SHORT).show()
    }

    private fun filterResultsByQuery(query: String) {
        val filteredList = originalList.filter { warung ->
            warung.name.contains(query, ignoreCase = true) ||
                    warung.categories.any { it.contains(query, ignoreCase = true) } ||
                    warung.location.contains(query, ignoreCase = true)
        }

        warungResults.clear()
        warungResults.addAll(filteredList)
        resultsAdapter.notifyDataSetChanged()
        updateResultCount()
        toggleEmptyState(filteredList.isEmpty())
    }

    private fun updateResultCount() {
        val countText = "${warungResults.size} warung ditemukan"
        findViewById<TextView>(R.id.tvResultCount)?.text = countText
    }

    private fun toggleEmptyState(isEmpty: Boolean) {
        val emptyState = findViewById<View>(R.id.emptyState)
        val resultsRecyclerView = findViewById<RecyclerView>(R.id.rvResults)

        if (isEmpty) {
            emptyState?.visibility = View.VISIBLE
            resultsRecyclerView?.visibility = View.GONE
        } else {
            emptyState?.visibility = View.GONE
            resultsRecyclerView?.visibility = View.VISIBLE
        }
    }
}