#pragma once

#include <box3d/box3d.h>
#include <box3d/collision.h>

#include <cstdint>
#include <vector>

namespace JBox3D {

class B3Vec3 {
public:
    b3Vec3 value;

    B3Vec3();
    B3Vec3(float x, float y, float z);
    explicit B3Vec3(b3Vec3 value);

    float GetX() const;
    float GetY() const;
    float GetZ() const;
    void SetX(float x);
    void SetY(float y);
    void SetZ(float z);
    void Set(float x, float y, float z);
};

class B3Quat {
public:
    b3Quat value;

    B3Quat();
    B3Quat(float x, float y, float z, float w);
    explicit B3Quat(b3Quat value);

    B3Vec3 GetV() const;
    void SetV(const B3Vec3& value);
    float GetS() const;
    void SetS(float s);
    void Set(float x, float y, float z, float w);
};

class B3Transform {
public:
    b3Transform value;

    B3Transform();
    B3Transform(const B3Vec3& position, const B3Quat& rotation);
    explicit B3Transform(b3Transform value);

    B3Vec3 GetP() const;
    void SetP(const B3Vec3& position);
    B3Quat GetQ() const;
    void SetQ(const B3Quat& rotation);
};

class B3AABB {
public:
    b3AABB value;

    B3AABB();
    B3AABB(const B3Vec3& lowerBound, const B3Vec3& upperBound);
    explicit B3AABB(b3AABB value);

    B3Vec3 GetLowerBound() const;
    void SetLowerBound(const B3Vec3& lowerBound);
    B3Vec3 GetUpperBound() const;
    void SetUpperBound(const B3Vec3& upperBound);
};

class B3Sphere {
public:
    b3Sphere value;

    B3Sphere();
    B3Sphere(const B3Vec3& center, float radius);
    explicit B3Sphere(b3Sphere value);

    B3Vec3 GetCenter() const;
    void SetCenter(const B3Vec3& center);
    float GetRadius() const;
    void SetRadius(float radius);
};

class B3Capsule {
public:
    b3Capsule value;

    B3Capsule();
    B3Capsule(const B3Vec3& center1, const B3Vec3& center2, float radius);
    explicit B3Capsule(b3Capsule value);

    B3Vec3 GetCenter1() const;
    void SetCenter1(const B3Vec3& center);
    B3Vec3 GetCenter2() const;
    void SetCenter2(const B3Vec3& center);
    float GetRadius() const;
    void SetRadius(float radius);
};

class B3MotionLocks {
public:
    b3MotionLocks value;

    B3MotionLocks();
    explicit B3MotionLocks(b3MotionLocks value);

    bool GetLinearX() const;
    void SetLinearX(bool locked);
    bool GetLinearY() const;
    void SetLinearY(bool locked);
    bool GetLinearZ() const;
    void SetLinearZ(bool locked);
    bool GetAngularX() const;
    void SetAngularX(bool locked);
    bool GetAngularY() const;
    void SetAngularY(bool locked);
    bool GetAngularZ() const;
    void SetAngularZ(bool locked);
};

class B3DistanceJointDef {
public:
    b3DistanceJointDef value;

    B3DistanceJointDef();

    long long GetBodyIdA() const;
    void SetBodyIdA(long long bodyId);
    long long GetBodyIdB() const;
    void SetBodyIdB(long long bodyId);
    B3Transform GetLocalFrameA() const;
    void SetLocalFrameA(const B3Transform& transform);
    B3Transform GetLocalFrameB() const;
    void SetLocalFrameB(const B3Transform& transform);
    void SetLocalPositionA(const B3Vec3& position);
    void SetLocalPositionB(const B3Vec3& position);
    float GetDrawScale() const;
    void SetDrawScale(float scale);
    bool GetCollideConnected() const;
    void SetCollideConnected(bool collide);
    float GetConstraintHertz() const;
    void SetConstraintHertz(float hertz);
    float GetConstraintDampingRatio() const;
    void SetConstraintDampingRatio(float ratio);
    float GetLength() const;
    void SetLength(float length);
    bool GetEnableSpring() const;
    void SetEnableSpring(bool enabled);
    float GetLowerSpringForce() const;
    void SetLowerSpringForce(float force);
    float GetUpperSpringForce() const;
    void SetUpperSpringForce(float force);
    float GetHertz() const;
    void SetHertz(float hertz);
    float GetDampingRatio() const;
    void SetDampingRatio(float ratio);
    bool GetEnableLimit() const;
    void SetEnableLimit(bool enabled);
    float GetMinLength() const;
    void SetMinLength(float length);
    float GetMaxLength() const;
    void SetMaxLength(float length);
    bool GetEnableMotor() const;
    void SetEnableMotor(bool enabled);
    float GetMaxMotorForce() const;
    void SetMaxMotorForce(float force);
    float GetMotorSpeed() const;
    void SetMotorSpeed(float speed);
};

class B3MotorJointDef {
public:
    b3MotorJointDef value;

