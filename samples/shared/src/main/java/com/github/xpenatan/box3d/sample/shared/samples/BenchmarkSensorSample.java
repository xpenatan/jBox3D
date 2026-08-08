package com.github.xpenatan.box3d.sample.shared.samples;

import com.github.xpenatan.box3d.B3;
import com.github.xpenatan.box3d.B3Body;
import com.github.xpenatan.box3d.B3BodyDef;
import com.github.xpenatan.box3d.B3CustomFilterEm;
import com.github.xpenatan.box3d.B3Hull;
import com.github.xpenatan.box3d.B3SensorBeginTouchEvent;
import com.github.xpenatan.box3d.B3SensorEndTouchEvent;
import com.github.xpenatan.box3d.B3SensorEvents;
import com.github.xpenatan.box3d.B3Shape;
import com.github.xpenatan.box3d.B3ShapeDef;
import com.github.xpenatan.box3d.B3Sphere;
import com.github.xpenatan.box3d.B3SurfaceMaterial;
import com.github.xpenatan.box3d.B3Vec3;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/** Exact port of BenchmarkSensor at the pinned Box3D commit. */
final class BenchmarkSensorSample extends AbstractBox3DSample {
    private static final int COLUMN_COUNT = 40;
    private static final int ROW_COUNT = 40;
    private static final int FILTER_ROW = ROW_COUNT >> 1;
    private static final int METALLIC_DARK_GRAY = (5 << 24) | 0x505050;
    private static final int FUCHSIA = 0xFF00FF;
    private static final int LIME = 0x00FF00;

    private final Map<Long, SensorData> sensors = new HashMap<Long, SensorData>();
    private final SampleRandom random = new SampleRandom(42);
    private final SensorFilter filter = new SensorFilter();
    private int stepCount;
    private int maxBeginCount;
    private int maxEndCount;

    BenchmarkSensorSample() {
        world().SetCustomFilterCallback(filter);
        createActiveSensors();
        createPassiveSensors();
    }

    @Override
    public void step(float deltaSeconds) {
        super.step(deltaSeconds);
        stepCount += 1;

        Set<Long> zombies = new HashSet<Long>();
        B3SensorEvents events = world().GetSensorEvents();
        int beginCount = events.GetBeginCount();
        for(int i = 0; i < beginCount; ++i) {
            B3SensorBeginTouchEvent event = events.GetBeginEvent(i);
            SensorData sensor = sensors.get(event.GetSensorShapeId());
            if(sensor != null && sensor.active) {
                B3Shape visitorShape = new B3Shape(event.GetVisitorShapeId());
                zombies.add(visitorShape.GetBodyId());
                dispose(visitorShape);
            }
            else if(sensor != null) {
                if(sensor.row == FILTER_ROW) {
                    throw new IllegalStateException("The custom-filtered sensor row produced a begin event");
                }
                setShapeColor(event.GetVisitorShapeId(), LIME);
            }
            dispose(event);
        }

        int endCount = events.GetEndCount();
        for(int i = 0; i < endCount; ++i) {
            B3SensorEndTouchEvent event = events.GetEndEvent(i);
            B3Shape visitorShape = new B3Shape(event.GetVisitorShapeId());
            if(visitorShape.IsValid()) {
                B3SurfaceMaterial material = visitorShape.GetSurfaceMaterial();
                material.SetCustomColor(0);
                visitorShape.SetSurfaceMaterial(material);
                dispose(material);
            }
            dispose(visitorShape, event);
        }
        dispose(events);

        for(Long bodyId : zombies) {
            B3Body body = new B3Body(bodyId.longValue());
            body.Destroy();
            dispose(body);
        }

        if((stepCount & 0x1F) == 0) {
            createRow(10.0f + ROW_COUNT * 5.0f);
        }

        maxBeginCount = Math.max(maxBeginCount, beginCount);
        maxEndCount = Math.max(maxEndCount, endCount);
    }

    @Override
    public void dispose() {
        super.dispose();
        dispose(filter);
    }

