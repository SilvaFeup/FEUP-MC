import '../Widgets/chart.dart';
import '../models/currency.dart';
import '../models/rates.dart';
import 'package:flutter/material.dart';
import '../Utils/color_generator.dart';
import '../Controllers/json_controller.dart';

class GraphPage extends StatefulWidget {
  const GraphPage({Key? key}) : super(key: key);

  @override
  State<GraphPage> createState() => _GraphPageState();
}

class _GraphPageState extends State<GraphPage> {
  late Future<List<Currency>> currencies;
  late Future<List<Rates>> rates;
  late Future<Currency> base;

  @override
  void initState() {
    super.initState();
    fetchCurrencies();
    fetchRates();
    fetchBaseCurrency();
  }

  void fetchCurrencies() async {
    currencies = readCurrencies();
  }

  void fetchRates() async {
    rates = readRates();
  }

  void fetchBaseCurrency() async {
    base = readBaseCurrency();
  }

  @override
  Widget build(BuildContext context) {
    return FutureBuilder<List<Currency>>(
      future: currencies,
      builder: (context, snapshot) {
        if (snapshot.connectionState == ConnectionState.waiting) {
          // While waiting for the currencies to load, you can show a loading indicator
          return const CircularProgressIndicator();
        } else if (snapshot.hasError) {
          // Handle any errors that occurred while loading the currencies
          return Text('Error: ${snapshot.error}');
        } else {
          final colors = ColorGenerator().getColorsToCurrencies(snapshot.data!);
          return Scaffold(
            body: Center(
              child: Chart(
                currencies: currencies,
                base: base,
                colors: colors,
                rates: rates,
              ),
            ),
          );
        }
      },
    );
  }
}
