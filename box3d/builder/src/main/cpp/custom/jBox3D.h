#pragma once

#include <box3d/box3d.h>
#include <box3d/collision.h>
#include <box3d/constants.h>

#include <array>
#include <cmath>
#include <cstdlib>
#include <cstdint>
#include <limits>
#include <memory>
#include <string>
#include <vector>

#define TINYOBJLOADER_USE_MAPBOX_EARCUT
#define TINYOBJLOADER_IMPLEMENTATION
#include "samples/tiny_obj_loader.h"

using NativeString = std::string;

namespace JBox3D {

class B3Mesh;
class B3HeightField;
class B3Compound;
class B3ContactData;
class B3DebugDrawEm;
class B3World;

class B3ByteArray {
public:
    B3ByteArray();
    explicit B3ByteArray(int size);
    B3ByteArray(const uint8_t* data, int size);

    int GetSize() const;
    int GetValue(int index) const;
    void SetValue(int index, int value);
    void Resize(int size);

    const uint8_t* GetData() const;
    uint8_t* GetMutableData();

private:
    std::vector<uint8_t> m_values;
};

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

class B3CosSin {
public:
    b3CosSin value;

    B3CosSin();
    explicit B3CosSin(b3CosSin value);

    float GetCosine() const;
    float GetSine() const;
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
    void Normalize();
    B3Vec3 RotateVector(const B3Vec3& vector) const;
    static B3Quat* ComputeBetweenUnitVectors(const B3Vec3& from, const B3Vec3& to);
    static B3Quat* Mul(const B3Quat& a, const B3Quat& b);
    static B3Quat* InvMul(const B3Quat& a, const B3Quat& b);
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
    B3Vec3 TransformPoint(const B3Vec3& point) const;
    static B3Transform* InvMul(const B3Transform& a, const B3Transform& b);
};

class B3Matrix3 {
public:
    b3Matrix3 value;

    B3Matrix3();
    B3Matrix3(const B3Vec3& columnX, const B3Vec3& columnY, const B3Vec3& columnZ);
    explicit B3Matrix3(b3Matrix3 value);

    B3Vec3 GetColumnX() const;
    void SetColumnX(const B3Vec3& column);
    B3Vec3 GetColumnY() const;
    void SetColumnY(const B3Vec3& column);
    B3Vec3 GetColumnZ() const;
    void SetColumnZ(const B3Vec3& column);
};

class B3Plane {
public:
    b3Plane value;

    B3Plane();
    B3Plane(const B3Vec3& normal, float offset);
    explicit B3Plane(b3Plane value);

    B3Vec3 GetNormal() const;
    void SetNormal(const B3Vec3& normal);
    float GetOffset() const;
    void SetOffset(float offset);
};

class B3SegmentDistanceResult {
public:
    B3SegmentDistanceResult();
    explicit B3SegmentDistanceResult(b3SegmentDistanceResult value);

    B3Vec3 GetPoint1() const;
    float GetFraction1() const;
    B3Vec3 GetPoint2() const;
    float GetFraction2() const;

private:
    b3SegmentDistanceResult m_value;
};

class B3Timer {
public:
    B3Timer();

    float GetMilliseconds() const;
    float GetMillisecondsAndReset();
    void Reset();
    long long GetTicks() const;

private:
    uint64_t m_ticks;
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

class B3MassData {
public:
    b3MassData value;

    B3MassData();
    explicit B3MassData(b3MassData value);

    float GetMass() const;
    void SetMass(float mass);
    B3Vec3 GetCenter() const;
    void SetCenter(const B3Vec3& center);
    B3Vec3 GetInertiaColumnX() const;
    void SetInertiaColumnX(const B3Vec3& column);
    B3Vec3 GetInertiaColumnY() const;
    void SetInertiaColumnY(const B3Vec3& column);
    B3Vec3 GetInertiaColumnZ() const;
    void SetInertiaColumnZ(const B3Vec3& column);
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
    void SetForceThreshold(float force);
    void SetTorqueThreshold(float torque);
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
    void SetForceThreshold(float force);
    void SetTorqueThreshold(float torque);
    float GetConstraintHertz() const;
    void SetConstraintHertz(float hertz);
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
    bool GetEnableConeLimit() const;
    void SetEnableConeLimit(bool enabled);
    float GetConeAngle() const;
    void SetConeAngle(float radians);
    bool GetEnableTwistLimit() const;
    void SetEnableTwistLimit(bool enabled);
    float GetLowerTwistAngle() const;
    void SetLowerTwistAngle(float radians);
    float GetUpperTwistAngle() const;
    void SetUpperTwistAngle(float radians);
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
    void SetForceThreshold(float force);
    void SetTorqueThreshold(float torque);
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
    void SetForceThreshold(float force);
    void SetTorqueThreshold(float torque);
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
    ~B3DebugShape();

    long long GetShapeId() const;
    long long GetGeometryId() const;
    int GetType() const;
    B3Vec3 GetScale() const;
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
    friend class B3World;
    friend class B3DebugDrawEm;

    void AddSphere(const b3Sphere& sphere, b3Transform transform);
    void AddCapsule(const b3Capsule& capsule, b3Transform transform);
    void AddEdge(b3Vec3 v0, b3Vec3 v1);
    void AddTriangle(b3Vec3 v0, b3Vec3 v1, b3Vec3 v2);
    void AddHull(const b3HullData* hull, b3Transform transform);
    void AddMesh(const b3Mesh* mesh, b3Transform transform);
    void AddHeightField(const b3HeightFieldData* heightField, b3Transform transform);
    void AddCompound(const b3CompoundData* compound);
    B3DebugShape* FindOrCreateHullGeometry(const b3HullData* hull);
    B3DebugShape* FindOrCreateMeshGeometry(const b3MeshData* mesh);
    static bool AddHeightFieldTriangle(b3Vec3 v0, b3Vec3 v1, b3Vec3 v2, int triangleIndex, void* context);

    long long m_shapeId;
    long long m_geometryId;
    int m_type;
    B3Vec3 m_scale;
    B3Sphere m_sphere;
    B3Capsule m_capsule;
    const B3DebugShape* m_geometrySource;
    const b3CompoundData* m_compound;
    b3Transform m_localTransform;
    std::vector<B3DebugShape*> m_compoundChildren;
    std::vector<B3DebugShape*> m_ownedCompoundGeometries;
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

class B3Capacity {
public:
    b3Capacity value;

    B3Capacity();
    explicit B3Capacity(b3Capacity value);

    int GetStaticShapeCount() const;
    void SetStaticShapeCount(int count);
    int GetDynamicShapeCount() const;
    void SetDynamicShapeCount(int count);
    int GetStaticBodyCount() const;
    void SetStaticBodyCount(int count);
    int GetDynamicBodyCount() const;
    void SetDynamicBodyCount(int count);
    int GetContactCount() const;
    void SetContactCount(int count);
};

class B3Profile {
public:
    B3Profile();
    explicit B3Profile(b3Profile value);

    float GetStep() const;
    float GetPairs() const;
    float GetCollide() const;
    float GetSolve() const;
    float GetSolverSetup() const;
    float GetConstraints() const;
    float GetPrepareConstraints() const;
    float GetIntegrateVelocities() const;
    float GetWarmStart() const;
    float GetSolveImpulses() const;
    float GetIntegratePositions() const;
    float GetRelaxImpulses() const;
    float GetApplyRestitution() const;
    float GetStoreImpulses() const;
    float GetSplitIslands() const;
    float GetTransforms() const;
    float GetSensorHits() const;
    float GetJointEvents() const;
    float GetHitEvents() const;
    float GetRefit() const;
    float GetBullets() const;
    float GetSleepIslands() const;
    float GetSensors() const;

private:
    b3Profile m_value;
};

class B3Counters {
public:
    B3Counters();
    explicit B3Counters(b3Counters value);

    int GetBodyCount() const;
    int GetShapeCount() const;
    int GetContactCount() const;
    int GetJointCount() const;
    int GetIslandCount() const;
    int GetStackUsed() const;
    int GetArenaCapacity() const;
    int GetStaticTreeHeight() const;
    int GetTreeHeight() const;
    int GetSatCallCount() const;
    int GetSatCacheHitCount() const;
    int GetByteCount() const;
    int GetTaskCount() const;
    int GetColorCount(int index) const;
    int GetManifoldCount(int index) const;
    int GetAwakeContactCount() const;
    int GetRecycledContactCount() const;
    int GetDistanceIterations() const;
    int GetPushBackIterations() const;
    int GetRootIterations() const;

private:
    b3Counters m_value;
};

class B3ExplosionDef {
public:
    b3ExplosionDef value;

