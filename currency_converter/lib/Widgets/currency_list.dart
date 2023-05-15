import 'dart:convert';
import 'dart:io';
import 'package:currency_converter/Controllers/JSON_controller.dart';
import 'package:flutter/material.dart';
import '../constants.dart';
import '../models/currency.dart';
import '../models/rates.dart';

class CurrencyList extends StatefulWidget {
  Rates baseCurrency = Rates(code: 'USD', rate: 1);
  CurrencyList({Key? key}) : super(key: key);

  @override
  State<CurrencyList> createState() => _CurrencyListState();
}

class _CurrencyListState extends State<CurrencyList> {
  final amountController = TextEditingController();

  @override
  Widget build(BuildContext context) {
    return FutureBuilder(
        future: readCurrencies(),
        builder: (context, snapshot) {
          switch (snapshot.connectionState) {
            case ConnectionState.waiting:
              return const CircularProgressIndicator();
            case ConnectionState.done:
              List<Currency> currencies = List.from(snapshot.data!);
              return ListView.builder(
                itemCount: currencies.length,
                itemBuilder: (context, index) {
                  return Card(
                    child: ListTile(
                        leading: CircleAvatar(
                          child: Text(currencies[index].code[0]),
                        ),
                        title: Text(currencies[index].name),
                        trailing: Row(
                          mainAxisSize: MainAxisSize.min,
                          children: [
                            Text(
                                '${(currencies[index].amount / currencies[index].rate).toStringAsFixed(2)} ${widget.baseCurrency.code}'),
                            const SizedBox(width: 30),
                            Text(currencies[index].rate.toStringAsFixed(2)),
                            const SizedBox(width: 30),
                            Text(
                                '${currencies[index].amount}  ${currencies[index].code}'),
                          ],
                        ),
                        onTap: () {
                          amountController.text =
                              currencies[index].amount.toString();
                          showDialog(
                            context: context,
                            builder: (context) {
                              return AlertDialog(
                                title: Text(
                                    "Change amount of ${currencies[index].code}"),
                                content: TextFormField(
                                  controller: amountController,
                                  keyboardType: TextInputType.number,
                                  decoration: const InputDecoration(
                                    hintText: 'Enter amount',
                                  ),
                                ),
                                actions: [
                                  TextButton(
                                      onPressed: () {
                                        Navigator.pop(context);
                                      },
                                      child: const Text('Cancel')),
                                  TextButton(
                                      onPressed: () {
                                        setState(() {
                                          currencies[index].amount =
                                              double.parse(
                                                  amountController.text);
                                          updateCurrency(currencies);
                                        });
                                        Navigator.pop(context);
                                      },
                                      child: const Text('Save')),
                                ],
                              );
                            },
                          );
                        }),
                  );
                },
              );
            default:
              return const Text('Something went wrong');
          }
        });
  }
}
