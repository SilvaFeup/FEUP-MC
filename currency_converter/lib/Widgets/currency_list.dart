import 'dart:convert';
import 'dart:io';
import 'package:currency_converter/Controllers/JSON_controller.dart';
import 'package:flutter/material.dart';
import '../constants.dart';
import '../models/currency.dart';
import '../models/rates.dart';

class CurrencyList extends StatefulWidget {
  Rates baseCurrency = Rates(code: 'USD', rate: 1);

  final void Function() onDeleteCurrency;

  CurrencyList({Key? key, required this.onDeleteCurrency}) : super(key: key);

  @override
  State<CurrencyList> createState() => _CurrencyListState();

  Future<void> addCurrency(String code) async {
    List<Currency> currencies = await readCurrencies();
    List<Rates> rates = await readRates();
    Currency baseCurrency = await readBaseCurrency();

    await readSymbols().then((value) {
      List<String> symbol = value.firstWhere((element) => element[0] == code);
      currencies.add(Currency(
          code: code,
          name: symbol[1],
          amount: 0,
          rate: rates.firstWhere((element) => element.code == code).rate / baseCurrency.rate));
    });
    updateCurrency(currencies);
  }

  Future<void> deleteCurrency(String code) async {
    List<Currency> currencies = await readCurrencies();
    // Find the index of the currency with the given code
    int index = currencies.indexWhere((element) => element.code == code);
    // If the currency exists, remove it from the list
    if (index != -1) {
      currencies.removeAt(index);
    }
    // Update the currency list
    updateCurrency(currencies);

  }
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
                    child : ListTile(
                            leading: CircleAvatar(
                              child: Text(currencies[index].code),
                            ),


                            title:  Center(child: Text(currencies[index].name)),

                            subtitle: Center ( child: Row(
                              mainAxisSize: MainAxisSize.min,
                              children: [
                                Text('${currencies[index].amount}  ${currencies[index].code}'),
                                const SizedBox(width: 30),
                                Text(currencies[index].rate.toStringAsFixed(2)),
                                const SizedBox(width: 30),
                                Text(
                                    '${(currencies[index].amount * currencies[index].rate).toStringAsFixed(2)} ${widget.baseCurrency.code}'),
                              ],
                            )),

                            trailing: IconButton(
                              icon: const Icon(Icons.delete),
                              onPressed: () {
                                // Call the deleteCurrency function with the code of the currency
                                widget.deleteCurrency(currencies[index].code);
                                // Show a snackbar to confirm the deletion
                                ScaffoldMessenger.of(context).showSnackBar(
                                  SnackBar(
                                    content: Text('Currency ${currencies[index].code} deleted'),
                                  ),
                                );
                                widget.onDeleteCurrency();
                              },
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
