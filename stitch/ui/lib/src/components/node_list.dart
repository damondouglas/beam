part of 'components.dart';

class NodeList extends StatefulWidget {
  const NodeList({Key? key}) : super(key: key);

  @override
  State<NodeList> createState() => _NodeListState();
}

class _NodeListState extends State<NodeList> {
  final nodeSize = const Size(300, 300);
  final nodeSpace = 50;

  @override
  Widget build(BuildContext context) {
    FirstNodeWidget firstNode = FirstNodeWidget(
        header: NodeHeader(initTitle: "First Node"),
        id: '0',
        size: nodeSize);
    NodeWidget secondNode = NodeWidget(header: NodeHeader(initTitle: "Second Node"),
        id: '1',
        size: nodeSize);
    NodeWidget lastNode = LastNodeWidget(header: NodeHeader(initTitle: "Last Node"),
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
