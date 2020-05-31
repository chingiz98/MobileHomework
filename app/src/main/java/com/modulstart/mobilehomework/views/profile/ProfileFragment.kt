package com.modulstart.mobilehomework.views.profile

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.modulstart.mobilehomework.App
import com.modulstart.mobilehomework.R
import com.modulstart.mobilehomework.presenters.ProfilePresenter
import com.modulstart.mobilehomework.repository.models.User
import com.modulstart.mobilehomework.repository.profile.ProfileRepository
import com.modulstart.mobilehomework.views.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_profile.*
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
    }

    override fun showLoading() {

    }

    override fun showProfile(user: User) {
        profileName.text = user.name
        profileUsername.text = user.username
        profileStatus.text = user.status
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

}