import 'package:flutter/material.dart';

class AddingCurrenciesPage extends StatefulWidget {
  const AddingCurrenciesPage({super.key});

  @override
  State<AddingCurrenciesPage> createState() => _AddingCurrenciesPageState();
}

class _AddingCurrenciesPageState extends State<AddingCurrenciesPage> {
  @override
  Widget build(BuildContext context) {
    return const Scaffold(
      body: Center(child:Text("new currency"),),

    );
  }
}