    B3ExplosionDef();

    long long GetMaskBits() const;
    void SetMaskBits(long long maskBits);
    B3Vec3 GetPosition() const;
    void SetPosition(const B3Vec3& position);
    float GetRadius() const;
    void SetRadius(float radius);
    float GetFalloff() const;
    void SetFalloff(float falloff);
    float GetImpulsePerArea() const;
    void SetImpulsePerArea(float impulse);
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
    B3Capacity GetCapacity() const;
    void SetCapacity(const B3Capacity& capacity);
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
    bool GetEnableSpeculativeContact() const;
    void SetEnableSpeculativeContact(bool enabled);
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
    bool IsValid() const;
    B3ContactData* GetData() const;
    b3ContactId Load() const;
};

class B3ManifoldPoint {
public:
    B3ManifoldPoint();
    explicit B3ManifoldPoint(b3ManifoldPoint value);

    B3Vec3 GetAnchorA() const;
    B3Vec3 GetAnchorB() const;
    float GetSeparation() const;
    float GetBaseSeparation() const;
    float GetNormalImpulse() const;
    float GetTotalNormalImpulse() const;
    float GetNormalVelocity() const;
    long GetFeatureId() const;
    int GetTriangleIndex() const;
    bool GetPersisted() const;

private:
    b3ManifoldPoint m_value;
};

class B3Manifold {
public:
    B3Manifold();
    explicit B3Manifold(b3Manifold value);

    int GetPointCount() const;
    B3ManifoldPoint GetPoint(int index) const;
    B3Vec3 GetNormal() const;
    float GetTwistImpulse() const;
    B3Vec3 GetFrictionImpulse() const;
    B3Vec3 GetRollingImpulse() const;

private:
    b3Manifold m_value;
};

class B3ContactData {
public:
    B3ContactData();
    explicit B3ContactData(const b3ContactData& value);

    B3ContactId GetContactId() const;
    long long GetShapeIdA() const;
    long long GetShapeIdB() const;
    int GetManifoldCount() const;
    B3Manifold GetManifold(int index) const;

private:
    b3ContactId m_contactId;
    b3ShapeId m_shapeIdA;
    b3ShapeId m_shapeIdB;
    std::vector<b3Manifold> m_manifolds;
};

class B3ContactDataArray {
public:
    B3ContactDataArray();
    explicit B3ContactDataArray(const std::vector<b3ContactData>& values);

    int GetSize() const;
    B3ContactData GetValue(int index) const;

private:
    std::vector<B3ContactData> m_values;
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

class B3JointEvent {
public:
    long long jointId;

    B3JointEvent();
    explicit B3JointEvent(const b3JointEvent& event);

    long long GetJointId() const;
};

class B3JointEvents {
public:
    std::vector<B3JointEvent> jointEvents;

    B3JointEvents();
    explicit B3JointEvents(const b3JointEvents& events);

    int GetCount() const;
    B3JointEvent GetEvent(int index) const;
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

class B3Recording {
public:
    B3Recording();
    explicit B3Recording(int byteCapacity);
    explicit B3Recording(b3Recording* recording);
    ~B3Recording();

    static B3Recording* Load(const char* path);
    bool IsValid() const;
    void Destroy();
    int GetSize() const;
    B3ByteArray* GetData() const;
    bool Save(const char* path) const;
    bool ValidateReplay(int workerCount) const;
    b3Recording* GetHandle() const;

private:
    b3Recording* m_recording;
};

class B3RecPlayerInfo {
public:
    B3RecPlayerInfo();
    explicit B3RecPlayerInfo(b3RecPlayerInfo value);

    int GetFrameCount() const;
    int GetWorkerCount() const;
    float GetTimeStep() const;
    int GetSubStepCount() const;
    float GetLengthScale() const;
    B3AABB GetBounds() const;

private:
    b3RecPlayerInfo m_value;
};

class B3RecQueryInfo {
public:
    B3RecQueryInfo();
    explicit B3RecQueryInfo(const b3RecQueryInfo& value);

    int GetType() const;
    B3QueryFilter GetFilter() const;
    B3AABB GetAABB() const;
    B3Vec3 GetOrigin() const;
    B3Vec3 GetTranslation() const;
    int GetHitCount() const;
    long long GetKey() const;
    long long GetId() const;
    void GetName(NativeString& name) const;

private:
    b3RecQueryType m_type;
    b3QueryFilter m_filter;
    b3AABB m_aabb;
    b3Pos m_origin;
    b3Vec3 m_translation;
    int m_hitCount;
    uint64_t m_key;
    uint64_t m_id;
    std::string m_name;
};

class B3RecQueryHit {
public:
    B3RecQueryHit();
    explicit B3RecQueryHit(b3RecQueryHit value);

    long long GetShapeId() const;
    B3Vec3 GetPoint() const;
    B3Vec3 GetNormal() const;
    float GetFraction() const;

private:
    b3RecQueryHit m_value;
};

class B3RecPlayer {
public:
    B3RecPlayer();
    B3RecPlayer(const B3ByteArray& data, int workerCount);
    ~B3RecPlayer();

    bool IsValid() const;
    void Destroy();
    bool StepFrame();
    void SubStepFrame();
    void Restart();
    void SeekFrame(int targetFrame);
    B3World* GetWorld() const;
    long long GetWorldId() const;
    int GetFrame() const;
    int GetFrameCount() const;
    bool IsAtEnd() const;
    bool IsAtPreStep() const;
    bool HasDiverged() const;
    B3RecPlayerInfo GetInfo() const;
    int GetDivergeFrame() const;
    void SetWorkerCount(int count);
    void SetKeyframePolicy(long long budgetBytes, int minIntervalFrames);
    long long GetKeyframeBudget() const;
    int GetKeyframeMinInterval() const;
    int GetKeyframeInterval() const;
    long long GetKeyframeBytes() const;
    int GetBodyCount() const;
    long long GetBodyId(int index) const;
    void SetDebugShapeCallbacks();
    void DrawFrameQueries(B3DebugDrawEm* draw, int queryIndex, int selectedIndex);
    int GetFrameQueryCount() const;
    B3RecQueryInfo GetFrameQuery(int index) const;
    B3RecQueryHit GetFrameQueryHit(int queryIndex, int hitIndex) const;

private:
    b3RecPlayer* m_player;
};

class B3Vec3Array {
public:
    explicit B3Vec3Array(int size);

    int GetSize() const;
    B3Vec3 GetValue(int index) const;
    void SetValue(int index, const B3Vec3& value);
    const b3Vec3* GetData() const;

private:
    std::vector<b3Vec3> m_values;
};

class B3Hull {
public:
    B3Hull();
    explicit B3Hull(b3HullData* hull);
    B3Hull(const b3HullData* hull, bool ownsHull);
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
    static B3Hull* CreateFromPoints(const B3Vec3Array& points, int maxVertexCount);
    static B3Hull* CloneAndTransform(const B3Hull& hull, const B3Transform& transform, const B3Vec3& scale);

    bool IsValid() const;
    void Destroy();
    int GetVertexCount() const;
    int GetFaceCount() const;
    B3Vec3 GetPoint(int index) const;
    const b3HullData* GetHandle() const;

private:
    b3HullData* m_hull;
    b3BoxHull m_boxHull;
    bool m_ownsHull;
};

class B3ShapeProxy {
public:
    B3ShapeProxy(const B3Vec3Array& points, int count, float radius);
    B3ShapeProxy(const B3Hull& hull, float radius);

    int GetCount() const;
    float GetRadius() const;
    const b3ShapeProxy& GetHandle() const;

private:
    std::vector<b3Vec3> m_points;
    b3ShapeProxy m_proxy;
};

class B3LocalManifoldPoint {
public:
    B3LocalManifoldPoint();
    explicit B3LocalManifoldPoint(const b3LocalManifoldPoint& point);

    B3Vec3 GetPoint() const;
    float GetSeparation() const;
    int GetOwner1() const;
    int GetIndex1() const;
    int GetOwner2() const;
    int GetIndex2() const;
    int GetTriangleIndex() const;

private:
    b3LocalManifoldPoint m_point;
};

class B3LocalManifold {
public:
    explicit B3LocalManifold(int capacity);

    B3Vec3 GetNormal() const;
    B3Vec3 GetTriangleNormal() const;
    int GetPointCount() const;
    B3LocalManifoldPoint GetPoint(int index) const;
    int GetFeature() const;
    int GetTriangleIndex() const;

private:
    friend class B3Collision;

