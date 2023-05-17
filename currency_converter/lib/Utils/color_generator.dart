// Import dart:math to use Random
import 'dart:math';
import 'package:flutter/material.dart';
import '../models/currency.dart';

// Define a class that has a function to generate random colors
class ColorGenerator {
// Create a Random object
  final random = Random();

// Define a function that returns a list of random colors
  List<Color> generateRandomColors(int length) {
// Use List.generate and Random to create a list of colors
    return List.generate(length, (index) {
// Generate random RGB values
      int r = random.nextInt(256);
      int g = random.nextInt(256);
      int b = random.nextInt(256);
// Create a Color object with the RGB values
      return Color.fromARGB(255, r, g, b);
    });
  }

  Future<Map<String, Color>> getColorsToCurrencies(List<Currency> currencies) async {
    // Generate a list of random colors with the same length as the currenciesList
    final colorList = generateRandomColors(currencies.length);

    final currencyCodes = currencies.map((currency) => currency.code).toList();

    // Create a map that maps each code to a color
    final colors = Map.fromIterables(currencyCodes, colorList);

    return Future.value(colors);
  }

}