    B3MotorJointDef();

    long long GetBodyIdA() const;
    void SetBodyIdA(long long bodyId);
    long long GetBodyIdB() const;
    void SetBodyIdB(long long bodyId);
    B3Transform GetLocalFrameA() const;
    void SetLocalFrameA(const B3Transform& transform);
    B3Transform GetLocalFrameB() const;
    void SetLocalFrameB(const B3Transform& transform);
    void SetLocalPositionA(const B3Vec3& position);
    void SetLocalPositionB(const B3Vec3& position);
    float GetDrawScale() const;
    void SetDrawScale(float scale);
    bool GetCollideConnected() const;
    void SetCollideConnected(bool collide);
    B3Vec3 GetLinearVelocity() const;
    void SetLinearVelocity(const B3Vec3& velocity);
    float GetMaxVelocityForce() const;
    void SetMaxVelocityForce(float force);
    B3Vec3 GetAngularVelocity() const;
    void SetAngularVelocity(const B3Vec3& velocity);
    float GetMaxVelocityTorque() const;
    void SetMaxVelocityTorque(float torque);
    float GetLinearHertz() const;
    void SetLinearHertz(float hertz);
    float GetLinearDampingRatio() const;
    void SetLinearDampingRatio(float ratio);
    float GetMaxSpringForce() const;
    void SetMaxSpringForce(float force);
    float GetAngularHertz() const;
    void SetAngularHertz(float hertz);
    float GetAngularDampingRatio() const;
    void SetAngularDampingRatio(float ratio);
    float GetMaxSpringTorque() const;
    void SetMaxSpringTorque(float torque);
};

class B3ParallelJointDef {
public:
    b3ParallelJointDef value;

    B3ParallelJointDef();

    long long GetBodyIdA() const;
    void SetBodyIdA(long long bodyId);
    long long GetBodyIdB() const;
    void SetBodyIdB(long long bodyId);
    B3Transform GetLocalFrameA() const;
    void SetLocalFrameA(const B3Transform& transform);
    B3Transform GetLocalFrameB() const;
    void SetLocalFrameB(const B3Transform& transform);
    void SetLocalPositionA(const B3Vec3& position);
    void SetLocalPositionB(const B3Vec3& position);
    float GetDrawScale() const;
    void SetDrawScale(float scale);
    bool GetCollideConnected() const;
    void SetCollideConnected(bool collide);
    float GetHertz() const;
    void SetHertz(float hertz);
    float GetDampingRatio() const;
    void SetDampingRatio(float ratio);
    float GetMaxTorque() const;
    void SetMaxTorque(float torque);
};

class B3PrismaticJointDef {
public:
    b3PrismaticJointDef value;

    B3PrismaticJointDef();

    long long GetBodyIdA() const;
    void SetBodyIdA(long long bodyId);
    long long GetBodyIdB() const;
    void SetBodyIdB(long long bodyId);
    B3Transform GetLocalFrameA() const;
    void SetLocalFrameA(const B3Transform& transform);
    B3Transform GetLocalFrameB() const;
    void SetLocalFrameB(const B3Transform& transform);
    void SetLocalPositionA(const B3Vec3& position);
    void SetLocalPositionB(const B3Vec3& position);
    float GetDrawScale() const;
    void SetDrawScale(float scale);
    bool GetCollideConnected() const;
    void SetCollideConnected(bool collide);
    bool GetEnableSpring() const;
    void SetEnableSpring(bool enabled);
    float GetHertz() const;
    void SetHertz(float hertz);
    float GetDampingRatio() const;
    void SetDampingRatio(float ratio);
    float GetTargetTranslation() const;
    void SetTargetTranslation(float translation);
    bool GetEnableLimit() const;
    void SetEnableLimit(bool enabled);
    float GetLowerTranslation() const;
    void SetLowerTranslation(float translation);
    float GetUpperTranslation() const;
    void SetUpperTranslation(float translation);
    bool GetEnableMotor() const;
    void SetEnableMotor(bool enabled);
    float GetMaxMotorForce() const;
    void SetMaxMotorForce(float force);
    float GetMotorSpeed() const;
    void SetMotorSpeed(float speed);
};

class B3SphericalJointDef {
public:
    b3SphericalJointDef value;

    B3SphericalJointDef();

