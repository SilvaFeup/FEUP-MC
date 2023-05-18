import 'package:currency_converter/models/currency.dart';

class Rates {
  String code;
  double rate;

  Rates({required this.code, required this.rate});
  Rates.fromCurrency(Currency currency)
      : code = currency.code,
        rate = currency.rate;
}