    void Clear();
    int GetCapacity() const;
    b3LocalManifold* GetHandle();

    std::vector<b3LocalManifoldPoint> m_points;
    b3LocalManifold m_manifold;
};

class B3DistanceOutput {
public:
    B3DistanceOutput();
    explicit B3DistanceOutput(const b3DistanceOutput& output);

    B3Vec3 GetPointA() const;
    B3Vec3 GetPointB() const;
    B3Vec3 GetNormal() const;
    float GetDistance() const;
    int GetIterations() const;
    int GetSimplexCount() const;

private:
    b3DistanceOutput m_output;
};

class B3CastOutput {
public:
    B3CastOutput();
    explicit B3CastOutput(const b3CastOutput& output);

    B3Vec3 GetNormal() const;
    B3Vec3 GetPoint() const;
    float GetFraction() const;
    int GetIterations() const;
    bool GetHit() const;

private:
    b3CastOutput m_output;
};

class B3RayCastInput {
public:
    b3RayCastInput value;

    B3RayCastInput();
    B3RayCastInput(const B3Vec3& origin, const B3Vec3& translation, float maxFraction);
    explicit B3RayCastInput(b3RayCastInput value);

    B3Vec3 GetOrigin() const;
    void SetOrigin(const B3Vec3& origin);
    B3Vec3 GetTranslation() const;
    void SetTranslation(const B3Vec3& translation);
    float GetMaxFraction() const;
    void SetMaxFraction(float fraction);
};

class B3BoxCastInput {
public:
    b3BoxCastInput value;

    B3BoxCastInput();
    B3BoxCastInput(const B3AABB& box, const B3Vec3& translation, float maxFraction);
    explicit B3BoxCastInput(b3BoxCastInput value);

    B3AABB GetBox() const;
    void SetBox(const B3AABB& box);
    B3Vec3 GetTranslation() const;
    void SetTranslation(const B3Vec3& translation);
    float GetMaxFraction() const;
    void SetMaxFraction(float fraction);
};

class B3TreeStats {
public:
    B3TreeStats();
    explicit B3TreeStats(b3TreeStats value);

    int GetNodeVisits() const;
    int GetLeafVisits() const;

private:
    b3TreeStats m_value;
};

class B3TreeClosestResult {
public:
    B3TreeClosestResult();
    B3TreeClosestResult(b3TreeStats stats, float minDistanceSquared);

    B3TreeStats GetStats() const;
    float GetMinDistanceSquared() const;

private:
    b3TreeStats m_stats;
    float m_minDistanceSquared;
};

class B3TreeQueryCallbackEm {
public:
    B3TreeQueryCallbackEm();
    virtual ~B3TreeQueryCallbackEm();
    virtual bool Query(int proxyId, long long userData);
};

class B3TreeClosestCallbackEm {
public:
    B3TreeClosestCallbackEm();
    virtual ~B3TreeClosestCallbackEm();
    virtual float QueryClosest(float distanceSquaredMin, int proxyId, long long userData);
};

class B3TreeRayCastCallbackEm {
public:
    B3TreeRayCastCallbackEm();
    virtual ~B3TreeRayCastCallbackEm();
    virtual float RayCast(const B3RayCastInput& input, int proxyId, long long userData);
};

class B3TreeBoxCastCallbackEm {
public:
    B3TreeBoxCastCallbackEm();
    virtual ~B3TreeBoxCastCallbackEm();
    virtual float BoxCast(const B3BoxCastInput& input, int proxyId, long long userData);
};

class B3DynamicTree {
public:
    B3DynamicTree();
    explicit B3DynamicTree(int proxyCapacity);
    explicit B3DynamicTree(b3DynamicTree tree);
    ~B3DynamicTree();

    static B3DynamicTree* Load(const char* fileName, float scale);
    bool IsValid() const;
    void Destroy();
    int CreateProxy(const B3AABB& aabb, long long categoryBits, long long userData);
    void DestroyProxy(int proxyId);
    void MoveProxy(int proxyId, const B3AABB& aabb);
    void EnlargeProxy(int proxyId, const B3AABB& aabb);
    void SetCategoryBits(int proxyId, long long categoryBits);
    long long GetCategoryBits(int proxyId);
    long long GetUserData(int proxyId) const;
    B3AABB GetAABB(int proxyId) const;
    B3TreeStats Query(const B3AABB& aabb, long long maskBits, bool requireAllBits,
                      B3TreeQueryCallbackEm* callback) const;
    B3TreeClosestResult QueryClosest(const B3Vec3& point, long long maskBits, bool requireAllBits,
                                     B3TreeClosestCallbackEm* callback, float minDistanceSquared) const;
    B3TreeStats RayCast(const B3RayCastInput& input, long long maskBits, bool requireAllBits,
                        B3TreeRayCastCallbackEm* callback) const;
    B3TreeStats BoxCast(const B3BoxCastInput& input, long long maskBits, bool requireAllBits,
                        B3TreeBoxCastCallbackEm* callback) const;
    int GetHeight() const;
    float GetAreaRatio() const;
    B3AABB GetRootBounds() const;
    int GetProxyCount() const;
    int Rebuild(bool fullBuild);
    int GetByteCount() const;
    void Validate() const;
    void ValidateNoEnlarged() const;
    void Save(const char* fileName) const;

private:
    b3DynamicTree m_tree;
    bool m_destroyed;
};

class B3Sweep {
public:
    B3Sweep();

    B3Vec3 GetLocalCenter() const;
    void SetLocalCenter(const B3Vec3& center);
    B3Vec3 GetC1() const;
    void SetC1(const B3Vec3& center);
    B3Vec3 GetC2() const;
    void SetC2(const B3Vec3& center);
    B3Quat GetQ1() const;
    void SetQ1(const B3Quat& rotation);
    B3Quat GetQ2() const;
    void SetQ2(const B3Quat& rotation);
    B3Transform GetTransform(float fraction) const;

private:
    friend class B3Collision;
    b3Sweep m_sweep;
};

class B3TOIOutput {
public:
    B3TOIOutput();
    explicit B3TOIOutput(const b3TOIOutput& output);

    int GetState() const;
    B3Vec3 GetPoint() const;
    B3Vec3 GetNormal() const;
    float GetFraction() const;
    float GetDistance() const;
    int GetDistanceIterations() const;
    int GetPushBackIterations() const;
    int GetRootIterations() const;
    bool GetUsedFallback() const;

private:
    b3TOIOutput m_output;
};

class B3MoverPlaneResult {
public:
    B3MoverPlaneResult();
    explicit B3MoverPlaneResult(const b3BodyPlaneResult& result);

    long long GetShapeId() const;
    B3Vec3 GetNormal() const;
    float GetOffset() const;
    B3Vec3 GetPoint() const;

private:
    b3BodyPlaneResult m_result;
};

class B3MoverCollision {
public:
    B3MoverCollision();

    int GetCount() const;
    B3MoverPlaneResult GetResult(int index) const;

private:
    friend class B3Body;
    friend class B3World;
    friend class B3Collision;
    std::vector<b3BodyPlaneResult> m_results;
};

class B3PlaneSolverResult {
public:
    B3PlaneSolverResult();
    explicit B3PlaneSolverResult(const b3PlaneSolverResult& result);

