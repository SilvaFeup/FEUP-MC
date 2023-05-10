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

  List<List<String>> symbolsList = readSymbolsFromFile();
  String dropDownValue = '';

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
                DropdownButton<String>(
                  value: dropDownValue,
                  items: [
                    for (var item in symbolsList)
                      DropdownMenuItem<String>(
                        value: item[1],
                        child: Text(item[0]),
                      )
                  ],
                  onChanged: (value) {
                    setState(() {
                      dropDownValue = value!;
                      print(dropDownValue + ' selected');
                    });
                  },
                ),
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
    dropDownValue = symbolsList[0][1];
  }
}
