package com.github.xpenatan.box3d.sample.shared.samples;

import com.github.xpenatan.box3d.B3Capsule;
import com.github.xpenatan.box3d.B3Collision;
import com.github.xpenatan.box3d.B3MoverCollision;
import com.github.xpenatan.box3d.B3MoverPlaneResult;
import com.github.xpenatan.box3d.B3PlaneSolverResult;
import com.github.xpenatan.box3d.B3QueryFilter;
import com.github.xpenatan.box3d.B3RayResult;
import com.github.xpenatan.box3d.B3Vec3;
import com.github.xpenatan.box3d.sample.shared.Box3DPlayerInput;
import com.github.xpenatan.box3d.sample.shared.Box3DPlayerTarget;

/** Direct port of {@code CharacterMover} from the pinned Box3D samples. */
final class ExactCharacterMover {
    private static final float JUMP_SPEED = 5.0f;
    private static final float MAX_SPEED = 6.0f;
    private static final float ACCELERATION = 30.0f;
    private final AbstractBox3DSample sample;
    private final B3Vec3 position;
    private final B3Capsule capsule;
    private float velocityX;
    private float velocityY;
    private float velocityZ;
    private float pogoVelocity;
    private boolean onGround;

    ExactCharacterMover(AbstractBox3DSample sample, float x, float y, float z) {
        this.sample = sample;
        position = new B3Vec3(x, y, z);
        B3Vec3 center1 = new B3Vec3(0.0f, -0.5f, 0.0f);
        B3Vec3 center2 = new B3Vec3(0.0f, 0.5f, 0.0f);
        capsule = new B3Capsule(center1, center2, 0.3f);
        AbstractBox3DSample.dispose(center2, center1);
    }

