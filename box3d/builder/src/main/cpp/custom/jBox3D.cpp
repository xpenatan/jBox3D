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

static bool drawShapeCallback(void* userShape, b3WorldTransform transform, b3HexColor color, void* context) {
    B3DebugDrawEm* draw = static_cast<B3DebugDrawEm*>(context);
    if(draw == nullptr || userShape == nullptr) {
        return true;
    }
    return draw->DrawShape(static_cast<B3DebugShape*>(userShape), toTransform(transform), static_cast<int>(color));
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
      m_type(-1),
      m_sphere(),
      m_capsule(),
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
      m_type(static_cast<int>(shape.type)),
      m_sphere(),
      m_capsule(),
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
        m_sphere = B3Sphere(*shape.sphere);
        AddSphere(*shape.sphere, identity);
    }
    else if(shape.type == b3_capsuleShape && shape.capsule != nullptr) {
        m_capsule = B3Capsule(*shape.capsule);
        AddCapsule(*shape.capsule, identity);
    }
    else if(shape.type == b3_hullShape && shape.hull != nullptr) {
        AddHull(shape.hull, identity);
    }
    else if(shape.type == b3_meshShape && shape.mesh != nullptr) {
        AddMesh(shape.mesh, identity);
    }
    else if(shape.type == b3_heightShape && shape.heightField != nullptr) {
        AddHeightField(shape.heightField, identity);
    }
    else if(shape.type == b3_compoundShape && shape.compound != nullptr) {
        AddCompound(shape.compound);
    }
}

long long B3DebugShape::GetShapeId() const {
    return m_shapeId;
}

int B3DebugShape::GetType() const {
    return m_type;
}

B3Sphere B3DebugShape::GetSphere() const {
    return m_sphere;
}

B3Capsule B3DebugShape::GetCapsule() const {
    return m_capsule;
}

int B3DebugShape::GetHullEdgeCount() const {
    return static_cast<int>(m_hullEdgeVertices0.size());
}

B3Vec3 B3DebugShape::GetHullEdgeVertex0(int index) const {
    return getIndexedVec3(m_hullEdgeVertices0, index);
}

B3Vec3 B3DebugShape::GetHullEdgeVertex1(int index) const {
    return getIndexedVec3(m_hullEdgeVertices1, index);
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
    return static_cast<int>(m_triangleVertices0.size());
}

B3Vec3 B3DebugShape::GetTriangleVertex0(int index) const {
    return getIndexedVec3(m_triangleVertices0, index);
}

B3Vec3 B3DebugShape::GetTriangleVertex1(int index) const {
    return getIndexedVec3(m_triangleVertices1, index);
}

B3Vec3 B3DebugShape::GetTriangleVertex2(int index) const {
    return getIndexedVec3(m_triangleVertices2, index);
}

B3Vec3 B3DebugShape::GetTriangleNormal(int index) const {
    return getIndexedVec3(m_triangleNormals, index);
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
    b3Transform identity = b3Transform_identity;
    for(int i = 0; i < compound->sphereCount; i++) {
        b3CompoundSphere sphere = b3GetCompoundSphere(compound, i);
        AddSphere(sphere.sphere, identity);
    }
    for(int i = 0; i < compound->capsuleCount; i++) {
        b3CompoundCapsule capsule = b3GetCompoundCapsule(compound, i);
        AddCapsule(capsule.capsule, identity);
    }
    for(int i = 0; i < compound->hullCount; i++) {
        b3CompoundHull hull = b3GetCompoundHull(compound, i);
        AddHull(hull.hull, hull.transform);
    }
    for(int i = 0; i < compound->meshCount; i++) {
        b3CompoundMesh compoundMesh = b3GetCompoundMesh(compound, i);
        b3Mesh mesh{compoundMesh.meshData, compoundMesh.scale};
        AddMesh(&mesh, compoundMesh.transform);
    }
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

const b3HullData* B3Hull::GetHandle() const {
    return m_hull;
}

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

B3Vec3 B3Body::GetPosition() const {
    return B3Vec3(b3Body_GetPosition(m_bodyId));
}

B3Quat B3Body::GetRotation() const {
    return B3Quat(b3Body_GetRotation(m_bodyId));
}

B3Transform B3Body::GetTransform() const {
    return B3Transform(b3Body_GetTransform(m_bodyId));
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

int B3Body::GetShapeCount() const {
    return b3Body_GetShapeCount(m_bodyId);
}

B3AABB B3Body::ComputeAABB() const {
    return B3AABB(b3Body_ComputeAABB(m_bodyId));
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

long long B3Joint::GetBodyIdA() const {
    return static_cast<long long>(b3StoreBodyId(b3Joint_GetBodyA(m_jointId)));
}

long long B3Joint::GetBodyIdB() const {
    return static_cast<long long>(b3StoreBodyId(b3Joint_GetBodyB(m_jointId)));
}

void B3Joint::WakeBodies() {
    b3Joint_WakeBodies(m_jointId);
}

float B3Joint::GetLinearSeparation() const {
    return b3Joint_GetLinearSeparation(m_jointId);
}

void B3Joint::SetRevoluteTargetAngle(float radians) {
    b3RevoluteJoint_SetTargetAngle(m_jointId, radians);
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

bool B3Shape::IsSensor() const {
    return b3Shape_IsSensor(m_shapeId);
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

B3AABB B3Shape::GetAABB() const {
    return B3AABB(b3Shape_GetAABB(m_shapeId));
}

B3Vec3 B3Shape::GetClosestPoint(const B3Vec3& target) const {
    return B3Vec3(b3Shape_GetClosestPoint(m_shapeId, target.value));
}

void B3Shape::ApplyWind(const B3Vec3& wind, float drag, float lift, float maxSpeed, bool wake) {
    b3Shape_ApplyWind(m_shapeId, wind.value, drag, lift, maxSpeed, wake);
}

B3DebugDrawEm::B3DebugDrawEm() : m_draw(b3DefaultDebugDraw()) {
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
    if(world != nullptr && world->IsValid()) {
        b3World_Draw(world->GetHandle(), &m_draw, static_cast<uint64_t>(maskBits));
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

void B3DebugDrawEm::SetDrawFrictionForces(bool enabled) {
    m_draw.drawFrictionForces = enabled;
}

bool B3DebugDrawEm::GetDrawFrictionForces() const {
    return m_draw.drawFrictionForces;
}

void B3DebugDrawEm::SetDrawIslands(bool enabled) {
    m_draw.drawIslands = enabled;
}

bool B3DebugDrawEm::GetDrawIslands() const {
    return m_draw.drawIslands;
}

bool B3DebugDrawEm::DrawShape(B3DebugShape*, const B3Transform&, int) {
    return true;
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

B3SensorEvents* B3World::GetSensorEvents() const {
    return new B3SensorEvents(b3World_GetSensorEvents(m_worldId));
}

B3ContactEvents* B3World::GetContactEvents() const {
    return new B3ContactEvents(b3World_GetContactEvents(m_worldId));
}

B3RayResult B3World::CastRayClosest(const B3Vec3& origin, const B3Vec3& translation, const B3QueryFilter& filter) const {
    return B3RayResult(b3World_CastRayClosest(m_worldId, origin.value, translation.value, filter.value));
}

b3WorldId B3World::GetHandle() const {
    return m_worldId;
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

long long B3::DefaultMaskBits() {
    return static_cast<long long>(B3_DEFAULT_MASK_BITS);
}

} // namespace JBox3D
