import 'package:flutter/material.dart';
import 'package:fl_chart/fl_chart.dart';
import 'dart:core';
import '../models/currency.dart';
import '../constants.dart';

class Chart extends StatelessWidget {
  final List<Currency> currencies;
  final Map<String, num> rates;
  const Chart({Key? key, required this.currencies, required this.rates})
      : super(key: key);

  @override
  Widget build(BuildContext context) {
    return PieChart(
      PieChartData(
        sections: getSections(),
        sectionsSpace: 0,
        centerSpaceRadius: 40,
        borderData: FlBorderData(show: false),
        pieTouchData: PieTouchData(touchCallback: (pieTouchResponse) {
          // TODO: implement showing details on touch
        }),
      ),
    );
  }

  // A helper method that creates the sections for the pie chart
  List<PieChartSectionData> getSections() {
    // Calculate the total value of the wallet in the base currency
    double total = currencies.fold(
        0, (sum, currency) => sum + currency.convertTo(base));
    // Create a list of sections for each currency
    return List.generate(currencies.length, (index) {
    final currency = currencies[index];
    // Calculate the percentage of the currency relative to the total
    final percentage = currency.convertTo(base) / total * 100;
    // Get the color for the currency from the map
    final color = colors[currency.code]!;
    // Create a section data with the percentage, color and title
    return PieChartSectionData(
      value: percentage,
      color: color,
      title: '${percentage.toStringAsFixed(1)}%',
      titleStyle: const TextStyle(color: Colors.white),
      radius: 100,
      showTitle: true,
    );
    });
  }

  // A helper method that creates the indicators for the pie chart
  Widget getIndicators() {
    return Row(
      mainAxisAlignment: MainAxisAlignment.spaceEvenly,
      children: currencies.map((currency) {
        // Get the color for the currency from the map
        final color = colors[currency.code]!;
        // Create a row with a colored box and a text with the currency code
        return Row(
          children: [
            Container(
              width: 16,
              height: 16,
              decoration:
              BoxDecoration(shape: BoxShape.circle, color: color),
            ),
            const SizedBox(width: 4),
            Text(currency.code),
          ],
        );
      }).toList(),
    );
  }

  // A helper method that creates the titles for the pie chart
  Widget getTitles() {
    return Column(
        mainAxisAlignment: MainAxisAlignment.center,
        children: const [
          Text('Your wallet', style: TextStyle(fontSize: 24)),
          Text('in $base', style: TextStyle(fontSize: 18)),
    ],
    );
  }
}