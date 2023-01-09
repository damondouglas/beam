part of 'components.dart';

class NodeList extends StatefulWidget {
  const NodeList({Key? key,
    List<String>? this.transformations}) : super(key: key);
  final List<String>? transformations;

  @override
  State<NodeList> createState() => _NodeListState();
}

class _NodeListState extends State<NodeList> {
  final nodeSize = const Size(300, 300);
  final nodeSpace = 50;

  @override
  Widget build(BuildContext context) {
    print(widget.transformations?.first);
    FirstNodeWidget firstNode = FirstNodeWidget(
        header: NodeHeader(initTitle: widget.transformations?.first??"First Node"),
        id: '0',
        size: nodeSize);
    NodeWidget secondNode = NodeWidget(header: NodeHeader(initTitle:widget.transformations?.elementAt(1)?? "Second Node"),
        id: '1',
        size: nodeSize);
    NodeWidget lastNode = LastNodeWidget(header: NodeHeader(initTitle:widget.transformations?.elementAt(2)?? "Last Node"),
        id: "2",
        size: nodeSize);

    secondNode = secondNode.setNext(lastNode);
    firstNode = firstNode.setNext(secondNode);

    NodeWidget? currentNode = firstNode;
    //Not sure how to make this evenly spaced from within a stack
    double offset = 60;
    List<Positioned> nodes = <Positioned>[];
    List<Edge> edges = <Edge>[];
    while(currentNode != null) {
      nodes.add(Positioned(left: offset, child: currentNode));
      currentNode = currentNode.next;
      offset += nodeSpace + nodeSize.width;
    }
    int nodeLen = nodes.length;
    for(int i = 0; i < nodeLen - 1; i++) {
      Offset nodeEdgeFrom = Offset(nodes[i].left! + nodeSize.width + 5, 150);
      Offset nodeEdgeTo = Offset(nodes[i+1].left! + 5, 150);
      edges.add(Edge(from: nodeEdgeFrom, to: nodeEdgeTo, canvas: Stack(children: nodes)));
    }

    List<Widget> components = <Widget>[];
    components.addAll(edges);
    components.addAll(nodes);
    return Expanded(child: Stack(children: components));
  }
}
