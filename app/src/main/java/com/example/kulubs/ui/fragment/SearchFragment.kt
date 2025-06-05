package com.example.kulubs.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.kulubs.R
import com.example.kulubs.ui.activity.AddWarungActivity
import com.example.kulubs.ui.activity.LoginActivity
import com.example.kulubs.ui.activity.SearchResultActivity
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.QuerySnapshot

class SearchFragment : Fragment() {

    private val MAX_HISTORY = 5
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private var firestoreListener: Task<QuerySnapshot>? = null // Track Firestore task

    // UI Components
    private lateinit var btnAddWarung: MaterialButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize Firebase Auth and Firestore
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        // Check if user is authenticated
        if (auth.currentUser == null) {
            startActivity(Intent(requireContext(), LoginActivity::class.java))
            requireActivity().finish()
            return
        }

        // Initialize UI components
        initializeViews(view)
        setupListeners()
        displaySearchHistory()

        // Initialize EditText for search
        val searchEditText = view.findViewById<EditText>(R.id.searchEditText)

        // Add IME options
        searchEditText.imeOptions = EditorInfo.IME_ACTION_SEARCH
        searchEditText.inputType = android.text.InputType.TYPE_CLASS_TEXT

        // Implement listener for keyboard action
        searchEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val query = searchEditText.text.toString().trim()
                if (query.isNotEmpty()) {
                    // Save query to Firestore
                    saveSearchQuery(query)
                    // Perform search
                    performSearch(query)
                }
                true
            } else {
                false
            }
        }
    }

    private fun initializeViews(view: View) {
        btnAddWarung = view.findViewById(R.id.btnAddWarung)
    }

    private fun setupListeners() {
        // Clear history TextView listener
        val tvHapus = view?.findViewById<TextView>(R.id.tvHapus)
        tvHapus?.setOnClickListener {
            clearSearchHistory()
        }

        // Add Warung button click listener
        btnAddWarung.setOnClickListener {
            val intent = Intent(requireContext(), AddWarungActivity::class.java)
            startActivity(intent)
        }
    }

    private fun performSearch(query: String) {
        if (!isAdded) return // Check if fragment is attached
        val intent = Intent(requireContext(), SearchResultActivity::class.java)
        intent.putExtra("QUERY", query)
        startActivity(intent)
    }

    private fun saveSearchQuery(query: String) {
        val userId = auth.currentUser?.uid ?: return
        val historyRef = db.collection("users").document(userId).collection("searchHistory")

        // Get current history to maintain order
        firestoreListener = historyRef.get().addOnSuccessListener { documents ->
            if (!isAdded) return@addOnSuccessListener // Check if fragment is attached
            val history = documents.map { it.getString("query") as String }.toMutableList()

            // Remove query if it exists to avoid duplicates
            history.remove(query)
            // Add new query
            history.add(query)

            // Limit to MAX_HISTORY
            if (history.size > MAX_HISTORY) {
                history.removeAt(0)
            }

            // Clear existing history in Firestore
            historyRef.get().addOnSuccessListener { docs ->
                if (!isAdded) return@addOnSuccessListener // Check if fragment is attached
                for (doc in docs) {
                    doc.reference.delete()
                }
                // Save updated history
                history.forEachIndexed { index, q ->
                    historyRef.document(index.toString()).set(mapOf("query" to q))
                }
            }
        }
    }

    private fun clearSearchHistory() {
        val userId = auth.currentUser?.uid ?: return
        val historyRef = db.collection("users").document(userId).collection("searchHistory")

        historyRef.get().addOnSuccessListener { documents ->
            if (!isAdded) return@addOnSuccessListener // Check if fragment is attached
            for (doc in documents) {
                doc.reference.delete()
            }
            // Update UI
            val historyListView = view?.findViewById<ListView>(R.id.lvHistory)
            val tvEmptyHistory = view?.findViewById<TextView>(R.id.tvEmptyHistory)
            tvEmptyHistory?.visibility = TextView.VISIBLE
            historyListView?.visibility = ListView.GONE
        }
    }

    private fun displaySearchHistory() {
        val userId = auth.currentUser?.uid ?: return
        val historyRef = db.collection("users").document(userId).collection("searchHistory")

        firestoreListener = historyRef.get().addOnSuccessListener { documents ->
            if (!isAdded) return@addOnSuccessListener // Check if fragment is attached
            val historyList = documents.map { it.getString("query") as String }
                .sortedByDescending { it }
                .take(MAX_HISTORY)

            val historyListView = view?.findViewById<ListView>(R.id.lvHistory)
            val tvEmptyHistory = view?.findViewById<TextView>(R.id.tvEmptyHistory)

            if (historyList.isEmpty()) {
                tvEmptyHistory?.visibility = TextView.VISIBLE
                historyListView?.visibility = ListView.GONE
            } else {
                tvEmptyHistory?.visibility = TextView.GONE
                historyListView?.visibility = ListView.VISIBLE
                val adapter = ArrayAdapter(requireContext(), R.layout.item_history, R.id.tvHistoryItem, historyList)
                historyListView?.adapter = adapter

                historyListView?.setOnItemClickListener { _, _, position, _ ->
                    val query = historyList[position]
                    Log.d("SearchFragment", "Item clicked: $query")
                    performSearch(query)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        // Update search history every time fragment resumes
        if (auth.currentUser != null) {
            displaySearchHistory()
        }
    }

    override fun onPause() {
        super.onPause()
        // Cancel any pending Firestore tasks to prevent callbacks after detachment
        firestoreListener?.let { task ->
            if (!task.isComplete) {
                task.addOnCompleteListener {
                    Log.d("SearchFragment", "Firestore task completed on pause")
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Clear Firestore listener to prevent leaks
        firestoreListener = null
    }
}