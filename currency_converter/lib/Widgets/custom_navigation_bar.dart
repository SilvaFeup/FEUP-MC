import 'package:flutter/material.dart';

import '../Pages/adding_currencies_page.dart';
import '../Pages/graph_page.dart';
import '../Pages/wallet_page.dart';

class CustomNavBar extends StatefulWidget {
  final int index;
  const CustomNavBar({Key? key, required this.index}) : super(key: key);

  @override
  _CustomNavBarState createState() => _CustomNavBarState();
}

class _CustomNavBarState extends State<CustomNavBar> {
  int selectedIndex = 0;
  late PageController _pageController;

  @override
  void initState() {
    super.initState();
    selectedIndex = widget.index;
    _pageController = PageController(initialPage: selectedIndex);
  }

  @override
  void dispose() {
    _pageController.dispose();
    super.dispose();
  }

  void _onItemTapped(int index) {
    if (selectedIndex != index) {
      setState(() {
        selectedIndex = index;
      });
      _pageController.jumpToPage(index);
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: PageView(
        controller: _pageController,
        physics: const NeverScrollableScrollPhysics(),
        children: const <Widget>[
          WalletPage(),
          AddingCurrenciesPage(),
          GraphPage(),
        ],
      ),
      bottomNavigationBar: BottomNavigationBar(
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
      ),
    );
  }
}
