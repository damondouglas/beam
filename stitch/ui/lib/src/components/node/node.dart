part of '../components.dart';

class NodeWidget extends StatefulWidget {
  NodeWidget({
    Key? key,
    required this.id,
    required this.header,
    required this.size,
    this.next,
  }) : super(key: key);

  Offset? globalPosition;

  final NodeHeader header;
  final String id;
  final Size size;
  final NodeWidget? next;

  @override
  State<NodeWidget> createState() => _NodeWidgetState();

  NodeWidget setNext(NodeWidget newNext) {
    NodeWidget newNode = NodeWidget(id: id, header: header, size: size, next: newNext);
    return newNode;
  }

}

class _NodeWidgetState extends State<NodeWidget> {

  @override
  Widget build(BuildContext context) {
    return Card(
      clipBehavior: Clip.hardEdge,
      elevation: 1,
      child: SizedBox.fromSize(
        size: widget.size,
        child: Column(children: [widget.header],),
          )
    );
  }
}
