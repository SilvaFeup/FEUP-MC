import 'package:currency_converter/Pages/adding_currencies_page.dart';
import 'package:currency_converter/Pages/graph_page.dart';
import 'package:currency_converter/Pages/wallet_page.dart';
import 'package:currency_converter/models/currency.dart';
import 'package:flutter/material.dart';
import 'package:flutter/src/widgets/framework.dart';
import 'package:flutter/src/widgets/placeholder.dart';

class NavigationPages extends StatefulWidget {
  const NavigationPages({Key? key}) : super(key: key);

  @override
  _NavigationPagesState createState() => _NavigationPagesState();
}

class _NavigationPagesState extends State<NavigationPages> {
  int _selectedIndex = 0;

  final List<Widget> _pages = [
    const WalletPage(),
    const AddingCurrenciesPage(),
    const GraphPage(),
  ];

void _onItemTapped(int index) {
  setState((){
    _selectedIndex = index;
  });
}

  @override
  Widget build(BuildContext context) => Scaffold(
    body: _pages[_selectedIndex],
    bottomNavigationBar: BottomNavigationBar(
      items: const <BottomNavigationBarItem>[
        BottomNavigationBarItem(
          icon: Icon(Icons.wallet),
          label: "My wallet",
        ),
          BottomNavigationBarItem(
          icon: Icon(Icons.add),
          label: "New currency",
        ),
         BottomNavigationBarItem(
          icon: Icon(Icons.bar_chart_rounded),
          label: "Graphic",
        ),
      ],
      currentIndex: _selectedIndex,
      selectedItemColor: Colors.blue,
      onTap: _onItemTapped,
    ),
  );
}

