import 'dart:convert';
import 'dart:io';

Future<List<String>> readSymbolsFromFile() async {
  const directory = './Assets/symbols.json';
  final file = File(directory);
  final contents = await file.readAsString();
  final json = jsonDecode(contents);
  final symbolsJson = json['symbols'];

  List<String> symbolsList = [];
  for (var symbol in symbolsJson.entries) {
    print(symbol.key + ' ' + symbol.value);
  }
  return symbolsList;
}
