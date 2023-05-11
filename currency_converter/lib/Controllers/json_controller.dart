import 'dart:convert';
import 'dart:io';
import '../models/currency.dart';

List<List<String>> readSymbolsFromFile() {
  const directory = './Assets/symbols.json';
  final file = File(directory);
  final contents = file.readAsStringSync();
  final json = jsonDecode(contents);
  final symbolsJson = json['symbols'];

  List<List<String>> symbolsList = [];
  for (var symbol in symbolsJson.entries) {
    symbolsList.add([symbol.key, symbol.value]);
  }
  return symbolsList;
}

void updateCurrencyList(List<Currency> currencies) {
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