    B3Vec3 GetDelta() const;
    int GetIterationCount() const;

private:
    b3PlaneSolverResult m_result;
};

class B3Collision {
public:
    static void ScaleBox(B3Vec3& halfWidths, B3Transform& transform, const B3Vec3& postScale, float minHalfWidth);
    static B3MassData ComputeSphereMass(const B3Sphere& sphere, float density);
    static B3MassData ComputeCapsuleMass(const B3Capsule& capsule, float density);
    static B3MassData ComputeHullMass(const B3Hull& hull, float density);
    static B3AABB ComputeSphereAABB(const B3Sphere& sphere, const B3Transform& transform);
    static B3AABB ComputeCapsuleAABB(const B3Capsule& capsule, const B3Transform& transform);
    static B3AABB ComputeHullAABB(const B3Hull& hull, const B3Transform& transform);
    static B3AABB ComputeMeshAABB(const B3Mesh& mesh, const B3Transform& transform, const B3Vec3& scale);
    static B3AABB ComputeHeightFieldAABB(const B3HeightField& heightField, const B3Transform& transform);
    static B3AABB ComputeCompoundAABB(const B3Compound& compound, const B3Transform& transform);
    static bool OverlapSphere(const B3Sphere& sphere, const B3Transform& transform, const B3ShapeProxy& proxy);
    static bool OverlapCapsule(const B3Capsule& capsule, const B3Transform& transform, const B3ShapeProxy& proxy);
    static bool OverlapHull(const B3Hull& hull, const B3Transform& transform, const B3ShapeProxy& proxy);
    static bool OverlapMesh(const B3Mesh& mesh, const B3Vec3& scale, const B3Transform& transform,
                            const B3ShapeProxy& proxy);
    static bool OverlapHeightField(const B3HeightField& heightField, const B3Transform& transform,
                                   const B3ShapeProxy& proxy);
    static bool OverlapCompound(const B3Compound& compound, const B3Transform& transform,
                                const B3ShapeProxy& proxy);
    static B3CastOutput RayCastSphere(const B3Sphere& sphere, const B3Vec3& origin,
                                      const B3Vec3& translation, float maxFraction);
    static B3CastOutput RayCastHollowSphere(const B3Sphere& sphere, const B3Vec3& origin,
                                            const B3Vec3& translation, float maxFraction);
    static B3CastOutput RayCastCapsule(const B3Capsule& capsule, const B3Vec3& origin,
                                       const B3Vec3& translation, float maxFraction);
    static B3CastOutput RayCastHull(const B3Hull& hull, const B3Vec3& origin,
                                    const B3Vec3& translation, float maxFraction);
    static B3CastOutput RayCastMesh(const B3Mesh& mesh, const B3Vec3& scale, const B3Vec3& origin,
                                    const B3Vec3& translation, float maxFraction);
    static B3CastOutput RayCastHeightField(const B3HeightField& heightField, const B3Vec3& origin,
                                           const B3Vec3& translation, float maxFraction);
    static B3CastOutput RayCastCompound(const B3Compound& compound, const B3Vec3& origin,
                                        const B3Vec3& translation, float maxFraction);
    static B3CastOutput ShapeCastSphere(const B3Sphere& sphere, const B3ShapeProxy& proxy,
                                        const B3Vec3& translation, float maxFraction, bool canEncroach);
    static B3CastOutput ShapeCastCapsule(const B3Capsule& capsule, const B3ShapeProxy& proxy,
                                         const B3Vec3& translation, float maxFraction, bool canEncroach);
    static B3CastOutput ShapeCastHull(const B3Hull& hull, const B3ShapeProxy& proxy,
                                      const B3Vec3& translation, float maxFraction, bool canEncroach);
    static B3CastOutput ShapeCastMesh(const B3Mesh& mesh, const B3Vec3& scale, const B3ShapeProxy& proxy,
                                      const B3Vec3& translation, float maxFraction, bool canEncroach);
    static B3CastOutput ShapeCastHeightField(const B3HeightField& heightField, const B3ShapeProxy& proxy,
                                             const B3Vec3& translation, float maxFraction, bool canEncroach);
    static B3CastOutput ShapeCastCompound(const B3Compound& compound, const B3ShapeProxy& proxy,
                                          const B3Vec3& translation, float maxFraction, bool canEncroach);
    static B3LocalManifold* CollideSpheres(int capacity, const B3Sphere& sphereA, const B3Sphere& sphereB,
                                           const B3Transform& transformBtoA);
    static B3LocalManifold* CollideCapsuleAndSphere(int capacity, const B3Capsule& capsuleA,
                                                    const B3Sphere& sphereB, const B3Transform& transformBtoA);
    static B3LocalManifold* CollideHullAndSphere(int capacity, const B3Hull& hullA, const B3Sphere& sphereB,
                                                 const B3Transform& transformBtoA);
    static B3LocalManifold* CollideCapsules(int capacity, const B3Capsule& capsuleA, const B3Capsule& capsuleB,
                                            const B3Transform& transformBtoA);
    static B3LocalManifold* CollideHullAndCapsule(int capacity, const B3Hull& hullA, const B3Capsule& capsuleB,
                                                  const B3Transform& transformBtoA);
    static B3LocalManifold* CollideHulls(int capacity, const B3Hull& hullA, const B3Hull& hullB,
                                         const B3Transform& transformBtoA);
    static B3LocalManifold* CollideTriangleAndSphere(int capacity, const B3Vec3Array& triangleA,
                                                     const B3Sphere& sphereB);
    static B3LocalManifold* CollideTriangleAndCapsule(int capacity, const B3Vec3Array& triangleA,
                                                      const B3Capsule& capsuleB);
    static B3LocalManifold* CollideTriangleAndHull(int capacity, const B3Vec3Array& triangleA, int triangleFlags,
                                                   const B3Hull& hullB, bool enableSpeculative);
    static B3DistanceOutput ShapeDistance(const B3ShapeProxy& proxyA, const B3ShapeProxy& proxyB,
                                           const B3Transform& transformBtoA, bool useRadii);
    static B3CastOutput ShapeCast(const B3ShapeProxy& proxyA, const B3ShapeProxy& proxyB,
                                  const B3Transform& transformBtoA, const B3Vec3& translationB,
                                  float maxFraction, bool canEncroach);
    static B3TOIOutput TimeOfImpact(const B3ShapeProxy& proxyA, const B3ShapeProxy& proxyB,
                                    const B3Sweep& sweepA, const B3Sweep& sweepB, float maxFraction);
    static B3PlaneSolverResult SolveMoverPlanes(const B3Vec3& targetDelta, const B3MoverCollision& collision);
    static B3Vec3 ClipVectorToMoverPlanes(const B3Vec3& vector, const B3MoverCollision& collision);
};

class B3MeshDef {
public:
    B3MeshDef(int vertexCapacity, int triangleCapacity);

    void AddVertex(const B3Vec3& vertex);
    void AddTriangle(int index1, int index2, int index3, int materialIndex);
    float GetWeldTolerance() const;
    void SetWeldTolerance(float tolerance);
    bool GetWeldVertices() const;
    void SetWeldVertices(bool enabled);
    bool GetUseMedianSplit() const;
    void SetUseMedianSplit(bool enabled);
    bool GetIdentifyEdges() const;
    void SetIdentifyEdges(bool enabled);
    int GetVertexCount() const;
    int GetTriangleCount() const;

    b3MeshData* CreateMeshData() const;

private:
    std::vector<b3Vec3> m_vertices;
    std::vector<int32_t> m_indices;
    std::vector<uint8_t> m_materialIndices;
    float m_weldTolerance;
    bool m_weldVertices;
    bool m_useMedianSplit;
    bool m_identifyEdges;
};

class B3MeshTriangle {
public:
    B3MeshTriangle();
    B3MeshTriangle(b3Vec3 a, b3Vec3 b, b3Vec3 c, int triangleIndex);

    B3Vec3 GetA() const;
    B3Vec3 GetB() const;
    B3Vec3 GetC() const;
    int GetTriangleIndex() const;

private:
    b3Vec3 m_a;
    b3Vec3 m_b;
    b3Vec3 m_c;
    int m_triangleIndex;
};

class B3MeshQueryResult {
public:
    B3MeshQueryResult();

    int GetSize() const;
    B3MeshTriangle GetValue(int index) const;
    void Add(b3Vec3 a, b3Vec3 b, b3Vec3 c, int triangleIndex);

private:
    std::vector<B3MeshTriangle> m_values;
};

class B3Mesh {
public:
    B3Mesh();
    explicit B3Mesh(b3MeshData* mesh);
    explicit B3Mesh(const b3Mesh& mesh);
    ~B3Mesh();

    static B3Mesh* CreateFromDef(const B3MeshDef& def);
    static B3Mesh* CreateFromObj(const char* objText, float scale, bool zUp, bool useMedianSplit,
                                 bool identifyEdges, bool weldVertices, float weldTolerance);
    static B3Mesh* CreateBox(const B3Vec3& center, const B3Vec3& extents, bool identifyEdges);
    static B3Mesh* CreateHollowBox(const B3Vec3& center, const B3Vec3& extents);
    static B3Mesh* CreatePlatform(const B3Vec3& center, float height, float topWidth, float bottomWidth);
    static B3Mesh* CreateGrid(int xCount, int zCount, float cellWidth, int materialCount, bool identifyEdges);
    static B3Mesh* CreateWave(int xCount, int zCount, float cellWidth, float amplitude, float rowFrequency,
                              float columnFrequency);
    static B3Mesh* CreateTorus(int radialResolution, int tubularResolution, float radius, float thickness);
    bool IsValid() const;
    void Destroy();
    int GetVertexCount() const;
    int GetTriangleCount() const;
    int GetMaterialCount() const;
    int GetTriangleMaterialIndex(int triangleIndex) const;
    void SetTriangleMaterialIndex(int triangleIndex, int materialIndex);
    B3Vec3 GetScale() const;
    int GetTreeHeight() const;
    B3MeshQueryResult* Query(const B3AABB& bounds) const;
    const b3MeshData* GetHandle() const;

private:
    b3MeshData* m_mesh;
    b3Vec3 m_scale;
    bool m_ownsMesh;
};

class B3HeightFieldDef {
public:
    B3HeightFieldDef(int countX, int countZ);

