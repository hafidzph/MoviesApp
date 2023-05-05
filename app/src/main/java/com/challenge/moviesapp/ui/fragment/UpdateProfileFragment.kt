package com.challenge.moviesapp.ui.fragment

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.NumberPicker
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.marginTop
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.challenge.moviesapp.R
import com.challenge.moviesapp.databinding.FragmentUpdateProfileBinding
import com.challenge.moviesapp.ui.viewmodel.UpdateProfileViewModel
import java.util.Calendar

class UpdateProfileFragment : Fragment() {
    var binding: FragmentUpdateProfileBinding? = null
    private val updateVM: UpdateProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUpdateProfileBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding!!.apply {
            etTglLahir.setOnClickListener {
                showDatePickerDialog()
            }

            btnUpdate.setOnClickListener {
                val username = etUsername.text.toString()
                val full_name = etNama.text.toString()
                val birth_date = etTglLahir.text.toString()
                val address = etAlamat.text.toString()

                if(username.isNotEmpty() && full_name.isNotEmpty() && birth_date.isNotEmpty() && address.isNotEmpty()){
                    updateProfile(username, full_name, birth_date, address)
                }else{
                    Toast.makeText(requireContext(), "Field must be filled", Toast.LENGTH_SHORT).show()
                }
            }

            btnLogout.setOnClickListener {
                updateVM.signOut(findNavController())
                updateVM.logout.observe(viewLifecycleOwner){
                    requireActivity().finish()
                }
                updateVM.clear()
            }
        }
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val currentYear = calendar.get(Calendar.YEAR)
        val currentMonth = calendar.get(Calendar.MONTH)
        val currentDay = calendar.get(Calendar.DAY_OF_MONTH)

        val dayPicker = NumberPicker(requireContext())
        dayPicker.minValue = 1
        dayPicker.maxValue = 31
        dayPicker.value = currentDay
        dayPicker.setFormatter { String.format("%02d", it) }

        val monthPicker = NumberPicker(requireContext())
        monthPicker.minValue = 1
        monthPicker.maxValue = 12
        monthPicker.displayedValues = arrayOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")
        monthPicker.value = currentMonth + 1

        val yearPicker = NumberPicker(requireContext())
        yearPicker.minValue = 1900
        yearPicker.maxValue = currentYear
        yearPicker.value = currentYear

        val pickerLayout = LinearLayout(requireContext())
        pickerLayout.orientation = LinearLayout.HORIZONTAL
        pickerLayout.gravity = Gravity.CENTER
        pickerLayout.addView(dayPicker)
        pickerLayout.addView(monthPicker)
        pickerLayout.addView(yearPicker)

        val customTitleView = LayoutInflater.from(requireContext()).inflate(R.layout.custom_dialog_title, null)

        AlertDialog.Builder(requireContext())
            .setCustomTitle(customTitleView)
            .setView(pickerLayout)
            .setPositiveButton("OK") { _, _ ->
                val selectedDate = "${String.format("%02d", dayPicker.value)}-${String.format("%02d", monthPicker.value)}-${yearPicker.value}"
                binding!!.etTglLahir.setText(selectedDate)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    fun updateProfile(username: String, full_name: String, birth_date: String, address: String){
        updateVM.updateProfile(username, full_name, birth_date, address)
    }
}