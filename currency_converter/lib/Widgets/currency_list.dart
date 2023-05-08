import 'dart:convert';
import 'dart:io';

import 'package:flutter/material.dart';
import '../models/currency.dart';

class CurrencyList extends StatefulWidget {
  const CurrencyList({Key? key}) : super(key: key);

  @override
  State<CurrencyList> createState() => _CurrencyListState();
}

class _CurrencyListState extends State<CurrencyList> {
  @override
  Widget build(BuildContext context) {
    return FutureBuilder<List<Currency>>(
      future: readFromFile(),
      builder: (context, snapshot) {
        switch (snapshot.connectionState) {
          case ConnectionState.none:
            return const Text('No connection');
          case ConnectionState.waiting:
            return const CircularProgressIndicator();
          case ConnectionState.active:
          case ConnectionState.done:
            if (snapshot.hasData) {
              return ListView.builder(
                itemCount: snapshot.data!.length,
                itemBuilder: (context, index) {
                  return ListTile(
                    title: Text(snapshot.data![index].name),
                    subtitle: Text(snapshot.data![index].code),
                    trailing: Text(snapshot.data![index].amount.toString()),
                  );
                },
              );
            } else {
              return const Text('No data');
            }
        }
      },
    );
  }

  static Future<List<Currency>> readFromFile() async {
    const directory = './Assets/currencies.json';
    final file = File(directory);
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
}
