package me.hatcloud.sms2mail.ui

import android.content.res.Configuration.UI_MODE_TYPE_NORMAL
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import me.hatcloud.sms2mail.R
import me.hatcloud.sms2mail.data.SecurityType
import me.hatcloud.sms2mail.ui.theme.Sms2MailTypography
import me.hatcloud.sms2mail.utils.ConfigurationUtil

@Composable
fun ConfigurationPage() {
    val securityOptions = SecurityType.values().toList()
    var isEditMode by remember {
        mutableStateOf(false)
    }

    val configuration = ConfigurationUtil.configuration.copy()

    var email by rememberSaveable {
        mutableStateOf(configuration.email ?: "")
    }

    var password by rememberSaveable {
        mutableStateOf(configuration.password)
    }

    var smtpHost by rememberSaveable {
        mutableStateOf(configuration.smtpHost ?: "")
    }

    var smtpPort by rememberSaveable {
        mutableStateOf(configuration.smtpPort ?: "")
    }

    var securityType by rememberSaveable {
        mutableStateOf(configuration.securityType)
    }

    var emailToForward by rememberSaveable {
        mutableStateOf(configuration.emailToForward ?: "")
    }


    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
        ConfigurationSection(R.string.configuration_label_account)
        ConfigurationTextField(
            hintRes = R.string.email,
            text = email,
            enabled = isEditMode,
            keyboardType = KeyboardType.Email,
            onValueChange = {
                email = it
            }
        )
        ConfigurationTextField(
            hintRes = R.string.password,
            text = password,
            enabled = isEditMode,
            keyboardType = KeyboardType.Password,
            onValueChange = {
                password = it
            }
        )

        ConfigurationSection(R.string.smtp)
        ConfigurationTextField(
            hintRes = R.string.host_name,
            text = smtpHost,
            enabled = isEditMode,
            onValueChange = {
                smtpHost = it
            }
        )
        ConfigurationTextField(
            hintRes = R.string.port,
            text = smtpPort,
            enabled = isEditMode,
            keyboardType = KeyboardType.Number,
            onValueChange = {
                smtpPort = it
            }
        )

        ConfigurationSection(R.string.security)
        ConfigurationSecuritySelector(
            securityOptions = securityOptions,
            selectedSecurityType = securityType,
            enabled = isEditMode,
        ) {
            securityType = it
        }

        ConfigurationSection(R.string.configuration_label_forward_to)
        ConfigurationTextField(
            hintRes = R.string.configuration_text_email_to_forward,
            text = emailToForward,
            enabled = isEditMode,
            keyboardType = KeyboardType.Email,
            onValueChange = {
                emailToForward = it
            }
        )

        Button(
            onClick = {
                isEditMode = !isEditMode
                if (isEditMode) {
                    ConfigurationUtil.configuration = configuration.apply {
                        this.email = email
                        this.password = password
                        this.smtpHost = email
                        this.smtpPort = smtpPort
                        this.securityType = securityType
                        this.emailToForward = emailToForward
                    }
                }
            },
            modifier = Modifier
                .padding(vertical = 16.dp)
                .fillMaxWidth()
                .height(40.dp)

        ) {
            Text(
                text = stringResource(id = if (isEditMode) R.string.apply else R.string.edit)
            )
        }
    }

}

@Composable
fun ConfigurationSection(@StringRes textRes: Int) {
    val sectionModifier = Modifier.padding(horizontal = 4.dp, vertical = 4.dp)
    val sectionStyle = Sms2MailTypography.subtitle1
    Text(
        text = stringResource(id = textRes),
        style = sectionStyle,
        modifier = sectionModifier
    )
}

@Composable
fun ConfigurationTextField(
    @StringRes hintRes: Int,
    text: String,
    enabled: Boolean,
    keyboardType: KeyboardType = KeyboardType.Text,
    onValueChange: (String) -> Unit
) {
    val itemModifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight(Alignment.CenterVertically)

    val visualTransformation = if (keyboardType == KeyboardType.Password)
        PasswordVisualTransformation()
    else
        VisualTransformation.None

    OutlinedTextField(
        value = text,
        onValueChange = {
            if (enabled) {
                onValueChange(it)
            }
        },
        label = {
            Text(
                text = stringResource(id = hintRes),
                modifier = Modifier.wrapContentSize(),
            )
        },
        visualTransformation = visualTransformation,
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType,
            imeAction = ImeAction.Next
        ),
        enabled = enabled,
        singleLine = true,
        modifier = itemModifier
    )
}

@Composable
fun ConfigurationSecuritySelector(
    securityOptions: List<SecurityType>,
    selectedSecurityType: SecurityType,
    enabled: Boolean,
    onSelect: (securityType: SecurityType) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 8.dp)
    ) {
        securityOptions.forEach { securityType ->
            RadioButton(
                selected = selectedSecurityType == securityType,
                enabled = enabled,
                onClick = {
                    onSelect(securityType)
                }
            )
            Text(text = securityType.name)
            Spacer(modifier = Modifier.width(4.dp))
        }
    }
}

@Preview(
    uiMode = UI_MODE_TYPE_NORMAL,
    showBackground = true
)
@Composable
fun PreviewConfigurationPage() {
    ConfigurationPage()
}