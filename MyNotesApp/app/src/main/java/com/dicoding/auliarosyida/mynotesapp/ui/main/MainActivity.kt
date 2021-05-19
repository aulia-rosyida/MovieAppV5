package com.dicoding.auliarosyida.mynotesapp.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.auliarosyida.mynotesapp.R
import com.dicoding.auliarosyida.mynotesapp.databinding.ActivityMainBinding
import com.dicoding.auliarosyida.mynotesapp.db.Note
import com.dicoding.auliarosyida.mynotesapp.helper.SortUtils
import com.dicoding.auliarosyida.mynotesapp.helper.ViewModelFactory
import com.dicoding.auliarosyida.mynotesapp.ui.insert.NoteAddUpdateActivity
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private var _activityMainBinding: ActivityMainBinding? = null
    private val binding get() = _activityMainBinding

    private lateinit var adapter: NotePagedListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val mainViewModel = obtainViewModel(this@MainActivity)
        mainViewModel.getAllNotes(SortUtils.NEWEST).observe(this, noteObserver)

        adapter = NotePagedListAdapter(this@MainActivity)
        binding?.rvNotes?.layoutManager = LinearLayoutManager(this)
        binding?.rvNotes?.setHasFixedSize(true)
        binding?.rvNotes?.adapter = adapter

        binding?.fabAdd?.setOnClickListener { view ->
            if (view.id == R.id.fab_add) {
                val intent = Intent(this@MainActivity, NoteAddUpdateActivity::class.java)
                startActivityForResult(intent, NoteAddUpdateActivity.REQUEST_ADD)
            }
        }
    }

    /**
     *  metode onActivityResult untuk menerima respon dari NoteAddUpdateActivity
     * */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null) {
            if (requestCode == NoteAddUpdateActivity.REQUEST_ADD) {
                if (resultCode == NoteAddUpdateActivity.RESULT_ADD) {
                    showSnackbarMessage(getString(R.string.added))
                }
            } else if (requestCode == NoteAddUpdateActivity.REQUEST_UPDATE) {
                if (resultCode == NoteAddUpdateActivity.RESULT_UPDATE) {
                    showSnackbarMessage(getString(R.string.changed))
                } else if (resultCode == NoteAddUpdateActivity.RESULT_DELETE) {
                    showSnackbarMessage(getString(R.string.deleted))
                }
            }
        }
    }

    private fun showSnackbarMessage(message: String) {
        Snackbar.make(binding?.root as View, message, Snackbar.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        _activityMainBinding = null
    }

    private fun obtainViewModel(activity: AppCompatActivity): MainViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory).get(MainViewModel::class.java)
    }

    /**
     * Kode di atas berfungsi untuk meng-observe data dari getAllNotes().
     * Setelah ada perubahan data, maka akan tampil di RecyclerView.
     *
     * ketika data sudah menjadi paged list, Anda cukup memanggil submitList untuk menaruh data list ke PagedListAdapter.
     * */
    private val noteObserver = Observer<PagedList<Note>> { noteList ->
        if (noteList != null) {
            adapter.submitList(noteList)
        }
    }

    /** Kode tersebut digunakan untuk membuat menu dengan menggunakan berkas menu_main.xml.
     *  Kemudian kode berikut digunakan untuk memberikan aksi ketika menu dipilih */
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    /**
     * Di sini Anda menentukan variabel yang dimasukkan untuk mengambil data berdasarkan menu yang dipilih.
     * Karena terdapat observer, maka data akan secara otomatis diperbarui ketika opsi berubah.
     * Setelah itu gunakan kode item.setChecked(true) untuk memberi tanda pada menu yang dipilih.
     * */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        var sort = ""
        when (item.getItemId()) {
            R.id.action_newest -> sort = SortUtils.NEWEST
            R.id.action_oldest -> sort = SortUtils.OLDEST
            R.id.action_random -> sort = SortUtils.RANDOM
        }
        val mainViewModel = obtainViewModel(this@MainActivity)
        mainViewModel.getAllNotes(sort).observe(this, noteObserver) //dipanggil di UI spt ini
        item.isChecked = true
        return super.onOptionsItemSelected(item)
    }
}