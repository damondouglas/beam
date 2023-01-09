part of '../components.dart';

class FirstNodeWidget extends NodeWidget {
  FirstNodeWidget({
    Key? key,
    required String id,
    required NodeHeader header,
    required Size size,
    NodeWidget? next,
  }) : super(key: key, id: id, header: header, size: size, next: next);

  @override
  State<FirstNodeWidget> createState() => _FirstNodeWidgetState();

  @override
  FirstNodeWidget setNext(NodeWidget newNext) {
    FirstNodeWidget newNode = FirstNodeWidget(id: id, header: header, size: size, next: newNext);
    return newNode;
  }

}
  class _FirstNodeWidgetState extends State<FirstNodeWidget> {
    String sourceInputPath = "";

    void _setSourceInputPath(String newPath) {
      sourceInputPath = newPath;
    }

    @override
    Widget build(BuildContext context) {
      return Card(
        clipBehavior: Clip.hardEdge,
        elevation: 1,
        child: SizedBox.fromSize(
          size: widget.size,
          child: Column(children: [
            widget.header,
            Container(
              color: Colors.amber,
              child: Row(children: [
                const Expanded(child: Text("Input Source")),
                Expanded(child: TextField(
                  decoration: const InputDecoration(border: OutlineInputBorder()),
                  onSubmitted: (path) {_setSourceInputPath(path);},),
                )
              ],),
            )
          ]),
        ),
      );
    }
}

