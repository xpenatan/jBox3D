// Included by jBox3D.h. Do not compile this file directly.
namespace JBox3D {

static b3BodyId loadBodyId(long long value) {
    return b3LoadBodyId(static_cast<uint64_t>(value));
}

static b3ShapeId loadShapeId(long long value) {
    return b3LoadShapeId(static_cast<uint64_t>(value));
}

static b3JointId loadJointId(long long value) {
    return b3LoadJointId(static_cast<uint64_t>(value));
}

static void* createDebugShape(const b3DebugShape* debugShape, void*) {
    return debugShape != nullptr ? new B3DebugShape(*debugShape) : nullptr;
}

static void destroyDebugShape(void* userShape, void*) {
    delete static_cast<B3DebugShape*>(userShape);
}

static B3Vec3 getIndexedVec3(const std::vector<B3Vec3>& values, int index) {
    if(index < 0 || index >= static_cast<int>(values.size())) {
        return B3Vec3();
    }
    return values[static_cast<size_t>(index)];
}

static B3Transform toTransform(b3WorldTransform transform) {
    return B3Transform(b3ToRelativeTransform(transform, b3Pos_zero));
}

void drawShapeCallback(void* userShape, b3WorldTransform transform, b3HexColor color, void* context) {
    B3DebugDrawEm* draw = static_cast<B3DebugDrawEm*>(context);
    if(draw == nullptr || userShape == nullptr) {
        return;
    }
    draw->DrawDebugShape(static_cast<B3DebugShape*>(userShape), transform, static_cast<int>(color));
}

static void drawSegmentCallback(b3Pos p1, b3Pos p2, b3HexColor color, void* context) {
    B3DebugDrawEm* draw = static_cast<B3DebugDrawEm*>(context);
    if(draw != nullptr) {
        draw->DrawSegment(B3Vec3(b3ToVec3(p1)), B3Vec3(b3ToVec3(p2)), static_cast<int>(color));
    }
}

static void drawTransformCallback(b3WorldTransform transform, void* context) {
    B3DebugDrawEm* draw = static_cast<B3DebugDrawEm*>(context);
    if(draw != nullptr) {
        draw->DrawTransform(toTransform(transform));
    }
}

static void drawPointCallback(b3Pos p, float size, b3HexColor color, void* context) {
    B3DebugDrawEm* draw = static_cast<B3DebugDrawEm*>(context);
    if(draw != nullptr) {
        draw->DrawPoint(B3Vec3(b3ToVec3(p)), size, static_cast<int>(color));
    }
}

static void drawSphereCallback(b3Pos p, float radius, b3HexColor color, float alpha, void* context) {
    B3DebugDrawEm* draw = static_cast<B3DebugDrawEm*>(context);
    if(draw != nullptr) {
        draw->DrawSphere(B3Vec3(b3ToVec3(p)), radius, static_cast<int>(color), alpha);
    }
}

static void drawCapsuleCallback(b3Pos p1, b3Pos p2, float radius, b3HexColor color, float alpha, void* context) {
    B3DebugDrawEm* draw = static_cast<B3DebugDrawEm*>(context);
    if(draw != nullptr) {
        draw->DrawCapsule(B3Vec3(b3ToVec3(p1)), B3Vec3(b3ToVec3(p2)), radius, static_cast<int>(color), alpha);
    }
}

static void drawBoundsCallback(b3AABB aabb, b3HexColor color, void* context) {
    B3DebugDrawEm* draw = static_cast<B3DebugDrawEm*>(context);
    if(draw != nullptr) {
        draw->DrawBounds(B3AABB(aabb), static_cast<int>(color));
    }
}

static void drawBoxCallback(b3Vec3 extents, b3WorldTransform transform, b3HexColor color, void* context) {
    B3DebugDrawEm* draw = static_cast<B3DebugDrawEm*>(context);
    if(draw != nullptr) {
        draw->DrawBox(B3Vec3(extents), toTransform(transform), static_cast<int>(color));
    }
}

static bool customFilterCallback(b3ShapeId shapeIdA, b3ShapeId shapeIdB, void* context) {
    B3CustomFilterEm* callback = static_cast<B3CustomFilterEm*>(context);
    return callback == nullptr || callback->Filter(static_cast<long long>(b3StoreShapeId(shapeIdA)),
                                                   static_cast<long long>(b3StoreShapeId(shapeIdB)));
}

B3Vec3::B3Vec3() : value(b3Vec3_zero) {
}

B3Vec3::B3Vec3(float x, float y, float z) : value{x, y, z} {
}

B3Vec3::B3Vec3(b3Vec3 value) : value(value) {
}

float B3Vec3::GetX() const {
    return value.x;
}

float B3Vec3::GetY() const {
    return value.y;
}

float B3Vec3::GetZ() const {
    return value.z;
}

void B3Vec3::SetX(float x) {
    value.x = x;
}

void B3Vec3::SetY(float y) {
    value.y = y;
}

void B3Vec3::SetZ(float z) {
    value.z = z;
}

void B3Vec3::Set(float x, float y, float z) {
    value = {x, y, z};
}

B3Quat::B3Quat() : value(b3Quat_identity) {
}

B3Quat::B3Quat(float x, float y, float z, float w) : value{{x, y, z}, w} {
}

B3Quat::B3Quat(b3Quat value) : value(value) {
}

B3Vec3 B3Quat::GetV() const {
    return B3Vec3(value.v);
}

void B3Quat::SetV(const B3Vec3& value) {
    this->value.v = value.value;
}

float B3Quat::GetS() const {
    return value.s;
}

void B3Quat::SetS(float s) {
    value.s = s;
}

void B3Quat::Set(float x, float y, float z, float w) {
    value = {{x, y, z}, w};
}

void B3Quat::Normalize() {
    value = b3NormalizeQuat(value);
}

B3Vec3 B3Quat::RotateVector(const B3Vec3& vector) const {
    return B3Vec3(b3RotateVector(value, vector.value));
}

B3Quat* B3Quat::ComputeBetweenUnitVectors(const B3Vec3& from, const B3Vec3& to) {
    return new B3Quat(b3ComputeQuatBetweenUnitVectors(from.value, to.value));
}

B3Quat* B3Quat::Mul(const B3Quat& a, const B3Quat& b) {
    return new B3Quat(b3MulQuat(a.value, b.value));
}

B3Quat* B3Quat::InvMul(const B3Quat& a, const B3Quat& b) {
    return new B3Quat(b3InvMulQuat(a.value, b.value));
}

B3Transform::B3Transform() : value(b3Transform_identity) {
}

B3Transform::B3Transform(const B3Vec3& position, const B3Quat& rotation) : value{position.value, rotation.value} {
}

B3Transform::B3Transform(b3Transform value) : value(value) {
}

B3Vec3 B3Transform::GetP() const {
    return B3Vec3(value.p);
}

void B3Transform::SetP(const B3Vec3& position) {
    value.p = position.value;
}

B3Quat B3Transform::GetQ() const {
    return B3Quat(value.q);
}

void B3Transform::SetQ(const B3Quat& rotation) {
    value.q = rotation.value;
}

B3Vec3 B3Transform::TransformPoint(const B3Vec3& point) const {
    return B3Vec3(b3TransformPoint(value, point.value));
}

B3Transform* B3Transform::InvMul(const B3Transform& a, const B3Transform& b) {
    return new B3Transform(b3InvMulTransforms(a.value, b.value));
}

B3AABB::B3AABB() : value{b3Vec3_zero, b3Vec3_zero} {
}

B3AABB::B3AABB(const B3Vec3& lowerBound, const B3Vec3& upperBound) : value{lowerBound.value, upperBound.value} {
}

B3AABB::B3AABB(b3AABB value) : value(value) {
}

B3Vec3 B3AABB::GetLowerBound() const {
    return B3Vec3(value.lowerBound);
}

void B3AABB::SetLowerBound(const B3Vec3& lowerBound) {
    value.lowerBound = lowerBound.value;
}

B3Vec3 B3AABB::GetUpperBound() const {
    return B3Vec3(value.upperBound);
}

void B3AABB::SetUpperBound(const B3Vec3& upperBound) {
    value.upperBound = upperBound.value;
}

B3Sphere::B3Sphere() : value{b3Vec3_zero, 0.0f} {
}

B3Sphere::B3Sphere(const B3Vec3& center, float radius) : value{center.value, radius} {
}

B3Sphere::B3Sphere(b3Sphere value) : value(value) {
}

B3Vec3 B3Sphere::GetCenter() const {
    return B3Vec3(value.center);
}

void B3Sphere::SetCenter(const B3Vec3& center) {
    value.center = center.value;
}

float B3Sphere::GetRadius() const {
    return value.radius;
}

void B3Sphere::SetRadius(float radius) {
    value.radius = radius;
}

B3Capsule::B3Capsule() : value{b3Vec3_zero, b3Vec3_zero, 0.0f} {
}

B3Capsule::B3Capsule(const B3Vec3& center1, const B3Vec3& center2, float radius) : value{center1.value, center2.value, radius} {
}

B3Capsule::B3Capsule(b3Capsule value) : value(value) {
}

B3Vec3 B3Capsule::GetCenter1() const {
    return B3Vec3(value.center1);
}

void B3Capsule::SetCenter1(const B3Vec3& center) {
    value.center1 = center.value;
}

B3Vec3 B3Capsule::GetCenter2() const {
    return B3Vec3(value.center2);
}

void B3Capsule::SetCenter2(const B3Vec3& center) {
    value.center2 = center.value;
}

float B3Capsule::GetRadius() const {
    return value.radius;
}

void B3Capsule::SetRadius(float radius) {
    value.radius = radius;
}

B3MotionLocks::B3MotionLocks() : value{} {
}

B3MotionLocks::B3MotionLocks(b3MotionLocks value) : value(value) {
}

bool B3MotionLocks::GetLinearX() const {
    return value.linearX;
}

void B3MotionLocks::SetLinearX(bool locked) {
    value.linearX = locked;
}

bool B3MotionLocks::GetLinearY() const {
    return value.linearY;
}

void B3MotionLocks::SetLinearY(bool locked) {
    value.linearY = locked;
}

bool B3MotionLocks::GetLinearZ() const {
    return value.linearZ;
}

void B3MotionLocks::SetLinearZ(bool locked) {
    value.linearZ = locked;
}

bool B3MotionLocks::GetAngularX() const {
    return value.angularX;
}

void B3MotionLocks::SetAngularX(bool locked) {
    value.angularX = locked;
}

bool B3MotionLocks::GetAngularY() const {
    return value.angularY;
}

void B3MotionLocks::SetAngularY(bool locked) {
    value.angularY = locked;
}

bool B3MotionLocks::GetAngularZ() const {
    return value.angularZ;
}

void B3MotionLocks::SetAngularZ(bool locked) {
    value.angularZ = locked;
}

B3MassData::B3MassData() : value{} {
}

B3MassData::B3MassData(b3MassData value) : value(value) {
}

float B3MassData::GetMass() const {
    return value.mass;
}

void B3MassData::SetMass(float mass) {
    value.mass = mass;
}

B3Vec3 B3MassData::GetCenter() const {
    return B3Vec3(value.center);
}

void B3MassData::SetCenter(const B3Vec3& center) {
    value.center = center.value;
}

B3Vec3 B3MassData::GetInertiaColumnX() const {
    return B3Vec3(value.inertia.cx);
}

void B3MassData::SetInertiaColumnX(const B3Vec3& column) {
    value.inertia.cx = column.value;
}

B3Vec3 B3MassData::GetInertiaColumnY() const {
    return B3Vec3(value.inertia.cy);
}

void B3MassData::SetInertiaColumnY(const B3Vec3& column) {
    value.inertia.cy = column.value;
}

B3Vec3 B3MassData::GetInertiaColumnZ() const {
    return B3Vec3(value.inertia.cz);
}

void B3MassData::SetInertiaColumnZ(const B3Vec3& column) {
    value.inertia.cz = column.value;
}

#define DEFINE_B3_JOINT_BASE_ACCESSORS(Type) \
long long Type::GetBodyIdA() const { \
    return static_cast<long long>(b3StoreBodyId(value.base.bodyIdA)); \
} \
void Type::SetBodyIdA(long long bodyId) { \
    value.base.bodyIdA = loadBodyId(bodyId); \
} \
long long Type::GetBodyIdB() const { \
    return static_cast<long long>(b3StoreBodyId(value.base.bodyIdB)); \
} \
void Type::SetBodyIdB(long long bodyId) { \
    value.base.bodyIdB = loadBodyId(bodyId); \
} \
B3Transform Type::GetLocalFrameA() const { \
    return B3Transform(value.base.localFrameA); \
} \
void Type::SetLocalFrameA(const B3Transform& transform) { \
    value.base.localFrameA = transform.value; \
} \
B3Transform Type::GetLocalFrameB() const { \
    return B3Transform(value.base.localFrameB); \
} \
void Type::SetLocalFrameB(const B3Transform& transform) { \
    value.base.localFrameB = transform.value; \
} \
void Type::SetLocalPositionA(const B3Vec3& position) { \
    value.base.localFrameA.p = position.value; \
} \
void Type::SetLocalPositionB(const B3Vec3& position) { \
    value.base.localFrameB.p = position.value; \
} \
float Type::GetDrawScale() const { \
    return value.base.drawScale; \
} \
void Type::SetDrawScale(float scale) { \
    value.base.drawScale = scale; \
} \
bool Type::GetCollideConnected() const { \
    return value.base.collideConnected; \
} \
void Type::SetCollideConnected(bool collide) { \
    value.base.collideConnected = collide; \
}

B3DistanceJointDef::B3DistanceJointDef() : value(b3DefaultDistanceJointDef()) {
}

DEFINE_B3_JOINT_BASE_ACCESSORS(B3DistanceJointDef)

void B3DistanceJointDef::SetForceThreshold(float force) { value.base.forceThreshold = force; }
void B3DistanceJointDef::SetTorqueThreshold(float torque) { value.base.torqueThreshold = torque; }

float B3DistanceJointDef::GetConstraintHertz() const {
    return value.base.constraintHertz;
}

void B3DistanceJointDef::SetConstraintHertz(float hertz) {
    value.base.constraintHertz = hertz;
}

float B3DistanceJointDef::GetConstraintDampingRatio() const {
    return value.base.constraintDampingRatio;
}

void B3DistanceJointDef::SetConstraintDampingRatio(float ratio) {
    value.base.constraintDampingRatio = ratio;
}

float B3DistanceJointDef::GetLength() const {
    return value.length;
}

void B3DistanceJointDef::SetLength(float length) {
    value.length = length;
}

bool B3DistanceJointDef::GetEnableSpring() const {
    return value.enableSpring;
}

void B3DistanceJointDef::SetEnableSpring(bool enabled) {
    value.enableSpring = enabled;
}

float B3DistanceJointDef::GetLowerSpringForce() const {
    return value.lowerSpringForce;
}

void B3DistanceJointDef::SetLowerSpringForce(float force) {
    value.lowerSpringForce = force;
}

float B3DistanceJointDef::GetUpperSpringForce() const {
    return value.upperSpringForce;
}

void B3DistanceJointDef::SetUpperSpringForce(float force) {
    value.upperSpringForce = force;
}

float B3DistanceJointDef::GetHertz() const {
    return value.hertz;
}

void B3DistanceJointDef::SetHertz(float hertz) {
    value.hertz = hertz;
}

float B3DistanceJointDef::GetDampingRatio() const {
    return value.dampingRatio;
}

void B3DistanceJointDef::SetDampingRatio(float ratio) {
    value.dampingRatio = ratio;
}

bool B3DistanceJointDef::GetEnableLimit() const {
    return value.enableLimit;
}

void B3DistanceJointDef::SetEnableLimit(bool enabled) {
    value.enableLimit = enabled;
}

float B3DistanceJointDef::GetMinLength() const {
    return value.minLength;
}

void B3DistanceJointDef::SetMinLength(float length) {
    value.minLength = length;
}

float B3DistanceJointDef::GetMaxLength() const {
    return value.maxLength;
}

void B3DistanceJointDef::SetMaxLength(float length) {
    value.maxLength = length;
}

bool B3DistanceJointDef::GetEnableMotor() const {
    return value.enableMotor;
}

void B3DistanceJointDef::SetEnableMotor(bool enabled) {
    value.enableMotor = enabled;
}

float B3DistanceJointDef::GetMaxMotorForce() const {
    return value.maxMotorForce;
}

void B3DistanceJointDef::SetMaxMotorForce(float force) {
    value.maxMotorForce = force;
}

float B3DistanceJointDef::GetMotorSpeed() const {
    return value.motorSpeed;
}

void B3DistanceJointDef::SetMotorSpeed(float speed) {
    value.motorSpeed = speed;
}

B3MotorJointDef::B3MotorJointDef() : value(b3DefaultMotorJointDef()) {
}

DEFINE_B3_JOINT_BASE_ACCESSORS(B3MotorJointDef)

B3Vec3 B3MotorJointDef::GetLinearVelocity() const {
    return B3Vec3(value.linearVelocity);
}

void B3MotorJointDef::SetLinearVelocity(const B3Vec3& velocity) {
    value.linearVelocity = velocity.value;
}

float B3MotorJointDef::GetMaxVelocityForce() const {
    return value.maxVelocityForce;
}

void B3MotorJointDef::SetMaxVelocityForce(float force) {
    value.maxVelocityForce = force;
}

B3Vec3 B3MotorJointDef::GetAngularVelocity() const {
    return B3Vec3(value.angularVelocity);
}

void B3MotorJointDef::SetAngularVelocity(const B3Vec3& velocity) {
    value.angularVelocity = velocity.value;
}

float B3MotorJointDef::GetMaxVelocityTorque() const {
    return value.maxVelocityTorque;
}

void B3MotorJointDef::SetMaxVelocityTorque(float torque) {
    value.maxVelocityTorque = torque;
}

float B3MotorJointDef::GetLinearHertz() const {
    return value.linearHertz;
}

void B3MotorJointDef::SetLinearHertz(float hertz) {
    value.linearHertz = hertz;
}

float B3MotorJointDef::GetLinearDampingRatio() const {
    return value.linearDampingRatio;
}

void B3MotorJointDef::SetLinearDampingRatio(float ratio) {
    value.linearDampingRatio = ratio;
}

float B3MotorJointDef::GetMaxSpringForce() const {
    return value.maxSpringForce;
}

void B3MotorJointDef::SetMaxSpringForce(float force) {
    value.maxSpringForce = force;
}

float B3MotorJointDef::GetAngularHertz() const {
    return value.angularHertz;
}

void B3MotorJointDef::SetAngularHertz(float hertz) {
    value.angularHertz = hertz;
}

float B3MotorJointDef::GetAngularDampingRatio() const {
    return value.angularDampingRatio;
}

void B3MotorJointDef::SetAngularDampingRatio(float ratio) {
    value.angularDampingRatio = ratio;
}

float B3MotorJointDef::GetMaxSpringTorque() const {
    return value.maxSpringTorque;
}

void B3MotorJointDef::SetMaxSpringTorque(float torque) {
    value.maxSpringTorque = torque;
}

B3ParallelJointDef::B3ParallelJointDef() : value(b3DefaultParallelJointDef()) {
}

DEFINE_B3_JOINT_BASE_ACCESSORS(B3ParallelJointDef)

float B3ParallelJointDef::GetHertz() const {
    return value.hertz;
}

void B3ParallelJointDef::SetHertz(float hertz) {
    value.hertz = hertz;
}

float B3ParallelJointDef::GetDampingRatio() const {
    return value.dampingRatio;
}

void B3ParallelJointDef::SetDampingRatio(float ratio) {
    value.dampingRatio = ratio;
}

float B3ParallelJointDef::GetMaxTorque() const {
    return value.maxTorque;
}

void B3ParallelJointDef::SetMaxTorque(float torque) {
    value.maxTorque = torque;
}

B3PrismaticJointDef::B3PrismaticJointDef() : value(b3DefaultPrismaticJointDef()) {
}

DEFINE_B3_JOINT_BASE_ACCESSORS(B3PrismaticJointDef)

void B3PrismaticJointDef::SetForceThreshold(float force) { value.base.forceThreshold = force; }
void B3PrismaticJointDef::SetTorqueThreshold(float torque) { value.base.torqueThreshold = torque; }
float B3PrismaticJointDef::GetConstraintHertz() const { return value.base.constraintHertz; }
void B3PrismaticJointDef::SetConstraintHertz(float hertz) { value.base.constraintHertz = hertz; }

bool B3PrismaticJointDef::GetEnableSpring() const {
    return value.enableSpring;
}

void B3PrismaticJointDef::SetEnableSpring(bool enabled) {
    value.enableSpring = enabled;
}

float B3PrismaticJointDef::GetHertz() const {
    return value.hertz;
}

void B3PrismaticJointDef::SetHertz(float hertz) {
    value.hertz = hertz;
}

float B3PrismaticJointDef::GetDampingRatio() const {
    return value.dampingRatio;
}

void B3PrismaticJointDef::SetDampingRatio(float ratio) {
    value.dampingRatio = ratio;
}

float B3PrismaticJointDef::GetTargetTranslation() const {
    return value.targetTranslation;
}

void B3PrismaticJointDef::SetTargetTranslation(float translation) {
    value.targetTranslation = translation;
}

bool B3PrismaticJointDef::GetEnableLimit() const {
    return value.enableLimit;
}

void B3PrismaticJointDef::SetEnableLimit(bool enabled) {
    value.enableLimit = enabled;
}

float B3PrismaticJointDef::GetLowerTranslation() const {
    return value.lowerTranslation;
}

void B3PrismaticJointDef::SetLowerTranslation(float translation) {
    value.lowerTranslation = translation;
}

float B3PrismaticJointDef::GetUpperTranslation() const {
    return value.upperTranslation;
}

void B3PrismaticJointDef::SetUpperTranslation(float translation) {
    value.upperTranslation = translation;
}

bool B3PrismaticJointDef::GetEnableMotor() const {
    return value.enableMotor;
}

void B3PrismaticJointDef::SetEnableMotor(bool enabled) {
    value.enableMotor = enabled;
}

float B3PrismaticJointDef::GetMaxMotorForce() const {
    return value.maxMotorForce;
}

void B3PrismaticJointDef::SetMaxMotorForce(float force) {
    value.maxMotorForce = force;
}

float B3PrismaticJointDef::GetMotorSpeed() const {
    return value.motorSpeed;
}

void B3PrismaticJointDef::SetMotorSpeed(float speed) {
    value.motorSpeed = speed;
}

B3SphericalJointDef::B3SphericalJointDef() : value(b3DefaultSphericalJointDef()) {
}

long long B3SphericalJointDef::GetBodyIdA() const {
    return static_cast<long long>(b3StoreBodyId(value.base.bodyIdA));
}

void B3SphericalJointDef::SetBodyIdA(long long bodyId) {
    value.base.bodyIdA = loadBodyId(bodyId);
}

long long B3SphericalJointDef::GetBodyIdB() const {
    return static_cast<long long>(b3StoreBodyId(value.base.bodyIdB));
}

void B3SphericalJointDef::SetBodyIdB(long long bodyId) {
    value.base.bodyIdB = loadBodyId(bodyId);
}

B3Transform B3SphericalJointDef::GetLocalFrameA() const {
    return B3Transform(value.base.localFrameA);
}

void B3SphericalJointDef::SetLocalFrameA(const B3Transform& transform) {
    value.base.localFrameA = transform.value;
}

B3Transform B3SphericalJointDef::GetLocalFrameB() const {
    return B3Transform(value.base.localFrameB);
}

void B3SphericalJointDef::SetLocalFrameB(const B3Transform& transform) {
    value.base.localFrameB = transform.value;
}

void B3SphericalJointDef::SetLocalPositionA(const B3Vec3& position) {
    value.base.localFrameA.p = position.value;
}

void B3SphericalJointDef::SetLocalPositionB(const B3Vec3& position) {
    value.base.localFrameB.p = position.value;
}

float B3SphericalJointDef::GetDrawScale() const {
    return value.base.drawScale;
}

void B3SphericalJointDef::SetDrawScale(float scale) {
    value.base.drawScale = scale;
}

bool B3SphericalJointDef::GetCollideConnected() const {
    return value.base.collideConnected;
}

void B3SphericalJointDef::SetCollideConnected(bool collide) {
    value.base.collideConnected = collide;
}

float B3SphericalJointDef::GetConstraintHertz() const {
    return value.base.constraintHertz;
}

void B3SphericalJointDef::SetConstraintHertz(float hertz) {
    value.base.constraintHertz = hertz;
}

float B3SphericalJointDef::GetConstraintDampingRatio() const {
    return value.base.constraintDampingRatio;
}

void B3SphericalJointDef::SetConstraintDampingRatio(float ratio) {
    value.base.constraintDampingRatio = ratio;
}

bool B3SphericalJointDef::GetEnableSpring() const {
    return value.enableSpring;
}

void B3SphericalJointDef::SetEnableSpring(bool enabled) {
    value.enableSpring = enabled;
}

float B3SphericalJointDef::GetHertz() const {
    return value.hertz;
}

void B3SphericalJointDef::SetHertz(float hertz) {
    value.hertz = hertz;
}

float B3SphericalJointDef::GetDampingRatio() const {
    return value.dampingRatio;
}

void B3SphericalJointDef::SetDampingRatio(float ratio) {
    value.dampingRatio = ratio;
}

bool B3SphericalJointDef::GetEnableConeLimit() const {
    return value.enableConeLimit;
}

void B3SphericalJointDef::SetEnableConeLimit(bool enabled) {
    value.enableConeLimit = enabled;
}

float B3SphericalJointDef::GetConeAngle() const {
    return value.coneAngle;
}

void B3SphericalJointDef::SetConeAngle(float radians) {
    value.coneAngle = radians;
}

bool B3SphericalJointDef::GetEnableTwistLimit() const {
    return value.enableTwistLimit;
}

void B3SphericalJointDef::SetEnableTwistLimit(bool enabled) {
    value.enableTwistLimit = enabled;
}

float B3SphericalJointDef::GetLowerTwistAngle() const {
    return value.lowerTwistAngle;
}

void B3SphericalJointDef::SetLowerTwistAngle(float radians) {
    value.lowerTwistAngle = radians;
}

float B3SphericalJointDef::GetUpperTwistAngle() const {
    return value.upperTwistAngle;
}

void B3SphericalJointDef::SetUpperTwistAngle(float radians) {
    value.upperTwistAngle = radians;
}

bool B3SphericalJointDef::GetEnableMotor() const {
    return value.enableMotor;
}

void B3SphericalJointDef::SetEnableMotor(bool enabled) {
    value.enableMotor = enabled;
}

float B3SphericalJointDef::GetMaxMotorTorque() const {
    return value.maxMotorTorque;
}

void B3SphericalJointDef::SetMaxMotorTorque(float torque) {
    value.maxMotorTorque = torque;
}

B3Vec3 B3SphericalJointDef::GetMotorVelocity() const {
    return B3Vec3(value.motorVelocity);
}

void B3SphericalJointDef::SetMotorVelocity(const B3Vec3& velocity) {
    value.motorVelocity = velocity.value;
}

B3RevoluteJointDef::B3RevoluteJointDef() : value(b3DefaultRevoluteJointDef()) {
}

long long B3RevoluteJointDef::GetBodyIdA() const {
    return static_cast<long long>(b3StoreBodyId(value.base.bodyIdA));
}

void B3RevoluteJointDef::SetBodyIdA(long long bodyId) {
    value.base.bodyIdA = loadBodyId(bodyId);
}

long long B3RevoluteJointDef::GetBodyIdB() const {
    return static_cast<long long>(b3StoreBodyId(value.base.bodyIdB));
}

void B3RevoluteJointDef::SetBodyIdB(long long bodyId) {
    value.base.bodyIdB = loadBodyId(bodyId);
}

B3Transform B3RevoluteJointDef::GetLocalFrameA() const {
    return B3Transform(value.base.localFrameA);
}

void B3RevoluteJointDef::SetLocalFrameA(const B3Transform& transform) {
    value.base.localFrameA = transform.value;
}

B3Transform B3RevoluteJointDef::GetLocalFrameB() const {
    return B3Transform(value.base.localFrameB);
}

void B3RevoluteJointDef::SetLocalFrameB(const B3Transform& transform) {
    value.base.localFrameB = transform.value;
}

void B3RevoluteJointDef::SetLocalPositionA(const B3Vec3& position) {
    value.base.localFrameA.p = position.value;
}

void B3RevoluteJointDef::SetLocalPositionB(const B3Vec3& position) {
    value.base.localFrameB.p = position.value;
}

float B3RevoluteJointDef::GetDrawScale() const {
    return value.base.drawScale;
}

void B3RevoluteJointDef::SetDrawScale(float scale) {
    value.base.drawScale = scale;
}

bool B3RevoluteJointDef::GetCollideConnected() const {
    return value.base.collideConnected;
}

void B3RevoluteJointDef::SetCollideConnected(bool collide) {
    value.base.collideConnected = collide;
}

void B3RevoluteJointDef::SetForceThreshold(float force) { value.base.forceThreshold = force; }
void B3RevoluteJointDef::SetTorqueThreshold(float torque) { value.base.torqueThreshold = torque; }

float B3RevoluteJointDef::GetConstraintHertz() const {
    return value.base.constraintHertz;
}

void B3RevoluteJointDef::SetConstraintHertz(float hertz) {
    value.base.constraintHertz = hertz;
}

float B3RevoluteJointDef::GetConstraintDampingRatio() const {
    return value.base.constraintDampingRatio;
}

void B3RevoluteJointDef::SetConstraintDampingRatio(float ratio) {
    value.base.constraintDampingRatio = ratio;
}

float B3RevoluteJointDef::GetTargetAngle() const {
    return value.targetAngle;
}

void B3RevoluteJointDef::SetTargetAngle(float radians) {
    value.targetAngle = radians;
}

bool B3RevoluteJointDef::GetEnableSpring() const {
    return value.enableSpring;
}

void B3RevoluteJointDef::SetEnableSpring(bool enabled) {
    value.enableSpring = enabled;
}

float B3RevoluteJointDef::GetHertz() const {
    return value.hertz;
}

void B3RevoluteJointDef::SetHertz(float hertz) {
    value.hertz = hertz;
}

float B3RevoluteJointDef::GetDampingRatio() const {
    return value.dampingRatio;
}

void B3RevoluteJointDef::SetDampingRatio(float ratio) {
    value.dampingRatio = ratio;
}

bool B3RevoluteJointDef::GetEnableLimit() const {
    return value.enableLimit;
}

void B3RevoluteJointDef::SetEnableLimit(bool enabled) {
    value.enableLimit = enabled;
}

float B3RevoluteJointDef::GetLowerAngle() const {
    return value.lowerAngle;
}

void B3RevoluteJointDef::SetLowerAngle(float radians) {
    value.lowerAngle = radians;
}

float B3RevoluteJointDef::GetUpperAngle() const {
    return value.upperAngle;
}

void B3RevoluteJointDef::SetUpperAngle(float radians) {
    value.upperAngle = radians;
}

bool B3RevoluteJointDef::GetEnableMotor() const {
    return value.enableMotor;
}

void B3RevoluteJointDef::SetEnableMotor(bool enabled) {
    value.enableMotor = enabled;
}

float B3RevoluteJointDef::GetMaxMotorTorque() const {
    return value.maxMotorTorque;
}

void B3RevoluteJointDef::SetMaxMotorTorque(float torque) {
    value.maxMotorTorque = torque;
}

float B3RevoluteJointDef::GetMotorSpeed() const {
    return value.motorSpeed;
}

void B3RevoluteJointDef::SetMotorSpeed(float speed) {
    value.motorSpeed = speed;
}

B3WeldJointDef::B3WeldJointDef() : value(b3DefaultWeldJointDef()) {
}

long long B3WeldJointDef::GetBodyIdA() const {
    return static_cast<long long>(b3StoreBodyId(value.base.bodyIdA));
}

void B3WeldJointDef::SetBodyIdA(long long bodyId) {
    value.base.bodyIdA = loadBodyId(bodyId);
}

long long B3WeldJointDef::GetBodyIdB() const {
    return static_cast<long long>(b3StoreBodyId(value.base.bodyIdB));
}

void B3WeldJointDef::SetBodyIdB(long long bodyId) {
    value.base.bodyIdB = loadBodyId(bodyId);
}

B3Transform B3WeldJointDef::GetLocalFrameA() const {
    return B3Transform(value.base.localFrameA);
}

void B3WeldJointDef::SetLocalFrameA(const B3Transform& transform) {
    value.base.localFrameA = transform.value;
}

B3Transform B3WeldJointDef::GetLocalFrameB() const {
    return B3Transform(value.base.localFrameB);
}

void B3WeldJointDef::SetLocalFrameB(const B3Transform& transform) {
    value.base.localFrameB = transform.value;
}

void B3WeldJointDef::SetLocalPositionA(const B3Vec3& position) {
    value.base.localFrameA.p = position.value;
}

void B3WeldJointDef::SetLocalPositionB(const B3Vec3& position) {
    value.base.localFrameB.p = position.value;
}

float B3WeldJointDef::GetDrawScale() const {
    return value.base.drawScale;
}

void B3WeldJointDef::SetDrawScale(float scale) {
    value.base.drawScale = scale;
}

bool B3WeldJointDef::GetCollideConnected() const {
    return value.base.collideConnected;
}

void B3WeldJointDef::SetCollideConnected(bool collide) {
    value.base.collideConnected = collide;
}

void B3WeldJointDef::SetForceThreshold(float force) { value.base.forceThreshold = force; }
void B3WeldJointDef::SetTorqueThreshold(float torque) { value.base.torqueThreshold = torque; }

float B3WeldJointDef::GetConstraintHertz() const {
    return value.base.constraintHertz;
}

void B3WeldJointDef::SetConstraintHertz(float hertz) {
    value.base.constraintHertz = hertz;
}

float B3WeldJointDef::GetConstraintDampingRatio() const {
    return value.base.constraintDampingRatio;
}

void B3WeldJointDef::SetConstraintDampingRatio(float ratio) {
    value.base.constraintDampingRatio = ratio;
}

float B3WeldJointDef::GetLinearHertz() const {
    return value.linearHertz;
}

void B3WeldJointDef::SetLinearHertz(float hertz) {
    value.linearHertz = hertz;
}

float B3WeldJointDef::GetAngularHertz() const {
    return value.angularHertz;
}

void B3WeldJointDef::SetAngularHertz(float hertz) {
    value.angularHertz = hertz;
}

float B3WeldJointDef::GetLinearDampingRatio() const {
    return value.linearDampingRatio;
}

void B3WeldJointDef::SetLinearDampingRatio(float ratio) {
    value.linearDampingRatio = ratio;
}

float B3WeldJointDef::GetAngularDampingRatio() const {
    return value.angularDampingRatio;
}

void B3WeldJointDef::SetAngularDampingRatio(float ratio) {
    value.angularDampingRatio = ratio;
}

B3FilterJointDef::B3FilterJointDef() : value(b3DefaultFilterJointDef()) {
}

long long B3FilterJointDef::GetBodyIdA() const {
    return static_cast<long long>(b3StoreBodyId(value.base.bodyIdA));
}

void B3FilterJointDef::SetBodyIdA(long long bodyId) {
    value.base.bodyIdA = loadBodyId(bodyId);
}

long long B3FilterJointDef::GetBodyIdB() const {
    return static_cast<long long>(b3StoreBodyId(value.base.bodyIdB));
}

void B3FilterJointDef::SetBodyIdB(long long bodyId) {
    value.base.bodyIdB = loadBodyId(bodyId);
}

B3Transform B3FilterJointDef::GetLocalFrameA() const {
    return B3Transform(value.base.localFrameA);
}

void B3FilterJointDef::SetLocalFrameA(const B3Transform& transform) {
    value.base.localFrameA = transform.value;
}

B3Transform B3FilterJointDef::GetLocalFrameB() const {
    return B3Transform(value.base.localFrameB);
}

void B3FilterJointDef::SetLocalFrameB(const B3Transform& transform) {
    value.base.localFrameB = transform.value;
}

float B3FilterJointDef::GetDrawScale() const {
    return value.base.drawScale;
}

void B3FilterJointDef::SetDrawScale(float scale) {
    value.base.drawScale = scale;
}

bool B3FilterJointDef::GetCollideConnected() const {
    return value.base.collideConnected;
}

void B3FilterJointDef::SetCollideConnected(bool collide) {
    value.base.collideConnected = collide;
}

B3WheelJointDef::B3WheelJointDef() : value(b3DefaultWheelJointDef()) {
}

DEFINE_B3_JOINT_BASE_ACCESSORS(B3WheelJointDef)

bool B3WheelJointDef::GetEnableSuspensionSpring() const {
    return value.enableSuspensionSpring;
}

void B3WheelJointDef::SetEnableSuspensionSpring(bool enabled) {
    value.enableSuspensionSpring = enabled;
}

float B3WheelJointDef::GetSuspensionHertz() const {
    return value.suspensionHertz;
}

void B3WheelJointDef::SetSuspensionHertz(float hertz) {
    value.suspensionHertz = hertz;
}

float B3WheelJointDef::GetSuspensionDampingRatio() const {
    return value.suspensionDampingRatio;
}

void B3WheelJointDef::SetSuspensionDampingRatio(float ratio) {
    value.suspensionDampingRatio = ratio;
}

bool B3WheelJointDef::GetEnableSuspensionLimit() const {
    return value.enableSuspensionLimit;
}

void B3WheelJointDef::SetEnableSuspensionLimit(bool enabled) {
    value.enableSuspensionLimit = enabled;
}

float B3WheelJointDef::GetLowerSuspensionLimit() const {
    return value.lowerSuspensionLimit;
}

void B3WheelJointDef::SetLowerSuspensionLimit(float limit) {
    value.lowerSuspensionLimit = limit;
}

float B3WheelJointDef::GetUpperSuspensionLimit() const {
    return value.upperSuspensionLimit;
}

void B3WheelJointDef::SetUpperSuspensionLimit(float limit) {
    value.upperSuspensionLimit = limit;
}

bool B3WheelJointDef::GetEnableSpinMotor() const {
    return value.enableSpinMotor;
}

void B3WheelJointDef::SetEnableSpinMotor(bool enabled) {
    value.enableSpinMotor = enabled;
}

float B3WheelJointDef::GetMaxSpinTorque() const {
    return value.maxSpinTorque;
}

void B3WheelJointDef::SetMaxSpinTorque(float torque) {
    value.maxSpinTorque = torque;
}

float B3WheelJointDef::GetSpinSpeed() const {
    return value.spinSpeed;
}

void B3WheelJointDef::SetSpinSpeed(float speed) {
    value.spinSpeed = speed;
}

bool B3WheelJointDef::GetEnableSteering() const {
    return value.enableSteering;
}

void B3WheelJointDef::SetEnableSteering(bool enabled) {
    value.enableSteering = enabled;
}

float B3WheelJointDef::GetSteeringHertz() const {
    return value.steeringHertz;
}

void B3WheelJointDef::SetSteeringHertz(float hertz) {
    value.steeringHertz = hertz;
}

float B3WheelJointDef::GetSteeringDampingRatio() const {
    return value.steeringDampingRatio;
}

void B3WheelJointDef::SetSteeringDampingRatio(float ratio) {
    value.steeringDampingRatio = ratio;
}

float B3WheelJointDef::GetTargetSteeringAngle() const {
    return value.targetSteeringAngle;
}

void B3WheelJointDef::SetTargetSteeringAngle(float radians) {
    value.targetSteeringAngle = radians;
}

float B3WheelJointDef::GetMaxSteeringTorque() const {
    return value.maxSteeringTorque;
}

void B3WheelJointDef::SetMaxSteeringTorque(float torque) {
    value.maxSteeringTorque = torque;
}

bool B3WheelJointDef::GetEnableSteeringLimit() const {
    return value.enableSteeringLimit;
}

void B3WheelJointDef::SetEnableSteeringLimit(bool enabled) {
    value.enableSteeringLimit = enabled;
}

float B3WheelJointDef::GetLowerSteeringLimit() const {
    return value.lowerSteeringLimit;
}

void B3WheelJointDef::SetLowerSteeringLimit(float radians) {
    value.lowerSteeringLimit = radians;
}

float B3WheelJointDef::GetUpperSteeringLimit() const {
    return value.upperSteeringLimit;
}

void B3WheelJointDef::SetUpperSteeringLimit(float radians) {
    value.upperSteeringLimit = radians;
}

#undef DEFINE_B3_JOINT_BASE_ACCESSORS

struct B3HeightFieldDebugContext {
    B3DebugShape* shape;
    b3Transform transform;
};

B3DebugShape::B3DebugShape()
    : m_shapeId(0),
      m_geometryId(0),
      m_type(-1),
      m_scale(b3Vec3_one),
      m_sphere(),
      m_capsule(),
      m_geometrySource(this),
      m_compound(nullptr),
      m_localTransform(b3Transform_identity),
      m_compoundChildren(),
      m_ownedCompoundGeometries(),
      m_spheres(),
      m_capsules(),
      m_hullEdgeVertices0(),
      m_hullEdgeVertices1(),
      m_triangleVertices0(),
      m_triangleVertices1(),
      m_triangleVertices2(),
      m_triangleNormals() {
}

B3DebugShape::B3DebugShape(const b3DebugShape& shape)
    : m_shapeId(static_cast<long long>(b3StoreShapeId(shape.shapeId))),
      m_geometryId(0),
      m_type(static_cast<int>(shape.type)),
      m_scale(b3Vec3_one),
      m_sphere(),
      m_capsule(),
      m_geometrySource(this),
      m_compound(nullptr),
      m_localTransform(b3Transform_identity),
      m_compoundChildren(),
      m_ownedCompoundGeometries(),
      m_spheres(),
      m_capsules(),
      m_hullEdgeVertices0(),
      m_hullEdgeVertices1(),
      m_triangleVertices0(),
      m_triangleVertices1(),
      m_triangleVertices2(),
      m_triangleNormals() {
    b3Transform identity = b3Transform_identity;
    if(shape.type == b3_sphereShape && shape.sphere != nullptr) {
        m_geometryId = -1;
        m_sphere = B3Sphere(*shape.sphere);
        AddSphere(*shape.sphere, identity);
    }
    else if(shape.type == b3_capsuleShape && shape.capsule != nullptr) {
        m_geometryId = -2;
        m_capsule = B3Capsule(*shape.capsule);
        AddCapsule(*shape.capsule, identity);
    }
    else if(shape.type == b3_hullShape && shape.hull != nullptr) {
        m_geometryId = static_cast<long long>(reinterpret_cast<intptr_t>(this));
        AddHull(shape.hull, identity);
    }
    else if(shape.type == b3_meshShape && shape.mesh != nullptr) {
        m_geometryId = static_cast<long long>(reinterpret_cast<intptr_t>(this));
        m_scale = B3Vec3(shape.mesh->scale);
        b3Mesh unscaledMesh{shape.mesh->data, b3Vec3_one};
        AddMesh(&unscaledMesh, identity);
    }
    else if(shape.type == b3_heightShape && shape.heightField != nullptr) {
        m_geometryId = static_cast<long long>(reinterpret_cast<intptr_t>(this));
        AddHeightField(shape.heightField, identity);
    }
    else if(shape.type == b3_compoundShape && shape.compound != nullptr) {
        m_geometryId = static_cast<long long>(reinterpret_cast<intptr_t>(this));
        AddCompound(shape.compound);
    }
}

B3DebugShape::~B3DebugShape() {
    for(B3DebugShape* child : m_compoundChildren) {
        delete child;
    }
    for(B3DebugShape* geometry : m_ownedCompoundGeometries) {
        delete geometry;
    }
}

long long B3DebugShape::GetShapeId() const {
    return m_shapeId;
}

long long B3DebugShape::GetGeometryId() const {
    return m_geometryId;
}

int B3DebugShape::GetType() const {
    return m_type;
}

B3Vec3 B3DebugShape::GetScale() const {
    return m_scale;
}

B3Sphere B3DebugShape::GetSphere() const {
    return m_sphere;
}

B3Capsule B3DebugShape::GetCapsule() const {
    return m_capsule;
}

int B3DebugShape::GetHullEdgeCount() const {
    return static_cast<int>(m_geometrySource->m_hullEdgeVertices0.size());
}

B3Vec3 B3DebugShape::GetHullEdgeVertex0(int index) const {
    return getIndexedVec3(m_geometrySource->m_hullEdgeVertices0, index);
}

B3Vec3 B3DebugShape::GetHullEdgeVertex1(int index) const {
    return getIndexedVec3(m_geometrySource->m_hullEdgeVertices1, index);
}

int B3DebugShape::GetSphereCount() const {
    return static_cast<int>(m_spheres.size());
}

B3Sphere B3DebugShape::GetSphereAt(int index) const {
    if(index < 0 || index >= static_cast<int>(m_spheres.size())) {
        return B3Sphere();
    }
    return m_spheres[static_cast<size_t>(index)];
}

int B3DebugShape::GetCapsuleCount() const {
    return static_cast<int>(m_capsules.size());
}

B3Capsule B3DebugShape::GetCapsuleAt(int index) const {
    if(index < 0 || index >= static_cast<int>(m_capsules.size())) {
        return B3Capsule();
    }
    return m_capsules[static_cast<size_t>(index)];
}

int B3DebugShape::GetTriangleCount() const {
    return static_cast<int>(m_geometrySource->m_triangleVertices0.size());
}

B3Vec3 B3DebugShape::GetTriangleVertex0(int index) const {
    return getIndexedVec3(m_geometrySource->m_triangleVertices0, index);
}

B3Vec3 B3DebugShape::GetTriangleVertex1(int index) const {
    return getIndexedVec3(m_geometrySource->m_triangleVertices1, index);
}

B3Vec3 B3DebugShape::GetTriangleVertex2(int index) const {
    return getIndexedVec3(m_geometrySource->m_triangleVertices2, index);
}

B3Vec3 B3DebugShape::GetTriangleNormal(int index) const {
    return getIndexedVec3(m_geometrySource->m_triangleNormals, index);
}

void B3DebugShape::AddSphere(const b3Sphere& sphere, b3Transform transform) {
    b3Sphere transformed = sphere;
    transformed.center = b3TransformPoint(transform, sphere.center);
    m_spheres.push_back(B3Sphere(transformed));
}

void B3DebugShape::AddCapsule(const b3Capsule& capsule, b3Transform transform) {
    b3Capsule transformed = capsule;
    transformed.center1 = b3TransformPoint(transform, capsule.center1);
    transformed.center2 = b3TransformPoint(transform, capsule.center2);
    m_capsules.push_back(B3Capsule(transformed));
}

void B3DebugShape::AddEdge(b3Vec3 v0, b3Vec3 v1) {
    m_hullEdgeVertices0.push_back(B3Vec3(v0));
    m_hullEdgeVertices1.push_back(B3Vec3(v1));
}

void B3DebugShape::AddTriangle(b3Vec3 v0, b3Vec3 v1, b3Vec3 v2) {
    b3Vec3 edge1 = b3Sub(v1, v0);
    b3Vec3 edge2 = b3Sub(v2, v0);
    b3Vec3 normal = b3Cross(edge1, edge2);
    if(b3LengthSquared(normal) > 0.00000001f) {
        normal = b3Normalize(normal);
    }
    else {
        normal = b3Vec3{0.0f, 1.0f, 0.0f};
    }
    m_triangleVertices0.push_back(B3Vec3(v0));
    m_triangleVertices1.push_back(B3Vec3(v1));
    m_triangleVertices2.push_back(B3Vec3(v2));
    m_triangleNormals.push_back(B3Vec3(normal));
}

void B3DebugShape::AddHull(const b3HullData* hull, b3Transform transform) {
    if(hull == nullptr) {
        return;
    }
    const b3Vec3* points = b3GetHullPoints(hull);
    const b3HullHalfEdge* edges = b3GetHullEdges(hull);
    if(points == nullptr || edges == nullptr) {
        return;
    }

    for(int i = 0; i < hull->edgeCount; i++) {
        const b3HullHalfEdge& edge = edges[i];
        if(i > edge.twin) {
            continue;
        }
        const b3HullHalfEdge& next = edges[edge.next];
        AddEdge(b3TransformPoint(transform, points[edge.origin]), b3TransformPoint(transform, points[next.origin]));
    }

    const b3HullFace* faces = b3GetHullFaces(hull);
    if(faces == nullptr) {
        return;
    }

    std::vector<b3Vec3> facePoints;
    facePoints.reserve(8);
    for(int faceIndex = 0; faceIndex < hull->faceCount; faceIndex++) {
        facePoints.clear();
        int startEdge = faces[faceIndex].edge;
        int edgeIndex = startEdge;
        int guard = 0;
        do {
            const b3HullHalfEdge& edge = edges[edgeIndex];
            facePoints.push_back(b3TransformPoint(transform, points[edge.origin]));
            edgeIndex = edge.next;
            guard++;
        } while(edgeIndex != startEdge && guard <= hull->edgeCount);

        if(facePoints.size() < 3 || guard > hull->edgeCount) {
            continue;
        }
        for(size_t i = 1; i + 1 < facePoints.size(); i++) {
            AddTriangle(facePoints[0], facePoints[i], facePoints[i + 1]);
        }
    }
}

void B3DebugShape::AddMesh(const b3Mesh* mesh, b3Transform transform) {
    if(mesh == nullptr || mesh->data == nullptr) {
        return;
    }
    const b3MeshData* meshData = mesh->data;
    const b3Vec3* vertices = b3GetMeshVertices(meshData);
    const b3MeshTriangle* triangles = b3GetMeshTriangles(meshData);
    if(vertices == nullptr || triangles == nullptr) {
        return;
    }

    for(int i = 0; i < meshData->triangleCount; i++) {
        const b3MeshTriangle& triangle = triangles[i];
        b3Vec3 v0 = vertices[triangle.index1];
        b3Vec3 v1 = vertices[triangle.index2];
        b3Vec3 v2 = vertices[triangle.index3];
        v0 = b3Vec3{v0.x * mesh->scale.x, v0.y * mesh->scale.y, v0.z * mesh->scale.z};
        v1 = b3Vec3{v1.x * mesh->scale.x, v1.y * mesh->scale.y, v1.z * mesh->scale.z};
        v2 = b3Vec3{v2.x * mesh->scale.x, v2.y * mesh->scale.y, v2.z * mesh->scale.z};
        v0 = b3TransformPoint(transform, v0);
        v1 = b3TransformPoint(transform, v1);
        v2 = b3TransformPoint(transform, v2);
        AddTriangle(v0, v1, v2);
        AddEdge(v0, v1);
        AddEdge(v1, v2);
        AddEdge(v2, v0);
    }
}

void B3DebugShape::AddHeightField(const b3HeightFieldData* heightField, b3Transform transform) {
    if(heightField == nullptr) {
        return;
    }
    B3HeightFieldDebugContext context{this, transform};
    b3QueryHeightField(heightField, heightField->aabb, AddHeightFieldTriangle, &context);
}

void B3DebugShape::AddCompound(const b3CompoundData* compound) {
    if(compound == nullptr) {
        return;
    }
    m_compound = compound;
    int childCount = compound->capsuleCount + compound->hullCount + compound->meshCount + compound->sphereCount;
    m_compoundChildren.reserve(static_cast<size_t>(childCount));
    for(int i = 0; i < childCount; i++) {
        b3ChildShape child = b3GetCompoundChild(compound, i);
        B3DebugShape* debugChild = new B3DebugShape();
        debugChild->m_shapeId = m_shapeId;
        debugChild->m_type = static_cast<int>(child.type);
        debugChild->m_localTransform = child.transform;

        if(child.type == b3_sphereShape) {
            debugChild->m_geometryId = -1;
            debugChild->m_sphere = B3Sphere(child.sphere);
            debugChild->AddSphere(child.sphere, b3Transform_identity);
        }
        else if(child.type == b3_capsuleShape) {
            debugChild->m_geometryId = -2;
            debugChild->m_capsule = B3Capsule(child.capsule);
            debugChild->AddCapsule(child.capsule, b3Transform_identity);
        }
        else if(child.type == b3_hullShape && child.hull != nullptr) {
            B3DebugShape* geometry = FindOrCreateHullGeometry(child.hull);
            debugChild->m_geometrySource = geometry;
            debugChild->m_geometryId = geometry->m_geometryId;
        }
        else if(child.type == b3_meshShape && child.mesh.data != nullptr) {
            B3DebugShape* geometry = FindOrCreateMeshGeometry(child.mesh.data);
            debugChild->m_geometrySource = geometry;
            debugChild->m_geometryId = geometry->m_geometryId;
            debugChild->m_scale = B3Vec3(child.mesh.scale);
        }
        m_compoundChildren.push_back(debugChild);
    }
}

B3DebugShape* B3DebugShape::FindOrCreateHullGeometry(const b3HullData* hull) {
    for(B3DebugShape* geometry : m_ownedCompoundGeometries) {
        if(geometry->m_type == b3_hullShape && geometry->m_geometrySource == geometry
                && geometry->m_geometryId == static_cast<long long>(reinterpret_cast<intptr_t>(hull))) {
            return geometry;
        }
    }
    B3DebugShape* geometry = new B3DebugShape();
    geometry->m_shapeId = m_shapeId;
    geometry->m_type = b3_hullShape;
    geometry->m_geometryId = static_cast<long long>(reinterpret_cast<intptr_t>(hull));
    geometry->AddHull(hull, b3Transform_identity);
    m_ownedCompoundGeometries.push_back(geometry);
    return geometry;
}

B3DebugShape* B3DebugShape::FindOrCreateMeshGeometry(const b3MeshData* meshData) {
    for(B3DebugShape* geometry : m_ownedCompoundGeometries) {
        if(geometry->m_type == b3_meshShape && geometry->m_geometrySource == geometry
                && geometry->m_geometryId == static_cast<long long>(reinterpret_cast<intptr_t>(meshData))) {
            return geometry;
        }
    }
    B3DebugShape* geometry = new B3DebugShape();
    geometry->m_shapeId = m_shapeId;
    geometry->m_type = b3_meshShape;
    geometry->m_geometryId = static_cast<long long>(reinterpret_cast<intptr_t>(meshData));
    b3Mesh mesh{meshData, b3Vec3_one};
    geometry->AddMesh(&mesh, b3Transform_identity);
    m_ownedCompoundGeometries.push_back(geometry);
    return geometry;
}

bool B3DebugShape::AddHeightFieldTriangle(b3Vec3 v0, b3Vec3 v1, b3Vec3 v2, int, void* context) {
    B3HeightFieldDebugContext* debugContext = static_cast<B3HeightFieldDebugContext*>(context);
    if(debugContext == nullptr || debugContext->shape == nullptr) {
        return true;
    }
    b3Vec3 tv0 = b3TransformPoint(debugContext->transform, v0);
    b3Vec3 tv1 = b3TransformPoint(debugContext->transform, v1);
    b3Vec3 tv2 = b3TransformPoint(debugContext->transform, v2);
    debugContext->shape->AddTriangle(tv0, tv1, tv2);
    debugContext->shape->AddEdge(tv0, tv1);
    debugContext->shape->AddEdge(tv1, tv2);
    debugContext->shape->AddEdge(tv2, tv0);
    return true;
}

B3Filter::B3Filter() : value(b3DefaultFilter()) {
}

B3Filter::B3Filter(b3Filter value) : value(value) {
}

long long B3Filter::GetCategoryBits() const {
    return static_cast<long long>(value.categoryBits);
}

void B3Filter::SetCategoryBits(long long categoryBits) {
    value.categoryBits = static_cast<uint64_t>(categoryBits);
}

long long B3Filter::GetMaskBits() const {
    return static_cast<long long>(value.maskBits);
}

void B3Filter::SetMaskBits(long long maskBits) {
    value.maskBits = static_cast<uint64_t>(maskBits);
}

int B3Filter::GetGroupIndex() const {
    return value.groupIndex;
}

void B3Filter::SetGroupIndex(int groupIndex) {
    value.groupIndex = groupIndex;
}

B3QueryFilter::B3QueryFilter() : value(b3DefaultQueryFilter()) {
}

B3QueryFilter::B3QueryFilter(b3QueryFilter value) : value(value) {
}

long long B3QueryFilter::GetCategoryBits() const {
    return static_cast<long long>(value.categoryBits);
}

void B3QueryFilter::SetCategoryBits(long long categoryBits) {
    value.categoryBits = static_cast<uint64_t>(categoryBits);
}

long long B3QueryFilter::GetMaskBits() const {
    return static_cast<long long>(value.maskBits);
}

void B3QueryFilter::SetMaskBits(long long maskBits) {
    value.maskBits = static_cast<uint64_t>(maskBits);
}

long long B3QueryFilter::GetId() const {
    return static_cast<long long>(value.id);
}

void B3QueryFilter::SetId(long long id) {
    value.id = static_cast<uint64_t>(id);
}

B3SurfaceMaterial::B3SurfaceMaterial() : value(b3DefaultSurfaceMaterial()) {
}

B3SurfaceMaterial::B3SurfaceMaterial(b3SurfaceMaterial value) : value(value) {
}

float B3SurfaceMaterial::GetFriction() const {
    return value.friction;
}

void B3SurfaceMaterial::SetFriction(float friction) {
    value.friction = friction;
}

float B3SurfaceMaterial::GetRestitution() const {
    return value.restitution;
}

void B3SurfaceMaterial::SetRestitution(float restitution) {
    value.restitution = restitution;
}

float B3SurfaceMaterial::GetRollingResistance() const {
    return value.rollingResistance;
}

void B3SurfaceMaterial::SetRollingResistance(float rollingResistance) {
    value.rollingResistance = rollingResistance;
}

B3Vec3 B3SurfaceMaterial::GetTangentVelocity() const {
    return B3Vec3(value.tangentVelocity);
}

void B3SurfaceMaterial::SetTangentVelocity(const B3Vec3& tangentVelocity) {
    value.tangentVelocity = tangentVelocity.value;
}

long long B3SurfaceMaterial::GetUserMaterialId() const {
    return static_cast<long long>(value.userMaterialId);
}

void B3SurfaceMaterial::SetUserMaterialId(long long userMaterialId) {
    value.userMaterialId = static_cast<uint64_t>(userMaterialId);
}

long B3SurfaceMaterial::GetCustomColor() const {
    return static_cast<long>(value.customColor);
}

void B3SurfaceMaterial::SetCustomColor(long customColor) {
    value.customColor = static_cast<uint32_t>(customColor);
}

B3Capacity::B3Capacity() : value{} {
}

B3Capacity::B3Capacity(b3Capacity capacity) : value(capacity) {
}

int B3Capacity::GetStaticShapeCount() const {
    return value.staticShapeCount;
}

void B3Capacity::SetStaticShapeCount(int count) {
    value.staticShapeCount = count;
}

int B3Capacity::GetDynamicShapeCount() const {
    return value.dynamicShapeCount;
}

void B3Capacity::SetDynamicShapeCount(int count) {
    value.dynamicShapeCount = count;
}

int B3Capacity::GetStaticBodyCount() const {
    return value.staticBodyCount;
}

void B3Capacity::SetStaticBodyCount(int count) {
    value.staticBodyCount = count;
}

int B3Capacity::GetDynamicBodyCount() const {
    return value.dynamicBodyCount;
}

void B3Capacity::SetDynamicBodyCount(int count) {
    value.dynamicBodyCount = count;
}

int B3Capacity::GetContactCount() const {
    return value.contactCount;
}

void B3Capacity::SetContactCount(int count) {
    value.contactCount = count;
}

B3ExplosionDef::B3ExplosionDef() : value(b3DefaultExplosionDef()) {
}

long long B3ExplosionDef::GetMaskBits() const {
    return static_cast<long long>(value.maskBits);
}

void B3ExplosionDef::SetMaskBits(long long maskBits) {
    value.maskBits = static_cast<uint64_t>(maskBits);
}

B3Vec3 B3ExplosionDef::GetPosition() const {
    return B3Vec3(value.position);
}

void B3ExplosionDef::SetPosition(const B3Vec3& position) {
    value.position = position.value;
}

float B3ExplosionDef::GetRadius() const {
    return value.radius;
}

void B3ExplosionDef::SetRadius(float radius) {
    value.radius = radius;
}

float B3ExplosionDef::GetFalloff() const {
    return value.falloff;
}

void B3ExplosionDef::SetFalloff(float falloff) {
    value.falloff = falloff;
}

float B3ExplosionDef::GetImpulsePerArea() const {
    return value.impulsePerArea;
}

void B3ExplosionDef::SetImpulsePerArea(float impulse) {
    value.impulsePerArea = impulse;
}

B3WorldDef::B3WorldDef() : value(b3DefaultWorldDef()) {
    value.createDebugShape = createDebugShape;
    value.destroyDebugShape = destroyDebugShape;
    value.userDebugShapeContext = nullptr;
}

B3Vec3 B3WorldDef::GetGravity() const {
    return B3Vec3(value.gravity);
}

void B3WorldDef::SetGravity(const B3Vec3& gravity) {
    value.gravity = gravity.value;
}

float B3WorldDef::GetRestitutionThreshold() const {
    return value.restitutionThreshold;
}

void B3WorldDef::SetRestitutionThreshold(float threshold) {
    value.restitutionThreshold = threshold;
}

float B3WorldDef::GetHitEventThreshold() const {
    return value.hitEventThreshold;
}

void B3WorldDef::SetHitEventThreshold(float threshold) {
    value.hitEventThreshold = threshold;
}

float B3WorldDef::GetContactHertz() const {
    return value.contactHertz;
}

void B3WorldDef::SetContactHertz(float hertz) {
    value.contactHertz = hertz;
}

float B3WorldDef::GetContactDampingRatio() const {
    return value.contactDampingRatio;
}

void B3WorldDef::SetContactDampingRatio(float ratio) {
    value.contactDampingRatio = ratio;
}

float B3WorldDef::GetContactSpeed() const {
    return value.contactSpeed;
}

void B3WorldDef::SetContactSpeed(float speed) {
    value.contactSpeed = speed;
}

float B3WorldDef::GetMaximumLinearSpeed() const {
    return value.maximumLinearSpeed;
}

void B3WorldDef::SetMaximumLinearSpeed(float speed) {
    value.maximumLinearSpeed = speed;
}

bool B3WorldDef::GetEnableSleep() const {
    return value.enableSleep;
}

void B3WorldDef::SetEnableSleep(bool enabled) {
    value.enableSleep = enabled;
}

bool B3WorldDef::GetEnableContinuous() const {
    return value.enableContinuous;
}

void B3WorldDef::SetEnableContinuous(bool enabled) {
    value.enableContinuous = enabled;
}

long B3WorldDef::GetWorkerCount() const {
    return static_cast<long>(value.workerCount);
}

void B3WorldDef::SetWorkerCount(long workerCount) {
    value.workerCount = static_cast<uint32_t>(workerCount);
}

B3Capacity B3WorldDef::GetCapacity() const {
    return B3Capacity(value.capacity);
}

void B3WorldDef::SetCapacity(const B3Capacity& capacity) {
    value.capacity = capacity.value;
}

B3BodyDef::B3BodyDef() : value(b3DefaultBodyDef()) {
}

int B3BodyDef::GetType() const {
    return static_cast<int>(value.type);
}

void B3BodyDef::SetType(int type) {
    value.type = static_cast<b3BodyType>(type);
}

B3Vec3 B3BodyDef::GetPosition() const {
    return B3Vec3(value.position);
}

void B3BodyDef::SetPosition(const B3Vec3& position) {
    value.position = position.value;
}

B3Quat B3BodyDef::GetRotation() const {
    return B3Quat(value.rotation);
}

void B3BodyDef::SetRotation(const B3Quat& rotation) {
    value.rotation = rotation.value;
}

B3Vec3 B3BodyDef::GetLinearVelocity() const {
    return B3Vec3(value.linearVelocity);
}

void B3BodyDef::SetLinearVelocity(const B3Vec3& velocity) {
    value.linearVelocity = velocity.value;
}

B3Vec3 B3BodyDef::GetAngularVelocity() const {
    return B3Vec3(value.angularVelocity);
}

void B3BodyDef::SetAngularVelocity(const B3Vec3& velocity) {
    value.angularVelocity = velocity.value;
}

float B3BodyDef::GetLinearDamping() const {
    return value.linearDamping;
}

void B3BodyDef::SetLinearDamping(float damping) {
    value.linearDamping = damping;
}

float B3BodyDef::GetAngularDamping() const {
    return value.angularDamping;
}

void B3BodyDef::SetAngularDamping(float damping) {
    value.angularDamping = damping;
}

float B3BodyDef::GetGravityScale() const {
    return value.gravityScale;
}

void B3BodyDef::SetGravityScale(float scale) {
    value.gravityScale = scale;
}

float B3BodyDef::GetSleepThreshold() const {
    return value.sleepThreshold;
}

void B3BodyDef::SetSleepThreshold(float threshold) {
    value.sleepThreshold = threshold;
}

B3MotionLocks B3BodyDef::GetMotionLocks() const {
    return B3MotionLocks(value.motionLocks);
}

void B3BodyDef::SetMotionLocks(const B3MotionLocks& locks) {
    value.motionLocks = locks.value;
}

bool B3BodyDef::GetEnableSleep() const {
    return value.enableSleep;
}

void B3BodyDef::SetEnableSleep(bool enabled) {
    value.enableSleep = enabled;
}

bool B3BodyDef::GetIsAwake() const {
    return value.isAwake;
}

void B3BodyDef::SetIsAwake(bool awake) {
    value.isAwake = awake;
}

bool B3BodyDef::GetIsBullet() const {
    return value.isBullet;
}

void B3BodyDef::SetIsBullet(bool bullet) {
    value.isBullet = bullet;
}

bool B3BodyDef::GetIsEnabled() const {
    return value.isEnabled;
}

void B3BodyDef::SetIsEnabled(bool enabled) {
    value.isEnabled = enabled;
}

bool B3BodyDef::GetAllowFastRotation() const {
    return value.allowFastRotation;
}

void B3BodyDef::SetAllowFastRotation(bool allowFastRotation) {
    value.allowFastRotation = allowFastRotation;
}

bool B3BodyDef::GetEnableContactRecycling() const {
    return value.enableContactRecycling;
}

void B3BodyDef::SetEnableContactRecycling(bool enableContactRecycling) {
    value.enableContactRecycling = enableContactRecycling;
}

B3ShapeDef::B3ShapeDef() : value(b3DefaultShapeDef()) {
}

B3SurfaceMaterial B3ShapeDef::GetBaseMaterial() const {
    return B3SurfaceMaterial(value.baseMaterial);
}

void B3ShapeDef::SetBaseMaterial(const B3SurfaceMaterial& material) {
    value.baseMaterial = material.value;
}

float B3ShapeDef::GetDensity() const {
    return value.density;
}

void B3ShapeDef::SetDensity(float density) {
    value.density = density;
}

float B3ShapeDef::GetExplosionScale() const {
    return value.explosionScale;
}

void B3ShapeDef::SetExplosionScale(float scale) {
    value.explosionScale = scale;
}

B3Filter B3ShapeDef::GetFilter() const {
    return B3Filter(value.filter);
}

void B3ShapeDef::SetFilter(const B3Filter& filter) {
    value.filter = filter.value;
}

bool B3ShapeDef::GetEnableCustomFiltering() const {
    return value.enableCustomFiltering;
}

void B3ShapeDef::SetEnableCustomFiltering(bool enabled) {
    value.enableCustomFiltering = enabled;
}

bool B3ShapeDef::GetIsSensor() const {
    return value.isSensor;
}

void B3ShapeDef::SetIsSensor(bool sensor) {
    value.isSensor = sensor;
}

bool B3ShapeDef::GetEnableSensorEvents() const {
    return value.enableSensorEvents;
}

void B3ShapeDef::SetEnableSensorEvents(bool enabled) {
    value.enableSensorEvents = enabled;
}

bool B3ShapeDef::GetEnableContactEvents() const {
    return value.enableContactEvents;
}

void B3ShapeDef::SetEnableContactEvents(bool enabled) {
    value.enableContactEvents = enabled;
}

bool B3ShapeDef::GetEnableHitEvents() const {
    return value.enableHitEvents;
}

void B3ShapeDef::SetEnableHitEvents(bool enabled) {
    value.enableHitEvents = enabled;
}

bool B3ShapeDef::GetEnablePreSolveEvents() const {
    return value.enablePreSolveEvents;
}

void B3ShapeDef::SetEnablePreSolveEvents(bool enabled) {
    value.enablePreSolveEvents = enabled;
}

bool B3ShapeDef::GetInvokeContactCreation() const {
    return value.invokeContactCreation;
}

void B3ShapeDef::SetInvokeContactCreation(bool invokeContactCreation) {
    value.invokeContactCreation = invokeContactCreation;
}

bool B3ShapeDef::GetEnableSpeculativeContact() const {
    return value.enableSpeculativeContact;
}

void B3ShapeDef::SetEnableSpeculativeContact(bool enabled) {
    value.enableSpeculativeContact = enabled;
}

bool B3ShapeDef::GetUpdateBodyMass() const {
    return value.updateBodyMass;
}

void B3ShapeDef::SetUpdateBodyMass(bool updateBodyMass) {
    value.updateBodyMass = updateBodyMass;
}

B3ContactId::B3ContactId() : values{0, 0, 0} {
}

B3ContactId::B3ContactId(long value0, long value1, long value2) : values{static_cast<uint32_t>(value0), static_cast<uint32_t>(value1), static_cast<uint32_t>(value2)} {
}

B3ContactId::B3ContactId(b3ContactId id) : values{0, 0, 0} {
    b3StoreContactId(id, values);
}

long B3ContactId::GetValue0() const {
    return static_cast<long>(values[0]);
}

long B3ContactId::GetValue1() const {
    return static_cast<long>(values[1]);
}

long B3ContactId::GetValue2() const {
    return static_cast<long>(values[2]);
}

bool B3ContactId::IsNull() const {
    return values[0] == 0;
}

b3ContactId B3ContactId::Load() const {
    uint32_t copy[3] = {values[0], values[1], values[2]};
    return b3LoadContactId(copy);
}

B3RayResult::B3RayResult() : value{} {
}

B3RayResult::B3RayResult(b3RayResult value) : value(value) {
}

long long B3RayResult::GetShapeId() const {
    return static_cast<long long>(b3StoreShapeId(value.shapeId));
}

B3Vec3 B3RayResult::GetPoint() const {
    return B3Vec3(value.point);
}

B3Vec3 B3RayResult::GetNormal() const {
    return B3Vec3(value.normal);
}

long long B3RayResult::GetUserMaterialId() const {
    return static_cast<long long>(value.userMaterialId);
}

float B3RayResult::GetFraction() const {
    return value.fraction;
}

int B3RayResult::GetTriangleIndex() const {
    return value.triangleIndex;
}

int B3RayResult::GetChildIndex() const {
    return value.childIndex;
}

int B3RayResult::GetNodeVisits() const {
    return value.nodeVisits;
}

int B3RayResult::GetLeafVisits() const {
    return value.leafVisits;
}

bool B3RayResult::GetHit() const {
    return value.hit;
}

B3BodyMoveEvent::B3BodyMoveEvent() : bodyId(0), transform(), fellAsleep(false) {
}

B3BodyMoveEvent::B3BodyMoveEvent(const b3BodyMoveEvent& event)
    : bodyId(static_cast<long long>(b3StoreBodyId(event.bodyId))), transform(event.transform), fellAsleep(event.fellAsleep) {
}

long long B3BodyMoveEvent::GetBodyId() const {
    return bodyId;
}

B3Transform B3BodyMoveEvent::GetTransform() const {
    return transform;
}

bool B3BodyMoveEvent::GetFellAsleep() const {
    return fellAsleep;
}

B3BodyEvents::B3BodyEvents() {
}

B3BodyEvents::B3BodyEvents(const b3BodyEvents& events) {
    moveEvents.reserve(static_cast<size_t>(events.moveCount));
    for(int i = 0; i < events.moveCount; i++) {
        moveEvents.push_back(B3BodyMoveEvent(events.moveEvents[i]));
    }
}

int B3BodyEvents::GetMoveCount() const {
    return static_cast<int>(moveEvents.size());
}

B3BodyMoveEvent B3BodyEvents::GetMoveEvent(int index) const {
    if(index < 0 || index >= static_cast<int>(moveEvents.size())) {
        return B3BodyMoveEvent();
    }
    return moveEvents[static_cast<size_t>(index)];
}

B3JointEvent::B3JointEvent() : jointId(0) {
}

B3JointEvent::B3JointEvent(const b3JointEvent& event)
    : jointId(static_cast<long long>(b3StoreJointId(event.jointId))) {
}

long long B3JointEvent::GetJointId() const { return jointId; }

B3JointEvents::B3JointEvents() {
}

B3JointEvents::B3JointEvents(const b3JointEvents& events) {
    jointEvents.reserve(static_cast<size_t>(events.count));
    for(int i = 0; i < events.count; ++i) {
        jointEvents.emplace_back(events.jointEvents[i]);
    }
}

int B3JointEvents::GetCount() const { return static_cast<int>(jointEvents.size()); }

B3JointEvent B3JointEvents::GetEvent(int index) const {
    if(index < 0 || index >= static_cast<int>(jointEvents.size())) {
        return B3JointEvent();
    }
    return jointEvents[static_cast<size_t>(index)];
}

B3SensorBeginTouchEvent::B3SensorBeginTouchEvent() : sensorShapeId(0), visitorShapeId(0) {
}

B3SensorBeginTouchEvent::B3SensorBeginTouchEvent(const b3SensorBeginTouchEvent& event)
    : sensorShapeId(static_cast<long long>(b3StoreShapeId(event.sensorShapeId))), visitorShapeId(static_cast<long long>(b3StoreShapeId(event.visitorShapeId))) {
}

long long B3SensorBeginTouchEvent::GetSensorShapeId() const {
    return sensorShapeId;
}

long long B3SensorBeginTouchEvent::GetVisitorShapeId() const {
    return visitorShapeId;
}

B3SensorEndTouchEvent::B3SensorEndTouchEvent() : sensorShapeId(0), visitorShapeId(0) {
}

B3SensorEndTouchEvent::B3SensorEndTouchEvent(const b3SensorEndTouchEvent& event)
    : sensorShapeId(static_cast<long long>(b3StoreShapeId(event.sensorShapeId))), visitorShapeId(static_cast<long long>(b3StoreShapeId(event.visitorShapeId))) {
}

long long B3SensorEndTouchEvent::GetSensorShapeId() const {
    return sensorShapeId;
}

long long B3SensorEndTouchEvent::GetVisitorShapeId() const {
    return visitorShapeId;
}

B3SensorEvents::B3SensorEvents() {
}

B3SensorEvents::B3SensorEvents(const b3SensorEvents& events) {
    beginEvents.reserve(static_cast<size_t>(events.beginCount));
    for(int i = 0; i < events.beginCount; i++) {
        beginEvents.push_back(B3SensorBeginTouchEvent(events.beginEvents[i]));
    }
    endEvents.reserve(static_cast<size_t>(events.endCount));
    for(int i = 0; i < events.endCount; i++) {
        endEvents.push_back(B3SensorEndTouchEvent(events.endEvents[i]));
    }
}

int B3SensorEvents::GetBeginCount() const {
    return static_cast<int>(beginEvents.size());
}

B3SensorBeginTouchEvent B3SensorEvents::GetBeginEvent(int index) const {
    if(index < 0 || index >= static_cast<int>(beginEvents.size())) {
        return B3SensorBeginTouchEvent();
    }
    return beginEvents[static_cast<size_t>(index)];
}

int B3SensorEvents::GetEndCount() const {
    return static_cast<int>(endEvents.size());
}

B3SensorEndTouchEvent B3SensorEvents::GetEndEvent(int index) const {
    if(index < 0 || index >= static_cast<int>(endEvents.size())) {
        return B3SensorEndTouchEvent();
    }
    return endEvents[static_cast<size_t>(index)];
}

B3ContactBeginTouchEvent::B3ContactBeginTouchEvent() : shapeIdA(0), shapeIdB(0), contactId() {
}

B3ContactBeginTouchEvent::B3ContactBeginTouchEvent(const b3ContactBeginTouchEvent& event)
    : shapeIdA(static_cast<long long>(b3StoreShapeId(event.shapeIdA))), shapeIdB(static_cast<long long>(b3StoreShapeId(event.shapeIdB))), contactId(event.contactId) {
}

long long B3ContactBeginTouchEvent::GetShapeIdA() const {
    return shapeIdA;
}

long long B3ContactBeginTouchEvent::GetShapeIdB() const {
    return shapeIdB;
}

B3ContactId B3ContactBeginTouchEvent::GetContactId() const {
    return contactId;
}

B3ContactEndTouchEvent::B3ContactEndTouchEvent() : shapeIdA(0), shapeIdB(0), contactId() {
}

B3ContactEndTouchEvent::B3ContactEndTouchEvent(const b3ContactEndTouchEvent& event)
    : shapeIdA(static_cast<long long>(b3StoreShapeId(event.shapeIdA))), shapeIdB(static_cast<long long>(b3StoreShapeId(event.shapeIdB))), contactId(event.contactId) {
}

long long B3ContactEndTouchEvent::GetShapeIdA() const {
    return shapeIdA;
}

long long B3ContactEndTouchEvent::GetShapeIdB() const {
    return shapeIdB;
}

B3ContactId B3ContactEndTouchEvent::GetContactId() const {
    return contactId;
}

B3ContactHitEvent::B3ContactHitEvent()
    : shapeIdA(0), shapeIdB(0), contactId(), point(), normal(), approachSpeed(0.0f), userMaterialIdA(0), userMaterialIdB(0) {
}

B3ContactHitEvent::B3ContactHitEvent(const b3ContactHitEvent& event)
    : shapeIdA(static_cast<long long>(b3StoreShapeId(event.shapeIdA))),
      shapeIdB(static_cast<long long>(b3StoreShapeId(event.shapeIdB))),
      contactId(event.contactId),
      point(event.point),
      normal(event.normal),
      approachSpeed(event.approachSpeed),
      userMaterialIdA(static_cast<long long>(event.userMaterialIdA)),
      userMaterialIdB(static_cast<long long>(event.userMaterialIdB)) {
}

long long B3ContactHitEvent::GetShapeIdA() const {
    return shapeIdA;
}

long long B3ContactHitEvent::GetShapeIdB() const {
    return shapeIdB;
}

B3ContactId B3ContactHitEvent::GetContactId() const {
    return contactId;
}

B3Vec3 B3ContactHitEvent::GetPoint() const {
    return point;
}

B3Vec3 B3ContactHitEvent::GetNormal() const {
    return normal;
}

float B3ContactHitEvent::GetApproachSpeed() const {
    return approachSpeed;
}

long long B3ContactHitEvent::GetUserMaterialIdA() const {
    return userMaterialIdA;
}

long long B3ContactHitEvent::GetUserMaterialIdB() const {
    return userMaterialIdB;
}

B3ContactEvents::B3ContactEvents() {
}

B3ContactEvents::B3ContactEvents(const b3ContactEvents& events) {
    beginEvents.reserve(static_cast<size_t>(events.beginCount));
    for(int i = 0; i < events.beginCount; i++) {
        beginEvents.push_back(B3ContactBeginTouchEvent(events.beginEvents[i]));
    }
    endEvents.reserve(static_cast<size_t>(events.endCount));
    for(int i = 0; i < events.endCount; i++) {
        endEvents.push_back(B3ContactEndTouchEvent(events.endEvents[i]));
    }
    hitEvents.reserve(static_cast<size_t>(events.hitCount));
    for(int i = 0; i < events.hitCount; i++) {
        hitEvents.push_back(B3ContactHitEvent(events.hitEvents[i]));
    }
}

int B3ContactEvents::GetBeginCount() const {
    return static_cast<int>(beginEvents.size());
}

B3ContactBeginTouchEvent B3ContactEvents::GetBeginEvent(int index) const {
    if(index < 0 || index >= static_cast<int>(beginEvents.size())) {
        return B3ContactBeginTouchEvent();
    }
    return beginEvents[static_cast<size_t>(index)];
}

int B3ContactEvents::GetEndCount() const {
    return static_cast<int>(endEvents.size());
}

B3ContactEndTouchEvent B3ContactEvents::GetEndEvent(int index) const {
    if(index < 0 || index >= static_cast<int>(endEvents.size())) {
        return B3ContactEndTouchEvent();
    }
    return endEvents[static_cast<size_t>(index)];
}

int B3ContactEvents::GetHitCount() const {
    return static_cast<int>(hitEvents.size());
}

B3ContactHitEvent B3ContactEvents::GetHitEvent(int index) const {
    if(index < 0 || index >= static_cast<int>(hitEvents.size())) {
        return B3ContactHitEvent();
    }
    return hitEvents[static_cast<size_t>(index)];
}

B3Vec3Array::B3Vec3Array(int size) : m_values(static_cast<size_t>(size > 0 ? size : 0)) {
}

int B3Vec3Array::GetSize() const {
    return static_cast<int>(m_values.size());
}

B3Vec3 B3Vec3Array::GetValue(int index) const {
    if(index < 0 || index >= static_cast<int>(m_values.size())) {
        return B3Vec3();
    }
    return B3Vec3(m_values[static_cast<size_t>(index)]);
}

void B3Vec3Array::SetValue(int index, const B3Vec3& value) {
    if(index >= 0 && index < static_cast<int>(m_values.size())) {
        m_values[static_cast<size_t>(index)] = value.value;
    }
}

const b3Vec3* B3Vec3Array::GetData() const {
    return m_values.empty() ? nullptr : m_values.data();
}

B3Hull::B3Hull() : m_hull(nullptr), m_boxHull{}, m_ownsHull(false) {
}

B3Hull::B3Hull(b3HullData* hull) : m_hull(hull), m_boxHull{}, m_ownsHull(true) {
}

B3Hull::B3Hull(b3BoxHull boxHull) : m_hull(nullptr), m_boxHull(boxHull), m_ownsHull(false) {
    m_hull = &m_boxHull.base;
}

B3Hull::~B3Hull() {
    Destroy();
}

B3Hull* B3Hull::CreateBox(float hx, float hy, float hz) {
    return new B3Hull(b3MakeBoxHull(hx, hy, hz));
}

B3Hull* B3Hull::CreateOffsetBox(float hx, float hy, float hz, const B3Vec3& offset) {
    return new B3Hull(b3MakeOffsetBoxHull(hx, hy, hz, offset.value));
}

B3Hull* B3Hull::CreateTransformedBox(float hx, float hy, float hz, const B3Transform& transform) {
    return new B3Hull(b3MakeTransformedBoxHull(hx, hy, hz, transform.value));
}

B3Hull* B3Hull::CreateScaledBox(const B3Vec3& halfWidths, const B3Transform& transform, const B3Vec3& postScale) {
    return new B3Hull(b3MakeScaledBoxHull(halfWidths.value, transform.value, postScale.value));
}

B3Hull* B3Hull::CreateCube(float halfWidth) {
    return new B3Hull(b3MakeCubeHull(halfWidth));
}

B3Hull* B3Hull::CreateCylinder(float height, float radius, float yOffset, int sides) {
    return new B3Hull(b3CreateCylinder(height, radius, yOffset, sides));
}

B3Hull* B3Hull::CreateCone(float height, float radius1, float radius2, int slices) {
    return new B3Hull(b3CreateCone(height, radius1, radius2, slices));
}

B3Hull* B3Hull::CreateRock(float radius) {
    return new B3Hull(b3CreateRock(radius));
}

B3Hull* B3Hull::CreateFromPoints(const B3Vec3Array& points, int maxVertexCount) {
    int pointCount = points.GetSize();
    if(pointCount < 4 || maxVertexCount < 4) {
        return new B3Hull();
    }
    return new B3Hull(b3CreateHull(points.GetData(), pointCount, maxVertexCount));
}

B3Hull* B3Hull::CloneAndTransform(const B3Hull& hull, const B3Transform& transform, const B3Vec3& scale) {
    const b3HullData* source = hull.GetHandle();
    if(source == nullptr) {
        return new B3Hull();
    }
    return new B3Hull(b3CloneAndTransformHull(source, transform.value, scale.value));
}

bool B3Hull::IsValid() const {
    return m_hull != nullptr;
}

void B3Hull::Destroy() {
    if(m_hull != nullptr && m_ownsHull) {
        b3DestroyHull(m_hull);
    }
    m_hull = nullptr;
    m_ownsHull = false;
}

int B3Hull::GetVertexCount() const {
    return m_hull != nullptr ? m_hull->vertexCount : 0;
}

int B3Hull::GetFaceCount() const {
    return m_hull != nullptr ? m_hull->faceCount : 0;
}

B3Vec3 B3Hull::GetPoint(int index) const {
    if(m_hull == nullptr || index < 0 || index >= m_hull->vertexCount) {
        return B3Vec3(b3Vec3_zero);
    }
    return B3Vec3(b3GetHullPoints(m_hull)[index]);
}

const b3HullData* B3Hull::GetHandle() const {
    return m_hull;
}

B3ShapeProxy::B3ShapeProxy(const B3Vec3Array& points, int count, float radius) : m_proxy{} {
    int sourceCount = points.GetSize();
    int copyCount = count < sourceCount ? count : sourceCount;
    if(copyCount < 0) {
        copyCount = 0;
    }
    const b3Vec3* data = points.GetData();
    if(data != nullptr && copyCount > 0) {
        m_points.assign(data, data + copyCount);
    }
    m_proxy.points = m_points.empty() ? nullptr : m_points.data();
    m_proxy.count = static_cast<int>(m_points.size());
    m_proxy.radius = radius;
}

B3ShapeProxy::B3ShapeProxy(const B3Hull& hull, float radius) : m_proxy{} {
    const b3HullData* hullData = hull.GetHandle();
    if(hullData != nullptr && hullData->vertexCount > 0) {
        const b3Vec3* points = b3GetHullPoints(hullData);
        m_points.assign(points, points + hullData->vertexCount);
    }
    m_proxy.points = m_points.empty() ? nullptr : m_points.data();
    m_proxy.count = static_cast<int>(m_points.size());
    m_proxy.radius = radius;
}

int B3ShapeProxy::GetCount() const {
    return m_proxy.count;
}

float B3ShapeProxy::GetRadius() const {
    return m_proxy.radius;
}

const b3ShapeProxy& B3ShapeProxy::GetHandle() const {
    return m_proxy;
}

B3LocalManifoldPoint::B3LocalManifoldPoint() : m_point{} {
}

B3LocalManifoldPoint::B3LocalManifoldPoint(const b3LocalManifoldPoint& point) : m_point(point) {
}

B3Vec3 B3LocalManifoldPoint::GetPoint() const {
    return B3Vec3(m_point.point);
}

float B3LocalManifoldPoint::GetSeparation() const {
    return m_point.separation;
}

int B3LocalManifoldPoint::GetOwner1() const {
    return m_point.pair.owner1;
}

int B3LocalManifoldPoint::GetIndex1() const {
    return m_point.pair.index1;
}

int B3LocalManifoldPoint::GetOwner2() const {
    return m_point.pair.owner2;
}

int B3LocalManifoldPoint::GetIndex2() const {
    return m_point.pair.index2;
}

int B3LocalManifoldPoint::GetTriangleIndex() const {
    return m_point.triangleIndex;
}

B3LocalManifold::B3LocalManifold(int capacity)
    : m_points(static_cast<size_t>(capacity > 0 ? capacity : 0)), m_manifold{} {
    m_manifold.points = m_points.empty() ? nullptr : m_points.data();
}

void B3LocalManifold::Clear() {
    m_manifold = {};
    m_manifold.points = m_points.empty() ? nullptr : m_points.data();
}

int B3LocalManifold::GetCapacity() const {
    return static_cast<int>(m_points.size());
}

b3LocalManifold* B3LocalManifold::GetHandle() {
    return &m_manifold;
}

B3Vec3 B3LocalManifold::GetNormal() const {
    return B3Vec3(m_manifold.normal);
}

B3Vec3 B3LocalManifold::GetTriangleNormal() const {
    return B3Vec3(m_manifold.triangleNormal);
}

int B3LocalManifold::GetPointCount() const {
    return m_manifold.pointCount;
}

B3LocalManifoldPoint B3LocalManifold::GetPoint(int index) const {
    if(index < 0 || index >= m_manifold.pointCount || index >= static_cast<int>(m_points.size())) {
        return B3LocalManifoldPoint();
    }
    return B3LocalManifoldPoint(m_points[static_cast<size_t>(index)]);
}

int B3LocalManifold::GetFeature() const {
    return static_cast<int>(m_manifold.feature);
}

int B3LocalManifold::GetTriangleIndex() const {
    return m_manifold.triangleIndex;
}

B3DistanceOutput::B3DistanceOutput() : m_output{} {
}

B3DistanceOutput::B3DistanceOutput(const b3DistanceOutput& output) : m_output(output) {
}

B3Vec3 B3DistanceOutput::GetPointA() const { return B3Vec3(m_output.pointA); }
B3Vec3 B3DistanceOutput::GetPointB() const { return B3Vec3(m_output.pointB); }
B3Vec3 B3DistanceOutput::GetNormal() const { return B3Vec3(m_output.normal); }
float B3DistanceOutput::GetDistance() const { return m_output.distance; }
int B3DistanceOutput::GetIterations() const { return m_output.iterations; }
int B3DistanceOutput::GetSimplexCount() const { return m_output.simplexCount; }

B3CastOutput::B3CastOutput() : m_output{} {
}

B3CastOutput::B3CastOutput(const b3CastOutput& output) : m_output(output) {
}

B3Vec3 B3CastOutput::GetNormal() const { return B3Vec3(m_output.normal); }
B3Vec3 B3CastOutput::GetPoint() const { return B3Vec3(m_output.point); }
float B3CastOutput::GetFraction() const { return m_output.fraction; }
int B3CastOutput::GetIterations() const { return m_output.iterations; }
bool B3CastOutput::GetHit() const { return m_output.hit; }

B3Sweep::B3Sweep() : m_sweep{} {
    m_sweep.q1 = b3Quat_identity;
    m_sweep.q2 = b3Quat_identity;
}

B3Vec3 B3Sweep::GetLocalCenter() const { return B3Vec3(m_sweep.localCenter); }
void B3Sweep::SetLocalCenter(const B3Vec3& center) { m_sweep.localCenter = center.value; }
B3Vec3 B3Sweep::GetC1() const { return B3Vec3(m_sweep.c1); }
void B3Sweep::SetC1(const B3Vec3& center) { m_sweep.c1 = center.value; }
B3Vec3 B3Sweep::GetC2() const { return B3Vec3(m_sweep.c2); }
void B3Sweep::SetC2(const B3Vec3& center) { m_sweep.c2 = center.value; }
B3Quat B3Sweep::GetQ1() const { return B3Quat(m_sweep.q1); }
void B3Sweep::SetQ1(const B3Quat& rotation) { m_sweep.q1 = rotation.value; }
B3Quat B3Sweep::GetQ2() const { return B3Quat(m_sweep.q2); }
void B3Sweep::SetQ2(const B3Quat& rotation) { m_sweep.q2 = rotation.value; }
B3Transform B3Sweep::GetTransform(float fraction) const { return B3Transform(b3GetSweepTransform(&m_sweep, fraction)); }

B3TOIOutput::B3TOIOutput() : m_output{} {
}

B3TOIOutput::B3TOIOutput(const b3TOIOutput& output) : m_output(output) {
}

int B3TOIOutput::GetState() const { return static_cast<int>(m_output.state); }
B3Vec3 B3TOIOutput::GetPoint() const { return B3Vec3(m_output.point); }
B3Vec3 B3TOIOutput::GetNormal() const { return B3Vec3(m_output.normal); }
float B3TOIOutput::GetFraction() const { return m_output.fraction; }
float B3TOIOutput::GetDistance() const { return m_output.distance; }
int B3TOIOutput::GetDistanceIterations() const { return m_output.distanceIterations; }
int B3TOIOutput::GetPushBackIterations() const { return m_output.pushBackIterations; }
int B3TOIOutput::GetRootIterations() const { return m_output.rootIterations; }
bool B3TOIOutput::GetUsedFallback() const { return m_output.usedFallback; }

B3MoverPlaneResult::B3MoverPlaneResult() : m_result{} {
}

B3MoverPlaneResult::B3MoverPlaneResult(const b3BodyPlaneResult& result) : m_result(result) {
}

long long B3MoverPlaneResult::GetShapeId() const {
    return static_cast<long long>(b3StoreShapeId(m_result.shapeId));
}

B3Vec3 B3MoverPlaneResult::GetNormal() const {
    return B3Vec3(m_result.result.plane.normal);
}

float B3MoverPlaneResult::GetOffset() const {
    return m_result.result.plane.offset;
}

B3Vec3 B3MoverPlaneResult::GetPoint() const {
    return B3Vec3(m_result.result.point);
}

B3MoverCollision::B3MoverCollision() {
}

int B3MoverCollision::GetCount() const {
    return static_cast<int>(m_results.size());
}

B3MoverPlaneResult B3MoverCollision::GetResult(int index) const {
    if(index < 0 || index >= static_cast<int>(m_results.size())) {
        return B3MoverPlaneResult();
    }
    return B3MoverPlaneResult(m_results[static_cast<size_t>(index)]);
}

B3PlaneSolverResult::B3PlaneSolverResult() : m_result{} {
}

B3PlaneSolverResult::B3PlaneSolverResult(const b3PlaneSolverResult& result) : m_result(result) {
}

B3Vec3 B3PlaneSolverResult::GetDelta() const {
    return B3Vec3(m_result.delta);
}

int B3PlaneSolverResult::GetIterationCount() const {
    return m_result.iterationCount;
}

static const b3Vec3* getTriangle(const B3Vec3Array& triangle) {
    return triangle.GetSize() >= 3 ? triangle.GetData() : nullptr;
}

static b3RayCastInput makeRayCastInput(const B3Vec3& origin, const B3Vec3& translation, float maxFraction) {
    b3RayCastInput input{};
    input.origin = origin.value;
    input.translation = translation.value;
    input.maxFraction = maxFraction;
    return input;
}

static b3ShapeCastInput makeShapeCastInput(const B3ShapeProxy& proxy, const B3Vec3& translation,
                                            float maxFraction, bool canEncroach) {
    b3ShapeCastInput input{};
    input.proxy = proxy.GetHandle();
    input.translation = translation.value;
    input.maxFraction = maxFraction;
    input.canEncroach = canEncroach;
    return input;
}

static b3Mesh makeMesh(const B3Mesh& mesh, const B3Vec3& scale) {
    b3Mesh value{};
    value.data = mesh.GetHandle();
    value.scale = scale.value;
    return value;
}

void B3Collision::ScaleBox(B3Vec3& halfWidths, B3Transform& transform, const B3Vec3& postScale,
                           float minHalfWidth) {
    b3ScaleBox(&halfWidths.value, &transform.value, postScale.value, minHalfWidth);
}

B3MassData B3Collision::ComputeSphereMass(const B3Sphere& sphere, float density) {
    return B3MassData(b3ComputeSphereMass(&sphere.value, density));
}

B3MassData B3Collision::ComputeCapsuleMass(const B3Capsule& capsule, float density) {
    return B3MassData(b3ComputeCapsuleMass(&capsule.value, density));
}

B3MassData B3Collision::ComputeHullMass(const B3Hull& hull, float density) {
    const b3HullData* data = hull.GetHandle();
    return data != nullptr ? B3MassData(b3ComputeHullMass(data, density)) : B3MassData();
}

B3AABB B3Collision::ComputeSphereAABB(const B3Sphere& sphere, const B3Transform& transform) {
    return B3AABB(b3ComputeSphereAABB(&sphere.value, transform.value));
}

B3AABB B3Collision::ComputeCapsuleAABB(const B3Capsule& capsule, const B3Transform& transform) {
    return B3AABB(b3ComputeCapsuleAABB(&capsule.value, transform.value));
}

B3AABB B3Collision::ComputeHullAABB(const B3Hull& hull, const B3Transform& transform) {
    const b3HullData* data = hull.GetHandle();
    return data != nullptr ? B3AABB(b3ComputeHullAABB(data, transform.value)) : B3AABB();
}

B3AABB B3Collision::ComputeMeshAABB(const B3Mesh& mesh, const B3Transform& transform, const B3Vec3& scale) {
    const b3MeshData* data = mesh.GetHandle();
    return data != nullptr ? B3AABB(b3ComputeMeshAABB(data, transform.value, scale.value)) : B3AABB();
}

B3AABB B3Collision::ComputeHeightFieldAABB(const B3HeightField& heightField, const B3Transform& transform) {
    const b3HeightFieldData* data = heightField.GetHandle();
    return data != nullptr ? B3AABB(b3ComputeHeightFieldAABB(data, transform.value)) : B3AABB();
}

B3AABB B3Collision::ComputeCompoundAABB(const B3Compound& compound, const B3Transform& transform) {
    const b3CompoundData* data = compound.GetHandle();
    return data != nullptr ? B3AABB(b3ComputeCompoundAABB(data, transform.value)) : B3AABB();
}

bool B3Collision::OverlapSphere(const B3Sphere& sphere, const B3Transform& transform,
                                const B3ShapeProxy& proxy) {
    b3ShapeProxy nativeProxy = proxy.GetHandle();
    return b3OverlapSphere(&sphere.value, transform.value, &nativeProxy);
}

bool B3Collision::OverlapCapsule(const B3Capsule& capsule, const B3Transform& transform,
                                 const B3ShapeProxy& proxy) {
    b3ShapeProxy nativeProxy = proxy.GetHandle();
    return b3OverlapCapsule(&capsule.value, transform.value, &nativeProxy);
}

bool B3Collision::OverlapHull(const B3Hull& hull, const B3Transform& transform,
                              const B3ShapeProxy& proxy) {
    const b3HullData* data = hull.GetHandle();
    b3ShapeProxy nativeProxy = proxy.GetHandle();
    return data != nullptr && b3OverlapHull(data, transform.value, &nativeProxy);
}

bool B3Collision::OverlapMesh(const B3Mesh& mesh, const B3Vec3& scale, const B3Transform& transform,
                              const B3ShapeProxy& proxy) {
    b3Mesh nativeMesh = makeMesh(mesh, scale);
    b3ShapeProxy nativeProxy = proxy.GetHandle();
    return nativeMesh.data != nullptr && b3OverlapMesh(&nativeMesh, transform.value, &nativeProxy);
}

bool B3Collision::OverlapHeightField(const B3HeightField& heightField, const B3Transform& transform,
                                     const B3ShapeProxy& proxy) {
    const b3HeightFieldData* data = heightField.GetHandle();
    b3ShapeProxy nativeProxy = proxy.GetHandle();
    return data != nullptr && b3OverlapHeightField(data, transform.value, &nativeProxy);
}

bool B3Collision::OverlapCompound(const B3Compound& compound, const B3Transform& transform,
                                  const B3ShapeProxy& proxy) {
    const b3CompoundData* data = compound.GetHandle();
    b3ShapeProxy nativeProxy = proxy.GetHandle();
    return data != nullptr && b3OverlapCompound(data, transform.value, &nativeProxy);
}

B3CastOutput B3Collision::RayCastSphere(const B3Sphere& sphere, const B3Vec3& origin,
                                        const B3Vec3& translation, float maxFraction) {
    b3RayCastInput input = makeRayCastInput(origin, translation, maxFraction);
    return B3CastOutput(b3RayCastSphere(&sphere.value, &input));
}

B3CastOutput B3Collision::RayCastHollowSphere(const B3Sphere& sphere, const B3Vec3& origin,
                                              const B3Vec3& translation, float maxFraction) {
    b3RayCastInput input = makeRayCastInput(origin, translation, maxFraction);
    return B3CastOutput(b3RayCastHollowSphere(&sphere.value, &input));
}

B3CastOutput B3Collision::RayCastCapsule(const B3Capsule& capsule, const B3Vec3& origin,
                                         const B3Vec3& translation, float maxFraction) {
    b3RayCastInput input = makeRayCastInput(origin, translation, maxFraction);
    return B3CastOutput(b3RayCastCapsule(&capsule.value, &input));
}

B3CastOutput B3Collision::RayCastHull(const B3Hull& hull, const B3Vec3& origin,
                                      const B3Vec3& translation, float maxFraction) {
    const b3HullData* data = hull.GetHandle();
    if(data == nullptr) {
        return B3CastOutput();
    }
    b3RayCastInput input = makeRayCastInput(origin, translation, maxFraction);
    return B3CastOutput(b3RayCastHull(data, &input));
}

B3CastOutput B3Collision::RayCastMesh(const B3Mesh& mesh, const B3Vec3& scale, const B3Vec3& origin,
                                      const B3Vec3& translation, float maxFraction) {
    b3Mesh nativeMesh = makeMesh(mesh, scale);
    if(nativeMesh.data == nullptr) {
        return B3CastOutput();
    }
    b3RayCastInput input = makeRayCastInput(origin, translation, maxFraction);
    return B3CastOutput(b3RayCastMesh(&nativeMesh, &input));
}

B3CastOutput B3Collision::RayCastHeightField(const B3HeightField& heightField, const B3Vec3& origin,
                                             const B3Vec3& translation, float maxFraction) {
    const b3HeightFieldData* data = heightField.GetHandle();
    if(data == nullptr) {
        return B3CastOutput();
    }
    b3RayCastInput input = makeRayCastInput(origin, translation, maxFraction);
    return B3CastOutput(b3RayCastHeightField(data, &input));
}

B3CastOutput B3Collision::RayCastCompound(const B3Compound& compound, const B3Vec3& origin,
                                          const B3Vec3& translation, float maxFraction) {
    const b3CompoundData* data = compound.GetHandle();
    if(data == nullptr) {
        return B3CastOutput();
    }
    b3RayCastInput input = makeRayCastInput(origin, translation, maxFraction);
    return B3CastOutput(b3RayCastCompound(data, &input));
}

B3CastOutput B3Collision::ShapeCastSphere(const B3Sphere& sphere, const B3ShapeProxy& proxy,
                                          const B3Vec3& translation, float maxFraction, bool canEncroach) {
    b3ShapeCastInput input = makeShapeCastInput(proxy, translation, maxFraction, canEncroach);
    return B3CastOutput(b3ShapeCastSphere(&sphere.value, &input));
}

B3CastOutput B3Collision::ShapeCastCapsule(const B3Capsule& capsule, const B3ShapeProxy& proxy,
                                           const B3Vec3& translation, float maxFraction, bool canEncroach) {
    b3ShapeCastInput input = makeShapeCastInput(proxy, translation, maxFraction, canEncroach);
    return B3CastOutput(b3ShapeCastCapsule(&capsule.value, &input));
}

B3CastOutput B3Collision::ShapeCastHull(const B3Hull& hull, const B3ShapeProxy& proxy,
                                        const B3Vec3& translation, float maxFraction, bool canEncroach) {
    const b3HullData* data = hull.GetHandle();
    if(data == nullptr) {
        return B3CastOutput();
    }
    b3ShapeCastInput input = makeShapeCastInput(proxy, translation, maxFraction, canEncroach);
    return B3CastOutput(b3ShapeCastHull(data, &input));
}

B3CastOutput B3Collision::ShapeCastMesh(const B3Mesh& mesh, const B3Vec3& scale,
                                        const B3ShapeProxy& proxy, const B3Vec3& translation,
                                        float maxFraction, bool canEncroach) {
    b3Mesh nativeMesh = makeMesh(mesh, scale);
    if(nativeMesh.data == nullptr) {
        return B3CastOutput();
    }
    b3ShapeCastInput input = makeShapeCastInput(proxy, translation, maxFraction, canEncroach);
    return B3CastOutput(b3ShapeCastMesh(&nativeMesh, &input));
}

B3CastOutput B3Collision::ShapeCastHeightField(const B3HeightField& heightField,
                                               const B3ShapeProxy& proxy, const B3Vec3& translation,
                                               float maxFraction, bool canEncroach) {
    const b3HeightFieldData* data = heightField.GetHandle();
    if(data == nullptr) {
        return B3CastOutput();
    }
    b3ShapeCastInput input = makeShapeCastInput(proxy, translation, maxFraction, canEncroach);
    return B3CastOutput(b3ShapeCastHeightField(data, &input));
}

B3CastOutput B3Collision::ShapeCastCompound(const B3Compound& compound, const B3ShapeProxy& proxy,
                                            const B3Vec3& translation, float maxFraction, bool canEncroach) {
    const b3CompoundData* data = compound.GetHandle();
    if(data == nullptr) {
        return B3CastOutput();
    }
    b3ShapeCastInput input = makeShapeCastInput(proxy, translation, maxFraction, canEncroach);
    return B3CastOutput(b3ShapeCastCompound(data, &input));
}

B3LocalManifold* B3Collision::CollideSpheres(int capacity, const B3Sphere& sphereA, const B3Sphere& sphereB,
                                              const B3Transform& transformBtoA) {
    B3LocalManifold* manifold = new B3LocalManifold(capacity);
    b3CollideSpheres(manifold->GetHandle(), manifold->GetCapacity(), &sphereA.value, &sphereB.value, transformBtoA.value);
    return manifold;
}

B3LocalManifold* B3Collision::CollideCapsuleAndSphere(int capacity, const B3Capsule& capsuleA,
                                                       const B3Sphere& sphereB, const B3Transform& transformBtoA) {
    B3LocalManifold* manifold = new B3LocalManifold(capacity);
    b3CollideCapsuleAndSphere(manifold->GetHandle(), manifold->GetCapacity(), &capsuleA.value, &sphereB.value,
                              transformBtoA.value);
    return manifold;
}

B3LocalManifold* B3Collision::CollideHullAndSphere(int capacity, const B3Hull& hullA, const B3Sphere& sphereB,
                                                    const B3Transform& transformBtoA) {
    B3LocalManifold* manifold = new B3LocalManifold(capacity);
    b3SimplexCache cache{};
    const b3HullData* hull = hullA.GetHandle();
    if(hull != nullptr) {
        b3CollideHullAndSphere(manifold->GetHandle(), manifold->GetCapacity(), hull, &sphereB.value,
                               transformBtoA.value, &cache);
    }
    return manifold;
}

B3LocalManifold* B3Collision::CollideCapsules(int capacity, const B3Capsule& capsuleA,
                                               const B3Capsule& capsuleB, const B3Transform& transformBtoA) {
    B3LocalManifold* manifold = new B3LocalManifold(capacity);
    b3CollideCapsules(manifold->GetHandle(), manifold->GetCapacity(), &capsuleA.value, &capsuleB.value,
                      transformBtoA.value);
    return manifold;
}

B3LocalManifold* B3Collision::CollideHullAndCapsule(int capacity, const B3Hull& hullA,
                                                     const B3Capsule& capsuleB, const B3Transform& transformBtoA) {
    B3LocalManifold* manifold = new B3LocalManifold(capacity);
    b3SimplexCache cache{};
    const b3HullData* hull = hullA.GetHandle();
    if(hull != nullptr) {
        b3CollideHullAndCapsule(manifold->GetHandle(), manifold->GetCapacity(), hull, &capsuleB.value,
                                transformBtoA.value, &cache);
    }
    return manifold;
}

B3LocalManifold* B3Collision::CollideHulls(int capacity, const B3Hull& hullA, const B3Hull& hullB,
                                            const B3Transform& transformBtoA) {
    B3LocalManifold* manifold = new B3LocalManifold(capacity);
    b3SATCache cache{};
    const b3HullData* a = hullA.GetHandle();
    const b3HullData* b = hullB.GetHandle();
    if(a != nullptr && b != nullptr) {
        b3CollideHulls(manifold->GetHandle(), manifold->GetCapacity(), a, b, transformBtoA.value, &cache);
    }
    return manifold;
}

B3LocalManifold* B3Collision::CollideTriangleAndSphere(int capacity, const B3Vec3Array& triangleA,
                                                        const B3Sphere& sphereB) {
    B3LocalManifold* manifold = new B3LocalManifold(capacity);
    const b3Vec3* triangle = getTriangle(triangleA);
    if(triangle != nullptr) {
        b3CollideTriangleAndSphere(manifold->GetHandle(), manifold->GetCapacity(), triangle, &sphereB.value);
    }
    return manifold;
}

B3LocalManifold* B3Collision::CollideTriangleAndCapsule(int capacity, const B3Vec3Array& triangleA,
                                                         const B3Capsule& capsuleB) {
    B3LocalManifold* manifold = new B3LocalManifold(capacity);
    const b3Vec3* triangle = getTriangle(triangleA);
    if(triangle != nullptr) {
        b3SimplexCache cache{};
        b3CollideTriangleAndCapsule(manifold->GetHandle(), manifold->GetCapacity(), triangle, &capsuleB.value, &cache);
    }
    return manifold;
}

B3LocalManifold* B3Collision::CollideTriangleAndHull(int capacity, const B3Vec3Array& triangleA,
                                                      int triangleFlags, const B3Hull& hullB,
                                                      bool enableSpeculative) {
    B3LocalManifold* manifold = new B3LocalManifold(capacity);
    const b3Vec3* triangle = getTriangle(triangleA);
    const b3HullData* hull = hullB.GetHandle();
    if(triangle != nullptr && hull != nullptr) {
        b3SATCache cache{};
        b3CollideTriangleAndHull(manifold->GetHandle(), manifold->GetCapacity(), triangle[0], triangle[1], triangle[2],
                                 triangleFlags, hull, &cache, enableSpeculative);
    }
    return manifold;
}

B3DistanceOutput B3Collision::ShapeDistance(const B3ShapeProxy& proxyA, const B3ShapeProxy& proxyB,
                                              const B3Transform& transformBtoA, bool useRadii) {
    b3DistanceInput input{};
    input.proxyA = proxyA.GetHandle();
    input.proxyB = proxyB.GetHandle();
    input.transform = transformBtoA.value;
    input.useRadii = useRadii;
    b3SimplexCache cache{};
    return B3DistanceOutput(b3ShapeDistance(&input, &cache, nullptr, 0));
}

B3CastOutput B3Collision::ShapeCast(const B3ShapeProxy& proxyA, const B3ShapeProxy& proxyB,
                                     const B3Transform& transformBtoA, const B3Vec3& translationB,
                                     float maxFraction, bool canEncroach) {
    b3ShapeCastPairInput input{};
    input.proxyA = proxyA.GetHandle();
    input.proxyB = proxyB.GetHandle();
    input.transform = transformBtoA.value;
    input.translationB = translationB.value;
    input.maxFraction = maxFraction;
    input.canEncroach = canEncroach;
    return B3CastOutput(b3ShapeCast(&input));
}

B3TOIOutput B3Collision::TimeOfImpact(const B3ShapeProxy& proxyA, const B3ShapeProxy& proxyB,
                                       const B3Sweep& sweepA, const B3Sweep& sweepB, float maxFraction) {
    b3TOIInput input{};
    input.proxyA = proxyA.GetHandle();
    input.proxyB = proxyB.GetHandle();
    input.sweepA = sweepA.m_sweep;
    input.sweepB = sweepB.m_sweep;
    input.maxFraction = maxFraction;
    return B3TOIOutput(b3TimeOfImpact(&input));
}

B3PlaneSolverResult B3Collision::SolveMoverPlanes(const B3Vec3& targetDelta,
                                                   const B3MoverCollision& collision) {
    std::vector<b3CollisionPlane> planes;
    planes.reserve(collision.m_results.size());
    for(const b3BodyPlaneResult& result : collision.m_results) {
        planes.push_back(b3CollisionPlane{result.result.plane, std::numeric_limits<float>::max(), 0.0f, true});
    }
    return B3PlaneSolverResult(b3SolvePlanes(targetDelta.value, planes.data(), static_cast<int>(planes.size())));
}

B3Vec3 B3Collision::ClipVectorToMoverPlanes(const B3Vec3& vector, const B3MoverCollision& collision) {
    std::vector<b3CollisionPlane> planes;
    planes.reserve(collision.m_results.size());
    for(const b3BodyPlaneResult& result : collision.m_results) {
        planes.push_back(b3CollisionPlane{result.result.plane, std::numeric_limits<float>::max(), 0.0f, true});
    }
    return B3Vec3(b3ClipVector(vector.value, planes.data(), static_cast<int>(planes.size())));
}

B3MeshDef::B3MeshDef(int vertexCapacity, int triangleCapacity)
    : m_weldTolerance(0.0f), m_weldVertices(false), m_useMedianSplit(false), m_identifyEdges(false) {
    if(vertexCapacity > 0) {
        m_vertices.reserve(static_cast<size_t>(vertexCapacity));
    }
    if(triangleCapacity > 0) {
        m_indices.reserve(static_cast<size_t>(3 * triangleCapacity));
        m_materialIndices.reserve(static_cast<size_t>(triangleCapacity));
    }
}

void B3MeshDef::AddVertex(const B3Vec3& vertex) {
    m_vertices.push_back(vertex.value);
}

void B3MeshDef::AddTriangle(int index1, int index2, int index3, int materialIndex) {
    m_indices.push_back(index1);
    m_indices.push_back(index2);
    m_indices.push_back(index3);
    m_materialIndices.push_back(static_cast<uint8_t>(materialIndex));
}

float B3MeshDef::GetWeldTolerance() const { return m_weldTolerance; }
void B3MeshDef::SetWeldTolerance(float tolerance) { m_weldTolerance = tolerance; }
bool B3MeshDef::GetWeldVertices() const { return m_weldVertices; }
void B3MeshDef::SetWeldVertices(bool enabled) { m_weldVertices = enabled; }
bool B3MeshDef::GetUseMedianSplit() const { return m_useMedianSplit; }
void B3MeshDef::SetUseMedianSplit(bool enabled) { m_useMedianSplit = enabled; }
bool B3MeshDef::GetIdentifyEdges() const { return m_identifyEdges; }
void B3MeshDef::SetIdentifyEdges(bool enabled) { m_identifyEdges = enabled; }
int B3MeshDef::GetVertexCount() const { return static_cast<int>(m_vertices.size()); }
int B3MeshDef::GetTriangleCount() const { return static_cast<int>(m_indices.size() / 3); }

b3MeshData* B3MeshDef::CreateMeshData() const {
    if(m_vertices.size() < 3 || m_indices.size() < 3 || m_indices.size() % 3 != 0) {
        return nullptr;
    }
    b3MeshDef def{};
    def.vertices = const_cast<b3Vec3*>(m_vertices.data());
    def.indices = const_cast<int32_t*>(m_indices.data());
    def.materialIndices = m_materialIndices.empty() ? nullptr : const_cast<uint8_t*>(m_materialIndices.data());
    def.weldTolerance = m_weldTolerance;
    def.vertexCount = static_cast<int>(m_vertices.size());
    def.triangleCount = static_cast<int>(m_indices.size() / 3);
    def.weldVertices = m_weldVertices;
    def.useMedianSplit = m_useMedianSplit;
    def.identifyEdges = m_identifyEdges;
    return b3CreateMesh(&def, nullptr, 0);
}

B3Mesh::B3Mesh() : m_mesh(nullptr) {
}

B3Mesh::B3Mesh(b3MeshData* mesh) : m_mesh(mesh) {
}

B3Mesh::~B3Mesh() {
    Destroy();
}

B3Mesh* B3Mesh::CreateFromDef(const B3MeshDef& def) {
    return new B3Mesh(def.CreateMeshData());
}

B3Mesh* B3Mesh::CreateBox(const B3Vec3& center, const B3Vec3& extents, bool identifyEdges) {
    return new B3Mesh(b3CreateBoxMesh(center.value, extents.value, identifyEdges));
}

B3Mesh* B3Mesh::CreateHollowBox(const B3Vec3& center, const B3Vec3& extents) {
    return new B3Mesh(b3CreateHollowBoxMesh(center.value, extents.value));
}

B3Mesh* B3Mesh::CreatePlatform(const B3Vec3& center, float height, float topWidth, float bottomWidth) {
    return new B3Mesh(b3CreatePlatformMesh(center.value, height, topWidth, bottomWidth));
}

B3Mesh* B3Mesh::CreateGrid(int xCount, int zCount, float cellWidth, int materialCount, bool identifyEdges) {
    return new B3Mesh(b3CreateGridMesh(xCount, zCount, cellWidth, materialCount, identifyEdges));
}

B3Mesh* B3Mesh::CreateWave(int xCount, int zCount, float cellWidth, float amplitude, float rowFrequency,
                           float columnFrequency) {
    return new B3Mesh(b3CreateWaveMesh(xCount, zCount, cellWidth, amplitude, rowFrequency, columnFrequency));
}

B3Mesh* B3Mesh::CreateTorus(int radialResolution, int tubularResolution, float radius, float thickness) {
    return new B3Mesh(b3CreateTorusMesh(radialResolution, tubularResolution, radius, thickness));
}

B3Mesh* B3Mesh::CreateFromObj(const char* objText, float scale, bool zUp, bool useMedianSplit,
                              bool identifyEdges, bool weldVertices, float weldTolerance) {
    if(objText == nullptr) {
        return new B3Mesh();
    }

    tinyobj::ObjReaderConfig config;
    config.triangulate = true;
    tinyobj::ObjReader reader;
    if(reader.ParseFromString(objText, "", config) == false) {
        return new B3Mesh();
    }

    const tinyobj::attrib_t& attrib = reader.GetAttrib();
    const std::vector<tinyobj::shape_t>& shapes = reader.GetShapes();
    B3MeshDef def(static_cast<int>(attrib.vertices.size() / 3), 0);
    def.SetWeldTolerance(weldTolerance);
    def.SetUseMedianSplit(useMedianSplit);
    def.SetIdentifyEdges(identifyEdges);
    def.SetWeldVertices(weldVertices);

    int vertexCount = static_cast<int>(attrib.vertices.size() / 3);
    for(int i = 0; i < vertexCount; ++i) {
        float x = scale * attrib.vertices[3 * i + 0];
        float y = scale * attrib.vertices[3 * i + 1];
        float z = scale * attrib.vertices[3 * i + 2];
        B3Vec3 vertex(zUp ? B3Vec3(y, z, x) : B3Vec3(x, y, z));
        def.AddVertex(vertex);
    }

    int materialIndex = 0;
    for(const tinyobj::shape_t& shape : shapes) {
        size_t baseIndex = 0;
        for(unsigned char faceVertexCount : shape.mesh.num_face_vertices) {
            if(faceVertexCount == 3) {
                int i1 = shape.mesh.indices[baseIndex + 0].vertex_index;
                int i2 = shape.mesh.indices[baseIndex + 1].vertex_index;
                int i3 = shape.mesh.indices[baseIndex + 2].vertex_index;
                def.AddTriangle(i1, i2, i3, materialIndex);
                materialIndex = (materialIndex + 1) % 3;
            }
            baseIndex += faceVertexCount;
        }
    }

    return new B3Mesh(def.CreateMeshData());
}

bool B3Mesh::IsValid() const { return m_mesh != nullptr; }

void B3Mesh::Destroy() {
    if(m_mesh != nullptr) {
        b3DestroyMesh(m_mesh);
        m_mesh = nullptr;
    }
}

int B3Mesh::GetVertexCount() const { return m_mesh != nullptr ? m_mesh->vertexCount : 0; }
int B3Mesh::GetTriangleCount() const { return m_mesh != nullptr ? m_mesh->triangleCount : 0; }
int B3Mesh::GetMaterialCount() const { return m_mesh != nullptr ? m_mesh->materialCount : 0; }
int B3Mesh::GetTriangleMaterialIndex(int triangleIndex) const {
    if(m_mesh == nullptr || triangleIndex < 0 || triangleIndex >= m_mesh->triangleCount) {
        return 0;
    }
    const uint8_t* materialIndices = b3GetMeshMaterialIndices(m_mesh);
    return materialIndices != nullptr ? materialIndices[triangleIndex] : 0;
}
void B3Mesh::SetTriangleMaterialIndex(int triangleIndex, int materialIndex) {
    if(m_mesh == nullptr || triangleIndex < 0 || triangleIndex >= m_mesh->triangleCount) {
        return;
    }
    uint8_t* materialIndices = reinterpret_cast<uint8_t*>(reinterpret_cast<intptr_t>(m_mesh) + m_mesh->materialOffset);
    materialIndices[triangleIndex] = static_cast<uint8_t>(materialIndex);
    m_mesh->materialCount = b3MaxInt(m_mesh->materialCount, materialIndex + 1);
}
const b3MeshData* B3Mesh::GetHandle() const { return m_mesh; }

B3HeightField::B3HeightField() : m_heightField(nullptr) {
}

B3HeightField::B3HeightField(b3HeightFieldData* heightField) : m_heightField(heightField) {
}

B3HeightField::~B3HeightField() {
    Destroy();
}

B3HeightField* B3HeightField::CreateGrid(int rowCount, int columnCount, const B3Vec3& scale, bool makeHoles) {
    return new B3HeightField(b3CreateGrid(rowCount, columnCount, scale.value, makeHoles));
}

B3HeightField* B3HeightField::CreateWave(int rowCount, int columnCount, const B3Vec3& scale, float rowFrequency,
                                         float columnFrequency, bool makeHoles) {
    return new B3HeightField(b3CreateWave(rowCount, columnCount, scale.value, rowFrequency, columnFrequency,
                                          makeHoles));
}

bool B3HeightField::IsValid() const { return m_heightField != nullptr; }

void B3HeightField::Destroy() {
    if(m_heightField != nullptr) {
        b3DestroyHeightField(m_heightField);
        m_heightField = nullptr;
    }
}

const b3HeightFieldData* B3HeightField::GetHandle() const { return m_heightField; }

B3SurfaceMaterialArray::B3SurfaceMaterialArray(int size)
    : m_values(static_cast<size_t>(size > 0 ? size : 0), b3DefaultSurfaceMaterial()) {
}

int B3SurfaceMaterialArray::GetSize() const { return static_cast<int>(m_values.size()); }

B3SurfaceMaterial B3SurfaceMaterialArray::GetValue(int index) const {
    if(index < 0 || index >= static_cast<int>(m_values.size())) {
        return B3SurfaceMaterial();
    }
    return B3SurfaceMaterial(m_values[static_cast<size_t>(index)]);
}

void B3SurfaceMaterialArray::SetValue(int index, const B3SurfaceMaterial& material) {
    if(index >= 0 && index < static_cast<int>(m_values.size())) {
        m_values[static_cast<size_t>(index)] = material.value;
    }
}

const b3SurfaceMaterial* B3SurfaceMaterialArray::GetData() const {
    return m_values.empty() ? nullptr : m_values.data();
}

B3CompoundDef::B3CompoundDef(int capsuleCapacity, int hullCapacity, int meshCapacity, int sphereCapacity) {
    if(capsuleCapacity > 0) m_capsules.reserve(static_cast<size_t>(capsuleCapacity));
    if(hullCapacity > 0) m_hulls.reserve(static_cast<size_t>(hullCapacity));
    if(meshCapacity > 0) m_meshes.reserve(static_cast<size_t>(meshCapacity));
    if(sphereCapacity > 0) m_spheres.reserve(static_cast<size_t>(sphereCapacity));
}

void B3CompoundDef::AddCapsule(const B3Capsule& capsule, const B3SurfaceMaterial& material) {
    m_capsules.push_back({capsule.value, material.value});
}

void B3CompoundDef::AddHull(const B3Hull& hull, const B3Transform& transform, const B3SurfaceMaterial& material) {
    if(hull.GetHandle() != nullptr) {
        m_hulls.push_back({hull.GetHandle(), transform.value, material.value});
    }
}

void B3CompoundDef::AddMesh(const B3Mesh& mesh, const B3Transform& transform, const B3Vec3& scale,
                            const B3SurfaceMaterialArray& materials) {
    if(mesh.GetHandle() == nullptr) {
        return;
    }
    MeshEntry entry;
    entry.meshData = mesh.GetHandle();
    entry.transform = transform.value;
    entry.scale = scale.value;
    const b3SurfaceMaterial* source = materials.GetData();
    if(source != nullptr) {
        entry.materials.assign(source, source + materials.GetSize());
    }
    m_meshes.push_back(entry);
}

void B3CompoundDef::AddSphere(const B3Sphere& sphere, const B3SurfaceMaterial& material) {
    m_spheres.push_back({sphere.value, material.value});
}

int B3CompoundDef::GetCapsuleCount() const { return static_cast<int>(m_capsules.size()); }
int B3CompoundDef::GetHullCount() const { return static_cast<int>(m_hulls.size()); }
int B3CompoundDef::GetMeshCount() const { return static_cast<int>(m_meshes.size()); }
int B3CompoundDef::GetSphereCount() const { return static_cast<int>(m_spheres.size()); }

b3CompoundData* B3CompoundDef::CreateCompoundData() const {
    std::vector<b3CompoundMeshDef> meshes;
    meshes.reserve(m_meshes.size());
    for(const MeshEntry& entry : m_meshes) {
        meshes.push_back({entry.meshData, entry.transform, entry.scale,
                          entry.materials.empty() ? nullptr : entry.materials.data(),
                          static_cast<int>(entry.materials.size())});
    }

    b3CompoundDef def{};
    def.capsules = m_capsules.empty() ? nullptr : const_cast<b3CompoundCapsuleDef*>(m_capsules.data());
    def.capsuleCount = static_cast<int>(m_capsules.size());
    def.hulls = m_hulls.empty() ? nullptr : const_cast<b3CompoundHullDef*>(m_hulls.data());
    def.hullCount = static_cast<int>(m_hulls.size());
    def.meshes = meshes.empty() ? nullptr : meshes.data();
    def.meshCount = static_cast<int>(meshes.size());
    def.spheres = m_spheres.empty() ? nullptr : const_cast<b3CompoundSphereDef*>(m_spheres.data());
    def.sphereCount = static_cast<int>(m_spheres.size());
    return b3CreateCompound(&def);
}

B3Compound::B3Compound() : m_compound(nullptr) {
}

B3Compound::B3Compound(b3CompoundData* compound) : m_compound(compound) {
}

B3Compound::~B3Compound() {
    Destroy();
}

B3Compound* B3Compound::CreateFromDef(const B3CompoundDef& def) {
    return new B3Compound(def.CreateCompoundData());
}

bool B3Compound::IsValid() const { return m_compound != nullptr; }

void B3Compound::Destroy() {
    if(m_compound != nullptr) {
        b3DestroyCompound(m_compound);
        m_compound = nullptr;
    }
}

int B3Compound::GetCapsuleCount() const { return m_compound != nullptr ? m_compound->capsuleCount : 0; }
int B3Compound::GetHullCount() const { return m_compound != nullptr ? m_compound->hullCount : 0; }
int B3Compound::GetMeshCount() const { return m_compound != nullptr ? m_compound->meshCount : 0; }
int B3Compound::GetSphereCount() const { return m_compound != nullptr ? m_compound->sphereCount : 0; }
const b3CompoundData* B3Compound::GetHandle() const { return m_compound; }

B3Body::B3Body() : m_bodyId(b3_nullBodyId) {
}

B3Body::B3Body(long long bodyId) : m_bodyId(loadBodyId(bodyId)) {
}

B3Body::B3Body(b3BodyId bodyId) : m_bodyId(bodyId) {
}

long long B3Body::GetId() const {
    return static_cast<long long>(b3StoreBodyId(m_bodyId));
}

bool B3Body::IsValid() const {
    return b3Body_IsValid(m_bodyId);
}

void B3Body::Destroy() {
    if(IsValid()) {
        b3DestroyBody(m_bodyId);
        m_bodyId = b3_nullBodyId;
    }
}

int B3Body::GetType() const {
    return static_cast<int>(b3Body_GetType(m_bodyId));
}

void B3Body::SetType(int type) {
    b3Body_SetType(m_bodyId, static_cast<b3BodyType>(type));
}

void B3Body::GetName(NativeString& name) const {
    const char* value = b3Body_GetName(m_bodyId);
    name = value != nullptr ? value : "";
}

void B3Body::SetName(const char* name) {
    b3Body_SetName(m_bodyId, name != nullptr ? name : "");
}

B3Vec3 B3Body::GetPosition() const {
    return B3Vec3(b3Body_GetPosition(m_bodyId));
}

B3Quat B3Body::GetRotation() const {
    return B3Quat(b3Body_GetRotation(m_bodyId));
}

B3Transform B3Body::GetTransform() const {
    return B3Transform(b3Body_GetTransform(m_bodyId));
}

B3Vec3 B3Body::GetWorldCenter() const {
    return B3Vec3(b3Body_GetWorldCenter(m_bodyId));
}

B3Vec3 B3Body::GetLocalCenter() const {
    return B3Vec3(b3Body_GetLocalCenter(m_bodyId));
}

B3Vec3 B3Body::GetLocalPoint(const B3Vec3& worldPoint) const {
    return B3Vec3(b3Body_GetLocalPoint(m_bodyId, worldPoint.value));
}

B3Vec3 B3Body::GetWorldPoint(const B3Vec3& localPoint) const {
    return B3Vec3(b3Body_GetWorldPoint(m_bodyId, localPoint.value));
}

B3Vec3 B3Body::GetLocalVector(const B3Vec3& worldVector) const {
    return B3Vec3(b3Body_GetLocalVector(m_bodyId, worldVector.value));
}

B3Vec3 B3Body::GetWorldVector(const B3Vec3& localVector) const {
    return B3Vec3(b3Body_GetWorldVector(m_bodyId, localVector.value));
}

void B3Body::SetTransform(const B3Vec3& position, const B3Quat& rotation) {
    b3Body_SetTransform(m_bodyId, position.value, rotation.value);
}

void B3Body::SetTargetTransform(const B3Vec3& position, const B3Quat& rotation, float timeStep, bool wake) {
    b3WorldTransform target = { position.value, rotation.value };
    b3Body_SetTargetTransform(m_bodyId, target, timeStep, wake);
}

B3Vec3 B3Body::GetLinearVelocity() const {
    return B3Vec3(b3Body_GetLinearVelocity(m_bodyId));
}

void B3Body::SetLinearVelocity(const B3Vec3& velocity) {
    b3Body_SetLinearVelocity(m_bodyId, velocity.value);
}

B3Vec3 B3Body::GetAngularVelocity() const {
    return B3Vec3(b3Body_GetAngularVelocity(m_bodyId));
}

void B3Body::SetAngularVelocity(const B3Vec3& velocity) {
    b3Body_SetAngularVelocity(m_bodyId, velocity.value);
}

B3Vec3 B3Body::GetLocalPointVelocity(const B3Vec3& localPoint) const {
    return B3Vec3(b3Body_GetLocalPointVelocity(m_bodyId, localPoint.value));
}

B3Vec3 B3Body::GetWorldPointVelocity(const B3Vec3& worldPoint) const {
    return B3Vec3(b3Body_GetWorldPointVelocity(m_bodyId, worldPoint.value));
}

void B3Body::ApplyForce(const B3Vec3& force, const B3Vec3& point, bool wake) {
    b3Body_ApplyForce(m_bodyId, force.value, point.value, wake);
}

void B3Body::ApplyForceToCenter(const B3Vec3& force, bool wake) {
    b3Body_ApplyForceToCenter(m_bodyId, force.value, wake);
}

void B3Body::ApplyTorque(const B3Vec3& torque, bool wake) {
    b3Body_ApplyTorque(m_bodyId, torque.value, wake);
}

void B3Body::ApplyLinearImpulse(const B3Vec3& impulse, const B3Vec3& point, bool wake) {
    b3Body_ApplyLinearImpulse(m_bodyId, impulse.value, point.value, wake);
}

void B3Body::ApplyLinearImpulseToCenter(const B3Vec3& impulse, bool wake) {
    b3Body_ApplyLinearImpulseToCenter(m_bodyId, impulse.value, wake);
}

void B3Body::ApplyAngularImpulse(const B3Vec3& impulse, bool wake) {
    b3Body_ApplyAngularImpulse(m_bodyId, impulse.value, wake);
}

float B3Body::GetMass() const {
    return b3Body_GetMass(m_bodyId);
}

float B3Body::GetInverseMass() const {
    return b3Body_GetInverseMass(m_bodyId);
}

void B3Body::ApplyMassFromShapes() {
    b3Body_ApplyMassFromShapes(m_bodyId);
}

B3Vec3 B3Body::GetLocalRotationalInertiaColumnX() const {
    return B3Vec3(b3Body_GetLocalRotationalInertia(m_bodyId).cx);
}

B3Vec3 B3Body::GetLocalRotationalInertiaColumnY() const {
    return B3Vec3(b3Body_GetLocalRotationalInertia(m_bodyId).cy);
}

B3Vec3 B3Body::GetLocalRotationalInertiaColumnZ() const {
    return B3Vec3(b3Body_GetLocalRotationalInertia(m_bodyId).cz);
}

void B3Body::SetMassData(float mass, const B3Vec3& center, const B3Vec3& inertiaColumnX,
                         const B3Vec3& inertiaColumnY, const B3Vec3& inertiaColumnZ) {
    b3MassData massData{};
    massData.mass = mass;
    massData.center = center.value;
    massData.inertia.cx = inertiaColumnX.value;
    massData.inertia.cy = inertiaColumnY.value;
    massData.inertia.cz = inertiaColumnZ.value;
    b3Body_SetMassData(m_bodyId, massData);
}

B3MassData B3Body::GetMassData() const {
    return B3MassData(b3Body_GetMassData(m_bodyId));
}

void B3Body::SetMassDataValue(const B3MassData& massData) {
    b3Body_SetMassData(m_bodyId, massData.value);
}

B3Vec3 B3Body::GetWorldInverseRotationalInertiaColumnX() const {
    return B3Vec3(b3Body_GetWorldInverseRotationalInertia(m_bodyId).cx);
}

B3Vec3 B3Body::GetWorldInverseRotationalInertiaColumnY() const {
    return B3Vec3(b3Body_GetWorldInverseRotationalInertia(m_bodyId).cy);
}

B3Vec3 B3Body::GetWorldInverseRotationalInertiaColumnZ() const {
    return B3Vec3(b3Body_GetWorldInverseRotationalInertia(m_bodyId).cz);
}

float B3Body::GetLinearDamping() const {
    return b3Body_GetLinearDamping(m_bodyId);
}

void B3Body::SetLinearDamping(float damping) {
    b3Body_SetLinearDamping(m_bodyId, damping);
}

float B3Body::GetAngularDamping() const {
    return b3Body_GetAngularDamping(m_bodyId);
}

void B3Body::SetAngularDamping(float damping) {
    b3Body_SetAngularDamping(m_bodyId, damping);
}

float B3Body::GetGravityScale() const {
    return b3Body_GetGravityScale(m_bodyId);
}

void B3Body::SetGravityScale(float scale) {
    b3Body_SetGravityScale(m_bodyId, scale);
}

bool B3Body::IsAwake() const {
    return b3Body_IsAwake(m_bodyId);
}

void B3Body::SetAwake(bool awake) {
    b3Body_SetAwake(m_bodyId, awake);
}

bool B3Body::IsSleepEnabled() const {
    return b3Body_IsSleepEnabled(m_bodyId);
}

void B3Body::EnableSleep(bool enabled) {
    b3Body_EnableSleep(m_bodyId, enabled);
}

float B3Body::GetSleepThreshold() const {
    return b3Body_GetSleepThreshold(m_bodyId);
}

void B3Body::SetSleepThreshold(float threshold) {
    b3Body_SetSleepThreshold(m_bodyId, threshold);
}

B3RayResult B3Body::CastRay(const B3Vec3& origin, const B3Vec3& translation, const B3QueryFilter& filter,
                            float maxFraction, const B3Transform& bodyTransform) const {
    b3BodyCastResult bodyResult = b3Body_CastRay(m_bodyId, origin.value, translation.value, filter.value,
                                                 maxFraction, b3MakeWorldTransform(bodyTransform.value));
    b3RayResult result{};
    result.shapeId = bodyResult.shapeId;
    result.point = bodyResult.point;
    result.normal = bodyResult.normal;
    result.userMaterialId = bodyResult.userMaterialId;
    result.fraction = bodyResult.fraction;
    result.triangleIndex = bodyResult.triangleIndex;
    result.childIndex = -1;
    result.hit = bodyResult.hit;
    return B3RayResult(result);
}

bool B3Body::IsEnabled() const {
    return b3Body_IsEnabled(m_bodyId);
}

void B3Body::Disable() {
    b3Body_Disable(m_bodyId);
}

void B3Body::Enable() {
    b3Body_Enable(m_bodyId);
}

B3MotionLocks B3Body::GetMotionLocks() const {
    return B3MotionLocks(b3Body_GetMotionLocks(m_bodyId));
}

void B3Body::SetMotionLocks(const B3MotionLocks& locks) {
    b3Body_SetMotionLocks(m_bodyId, locks.value);
}

bool B3Body::IsBullet() const {
    return b3Body_IsBullet(m_bodyId);
}

void B3Body::SetBullet(bool bullet) {
    b3Body_SetBullet(m_bodyId, bullet);
}

bool B3Body::IsFastRotationAllowed() const {
    return b3Body_IsFastRotationAllowed(m_bodyId);
}

void B3Body::AllowFastRotation(bool allowed) {
    b3Body_AllowFastRotation(m_bodyId, allowed);
}

bool B3Body::IsContactRecyclingEnabled() const {
    return b3Body_IsContactRecyclingEnabled(m_bodyId);
}

void B3Body::EnableContactRecycling(bool enabled) {
    b3Body_EnableContactRecycling(m_bodyId, enabled);
}

void B3Body::EnableHitEvents(bool enabled) {
    b3Body_EnableHitEvents(m_bodyId, enabled);
}

long long B3Body::GetWorldId() const {
    return static_cast<long long>(b3StoreWorldId(b3Body_GetWorld(m_bodyId)));
}

int B3Body::GetShapeCount() const {
    return b3Body_GetShapeCount(m_bodyId);
}

long long B3Body::GetShapeId(int index) const {
    int capacity = b3Body_GetShapeCount(m_bodyId);
    if(index < 0 || index >= capacity) {
        return static_cast<long long>(b3StoreShapeId(b3_nullShapeId));
    }
    std::vector<b3ShapeId> shapes(static_cast<size_t>(capacity));
    int count = b3Body_GetShapes(m_bodyId, shapes.data(), capacity);
    if(index >= count) {
        return static_cast<long long>(b3StoreShapeId(b3_nullShapeId));
    }
    return static_cast<long long>(b3StoreShapeId(shapes[static_cast<size_t>(index)]));
}

int B3Body::GetJointCount() const {
    return b3Body_GetJointCount(m_bodyId);
}

long long B3Body::GetJointId(int index) const {
    int capacity = b3Body_GetJointCount(m_bodyId);
    if(index < 0 || index >= capacity) {
        return static_cast<long long>(b3StoreJointId(b3_nullJointId));
    }
    std::vector<b3JointId> joints(static_cast<size_t>(capacity));
    int count = b3Body_GetJoints(m_bodyId, joints.data(), capacity);
    if(index >= count) {
        return static_cast<long long>(b3StoreJointId(b3_nullJointId));
    }
    return static_cast<long long>(b3StoreJointId(joints[static_cast<size_t>(index)]));
}

B3AABB B3Body::ComputeAABB() const {
    return B3AABB(b3Body_ComputeAABB(m_bodyId));
}

B3Vec3 B3Body::GetClosestPoint(const B3Vec3& target) const {
    b3Vec3 result = b3Vec3_zero;
    b3Body_GetClosestPoint(m_bodyId, &result, target.value);
    return B3Vec3(result);
}

float B3Body::GetClosestPointDistance(const B3Vec3& target) const {
    b3Vec3 result = b3Vec3_zero;
    return b3Body_GetClosestPoint(m_bodyId, &result, target.value);
}

B3RayResult B3Body::CastShape(const B3Vec3& origin, const B3ShapeProxy& proxy, const B3Vec3& translation,
                              const B3QueryFilter& filter, float maxFraction, bool canEncroach,
                              const B3Transform& bodyTransform) const {
    b3ShapeProxy nativeProxy = proxy.GetHandle();
    b3BodyCastResult bodyResult = b3Body_CastShape(m_bodyId, origin.value, &nativeProxy, translation.value,
                                                   filter.value, maxFraction, canEncroach,
                                                   b3MakeWorldTransform(bodyTransform.value));
    b3RayResult result{};
    result.shapeId = bodyResult.shapeId;
    result.point = bodyResult.point;
    result.normal = bodyResult.normal;
    result.userMaterialId = bodyResult.userMaterialId;
    result.fraction = bodyResult.fraction;
    result.triangleIndex = bodyResult.triangleIndex;
    result.childIndex = -1;
    result.hit = bodyResult.hit;
    return B3RayResult(result);
}

bool B3Body::OverlapShape(const B3Vec3& origin, const B3ShapeProxy& proxy, const B3QueryFilter& filter,
                          const B3Transform& bodyTransform) const {
    b3ShapeProxy nativeProxy = proxy.GetHandle();
    return b3Body_OverlapShape(m_bodyId, origin.value, &nativeProxy, filter.value,
                               b3MakeWorldTransform(bodyTransform.value));
}

B3MoverCollision* B3Body::CollideMover(const B3Vec3& origin, const B3Capsule& mover,
                                        const B3QueryFilter& filter, const B3Transform& bodyTransform,
                                        int capacity) const {
    B3MoverCollision* collision = new B3MoverCollision();
    if(capacity <= 0) {
        return collision;
    }
    collision->m_results.resize(static_cast<size_t>(capacity));
    int count = b3Body_CollideMover(m_bodyId, collision->m_results.data(), capacity, origin.value, &mover.value,
                                    filter.value, b3MakeWorldTransform(bodyTransform.value));
    count = b3MaxInt(0, b3MinInt(count, capacity));
    collision->m_results.resize(static_cast<size_t>(count));
    return collision;
}

B3Shape* B3Body::CreateSphereShape(const B3ShapeDef& def, const B3Sphere& sphere) {
    return new B3Shape(b3CreateSphereShape(m_bodyId, &def.value, &sphere.value));
}

B3Shape* B3Body::CreateCapsuleShape(const B3ShapeDef& def, const B3Capsule& capsule) {
    return new B3Shape(b3CreateCapsuleShape(m_bodyId, &def.value, &capsule.value));
}

B3Shape* B3Body::CreateHullShape(const B3ShapeDef& def, const B3Hull& hull) {
    const b3HullData* hullData = hull.GetHandle();
    return new B3Shape(hullData != nullptr ? b3CreateHullShape(m_bodyId, &def.value, hullData) : b3_nullShapeId);
}

B3Shape* B3Body::CreateTransformedHullShape(const B3ShapeDef& def, const B3Hull& hull,
                                             const B3Transform& transform, const B3Vec3& scale) {
    const b3HullData* hullData = hull.GetHandle();
    return new B3Shape(hullData != nullptr
        ? b3CreateTransformedHullShape(m_bodyId, &def.value, hullData, transform.value, scale.value)
        : b3_nullShapeId);
}

B3Shape* B3Body::CreateMeshShape(const B3ShapeDef& def, const B3Mesh& mesh, const B3Vec3& scale) {
    const b3MeshData* meshData = mesh.GetHandle();
    return new B3Shape(meshData != nullptr ? b3CreateMeshShape(m_bodyId, &def.value, meshData, scale.value)
                                           : b3_nullShapeId);
}

B3Shape* B3Body::CreateMeshShapeWithMaterials(const B3ShapeDef& def, const B3Mesh& mesh, const B3Vec3& scale,
                                               const B3SurfaceMaterialArray& materials) {
    const b3MeshData* meshData = mesh.GetHandle();
    if(meshData == nullptr) {
        return new B3Shape(b3_nullShapeId);
    }
    b3ShapeDef shapeDef = def.value;
    std::vector<b3SurfaceMaterial> materialCopy;
    const b3SurfaceMaterial* materialData = materials.GetData();
    if(materialData != nullptr && materials.GetSize() > 0) {
        materialCopy.assign(materialData, materialData + materials.GetSize());
        shapeDef.materials = materialCopy.data();
        shapeDef.materialCount = static_cast<int>(materialCopy.size());
    }
    else {
        shapeDef.materials = nullptr;
        shapeDef.materialCount = 0;
    }
    return new B3Shape(b3CreateMeshShape(m_bodyId, &shapeDef, meshData, scale.value));
}

B3Shape* B3Body::CreateHeightFieldShape(const B3ShapeDef& def, const B3HeightField& heightField) {
    return new B3Shape(b3CreateHeightFieldShape(m_bodyId, &def.value, heightField.GetHandle()));
}

B3Shape* B3Body::CreateBakedCompoundShape(B3ShapeDef& def, const B3Compound& compound) {
    const b3CompoundData* compoundData = compound.GetHandle();
    return new B3Shape(compoundData != nullptr ? b3CreateBakedCompoundShape(m_bodyId, &def.value, compoundData)
                                               : b3_nullShapeId);
}

B3Joint::B3Joint() : m_jointId(b3_nullJointId) {
}

B3Joint::B3Joint(long long jointId) : m_jointId(loadJointId(jointId)) {
}

B3Joint::B3Joint(b3JointId jointId) : m_jointId(jointId) {
}

long long B3Joint::GetId() const {
    return static_cast<long long>(b3StoreJointId(m_jointId));
}

bool B3Joint::IsValid() const {
    return b3Joint_IsValid(m_jointId);
}

void B3Joint::Destroy(bool wakeAttached) {
    if(IsValid()) {
        b3DestroyJoint(m_jointId, wakeAttached);
        m_jointId = b3_nullJointId;
    }
}

int B3Joint::GetType() const {
    return static_cast<int>(b3Joint_GetType(m_jointId));
}

long long B3Joint::GetBodyIdA() const {
    return static_cast<long long>(b3StoreBodyId(b3Joint_GetBodyA(m_jointId)));
}

long long B3Joint::GetBodyIdB() const {
    return static_cast<long long>(b3StoreBodyId(b3Joint_GetBodyB(m_jointId)));
}

long long B3Joint::GetWorldId() const {
    return static_cast<long long>(b3StoreWorldId(b3Joint_GetWorld(m_jointId)));
}

B3Transform B3Joint::GetLocalFrameA() const {
    return B3Transform(b3Joint_GetLocalFrameA(m_jointId));
}

void B3Joint::SetLocalFrameA(const B3Transform& localFrame) {
    b3Joint_SetLocalFrameA(m_jointId, localFrame.value);
}

B3Transform B3Joint::GetLocalFrameB() const {
    return B3Transform(b3Joint_GetLocalFrameB(m_jointId));
}

void B3Joint::SetLocalFrameB(const B3Transform& localFrame) {
    b3Joint_SetLocalFrameB(m_jointId, localFrame.value);
}

bool B3Joint::GetCollideConnected() const {
    return b3Joint_GetCollideConnected(m_jointId);
}

void B3Joint::SetCollideConnected(bool collideConnected) {
    b3Joint_SetCollideConnected(m_jointId, collideConnected);
}

void B3Joint::WakeBodies() {
    b3Joint_WakeBodies(m_jointId);
}

B3Vec3 B3Joint::GetConstraintForce() const {
    return B3Vec3(b3Joint_GetConstraintForce(m_jointId));
}

B3Vec3 B3Joint::GetConstraintTorque() const {
    return B3Vec3(b3Joint_GetConstraintTorque(m_jointId));
}

float B3Joint::GetLinearSeparation() const {
    return b3Joint_GetLinearSeparation(m_jointId);
}

float B3Joint::GetAngularSeparation() const {
    return b3Joint_GetAngularSeparation(m_jointId);
}

void B3Joint::SetConstraintTuning(float hertz, float dampingRatio) {
    b3Joint_SetConstraintTuning(m_jointId, hertz, dampingRatio);
}

float B3Joint::GetConstraintHertz() const {
    float hertz = 0.0f;
    float dampingRatio = 0.0f;
    b3Joint_GetConstraintTuning(m_jointId, &hertz, &dampingRatio);
    return hertz;
}

float B3Joint::GetConstraintDampingRatio() const {
    float hertz = 0.0f;
    float dampingRatio = 0.0f;
    b3Joint_GetConstraintTuning(m_jointId, &hertz, &dampingRatio);
    return dampingRatio;
}

void B3Joint::SetForceThreshold(float threshold) {
    b3Joint_SetForceThreshold(m_jointId, threshold);
}

float B3Joint::GetForceThreshold() const {
    return b3Joint_GetForceThreshold(m_jointId);
}

void B3Joint::SetTorqueThreshold(float threshold) {
    b3Joint_SetTorqueThreshold(m_jointId, threshold);
}

float B3Joint::GetTorqueThreshold() const {
    return b3Joint_GetTorqueThreshold(m_jointId);
}

void B3Joint::SetParallelSpringHertz(float hertz) {
    b3ParallelJoint_SetSpringHertz(m_jointId, hertz);
}

float B3Joint::GetParallelSpringHertz() const {
    return b3ParallelJoint_GetSpringHertz(m_jointId);
}

void B3Joint::SetParallelSpringDampingRatio(float dampingRatio) {
    b3ParallelJoint_SetSpringDampingRatio(m_jointId, dampingRatio);
}

float B3Joint::GetParallelSpringDampingRatio() const {
    return b3ParallelJoint_GetSpringDampingRatio(m_jointId);
}

void B3Joint::SetParallelMaxTorque(float torque) {
    b3ParallelJoint_SetMaxTorque(m_jointId, torque);
}

float B3Joint::GetParallelMaxTorque() const {
    return b3ParallelJoint_GetMaxTorque(m_jointId);
}

void B3Joint::SetDistanceLength(float length) {
    b3DistanceJoint_SetLength(m_jointId, length);
}

float B3Joint::GetDistanceLength() const {
    return b3DistanceJoint_GetLength(m_jointId);
}

void B3Joint::EnableDistanceSpring(bool enabled) {
    b3DistanceJoint_EnableSpring(m_jointId, enabled);
}

bool B3Joint::IsDistanceSpringEnabled() const {
    return b3DistanceJoint_IsSpringEnabled(m_jointId);
}

void B3Joint::SetDistanceSpringForceRange(float lowerForce, float upperForce) {
    b3DistanceJoint_SetSpringForceRange(m_jointId, lowerForce, upperForce);
}

float B3Joint::GetDistanceLowerSpringForce() const {
    float lowerForce = 0.0f;
    float upperForce = 0.0f;
    b3DistanceJoint_GetSpringForceRange(m_jointId, &lowerForce, &upperForce);
    return lowerForce;
}

float B3Joint::GetDistanceUpperSpringForce() const {
    float lowerForce = 0.0f;
    float upperForce = 0.0f;
    b3DistanceJoint_GetSpringForceRange(m_jointId, &lowerForce, &upperForce);
    return upperForce;
}

void B3Joint::SetDistanceSpringHertz(float hertz) {
    b3DistanceJoint_SetSpringHertz(m_jointId, hertz);
}

float B3Joint::GetDistanceSpringHertz() const {
    return b3DistanceJoint_GetSpringHertz(m_jointId);
}

void B3Joint::SetDistanceSpringDampingRatio(float dampingRatio) {
    b3DistanceJoint_SetSpringDampingRatio(m_jointId, dampingRatio);
}

float B3Joint::GetDistanceSpringDampingRatio() const {
    return b3DistanceJoint_GetSpringDampingRatio(m_jointId);
}

void B3Joint::EnableDistanceLimit(bool enabled) {
    b3DistanceJoint_EnableLimit(m_jointId, enabled);
}

bool B3Joint::IsDistanceLimitEnabled() const {
    return b3DistanceJoint_IsLimitEnabled(m_jointId);
}

void B3Joint::SetDistanceLengthRange(float minLength, float maxLength) {
    b3DistanceJoint_SetLengthRange(m_jointId, minLength, maxLength);
}

float B3Joint::GetDistanceMinLength() const {
    return b3DistanceJoint_GetMinLength(m_jointId);
}

float B3Joint::GetDistanceMaxLength() const {
    return b3DistanceJoint_GetMaxLength(m_jointId);
}

float B3Joint::GetDistanceCurrentLength() const {
    return b3DistanceJoint_GetCurrentLength(m_jointId);
}

void B3Joint::EnableDistanceMotor(bool enabled) {
    b3DistanceJoint_EnableMotor(m_jointId, enabled);
}

bool B3Joint::IsDistanceMotorEnabled() const {
    return b3DistanceJoint_IsMotorEnabled(m_jointId);
}

void B3Joint::SetDistanceMotorSpeed(float speed) {
    b3DistanceJoint_SetMotorSpeed(m_jointId, speed);
}

float B3Joint::GetDistanceMotorSpeed() const {
    return b3DistanceJoint_GetMotorSpeed(m_jointId);
}

void B3Joint::SetDistanceMaxMotorForce(float force) {
    b3DistanceJoint_SetMaxMotorForce(m_jointId, force);
}

float B3Joint::GetDistanceMaxMotorForce() const {
    return b3DistanceJoint_GetMaxMotorForce(m_jointId);
}

float B3Joint::GetDistanceMotorForce() const {
    return b3DistanceJoint_GetMotorForce(m_jointId);
}

void B3Joint::SetMotorLinearVelocity(const B3Vec3& velocity) {
    b3MotorJoint_SetLinearVelocity(m_jointId, velocity.value);
}

B3Vec3 B3Joint::GetMotorLinearVelocity() const {
    return B3Vec3(b3MotorJoint_GetLinearVelocity(m_jointId));
}

void B3Joint::SetMotorAngularVelocity(const B3Vec3& velocity) {
    b3MotorJoint_SetAngularVelocity(m_jointId, velocity.value);
}

B3Vec3 B3Joint::GetMotorAngularVelocity() const {
    return B3Vec3(b3MotorJoint_GetAngularVelocity(m_jointId));
}

void B3Joint::SetMotorMaxVelocityForce(float force) {
    b3MotorJoint_SetMaxVelocityForce(m_jointId, force);
}

float B3Joint::GetMotorMaxVelocityForce() const {
    return b3MotorJoint_GetMaxVelocityForce(m_jointId);
}

void B3Joint::SetMotorMaxVelocityTorque(float torque) {
    b3MotorJoint_SetMaxVelocityTorque(m_jointId, torque);
}

float B3Joint::GetMotorMaxVelocityTorque() const {
    return b3MotorJoint_GetMaxVelocityTorque(m_jointId);
}

void B3Joint::SetMotorLinearHertz(float hertz) {
    b3MotorJoint_SetLinearHertz(m_jointId, hertz);
}

float B3Joint::GetMotorLinearHertz() const {
    return b3MotorJoint_GetLinearHertz(m_jointId);
}

void B3Joint::SetMotorLinearDampingRatio(float dampingRatio) {
    b3MotorJoint_SetLinearDampingRatio(m_jointId, dampingRatio);
}

float B3Joint::GetMotorLinearDampingRatio() const {
    return b3MotorJoint_GetLinearDampingRatio(m_jointId);
}

void B3Joint::SetMotorAngularHertz(float hertz) {
    b3MotorJoint_SetAngularHertz(m_jointId, hertz);
}

float B3Joint::GetMotorAngularHertz() const {
    return b3MotorJoint_GetAngularHertz(m_jointId);
}

void B3Joint::SetMotorAngularDampingRatio(float dampingRatio) {
    b3MotorJoint_SetAngularDampingRatio(m_jointId, dampingRatio);
}

float B3Joint::GetMotorAngularDampingRatio() const {
    return b3MotorJoint_GetAngularDampingRatio(m_jointId);
}

void B3Joint::SetMotorMaxSpringForce(float force) {
    b3MotorJoint_SetMaxSpringForce(m_jointId, force);
}

float B3Joint::GetMotorMaxSpringForce() const {
    return b3MotorJoint_GetMaxSpringForce(m_jointId);
}

void B3Joint::SetMotorMaxSpringTorque(float torque) {
    b3MotorJoint_SetMaxSpringTorque(m_jointId, torque);
}

float B3Joint::GetMotorMaxSpringTorque() const {
    return b3MotorJoint_GetMaxSpringTorque(m_jointId);
}

void B3Joint::EnablePrismaticSpring(bool enabled) {
    b3PrismaticJoint_EnableSpring(m_jointId, enabled);
}

bool B3Joint::IsPrismaticSpringEnabled() const {
    return b3PrismaticJoint_IsSpringEnabled(m_jointId);
}

void B3Joint::SetPrismaticSpringHertz(float hertz) {
    b3PrismaticJoint_SetSpringHertz(m_jointId, hertz);
}

float B3Joint::GetPrismaticSpringHertz() const {
    return b3PrismaticJoint_GetSpringHertz(m_jointId);
}

void B3Joint::SetPrismaticSpringDampingRatio(float dampingRatio) {
    b3PrismaticJoint_SetSpringDampingRatio(m_jointId, dampingRatio);
}

float B3Joint::GetPrismaticSpringDampingRatio() const {
    return b3PrismaticJoint_GetSpringDampingRatio(m_jointId);
}

void B3Joint::SetPrismaticTargetTranslation(float translation) {
    b3PrismaticJoint_SetTargetTranslation(m_jointId, translation);
}

float B3Joint::GetPrismaticTargetTranslation() const {
    return b3PrismaticJoint_GetTargetTranslation(m_jointId);
}

void B3Joint::EnablePrismaticLimit(bool enabled) {
    b3PrismaticJoint_EnableLimit(m_jointId, enabled);
}

bool B3Joint::IsPrismaticLimitEnabled() const {
    return b3PrismaticJoint_IsLimitEnabled(m_jointId);
}

float B3Joint::GetPrismaticLowerLimit() const {
    return b3PrismaticJoint_GetLowerLimit(m_jointId);
}

float B3Joint::GetPrismaticUpperLimit() const {
    return b3PrismaticJoint_GetUpperLimit(m_jointId);
}

void B3Joint::SetPrismaticLimits(float lower, float upper) {
    b3PrismaticJoint_SetLimits(m_jointId, lower, upper);
}

void B3Joint::EnablePrismaticMotor(bool enabled) {
    b3PrismaticJoint_EnableMotor(m_jointId, enabled);
}

bool B3Joint::IsPrismaticMotorEnabled() const {
    return b3PrismaticJoint_IsMotorEnabled(m_jointId);
}

float B3Joint::GetPrismaticTranslation() const {
    return b3PrismaticJoint_GetTranslation(m_jointId);
}

void B3Joint::SetPrismaticMotorSpeed(float speed) {
    b3PrismaticJoint_SetMotorSpeed(m_jointId, speed);
}

float B3Joint::GetPrismaticMotorSpeed() const {
    return b3PrismaticJoint_GetMotorSpeed(m_jointId);
}

void B3Joint::SetPrismaticMaxMotorForce(float force) {
    b3PrismaticJoint_SetMaxMotorForce(m_jointId, force);
}

float B3Joint::GetPrismaticMaxMotorForce() const {
    return b3PrismaticJoint_GetMaxMotorForce(m_jointId);
}

float B3Joint::GetPrismaticMotorForce() const {
    return b3PrismaticJoint_GetMotorForce(m_jointId);
}

float B3Joint::GetPrismaticSpeed() const {
    return b3PrismaticJoint_GetSpeed(m_jointId);
}

void B3Joint::EnableRevoluteSpring(bool enabled) {
    b3RevoluteJoint_EnableSpring(m_jointId, enabled);
}

bool B3Joint::IsRevoluteSpringEnabled() const {
    return b3RevoluteJoint_IsSpringEnabled(m_jointId);
}

void B3Joint::SetRevoluteTargetAngle(float radians) {
    b3RevoluteJoint_SetTargetAngle(m_jointId, radians);
}

float B3Joint::GetRevoluteTargetAngle() const {
    return b3RevoluteJoint_GetTargetAngle(m_jointId);
}

float B3Joint::GetRevoluteAngle() const {
    return b3RevoluteJoint_GetAngle(m_jointId);
}

void B3Joint::EnableRevoluteLimit(bool enabled) {
    b3RevoluteJoint_EnableLimit(m_jointId, enabled);
}

bool B3Joint::IsRevoluteLimitEnabled() const {
    return b3RevoluteJoint_IsLimitEnabled(m_jointId);
}

float B3Joint::GetRevoluteLowerLimit() const {
    return b3RevoluteJoint_GetLowerLimit(m_jointId);
}

float B3Joint::GetRevoluteUpperLimit() const {
    return b3RevoluteJoint_GetUpperLimit(m_jointId);
}

void B3Joint::SetRevoluteLimits(float lowerRadians, float upperRadians) {
    b3RevoluteJoint_SetLimits(m_jointId, lowerRadians, upperRadians);
}

void B3Joint::EnableRevoluteMotor(bool enabled) {
    b3RevoluteJoint_EnableMotor(m_jointId, enabled);
}

bool B3Joint::IsRevoluteMotorEnabled() const {
    return b3RevoluteJoint_IsMotorEnabled(m_jointId);
}

void B3Joint::SetRevoluteMotorSpeed(float speed) {
    b3RevoluteJoint_SetMotorSpeed(m_jointId, speed);
}

float B3Joint::GetRevoluteMotorSpeed() const {
    return b3RevoluteJoint_GetMotorSpeed(m_jointId);
}

float B3Joint::GetRevoluteMotorTorque() const {
    return b3RevoluteJoint_GetMotorTorque(m_jointId);
}

void B3Joint::SetRevoluteMaxMotorTorque(float torque) {
    b3RevoluteJoint_SetMaxMotorTorque(m_jointId, torque);
}

float B3Joint::GetRevoluteMaxMotorTorque() const {
    return b3RevoluteJoint_GetMaxMotorTorque(m_jointId);
}

void B3Joint::SetRevoluteSpringHertz(float hertz) {
    b3RevoluteJoint_SetSpringHertz(m_jointId, hertz);
}

float B3Joint::GetRevoluteSpringHertz() const {
    return b3RevoluteJoint_GetSpringHertz(m_jointId);
}

void B3Joint::SetRevoluteSpringDampingRatio(float dampingRatio) {
    b3RevoluteJoint_SetSpringDampingRatio(m_jointId, dampingRatio);
}

float B3Joint::GetRevoluteSpringDampingRatio() const {
    return b3RevoluteJoint_GetSpringDampingRatio(m_jointId);
}

void B3Joint::EnableSphericalConeLimit(bool enabled) {
    b3SphericalJoint_EnableConeLimit(m_jointId, enabled);
}

bool B3Joint::IsSphericalConeLimitEnabled() const {
    return b3SphericalJoint_IsConeLimitEnabled(m_jointId);
}

float B3Joint::GetSphericalConeLimit() const {
    return b3SphericalJoint_GetConeLimit(m_jointId);
}

void B3Joint::SetSphericalConeLimit(float radians) {
    b3SphericalJoint_SetConeLimit(m_jointId, radians);
}

float B3Joint::GetSphericalConeAngle() const {
    return b3SphericalJoint_GetConeAngle(m_jointId);
}

void B3Joint::EnableSphericalTwistLimit(bool enabled) {
    b3SphericalJoint_EnableTwistLimit(m_jointId, enabled);
}

bool B3Joint::IsSphericalTwistLimitEnabled() const {
    return b3SphericalJoint_IsTwistLimitEnabled(m_jointId);
}

float B3Joint::GetSphericalLowerTwistLimit() const {
    return b3SphericalJoint_GetLowerTwistLimit(m_jointId);
}

float B3Joint::GetSphericalUpperTwistLimit() const {
    return b3SphericalJoint_GetUpperTwistLimit(m_jointId);
}

void B3Joint::SetSphericalTwistLimits(float lowerRadians, float upperRadians) {
    b3SphericalJoint_SetTwistLimits(m_jointId, lowerRadians, upperRadians);
}

float B3Joint::GetSphericalTwistAngle() const {
    return b3SphericalJoint_GetTwistAngle(m_jointId);
}

void B3Joint::EnableSphericalSpring(bool enabled) {
    b3SphericalJoint_EnableSpring(m_jointId, enabled);
}

bool B3Joint::IsSphericalSpringEnabled() const {
    return b3SphericalJoint_IsSpringEnabled(m_jointId);
}

void B3Joint::SetSphericalMaxMotorTorque(float torque) {
    b3SphericalJoint_SetMaxMotorTorque(m_jointId, torque);
}

float B3Joint::GetSphericalMaxMotorTorque() const {
    return b3SphericalJoint_GetMaxMotorTorque(m_jointId);
}

void B3Joint::SetSphericalSpringHertz(float hertz) {
    b3SphericalJoint_SetSpringHertz(m_jointId, hertz);
}

float B3Joint::GetSphericalSpringHertz() const {
    return b3SphericalJoint_GetSpringHertz(m_jointId);
}

void B3Joint::SetSphericalSpringDampingRatio(float dampingRatio) {
    b3SphericalJoint_SetSpringDampingRatio(m_jointId, dampingRatio);
}

float B3Joint::GetSphericalSpringDampingRatio() const {
    return b3SphericalJoint_GetSpringDampingRatio(m_jointId);
}

void B3Joint::SetSphericalTargetRotation(const B3Quat& rotation) {
    b3SphericalJoint_SetTargetRotation(m_jointId, rotation.value);
}

B3Quat B3Joint::GetSphericalTargetRotation() const {
    return B3Quat(b3SphericalJoint_GetTargetRotation(m_jointId));
}

void B3Joint::EnableSphericalMotor(bool enabled) {
    b3SphericalJoint_EnableMotor(m_jointId, enabled);
}

bool B3Joint::IsSphericalMotorEnabled() const {
    return b3SphericalJoint_IsMotorEnabled(m_jointId);
}

void B3Joint::SetSphericalMotorVelocity(const B3Vec3& velocity) {
    b3SphericalJoint_SetMotorVelocity(m_jointId, velocity.value);
}

B3Vec3 B3Joint::GetSphericalMotorVelocity() const {
    return B3Vec3(b3SphericalJoint_GetMotorVelocity(m_jointId));
}

B3Vec3 B3Joint::GetSphericalMotorTorque() const {
    return B3Vec3(b3SphericalJoint_GetMotorTorque(m_jointId));
}

void B3Joint::SetWeldLinearHertz(float hertz) {
    b3WeldJoint_SetLinearHertz(m_jointId, hertz);
}

float B3Joint::GetWeldLinearHertz() const {
    return b3WeldJoint_GetLinearHertz(m_jointId);
}

void B3Joint::SetWeldLinearDampingRatio(float dampingRatio) {
    b3WeldJoint_SetLinearDampingRatio(m_jointId, dampingRatio);
}

float B3Joint::GetWeldLinearDampingRatio() const {
    return b3WeldJoint_GetLinearDampingRatio(m_jointId);
}

void B3Joint::SetWeldAngularHertz(float hertz) {
    b3WeldJoint_SetAngularHertz(m_jointId, hertz);
}

float B3Joint::GetWeldAngularHertz() const {
    return b3WeldJoint_GetAngularHertz(m_jointId);
}

void B3Joint::SetWeldAngularDampingRatio(float dampingRatio) {
    b3WeldJoint_SetAngularDampingRatio(m_jointId, dampingRatio);
}

float B3Joint::GetWeldAngularDampingRatio() const {
    return b3WeldJoint_GetAngularDampingRatio(m_jointId);
}

void B3Joint::EnableWheelSuspension(bool enabled) {
    b3WheelJoint_EnableSuspension(m_jointId, enabled);
}

bool B3Joint::IsWheelSuspensionEnabled() const {
    return b3WheelJoint_IsSuspensionEnabled(m_jointId);
}

void B3Joint::SetWheelSuspensionHertz(float hertz) {
    b3WheelJoint_SetSuspensionHertz(m_jointId, hertz);
}

float B3Joint::GetWheelSuspensionHertz() const {
    return b3WheelJoint_GetSuspensionHertz(m_jointId);
}

void B3Joint::SetWheelSuspensionDampingRatio(float dampingRatio) {
    b3WheelJoint_SetSuspensionDampingRatio(m_jointId, dampingRatio);
}

float B3Joint::GetWheelSuspensionDampingRatio() const {
    return b3WheelJoint_GetSuspensionDampingRatio(m_jointId);
}

void B3Joint::EnableWheelSuspensionLimit(bool enabled) {
    b3WheelJoint_EnableSuspensionLimit(m_jointId, enabled);
}

bool B3Joint::IsWheelSuspensionLimitEnabled() const {
    return b3WheelJoint_IsSuspensionLimitEnabled(m_jointId);
}

float B3Joint::GetWheelLowerSuspensionLimit() const {
    return b3WheelJoint_GetLowerSuspensionLimit(m_jointId);
}

float B3Joint::GetWheelUpperSuspensionLimit() const {
    return b3WheelJoint_GetUpperSuspensionLimit(m_jointId);
}

void B3Joint::SetWheelSuspensionLimits(float lower, float upper) {
    b3WheelJoint_SetSuspensionLimits(m_jointId, lower, upper);
}

void B3Joint::EnableWheelSpinMotor(bool enabled) {
    b3WheelJoint_EnableSpinMotor(m_jointId, enabled);
}

bool B3Joint::IsWheelSpinMotorEnabled() const {
    return b3WheelJoint_IsSpinMotorEnabled(m_jointId);
}

void B3Joint::SetWheelTargetSteeringAngle(float radians) {
    b3WheelJoint_SetTargetSteeringAngle(m_jointId, radians);
}

float B3Joint::GetWheelTargetSteeringAngle() const {
    return b3WheelJoint_GetTargetSteeringAngle(m_jointId);
}

void B3Joint::SetWheelSpinMotorSpeed(float speed) {
    b3WheelJoint_SetSpinMotorSpeed(m_jointId, speed);
}

float B3Joint::GetWheelSpinMotorSpeed() const {
    return b3WheelJoint_GetSpinMotorSpeed(m_jointId);
}

void B3Joint::SetWheelMaxSpinTorque(float torque) {
    b3WheelJoint_SetMaxSpinTorque(m_jointId, torque);
}

float B3Joint::GetWheelMaxSpinTorque() const {
    return b3WheelJoint_GetMaxSpinTorque(m_jointId);
}

float B3Joint::GetWheelSpinSpeed() const {
    return b3WheelJoint_GetSpinSpeed(m_jointId);
}

float B3Joint::GetWheelSpinTorque() const {
    return b3WheelJoint_GetSpinTorque(m_jointId);
}

void B3Joint::EnableWheelSteering(bool enabled) {
    b3WheelJoint_EnableSteering(m_jointId, enabled);
}

bool B3Joint::IsWheelSteeringEnabled() const {
    return b3WheelJoint_IsSteeringEnabled(m_jointId);
}

void B3Joint::SetWheelSteeringHertz(float hertz) {
    b3WheelJoint_SetSteeringHertz(m_jointId, hertz);
}

float B3Joint::GetWheelSteeringHertz() const {
    return b3WheelJoint_GetSteeringHertz(m_jointId);
}

void B3Joint::SetWheelSteeringDampingRatio(float dampingRatio) {
    b3WheelJoint_SetSteeringDampingRatio(m_jointId, dampingRatio);
}

float B3Joint::GetWheelSteeringDampingRatio() const {
    return b3WheelJoint_GetSteeringDampingRatio(m_jointId);
}

void B3Joint::SetWheelMaxSteeringTorque(float torque) {
    b3WheelJoint_SetMaxSteeringTorque(m_jointId, torque);
}

float B3Joint::GetWheelMaxSteeringTorque() const {
    return b3WheelJoint_GetMaxSteeringTorque(m_jointId);
}

void B3Joint::EnableWheelSteeringLimit(bool enabled) {
    b3WheelJoint_EnableSteeringLimit(m_jointId, enabled);
}

bool B3Joint::IsWheelSteeringLimitEnabled() const {
    return b3WheelJoint_IsSteeringLimitEnabled(m_jointId);
}

float B3Joint::GetWheelLowerSteeringLimit() const {
    return b3WheelJoint_GetLowerSteeringLimit(m_jointId);
}

float B3Joint::GetWheelUpperSteeringLimit() const {
    return b3WheelJoint_GetUpperSteeringLimit(m_jointId);
}

void B3Joint::SetWheelSteeringLimits(float lowerRadians, float upperRadians) {
    b3WheelJoint_SetSteeringLimits(m_jointId, lowerRadians, upperRadians);
}

float B3Joint::GetWheelSteeringAngle() const {
    return b3WheelJoint_GetSteeringAngle(m_jointId);
}

float B3Joint::GetWheelSteeringTorque() const {
    return b3WheelJoint_GetSteeringTorque(m_jointId);
}

B3Shape::B3Shape() : m_shapeId(b3_nullShapeId) {
}

B3Shape::B3Shape(long long shapeId) : m_shapeId(loadShapeId(shapeId)) {
}

B3Shape::B3Shape(b3ShapeId shapeId) : m_shapeId(shapeId) {
}

long long B3Shape::GetId() const {
    return static_cast<long long>(b3StoreShapeId(m_shapeId));
}

bool B3Shape::IsValid() const {
    return b3Shape_IsValid(m_shapeId);
}

void B3Shape::Destroy(bool updateBodyMass) {
    if(IsValid()) {
        b3DestroyShape(m_shapeId, updateBodyMass);
        m_shapeId = b3_nullShapeId;
    }
}

int B3Shape::GetType() const {
    return static_cast<int>(b3Shape_GetType(m_shapeId));
}

long long B3Shape::GetBodyId() const {
    return static_cast<long long>(b3StoreBodyId(b3Shape_GetBody(m_shapeId)));
}

long long B3Shape::GetWorldId() const {
    return static_cast<long long>(b3StoreWorldId(b3Shape_GetWorld(m_shapeId)));
}

bool B3Shape::IsSensor() const {
    return b3Shape_IsSensor(m_shapeId);
}

void B3Shape::GetName(NativeString& name) const {
    const char* value = b3Shape_GetName(m_shapeId);
    name = value != nullptr ? value : "";
}

void B3Shape::SetName(const char* name) {
    b3Shape_SetName(m_shapeId, name != nullptr ? name : "");
}

float B3Shape::GetDensity() const {
    return b3Shape_GetDensity(m_shapeId);
}

void B3Shape::SetDensity(float density, bool updateBodyMass) {
    b3Shape_SetDensity(m_shapeId, density, updateBodyMass);
}

float B3Shape::GetFriction() const {
    return b3Shape_GetFriction(m_shapeId);
}

void B3Shape::SetFriction(float friction) {
    b3Shape_SetFriction(m_shapeId, friction);
}

float B3Shape::GetRestitution() const {
    return b3Shape_GetRestitution(m_shapeId);
}

void B3Shape::SetRestitution(float restitution) {
    b3Shape_SetRestitution(m_shapeId, restitution);
}

B3SurfaceMaterial B3Shape::GetSurfaceMaterial() const {
    return B3SurfaceMaterial(b3Shape_GetSurfaceMaterial(m_shapeId));
}

void B3Shape::SetSurfaceMaterial(const B3SurfaceMaterial& material) {
    b3Shape_SetSurfaceMaterial(m_shapeId, material.value);
}

int B3Shape::GetMeshMaterialCount() const {
    return b3Shape_GetMeshMaterialCount(m_shapeId);
}

B3SurfaceMaterial B3Shape::GetMeshSurfaceMaterial(int index) const {
    return B3SurfaceMaterial(b3Shape_GetMeshSurfaceMaterial(m_shapeId, index));
}

void B3Shape::SetMeshMaterial(const B3SurfaceMaterial& material, int index) {
    b3Shape_SetMeshMaterial(m_shapeId, material.value, index);
}

B3Filter B3Shape::GetFilter() const {
    return B3Filter(b3Shape_GetFilter(m_shapeId));
}

void B3Shape::SetFilter(const B3Filter& filter, bool invokeContacts) {
    b3Shape_SetFilter(m_shapeId, filter.value, invokeContacts);
}

void B3Shape::EnableSensorEvents(bool enabled) {
    b3Shape_EnableSensorEvents(m_shapeId, enabled);
}

bool B3Shape::AreSensorEventsEnabled() const {
    return b3Shape_AreSensorEventsEnabled(m_shapeId);
}

void B3Shape::EnableContactEvents(bool enabled) {
    b3Shape_EnableContactEvents(m_shapeId, enabled);
}

bool B3Shape::AreContactEventsEnabled() const {
    return b3Shape_AreContactEventsEnabled(m_shapeId);
}

void B3Shape::EnablePreSolveEvents(bool enabled) {
    b3Shape_EnablePreSolveEvents(m_shapeId, enabled);
}

bool B3Shape::ArePreSolveEventsEnabled() const {
    return b3Shape_ArePreSolveEventsEnabled(m_shapeId);
}

void B3Shape::EnableHitEvents(bool enabled) {
    b3Shape_EnableHitEvents(m_shapeId, enabled);
}

bool B3Shape::AreHitEventsEnabled() const {
    return b3Shape_AreHitEventsEnabled(m_shapeId);
}

B3RayResult B3Shape::RayCast(const B3Vec3& origin, const B3Vec3& translation) const {
    b3WorldCastOutput output = b3Shape_RayCast(m_shapeId, origin.value, translation.value);
    b3RayResult result{};
    result.shapeId = m_shapeId;
    result.point = output.point;
    result.normal = output.normal;
    result.fraction = output.fraction;
    result.triangleIndex = output.triangleIndex;
    result.childIndex = output.childIndex;
    result.hit = output.hit;
    return B3RayResult(result);
}

B3Sphere B3Shape::GetSphere() const {
    return B3Sphere(b3Shape_GetSphere(m_shapeId));
}

void B3Shape::SetSphere(const B3Sphere& sphere) {
    b3Shape_SetSphere(m_shapeId, &sphere.value);
}

B3Capsule B3Shape::GetCapsule() const {
    return B3Capsule(b3Shape_GetCapsule(m_shapeId));
}

void B3Shape::SetCapsule(const B3Capsule& capsule) {
    b3Shape_SetCapsule(m_shapeId, &capsule.value);
}

B3Hull* B3Shape::GetHull() const {
    const b3HullData* hull = b3Shape_GetHull(m_shapeId);
    return new B3Hull(hull != nullptr ? b3CloneHull(hull) : nullptr);
}

void B3Shape::SetHull(const B3Hull& hull) {
    const b3HullData* hullData = hull.GetHandle();
    if(hullData != nullptr) {
        b3Shape_SetHull(m_shapeId, hullData);
    }
}

void B3Shape::SetMesh(const B3Mesh& mesh, const B3Vec3& scale) {
    const b3MeshData* meshData = mesh.GetHandle();
    if(meshData != nullptr) {
        b3Shape_SetMesh(m_shapeId, meshData, scale.value);
    }
}

int B3Shape::GetContactCapacity() const {
    return b3Shape_GetContactCapacity(m_shapeId);
}

int B3Shape::GetSensorCapacity() const {
    return b3Shape_GetSensorCapacity(m_shapeId);
}

long long B3Shape::GetSensorShapeId(int index) const {
    int capacity = b3Shape_GetSensorCapacity(m_shapeId);
    if(index < 0 || index >= capacity) {
        return static_cast<long long>(b3StoreShapeId(b3_nullShapeId));
    }
    std::vector<b3ShapeId> shapes(static_cast<size_t>(capacity));
    int count = b3Shape_GetSensorData(m_shapeId, shapes.data(), capacity);
    if(index >= count) {
        return static_cast<long long>(b3StoreShapeId(b3_nullShapeId));
    }
    return static_cast<long long>(b3StoreShapeId(shapes[static_cast<size_t>(index)]));
}

B3AABB B3Shape::GetAABB() const {
    return B3AABB(b3Shape_GetAABB(m_shapeId));
}

B3MassData B3Shape::ComputeMassData() const {
    return B3MassData(b3Shape_ComputeMassData(m_shapeId));
}

B3Vec3 B3Shape::GetClosestPoint(const B3Vec3& target) const {
    return B3Vec3(b3Shape_GetClosestPoint(m_shapeId, target.value));
}

void B3Shape::ApplyWind(const B3Vec3& wind, float drag, float lift, float maxSpeed, bool wake) {
    b3Shape_ApplyWind(m_shapeId, wind.value, drag, lift, maxSpeed, wake);
}

B3DebugDrawEm::B3DebugDrawEm()
    : m_draw(b3DefaultDebugDraw()),
      m_drawnCompoundChildCount(0),
      m_totalCompoundChildCount(0) {
    m_draw.DrawShapeFcn = drawShapeCallback;
    m_draw.DrawSegmentFcn = drawSegmentCallback;
    m_draw.DrawTransformFcn = drawTransformCallback;
    m_draw.DrawPointFcn = drawPointCallback;
    m_draw.DrawSphereFcn = drawSphereCallback;
    m_draw.DrawCapsuleFcn = drawCapsuleCallback;
    m_draw.DrawBoundsFcn = drawBoundsCallback;
    m_draw.DrawBoxFcn = drawBoxCallback;
    m_draw.context = this;
    m_draw.drawShapes = true;
    m_draw.drawJoints = true;
}

B3DebugDrawEm::~B3DebugDrawEm() {
    m_draw.context = nullptr;
}

void B3DebugDrawEm::DrawWorld(B3World* world, long long maskBits) {
    m_drawnCompoundChildCount = 0;
    m_totalCompoundChildCount = 0;
    if(world != nullptr && world->IsValid()) {
        b3World_Draw(world->GetHandle(), &m_draw, static_cast<uint64_t>(maskBits));
        world->DrawDebugOverlay(this);
    }
}

void B3DebugDrawEm::SetDrawingBounds(const B3AABB& bounds) {
    m_draw.drawingBounds = bounds.value;
}

B3AABB B3DebugDrawEm::GetDrawingBounds() const {
    return B3AABB(m_draw.drawingBounds);
}

void B3DebugDrawEm::SetForceScale(float scale) {
    m_draw.forceScale = scale;
}

float B3DebugDrawEm::GetForceScale() const {
    return m_draw.forceScale;
}

void B3DebugDrawEm::SetJointScale(float scale) {
    m_draw.jointScale = scale;
}

float B3DebugDrawEm::GetJointScale() const {
    return m_draw.jointScale;
}

void B3DebugDrawEm::SetDrawShapes(bool enabled) {
    m_draw.drawShapes = enabled;
}

bool B3DebugDrawEm::GetDrawShapes() const {
    return m_draw.drawShapes;
}

void B3DebugDrawEm::SetDrawJoints(bool enabled) {
    m_draw.drawJoints = enabled;
}

bool B3DebugDrawEm::GetDrawJoints() const {
    return m_draw.drawJoints;
}

void B3DebugDrawEm::SetDrawJointExtras(bool enabled) {
    m_draw.drawJointExtras = enabled;
}

bool B3DebugDrawEm::GetDrawJointExtras() const {
    return m_draw.drawJointExtras;
}

void B3DebugDrawEm::SetDrawBounds(bool enabled) {
    m_draw.drawBounds = enabled;
}

bool B3DebugDrawEm::GetDrawBounds() const {
    return m_draw.drawBounds;
}

void B3DebugDrawEm::SetDrawMass(bool enabled) {
    m_draw.drawMass = enabled;
}

bool B3DebugDrawEm::GetDrawMass() const {
    return m_draw.drawMass;
}

void B3DebugDrawEm::SetDrawBodyNames(bool enabled) {
    m_draw.drawBodyNames = enabled;
}

bool B3DebugDrawEm::GetDrawBodyNames() const {
    return m_draw.drawBodyNames;
}

void B3DebugDrawEm::SetDrawContacts(bool enabled) {
    m_draw.drawContacts = enabled;
}

bool B3DebugDrawEm::GetDrawContacts() const {
    return m_draw.drawContacts;
}

void B3DebugDrawEm::SetDrawAnchorA(bool enabled) {
    m_draw.drawAnchorA = enabled;
}

bool B3DebugDrawEm::GetDrawAnchorA() const {
    return m_draw.drawAnchorA;
}

void B3DebugDrawEm::SetDrawGraphColors(bool enabled) {
    m_draw.drawGraphColors = enabled;
}

bool B3DebugDrawEm::GetDrawGraphColors() const {
    return m_draw.drawGraphColors;
}

void B3DebugDrawEm::SetDrawContactFeatures(bool enabled) {
    m_draw.drawContactFeatures = enabled;
}

bool B3DebugDrawEm::GetDrawContactFeatures() const {
    return m_draw.drawContactFeatures;
}

void B3DebugDrawEm::SetDrawContactNormals(bool enabled) {
    m_draw.drawContactNormals = enabled;
}

bool B3DebugDrawEm::GetDrawContactNormals() const {
    return m_draw.drawContactNormals;
}

void B3DebugDrawEm::SetDrawContactForces(bool enabled) {
    m_draw.drawContactForces = enabled;
}

bool B3DebugDrawEm::GetDrawContactForces() const {
    return m_draw.drawContactForces;
}

void B3DebugDrawEm::SetDrawIslands(bool enabled) {
    m_draw.drawIslands = enabled;
}

bool B3DebugDrawEm::GetDrawIslands() const {
    return m_draw.drawIslands;
}

int B3DebugDrawEm::GetDrawnCompoundChildCount() const {
    return m_drawnCompoundChildCount;
}

int B3DebugDrawEm::GetTotalCompoundChildCount() const {
    return m_totalCompoundChildCount;
}

struct B3CompoundDebugDrawContext {
    B3DebugDrawEm* draw;
    B3DebugShape* parent;
    b3Transform bodyTransform;
    int color;
};

void B3DebugDrawEm::DrawDebugShape(B3DebugShape* shape, b3WorldTransform transform, int color) {
    if(shape == nullptr) {
        return;
    }
    b3Transform bodyTransform = b3ToRelativeTransform(transform, b3Pos_zero);
    if(shape->m_compound == nullptr) {
        DrawShape(shape, B3Transform(bodyTransform), color);
        return;
    }

    int childCount = static_cast<int>(shape->m_compoundChildren.size());
    m_totalCompoundChildCount += childCount;
    if(childCount == 0) {
        return;
    }

    b3AABB localBounds = b3AABB_Transform(b3InvertTransform(bodyTransform), m_draw.drawingBounds);
    B3CompoundDebugDrawContext context{this, shape, bodyTransform, color};
    b3QueryCompound(shape->m_compound, localBounds, DrawCompoundChild, &context);
}

bool B3DebugDrawEm::DrawCompoundChild(const b3CompoundData*, int childIndex, void* contextPointer) {
    B3CompoundDebugDrawContext* context = static_cast<B3CompoundDebugDrawContext*>(contextPointer);
    if(context == nullptr || context->draw == nullptr || context->parent == nullptr
            || childIndex < 0 || childIndex >= static_cast<int>(context->parent->m_compoundChildren.size())) {
        return true;
    }
    B3DebugShape* child = context->parent->m_compoundChildren[static_cast<size_t>(childIndex)];
    if(child == nullptr) {
        return true;
    }
    b3Transform childTransform = b3MulTransforms(context->bodyTransform, child->m_localTransform);
    context->draw->DrawShape(child, B3Transform(childTransform), context->color);
    context->draw->m_drawnCompoundChildCount += 1;
    return true;
}

void B3DebugDrawEm::DrawShape(B3DebugShape*, const B3Transform&, int) {
}

void B3DebugDrawEm::DrawSegment(const B3Vec3&, const B3Vec3&, int) {
}

void B3DebugDrawEm::DrawTransform(const B3Transform&) {
}

void B3DebugDrawEm::DrawPoint(const B3Vec3&, float, int) {
}

void B3DebugDrawEm::DrawSphere(const B3Vec3&, float, int, float) {
}

void B3DebugDrawEm::DrawCapsule(const B3Vec3&, const B3Vec3&, float, int, float) {
}

void B3DebugDrawEm::DrawBounds(const B3AABB&, int) {
}

void B3DebugDrawEm::DrawBox(const B3Vec3&, const B3Transform&, int) {
}

B3CustomFilterEm::B3CustomFilterEm() = default;

B3CustomFilterEm::~B3CustomFilterEm() = default;

bool B3CustomFilterEm::Filter(long long, long long) {
    return true;
}

B3World::B3World() : B3World(B3WorldDef()) {
}

B3World::B3World(const B3WorldDef& def) : m_worldId(b3CreateWorld(&def.value)), m_destroyed(false) {
}

B3World::~B3World() {
    Destroy();
}

long long B3World::GetId() const {
    return static_cast<long long>(b3StoreWorldId(m_worldId));
}

bool B3World::IsValid() const {
    return !m_destroyed && b3World_IsValid(m_worldId);
}

void B3World::Destroy() {
    if(IsValid()) {
        b3DestroyWorld(m_worldId);
    }
    m_worldId = b3_nullWorldId;
    m_destroyed = true;
}

void B3World::Step(float timeStep, int subStepCount) {
    b3World_Step(m_worldId, timeStep, subStepCount);
}

B3AABB B3World::GetBounds() const {
    return B3AABB(b3World_GetBounds(m_worldId));
}

B3Vec3 B3World::GetGravity() const {
    return B3Vec3(b3World_GetGravity(m_worldId));
}

void B3World::SetGravity(const B3Vec3& gravity) {
    b3World_SetGravity(m_worldId, gravity.value);
}

bool B3World::IsSleepingEnabled() const {
    return b3World_IsSleepingEnabled(m_worldId);
}

void B3World::EnableSleeping(bool enabled) {
    b3World_EnableSleeping(m_worldId, enabled);
}

bool B3World::IsWarmStartingEnabled() const {
    return b3World_IsWarmStartingEnabled(m_worldId);
}

void B3World::EnableWarmStarting(bool enabled) {
    b3World_EnableWarmStarting(m_worldId, enabled);
}

bool B3World::IsContinuousEnabled() const {
    return b3World_IsContinuousEnabled(m_worldId);
}

void B3World::EnableContinuous(bool enabled) {
    b3World_EnableContinuous(m_worldId, enabled);
}

float B3World::GetContactRecycleDistance() const {
    return b3World_GetContactRecycleDistance(m_worldId);
}

void B3World::SetContactRecycleDistance(float recycleDistance) {
    b3World_SetContactRecycleDistance(m_worldId, recycleDistance);
}

float B3World::GetRestitutionThreshold() const {
    return b3World_GetRestitutionThreshold(m_worldId);
}

void B3World::SetRestitutionThreshold(float threshold) {
    b3World_SetRestitutionThreshold(m_worldId, threshold);
}

float B3World::GetHitEventThreshold() const {
    return b3World_GetHitEventThreshold(m_worldId);
}

void B3World::SetHitEventThreshold(float threshold) {
    b3World_SetHitEventThreshold(m_worldId, threshold);
}

float B3World::GetMaximumLinearSpeed() const {
    return b3World_GetMaximumLinearSpeed(m_worldId);
}

void B3World::SetMaximumLinearSpeed(float speed) {
    b3World_SetMaximumLinearSpeed(m_worldId, speed);
}

void B3World::SetContactTuning(float hertz, float dampingRatio, float contactSpeed) {
    b3World_SetContactTuning(m_worldId, hertz, dampingRatio, contactSpeed);
}

int B3World::GetWorkerCount() const {
    return b3World_GetWorkerCount(m_worldId);
}

void B3World::SetWorkerCount(long workerCount) {
    b3World_SetWorkerCount(m_worldId, static_cast<int>(workerCount));
}

int B3World::GetAwakeBodyCount() const {
    return b3World_GetAwakeBodyCount(m_worldId);
}

B3Capacity B3World::GetMaxCapacity() const {
    return B3Capacity(b3World_GetMaxCapacity(m_worldId));
}

void B3World::Explode(const B3ExplosionDef& def) {
    b3World_Explode(m_worldId, &def.value);
}

void B3World::DumpMemoryStats() {
    b3World_DumpMemoryStats(m_worldId);
}

void B3World::RebuildStaticTree() {
    b3World_RebuildStaticTree(m_worldId);
}

void B3World::EnableSpeculative(bool enabled) {
    b3World_EnableSpeculative(m_worldId, enabled);
}

void B3World::SetCustomFilterCallback(B3CustomFilterEm* callback) {
    b3World_SetCustomFilterCallback(m_worldId, callback != nullptr ? customFilterCallback : nullptr, callback);
}

void B3World::ClearDebugOverlay() {
    m_debugSegments.clear();
    m_debugPoints.clear();
    m_debugSpheres.clear();
    m_debugCapsules.clear();
    m_debugBounds.clear();
    m_debugBoxes.clear();
    m_debugHulls.clear();
}

void B3World::AddDebugSegment(const B3Vec3& p1, const B3Vec3& p2, long color) {
    m_debugSegments.push_back({p1.value, p2.value, static_cast<uint32_t>(color)});
}

void B3World::AddDebugPoint(const B3Vec3& point, float size, long color) {
    m_debugPoints.push_back({point.value, size, static_cast<uint32_t>(color)});
}

void B3World::AddDebugSphere(const B3Vec3& center, float radius, long color, float alpha) {
    m_debugSpheres.push_back({center.value, radius, static_cast<uint32_t>(color), alpha});
}

void B3World::AddDebugCapsule(const B3Vec3& p1, const B3Vec3& p2, float radius, long color, float alpha) {
    m_debugCapsules.push_back({p1.value, p2.value, radius, static_cast<uint32_t>(color), alpha});
}

void B3World::AddDebugBounds(const B3AABB& bounds, long color) {
    m_debugBounds.push_back({bounds.value, static_cast<uint32_t>(color)});
}

void B3World::AddDebugBox(const B3Vec3& extents, const B3Transform& transform, long color) {
    m_debugBoxes.push_back({extents.value, transform.value, static_cast<uint32_t>(color)});
}

void B3World::AddDebugHull(const B3Hull& hull, const B3Transform& transform, const B3Vec3& scale, long color) {
    const b3HullData* data = hull.GetHandle();
    if(data == nullptr) {
        return;
    }
    b3HullData* scaledHull = b3CloneAndTransformHull(data, b3Transform_identity, scale.value);
    if(scaledHull == nullptr) {
        return;
    }
    std::unique_ptr<B3DebugShape> debugShape(new B3DebugShape());
    debugShape->m_type = static_cast<int>(b3_hullShape);
    debugShape->AddHull(scaledHull, b3Transform_identity);
    b3DestroyHull(scaledHull);
    m_debugHulls.push_back({std::move(debugShape), transform.value, static_cast<uint32_t>(color)});
}

void B3World::AddDebugTriangle(const B3Vec3& p1, const B3Vec3& p2, const B3Vec3& p3, long color) {
    std::unique_ptr<B3DebugShape> debugShape(new B3DebugShape());
    debugShape->m_type = static_cast<int>(b3_meshShape);
    debugShape->AddTriangle(p1.value, p2.value, p3.value);
    m_debugHulls.push_back({std::move(debugShape), b3Transform_identity, static_cast<uint32_t>(color)});
}

void B3World::DrawDebugOverlay(B3DebugDrawEm* draw) const {
    if(draw == nullptr) {
        return;
    }
    for(const DebugSegment& segment : m_debugSegments) {
        draw->DrawSegment(B3Vec3(segment.p1), B3Vec3(segment.p2), static_cast<int>(segment.color));
    }
    for(const DebugPoint& point : m_debugPoints) {
        draw->DrawPoint(B3Vec3(point.point), point.size, static_cast<int>(point.color));
    }
    for(const DebugSphere& sphere : m_debugSpheres) {
        draw->DrawSphere(B3Vec3(sphere.center), sphere.radius, static_cast<int>(sphere.color), sphere.alpha);
    }
    for(const DebugCapsule& capsule : m_debugCapsules) {
        draw->DrawCapsule(B3Vec3(capsule.p1), B3Vec3(capsule.p2), capsule.radius,
                          static_cast<int>(capsule.color), capsule.alpha);
    }
    for(const DebugBounds& bounds : m_debugBounds) {
        draw->DrawBounds(B3AABB(bounds.bounds), static_cast<int>(bounds.color));
    }
    for(const DebugBox& box : m_debugBoxes) {
        draw->DrawBox(B3Vec3(box.extents), B3Transform(box.transform), static_cast<int>(box.color));
    }
    for(const DebugHull& hull : m_debugHulls) {
        draw->DrawShape(hull.shape.get(), B3Transform(hull.transform),
                        static_cast<int>(hull.color));
    }
}

B3Body* B3World::CreateBody(const B3BodyDef& def) {
    return new B3Body(b3CreateBody(m_worldId, &def.value));
}

B3Joint* B3World::CreateDistanceJoint(const B3DistanceJointDef& def) {
    return new B3Joint(b3CreateDistanceJoint(m_worldId, &def.value));
}

B3Joint* B3World::CreateMotorJoint(const B3MotorJointDef& def) {
    return new B3Joint(b3CreateMotorJoint(m_worldId, &def.value));
}

B3Joint* B3World::CreateParallelJoint(const B3ParallelJointDef& def) {
    return new B3Joint(b3CreateParallelJoint(m_worldId, &def.value));
}

B3Joint* B3World::CreatePrismaticJoint(const B3PrismaticJointDef& def) {
    return new B3Joint(b3CreatePrismaticJoint(m_worldId, &def.value));
}

B3Joint* B3World::CreateSphericalJoint(const B3SphericalJointDef& def) {
    return new B3Joint(b3CreateSphericalJoint(m_worldId, &def.value));
}

B3Joint* B3World::CreateRevoluteJoint(const B3RevoluteJointDef& def) {
    return new B3Joint(b3CreateRevoluteJoint(m_worldId, &def.value));
}

B3Joint* B3World::CreateWeldJoint(const B3WeldJointDef& def) {
    return new B3Joint(b3CreateWeldJoint(m_worldId, &def.value));
}

B3Joint* B3World::CreateFilterJoint(const B3FilterJointDef& def) {
    return new B3Joint(b3CreateFilterJoint(m_worldId, &def.value));
}

B3Joint* B3World::CreateWheelJoint(const B3WheelJointDef& def) {
    return new B3Joint(b3CreateWheelJoint(m_worldId, &def.value));
}

B3BodyEvents* B3World::GetBodyEvents() const {
    return new B3BodyEvents(b3World_GetBodyEvents(m_worldId));
}

B3JointEvents* B3World::GetJointEvents() const {
    return new B3JointEvents(b3World_GetJointEvents(m_worldId));
}

B3SensorEvents* B3World::GetSensorEvents() const {
    return new B3SensorEvents(b3World_GetSensorEvents(m_worldId));
}

B3ContactEvents* B3World::GetContactEvents() const {
    return new B3ContactEvents(b3World_GetContactEvents(m_worldId));
}

B3RayResult B3World::CastRayClosest(const B3Vec3& origin, const B3Vec3& translation, const B3QueryFilter& filter) const {
    return B3RayResult(b3World_CastRayClosest(m_worldId, origin.value, translation.value, filter.value));
}

int B3World::CountOverlapsAABB(const B3AABB& bounds, const B3QueryFilter& filter) const {
    struct Context {
        int count;
    } context = {0};
    auto callback = [](b3ShapeId, void* rawContext) -> bool {
        static_cast<Context*>(rawContext)->count += 1;
        return true;
    };
    b3World_OverlapAABB(m_worldId, bounds.value, filter.value, callback, &context);
    return context.count;
}

B3RayResult B3World::CastSphereClosest(const B3Vec3& origin, float radius, const B3Vec3& translation,
                                        const B3QueryFilter& filter) const {
    b3RayResult closest{};
    closest.shapeId = b3_nullShapeId;
    closest.fraction = 1.0f;
    b3Vec3 point = b3Vec3_zero;
    b3ShapeProxy proxy = {&point, 1, radius};
    auto callback = [](b3ShapeId shapeId, b3Pos hitPoint, b3Vec3 normal, float fraction, uint64_t userMaterialId,
                       int triangleIndex, int childIndex, void* context) -> float {
        b3RayResult* result = static_cast<b3RayResult*>(context);
        result->shapeId = shapeId;
        result->point = hitPoint;
        result->normal = normal;
        result->userMaterialId = userMaterialId;
        result->fraction = fraction;
        result->triangleIndex = triangleIndex;
        result->childIndex = childIndex;
        result->hit = true;
        return fraction;
    };
    b3World_CastShape(m_worldId, origin.value, &proxy, translation.value, filter.value, callback, &closest);
    return B3RayResult(closest);
}

B3RayResult B3World::CastShapeClosest(const B3Vec3& origin, const B3ShapeProxy& proxy,
                                       const B3Vec3& translation, const B3QueryFilter& filter,
                                       bool initialOverlap) const {
    struct Context {
        b3RayResult result;
        bool initialOverlap;
    } context{};
    context.result.shapeId = b3_nullShapeId;
    context.result.fraction = 1.0f;
    context.initialOverlap = initialOverlap;
    auto callback = [](b3ShapeId shapeId, b3Pos hitPoint, b3Vec3 normal, float fraction, uint64_t userMaterialId,
                       int triangleIndex, int childIndex, void* rawContext) -> float {
        Context* context = static_cast<Context*>(rawContext);
        if(!context->initialOverlap && fraction == 0.0f) {
            return -1.0f;
        }
        b3RayResult& result = context->result;
        result.shapeId = shapeId;
        result.point = hitPoint;
        result.normal = normal;
        result.userMaterialId = userMaterialId;
        result.fraction = fraction;
        result.triangleIndex = triangleIndex;
        result.childIndex = childIndex;
        result.hit = true;
        return fraction;
    };
    b3ShapeProxy nativeProxy = proxy.GetHandle();
    b3World_CastShape(m_worldId, origin.value, &nativeProxy, translation.value, filter.value, callback, &context);
    return B3RayResult(context.result);
}

bool B3World::OverlapShape(const B3Vec3& origin, const B3ShapeProxy& proxy, const B3QueryFilter& filter) const {
    bool overlap = false;
    auto callback = [](b3ShapeId, void* context) -> bool {
        *static_cast<bool*>(context) = true;
        return false;
    };
    b3ShapeProxy nativeProxy = proxy.GetHandle();
    b3World_OverlapShape(m_worldId, origin.value, &nativeProxy, filter.value, callback, &overlap);
    return overlap;
}

B3MoverCollision* B3World::CollideMover(const B3Vec3& origin, const B3Capsule& mover,
                                         const B3QueryFilter& filter, int capacity) const {
    B3MoverCollision* collision = new B3MoverCollision();
    if(capacity <= 0) {
        return collision;
    }

    collision->m_results.reserve(static_cast<size_t>(capacity));
    struct Context {
        B3MoverCollision* collision;
        int capacity;
    } context{collision, capacity};

    auto callback = [](b3ShapeId shapeId, const b3PlaneResult* results, int count, void* rawContext) -> bool {
        Context* context = static_cast<Context*>(rawContext);
        for(int i = 0; i < count && static_cast<int>(context->collision->m_results.size()) < context->capacity; ++i) {
            context->collision->m_results.push_back(b3BodyPlaneResult{shapeId, results[i]});
        }
        return static_cast<int>(context->collision->m_results.size()) < context->capacity;
    };

    b3World_CollideMover(m_worldId, origin.value, &mover.value, filter.value, callback, &context);
    return collision;
}

float B3World::CastMover(const B3Vec3& origin, const B3Capsule& mover, const B3Vec3& translation,
                         const B3QueryFilter& filter) const {
    return b3World_CastMover(m_worldId, origin.value, &mover.value, translation.value, filter.value, nullptr, nullptr);
}

float B3World::CastSphereClosestFraction(const B3Vec3& origin, float radius, const B3Vec3& translation,
                                         const B3QueryFilter& filter) const {
    float closest = 1.0f;
    b3Vec3 point = b3Vec3_zero;
    b3ShapeProxy proxy = {&point, 1, radius};
    auto callback = [](b3ShapeId, b3Pos, b3Vec3, float fraction, uint64_t, int, int, void* context) -> float {
        *static_cast<float*>(context) = fraction;
        return fraction;
    };
    b3World_CastShape(m_worldId, origin.value, &proxy, translation.value, filter.value, callback, &closest);
    return closest;
}

b3WorldId B3World::GetHandle() const {
    return m_worldId;
}

bool B3::IsDoublePrecision() {
    return b3IsDoublePrecision();
}

float B3::Atan2(float y, float x) {
    return b3Atan2(y, x);
}

bool B3::IsValidFloat(float value) {
    return b3IsValidFloat(value);
}

bool B3::IsValidVec3(const B3Vec3& value) {
    return b3IsValidVec3(value.value);
}

bool B3::IsValidQuat(const B3Quat& value) {
    return b3IsValidQuat(value.value);
}

bool B3::IsValidTransform(const B3Transform& value) {
    return b3IsValidTransform(value.value);
}

bool B3::IsValidAABB(const B3AABB& value) {
    return b3IsValidAABB(value.value);
}

bool B3::IsBoundedAABB(const B3AABB& value) {
    return b3IsBoundedAABB(value.value);
}

bool B3::IsSaneAABB(const B3AABB& value) {
    return b3IsSaneAABB(value.value);
}

int B3::GetGraphColor(int index) {
    return static_cast<int>(b3GetGraphColor(index));
}

int B3::GetVersionMajor() {
    return b3GetVersion().major;
}

int B3::GetVersionMinor() {
    return b3GetVersion().minor;
}

int B3::GetVersionRevision() {
    return b3GetVersion().revision;
}

float B3::GetLengthUnitsPerMeter() {
    return b3GetLengthUnitsPerMeter();
}

void B3::SetLengthUnitsPerMeter(float lengthUnits) {
    b3SetLengthUnitsPerMeter(lengthUnits);
}

int B3::StaticBody() {
    return static_cast<int>(b3_staticBody);
}

int B3::KinematicBody() {
    return static_cast<int>(b3_kinematicBody);
}

int B3::DynamicBody() {
    return static_cast<int>(b3_dynamicBody);
}

int B3::CapsuleShape() {
    return static_cast<int>(b3_capsuleShape);
}

int B3::CompoundShape() {
    return static_cast<int>(b3_compoundShape);
}

int B3::HeightShape() {
    return static_cast<int>(b3_heightShape);
}

int B3::HullShape() {
    return static_cast<int>(b3_hullShape);
}

int B3::MeshShape() {
    return static_cast<int>(b3_meshShape);
}

int B3::SphereShape() {
    return static_cast<int>(b3_sphereShape);
}

int B3::GetWorldCount() {
    return b3GetWorldCount();
}

int B3::GetMaxWorldCount() {
    return b3GetMaxWorldCount();
}

float B3::GetStallThreshold() {
    return b3GetStallThreshold();
}

void B3::SetStallThreshold(float seconds) {
    b3SetStallThreshold(seconds);
}

long long B3::DefaultMaskBits() {
    return static_cast<long long>(B3_DEFAULT_MASK_BITS);
}

} // namespace JBox3D
