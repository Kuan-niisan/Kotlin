package com.example.calculator

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.text.InputType
import android.view.Gravity
import android.view.View
import android.util.Log
import android.widget.LinearLayout
import android.provider.Settings
import android.view.*
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.File

class FileStorage : AppCompatActivity() {
    private lateinit var listView: ListView
    private lateinit var tvCurrentPath: TextView
    private lateinit var btnBack: Button
    private lateinit var imagePreview: ImageView
    private lateinit var textPreview: TextView

    private var currentPath: File = Environment.getExternalStorageDirectory()
    private var fileList = mutableListOf<File>()
    private lateinit var adapter: FileAdapter

    companion object {
        private const val PERMISSION_REQUEST_CODE = 1234
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_file_storage)

        listView = findViewById(R.id.listView)
        tvCurrentPath = findViewById(R.id.tvCurrentPath)
        btnBack = findViewById(R.id.btnBack)
        imagePreview = findViewById(R.id.imagePreview)
        textPreview = findViewById(R.id.textPreview)

        val btnNewFolder = findViewById<Button>(R.id.btnNewFolder)
        val btnNewFile = findViewById<Button>(R.id.btnNewFile)

        adapter = FileAdapter(this, fileList)
        listView.adapter = adapter

        btnBack.setOnClickListener {
            navigateUp()
        }

        btnNewFolder.setOnClickListener {
            showCreateFolderDialog()
        }

        btnNewFile.setOnClickListener {
            showCreateFileDialog()
        }

        listView.setOnItemClickListener { _, _, position, _ ->
            val selectedFile = fileList[position]
            handleFileClick(selectedFile)
        }

        registerForContextMenu(listView)

