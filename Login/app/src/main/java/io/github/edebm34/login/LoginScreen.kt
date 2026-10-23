package io.github.edebm34.login

import android.util.Patterns
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun LoginScreen(
    onLoginSuccess: (String) -> Unit,   // Ahora recibe el email
    onGoToRegister: () -> Unit          // Nuevo callback
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf(false) }
    var passwordError by remember { mutableStateOf(false) }
    var showPassword by remember { mutableStateOf(false) }
    var recordarSesión by remember { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current
    val emailFocusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        emailFocusRequester.requestFocus()
    }

    fun validarEmail(): Boolean {
        val emailValido = email.isNotBlank() &&
                Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches()
        emailError = !emailValido
        return emailValido
    }

    fun validarPassword(): Boolean {
        val regex = "^(?=.*[A-Z])(?=.*[0-9]).{8,}$".toRegex()
        val passValida = regex.matches(password)
        passwordError = !passValida
        return passValida
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(modifier = Modifier.height(48.dp))

        Icon(
            painter = painterResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = stringResource(R.string.logo_desc),
            modifier = Modifier.size(120.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = stringResource(R.string.titulo_bienvenida),
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
                if (it.isNotEmpty()) validarEmail()
            },
            label = { Text(stringResource(R.string.hint_email)) },
            isError = emailError,
            supportingText = { if (emailError) Text(stringResource(R.string.error_email)) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    focusManager.moveFocus(FocusDirection.Down)
                }
            ),
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(emailFocusRequester)
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
                if (it.isNotEmpty()) validarPassword()
            },
            label = { Text(stringResource(R.string.hint_password)) },
            isError = passwordError,
            supportingText = { if (passwordError) Text(stringResource(R.string.error_password)) },
            visualTransformation = if (showPassword) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            },
            trailingIcon = {
                IconButton(onClick = { showPassword = !showPassword }) {
                    Icon(
                        imageVector = if (showPassword) {
                            Icons.Filled.Visibility
                        } else {
                            Icons.Filled.VisibilityOff
                        },
                        contentDescription = stringResource(R.string.toggle_password)
                    )
                }
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                }
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Checkbox(
                checked = recordarSesión,
                onCheckedChange = {
                    recordarSesión = !recordarSesión
                }
            )
            Text(stringResource(R.string.recordar_sesion))
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                val emailOk = validarEmail()
                val passOk = validarPassword()
                if (emailOk && passOk) {
                    focusManager.clearFocus()
                    onLoginSuccess(email.trim())   // Pasamos el email
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.boton_entrar))
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(
            onClick = { /* Acción */ },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text(stringResource(R.string.olvidaste_password))
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = onGoToRegister) {
            Text(stringResource(R.string.no_tienes_cuenta))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    MaterialTheme {
        LoginScreen(onLoginSuccess = { _ -> }, onGoToRegister = {})
    }
}