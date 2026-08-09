package com.example.ui.screens

import android.graphics.Bitmap
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.local.entity.TournamentEntity
import com.example.data.remote.AiVerificationResult
import com.example.ui.theme.CardBorder
import com.example.ui.theme.DarkCanvas
import com.example.ui.theme.LightCanvas
import com.example.ui.theme.LiveGreen
import com.example.ui.theme.MintAccent
import com.example.ui.theme.MintBorder
import com.example.ui.theme.MutedTextDark
import com.example.ui.theme.MutedTextLight
import com.example.ui.theme.SubCardBackground

@Composable
fun PaymentScreen(
    tournaments: List<TournamentEntity>,
    selectedTournament: TournamentEntity?,
    isVerifying: Boolean,
    verificationResult: AiVerificationResult?,
    onVerifyPayment: (TournamentEntity, String, String, String, String, Bitmap?) -> Unit,
    onClearResult: () -> Unit,
    modifier: Modifier = Modifier
) {
    var activeTourney by remember(selectedTournament, tournaments) {
        mutableStateOf(selectedTournament ?: tournaments.firstOrNull())
    }

    var teamName by remember { mutableStateOf("") }
    var freeFireUid by remember { mutableStateOf("") }
    var upiSenderName by remember { mutableStateOf("Sangam Nishad") }
    var utrNumber by remember { mutableStateOf("") }

    val clipboardManager = LocalClipboardManager.current
    var copiedNote by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(LightCanvas)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(8.dp))
            Column {
                Text(
                    text = "SECURE ENTRY & PAYMENTS",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.5.sp,
                        color = MutedTextLight
                    )
                )
                Text(
                    text = "UPI Pay & AI Verification",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = DarkCanvas
                    )
                )
            }
        }

        // QR Code Card matching the design theme snippet
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("upi_qr_card"),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = DarkCanvas)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "SECURE ENTRY",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.5.sp,
                                    color = MutedTextDark
                                )
                            )
                            Text(
                                text = "Scan to Pay",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            )
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(Color(0xFF2B2E2A))
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF60A5FA))
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "AI Intelligent Check Active",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // QR Code Image Box
                    Box(
                        modifier = Modifier
                            .size(200.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .background(Color.White)
                            .border(2.dp, MintBorder, RoundedCornerShape(20.dp))
                            .padding(12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.img_upi_qr),
                            contentDescription = "UPI QR Code for sangam nishad sangamnishad@fam",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Fit
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "sangam nishad",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "sangamnishad@fam",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontFamily = FontFamily.Monospace,
                                color = MintBorder,
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        IconButton(
                            onClick = {
                                clipboardManager.setText(AnnotatedString("sangamnishad@fam"))
                                copiedNote = true
                            },
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ContentCopy,
                                contentDescription = "Copy UPI ID",
                                tint = MintAccent,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }

                    if (copiedNote) {
                        Text(
                            text = "UPI ID copied to clipboard!",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = LiveGreen,
                                fontSize = 10.sp
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Scan to pay with any UPI app (PhonePe, GPay, Paytm, BHIM)",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = MutedTextDark,
                            fontSize = 10.sp
                        )
                    )
                }
            }
        }

        // Tournament Organised Intelligence Payment Form
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("payment_form_card"),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(MintAccent)
                                .padding(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = null,
                                tint = DarkCanvas,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Tournament Payment Intelligence",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = DarkCanvas
                                )
                            )
                            Text(
                                text = "Submit UTR Ref ID for AI instant verification",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontSize = 11.sp,
                                    color = MutedTextLight
                                )
                            )
                        }
                    }

                    // Tournament Entry Fee Info Box
                    activeTourney?.let { tourney ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .background(SubCardBackground)
                                .padding(12.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = "Selected Event",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = MutedTextLight
                                        )
                                    )
                                    Text(
                                        text = tourney.title,
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = DarkCanvas
                                        )
                                    )
                                }

                                Column(horizontalAlignment = Alignment.End) {
                                    Text(
                                        text = "Entry Fee Required",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = MutedTextLight
                                        )
                                    )
                                    Text(
                                        text = "₹${tourney.entryFee}",
                                        style = MaterialTheme.typography.titleLarge.copy(
                                            fontWeight = FontWeight.Black,
                                            color = DarkCanvas
                                        )
                                    )
                                }
                            }
                        }
                    }

                    OutlinedTextField(
                        value = teamName,
                        onValueChange = { teamName = it },
                        label = { Text("Team Name") },
                        placeholder = { Text("e.g. Team_Viper") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("input_team_name"),
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = DarkCanvas,
                            unfocusedBorderColor = CardBorder
                        ),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = freeFireUid,
                        onValueChange = { freeFireUid = it },
                        label = { Text("Leader Free Fire In-Game UID") },
                        placeholder = { Text("e.g. 8429103982") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("input_ff_uid"),
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = DarkCanvas,
                            unfocusedBorderColor = CardBorder
                        ),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = utrNumber,
                        onValueChange = { utrNumber = it },
                        label = { Text("UPI UTR / Ref Transaction No (12 Digits)") },
                        placeholder = { Text("e.g. 428910398201") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("input_utr_no"),
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = DarkCanvas,
                            unfocusedBorderColor = CardBorder
                        ),
                        singleLine = true
                    )

                    Button(
                        onClick = {
                            activeTourney?.let { t ->
                                onVerifyPayment(t, teamName, freeFireUid, upiSenderName, utrNumber, null)
                            }
                        },
                        enabled = !isVerifying && teamName.isNotBlank() && freeFireUid.isNotBlank() && utrNumber.isNotBlank(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .testTag("verify_payment_btn"),
                        shape = RoundedCornerShape(18.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MintAccent,
                            contentColor = DarkCanvas
                        )
                    ) {
                        if (isVerifying) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(22.dp),
                                color = DarkCanvas,
                                strokeWidth = 2.dp
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("AI Agent Auditing Payment...", fontWeight = FontWeight.Bold)
                        } else {
                            Icon(
                                imageVector = Icons.Default.VerifiedUser,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Verify Payment & Unlock Room ID", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // AI Verification Result Card
        item {
            AnimatedVisibility(visible = verificationResult != null) {
                verificationResult?.let { res ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("verification_result_card"),
                        shape = RoundedCornerShape(28.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (res.isVerified) DarkCanvas else Color(0xFFFEF2F2)
                        ),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (res.isVerified) MintBorder else Color(0xFFFECACA)
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = null,
                                        tint = if (res.isVerified) LiveGreen else Color(0xFFDC2626),
                                        modifier = Modifier.size(24.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = res.statusLabel,
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = if (res.isVerified) Color.White else Color(0xFF991B1B)
                                        )
                                    )
                                }

                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(if (res.isVerified) MintAccent else Color(0xFFFCA5A5))
                                        .padding(horizontal = 10.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = "${res.confidence} AI Confidence",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = DarkCanvas
                                        )
                                    )
                                }
                            }

                            Text(
                                text = res.summaryNotes,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = if (res.isVerified) MintBorder else Color(0xFF7F1D1D),
                                    fontSize = 12.sp
                                )
                            )

                            if (res.isVerified && activeTourney != null) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(16.dp))
                                        .background(Color(0xFF2B2E2A))
                                        .padding(14.dp)
                                ) {
                                    Column {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(
                                                imageVector = Icons.Default.Key,
                                                contentDescription = null,
                                                tint = MintAccent,
                                                modifier = Modifier.size(16.dp)
                                            )
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text(
                                                text = "UNLOCKED TOURNAMENT ROOM ACCESS",
                                                style = MaterialTheme.typography.labelSmall.copy(
                                                    fontWeight = FontWeight.Bold,
                                                    letterSpacing = 1.sp,
                                                    color = MintAccent
                                                )
                                            )
                                        }

                                        Spacer(modifier = Modifier.height(8.dp))

                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            val roomId = activeTourney?.roomId.orEmpty().ifEmpty { "FFMAX-8841" }
                                            val roomPass = activeTourney?.roomPassword.orEmpty().ifEmpty { "991" }
                                            Text(
                                                text = "Room ID: $roomId",
                                                style = MaterialTheme.typography.bodyMedium.copy(
                                                    fontFamily = FontFamily.Monospace,
                                                    fontWeight = FontWeight.Bold,
                                                    color = Color.White
                                                )
                                            )
                                            Text(
                                                text = "Pass: $roomPass",
                                                style = MaterialTheme.typography.bodyMedium.copy(
                                                    fontFamily = FontFamily.Monospace,
                                                    fontWeight = FontWeight.Bold,
                                                    color = Color.White
                                                )
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
