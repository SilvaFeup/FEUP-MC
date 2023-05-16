import 'package:currency_converter/models/currency.dart';
import 'package:currency_converter/models/rates.dart';
import 'package:currency_converter/Widgets/custom_navigation_bar.dart';
import 'package:flutter/material.dart';
import '../Controllers/json_controller.dart';
import '../Services/fixer_service.dart';
import '../Widgets/currency_list.dart';

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
      body: FutureBuilder(
        future: importAssets(),
        builder: (context, assets) {
          switch (assets.connectionState) {
            case ConnectionState.waiting:
              return const CircularProgressIndicator();
            case ConnectionState.done:
              return Center(
                child: Column(children: [
                  Row(
                    mainAxisAlignment: MainAxisAlignment.center,
                    children: [
                      const Text('Total: '),
                      const Text('0.00'),
                      const SizedBox(width: 20),
                      FutureBuilder(
                          future: readSymbolsAndRates(),
                          builder: (context, snapshot) {
                            switch (snapshot.connectionState) {
                              case ConnectionState.waiting:
                                return const CircularProgressIndicator();
                              case ConnectionState.done:
                                symbolsList = List.from(snapshot.data![0]);
                                rates = List.from(snapshot.data![1]);
                                return DropdownMenu(
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
                                        Rates newBaseCurrency = rates.firstWhere(
                                            (element) => element.code == value,
                                            orElse: () => throw Exception(
                                                'Currency with code $value not found'));
                                        baseCurrencyChanged(newBaseCurrency);
                                      });
                                    });
                              default:
                                return const Text('Error loading data');
                            }
                          }),
                    ],
                  ),
                  Expanded(
                      flex: 2,
                      child: FutureBuilder(
                        future: readCurrencies(),
                        builder: (context, currenciesSnapshot) {
                          switch (currenciesSnapshot.connectionState) {
                            case ConnectionState.waiting:
                              return const CircularProgressIndicator();
                            case ConnectionState.done:
                              currencies = List.from(currenciesSnapshot.data!);
                              return CurrencyList(
                                baseCurrency: baseCurrency,
                                currencies: currencies,
                              );
                            default:
                              return const Text('Error loading data');
                          }
                        },
                      )),
                ]),
              );
            default:
              return const Text('Error loading data');
          }
        },
      ),
      floatingActionButton: FloatingActionButton(
        onPressed: () {
          setState(() {
            updateRates();
          });
        },
        child: const Icon(Icons.refresh),
      ),
      bottomNavigationBar: const CustomNavBar(index: 0),
    );
  }

  @override
  initState() {
    super.initState();
    // Load the currencies and rates from the JSON files
  }

  void baseCurrencyChanged(Rates newBaseCurrency) {
    baseCurrency = newBaseCurrency;
    for (var currency in currencies) {
      currency.rate =
          rates.firstWhere((element) => element.code == currency.code).rate /
              baseCurrency.rate;
    }
  }
}
