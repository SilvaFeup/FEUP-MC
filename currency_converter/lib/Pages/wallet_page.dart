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
  // An instance of FixerService
  FixerService fixerService = FixerService();

  // A Future variable to store the rates
  Future<Map<String, num>>? rates;

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
              children: const [
                Text('Total: '),
                Text('0.00'),
                DropdownMenu(dropdownMenuEntries: [])
              ],
            ),
            const Expanded(
              flex: 2,
              child: CurrencyList(),
            ),
          ]),

          /*child: FutureBuilder<Map<String, num>>(
          future: rates,
          builder: (context, snapshot) {
            switch (snapshot.connectionState) {
              case ConnectionState.none:
                return const Text('No connection');
              case ConnectionState.waiting:
                return const CircularProgressIndicator();
              case ConnectionState.active:
              case ConnectionState.done:
                if (snapshot.hasData) {
                  return Column(
                    children: [
                      Expanded(
                        flex: 2,
                        child: CurrencyList(currencies: currencies),
                      ),
                      Expanded(
                        flex: 3,
                        child: Chart(
                            currencies: currencies, rates: snapshot.data!),
                      ),
                    ],
                  );
                } else if (snapshot.hasError) {
                  return Text('Error: ${snapshot.error}');
                }
            }

            return const Text('Something went wrong');
          },
        ), */
        ),
        floatingActionButton: FloatingActionButton(
          onPressed: () {
            setState(() {
              readSymbolsFromFile();
              //TODO: Update the rates
            });
          },
          child: const Icon(Icons.refresh),
        ));
  }
}
