part of 'components.dart';

class Home extends StatelessWidget {
  const Home({Key? key, required this.db}) : super(key: key);

  final FirebaseFirestore db;

  @override
  Widget build(BuildContext context) {
    return DefaultScaffold(db: db);
  }
}