    long long GetBodyIdA() const;
    void SetBodyIdA(long long bodyId);
    long long GetBodyIdB() const;
    void SetBodyIdB(long long bodyId);
    B3Transform GetLocalFrameA() const;
    void SetLocalFrameA(const B3Transform& transform);
    B3Transform GetLocalFrameB() const;
    void SetLocalFrameB(const B3Transform& transform);
    void SetLocalPositionA(const B3Vec3& position);
    void SetLocalPositionB(const B3Vec3& position);
    float GetDrawScale() const;
    void SetDrawScale(float scale);
    bool GetCollideConnected() const;
    void SetCollideConnected(bool collide);
    float GetConstraintHertz() const;
    void SetConstraintHertz(float hertz);
    float GetConstraintDampingRatio() const;
    void SetConstraintDampingRatio(float ratio);
    bool GetEnableSpring() const;
    void SetEnableSpring(bool enabled);
    float GetHertz() const;
    void SetHertz(float hertz);
    float GetDampingRatio() const;
    void SetDampingRatio(float ratio);
    bool GetEnableMotor() const;
    void SetEnableMotor(bool enabled);
    float GetMaxMotorTorque() const;
    void SetMaxMotorTorque(float torque);
    B3Vec3 GetMotorVelocity() const;
    void SetMotorVelocity(const B3Vec3& velocity);
};

class B3RevoluteJointDef {
public:
    b3RevoluteJointDef value;

    B3RevoluteJointDef();

    long long GetBodyIdA() const;
    void SetBodyIdA(long long bodyId);
    long long GetBodyIdB() const;
    void SetBodyIdB(long long bodyId);
    B3Transform GetLocalFrameA() const;
    void SetLocalFrameA(const B3Transform& transform);
    B3Transform GetLocalFrameB() const;
    void SetLocalFrameB(const B3Transform& transform);
    void SetLocalPositionA(const B3Vec3& position);
    void SetLocalPositionB(const B3Vec3& position);
    float GetDrawScale() const;
    void SetDrawScale(float scale);
    bool GetCollideConnected() const;
    void SetCollideConnected(bool collide);
    float GetConstraintHertz() const;
    void SetConstraintHertz(float hertz);
    float GetConstraintDampingRatio() const;
    void SetConstraintDampingRatio(float ratio);
    float GetTargetAngle() const;
    void SetTargetAngle(float radians);
    bool GetEnableSpring() const;
    void SetEnableSpring(bool enabled);
    float GetHertz() const;
    void SetHertz(float hertz);
    float GetDampingRatio() const;
    void SetDampingRatio(float ratio);
    bool GetEnableLimit() const;
    void SetEnableLimit(bool enabled);
    float GetLowerAngle() const;
    void SetLowerAngle(float radians);
    float GetUpperAngle() const;
    void SetUpperAngle(float radians);
    bool GetEnableMotor() const;
    void SetEnableMotor(bool enabled);
    float GetMaxMotorTorque() const;
    void SetMaxMotorTorque(float torque);
    float GetMotorSpeed() const;
    void SetMotorSpeed(float speed);
};

class B3WeldJointDef {
public:
    b3WeldJointDef value;

    B3WeldJointDef();

    long long GetBodyIdA() const;
    void SetBodyIdA(long long bodyId);
    long long GetBodyIdB() const;
    void SetBodyIdB(long long bodyId);
    B3Transform GetLocalFrameA() const;
    void SetLocalFrameA(const B3Transform& transform);
    B3Transform GetLocalFrameB() const;
    void SetLocalFrameB(const B3Transform& transform);
    void SetLocalPositionA(const B3Vec3& position);
    void SetLocalPositionB(const B3Vec3& position);
    float GetDrawScale() const;
    void SetDrawScale(float scale);
    bool GetCollideConnected() const;
    void SetCollideConnected(bool collide);
    float GetConstraintHertz() const;
    void SetConstraintHertz(float hertz);
    float GetConstraintDampingRatio() const;
    void SetConstraintDampingRatio(float ratio);
    float GetLinearHertz() const;
    void SetLinearHertz(float hertz);
    float GetAngularHertz() const;
    void SetAngularHertz(float hertz);
    float GetLinearDampingRatio() const;
    void SetLinearDampingRatio(float ratio);
    float GetAngularDampingRatio() const;
    void SetAngularDampingRatio(float ratio);
};

class B3FilterJointDef {
public:
    b3FilterJointDef value;

    B3FilterJointDef();

    long long GetBodyIdA() const;
    void SetBodyIdA(long long bodyId);
    long long GetBodyIdB() const;
    void SetBodyIdB(long long bodyId);
    B3Transform GetLocalFrameA() const;
    void SetLocalFrameA(const B3Transform& transform);
    B3Transform GetLocalFrameB() const;
    void SetLocalFrameB(const B3Transform& transform);
    float GetDrawScale() const;
    void SetDrawScale(float scale);
    bool GetCollideConnected() const;
    void SetCollideConnected(bool collide);
};

class B3WheelJointDef {
public:
    b3WheelJointDef value;

    B3WheelJointDef();

