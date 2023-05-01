import 'package:flutter/material.dart';
import '../models/currency.dart';

class CurrencyList extends StatelessWidget {
  final List<Currency> currencies;
  const CurrencyList({Key? key, required this.currencies}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return ListView.builder(
      itemCount: currencies.length,
      itemBuilder: (context, index) {
        final currency = currencies[index];
        return ListTile(
          leading: Text(currency.symbol),
          title: Text(currency.name),
          subtitle: Text(currency.code),
          trailing: Text(currency.amount.toString()),
          onTap: () {
            // TODO: implement editing or deleting currency
          },
        );
      },
    );
  }
}