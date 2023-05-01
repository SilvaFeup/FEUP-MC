import 'dart:convert';
import 'dart:core';
import '../constants.dart';
import 'package:http/http.dart' as http;

class FixerService {
  final String baseUrl = 'https://api.apilayer.com/fixer/latest';
  final String apiKey = '9dOdXBzMDWBpAY8ZZ9IdzYfpGKJuBqAQ';


  Future<Map<String, num>> getRates(String base, {List<String>? symbols}) async {
    try {
      // Initialize the URL with the base and access key
      String url = '$baseUrl?&base=$base';
      // If symbols are provided, add them to the URL
      if (symbols != null && symbols.isNotEmpty) {
        url += '&symbols=${symbols.join(',')}';
      }
      // Make the API request
      final response = await http.get(Uri.parse(url), headers: {'apikey': apiKey});
      if (response.statusCode == 200) {
        final data = jsonDecode(response.body) as Map<String, dynamic>;
        final rates = data['rates'] as Map<String, dynamic>;
        return rates.map((key, value) => MapEntry(key, value as num));
      }else {
      throw Exception('Failed to get rates');
      }
    } catch (e) {
      throw Exception(e.toString());
    }
  }
}