    long long GetBodyIdA() const;
    void SetBodyIdA(long long bodyId);
    long long GetBodyIdB() const;
    void SetBodyIdB(long long bodyId);
    B3Transform GetLocalFrameA() const;
    void SetLocalFrameA(const B3Transform& transform);
    B3Transform GetLocalFrameB() const;
    void SetLocalFrameB(const B3Transform& transform);
    void SetLocalPositionA(const B3Vec3& position);
    void SetLocalPositionB(const B3Vec3& position);
    float GetDrawScale() const;
    void SetDrawScale(float scale);
    bool GetCollideConnected() const;
    void SetCollideConnected(bool collide);
    bool GetEnableSuspensionSpring() const;
    void SetEnableSuspensionSpring(bool enabled);
    float GetSuspensionHertz() const;
    void SetSuspensionHertz(float hertz);
    float GetSuspensionDampingRatio() const;
    void SetSuspensionDampingRatio(float ratio);
    bool GetEnableSuspensionLimit() const;
    void SetEnableSuspensionLimit(bool enabled);
    float GetLowerSuspensionLimit() const;
    void SetLowerSuspensionLimit(float limit);
    float GetUpperSuspensionLimit() const;
    void SetUpperSuspensionLimit(float limit);
    bool GetEnableSpinMotor() const;
    void SetEnableSpinMotor(bool enabled);
    float GetMaxSpinTorque() const;
    void SetMaxSpinTorque(float torque);
    float GetSpinSpeed() const;
    void SetSpinSpeed(float speed);
    bool GetEnableSteering() const;
    void SetEnableSteering(bool enabled);
    float GetSteeringHertz() const;
    void SetSteeringHertz(float hertz);
    float GetSteeringDampingRatio() const;
    void SetSteeringDampingRatio(float ratio);
    float GetTargetSteeringAngle() const;
    void SetTargetSteeringAngle(float radians);
    float GetMaxSteeringTorque() const;
    void SetMaxSteeringTorque(float torque);
    bool GetEnableSteeringLimit() const;
    void SetEnableSteeringLimit(bool enabled);
    float GetLowerSteeringLimit() const;
    void SetLowerSteeringLimit(float radians);
    float GetUpperSteeringLimit() const;
    void SetUpperSteeringLimit(float radians);
};

class B3DebugShape {
public:
    B3DebugShape();
    explicit B3DebugShape(const b3DebugShape& shape);

    long long GetShapeId() const;
    int GetType() const;
    B3Sphere GetSphere() const;
    B3Capsule GetCapsule() const;
    int GetHullEdgeCount() const;
    B3Vec3 GetHullEdgeVertex0(int index) const;
    B3Vec3 GetHullEdgeVertex1(int index) const;
    int GetSphereCount() const;
    B3Sphere GetSphereAt(int index) const;
    int GetCapsuleCount() const;
    B3Capsule GetCapsuleAt(int index) const;
    int GetTriangleCount() const;
    B3Vec3 GetTriangleVertex0(int index) const;
    B3Vec3 GetTriangleVertex1(int index) const;
    B3Vec3 GetTriangleVertex2(int index) const;
    B3Vec3 GetTriangleNormal(int index) const;

private:
    void AddSphere(const b3Sphere& sphere, b3Transform transform);
    void AddCapsule(const b3Capsule& capsule, b3Transform transform);
    void AddEdge(b3Vec3 v0, b3Vec3 v1);
    void AddTriangle(b3Vec3 v0, b3Vec3 v1, b3Vec3 v2);
    void AddHull(const b3HullData* hull, b3Transform transform);
    void AddMesh(const b3Mesh* mesh, b3Transform transform);
    void AddHeightField(const b3HeightFieldData* heightField, b3Transform transform);
    void AddCompound(const b3CompoundData* compound);
    static bool AddHeightFieldTriangle(b3Vec3 v0, b3Vec3 v1, b3Vec3 v2, int triangleIndex, void* context);

    long long m_shapeId;
    int m_type;
    B3Sphere m_sphere;
    B3Capsule m_capsule;
    std::vector<B3Sphere> m_spheres;
    std::vector<B3Capsule> m_capsules;
    std::vector<B3Vec3> m_hullEdgeVertices0;
    std::vector<B3Vec3> m_hullEdgeVertices1;
    std::vector<B3Vec3> m_triangleVertices0;
    std::vector<B3Vec3> m_triangleVertices1;
    std::vector<B3Vec3> m_triangleVertices2;
    std::vector<B3Vec3> m_triangleNormals;
};

class B3Filter {
public:
    b3Filter value;

    B3Filter();
    explicit B3Filter(b3Filter value);

    long long GetCategoryBits() const;
    void SetCategoryBits(long long categoryBits);
    long long GetMaskBits() const;
    void SetMaskBits(long long maskBits);
    int GetGroupIndex() const;
    void SetGroupIndex(int groupIndex);
};

class B3QueryFilter {
public:
    b3QueryFilter value;

    B3QueryFilter();
    explicit B3QueryFilter(b3QueryFilter value);

    long long GetCategoryBits() const;
    void SetCategoryBits(long long categoryBits);
    long long GetMaskBits() const;
    void SetMaskBits(long long maskBits);
    long long GetId() const;
    void SetId(long long id);
};

class B3SurfaceMaterial {
public:
    b3SurfaceMaterial value;