        requestStoragePermission()
    }

    override fun onResume() {
        super.onResume()
        if (hasStoragePermission()) {
            loadDirectory(currentPath)
        }
    }

    private fun hasStoragePermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            Environment.isExternalStorageManager()
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                    PackageManager.PERMISSION_GRANTED
        } else {
            true
        }
    }

    private fun requestStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {
                try {
                    val intent = Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
                    startActivity(intent)
                    Toast.makeText(this,
                        "Please grant 'All files access' permission",
                        Toast.LENGTH_LONG).show()
                } catch (e: Exception) {
                    val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                    intent.data = Uri.parse("package:$packageName")
                    startActivity(intent)
                }
            } else {
                loadDirectory(currentPath)
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE),
                    PERMISSION_REQUEST_CODE)
            } else {
                loadDirectory(currentPath)
            }
        } else {
            loadDirectory(currentPath)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                loadDirectory(currentPath)
            } else {
                Toast.makeText(this, "Permission denied!", Toast.LENGTH_LONG).show()
                finish()
            }
        }
    }

    private fun loadDirectory(directory: File) {
        currentPath = directory
        tvCurrentPath.text = directory.absolutePath

        fileList.clear()
        imagePreview.visibility = View.GONE
        textPreview.visibility = View.GONE

        try {
            val files = directory.listFiles()
            if (files != null) {
                val sortedFiles = files.sortedWith(compareBy({ !it.isDirectory }, { it.name }))
                fileList.addAll(sortedFiles)
            }
            adapter.notifyDataSetChanged()
        } catch (e: Exception) {
            Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun navigateUp() {
        val parent = currentPath.parentFile
        if (parent != null && parent.canRead()) {
            loadDirectory(parent)
        } else {
            Toast.makeText(this, "Cannot go up", Toast.LENGTH_SHORT).show()
        }
    }

    private fun handleFileClick(file: File) {
        if (file.isDirectory) {
            loadDirectory(file)
        } else {
            val extension = file.extension.lowercase()
            Log.d("FileStorage", "File clicked: ${file.name}, extension: $extension")

            when (extension) {
                "txt", "text", "log", "md" -> previewTextFile(file)
                "jpg", "jpeg", "png", "bmp", "gif" -> previewImage(file)
                else -> {
                    Toast.makeText(this,
                        "Cannot preview .$extension files\nFile: ${file.name}",
                        Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun previewTextFile(file: File) {
        try {
            Log.d("FileStorage", "Attempting to read: ${file.absolutePath}")

            if (!file.exists()) {
                Toast.makeText(this, "File does not exist", Toast.LENGTH_SHORT).show()
                return
            }

            if (!file.canRead()) {
                Toast.makeText(this, "Cannot read file - no permission", Toast.LENGTH_SHORT).show()
                return
            }

            val content = file.readText()
            Log.d("FileStorage", "File content length: ${content.length}")

            textPreview.text = content
            textPreview.visibility = View.VISIBLE
            imagePreview.visibility = View.GONE

            Toast.makeText(this, "Loaded ${file.name} (${content.length} chars)",
                Toast.LENGTH_SHORT).show()

        } catch (e: Exception) {
            Log.e("FileStorage", "Error reading file: ${file.absolutePath}", e)
            Toast.makeText(this, "Error: ${e.message}",
                Toast.LENGTH_LONG).show()
        }
    }

    private fun previewImage(file: File) {
        try {
            Log.d("FileStorage", "Attempting to load image: ${file.absolutePath}")

            if (!file.exists()) {
                Toast.makeText(this, "Image file does not exist", Toast.LENGTH_SHORT).show()
                return
            }

            val bitmap = BitmapFactory.decodeFile(file.absolutePath)
            if (bitmap != null) {
                imagePreview.setImageBitmap(bitmap)
                imagePreview.visibility = View.VISIBLE
                textPreview.visibility = View.GONE
                Toast.makeText(this, "Loaded image: ${file.name}", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Failed to decode image", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Log.e("FileStorage", "Error loading image: ${file.absolutePath}", e)
            Toast.makeText(this, "Error loading image: ${e.message}",
                Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_options, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_new_folder -> {
                showCreateFolderDialog()
                true
            }
            R.id.menu_new_file -> {
                showCreateFileDialog()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menuInflater.inflate(R.menu.context_menu, menu)

        val info = menuInfo as AdapterView.AdapterContextMenuInfo
        val selectedFile = fileList[info.position]

        if (selectedFile.isDirectory) {
            menu?.findItem(R.id.context_copy)?.isVisible = false
        }
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val info = item.menuInfo as AdapterView.AdapterContextMenuInfo
        val selectedFile = fileList[info.position]

        return when (item.itemId) {
            R.id.context_rename -> {
                showRenameDialog(selectedFile)
                true
            }
            R.id.context_delete -> {
                showDeleteConfirmDialog(selectedFile)
                true
            }
            R.id.context_copy -> {
                showCopyDialog(selectedFile)
                true
            }
            else -> super.onContextItemSelected(item)
        }
    }

    private fun showCreateFolderDialog() {
        val input = EditText(this)
        input.hint = "Folder name"

        AlertDialog.Builder(this)
            .setTitle("Create New Folder")
            .setView(input)
            .setPositiveButton("Create") { _, _ ->
                val folderName = input.text.toString()
                if (folderName.isNotEmpty()) {
                    val newFolder = File(currentPath, folderName)
                    if (newFolder.mkdir()) {
                        Toast.makeText(this, "Folder created", Toast.LENGTH_SHORT).show()
                        loadDirectory(currentPath)
                    } else {
                        Toast.makeText(this, "Failed to create folder",
                            Toast.LENGTH_SHORT).show()
                    }
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

  private fun showCreateFileDialog() {
        val layout = LinearLayout(this)
        layout.orientation = LinearLayout.VERTICAL
        layout.setPadding(60, 20, 60, 20)

        val inputFileName = EditText(this)
        inputFileName.hint = "File name (e.g., note.txt)"
        layout.addView(inputFileName)

        val space = View(this)
        space.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            40
        )
        layout.addView(space)

        val inputContent = EditText(this)
        inputContent.hint = "File content (optional)"
        inputContent.gravity = Gravity.TOP or Gravity.START
        inputContent.minLines = 5
        inputContent.maxLines = 10
        inputContent.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_MULTI_LINE
        inputContent.setBackgroundResource(android.R.drawable.edit_text)
        layout.addView(inputContent)

        AlertDialog.Builder(this)
            .setTitle("Create New Text File")
            .setView(layout)
            .setPositiveButton("Create") { _, _ ->
                val fileName = inputFileName.text.toString()
                val fileContent = inputContent.text.toString()

                if (fileName.isNotEmpty()) {
                    val newFile = File(currentPath, fileName)
                    try {
                        if (fileContent.isNotEmpty()) {
                            newFile.writeText(fileContent)
                        } else {
                            newFile.createNewFile()
                        }

                        Toast.makeText(this, "File created", Toast.LENGTH_SHORT).show()
                        loadDirectory(currentPath)
                    } catch (e: Exception) {
                        Toast.makeText(this, "Failed: ${e.message}",
                            Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "File name cannot be empty",
                        Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showRenameDialog(file: File) {
        val input = EditText(this)
        input.setText(file.name)

        AlertDialog.Builder(this)
            .setTitle("Rename")
            .setView(input)
            .setPositiveButton("Rename") { _, _ ->
                val newName = input.text.toString()
                if (newName.isNotEmpty()) {
                    val newFile = File(file.parent, newName)
                    if (file.renameTo(newFile)) {
                        Toast.makeText(this, "Renamed successfully",
                            Toast.LENGTH_SHORT).show()
                        loadDirectory(currentPath)
                    } else {
                        Toast.makeText(this, "Failed to rename",
                            Toast.LENGTH_SHORT).show()
                    }
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showDeleteConfirmDialog(file: File) {
        val message = if (file.isDirectory) {
            "Delete folder '${file.name}' and all its contents?"
        } else {
            "Delete file '${file.name}'?"
        }

        AlertDialog.Builder(this)
            .setTitle("Confirm Delete")
            .setMessage(message)
            .setPositiveButton("Delete") { _, _ ->
                if (deleteRecursive(file)) {
                    Toast.makeText(this, "Deleted successfully",
                        Toast.LENGTH_SHORT).show()
                    loadDirectory(currentPath)
                } else {
                    Toast.makeText(this, "Failed to delete", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun deleteRecursive(file: File): Boolean {
        if (file.isDirectory) {
            file.listFiles()?.forEach { deleteRecursive(it) }
        }
        return file.delete()
    }

    private fun showCopyDialog(file: File) {
        val input = EditText(this)
        input.hint = "Destination path"
        input.setText(currentPath.absolutePath)

        AlertDialog.Builder(this)
            .setTitle("Copy File")
            .setMessage("Copy '${file.name}' to:")
            .setView(input)
            .setPositiveButton("Copy") { _, _ ->
                val destPath = input.text.toString()
                val destDir = File(destPath)

                if (destDir.exists() && destDir.isDirectory) {
                    val destFile = File(destDir, file.name)
                    try {
                        file.copyTo(destFile, overwrite = false)
                        Toast.makeText(this, "File copied successfully",
                            Toast.LENGTH_SHORT).show()
                    } catch (e: Exception) {
                        Toast.makeText(this, "Copy failed: ${e.message}",
                            Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "Invalid destination",
                        Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}

class FileAdapter(
    private val context: FileStorage,
    private val files: List<File>
) : BaseAdapter() {

    override fun getCount(): Int = files.size

    override fun getItem(position: Int): Any = files[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.file_item, parent, false)

        val file = files[position]
        val icon = view.findViewById<ImageView>(R.id.fileIcon)
        val name = view.findViewById<TextView>(R.id.fileName)
        val info = view.findViewById<TextView>(R.id.fileInfo)

        name.text = file.name

        if (file.isDirectory) {
            icon.setImageResource(android.R.drawable.ic_menu_view)
            val itemCount = file.listFiles()?.size ?: 0
            info.text = "$itemCount items"
        } else {
            icon.setImageResource(android.R.drawable.ic_menu_info_details)
            val size = file.length() / 1024
            info.text = "$size KB"
        }

        return view
    }
}