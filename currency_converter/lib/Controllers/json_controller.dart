import 'dart:convert';
import 'dart:io';
import 'package:flutter/services.dart';
import 'package:path_provider/path_provider.dart';
import '../models/currency.dart';
import '../models/rates.dart';
import 'package:path/path.dart' as path;

//final Directory dataDirectory = await getApplicationSupportDirectory();

Future<void> importAssets() async {
  final Directory appSupportDir = await getApplicationSupportDirectory();
  final Directory dataDir = Directory(path.join(appSupportDir.path, 'data'));
  if (!await dataDir.exists()) {
    await dataDir.create();

    final currencies = await rootBundle.load('Assets/currencies.json');
    File file = File(path.join(dataDir.path, 'currencies.json'));
    file.writeAsBytes(currencies.buffer.asUint8List());

    final rates = await rootBundle.load('Assets/rates.json');
    file = File(path.join(dataDir.path, 'rates.json'));
    file.writeAsBytes(rates.buffer.asUint8List());

    final symbols = await rootBundle.load('Assets/symbols.json');
    file = File(path.join(dataDir.path, 'symbols.json'));
    file.writeAsBytes(symbols.buffer.asUint8List());

    final baseCurrency = await rootBundle.load('Assets/base-currency.json');
    file = File(path.join(dataDir.path, 'base-currency.json'));
    file.writeAsBytes(baseCurrency.buffer.asUint8List());
    print('Data directory created');
  } else {
    print('Data directory already exists');
    print((await getApplicationSupportDirectory()).path);
  }
}

Future<List<List<String>>> readSymbols() async {
  final Directory appSupportDir = await getApplicationSupportDirectory();
  final Directory dataDir = Directory(path.join(appSupportDir.path, 'data'));
  final File file = File(path.join(dataDir.path, 'symbols.json'));
  final contents = await file.readAsString();
  final json = jsonDecode(contents);
  final symbolsJson = json['symbols'];

  List<List<String>> symbolsList = [];
  for (var symbol in symbolsJson.entries) {
    symbolsList.add([symbol.key, symbol.value]);
  }
  return symbolsList;
}

void updateCurrency(List<Currency> currencies) async {
  final Directory appSupportDir = await getApplicationSupportDirectory();
  final Directory dataDir = Directory(path.join(appSupportDir.path, 'data'));
  final File file = File(path.join(dataDir.path, 'currencies.json'));
  List<Currency> currenciesList = List.from(currencies);

  Map<String, dynamic> json = {};
  json['currencies'] = [];
  for (var currency in currenciesList) {
    json['currencies'].add({
      'name': currency.name,
      'code': currency.code,
      'amount': currency.amount,
      'rate': currency.rate,
    });
  }
  file.writeAsString(jsonEncode(json));
}

Future<Currency> readBaseCurrency() async {
  final Directory appSupportDir = await getApplicationSupportDirectory();
  final Directory dataDir = Directory(path.join(appSupportDir.path, 'data'));
  final File file = File(path.join(dataDir.path, 'base-currency.json'));
  final contents = await file.readAsString();
  final json = jsonDecode(contents);
  final currenciesJson = json['base-currency'];

  var baseCurrency = Currency(
    name: currenciesJson['name'],
    code: currenciesJson['code'],
    amount: currenciesJson['amount'],
    rate: currenciesJson['rate'],
  );

  return baseCurrency;
}

Future<List<Currency>> readCurrencies() async {
  final Directory appSupportDir = await getApplicationSupportDirectory();
  final Directory dataDir = Directory(path.join(appSupportDir.path, 'data'));
  final File file = File(path.join(dataDir.path, 'currencies.json'));
  final contents = await file.readAsString();
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
  final Directory appSupportDir = await getApplicationSupportDirectory();
  final Directory dataDir = Directory(path.join(appSupportDir.path, 'data'));
  final File file = File(path.join(dataDir.path, 'rates.json'));
  final contents = await file.readAsString();
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

Future<List> readSymbolsAndRates() async {
  var rates = await readRates();
  var symbols = await readSymbols();
  return [symbols, rates];
}
