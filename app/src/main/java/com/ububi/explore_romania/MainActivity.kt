package com.ububi.explore_romania

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ububi.explore_romania.ui.theme.Explore_romaniaTheme
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Explore_romaniaTheme {
                RomaniaLicensePlateBoardScreen()
            }
        }
    }
}

@Composable
fun RomaniaLicensePlateBoardScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF0A1733), // Deep Romanian night blue
                        Color(0xFF1A2A5F),
                        Color(0xFF2D4B73)
                    )
                )
            )
            .padding(20.dp)
    ) {
        RomaniaPlateBoard()
    }
}

@Composable
fun RomaniaPlateBoard() {
    Column(
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        // TOP ROW - 5 plates
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            val topCounties = listOf("B", "CJ", "TM", "IS", "PH")
            topCounties.forEach { county ->
                RomanianPlate(
                    countyCode = county,
                    number = String.format("%02d", Random.nextInt(1, 100)),
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // MIDDLE: Left + Center + Right
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // LEFT column (2 plates)
            Column(
                verticalArrangement = Arrangement.spacedBy(20.dp),
                modifier = Modifier.fillMaxHeight()
            ) {
                RomanianPlate(countyCode = "AG", number = "07", modifier = Modifier.weight(1f))
                RomanianPlate(countyCode = "VL", number = "23", modifier = Modifier.weight(1f))
            }

            // CENTER - Main game area
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(32.dp))
                    .background(Color(0xFF212121))
                    .border(8.dp, Color(0xFF424242), RoundedCornerShape(32.dp))
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(40.dp)
                ) {
                    // Two big letters (like in your screenshot)
                    Row(horizontalArrangement = Arrangement.spacedBy(50.dp)) {
                        BigCenterLetter("T")
                        BigCenterLetter("G")
                    }

                    Text(
                        text = "EXPLORE ROMANIA",
                        fontSize = 52.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White,
                        letterSpacing = 6.sp
                    )

                    Text(
                        text = "Colectează județe • Călătorește • Descoperă",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFFB0BEC5)
                    )

                    // Bonus: Player name bubble like in your pic
                    Box(
                        modifier = Modifier
                            .offset(x = 80.dp, y = 20.dp)
                            .background(Color(0xFFE91E63), RoundedCornerShape(30.dp))
                            .padding(horizontal = 20.dp, vertical = 10.dp)
                    ) {
                        Text(
                            text = "ALEXANDRA-ISA...",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }
                }
            }

            // RIGHT column (2 plates)
            Column(
                verticalArrangement = Arrangement.spacedBy(20.dp),
                modifier = Modifier.fillMaxHeight()
            ) {
                RomanianPlate(countyCode = "CT", number = "77", modifier = Modifier.weight(1f))
                RomanianPlate(countyCode = "BR", number = "01", modifier = Modifier.weight(1f))
            }
        }

        // BOTTOM ROW - 5 plates
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            val bottomCounties = listOf("IF", "GL", "HR", "SM", "BH")
            bottomCounties.forEach { county ->
                RomanianPlate(
                    countyCode = county,
                    number = String.format("%02d", Random.nextInt(1, 100)),
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

// Authentic Romanian License Plate
@Composable
fun RomanianPlate(
    countyCode: String,
    number: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .aspectRatio(2.15f)
            .shadow(16.dp, RoundedCornerShape(14.dp)),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Blue EU strip with RO
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(44.dp)
                    .background(Color(0xFF003087))
                    .padding(4.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "RO",
                    color = Color.Yellow,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 18.sp
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // County code + number
            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = countyCode,
                    color = Color.Black,
                    fontSize = 34.sp,
                    fontWeight = FontWeight.ExtraBold
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = number,
                    color = Color.Black,
                    fontSize = 44.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 3.sp
                )
            }
        }
    }
}

// Big letters in the center (T and G style)
@Composable
fun BigCenterLetter(letter: String) {
    Box(
        modifier = Modifier
            .size(110.dp)
            .background(Color(0xFF37474F), RoundedCornerShape(20.dp))
            .border(8.dp, Color(0xFF546E7A), RoundedCornerShape(20.dp)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = letter,
            fontSize = 84.sp,
            fontWeight = FontWeight.Black,
            color = Color(0xFFEEEEEE)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewRomaniaBoard() {
    Explore_romaniaTheme {
        RomaniaLicensePlateBoardScreen()
    }
}