    void step(float timeStep, long ignoredShapeId, boolean clipVelocity, Box3DPlayerInput input,
            boolean thirdPerson) {
        if(thirdPerson && input != null && input.jump() && onGround) {
            velocityY = JUMP_SPEED;
            onGround = false;
        }
        boolean sprint = thirdPerson && input != null && onGround && input.sprint();

        float speed = length(velocityX, velocityY, velocityZ);
        if(speed < 0.01f) {
            velocityX = 0.0f;
            velocityZ = 0.0f;
        }
        else {
            float control = speed < 1.0f ? 1.0f : speed;
            float newSpeed = Math.max(0.0f, speed - control * 4.0f * timeStep);
            float ratio = newSpeed / speed;
            velocityX *= ratio;
            velocityZ *= ratio;
        }

        float maxSpeed = sprint ? 1.5f * MAX_SPEED : MAX_SPEED;
        float desiredX = 0.0f;
        float desiredZ = 0.0f;
        if(thirdPerson && input != null) {
            desiredX = maxSpeed * (input.moveForward() * input.cameraForwardX()
                    + input.moveRight() * input.cameraRightX());
            desiredZ = maxSpeed * (input.moveForward() * input.cameraForwardZ()
                    + input.moveRight() * input.cameraRightZ());
        }
        float desiredSpeed = length(desiredX, 0.0f, desiredZ);
        if(desiredSpeed > maxSpeed) {
            float scale = maxSpeed / desiredSpeed;
            desiredX *= scale;
            desiredZ *= scale;
            desiredSpeed = maxSpeed;
        }

        if(onGround) {
            velocityY = 0.0f;
        }

        if(desiredSpeed > 0.000001f) {
            float directionX = desiredX / desiredSpeed;
            float directionZ = desiredZ / desiredSpeed;
            float currentSpeed = velocityX * directionX + velocityZ * directionZ;
            float addSpeed = desiredSpeed - currentSpeed;
            if(addSpeed > 0.0f) {
                float accelerationSpeed = Math.min(addSpeed, ACCELERATION * maxSpeed * timeStep);
                velocityX += accelerationSpeed * directionX;
                velocityZ += accelerationSpeed * directionZ;
            }
        }
        velocityY -= 15.0f * timeStep;

        float pogoRestLength = 3.0f * capsule.GetRadius();
        float rayLength = pogoRestLength + capsule.GetRadius();
        B3Vec3 rayOrigin = offset(position, 0.0f, -0.5f, 0.0f);
        B3Vec3 rayTranslation = new B3Vec3(0.0f, -rayLength, 0.0f);
        B3QueryFilter pogoFilter = queryFilter(1L, 0xFFFFFFFDL, 0L);
        B3RayResult rayResult = sample.world().CastRayClosest(rayOrigin, rayTranslation, pogoFilter);
        if(!rayResult.GetHit() || velocityY > 0.0f) {
            onGround = false;
            pogoVelocity = 0.0f;
            B3Vec3 rayEnd = offset(rayOrigin, 0.0f, -rayLength, 0.0f);
            sample.world().AddDebugSegment(rayOrigin, rayEnd, 0x808080);
            AbstractBox3DSample.dispose(rayEnd);
        }
        else {
            onGround = true;
            float currentLength = rayResult.GetFraction() * rayLength;
            float omega = 2.0f * (float)Math.PI * 4.0f;
            float omegaH = omega * timeStep;
            pogoVelocity = (pogoVelocity - omega * omegaH * (currentLength - pogoRestLength))
                    / (1.0f + 2.0f * 0.7f * omegaH + omegaH * omegaH);
            B3Vec3 hitPoint = rayResult.GetPoint();
            sample.world().AddDebugSegment(rayOrigin, hitPoint, 0x008000);
            AbstractBox3DSample.dispose(hitPoint);
        }

        float targetX = position.GetX() + timeStep * velocityX;
        float targetY = position.GetY() + timeStep * (velocityY + pogoVelocity);
        float targetZ = position.GetZ() + timeStep * velocityZ;
        B3QueryFilter moverFilter = queryFilter(1L, 0xFFFFFFFFL, 1L);
        B3QueryFilter castFilter = queryFilter(1L, 0xFFFFFFFDL, 1L);
        B3MoverCollision finalCollision = null;

        for(int iteration = 0; iteration < 5; ++iteration) {
            B3MoverCollision collision = sample.world().CollideMover(position, capsule, moverFilter, 8);
            B3Vec3 targetDelta = new B3Vec3(targetX - position.GetX(), targetY - position.GetY(),
                    targetZ - position.GetZ());
            B3PlaneSolverResult result = B3Collision.SolveMoverPlanes(targetDelta, collision);
            B3Vec3 delta = result.GetDelta();
            float fraction = sample.world().CastMover(position, capsule, delta, castFilter);
            float dx = fraction * delta.GetX();
            float dy = fraction * delta.GetY();
            float dz = fraction * delta.GetZ();
            position.Set(position.GetX() + dx, position.GetY() + dy, position.GetZ() + dz);

            AbstractBox3DSample.dispose(delta, result, targetDelta);
            AbstractBox3DSample.dispose(finalCollision);
            finalCollision = collision;
            if(dx * dx + dy * dy + dz * dz < 0.0001f) {
                break;
            }
        }

        if(finalCollision != null) {
            if(clipVelocity) {
                B3Vec3 velocity = new B3Vec3(velocityX, velocityY, velocityZ);
                B3Vec3 clipped = B3Collision.ClipVectorToMoverPlanes(velocity, finalCollision);
                velocityX = clipped.GetX();
                velocityY = clipped.GetY();
                velocityZ = clipped.GetZ();
                AbstractBox3DSample.dispose(clipped, velocity);
            }

            for(int i = 0; i < finalCollision.GetCount(); ++i) {
                B3MoverPlaneResult plane = finalCollision.GetResult(i);
                if(plane.GetShapeId() != ignoredShapeId) {
                    B3Vec3 normal = plane.GetNormal();
                    float distance = plane.GetOffset() - capsule.GetRadius();
                    B3Vec3 point = offset(position, distance * normal.GetX(), distance * normal.GetY(),
                            distance * normal.GetZ());
                    B3Vec3 normalEnd = offset(point, 0.1f * normal.GetX(), 0.1f * normal.GetY(),
                            0.1f * normal.GetZ());
                    sample.world().AddDebugPoint(point, 5.0f, 0xFFFF00);
                    sample.world().AddDebugSegment(point, normalEnd, 0xFFFF00);
                    AbstractBox3DSample.dispose(normalEnd, point, normal);
                }
                AbstractBox3DSample.dispose(plane);
            }
        }

        B3Vec3 capsuleP1 = offset(position, 0.0f, -0.5f, 0.0f);
        B3Vec3 capsuleP2 = offset(position, 0.0f, 0.5f, 0.0f);
        B3Vec3 velocityEnd = offset(position, velocityX, velocityY, velocityZ);
        sample.world().AddDebugCapsule(capsuleP1, capsuleP2, capsule.GetRadius(), 0x0000FF, 1.0f);
        sample.world().AddDebugSegment(position, velocityEnd, 0x800080);

        AbstractBox3DSample.dispose(velocityEnd, capsuleP2, capsuleP1, finalCollision, castFilter, moverFilter,
                rayResult, pogoFilter, rayTranslation, rayOrigin);
    }

    void dispose() {
        AbstractBox3DSample.dispose(capsule, position);
    }

    void getPosition(Box3DPlayerTarget target) {
        target.set(position.GetX(), position.GetY(), position.GetZ());
    }

    private static B3QueryFilter queryFilter(long categoryBits, long maskBits, long id) {
        B3QueryFilter filter = new B3QueryFilter();
        filter.SetCategoryBits(categoryBits);
        filter.SetMaskBits(maskBits);
        filter.SetId(id);
        return filter;
    }

    private static float length(float x, float y, float z) {
        return (float)Math.sqrt(x * x + y * y + z * z);
    }

    private static B3Vec3 offset(B3Vec3 p, float x, float y, float z) {
        return new B3Vec3(p.GetX() + x, p.GetY() + y, p.GetZ() + z);
    }
}
