package com.guardmonitor.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.telephony.TelephonyManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestPermissions()
        setContent {
            GuardMonitorTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFF0A0E27)
                ) {
                    GuardMonitorScreen()
                }
            }
        }
    }

    private fun requestPermissions() {
        val permissions = listOf(
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.POST_NOTIFICATIONS
        )
        permissions.forEach { perm ->
            if (ContextCompat.checkSelfPermission(this, perm) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(perm), 0)
            }
        }
    }
}

@Composable
fun GuardMonitorScreen() {
    var selectedTab by remember { mutableStateOf(0) }
    var is2GBlocked by remember { mutableStateOf(false) }
    var isTeliaBlocked by remember { mutableStateOf(false) }
    var operatorStatus by remember { mutableStateOf("Hallon (064) ✅") }
    var isTeliaDetected by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0A0E27))
    ) {
        Header(isTeliaDetected, operatorStatus)

        TabRow(
            selectedTabIndex = selectedTab,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF1A1F3A)),
            containerColor = Color(0xFF1A1F3A),
            contentColor = Color.White
        ) {
            Tab(selectedTab == 0, { selectedTab = 0 }) { Text("Dashboard") }
            Tab(selectedTab == 1, { selectedTab = 1 }) { Text("Alarmer") }
            Tab(selectedTab == 2, { selectedTab = 2 }) { Text("Settings") }
        }

        when (selectedTab) {
            0 -> DashboardTab(is2GBlocked, isTeliaBlocked, { is2GBlocked = !is2GBlocked }, { isTeliaBlocked = !isTeliaBlocked })
            1 -> AlarmsTab()
            2 -> SettingsTab()
        }
    }
}

@Composable
fun Header(isAlert: Boolean, status: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(if (isAlert) Color(0xFF4A0000) else Color(0xFF1A3A1A))
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Guard Monitor", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.White)
            Icon(
                imageVector = if (isAlert) Icons.Filled.Warning else Icons.Filled.CheckCircle,
                contentDescription = "Status",
                tint = if (isAlert) Color(0xFFFF4444) else Color(0xFF44FF44),
                modifier = Modifier.size(40.dp)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text("Operatör: $status", fontSize = 14.sp, color = if (isAlert) Color(0xFFFF6B6B) else Color(0xFF6BFF6B))
    }
}

@Composable
fun DashboardTab(is2GBlocked: Boolean, isTeliaBlocked: Boolean, on2G: () -> Unit, onTelia: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(Color(0xFF0A0E27))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        ToggleCard("2G/3G Blocking", is2GBlocked, on2G)
        ToggleCard("Telia Blocking", isTeliaBlocked, onTelia)
        
        Button(onClick = { }, modifier = Modifier.fillMaxWidth().height(48.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))) {
            Text("Exportera Rapporter")
        }
    }
}

@Composable
fun AlarmsTab() {
    Column(modifier = Modifier.fillMaxSize().background(Color(0xFF0A0E27)).padding(16.dp)) {
        Text("Alarmhistorik", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
        Spacer(modifier = Modifier.height(8.dp))
        Text("Inga alarmer än ✅", color = Color.Gray)
    }
}

@Composable
fun SettingsTab() {
    Column(modifier = Modifier.fillMaxSize().background(Color(0xFF0A0E27)).padding(16.dp)) {
        Text("Inställningar", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
        Text("2G/3G Blocking: ☐", color = Color.Gray, modifier = Modifier.padding(top = 16.dp))
        Text("Telia Blocking: ☐", color = Color.Gray)
    }
}

@Composable
fun ToggleCard(title: String, isOn: Boolean, onToggle: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1F3A))) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(title, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
            Checkbox(checked = isOn, onCheckedChange = { onToggle() },
                colors = CheckboxDefaults.colors(checkedColor = Color(0xFF4CAF50)))
        }
    }
}

@Composable
fun GuardMonitorTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = darkColorScheme(
            primary = Color(0xFF2196F3),
            secondary = Color(0xFF4CAF50),
            background = Color(0xFF0A0E27),
            surface = Color(0xFF1A1F3A)
        )
    ) {
        content()
    }
}