    private void createActiveSensors() {
        float gridSize = 3.0f;
        B3Hull box = B3Hull.CreateCube(0.48f * gridSize);
        B3BodyDef bodyDef = new B3BodyDef();
        B3ShapeDef shapeDef = sensorShapeDef(METALLIC_DARK_GRAY);
        SensorData activeSensor = new SensorData(0, true);
        B3Vec3 position = new B3Vec3();

        float x = -40.0f * gridSize;
        for(int i = 0; i < 81; ++i) {
            position.Set(x, 0.0f, 0.0f);
            bodyDef.SetPosition(position);
            B3Body body = world().CreateBody(bodyDef);
            B3Shape shape = body.CreateHullShape(shapeDef, box);
            sensors.put(shape.GetId(), activeSensor);
            dispose(shape, body);
            x += gridSize;
        }
        dispose(position, shapeDef, bodyDef, box);
    }

    private void createPassiveSensors() {
        float shift = 5.0f;
        float xCenter = 0.5f * shift * COLUMN_COUNT;
        B3BodyDef bodyDef = new B3BodyDef();
        B3Hull box = B3Hull.CreateCube(0.5f);
        B3Vec3 position = new B3Vec3();

        for(int row = 0; row < ROW_COUNT; ++row) {
            SensorData sensorData = new SensorData(row, false);
            B3ShapeDef shapeDef = sensorShapeDef(row == FILTER_ROW ? FUCHSIA : 0);
            shapeDef.SetEnableCustomFiltering(row == FILTER_ROW);
            float y = row * shift + 10.0f;
            for(int column = 0; column < COLUMN_COUNT; ++column) {
                position.Set(column * shift - xCenter, y, 0.0f);
                bodyDef.SetPosition(position);
                B3Body body = world().CreateBody(bodyDef);
                B3Shape shape = body.CreateHullShape(shapeDef, box);
                sensors.put(shape.GetId(), sensorData);
                dispose(shape, body);
            }
            dispose(shapeDef);
        }
        dispose(position, box, bodyDef);
    }

    private void createRow(float y) {
        float shift = 5.0f;
        float xCenter = 0.5f * shift * COLUMN_COUNT;
        B3BodyDef bodyDef = new B3BodyDef();
        bodyDef.SetType(B3.DynamicBody());
        bodyDef.SetGravityScale(0.0f);
        B3Vec3 velocity = new B3Vec3(0.0f, -5.0f, 0.0f);
        bodyDef.SetLinearVelocity(velocity);

        B3ShapeDef shapeDef = new B3ShapeDef();
        shapeDef.SetEnableSensorEvents(true);
        B3Vec3 center = new B3Vec3();
        B3Sphere sphere = new B3Sphere(center, 0.5f);
        B3Vec3 position = new B3Vec3();
        for(int i = 0; i < COLUMN_COUNT; ++i) {
            float yOffset = random.nextFloat(-1.0f, 1.0f);
            position.Set(shift * i - xCenter, y + yOffset, 0.0f);
            bodyDef.SetPosition(position);
            B3Body body = world().CreateBody(bodyDef);
            dispose(body.CreateSphereShape(shapeDef, sphere), body);
        }
        dispose(position, sphere, center, shapeDef, velocity, bodyDef);
    }

    private static B3ShapeDef sensorShapeDef(int color) {
        B3ShapeDef shapeDef = new B3ShapeDef();
        shapeDef.SetIsSensor(true);
        shapeDef.SetEnableSensorEvents(true);
        B3SurfaceMaterial material = shapeDef.GetBaseMaterial();
        material.SetCustomColor(color);
        shapeDef.SetBaseMaterial(material);
        dispose(material);
        return shapeDef;
    }

    private static void setShapeColor(long shapeId, int color) {
        B3Shape shape = new B3Shape(shapeId);
        B3SurfaceMaterial material = shape.GetSurfaceMaterial();
        material.SetCustomColor(color);
        shape.SetSurfaceMaterial(material);
        dispose(material, shape);
    }

    private final class SensorFilter extends B3CustomFilterEm {
        @Override
        protected boolean Filter(long shapeIdA, long shapeIdB) {
            SensorData sensor = sensors.get(shapeIdA);
            if(sensor == null) {
                sensor = sensors.get(shapeIdB);
            }
            return sensor == null || sensor.active || sensor.row != FILTER_ROW;
        }
    }

    private static final class SensorData {
        final int row;
        final boolean active;

        SensorData(int row, boolean active) {
            this.row = row;
            this.active = active;
        }
    }
}
