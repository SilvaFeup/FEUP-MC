import 'dart:convert';
import 'dart:io';
import 'package:flutter/services.dart';

import '../models/currency.dart';
import '../models/rates.dart';

Future<List<List<String>>> readSymbols() async {
  const directory = './Assets/symbols.json';
  final contents = await rootBundle.loadString(directory);
  final json = jsonDecode(contents);
  final symbolsJson = json['symbols'];

  List<List<String>> symbolsList = [];
  for (var symbol in symbolsJson.entries) {
    symbolsList.add([symbol.key, symbol.value]);
  }
  return symbolsList;
}

void updateCurrencyFile(List<Currency> currencies) {
  const directory = './Assets/currencies.json';
  final file = File(directory);
  final contents = file.readAsStringSync();
  final json = jsonDecode(contents);
  final currenciesJson = json['currencies'];

  for (var currency in currencies) {
    currenciesJson[currency.code] = {
      'name': currency.name,
      'amount': currency.amount,
      'rate': currency.rate
    };
  }

  file.writeAsStringSync(jsonEncode(json));
}

Future<List<Currency>> readCurrencies() async {
  const directory = 'Assets/currencies.json';
  final contents = await rootBundle.loadString(directory);
  final json = jsonDecode(contents);
  final currenciesJson = json['currencies'];

  List<Currency> currenciesList = [];
  for (var currency in currenciesJson) {
    currenciesList.add(Currency(
      name: currency['name'],
      code: currency['code'],
      amount: currency['amount'],
      rate: currency['rate'],
    ));
  }
  return currenciesList;
}

Future<List<Rates>> readRates() async {
  const directory = './Assets/rates.json';
  final contents = await rootBundle.loadString(directory);
  final json = jsonDecode(contents);
  final ratesJson = json['rates'];

  List<Rates> ratesList = [];
  for (var rate in ratesJson.entries) {
    ratesList.add(Rates(
      code: rate.key,
      rate: rate.value.toDouble(),
    ));
  }
  return ratesList;
}
