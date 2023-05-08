import 'dart:convert';
import 'dart:io';

import 'package:flutter/material.dart';
import '../constants.dart';
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
                  return Card(
                    child: ListTile(
                        leading: CircleAvatar(
                          child: Text(snapshot.data![index].code),
                        ),
                        title: Text(snapshot.data![index].name),
                        trailing: Row(
                          mainAxisSize: MainAxisSize.min,
                          children: [
                            Text(
                                '${snapshot.data![index].amount}  ${snapshot.data![index].code}'),
                            const SizedBox(width: 30),
                            Text(
                                '${(snapshot.data![index].rate * snapshot.data![index].amount).toStringAsFixed(2)} $base'),
                          ],
                        )),
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
