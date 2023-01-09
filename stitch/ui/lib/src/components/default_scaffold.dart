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

  NodeList nodeList = const NodeList();

  @override
  Widget build(BuildContext context) {
    return Column(
        children: [
          TextButton(child: const Text("Submit"), onPressed: () => pullPipeline()),
          TextButton(onPressed: () => pullPipeline(), child: const Text("Pull")),
          const Spacer(),
          nodeList]);
  }

  void pullPipeline() {
    pullPipelineAsync();
  }

  Future<void> pullPipelineAsync() async {
    print('pulling...');
    List<String> steps = [];
    await widget.db.collection("pipelines").get().then((event) {
      for (var doc in event.docs) {
        steps.add(doc.data()['steps'][0]['expansion'].path);
        steps.add(doc.data()['steps'][1]['expansion'].path);
        steps.add(doc.data()['steps'][2]['expansion'].path);
      }
      nodeList = NodeList(transformations: steps);
      build(context);
    });
  }
}

