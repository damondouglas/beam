part of '../components.dart';

class BlackLinesPainter extends LinesPainter {
  BlackLinesPainter(super.start, super.end);

  @override
  void paint(Canvas canvas, Size size) {
    if (start == null || end == null) return;
    canvas.drawLine(
        start,
        end,
        Paint()
          ..strokeWidth = 4
          ..color = Colors.black);
  }
}

class Edge extends StatelessWidget {
  const Edge({
    Key? key,
    required Offset this.from,
    required Offset this.to,
    required Stack this.canvas}) : super(key: key);

  final Offset from;
  final Offset to;
  final Stack canvas;

  CustomPainter painter() {
    return BlackLinesPainter(from, to);
  }

  @override
  Widget build(BuildContext context) {
    return CustomPaint(painter: painter(), child: canvas);
  }
}
