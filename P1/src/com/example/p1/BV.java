package com.example.p1;

import java.util.ArrayList;
import java.util.List;

import org.jbox2d.collision.AABB;
import org.jbox2d.collision.CircleDef;
import org.jbox2d.collision.Shape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.World;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.example.p1.GameTimer.ITimingListener;

public class BV extends RelativeLayout implements ITimingListener {

	private Handler handler = new Handler();
	private AABB worldAABB;
	private List<Body> theBodies;
	private World world;
	private Paint paint;
	private int index = 1000000;
	private int[] colors = new int[] { Color.WHITE, Color.MAGENTA, Color.CYAN };;

	public BV(Context context) {
		super(context);
		init();
	}

	public BV(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public BV(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	private void init() {
		theBodies = new ArrayList<Body>();
		paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		worldAABB = new AABB(new Vec2(0, 0), new Vec2(500, 800));
		Vec2 gravity = new Vec2(0, 0.0001f);
		world = new World(worldAABB, gravity, true);

		addBody(5f, new Vec2(190, 100));
		addBody(5f, new Vec2(202, 250));
		addBody(25f, new Vec2(200, 350));

		world.step(300, 10);
	}

	private void addBody(float size, Vec2 position) {
		BodyDef bd = new BodyDef();
		bd.position = position;
		bd.massData.mass = .1f;
		CircleDef circleDef = new CircleDef();
		circleDef.radius = size;
		Body theBody = world.createBody(bd);
		theBody.createShape(circleDef);
		theBodies.add(theBody);
	}

	@Override
	public void onMainLoop(long milliSecond) {
		handler.post(new Runnable() {
			public void run() {
				world.step(GameTimer.TIMER_PERIOD, 1);
				invalidate();
			}
		});
	}

	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawColor(Color.GREEN);
		drawWorld(canvas);
		int size = theBodies.size();
		for (int i = 0; i < size; i++) {
			drawBody(canvas, theBodies.get(i));
		}
		paint.setColor(Color.CYAN);
		// canvas.drawText("" + Math.random(), 50, 50, paint);
	}

	private void drawBody(Canvas canvas, Body bb) {
		Vec2 position = bb.getPosition();
		paint.setColor(nextColor());
		Shape shapeList = bb.getShapeList();
		float sweepRadius = shapeList.getSweepRadius();
		canvas.drawCircle(position.x, position.y, sweepRadius, paint);
	}

	private int nextColor() {

		index++;
		if (index >= colors.length)
			index = 0;
		return colors[index];
	}

	private void drawWorld(Canvas canvas) {
		Vec2 lowerBound = world.getWorldAABB().lowerBound;
		Vec2 uppderBound = world.getWorldAABB().upperBound;

		RectF r = new RectF(lowerBound.x, lowerBound.y, uppderBound.x,
				uppderBound.y);

		paint.setColor(Color.BLACK);
		canvas.drawRect(r, paint);
	}

}
