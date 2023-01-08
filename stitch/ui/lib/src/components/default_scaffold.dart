part of 'components.dart';

class DefaultScaffold extends StatelessWidget {
  const DefaultScaffold({Key? key, required this.db}) : super(key: key);
  final FirebaseFirestore db;

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Stitch Service'),
      ),
      body: _Body(db: db),
    );
  }
}
class _Body extends StatefulWidget {
  final FirebaseFirestore db;

  const _Body({Key? key, required this.db}) : super(key: key);

  @override
  State<_Body> createState() => _BodyState();
}

class _BodyState extends State<_Body> {

  @override
  Widget build(BuildContext context) {
    return Column(
        children: [
          TextButton(child: const Text("Submit"), onPressed: () => pullPipeline()),
          TextButton(onPressed: () => pullPipeline(), child: const Text("Pull")),
          const Spacer(),
          const NodeList()]);
  }

  void pullPipeline() {
    pullPipelineAsync();
  }

  Future<void> pullPipelineAsync() async {
    await widget.db.collection("FYUXSFZLRdkb3h3cXjiW").get().then((event) {
      for (var doc in event.docs) {
        print("${doc.id} => ${doc.data()}");
      }
    });
  }
}

