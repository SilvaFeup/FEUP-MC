import 'package:flutter/material.dart';
import 'package:fl_chart/fl_chart.dart';
import 'dart:core';
import 'dart:math';
import '../models/currency.dart';
import '../models/rates.dart';

final random = Random();

class Chart extends StatelessWidget {
  final Future<List<Currency>> currencies;
  final Future<Currency> base;
  final Future<Map<String, Color>> colors;
  final Future<List<Rates>> rates;

  const Chart({
    Key? key,
    required this.currencies,
    required this.base,
    required this.colors,
    required this.rates,
  }) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return FutureBuilder<List<Currency>>(
      future: currencies,
      builder: (context, snapshot) {
        if (snapshot.connectionState == ConnectionState.waiting) {
          // While waiting for the currencies to load, you can show a loading indicator
          return CircularProgressIndicator();
        } else if (snapshot.hasError) {
          // Handle any errors that occurred while loading the currencies
          return Text('Error: ${snapshot.error}');
        } else {
          final currencyList = snapshot.data!;
          return FutureBuilder<Currency>(
            future: base,
            builder: (context, snapshot) {
              if (snapshot.connectionState == ConnectionState.waiting) {
                return CircularProgressIndicator();
              } else if (snapshot.hasError) {
                return Text('Error: ${snapshot.error}');
              } else {
                final baseCurrency = snapshot.data!;
                return FutureBuilder<Map<String, Color>>(
                  future: colors,
                  builder: (context, snapshot) {
                    if (snapshot.connectionState == ConnectionState.waiting) {
                      return CircularProgressIndicator();
                    } else if (snapshot.hasError) {
                      return Text('Error: ${snapshot.error}');
                    } else {
                      final colorsMap = snapshot.data!;
                      return FutureBuilder<List<Rates>>(
                        future: rates,
                        builder: (context, snapshot) {
                          if (snapshot.connectionState == ConnectionState.waiting) {
                            return CircularProgressIndicator();
                          } else if (snapshot.hasError) {
                            return Text('Error: ${snapshot.error}');
                          } else {
                            final ratesList = snapshot.data!;
                            return FutureBuilder<List<PieChartSectionData>>(
                              future: getSections(currencyList, baseCurrency, ratesList),
                              builder: (context, snapshot) {
                                if (snapshot.connectionState == ConnectionState.waiting) {
                                  return CircularProgressIndicator();
                                } else if (snapshot.hasError) {
                                  return Text('Error: ${snapshot.error}');
                                } else {
                                  final sections = snapshot.data!;
                                  return PieChart(
                                    PieChartData(
                                      sections: sections,
                                      sectionsSpace: 0,
                                      centerSpaceRadius: 40,
                                      borderData: FlBorderData(show: false),
                                      pieTouchData: PieTouchData(touchCallback: (pieTouchResponse) {
                                        // TODO: implement showing details on touch
                                      }),
                                    ),
                                  );
                                }
                              },
                            );
                          }
                        },
                      );
                    }
                  },
                );
              }
            },
          );
        }
      },
    );
  }


  Future<List<PieChartSectionData>> getSections(List<Currency> currencies, Currency base, List<Rates> rates) {
    return colors.then((Map<String, Color> colorsMap) {
      double total = currencies.fold(0, (sum, currency) => sum + currency.convertTo(base.code));
      return List.generate(currencies.length, (index) {
        final currency = currencies[index];
        final percentage = currency.convertTo(base.code) / total * 100;
        final color = colorsMap[currency.code]!;
        return PieChartSectionData(
          value: percentage,
          color: color,
          title: '${percentage.toStringAsFixed(1)}%',
          titleStyle: const TextStyle(color: Colors.white),
          radius: 100,
          showTitle: true,
        );
      });
    });
  }


  Widget getIndicators(Map<String, Color> colors, List<Currency> currencies) {
    return Row(
      mainAxisAlignment: MainAxisAlignment.spaceEvenly,
      children: currencies.map((currency) {
        final color = colors[currency.code]!;
        return Row(
          children: [
            Container(
              width: 16,
              height: 16,
              decoration: BoxDecoration(shape: BoxShape.circle, color: color),
            ),
            const SizedBox(width: 4),
            Text(currency.code),
          ],
        );
      }).toList(),
    );
  }

  Widget getTitles(Currency base) {
    var baseCode = base.code;
    return Column(
      mainAxisAlignment: MainAxisAlignment.center,
      children: [
        const Text('Your wallet', style: TextStyle(fontSize: 24)),
        Text('in $baseCode', style: const TextStyle(fontSize: 18)),
      ],
    );
  }
}
