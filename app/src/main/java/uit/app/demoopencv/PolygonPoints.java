package uit.app.demoopencv;

import android.graphics.PointF;

public class PolygonPoints {
    private final PointF topLeftPoint;
    private final PointF topRightPoint;
    private final PointF bottomLeftPoint;
    private final PointF bottomRightPoint;

    public PolygonPoints(PointF topLeftPoint, PointF topRightPoint, PointF bottomLeftPoint, PointF bottomRightPoint) {
        this.topLeftPoint = topLeftPoint;
        this.topRightPoint = topRightPoint;
        this.bottomLeftPoint = bottomLeftPoint;
        this.bottomRightPoint = bottomRightPoint;
    }

    public PointF getTopLeftPoint() {
        return topLeftPoint;
    }

    public PointF getTopRightPoint() {
        return topRightPoint;
    }

    public PointF getBottomLeftPoint() {
        return bottomLeftPoint;
    }

    public PointF getBottomRightPoint() {
        return bottomRightPoint;
    }
}
