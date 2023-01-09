part of '../components.dart';

class NodeHeader extends StatefulWidget {
  final String initTitle;
  final List<Widget> actions;

  NodeHeader(
      {Key? key,
        required this.initTitle,
        this.actions = const <Widget>[]})
      : super(key: key);

  @override
  State<NodeHeader> createState() => _NodeHeaderState();

}

class _NodeHeaderState extends State<NodeHeader> {
  late String title;

  @override
  void initState() {
    super.initState();
    title = widget.initTitle;
  }

  void _setTitle(String title) {
    this.title = title;
  }

  @override
  Widget build(BuildContext context) {
    return Container(
      color: Colors.blue,
      child: Row(children: [
        Expanded(child: Text("Transformation: ")),
        Expanded(
          child: TextField(
              decoration: InputDecoration(border: const OutlineInputBorder(), hintText: widget.initTitle),
              onSubmitted: (title) {_setTitle(title);}),
        ),
        Expanded(
          child: Row(
            mainAxisAlignment: MainAxisAlignment.end,
            children: widget.actions,
          ),
        )
      ]),
    );
  }
}
