package com.cardscanner.app.utils

object TextParser {
    
    data class ParsedCard(
        val name: String,
        val address: String
    )
    
    fun parseCardText(text: String): ParsedCard {
        val lines = text.split("\n").map { it.trim() }.filter { it.isNotEmpty() }
        
        if (lines.isEmpty()) {
            return ParsedCard("Unknown", "No address found")
        }
        
        // Simple heuristic: First non-empty line is likely the name
        val name = lines.firstOrNull()?.takeIf { it.isNotBlank() } ?: "Unknown"
        
        // Look for address patterns
        val addressLines = mutableListOf<String>()
        val addressKeywords = listOf("street", "st", "road", "rd", "avenue", "ave", "lane", "ln", 
                                     "drive", "dr", "court", "ct", "place", "pl", "city", "state", "zip")
        
        for (i in 1 until lines.size) {
            val line = lines[i].lowercase()
            
            // Check if line contains address keywords or numbers (street numbers, zip codes)
            if (addressKeywords.any { line.contains(it) } || 
                line.any { it.isDigit() } && line.length > 3) {
                addressLines.add(lines[i])
            }
        }
        
        // If no address found using keywords, take lines after name that contain numbers
        if (addressLines.isEmpty()) {
            for (i in 1 until lines.size) {
                if (lines[i].any { it.isDigit() }) {
                    addressLines.add(lines[i])
                }
            }
        }
        
        val address = if (addressLines.isNotEmpty()) {
            addressLines.joinToString(", ")
        } else {
            "No address found"
        }
        
        return ParsedCard(name, address)
    }
}