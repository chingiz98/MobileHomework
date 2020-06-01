package com.modulstart.mobilehomework.views.profile

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AlertDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.modulstart.mobilehomework.App
import com.modulstart.mobilehomework.R
import com.modulstart.mobilehomework.presenters.ProfilePresenter
import com.modulstart.mobilehomework.repository.models.Account
import com.modulstart.mobilehomework.repository.models.User
import com.modulstart.mobilehomework.repository.profile.ProfileRepository
import com.modulstart.mobilehomework.views.base.BaseFragment
import kotlinx.android.synthetic.main.dialog_edit_user_info.view.*
import kotlinx.android.synthetic.main.dialog_transfer.view.*
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.progress_layer.*
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject


class ProfileFragment: BaseFragment(), ProfileView {

    private val GALLERY_REQUEST_CODE = 100

    @InjectPresenter
    lateinit var profilePresenter: ProfilePresenter

    @Inject
    lateinit var repository: ProfileRepository

    lateinit var user: User

    @ProvidePresenter
    fun provideProfilePresenter(): ProfilePresenter {
        return ProfilePresenter(repository)
    }

    override fun onAttach(context: Context) {
        App.appComponent.inject(this)
        super.onAttach(context)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_profile, container, false)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        profilePic.setOnClickListener {
            pickFromGallery()
        }
        logoutButton.setOnClickListener {
            MaterialAlertDialogBuilder(context)
                .setTitle(resources.getString(R.string.warning))
                .setMessage(resources.getString(R.string.logout_warning))
                .setNegativeButton(resources.getString(R.string.no)) { dialog, _ ->
                    dialog.dismiss()
                }
                .setPositiveButton(resources.getString(R.string.yes)) { _, _ ->
                    profilePresenter.logout()
                }
                .show()
        }

        editButton.setOnClickListener {
            showEditInfoDialog()
        }
    }

    override fun showLoading() {
        progressLayer.visibility = View.VISIBLE
        profileContent.visibility = View.INVISIBLE
    }

    override fun showProfile(user: User) {
        this.user = user
        profileName.text = user.name
        profileUsername.text = user.username
        profileStatus.text = user.status
        progressLayer.visibility = View.GONE
        profileContent.visibility = View.VISIBLE
    }

    override fun showProfileImage(bitmap: Bitmap) {
        profilePic.setImageBitmap(bitmap)
    }

    private fun pickFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        val mimeTypes = arrayOf("image/jpeg", "image/png")
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
        startActivityForResult(intent, GALLERY_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) when (requestCode) {
            GALLERY_REQUEST_CODE -> {
                val selectedImage: Uri? = data!!.data
                profilePic.setImageURI(selectedImage)
                selectedImage?.let {
                    val input = context?.contentResolver?.openInputStream(selectedImage)
                    lateinit var file: File
                    try {
                        file = File(context?.cacheDir, "file.jpg")
                        FileOutputStream(file).use { output ->
                            val buffer = ByteArray(4 * 1024)
                            var read: Int
                            while (input!!.read(buffer).also { read = it } != -1) {
                                output.write(buffer, 0, read)
                            }
                            output.flush()
                        }
                    } finally {
                        input!!.close()
                    }
                    profilePresenter.uploadPhoto(file)
                }
            }
        }
    }

    private fun showEditInfoDialog() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        builder.setTitle(getString(R.string.edit_profile_info))
        val viewInflated: View = LayoutInflater.from(context).inflate(R.layout.dialog_edit_user_info, view as ViewGroup?, false)
        viewInflated.nameEdit.text = Editable.Factory.getInstance().newEditable(user.name)
        viewInflated.usernameEdit.text = Editable.Factory.getInstance().newEditable(user.username)
        builder.setView(viewInflated)
        builder.setPositiveButton(
            R.string.save,
            DialogInterface.OnClickListener { _, _ ->

            }
        )
        builder.setNegativeButton(
            android.R.string.cancel,
            DialogInterface.OnClickListener{ _, _ -> }
        )

        val dialog: AlertDialog = builder.create()
        dialog.setOnShowListener {
            dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener {
                var error = false
                viewInflated.nameLayoutEdit.error = null
                viewInflated.usernameLayoutEdit.error = null
                val nameText = viewInflated.nameEdit.text.toString()
                val usernameText = viewInflated.usernameEdit.text.toString()
                if(nameText.isEmpty()){
                    viewInflated.nameLayoutEdit.error = getString(R.string.name_empty_error)
                    error = true
                }
                if(nameText.length <= 3){
                    viewInflated.nameLayoutEdit.error = getString(R.string.name_short_error)
                    error = true
                }
                if(!android.util.Patterns.EMAIL_ADDRESS.matcher(usernameText).matches()){
                    viewInflated.usernameLayoutEdit.error = getString(R.string.username_invalid_error)
                    error = true
                }
                if(usernameText.isEmpty()){
                    viewInflated.usernameLayoutEdit.error = getString(R.string.username_empty_error)
                    error = true
                }

                if(!error){
                    profilePresenter.updateInfo(usernameText, nameText)
                    dialog.dismiss()
                }
            }
        }

        dialog.show()
        dialog.setCancelable(false)
    }

}