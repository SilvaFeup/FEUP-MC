import 'package:flutter/material.dart';

import 'models/currency.dart';

// A list of currencies in the user's wallet
List<Currency> currencies = [
  Currency(name: 'US Dollar', code: 'USD', symbol: '\$', amount: 100, rate: 1),
  Currency(name: 'Euro', code: 'EUR', symbol: '€', amount: 50, rate: 0.89),
  Currency(name:  'British Pound', code: 'GBP', symbol: '£', amount: 35, rate: 0.77),
];

// A map of colors for each currency
Map<String, Color> colors = {
  'USD': Colors.blue,
  'EUR': Colors.green,
  'GBP': Colors.red,
};

// A constant for the base currency
const String base = 'USD';

// A constant for the API key from Fixer.io
const String apiKey = '9dOdXBzMDWBpAY8ZZ9IdzYfpGKJuBqAQ';