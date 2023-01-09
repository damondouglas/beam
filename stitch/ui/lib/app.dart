import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:flutter/material.dart';
import 'package:stitch/src/components/components.dart';

class App extends StatelessWidget {
  const App({Key? key, required this.db}) : super(key: key);

  final FirebaseFirestore db;

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      debugShowCheckedModeBanner: false,
      home: Home(db: db),
    );
  }
}