    int GetCountX() const;
    int GetCountZ() const;
    float GetHeight(int x, int z) const;
    void SetHeight(int x, int z, float height);
    int GetMaterialIndex(int x, int z) const;
    void SetMaterialIndex(int x, int z, int materialIndex);
    B3Vec3 GetScale() const;
    void SetScale(const B3Vec3& scale);
    float GetGlobalMinimumHeight() const;
    void SetGlobalMinimumHeight(float height);
    float GetGlobalMaximumHeight() const;
    void SetGlobalMaximumHeight(float height);
    bool GetClockwiseWinding() const;
    void SetClockwiseWinding(bool clockwise);
    void Dump(const char* fileName) const;
    b3HeightFieldDef GetValue() const;

private:
    int m_countX;
    int m_countZ;
    std::vector<float> m_heights;
    std::vector<uint8_t> m_materialIndices;
    b3Vec3 m_scale;
    float m_globalMinimumHeight;
    float m_globalMaximumHeight;
    bool m_clockwiseWinding;
};

class B3HeightField {
public:
    B3HeightField();
    explicit B3HeightField(b3HeightFieldData* heightField);
    B3HeightField(const b3HeightFieldData* heightField, bool ownsHeightField);
    ~B3HeightField();

    static B3HeightField* CreateFromDef(const B3HeightFieldDef& def);
    static B3HeightField* CreateGrid(int rowCount, int columnCount, const B3Vec3& scale, bool makeHoles);
    static B3HeightField* CreateWave(int rowCount, int columnCount, const B3Vec3& scale, float rowFrequency,
                                     float columnFrequency, bool makeHoles);
    static B3HeightField* Load(const char* fileName);
    bool IsValid() const;
    void Destroy();
    int GetRowCount() const;
    int GetColumnCount() const;
    B3Vec3 GetScale() const;
    const b3HeightFieldData* GetHandle() const;

private:
    b3HeightFieldData* m_heightField;
    bool m_ownsHeightField;
};

class B3SurfaceMaterialArray {
public:
    explicit B3SurfaceMaterialArray(int size);

    int GetSize() const;
    B3SurfaceMaterial GetValue(int index) const;
    void SetValue(int index, const B3SurfaceMaterial& material);
    const b3SurfaceMaterial* GetData() const;

private:
    std::vector<b3SurfaceMaterial> m_values;
};

class B3CompoundDef {
public:
    B3CompoundDef(int capsuleCapacity, int hullCapacity, int meshCapacity, int sphereCapacity);

    void AddCapsule(const B3Capsule& capsule, const B3SurfaceMaterial& material);
    void AddHull(const B3Hull& hull, const B3Transform& transform, const B3SurfaceMaterial& material);
    void AddMesh(const B3Mesh& mesh, const B3Transform& transform, const B3Vec3& scale,
                 const B3SurfaceMaterialArray& materials);
    void AddSphere(const B3Sphere& sphere, const B3SurfaceMaterial& material);
    int GetCapsuleCount() const;
    int GetHullCount() const;
    int GetMeshCount() const;
    int GetSphereCount() const;
    b3CompoundData* CreateCompoundData() const;

private:
    struct MeshEntry {
        const b3MeshData* meshData;
        b3Transform transform;
        b3Vec3 scale;
        std::vector<b3SurfaceMaterial> materials;
    };

    std::vector<b3CompoundCapsuleDef> m_capsules;
    std::vector<b3CompoundHullDef> m_hulls;
    std::vector<MeshEntry> m_meshes;
    std::vector<b3CompoundSphereDef> m_spheres;
};

class B3Compound {
public:
    B3Compound();
    explicit B3Compound(b3CompoundData* compound);
    ~B3Compound();

    static B3Compound* CreateFromDef(const B3CompoundDef& def);
    static B3Compound* CreateFromBytes(const B3ByteArray& bytes);
    bool IsValid() const;
    void Destroy();
    int GetCapsuleCount() const;
    int GetHullCount() const;
    int GetMeshCount() const;
    int GetSphereCount() const;
    B3SurfaceMaterial GetMaterial(int index) const;
    B3Capsule GetCapsule(int index) const;
    int GetCapsuleMaterialIndex(int index) const;
    B3Hull* GetHull(int index) const;
    B3Transform GetHullTransform(int index) const;
    int GetHullMaterialIndex(int index) const;
    B3Mesh* GetMesh(int index) const;
    B3Transform GetMeshTransform(int index) const;
    B3Vec3 GetMeshScale(int index) const;
    int GetMeshMaterialIndex(int meshIndex, int sourceMaterialIndex) const;
    B3Sphere GetSphere(int index) const;
    int GetSphereMaterialIndex(int index) const;
    B3ByteArray* ToBytes();
    const b3CompoundData* GetHandle() const;

private:
    b3CompoundData* m_compound;
    bool m_ownsCompound;
    std::vector<uint8_t> m_serializedStorage;
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
    void GetName(NativeString& name) const;
    void SetName(const char* name);
    B3Vec3 GetPosition() const;
    B3Quat GetRotation() const;
    B3Transform GetTransform() const;
    B3Vec3 GetWorldCenter() const;
    B3Vec3 GetLocalCenter() const;
    B3Vec3 GetLocalPoint(const B3Vec3& worldPoint) const;
    B3Vec3 GetWorldPoint(const B3Vec3& localPoint) const;
    B3Vec3 GetLocalVector(const B3Vec3& worldVector) const;
    B3Vec3 GetWorldVector(const B3Vec3& localVector) const;
    void SetTransform(const B3Vec3& position, const B3Quat& rotation);
    void SetTargetTransform(const B3Vec3& position, const B3Quat& rotation, float timeStep, bool wake);
    B3Vec3 GetLinearVelocity() const;
    void SetLinearVelocity(const B3Vec3& velocity);
    B3Vec3 GetAngularVelocity() const;
    void SetAngularVelocity(const B3Vec3& velocity);
    B3Vec3 GetLocalPointVelocity(const B3Vec3& localPoint) const;
    B3Vec3 GetWorldPointVelocity(const B3Vec3& worldPoint) const;
    void ApplyForce(const B3Vec3& force, const B3Vec3& point, bool wake);
    void ApplyForceToCenter(const B3Vec3& force, bool wake);
    void ApplyTorque(const B3Vec3& torque, bool wake);
    void ApplyLinearImpulse(const B3Vec3& impulse, const B3Vec3& point, bool wake);
    void ApplyLinearImpulseToCenter(const B3Vec3& impulse, bool wake);
    void ApplyAngularImpulse(const B3Vec3& impulse, bool wake);
    float GetMass() const;
    float GetInverseMass() const;
    void ApplyMassFromShapes();
    B3Vec3 GetLocalRotationalInertiaColumnX() const;
    B3Vec3 GetLocalRotationalInertiaColumnY() const;
    B3Vec3 GetLocalRotationalInertiaColumnZ() const;
    void SetMassData(float mass, const B3Vec3& center, const B3Vec3& inertiaColumnX,
                     const B3Vec3& inertiaColumnY, const B3Vec3& inertiaColumnZ);
    B3MassData GetMassData() const;
    void SetMassDataValue(const B3MassData& massData);
    B3Vec3 GetWorldInverseRotationalInertiaColumnX() const;
    B3Vec3 GetWorldInverseRotationalInertiaColumnY() const;
    B3Vec3 GetWorldInverseRotationalInertiaColumnZ() const;
    float GetLinearDamping() const;
    void SetLinearDamping(float damping);
    float GetAngularDamping() const;
    void SetAngularDamping(float damping);
    float GetGravityScale() const;
    void SetGravityScale(float scale);
    bool IsAwake() const;
    void SetAwake(bool awake);
    bool IsSleepEnabled() const;
    void EnableSleep(bool enabled);
    float GetSleepThreshold() const;
    void SetSleepThreshold(float threshold);
    B3RayResult CastRay(const B3Vec3& origin, const B3Vec3& translation, const B3QueryFilter& filter,
                        float maxFraction, const B3Transform& bodyTransform) const;
    bool IsEnabled() const;
    void Disable();
    void Enable();
    B3MotionLocks GetMotionLocks() const;
    void SetMotionLocks(const B3MotionLocks& locks);
    bool IsBullet() const;
    void SetBullet(bool bullet);
    bool IsFastRotationAllowed() const;
    void AllowFastRotation(bool allowed);
    bool IsContactRecyclingEnabled() const;
    void EnableContactRecycling(bool enabled);
    void EnableHitEvents(bool enabled);
    long long GetWorldId() const;
    long long GetUserData() const;
    void SetUserData(long long userData);
    int GetShapeCount() const;
    long long GetShapeId(int index) const;
    int GetJointCount() const;
    long long GetJointId(int index) const;
    int GetContactCapacity() const;
    B3ContactDataArray* GetContactData() const;
    B3AABB ComputeAABB() const;
    B3Vec3 GetClosestPoint(const B3Vec3& target) const;
    float GetClosestPointDistance(const B3Vec3& target) const;
    B3RayResult CastShape(const B3Vec3& origin, const B3ShapeProxy& proxy, const B3Vec3& translation,
                          const B3QueryFilter& filter, float maxFraction, bool canEncroach,
                          const B3Transform& bodyTransform) const;
    bool OverlapShape(const B3Vec3& origin, const B3ShapeProxy& proxy, const B3QueryFilter& filter,
                      const B3Transform& bodyTransform) const;
    B3MoverCollision* CollideMover(const B3Vec3& origin, const B3Capsule& mover, const B3QueryFilter& filter,
                                   const B3Transform& bodyTransform, int capacity) const;
    B3Shape* CreateSphereShape(const B3ShapeDef& def, const B3Sphere& sphere);
    B3Shape* CreateCapsuleShape(const B3ShapeDef& def, const B3Capsule& capsule);
    B3Shape* CreateHullShape(const B3ShapeDef& def, const B3Hull& hull);
    B3Shape* CreateTransformedHullShape(const B3ShapeDef& def, const B3Hull& hull, const B3Transform& transform,
                                        const B3Vec3& scale);
    B3Shape* CreateMeshShape(const B3ShapeDef& def, const B3Mesh& mesh, const B3Vec3& scale);
    B3Shape* CreateMeshShapeWithMaterials(const B3ShapeDef& def, const B3Mesh& mesh, const B3Vec3& scale,
                                          const B3SurfaceMaterialArray& materials);
    B3Shape* CreateHeightFieldShape(const B3ShapeDef& def, const B3HeightField& heightField);
    B3Shape* CreateBakedCompoundShape(B3ShapeDef& def, const B3Compound& compound);

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
    int GetType() const;
    long long GetBodyIdA() const;
    long long GetBodyIdB() const;
    long long GetWorldId() const;
    long long GetUserData() const;
    void SetUserData(long long userData);
    B3Transform GetLocalFrameA() const;
    void SetLocalFrameA(const B3Transform& localFrame);
    B3Transform GetLocalFrameB() const;
    void SetLocalFrameB(const B3Transform& localFrame);
    bool GetCollideConnected() const;
    void SetCollideConnected(bool collideConnected);
    void WakeBodies();
    B3Vec3 GetConstraintForce() const;
    B3Vec3 GetConstraintTorque() const;
    float GetLinearSeparation() const;
    float GetAngularSeparation() const;
    void SetConstraintTuning(float hertz, float dampingRatio);
    float GetConstraintHertz() const;
    float GetConstraintDampingRatio() const;
    void SetForceThreshold(float threshold);
    float GetForceThreshold() const;
    void SetTorqueThreshold(float threshold);
    float GetTorqueThreshold() const;

