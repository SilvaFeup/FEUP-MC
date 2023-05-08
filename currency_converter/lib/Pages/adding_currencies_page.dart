import 'package:flutter/material.dart';
import 'package:flutter/src/widgets/framework.dart';
import 'package:flutter/src/widgets/placeholder.dart';

class AddingCurrenciesPage extends StatefulWidget {
  const AddingCurrenciesPage({super.key});

  @override
  State<AddingCurrenciesPage> createState() => _AddingCurrenciesPageState();
}

class _AddingCurrenciesPageState extends State<AddingCurrenciesPage> {
  @override
  Widget build(BuildContext context) {
    return const Scaffold(
      body: Text("new currency"),
    );
  }
}