    B3SurfaceMaterial();
    explicit B3SurfaceMaterial(b3SurfaceMaterial value);

    float GetFriction() const;
    void SetFriction(float friction);
    float GetRestitution() const;
    void SetRestitution(float restitution);
    float GetRollingResistance() const;
    void SetRollingResistance(float rollingResistance);
    B3Vec3 GetTangentVelocity() const;
    void SetTangentVelocity(const B3Vec3& tangentVelocity);
    long long GetUserMaterialId() const;
    void SetUserMaterialId(long long userMaterialId);
    long GetCustomColor() const;
    void SetCustomColor(long customColor);
};

class B3WorldDef {
public:
    b3WorldDef value;

    B3WorldDef();

    B3Vec3 GetGravity() const;
    void SetGravity(const B3Vec3& gravity);
    float GetRestitutionThreshold() const;
    void SetRestitutionThreshold(float threshold);
    float GetHitEventThreshold() const;
    void SetHitEventThreshold(float threshold);
    float GetContactHertz() const;
    void SetContactHertz(float hertz);
    float GetContactDampingRatio() const;
    void SetContactDampingRatio(float ratio);
    float GetContactSpeed() const;
    void SetContactSpeed(float speed);
    float GetMaximumLinearSpeed() const;
    void SetMaximumLinearSpeed(float speed);
    bool GetEnableSleep() const;
    void SetEnableSleep(bool enabled);
    bool GetEnableContinuous() const;
    void SetEnableContinuous(bool enabled);
    long GetWorkerCount() const;
    void SetWorkerCount(long workerCount);
};

class B3BodyDef {
public:
    b3BodyDef value;

    B3BodyDef();

    int GetType() const;
    void SetType(int type);
    B3Vec3 GetPosition() const;
    void SetPosition(const B3Vec3& position);
    B3Quat GetRotation() const;
    void SetRotation(const B3Quat& rotation);
    B3Vec3 GetLinearVelocity() const;
    void SetLinearVelocity(const B3Vec3& velocity);
    B3Vec3 GetAngularVelocity() const;
    void SetAngularVelocity(const B3Vec3& velocity);
    float GetLinearDamping() const;
    void SetLinearDamping(float damping);
    float GetAngularDamping() const;
    void SetAngularDamping(float damping);
    float GetGravityScale() const;
    void SetGravityScale(float scale);
    float GetSleepThreshold() const;
    void SetSleepThreshold(float threshold);
    B3MotionLocks GetMotionLocks() const;
    void SetMotionLocks(const B3MotionLocks& locks);
    bool GetEnableSleep() const;
    void SetEnableSleep(bool enabled);
    bool GetIsAwake() const;
    void SetIsAwake(bool awake);
    bool GetIsBullet() const;
    void SetIsBullet(bool bullet);
    bool GetIsEnabled() const;
    void SetIsEnabled(bool enabled);
    bool GetAllowFastRotation() const;
    void SetAllowFastRotation(bool allowFastRotation);
    bool GetEnableContactRecycling() const;
    void SetEnableContactRecycling(bool enableContactRecycling);
};

class B3ShapeDef {
public:
    b3ShapeDef value;

    B3ShapeDef();

    B3SurfaceMaterial GetBaseMaterial() const;
    void SetBaseMaterial(const B3SurfaceMaterial& material);
    float GetDensity() const;
    void SetDensity(float density);
    float GetExplosionScale() const;
    void SetExplosionScale(float scale);
    B3Filter GetFilter() const;
    void SetFilter(const B3Filter& filter);
    bool GetEnableCustomFiltering() const;
    void SetEnableCustomFiltering(bool enabled);
    bool GetIsSensor() const;
    void SetIsSensor(bool sensor);
    bool GetEnableSensorEvents() const;
    void SetEnableSensorEvents(bool enabled);
    bool GetEnableContactEvents() const;
    void SetEnableContactEvents(bool enabled);
    bool GetEnableHitEvents() const;
    void SetEnableHitEvents(bool enabled);
    bool GetEnablePreSolveEvents() const;
    void SetEnablePreSolveEvents(bool enabled);
    bool GetInvokeContactCreation() const;
    void SetInvokeContactCreation(bool invokeContactCreation);
    bool GetUpdateBodyMass() const;
    void SetUpdateBodyMass(bool updateBodyMass);
};

class B3ContactId {
public:
    uint32_t values[3];

    B3ContactId();
    B3ContactId(long value0, long value1, long value2);
    explicit B3ContactId(b3ContactId id);

    long GetValue0() const;
    long GetValue1() const;
    long GetValue2() const;
    bool IsNull() const;
    b3ContactId Load() const;
};

class B3RayResult {
public:
    b3RayResult value;

    B3RayResult();
    explicit B3RayResult(b3RayResult value);

