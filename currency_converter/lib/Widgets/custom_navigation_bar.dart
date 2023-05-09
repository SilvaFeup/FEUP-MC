import 'package:flutter/material.dart';
import 'package:flutter/src/widgets/framework.dart';
import 'package:flutter/src/widgets/placeholder.dart';

import '../Pages/adding_currencies_page.dart';
import '../Pages/graph_page.dart';
import '../Pages/wallet_page.dart';

class CustomNavBar extends StatefulWidget {
  final int index;
  const CustomNavBar({super.key, required this.index});

  @override
  State<CustomNavBar> createState() => _CustomNavBarState( selectedIndex: index );
}

class _CustomNavBarState extends State<CustomNavBar> {
  int selectedIndex;
  _CustomNavBarState({required this.selectedIndex});

  

  final List<Widget> _pages = [
    const WalletPage(),
    const AddingCurrenciesPage(),
    const GraphPage(),
  ];
  
  

void _onItemTapped(int index) {
  if(selectedIndex != index) {
    selectedIndex = index;
    Navigator.of(context).push(MaterialPageRoute(
      builder: (context) =>  _pages[selectedIndex]
      )
    );
  }
}


  @override
  Widget build(BuildContext context) {
    return BottomNavigationBar(
      items: const <BottomNavigationBarItem>[
        BottomNavigationBarItem(
          icon: Icon(Icons.wallet),
          label: "My wallet",
        ),
          BottomNavigationBarItem(
          icon: Icon(Icons.add),
          label: "New currencies",
        ),
         BottomNavigationBarItem(
          icon: Icon(Icons.bar_chart_rounded),
          label: "Graphic",
        ),
      ],
      currentIndex: selectedIndex,
      selectedItemColor: Colors.white,
      backgroundColor: Colors.blue,
      onTap: _onItemTapped,
      );
  }
}