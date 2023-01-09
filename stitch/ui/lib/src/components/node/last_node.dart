part of '../components.dart';

class LastNodeWidget extends NodeWidget {
  LastNodeWidget({
    Key? key,
    required String id,
    required NodeHeader header,
    required Size size,
  }) : super(key: key, id: id, header: header, size: size);

  @override
  State<LastNodeWidget> createState() => _LastNodeWidgetState();

}
class _LastNodeWidgetState extends State<LastNodeWidget> {
  String sinkOutputPath = "";

  void _setSinkOutputPath(String newPath) {
    sinkOutputPath = newPath;
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
              const Expanded(child: Text("Output Source")),
              Expanded(child: TextField(
                decoration: const InputDecoration(border: OutlineInputBorder()),
                onSubmitted: (path) {_setSinkOutputPath(path);},),
              )
            ],),
          )
        ]),
      ),
    );
  }
}