    long long GetShapeId() const;
    B3Vec3 GetPoint() const;
    B3Vec3 GetNormal() const;
    long long GetUserMaterialId() const;
    float GetFraction() const;
    int GetTriangleIndex() const;
    int GetChildIndex() const;
    int GetNodeVisits() const;
    int GetLeafVisits() const;
    bool GetHit() const;
};

class B3BodyMoveEvent {
public:
    long long bodyId;
    B3Transform transform;
    bool fellAsleep;

    B3BodyMoveEvent();
    explicit B3BodyMoveEvent(const b3BodyMoveEvent& event);

    long long GetBodyId() const;
    B3Transform GetTransform() const;
    bool GetFellAsleep() const;
};

class B3BodyEvents {
public:
    std::vector<B3BodyMoveEvent> moveEvents;

    B3BodyEvents();
    explicit B3BodyEvents(const b3BodyEvents& events);

    int GetMoveCount() const;
    B3BodyMoveEvent GetMoveEvent(int index) const;
};

class B3SensorBeginTouchEvent {
public:
    long long sensorShapeId;
    long long visitorShapeId;

    B3SensorBeginTouchEvent();
    explicit B3SensorBeginTouchEvent(const b3SensorBeginTouchEvent& event);

    long long GetSensorShapeId() const;
    long long GetVisitorShapeId() const;
};

class B3SensorEndTouchEvent {
public:
    long long sensorShapeId;
    long long visitorShapeId;

    B3SensorEndTouchEvent();
    explicit B3SensorEndTouchEvent(const b3SensorEndTouchEvent& event);

    long long GetSensorShapeId() const;
    long long GetVisitorShapeId() const;
};

class B3SensorEvents {
public:
    std::vector<B3SensorBeginTouchEvent> beginEvents;
    std::vector<B3SensorEndTouchEvent> endEvents;

    B3SensorEvents();
    explicit B3SensorEvents(const b3SensorEvents& events);

    int GetBeginCount() const;
    B3SensorBeginTouchEvent GetBeginEvent(int index) const;
    int GetEndCount() const;
    B3SensorEndTouchEvent GetEndEvent(int index) const;
};

class B3ContactBeginTouchEvent {
public:
    long long shapeIdA;
    long long shapeIdB;
    B3ContactId contactId;

    B3ContactBeginTouchEvent();
    explicit B3ContactBeginTouchEvent(const b3ContactBeginTouchEvent& event);

    long long GetShapeIdA() const;
    long long GetShapeIdB() const;
    B3ContactId GetContactId() const;
};

class B3ContactEndTouchEvent {
public:
    long long shapeIdA;
    long long shapeIdB;
    B3ContactId contactId;

    B3ContactEndTouchEvent();
    explicit B3ContactEndTouchEvent(const b3ContactEndTouchEvent& event);

    long long GetShapeIdA() const;
    long long GetShapeIdB() const;
    B3ContactId GetContactId() const;
};

class B3ContactHitEvent {
public:
    long long shapeIdA;
    long long shapeIdB;
    B3ContactId contactId;
    B3Vec3 point;
    B3Vec3 normal;
    float approachSpeed;
    long long userMaterialIdA;
    long long userMaterialIdB;

    B3ContactHitEvent();
    explicit B3ContactHitEvent(const b3ContactHitEvent& event);

    long long GetShapeIdA() const;
    long long GetShapeIdB() const;
    B3ContactId GetContactId() const;
    B3Vec3 GetPoint() const;
    B3Vec3 GetNormal() const;
    float GetApproachSpeed() const;
    long long GetUserMaterialIdA() const;
    long long GetUserMaterialIdB() const;
};

class B3ContactEvents {
public:
    std::vector<B3ContactBeginTouchEvent> beginEvents;
    std::vector<B3ContactEndTouchEvent> endEvents;
    std::vector<B3ContactHitEvent> hitEvents;

    B3ContactEvents();
    explicit B3ContactEvents(const b3ContactEvents& events);

    int GetBeginCount() const;
    B3ContactBeginTouchEvent GetBeginEvent(int index) const;
    int GetEndCount() const;
    B3ContactEndTouchEvent GetEndEvent(int index) const;
    int GetHitCount() const;
    B3ContactHitEvent GetHitEvent(int index) const;
};

class B3Hull {
public:
    B3Hull();
    explicit B3Hull(b3HullData* hull);
    explicit B3Hull(b3BoxHull boxHull);
    ~B3Hull();

    static B3Hull* CreateBox(float hx, float hy, float hz);
    static B3Hull* CreateOffsetBox(float hx, float hy, float hz, const B3Vec3& offset);
    static B3Hull* CreateTransformedBox(float hx, float hy, float hz, const B3Transform& transform);
    static B3Hull* CreateScaledBox(const B3Vec3& halfWidths, const B3Transform& transform, const B3Vec3& postScale);
    static B3Hull* CreateCube(float halfWidth);
    static B3Hull* CreateCylinder(float height, float radius, float yOffset, int sides);
    static B3Hull* CreateCone(float height, float radius1, float radius2, int slices);
    static B3Hull* CreateRock(float radius);

