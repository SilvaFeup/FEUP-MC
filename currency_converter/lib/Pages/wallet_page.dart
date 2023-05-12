import 'package:currency_converter/models/currency.dart';
import 'package:currency_converter/models/rates.dart';
import 'package:flutter/material.dart';
import '../Services/fixer_service.dart';
import '../Widgets/currency_list.dart';
import '../Controllers/JSON_controller.dart';

class WalletPage extends StatefulWidget {
  const WalletPage({Key? key}) : super(key: key);

  @override
  _WalletPageState createState() => _WalletPageState();
}

class _WalletPageState extends State<WalletPage> {
  List<List<String>> symbolsList = [];
  Rates baseCurrency = Rates(code: 'USD', rate: 1);
  List<Currency> currencies = [];
  List<Rates> rates = [];

  @override
  Widget build(BuildContext context) {
    return Scaffold(
        appBar: AppBar(
          title: const Text('My Touristic Wallet'),
        ),
        body: Center(
          child: Column(children: [
            Row(
              mainAxisAlignment: MainAxisAlignment.center,
              children: [
                const Text('Total: '),
                const Text('0.00'),
                const SizedBox(width: 20),
                DropdownMenu(
                    initialSelection: baseCurrency.code,
                    dropdownMenuEntries: [
                      for (var item in symbolsList)
                        DropdownMenuEntry(
                          value: item[0],
                          label: item[0],
                        )
                    ],
                    enableFilter: true,
                    menuHeight: 500.0,
                    onSelected: (value) {
                      if (value == baseCurrency.code) return;
                      if (value == null) return;
                      setState(() {
                        baseCurrency = rates.firstWhere(
                            (element) => element.code == value,
                            orElse: () => throw Exception(
                                'Currency with code $value not found'));
                      });
                      baseCurrencyChanged();
                    })
              ],
            ),
            Expanded(
              flex: 2,
              child: CurrencyList(
                baseCurrency: baseCurrency,
                currencies: currencies,
              ),
            ),
          ]),
        ),
        floatingActionButton: FloatingActionButton(
          onPressed: () {
            setState(() {
              //TODO: Update the rates
            });
          },
          child: const Icon(Icons.refresh),
        ));
  }

  @override
  initState() {
    super.initState();
    readCurrencies().then((value) {
      setState(() {
        currencies = List.from(value);
      });
    });
    readRates().then((value) {
      setState(() {
        rates = List.from(value);
      });
    });
    readSymbols().then((value) {
      setState(() {
        symbolsList = List.from(value);
      });
    });
  }

  void baseCurrencyChanged() {
    for (var currency in currencies) {
      currency.rate =
          rates.firstWhere((element) => element.code == currency.code).rate /
              baseCurrency.rate;
    }
  }
}
