import 'package:currency_converter/models/currency.dart';
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
  List<List<String>> symbolsList = readSymbolsFromFile();
  Currency baseCurrency =
      Currency(code: 'EUR', name: 'Euro', amount: 0.0, rate: 1.0);

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
                )
              ],
            ),
            const Expanded(
              flex: 2,
              child: CurrencyList(),
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
    baseCurrency = Currency(code: 'EUR', name: 'Euro', amount: 0.0, rate: 1.0);
  }
}
