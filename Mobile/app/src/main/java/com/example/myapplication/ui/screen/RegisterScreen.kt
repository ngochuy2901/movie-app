package com.example.myapplication.ui.screen

import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.R
import com.example.myapplication.auth.Auth
import com.example.myapplication.data.dto.LoginRequest
import com.example.myapplication.data.model.User
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RegisterScreen(navHostController: NavHostController) {
    var fullName by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }

    // Dropdown giới tính
    val genders = listOf("Nam", "Nữ", "Khác")
    var gender by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    // Birth date
    var birthDate by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()  // Coroutine scope

    val context = LocalContext.current
    val auth = Auth(context)

    Column(
        modifier = Modifier
            .padding(20.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Đăng ký tài khoản",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 20.dp)
        )

        OutlinedTextField(
            value = fullName,
            onValueChange = { fullName = it },
            label = { Text("Họ tên") },
            placeholder = { Text("Nhập họ tên") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Tài khoản") },
            placeholder = { Text("Nhập tài khoản") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Mật khẩu") },
            placeholder = { Text("Nhập mật khẩu") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            placeholder = { Text("Nhập email") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = phoneNumber,
            onValueChange = { phoneNumber = it },
            label = { Text("Số điện thoại") },
            placeholder = { Text("Nhập số điện thoại") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(16.dp))

        // Dropdown giới tính
        Box {
            OutlinedTextField(
                value = gender,
                onValueChange = {},
                label = { Text("Giới tính") },
                readOnly = true,
                trailingIcon = {
                    Icon(
                        painter = painterResource(R.drawable.icon_dropdown),
                        contentDescription = null,
                        Modifier.clickable { expanded = true }
                    )
                },
                modifier = Modifier.fillMaxWidth()
            )

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                genders.forEach { item ->
                    DropdownMenuItem(
                        text = { Text(item) },
                        onClick = {
                            gender = item
                            expanded = false
                        }
                    )
                }
            }
        }

        Spacer(Modifier.height(24.dp))

        BirthDatePicker(
            selectedDate = birthDate,
            onDateSelected = { birthDate = it }
        )
        Spacer(Modifier.height(24.dp))
        Button(
            onClick = {
                coroutineScope.launch {
                    val newUser = User(
                        fullName = fullName,
                        username = username,
                        password = password,
                        email = email,
                        phoneNumber = phoneNumber,
                        gender = gender,
                        dateOfBirth = birthDate
                    )
                    val response = auth.register(newUser)
                    Log.d("AuthRegister", "Response: $response")
                    if (response != null) {
                        val loginResponse = auth.login(LoginRequest(username, password))
                        Toast.makeText(
                            context,
                            "Đăng ký thành công: ${response.fullname}",
                            Toast.LENGTH_SHORT
                        ).show()
                        navHostController.navigate("home") {
                            popUpTo("register") { inclusive = true }
                        }
                    } else {
                        Toast.makeText(
                            context,
                            "Đăng ký thất bại",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

            },
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium
        ) {
            Text("Đăng ký")
        }

        Spacer(Modifier.height(16.dp))

        Text(
            text = "Đã có tài khoản? Đăng nhập",
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.clickable {
                navHostController.navigate("login")
            }
        )
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
@Preview
fun RegisterPreview() {
    RegisterScreen(rememberNavController())
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BirthDatePicker(
    selectedDate: String,
    onDateSelected: (String) -> Unit
) {
    var showPicker by remember { mutableStateOf(false) }

    if (showPicker) {
        val dateState = rememberDatePickerState()

        DatePickerDialog(
            onDismissRequest = { showPicker = false },
            confirmButton = {
                TextButton(onClick = {
                    dateState.selectedDateMillis?.let {
                        val date = Instant.ofEpochMilli(it)
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate()
                            .toString()
                        onDateSelected(date)
                    }
                    showPicker = false
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showPicker = false }) {
                    Text("Huỷ")
                }
            }
        ) {
            DatePicker(state = dateState)
        }
    }

    Column {
        OutlinedTextField(
            value = selectedDate,
            onValueChange = {},
//            label = { Text("Ngày sinh: $selectedDate") },
            readOnly = true,

            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                Icon(
                    painter = painterResource(R.drawable.icon_dropdown),
                    contentDescription = null,
                    Modifier.clickable { showPicker = true }
                )
            },
        )
    }
}