    void SetParallelSpringHertz(float hertz);
    float GetParallelSpringHertz() const;
    void SetParallelSpringDampingRatio(float dampingRatio);
    float GetParallelSpringDampingRatio() const;
    void SetParallelMaxTorque(float torque);
    float GetParallelMaxTorque() const;

    void SetDistanceLength(float length);
    float GetDistanceLength() const;
    void EnableDistanceSpring(bool enabled);
    bool IsDistanceSpringEnabled() const;
    void SetDistanceSpringForceRange(float lowerForce, float upperForce);
    float GetDistanceLowerSpringForce() const;
    float GetDistanceUpperSpringForce() const;
    void SetDistanceSpringHertz(float hertz);
    float GetDistanceSpringHertz() const;
    void SetDistanceSpringDampingRatio(float dampingRatio);
    float GetDistanceSpringDampingRatio() const;
    void EnableDistanceLimit(bool enabled);
    bool IsDistanceLimitEnabled() const;
    void SetDistanceLengthRange(float minLength, float maxLength);
    float GetDistanceMinLength() const;
    float GetDistanceMaxLength() const;
    float GetDistanceCurrentLength() const;
    void EnableDistanceMotor(bool enabled);
    bool IsDistanceMotorEnabled() const;
    void SetDistanceMotorSpeed(float speed);
    float GetDistanceMotorSpeed() const;
    void SetDistanceMaxMotorForce(float force);
    float GetDistanceMaxMotorForce() const;
    float GetDistanceMotorForce() const;

    void SetMotorLinearVelocity(const B3Vec3& velocity);
    B3Vec3 GetMotorLinearVelocity() const;
    void SetMotorAngularVelocity(const B3Vec3& velocity);
    B3Vec3 GetMotorAngularVelocity() const;
    void SetMotorMaxVelocityForce(float force);
    float GetMotorMaxVelocityForce() const;
    void SetMotorMaxVelocityTorque(float torque);
    float GetMotorMaxVelocityTorque() const;
    void SetMotorLinearHertz(float hertz);
    float GetMotorLinearHertz() const;
    void SetMotorLinearDampingRatio(float dampingRatio);
    float GetMotorLinearDampingRatio() const;
    void SetMotorAngularHertz(float hertz);
    float GetMotorAngularHertz() const;
    void SetMotorAngularDampingRatio(float dampingRatio);
    float GetMotorAngularDampingRatio() const;
    void SetMotorMaxSpringForce(float force);
    float GetMotorMaxSpringForce() const;
    void SetMotorMaxSpringTorque(float torque);
    float GetMotorMaxSpringTorque() const;

    void EnablePrismaticSpring(bool enabled);
    bool IsPrismaticSpringEnabled() const;
    void SetPrismaticSpringHertz(float hertz);
    float GetPrismaticSpringHertz() const;
    void SetPrismaticSpringDampingRatio(float dampingRatio);
    float GetPrismaticSpringDampingRatio() const;
    void SetPrismaticTargetTranslation(float translation);
    float GetPrismaticTargetTranslation() const;
    void EnablePrismaticLimit(bool enabled);
    bool IsPrismaticLimitEnabled() const;
    float GetPrismaticLowerLimit() const;
    float GetPrismaticUpperLimit() const;
    void SetPrismaticLimits(float lower, float upper);
    void EnablePrismaticMotor(bool enabled);
    bool IsPrismaticMotorEnabled() const;
    float GetPrismaticTranslation() const;
    void SetPrismaticMotorSpeed(float speed);
    float GetPrismaticMotorSpeed() const;
    void SetPrismaticMaxMotorForce(float force);
    float GetPrismaticMaxMotorForce() const;
    float GetPrismaticMotorForce() const;
    float GetPrismaticSpeed() const;

    void EnableRevoluteSpring(bool enabled);
    bool IsRevoluteSpringEnabled() const;
    void SetRevoluteTargetAngle(float radians);
    float GetRevoluteTargetAngle() const;
    float GetRevoluteAngle() const;
    void EnableRevoluteLimit(bool enabled);
    bool IsRevoluteLimitEnabled() const;
    float GetRevoluteLowerLimit() const;
    float GetRevoluteUpperLimit() const;
    void SetRevoluteLimits(float lowerRadians, float upperRadians);
    void EnableRevoluteMotor(bool enabled);
    bool IsRevoluteMotorEnabled() const;
    void SetRevoluteMotorSpeed(float speed);
    float GetRevoluteMotorSpeed() const;
    float GetRevoluteMotorTorque() const;
    void SetRevoluteMaxMotorTorque(float torque);
    float GetRevoluteMaxMotorTorque() const;
    void SetRevoluteSpringHertz(float hertz);
    float GetRevoluteSpringHertz() const;
    void SetRevoluteSpringDampingRatio(float dampingRatio);
    float GetRevoluteSpringDampingRatio() const;

