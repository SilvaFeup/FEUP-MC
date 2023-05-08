import 'package:currency_converter/Pages/wallet_page.dart';
import 'package:flutter/material.dart';
import 'Pages/Navigation.dart';


void main() {
  runApp(App()
    /*const MaterialApp(
    home: WalletPage(),
  )*/
  );
}


class  App extends StatelessWidget{
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: "Currency Converter",
      theme: ThemeData(
        primarySwatch: Colors.blue,
      ),
    home: NavigationPages(),
    );
  }
}

