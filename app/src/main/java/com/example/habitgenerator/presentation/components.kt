package com.example.habitgenerator.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.habitgenerator.services.Habit
import com.example.habitgenerator.services.HabitType

@Composable
fun EditHabit(
    habit: Habit = Habit(),
    expanded: Boolean = true
) {

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            CircleShape("A")
            Spacer(Modifier.width(8.dp))
            OutlinedTextField(
                value = habit.name,
                onValueChange = {},
                label = {Text("single habit")},
                modifier = Modifier.fillMaxWidth(fraction = 0.6f)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.fillMaxWidth()
            ) {
                Checkbox(
                    checked = false,
                    onCheckedChange = {}
                )
                IconButton(onClick = {}) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = ""
                    )
                }
            }
        }
        if (expanded) {
            SingleHabitPart(habit)
        }
    }
}

@Composable
fun SingleHabitPart(
    habit: Habit,
    singlePart: HabitType.SingleHabit = HabitType.SingleHabit(
        hashMapOf(Pair(3,"first"))
    )
) {
   Column {
       Row(
           horizontalArrangement = Arrangement.SpaceBetween,
           verticalAlignment = Alignment.CenterVertically,
           modifier = Modifier.padding(horizontal = 8.dp).fillMaxWidth()
       ) {

           OutlinedTextField(
               value = habit.startFrom.toString(),
               onValueChange = {},
               label = { Text("start from") }
           )
           IconButton(onClick = {}) {
               Icon(
                   imageVector = Icons.Default.Delete,
                   contentDescription = ""
               )
           }
       }
       singlePart.streakName?.let { streakNames ->
           for (pair in streakNames.toList()) {
               Row(
                   horizontalArrangement = Arrangement.SpaceBetween,
                   verticalAlignment = Alignment.CenterVertically,
                   modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp)
               ) {
                   OutlinedTextField(
                       value = pair.second,
                       onValueChange = {},
                       label = { Text("name") }
                   )
                   OutlinedTextField(
                       value = pair.first.toString(),
                       onValueChange = {},
                       label = { Text("start from") }
                   )
               }

           }
       }
   }
}

@Preview
@Composable
private fun EditHabitPreview() {
    EditHabit()
}