    void EnableSphericalConeLimit(bool enabled);
    bool IsSphericalConeLimitEnabled() const;
    float GetSphericalConeLimit() const;
    void SetSphericalConeLimit(float radians);
    float GetSphericalConeAngle() const;
    void EnableSphericalTwistLimit(bool enabled);
    bool IsSphericalTwistLimitEnabled() const;
    float GetSphericalLowerTwistLimit() const;
    float GetSphericalUpperTwistLimit() const;
    void SetSphericalTwistLimits(float lowerRadians, float upperRadians);
    float GetSphericalTwistAngle() const;
    void EnableSphericalSpring(bool enabled);
    bool IsSphericalSpringEnabled() const;
    void SetSphericalMaxMotorTorque(float torque);
    float GetSphericalMaxMotorTorque() const;
    void SetSphericalSpringHertz(float hertz);
    float GetSphericalSpringHertz() const;
    void SetSphericalSpringDampingRatio(float dampingRatio);
    float GetSphericalSpringDampingRatio() const;
    void SetSphericalTargetRotation(const B3Quat& rotation);
    B3Quat GetSphericalTargetRotation() const;
    void EnableSphericalMotor(bool enabled);
    bool IsSphericalMotorEnabled() const;
    void SetSphericalMotorVelocity(const B3Vec3& velocity);
    B3Vec3 GetSphericalMotorVelocity() const;
    B3Vec3 GetSphericalMotorTorque() const;

    void SetWeldLinearHertz(float hertz);
    float GetWeldLinearHertz() const;
    void SetWeldLinearDampingRatio(float dampingRatio);
    float GetWeldLinearDampingRatio() const;
    void SetWeldAngularHertz(float hertz);
    float GetWeldAngularHertz() const;
    void SetWeldAngularDampingRatio(float dampingRatio);
    float GetWeldAngularDampingRatio() const;

    void EnableWheelSuspension(bool enabled);
    bool IsWheelSuspensionEnabled() const;
    void SetWheelSuspensionHertz(float hertz);
    float GetWheelSuspensionHertz() const;
    void SetWheelSuspensionDampingRatio(float dampingRatio);
    float GetWheelSuspensionDampingRatio() const;
    void EnableWheelSuspensionLimit(bool enabled);
    bool IsWheelSuspensionLimitEnabled() const;
    float GetWheelLowerSuspensionLimit() const;
    float GetWheelUpperSuspensionLimit() const;
    void SetWheelSuspensionLimits(float lower, float upper);
    void EnableWheelSpinMotor(bool enabled);
    bool IsWheelSpinMotorEnabled() const;
    void SetWheelTargetSteeringAngle(float radians);
    float GetWheelTargetSteeringAngle() const;
    void SetWheelSpinMotorSpeed(float speed);
    float GetWheelSpinMotorSpeed() const;
    void SetWheelMaxSpinTorque(float torque);
    float GetWheelMaxSpinTorque() const;
    float GetWheelSpinSpeed() const;
    float GetWheelSpinTorque() const;
    void EnableWheelSteering(bool enabled);
    bool IsWheelSteeringEnabled() const;
    void SetWheelSteeringHertz(float hertz);
    float GetWheelSteeringHertz() const;
    void SetWheelSteeringDampingRatio(float dampingRatio);
    float GetWheelSteeringDampingRatio() const;
    void SetWheelMaxSteeringTorque(float torque);
    float GetWheelMaxSteeringTorque() const;
    void EnableWheelSteeringLimit(bool enabled);
    bool IsWheelSteeringLimitEnabled() const;
    float GetWheelLowerSteeringLimit() const;
    float GetWheelUpperSteeringLimit() const;
    void SetWheelSteeringLimits(float lowerRadians, float upperRadians);
    float GetWheelSteeringAngle() const;
    float GetWheelSteeringTorque() const;

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
    long long GetWorldId() const;
    long long GetUserData() const;
    void SetUserData(long long userData);
    bool IsSensor() const;
    void GetName(NativeString& name) const;
    void SetName(const char* name);
    float GetDensity() const;
    void SetDensity(float density, bool updateBodyMass);
    float GetFriction() const;
    void SetFriction(float friction);
    float GetRestitution() const;
    void SetRestitution(float restitution);
    B3SurfaceMaterial GetSurfaceMaterial() const;
    void SetSurfaceMaterial(const B3SurfaceMaterial& material);
    int GetMeshMaterialCount() const;
    B3SurfaceMaterial GetMeshSurfaceMaterial(int index) const;
    void SetMeshMaterial(const B3SurfaceMaterial& material, int index);
    B3Filter GetFilter() const;
    void SetFilter(const B3Filter& filter, bool invokeContacts);
    void EnableSensorEvents(bool enabled);
    bool AreSensorEventsEnabled() const;
    void EnableContactEvents(bool enabled);
    bool AreContactEventsEnabled() const;
    void EnablePreSolveEvents(bool enabled);
    bool ArePreSolveEventsEnabled() const;
    void EnableHitEvents(bool enabled);
    bool AreHitEventsEnabled() const;
    B3RayResult RayCast(const B3Vec3& origin, const B3Vec3& translation) const;
    B3Sphere GetSphere() const;
    void SetSphere(const B3Sphere& sphere);
    B3Capsule GetCapsule() const;
    void SetCapsule(const B3Capsule& capsule);
    B3Hull* GetHull() const;
    void SetHull(const B3Hull& hull);
    B3Mesh* GetMesh() const;
    B3HeightField* GetHeightField() const;
    void SetMesh(const B3Mesh& mesh, const B3Vec3& scale);
    int GetContactCapacity() const;
    B3ContactDataArray* GetContactData() const;
    int GetSensorCapacity() const;
    long long GetSensorShapeId(int index) const;
    B3AABB GetAABB() const;
    B3MassData ComputeMassData() const;
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
    void SetDrawIslands(bool enabled);
    bool GetDrawIslands() const;
    int GetDrawnCompoundChildCount() const;
    int GetTotalCompoundChildCount() const;

    virtual void DrawShape(B3DebugShape* shape, const B3Transform& transform, int color);
    virtual void DrawSegment(const B3Vec3& p1, const B3Vec3& p2, int color);
    virtual void DrawTransform(const B3Transform& transform);
    virtual void DrawPoint(const B3Vec3& p, float size, int color);
    virtual void DrawSphere(const B3Vec3& p, float radius, int color, float alpha);
    virtual void DrawCapsule(const B3Vec3& p1, const B3Vec3& p2, float radius, int color, float alpha);
    virtual void DrawBounds(const B3AABB& aabb, int color);
    virtual void DrawBox(const B3Vec3& extents, const B3Transform& transform, int color);

private:
    friend void drawShapeCallback(void* userShape, b3WorldTransform transform, b3HexColor color, void* context);
    friend class B3RecPlayer;

    void DrawDebugShape(B3DebugShape* shape, b3WorldTransform transform, int color);
    static bool DrawCompoundChild(const b3CompoundData* compound, int childIndex, void* context);

    b3DebugDraw m_draw;
    int m_drawnCompoundChildCount;
    int m_totalCompoundChildCount;
};

class B3CustomFilterEm {
public:
    B3CustomFilterEm();
    virtual ~B3CustomFilterEm();

