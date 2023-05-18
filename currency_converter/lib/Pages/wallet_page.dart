import 'package:currency_converter/Controllers/json_controller.dart';
import 'package:currency_converter/models/currency.dart';
import 'package:currency_converter/models/rates.dart';
import 'package:flutter/material.dart';
import '../Controllers/json_controller.dart';
import '../Widgets/currency_list.dart';

class WalletPage extends StatefulWidget {
  const WalletPage({Key? key}) : super(key: key);

  @override
  _WalletPageState createState() => _WalletPageState();
}

class _WalletPageState extends State<WalletPage> {
  List<List<String>> symbolsList = [];
  Rates baseCurrency = Rates(code: 'USD', rate: 1);
  List<Rates> rates = [];
  CurrencyList currencyList = CurrencyList();
  bool isRefreshButtonDisabled = false;

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
                        FutureBuilder(
                          future: readCurrencies(),
                          builder: (context, snapshot) {
                            switch (snapshot.connectionState) {
                              case ConnectionState.waiting:
                                return const CircularProgressIndicator();
                              case ConnectionState.done:
                                List<Currency> currencies =
                                    List.from(snapshot.data!);
                                double total = 0;
                                for (var item in currencies) {
                                  total += item.amount / item.rate;
                                }
                                return Text(total.toStringAsFixed(2));
                              default:
                                return const Text('Error loading data');
                            }
                          },
                        ),
                        const SizedBox(width: 20),
                        FutureBuilder(
                            future: readSymbolsRatesAndBaseCurrency(),
                            builder: (context, snapshot) {
                              switch (snapshot.connectionState) {
                                case ConnectionState.waiting:
                                  return const CircularProgressIndicator();
                                case ConnectionState.done:
                                  symbolsList = List.from(snapshot.data![0]);
                                  rates = List.from(snapshot.data![1]);
                                  baseCurrency =
                                      Rates.fromCurrency(snapshot.data![2]);
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
                                        setState(() {
                                          if (value == baseCurrency.code) {
                                            return;
                                          }
                                          if (value == null) return;
                                          Rates newBaseCurrency = rates.firstWhere(
                                              (element) =>
                                                  element.code == value,
                                              orElse: () => throw Exception(
                                                  'Currency with code $value not found'));
                                          baseCurrency = newBaseCurrency;
                                          readCurrencies().then((value) {
                                            List<Currency> currencies =
                                                List.from(value);
                                            for (var item in currencies) {
                                              Rates currency = rates.firstWhere(
                                                  (element) =>
                                                      element.code ==
                                                      item.code);
                                              item.rate = currency.rate /
                                                  baseCurrency.rate;
                                            }
                                            updateCurrency(currencies);
                                            updateBaseCurrency(baseCurrency);
                                            currencyList.baseCurrency =
                                                baseCurrency;
                                          });
                                        });
                                      });
                                default:
                                  return const Text('Error loading data');
                              }
                            }),
                      ],
                    ),
                    Expanded(flex: 2, child: currencyList),
                  ]),
                );
              default:
                return const Text('Error loading data');
            }
          },
        ),
        floatingActionButtonLocation: FloatingActionButtonLocation.centerFloat,
        floatingActionButton: Stack(
          fit: StackFit.expand,
          children: [
            Positioned(
              left: 30,
              bottom: 20,
              child: FloatingActionButton(
                onPressed: () {},
                child: const Icon(Icons.add),
              ),
            ),
            Positioned(
              right: 30,
              bottom: 20,
              child: FloatingActionButton(
                onPressed: () {
                  if (isRefreshButtonDisabled) {
                    null;
                  }
                  else {
                    isRefreshButtonDisabled = true;
                    ScaffoldMessenger.of(context).showSnackBar(
                      const SnackBar(
                        content: Text("the request is send, please wait for the response.")
                      ));
                    updateRates().then((value) {
                      setState(() {
                        isRefreshButtonDisabled = false;
                      });
                    });
                }},
                child: const Icon(Icons.refresh)
              )
            ),
          ],
        ));
  }

  @override
  initState() {
    super.initState();
    // Load the currencies and rates from the JSON files
  }
}
