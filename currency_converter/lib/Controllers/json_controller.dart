import 'dart:convert';
import 'dart:io';

List<List<String>> readSymbolsFromFile() {
  const directory = './Assets/symbols.json';
  final file = File(directory);
  final contents = file.readAsStringSync();
  final json = jsonDecode(contents);
  final symbolsJson = json['symbols'];

  List<List<String>> symbolsList = [];
  for (var symbol in symbolsJson.entries) {
    symbolsList.add([symbol.key, symbol.value]);
  }
  return symbolsList;
}