    virtual bool Filter(long long shapeIdA, long long shapeIdB);
};

class B3PreSolveCallbackEm {
public:
    B3PreSolveCallbackEm();
    virtual ~B3PreSolveCallbackEm();
    virtual bool PreSolve(long long shapeIdA, long long shapeIdB, const B3Vec3& point, const B3Vec3& normal);
};

class B3FrictionCallbackEm {
public:
    B3FrictionCallbackEm();
    virtual ~B3FrictionCallbackEm();
    virtual float MixFriction(float frictionA, long long userMaterialIdA,
                              float frictionB, long long userMaterialIdB);
};

class B3RestitutionCallbackEm {
public:
    B3RestitutionCallbackEm();
    virtual ~B3RestitutionCallbackEm();
    virtual float MixRestitution(float restitutionA, long long userMaterialIdA,
                                 float restitutionB, long long userMaterialIdB);
};

class B3CastResult {
public:
    B3CastResult();
    B3CastResult(long long shapeId, const B3Vec3& point, const B3Vec3& normal, float fraction,
                 long long userMaterialId, int triangleIndex, int childIndex);
    long long GetShapeId() const;
    B3Vec3 GetPoint() const;
    B3Vec3 GetNormal() const;
    float GetFraction() const;
    long long GetUserMaterialId() const;
    int GetTriangleIndex() const;
    int GetChildIndex() const;

private:
    long long m_shapeId;
    B3Vec3 m_point;
    B3Vec3 m_normal;
    float m_fraction;
    long long m_userMaterialId;
    int m_triangleIndex;
    int m_childIndex;
};

class B3CastResultCallbackEm {
public:
    B3CastResultCallbackEm();
    virtual ~B3CastResultCallbackEm();
    virtual float ReportHit(const B3CastResult& result);
};

class B3AllocatorEm {
public:
    B3AllocatorEm();
    virtual ~B3AllocatorEm();
    virtual long long Allocate(int size, int alignment);
    virtual void Free(long long address);
};

class B3AssertCallbackEm {
public:
    B3AssertCallbackEm();
    virtual ~B3AssertCallbackEm();
    virtual bool Assert(const char* condition, const char* fileName, int lineNumber);
};

class B3LogCallbackEm {
public:
    B3LogCallbackEm();
    virtual ~B3LogCallbackEm();
    virtual void Log(const char* message);
};

class B3World {
public:
    B3World();
    explicit B3World(const B3WorldDef& def);
    B3World(b3WorldId worldId, bool ownsWorld);
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
    float GetRestitutionThreshold() const;
    void SetRestitutionThreshold(float threshold);
    float GetHitEventThreshold() const;
    void SetHitEventThreshold(float threshold);
    float GetMaximumLinearSpeed() const;
    void SetMaximumLinearSpeed(float speed);
    void SetContactTuning(float hertz, float dampingRatio, float contactSpeed);
    int GetWorkerCount() const;
    void SetWorkerCount(long workerCount);
    int GetAwakeBodyCount() const;
    B3Profile GetProfile() const;
    B3Counters GetCounters() const;
    B3Capacity GetMaxCapacity() const;
    long long GetUserData() const;
    void SetUserData(long long userData);
    void Explode(const B3ExplosionDef& def);
    void DumpMemoryStats();
    void DumpShapeBounds(int bodyType);
    void RebuildStaticTree();
    void EnableSpeculative(bool enabled);
    void StartRecording(B3Recording* recording);
    void StopRecording();
    void SetCustomFilterCallback(B3CustomFilterEm* callback);
    void SetPreSolveCallback(B3PreSolveCallbackEm* callback);
    void SetFrictionCallback(B3FrictionCallbackEm* callback);
    void SetRestitutionCallback(B3RestitutionCallbackEm* callback);
    void ClearDebugOverlay();
    void AddDebugSegment(const B3Vec3& p1, const B3Vec3& p2, long color);
    void AddDebugPoint(const B3Vec3& point, float size, long color);
    void AddDebugSphere(const B3Vec3& center, float radius, long color, float alpha);
    void AddDebugCapsule(const B3Vec3& p1, const B3Vec3& p2, float radius, long color, float alpha);
    void AddDebugBounds(const B3AABB& bounds, long color);
    void AddDebugBox(const B3Vec3& extents, const B3Transform& transform, long color);
    void AddDebugHull(const B3Hull& hull, const B3Transform& transform, const B3Vec3& scale, long color);
    void AddDebugTriangle(const B3Vec3& p1, const B3Vec3& p2, const B3Vec3& p3, long color);
    void DrawDebugOverlay(B3DebugDrawEm* draw) const;
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
    B3JointEvents* GetJointEvents() const;
    B3SensorEvents* GetSensorEvents() const;
    B3ContactEvents* GetContactEvents() const;
    B3RayResult CastRayClosest(const B3Vec3& origin, const B3Vec3& translation, const B3QueryFilter& filter) const;
    B3TreeStats CastRay(const B3Vec3& origin, const B3Vec3& translation, const B3QueryFilter& filter,
                        B3CastResultCallbackEm* callback) const;
    int CountOverlapsAABB(const B3AABB& bounds, const B3QueryFilter& filter) const;
    B3RayResult CastSphereClosest(const B3Vec3& origin, float radius, const B3Vec3& translation,
                                  const B3QueryFilter& filter) const;
    B3RayResult CastShapeClosest(const B3Vec3& origin, const B3ShapeProxy& proxy, const B3Vec3& translation,
                                 const B3QueryFilter& filter, bool initialOverlap) const;
    bool OverlapShape(const B3Vec3& origin, const B3ShapeProxy& proxy, const B3QueryFilter& filter) const;
    B3MoverCollision* CollideMover(const B3Vec3& origin, const B3Capsule& mover, const B3QueryFilter& filter,
                                   int capacity) const;
    float CastMover(const B3Vec3& origin, const B3Capsule& mover, const B3Vec3& translation,
                    const B3QueryFilter& filter) const;
    float CastSphereClosestFraction(const B3Vec3& origin, float radius, const B3Vec3& translation,
                                    const B3QueryFilter& filter) const;
    b3WorldId GetHandle() const;

private:
    struct DebugSegment {
        b3Vec3 p1;
        b3Vec3 p2;
        uint32_t color;
    };
    struct DebugPoint {
        b3Vec3 point;
        float size;
        uint32_t color;
    };
    struct DebugSphere {
        b3Vec3 center;
        float radius;
        uint32_t color;
        float alpha;
    };
    struct DebugCapsule {
        b3Vec3 p1;
        b3Vec3 p2;
        float radius;
        uint32_t color;
        float alpha;
    };
    struct DebugBounds {
        b3AABB bounds;
        uint32_t color;
    };
    struct DebugBox {
        b3Vec3 extents;
        b3Transform transform;
        uint32_t color;
    };
    struct DebugHull {
        std::unique_ptr<B3DebugShape> shape;
        b3Transform transform;
        uint32_t color;
    };

    b3WorldId m_worldId;
    bool m_destroyed;
    bool m_ownsWorld;
    std::vector<DebugSegment> m_debugSegments;
    std::vector<DebugPoint> m_debugPoints;
    std::vector<DebugSphere> m_debugSpheres;
    std::vector<DebugCapsule> m_debugCapsules;
    std::vector<DebugBounds> m_debugBounds;
    std::vector<DebugBox> m_debugBoxes;
    std::vector<DebugHull> m_debugHulls;
};

class B3 {
public:
    static bool IsDoublePrecision();
    static float Atan2(float y, float x);
    static B3CosSin ComputeCosSin(float radians);
    static B3Quat MakeQuatFromMatrix(const B3Matrix3& matrix);
    static B3Matrix3 Steiner(float mass, const B3Vec3& origin);
    static B3Vec3 PointToSegmentDistance(const B3Vec3& a, const B3Vec3& b, const B3Vec3& q);
    static B3SegmentDistanceResult LineDistance(const B3Vec3& p1, const B3Vec3& d1,
                                                const B3Vec3& p2, const B3Vec3& d2);
    static B3SegmentDistanceResult SegmentDistance(const B3Vec3& p1, const B3Vec3& q1,
                                                   const B3Vec3& p2, const B3Vec3& q2);
    static bool IsValidFloat(float value);
    static bool IsValidVec3(const B3Vec3& value);
    static bool IsValidQuat(const B3Quat& value);
    static bool IsValidTransform(const B3Transform& value);
    static bool IsValidMatrix3(const B3Matrix3& value);
    static bool IsValidPlane(const B3Plane& value);
    static bool IsValidPosition(const B3Vec3& value);
    static bool IsValidWorldTransform(const B3Transform& value);
    static bool IsValidRay(const B3RayCastInput& value);
    static bool IsValidAABB(const B3AABB& value);
    static bool IsBoundedAABB(const B3AABB& value);
    static bool IsSaneAABB(const B3AABB& value);
    static int GetGraphColor(int index);
    static int GetVersionMajor();
    static int GetVersionMinor();
    static int GetVersionRevision();
    static float GetLengthUnitsPerMeter();
    static void SetLengthUnitsPerMeter(float lengthUnits);
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
    static float GetStallThreshold();
    static void SetStallThreshold(float seconds);
    static int GetByteCount();
    static long long GetTicks();
    static float GetMilliseconds(long long ticks);
    static long long Hash(long long hash, const B3ByteArray& data);
    static int InternalAssert(const char* condition, const char* fileName, int lineNumber);
    static void SetAllocatorCallback(B3AllocatorEm* callback);
    static void SetAssertCallback(B3AssertCallbackEm* callback);
    static void SetLogCallback(B3LogCallbackEm* callback);
    static long long AllocateMemory(int size, int alignment);
    static void FreeMemory(long long address);
    static void Yield();
    static void Sleep(int milliseconds);
    static long long DefaultMaskBits();
};

} // namespace JBox3D

// jParser compiles upstream Box3D sources as C17. Keep the wrapper implementation
// header-included so it is compiled by the generated C++ glue stage instead.
#include "jBox3D.cpp"
