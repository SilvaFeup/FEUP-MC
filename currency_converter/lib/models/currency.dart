class Currency {
  final String name;
  final String code;
  double amount;
  double rate;

  Currency({
    required this.name,
    required this.code,
    required this.amount,
    required this.rate,
  });

  double convertTo(String destinationCode) {
    if (code == destinationCode) {
      return amount;
    } else {
      return amount * rate;
    }
  }
}