    bool IsValid() const;
    void Destroy();
    int GetVertexCount() const;
    int GetFaceCount() const;
    const b3HullData* GetHandle() const;

private:
    b3HullData* m_hull;
    b3BoxHull m_boxHull;
    bool m_ownsHull;
};

class B3Shape;
class B3Joint;
class B3World;

class B3Body {
public:
    B3Body();
    explicit B3Body(long long bodyId);
    explicit B3Body(b3BodyId bodyId);

    long long GetId() const;
    bool IsValid() const;
    void Destroy();
    int GetType() const;
    void SetType(int type);
    B3Vec3 GetPosition() const;
    B3Quat GetRotation() const;
    B3Transform GetTransform() const;
    void SetTransform(const B3Vec3& position, const B3Quat& rotation);
    void SetTargetTransform(const B3Vec3& position, const B3Quat& rotation, float timeStep, bool wake);
    B3Vec3 GetLinearVelocity() const;
    void SetLinearVelocity(const B3Vec3& velocity);
    B3Vec3 GetAngularVelocity() const;
    void SetAngularVelocity(const B3Vec3& velocity);
    void ApplyForce(const B3Vec3& force, const B3Vec3& point, bool wake);
    void ApplyForceToCenter(const B3Vec3& force, bool wake);
    void ApplyTorque(const B3Vec3& torque, bool wake);
    void ApplyLinearImpulse(const B3Vec3& impulse, const B3Vec3& point, bool wake);
    void ApplyLinearImpulseToCenter(const B3Vec3& impulse, bool wake);
    void ApplyAngularImpulse(const B3Vec3& impulse, bool wake);
    float GetMass() const;
    float GetInverseMass() const;
    void ApplyMassFromShapes();
    float GetLinearDamping() const;
    void SetLinearDamping(float damping);
    float GetAngularDamping() const;
    void SetAngularDamping(float damping);
    float GetGravityScale() const;
    void SetGravityScale(float scale);
    bool IsAwake() const;
    void SetAwake(bool awake);
    bool IsEnabled() const;
    void Disable();
    void Enable();
    B3MotionLocks GetMotionLocks() const;
    void SetMotionLocks(const B3MotionLocks& locks);
    bool IsBullet() const;
    void SetBullet(bool bullet);
    int GetShapeCount() const;
    B3AABB ComputeAABB() const;
    B3Shape* CreateSphereShape(const B3ShapeDef& def, const B3Sphere& sphere);
    B3Shape* CreateCapsuleShape(const B3ShapeDef& def, const B3Capsule& capsule);
    B3Shape* CreateHullShape(const B3ShapeDef& def, const B3Hull& hull);

private:
    b3BodyId m_bodyId;
};

class B3Joint {
public:
    B3Joint();
    explicit B3Joint(long long jointId);
    explicit B3Joint(b3JointId jointId);

    long long GetId() const;
    bool IsValid() const;
    void Destroy(bool wakeAttached);
    long long GetBodyIdA() const;
    long long GetBodyIdB() const;
    void WakeBodies();
    float GetLinearSeparation() const;
    void SetRevoluteTargetAngle(float radians);

private:
    b3JointId m_jointId;
};

class B3Shape {
public:
    B3Shape();
    explicit B3Shape(long long shapeId);
    explicit B3Shape(b3ShapeId shapeId);

    long long GetId() const;
    bool IsValid() const;
    void Destroy(bool updateBodyMass);
    int GetType() const;
    long long GetBodyId() const;
    bool IsSensor() const;
    float GetDensity() const;
    void SetDensity(float density, bool updateBodyMass);
    float GetFriction() const;
    void SetFriction(float friction);
    float GetRestitution() const;
    void SetRestitution(float restitution);
    B3Filter GetFilter() const;
    void SetFilter(const B3Filter& filter, bool invokeContacts);
    void EnableSensorEvents(bool enabled);
    bool AreSensorEventsEnabled() const;
    void EnableContactEvents(bool enabled);
    bool AreContactEventsEnabled() const;
    void EnableHitEvents(bool enabled);
    bool AreHitEventsEnabled() const;
    B3RayResult RayCast(const B3Vec3& origin, const B3Vec3& translation) const;
    B3Sphere GetSphere() const;
    void SetSphere(const B3Sphere& sphere);
    B3Capsule GetCapsule() const;
    void SetCapsule(const B3Capsule& capsule);
    B3AABB GetAABB() const;
    B3Vec3 GetClosestPoint(const B3Vec3& target) const;
    void ApplyWind(const B3Vec3& wind, float drag, float lift, float maxSpeed, bool wake);

private:
    b3ShapeId m_shapeId;
};

class B3DebugDrawEm {
public:
    B3DebugDrawEm();
    virtual ~B3DebugDrawEm();

