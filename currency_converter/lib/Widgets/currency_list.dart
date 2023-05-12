import 'dart:convert';
import 'dart:io';
import 'package:currency_converter/Controllers/JSON_controller.dart';
import 'package:flutter/material.dart';
import '../constants.dart';
import '../models/currency.dart';
import '../models/rates.dart';

class CurrencyList extends StatefulWidget {
  final Rates baseCurrency;
  final List<Currency> currencies;
  const CurrencyList(
      {Key? key, required this.baseCurrency, required this.currencies})
      : super(key: key);

  @override
  State<CurrencyList> createState() => _CurrencyListState();
}

class _CurrencyListState extends State<CurrencyList> {
  @override
  Widget build(BuildContext context) {
    return ListView.builder(
      itemCount: widget.currencies.length,
      itemBuilder: (context, index) {
        return Card(
          child: ListTile(
              leading: CircleAvatar(
                child: Text(widget.currencies[index].code[0]),
              ),
              title: Text(widget.currencies[index].name),
              trailing: Row(
                mainAxisSize: MainAxisSize.min,
                children: [
                  Text(
                      '${(widget.currencies[index].amount / widget.currencies[index].rate).toStringAsFixed(2)} ${widget.baseCurrency.code}'),
                  const SizedBox(width: 30),
                  Text(widget.currencies[index].rate.toStringAsFixed(2)),
                  const SizedBox(width: 30),
                  Text(
                      '${widget.currencies[index].amount}  ${widget.currencies[index].code}'),
                ],
              )),
        );
      },
    );
  }
}
