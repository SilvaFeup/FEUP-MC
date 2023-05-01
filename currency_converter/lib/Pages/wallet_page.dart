import 'package:flutter/material.dart';
import 'package:fl_chart/fl_chart.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';

import '../models/currency.dart';

import '../Services/fixer_service.dart';

import '../constants.dart';

import '../Widgets/currency_list.dart';
import '../Widgets/chart.dart';





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
        child: FutureBuilder<Map<String, num>>(
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
                    child: Chart(currencies: currencies, rates: snapshot.data!),
                  ),
                ],
              );
            } else if (snapshot.hasError) {
                return Text('Error: ${snapshot.error}');
            }
          }

          return const Text('Something went wrong');
        },
      ),
    ),

    );
  }

  @override
  void initState() {
    super.initState();
    // Get the rates for the base currency and the symbols in the wallet
    rates = fixerService.getRates(base,
        symbols: currencies.map((currency) => currency.code).toList());
  }
}