    void DrawWorld(B3World* world, long long maskBits);
    void SetDrawingBounds(const B3AABB& bounds);
    B3AABB GetDrawingBounds() const;
    void SetForceScale(float scale);
    float GetForceScale() const;
    void SetJointScale(float scale);
    float GetJointScale() const;
    void SetDrawShapes(bool enabled);
    bool GetDrawShapes() const;
    void SetDrawJoints(bool enabled);
    bool GetDrawJoints() const;
    void SetDrawJointExtras(bool enabled);
    bool GetDrawJointExtras() const;
    void SetDrawBounds(bool enabled);
    bool GetDrawBounds() const;
    void SetDrawMass(bool enabled);
    bool GetDrawMass() const;
    void SetDrawBodyNames(bool enabled);
    bool GetDrawBodyNames() const;
    void SetDrawContacts(bool enabled);
    bool GetDrawContacts() const;
    void SetDrawAnchorA(bool enabled);
    bool GetDrawAnchorA() const;
    void SetDrawGraphColors(bool enabled);
    bool GetDrawGraphColors() const;
    void SetDrawContactFeatures(bool enabled);
    bool GetDrawContactFeatures() const;
    void SetDrawContactNormals(bool enabled);
    bool GetDrawContactNormals() const;
    void SetDrawContactForces(bool enabled);
    bool GetDrawContactForces() const;
    void SetDrawFrictionForces(bool enabled);
    bool GetDrawFrictionForces() const;
    void SetDrawIslands(bool enabled);
    bool GetDrawIslands() const;

    virtual bool DrawShape(B3DebugShape* shape, const B3Transform& transform, int color);
    virtual void DrawSegment(const B3Vec3& p1, const B3Vec3& p2, int color);
    virtual void DrawTransform(const B3Transform& transform);
    virtual void DrawPoint(const B3Vec3& p, float size, int color);
    virtual void DrawSphere(const B3Vec3& p, float radius, int color, float alpha);
    virtual void DrawCapsule(const B3Vec3& p1, const B3Vec3& p2, float radius, int color, float alpha);
    virtual void DrawBounds(const B3AABB& aabb, int color);
    virtual void DrawBox(const B3Vec3& extents, const B3Transform& transform, int color);

private:
    b3DebugDraw m_draw;
};

class B3World {
public:
    B3World();
    explicit B3World(const B3WorldDef& def);
    ~B3World();

    long long GetId() const;
    bool IsValid() const;
    void Destroy();
    void Step(float timeStep, int subStepCount);
    B3AABB GetBounds() const;
    B3Vec3 GetGravity() const;
    void SetGravity(const B3Vec3& gravity);
    bool IsSleepingEnabled() const;
    void EnableSleeping(bool enabled);
    bool IsWarmStartingEnabled() const;
    void EnableWarmStarting(bool enabled);
    bool IsContinuousEnabled() const;
    void EnableContinuous(bool enabled);
    float GetContactRecycleDistance() const;
    void SetContactRecycleDistance(float recycleDistance);
    void SetContactTuning(float hertz, float dampingRatio, float contactSpeed);
    int GetWorkerCount() const;
    void SetWorkerCount(long workerCount);
    int GetAwakeBodyCount() const;
    B3Body* CreateBody(const B3BodyDef& def);
    B3Joint* CreateDistanceJoint(const B3DistanceJointDef& def);
    B3Joint* CreateMotorJoint(const B3MotorJointDef& def);
    B3Joint* CreateParallelJoint(const B3ParallelJointDef& def);
    B3Joint* CreatePrismaticJoint(const B3PrismaticJointDef& def);
    B3Joint* CreateSphericalJoint(const B3SphericalJointDef& def);
    B3Joint* CreateRevoluteJoint(const B3RevoluteJointDef& def);
    B3Joint* CreateWeldJoint(const B3WeldJointDef& def);
    B3Joint* CreateFilterJoint(const B3FilterJointDef& def);
    B3Joint* CreateWheelJoint(const B3WheelJointDef& def);
    B3BodyEvents* GetBodyEvents() const;
    B3SensorEvents* GetSensorEvents() const;
    B3ContactEvents* GetContactEvents() const;
    B3RayResult CastRayClosest(const B3Vec3& origin, const B3Vec3& translation, const B3QueryFilter& filter) const;
    b3WorldId GetHandle() const;

private:
    b3WorldId m_worldId;
    bool m_destroyed;
};

class B3 {
public:
    static int StaticBody();
    static int KinematicBody();
    static int DynamicBody();
    static int CapsuleShape();
    static int CompoundShape();
    static int HeightShape();
    static int HullShape();
    static int MeshShape();
    static int SphereShape();
    static int GetWorldCount();
    static int GetMaxWorldCount();
    static long long DefaultMaskBits();
};

} // namespace JBox3D

// jParser compiles upstream Box3D sources as C17. Keep the wrapper implementation
// header-included so it is compiled by the generated C++ glue stage instead.
#include "jBox3D.cpp"
