package com.github.xpenatan.box3d.sample.shared.samples;

import com.github.xpenatan.box3d.sample.shared.Box3DSampleCamera;
import com.github.xpenatan.box3d.sample.shared.Box3DSampleEntry;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class Box3DSampleRegistry {
    private static final List<Box3DSampleEntry> ENTRIES = createEntries();

    private Box3DSampleRegistry() {
    }

    public static List<Box3DSampleEntry> entries() {
        return ENTRIES;
    }

    private static List<Box3DSampleEntry> createEntries() {
        ArrayList<Box3DSampleEntry> entries = new ArrayList<Box3DSampleEntry>();
        entries.add(new Box3DSampleEntry("Stacking", "Single Box", SingleBoxSample::new,
                new Box3DSampleCamera(0.0f, 25.0f, 10.0f, 0.0f, 0.0f, 0.0f)));
        entries.add(new Box3DSampleEntry("Stacking", "Card House Thick", CardHouseThickSample::new,
                new Box3DSampleCamera(0.0f, 25.0f, 10.0f, 0.0f, 2.0f, 0.0f)));
        entries.add(new Box3DSampleEntry("Stacking", "Card House", CardHouseSample::new,
                new Box3DSampleCamera(30.0f, 10.0f, 3.0f, 0.75f, 1.0f, 0.4f)));
        entries.add(new Box3DSampleEntry("Stacking", "Box Stack", BoxStackSample::new,
                new Box3DSampleCamera(0.0f, 15.0f, 50.0f, 0.0f, 20.0f, 0.0f)));
        entries.add(new Box3DSampleEntry("Stacking", "Sphere Stack", SphereStackSample::new,
                new Box3DSampleCamera(0.0f, 15.0f, 50.0f, 0.0f, 10.0f, 0.0f)));
        entries.add(new Box3DSampleEntry("Stacking", "Capsule Stack", CapsuleStackSample::new,
                new Box3DSampleCamera(0.0f, 15.0f, 50.0f, 0.0f, 10.0f, 0.0f)));
        entries.add(new Box3DSampleEntry("Stacking", "Cylinder", CylinderSample::new,
                new Box3DSampleCamera(0.0f, 15.0f, 10.0f, 0.0f, 0.0f, 0.0f)));
        entries.add(new Box3DSampleEntry("Stacking", "Cylinder Stack", CylinderStackSample::new,
                new Box3DSampleCamera(0.0f, 15.0f, 15.0f, 0.0f, 5.0f, 0.0f)));
        entries.add(new Box3DSampleEntry("Stacking", "Jenga Stack", JengaStackSample::new,
                new Box3DSampleCamera(35.0f, 15.0f, 30.0f, 0.0f, 10.0f, 0.0f)));
        entries.add(new Box3DSampleEntry("Stacking", "Dominoes", DominoesSample::new,
                new Box3DSampleCamera(0.0f, 15.0f, 75.0f, 0.0f, 0.0f, 0.0f)));
        entries.add(new Box3DSampleEntry("Stacking", "Wedge", WedgeSample::new,
                new Box3DSampleCamera(10.0f, 7.0f, 10.0f, 0.0f, 0.8f, 0.0f)));
        entries.add(new Box3DSampleEntry("Stacking", "Arch", ArchSample::new,
                new Box3DSampleCamera(25.0f, 10.0f, 30.0f, 0.0f, 5.0f, 0.0f)));
        entries.add(new Box3DSampleEntry("Stacking", "Double Domino", DoubleDominoSample::new,
                new Box3DSampleCamera(0.0f, 15.0f, 15.0f, 0.0f, 0.5f, 1.0f)));
        entries.add(new Box3DSampleEntry("Stacking", "Pyramid2D", Pyramid2DSample::new,
                new Box3DSampleCamera(0.0f, 30.0f, 50.0f, 0.0f, 5.0f, 0.0f)));
        entries.add(new Box3DSampleEntry("Shapes", "Restitution", RestitutionSample::new,
                new Box3DSampleCamera(0.0f, 25.0f, 85.0f, 0.0f, 20.0f, 0.0f)));
        entries.add(new Box3DSampleEntry("Shapes", "Inclined Plane", InclinedPlaneSample::new,
                new Box3DSampleCamera(-55.0f, 30.0f, 60.0f, 0.0f, 7.5f, 0.0f)));
        entries.add(new Box3DSampleEntry("Shapes", "Rolling Resistance", RollingResistanceSample::new,
                new Box3DSampleCamera(-140.0f, 17.0f, 60.0f, 0.0f, 7.5f, 0.0f)));
        entries.add(new Box3DSampleEntry("Shapes", "High Resistance", HighResistanceSample::new,
                new Box3DSampleCamera(0.0f, 5.0f, 40.0f, 0.0f, 7.5f, 0.0f)));
        entries.add(new Box3DSampleEntry("Shapes", "Isotropic Friction", IsotropicFrictionSample::new,
                new Box3DSampleCamera(45.0f, 30.0f, 150.0f, 0.0f, 0.0f, 0.0f)));
        entries.add(new Box3DSampleEntry("Shapes", "Slide Twist", SlideTwistSample::new,
                new Box3DSampleCamera(-30.0f, 17.0f, 30.0f, 0.0f, 5.0f, 0.0f)));
        entries.add(new Box3DSampleEntry("Shapes", "Static Invoke", StaticInvokeSample::new,
                new Box3DSampleCamera(0.0f, 25.0f, 10.0f, 0.0f, 1.0f, 0.0f)));
        entries.add(new Box3DSampleEntry("Shapes", "Conveyor Belt", ConveyorBeltSample::new,
                new Box3DSampleCamera(0.0f, 25.0f, 40.0f, 0.0f, 1.0f, 0.0f)));
        entries.add(new Box3DSampleEntry("Shapes", "Conveyor Mesh", ConveyorMeshSample::new,
                new Box3DSampleCamera(35.0f, 25.0f, 30.0f, 0.0f, 4.0f, 0.0f)));
        entries.add(new Box3DSampleEntry("Shapes", "Wind", WindSample::new,
                new Box3DSampleCamera(0.0f, 0.0f, 5.0f, 0.0f, 1.0f, 0.0f)));
        entries.add(new Box3DSampleEntry("Shapes", "Wind Drop", WindDropSample::new,
                new Box3DSampleCamera(-45.0f, 15.0f, 20.0f, 0.0f, 5.0f, 0.0f)));
        entries.add(new Box3DSampleEntry("Shapes", "Wind Flap", WindFlapSample::new,
                new Box3DSampleCamera(-35.0f, 15.0f, 65.0f, 0.0f, 5.0f, 10.0f)));
        entries.add(new Box3DSampleEntry("Bodies", "Body Type", BodyTypeSample::new,
                new Box3DSampleCamera(0.0f, 30.0f, 30.0f, 0.0f, 1.5f, 0.0f)));
        entries.add(new Box3DSampleEntry("Bodies", "Spinning Book", SpinningBookSample::new,
                new Box3DSampleCamera(0.0f, 7.0f, 12.0f, 0.0f, 1.5f, 0.0f)));
        entries.add(new Box3DSampleEntry("Bodies", "Gyroscopic Torque", GyroscopicTorqueSample::new,
                new Box3DSampleCamera(0.0f, 7.0f, 10.0f, 0.0f, 1.5f, 0.0f)));
        entries.add(new Box3DSampleEntry("Bodies", "Weeble", WeebleSample::new,
                new Box3DSampleCamera(0.0f, 10.0f, 15.0f, 0.0f, 2.0f, 0.0f)));
        entries.add(new Box3DSampleEntry("Bodies", "Disable", DisableSample::new,
                new Box3DSampleCamera(45.0f, 25.0f, 10.0f, 0.0f, 0.0f, 0.0f)));
        entries.add(new Box3DSampleEntry("Bodies", "Disable Body", DisableBodySample::new,
                new Box3DSampleCamera(0.0f, 10.0f, 16.0f, 0.0f, 3.0f, 0.0f)));
        entries.add(new Box3DSampleEntry("Bodies", "Kinematic", KinematicSample::new,
                new Box3DSampleCamera(0.0f, 10.0f, 15.0f, 0.0f, 3.0f, 0.0f)));
        entries.add(new Box3DSampleEntry("Bodies", "Cast", BodyCastSample::new,
                new Box3DSampleCamera(35.0f, 20.0f, 32.0f, 0.0f, 4.0f, 0.0f)));
        entries.add(new Box3DSampleEntry("Bodies", "Lock Mixing", LockMixingSample::new,
                new Box3DSampleCamera(45.0f, 30.0f, 40.0f, 0.0f, 0.0f, 0.0f)));
        entries.add(new Box3DSampleEntry("Bodies", "Fixed Rotation", FixedRotationSample::new,
                new Box3DSampleCamera(0.0f, 15.0f, 10.0f, 0.0f, 0.0f, 0.0f)));
        entries.add(new Box3DSampleEntry("Continuous", "Thin Wall", ThinWallSample::new,
                new Box3DSampleCamera(45.0f, 30.0f, 30.0f, 0.0f, 0.0f, 0.0f)));
        entries.add(new Box3DSampleEntry("Continuous", "Bounce House", BounceHouseSample::new,
                new Box3DSampleCamera(45.0f, 45.0f, 50.0f, 0.0f, 0.0f, 0.0f)));
        entries.add(new Box3DSampleEntry("Continuous", "Spinning Stick", SpinningStickSample::new,
                new Box3DSampleCamera(45.0f, 25.0f, 20.0f, 0.0f, 2.0f, 0.0f)));
        entries.add(new Box3DSampleEntry("Continuous", "Bullet vs Stack", BulletVersusStackSample::new,
                new Box3DSampleCamera(15.0f, 20.0f, 30.0f, 0.0f, 2.0f, 0.0f)));
        entries.add(new Box3DSampleEntry("Continuous", "Hump Mesh", ContinuousHumpMeshSample::new,
                new Box3DSampleCamera(25.0f, 15.0f, 38.0f, 0.0f, 4.0f, 0.0f)));
        entries.add(new Box3DSampleEntry("Continuous", "Is Fast", ContinuousIsFastSample::new,
                new Box3DSampleCamera(25.0f, 15.0f, 32.0f, 0.0f, 4.0f, 0.0f)));
        entries.add(new Box3DSampleEntry("Continuous", "Mesh Drop", ContinuousMeshDropSample::new,
                new Box3DSampleCamera(25.0f, 15.0f, 34.0f, 0.0f, 6.0f, 0.0f)));
        entries.add(new Box3DSampleEntry("Continuous", "Mesh Drop Unit Test", MeshDropUnitTestSample::new,
                new Box3DSampleCamera(25.0f, 15.0f, 24.0f, 0.0f, 4.0f, 0.0f)));
        entries.add(new Box3DSampleEntry("Continuous", "Needle Mesh", NeedleMeshSample::new,
                new Box3DSampleCamera(20.0f, 14.0f, 34.0f, 0.0f, 6.0f, 0.0f)));
        entries.add(new Box3DSampleEntry("Continuous", "Stall", ContinuousStallSample::new,
                new Box3DSampleCamera(25.0f, 15.0f, 36.0f, 0.0f, 4.0f, 0.0f)));
        entries.add(new Box3DSampleEntry("Compound", "Simple", SimpleCompoundSample::new,
                new Box3DSampleCamera(45.0f, 30.0f, 45.0f, 0.0f, 0.0f, 0.0f)));
        entries.add(new Box3DSampleEntry("Compound", "Spheres", CompoundSpheresSample::new,
                new Box3DSampleCamera(45.0f, 30.0f, 45.0f, 0.0f, 0.0f, 0.0f)));
        entries.add(new Box3DSampleEntry("Compound", "Hulls", CompoundHullsSample::new,
                new Box3DSampleCamera(45.0f, 30.0f, 45.0f, 0.0f, 0.0f, 0.0f)));
        entries.add(new Box3DSampleEntry("Compound", "Tile Floor", TileFloorSample::new,
                new Box3DSampleCamera(45.0f, 30.0f, 55.0f, 0.0f, 0.0f, 0.0f)));
        entries.add(new Box3DSampleEntry("Compound", "Village", VillageSample::new,
                new Box3DSampleCamera(45.0f, 25.0f, 65.0f, 0.0f, 0.0f, 0.0f)));
        entries.add(new Box3DSampleEntry("Compound", "Mesh Tile", CompoundMeshTileSample::new,
                new Box3DSampleCamera(45.0f, 25.0f, 34.0f, 0.0f, 3.0f, 0.0f)));
        entries.add(new Box3DSampleEntry("Events", "Sensor Visit", SensorVisitSample::new,
                new Box3DSampleCamera(0.0f, 30.0f, 20.0f, 0.0f, 5.0f, 0.0f)));
        entries.add(new Box3DSampleEntry("Events", "Hit", HitEventSample::new,
                new Box3DSampleCamera(0.0f, 30.0f, 100.0f, 0.0f, 5.0f, 0.0f)));
        entries.add(new Box3DSampleEntry("Events", "Move", MoveEventSample::new,
                new Box3DSampleCamera(0.0f, 30.0f, 40.0f, 0.0f, 5.0f, 0.0f)));
        entries.add(new Box3DSampleEntry("Events", "Persistent Contact", PersistentContactSample::new,
                new Box3DSampleCamera(0.0f, 30.0f, 40.0f, 0.0f, 5.0f, 0.0f)));
        entries.add(new Box3DSampleEntry("Events", "Sensor Hits", SensorHitsSample::new,
                new Box3DSampleCamera(0.0f, 30.0f, 40.0f, 0.0f, 5.0f, 0.0f)));
        entries.add(new Box3DSampleEntry("Events", "Joint", EventJointSample::new,
                new Box3DSampleCamera(25.0f, 18.0f, 24.0f, 0.0f, 4.0f, 0.0f)));
        entries.add(new Box3DSampleEntry("Joints", "Bridge", BridgeSample::new,
                new Box3DSampleCamera(0.0f, 20.0f, 35.0f, 0.0f, 10.0f, 0.0f)));
        entries.add(new Box3DSampleEntry("Joints", "Distance Joint", DistanceJointSample::new,
                new Box3DSampleCamera(0.0f, 10.0f, 40.0f, 0.0f, 10.0f, 0.0f)));
        entries.add(new Box3DSampleEntry("Joints", "Door", DoorSample::new,
                new Box3DSampleCamera(45.0f, 30.0f, 15.0f, 0.0f, 2.0f, 0.0f)));
        entries.add(new Box3DSampleEntry("Joints", "Filter", FilterJointSample::new,
                new Box3DSampleCamera(45.0f, 30.0f, 15.0f, 0.0f, 2.0f, 0.0f)));
        entries.add(new Box3DSampleEntry("Joints", "Motion Locks", MotionLocksSample::new,
                new Box3DSampleCamera(0.0f, 30.0f, 40.0f, 0.0f, 5.0f, 0.0f)));
        entries.add(new Box3DSampleEntry("Joints", "Motor Joint", MotorJointSample::new,
                new Box3DSampleCamera(0.0f, 8.0f, 25.0f, 0.0f, 8.0f, 0.0f)));
        entries.add(new Box3DSampleEntry("Joints", "Parallel Spring", ParallelSpringSample::new,
                new Box3DSampleCamera(45.0f, 30.0f, 15.0f, 0.0f, 2.0f, 0.0f)));
        entries.add(new Box3DSampleEntry("Joints", "Prismatic", PrismaticJointSample::new,
                new Box3DSampleCamera(45.0f, 30.0f, 15.0f, 0.0f, 2.0f, 0.0f)));
        entries.add(new Box3DSampleEntry("Joints", "Spherical", SphericalJointSample::new,
                new Box3DSampleCamera(45.0f, 30.0f, 15.0f, 0.0f, 2.0f, 0.0f)));
        entries.add(new Box3DSampleEntry("Joints", "Revolute", RevoluteJointSample::new,
                new Box3DSampleCamera(45.0f, 30.0f, 15.0f, 0.0f, 2.0f, 0.0f)));
        entries.add(new Box3DSampleEntry("Joints", "Top Down Friction", TopDownFrictionSample::new,
                new Box3DSampleCamera(0.0f, 10.0f, 26.0f, 0.0f, 10.0f, 0.0f)));
        entries.add(new Box3DSampleEntry("Joints", "Weld", WeldJointSample::new,
                new Box3DSampleCamera(45.0f, 30.0f, 15.0f, 0.0f, 2.0f, 0.0f)));
        entries.add(new Box3DSampleEntry("Joints", "Wheel", WheelJointSample::new,
                new Box3DSampleCamera(3.0f, 3.5f, 6.0f, 0.0f, 2.0f, 0.0f)));
        entries.add(new Box3DSampleEntry("Joints", "Ball and Chain", BallAndChainSample::new,
                new Box3DSampleCamera(16.0f, -7.0f, -48.0f, 16.0f, -20.0f, 0.0f)));
        entries.add(new Box3DSampleEntry("Benchmark", "Large Pyramid", BenchmarkLargePyramidSample::new,
                new Box3DSampleCamera(40.0f, -10.0f, 110.0f, 0.0f, 40.0f, 0.0f)));
        entries.add(new Box3DSampleEntry("Benchmark", "Wide Pyramid", BenchmarkWidePyramidSample::new,
                new Box3DSampleCamera(0.0f, 5.0f, 80.0f, 0.0f, 18.0f, 0.0f)));
        entries.add(new Box3DSampleEntry("Benchmark", "Many Pyramids", BenchmarkManyPyramidsSample::new,
                new Box3DSampleCamera(-10.0f, 10.0f, 120.0f, 0.0f, 5.0f, 0.0f)));
        entries.add(new Box3DSampleEntry("Benchmark", "Rain", BenchmarkRainSample::new,
                new Box3DSampleCamera(25.0f, 10.0f, 70.0f, 0.0f, 0.0f, 0.0f)));
        entries.add(new Box3DSampleEntry("Benchmark", "Falling Boxes", FallingBoxesSample::new,
                new Box3DSampleCamera(45.0f, 10.0f, 80.0f, 0.0f, 20.0f, 0.0f)));
        entries.add(new Box3DSampleEntry("Benchmark", "Candy Cups", CandyCupsSample::new,
                new Box3DSampleCamera(45.0f, 20.0f, 70.0f, 0.0f, 0.0f, 0.0f)));
        entries.add(new Box3DSampleEntry("Benchmark", "Hull", BenchmarkHullSample::new,
                new Box3DSampleCamera(45.0f, 20.0f, 45.0f, 0.0f, 5.0f, 0.0f)));
        entries.add(new Box3DSampleEntry("Benchmark", "Washer", BenchmarkWasherSample::new,
                new Box3DSampleCamera(45.0f, 20.0f, 45.0f, 0.0f, 5.0f, 0.0f)));
        entries.add(new Box3DSampleEntry("Benchmark", "Chains", BenchmarkChainsSample::new,
                new Box3DSampleCamera(35.0f, 15.0f, 42.0f, 0.0f, 8.0f, 0.0f)));
        entries.add(new Box3DSampleEntry("Benchmark", "Destruction", BenchmarkDestructionSample::new,
                new Box3DSampleCamera(35.0f, 18.0f, 36.0f, 0.0f, 6.0f, 0.0f)));
        entries.add(new Box3DSampleEntry("Benchmark", "Explosion", BenchmarkExplosionSample::new,
                new Box3DSampleCamera(35.0f, 18.0f, 38.0f, 0.0f, 6.0f, 0.0f)));
        entries.add(new Box3DSampleEntry("Benchmark", "Falling Trees", BenchmarkFallingTreesSample::new,
                new Box3DSampleCamera(45.0f, 18.0f, 50.0f, 0.0f, 5.0f, 0.0f)));
        entries.add(new Box3DSampleEntry("Benchmark", "Height Field", BenchmarkHeightFieldSample::new,
                new Box3DSampleCamera(25.0f, 14.0f, 42.0f, 0.0f, 5.0f, 0.0f)));
        entries.add(new Box3DSampleEntry("Benchmark", "Joint Grid", BenchmarkJointGridSample::new,
                new Box3DSampleCamera(35.0f, 15.0f, 34.0f, 0.0f, 8.0f, 0.0f)));
        entries.add(new Box3DSampleEntry("Benchmark", "Junkyard", BenchmarkJunkyardSample::new,
                new Box3DSampleCamera(45.0f, 20.0f, 52.0f, 0.0f, 6.0f, 0.0f)));
        entries.add(new Box3DSampleEntry("Benchmark", "Large World", BenchmarkLargeWorldSample::new,
                new Box3DSampleCamera(50045.0f, 20.0f, 50090.0f, 50000.0f, 8.0f, 50000.0f)));
        entries.add(new Box3DSampleEntry("Benchmark", "Sensor", BenchmarkSensorSample::new,
                new Box3DSampleCamera(30.0f, 18.0f, 34.0f, 0.0f, 6.0f, 0.0f)));
        entries.add(new Box3DSampleEntry("Character", "CapsulePlane", CharacterCapsulePlaneSample::new,
                new Box3DSampleCamera(25.0f, 14.0f, 26.0f, 0.0f, 4.0f, 0.0f)));
        entries.add(new Box3DSampleEntry("Character", "Mover", CharacterMoverSample::new,
                new Box3DSampleCamera(25.0f, 14.0f, 26.0f, 0.0f, 4.0f, 0.0f)));
        entries.add(new Box3DSampleEntry("Character", "MoverOverlap", CharacterMoverOverlapSample::new,
                new Box3DSampleCamera(25.0f, 14.0f, 24.0f, 0.0f, 4.0f, 0.0f)));
        entries.add(new Box3DSampleEntry("Character", "Rigid Body", CharacterRigidBodySample::new,
                new Box3DSampleCamera(25.0f, 14.0f, 28.0f, 0.0f, 4.0f, 0.0f)));
        entries.add(new Box3DSampleEntry("Determinism", "Falling Ragdolls", DeterminismFallingRagdollsSample::new,
                new Box3DSampleCamera(35.0f, 16.0f, 42.0f, 0.0f, 8.0f, 0.0f)));
        entries.add(new Box3DSampleEntry("Ragdoll", "Box", RagdollBoxSample::new,
                new Box3DSampleCamera(25.0f, 14.0f, 24.0f, 0.0f, 5.0f, 0.0f)));
        entries.add(new Box3DSampleEntry("Ragdoll", "Incline", RagdollInclineSample::new,
                new Box3DSampleCamera(25.0f, 14.0f, 26.0f, 0.0f, 5.0f, 0.0f)));
        entries.add(new Box3DSampleEntry("Ragdoll", "Mesh", RagdollMeshSample::new,
                new Box3DSampleCamera(25.0f, 14.0f, 30.0f, 0.0f, 5.0f, 0.0f)));
        entries.add(new Box3DSampleEntry("Ragdoll", "Pile", RagdollPileSample::new,
                new Box3DSampleCamera(30.0f, 16.0f, 32.0f, 0.0f, 6.0f, 0.0f)));
        entries.add(new Box3DSampleEntry("Ragdoll", "Pose", RagdollPoseSample::new,
                new Box3DSampleCamera(25.0f, 14.0f, 24.0f, 0.0f, 5.0f, 0.0f)));
        entries.add(new Box3DSampleEntry("Tree", "Benchmark", TreeBenchmarkSample::new,
                new Box3DSampleCamera(45.0f, 20.0f, 52.0f, 0.0f, 6.0f, 0.0f)));
        entries.add(new Box3DSampleEntry("Issues", "Capsule Mesh", IssueCapsuleMeshSample::new,
                new Box3DSampleCamera(25.0f, 14.0f, 28.0f, 0.0f, 5.0f, 0.0f)));
        entries.add(new Box3DSampleEntry("Issues", "Convex Jitter", IssueConvexJitterSample::new,
                new Box3DSampleCamera(25.0f, 14.0f, 24.0f, 0.0f, 5.0f, 0.0f)));
        entries.add(new Box3DSampleEntry("Issues", "Crash", IssueCrashSample::new,
                new Box3DSampleCamera(25.0f, 14.0f, 24.0f, 0.0f, 5.0f, 0.0f)));
        entries.add(new Box3DSampleEntry("Issues", "Dump Loader", IssueDumpLoaderSample::new,
                new Box3DSampleCamera(25.0f, 14.0f, 24.0f, 0.0f, 5.0f, 0.0f)));
        entries.add(new Box3DSampleEntry("Issues", "Hull Crash", IssueHullCrashSample::new,
                new Box3DSampleCamera(25.0f, 14.0f, 24.0f, 0.0f, 5.0f, 0.0f)));
        entries.add(new Box3DSampleEntry("Issues", "Multiple Prismatic", IssueMultiplePrismaticSample::new,
                new Box3DSampleCamera(25.0f, 14.0f, 26.0f, 0.0f, 5.0f, 0.0f)));
        entries.add(new Box3DSampleEntry("Issues", "s&box mover", IssueSandboxMoverSample::new,
                new Box3DSampleCamera(25.0f, 14.0f, 26.0f, 0.0f, 5.0f, 0.0f)));
        entries.add(new Box3DSampleEntry("Robustness", "HighMassRatio1", HighMassRatio1Sample::new,
                new Box3DSampleCamera(30.0f, 15.0f, 70.0f, 0.0f, 0.0f, 0.0f)));
        entries.add(new Box3DSampleEntry("Robustness", "Tiny Pyramid", TinyPyramidSample::new,
                new Box3DSampleCamera(-30.0f, 20.0f, 10.0f, 0.0f, 0.5f, 0.0f)));
        entries.add(new Box3DSampleEntry("Robustness", "Overlap Recovery", OverlapRecoverySample::new,
                new Box3DSampleCamera(45.0f, 20.0f, 15.0f, 0.0f, 0.0f, 0.0f)));
        entries.add(new Box3DSampleEntry("Robustness", "Overflow Color Pile", OverflowColorPileSample::new,
                new Box3DSampleCamera(30.0f, 35.0f, 15.0f, 0.0f, 0.0f, 0.0f)));
        entries.add(new Box3DSampleEntry("World", "Far Stack", FarStackSample::new,
                new Box3DSampleCamera(FarStackSample.offset(), 8.0f, 16.0f, FarStackSample.offset(), 2.0f, 0.0f)));
        entries.add(new Box3DSampleEntry("World", "Far Pyramid", FarPyramidSample::new,
                new Box3DSampleCamera(FarPyramidSample.offset() + 40.0f, -10.0f, 60.0f,
                        FarPyramidSample.offset(), 20.0f, 0.0f)));
        entries.add(new Box3DSampleEntry("World", "Far Ragdolls", FarRagdollsSample::new,
                new Box3DSampleCamera(FarRagdollsSample.offset() + 25.0f, 30.0f, 20.0f,
                        FarRagdollsSample.offset(), 2.0f, 0.0f)));
        entries.add(new Box3DSampleEntry("World", "Far Mesh Drop", FarMeshDropSample::new,
                new Box3DSampleCamera(10025.0f, 20.0f, 10065.0f, 10000.0f, 10.0f, 10000.0f)));
        entries.add(new Box3DSampleEntry("Collision", "Capsule Cast Ray", CapsuleCastRaySample::new,
                new Box3DSampleCamera(25.0f, 14.0f, 30.0f, 0.0f, 5.0f, 0.0f)));
        entries.add(new Box3DSampleEntry("Collision", "Cast World", CastWorldSample::new,
                new Box3DSampleCamera(25.0f, 14.0f, 32.0f, 0.0f, 5.0f, 0.0f)));
        entries.add(new Box3DSampleEntry("Collision", "Distance Debug", DistanceDebugSample::new,
                new Box3DSampleCamera(20.0f, 12.0f, 22.0f, 0.0f, 4.0f, 0.0f)));
        entries.add(new Box3DSampleEntry("Collision", "Initial Overlap", InitialOverlapSample::new,
                new Box3DSampleCamera(20.0f, 12.0f, 20.0f, 0.0f, 4.0f, 0.0f)));
        entries.add(new Box3DSampleEntry("Collision", "Long Ray Cast", LongRayCastSample::new,
                new Box3DSampleCamera(30.0f, 15.0f, 42.0f, 0.0f, 6.0f, 0.0f)));
        entries.add(new Box3DSampleEntry("Collision", "Mesh Scale", MeshScaleSample::new,
                new Box3DSampleCamera(30.0f, 15.0f, 36.0f, 0.0f, 5.0f, 0.0f)));
        entries.add(new Box3DSampleEntry("Collision", "Overlap World", OverlapWorldSample::new,
                new Box3DSampleCamera(25.0f, 14.0f, 28.0f, 0.0f, 6.0f, 0.0f)));
        entries.add(new Box3DSampleEntry("Collision", "Ray Curtain", RayCurtainSample::new,
                new Box3DSampleCamera(30.0f, 15.0f, 38.0f, 0.0f, 6.0f, 0.0f)));
        entries.add(new Box3DSampleEntry("Collision", "Shape Cast", ShapeCastSample::new,
                new Box3DSampleCamera(25.0f, 14.0f, 28.0f, 0.0f, 5.0f, 0.0f)));
        entries.add(new Box3DSampleEntry("Collision", "Shape Cast Debug", ShapeCastDebugSample::new,
                new Box3DSampleCamera(25.0f, 14.0f, 30.0f, 0.0f, 5.0f, 0.0f)));
        entries.add(new Box3DSampleEntry("Collision", "Shape Distance", ShapeDistanceSample::new,
                new Box3DSampleCamera(20.0f, 12.0f, 22.0f, 0.0f, 4.0f, 0.0f)));
        entries.add(new Box3DSampleEntry("Collision", "Time of Impact", TimeOfImpactSample::new,
                new Box3DSampleCamera(25.0f, 14.0f, 28.0f, 0.0f, 5.0f, 0.0f)));
        entries.add(new Box3DSampleEntry("Geometry", "Box Hull", GeometryBoxHullSample::new,
                new Box3DSampleCamera(20.0f, 12.0f, 22.0f, 0.0f, 4.0f, 0.0f)));
        entries.add(new Box3DSampleEntry("Geometry", "Capsule Mass", GeometryCapsuleMassSample::new,
                new Box3DSampleCamera(20.0f, 12.0f, 22.0f, 0.0f, 4.0f, 0.0f)));
        entries.add(new Box3DSampleEntry("Geometry", "Hull", GeometryHullSample::new,
                new Box3DSampleCamera(20.0f, 12.0f, 24.0f, 0.0f, 4.0f, 0.0f)));
        entries.add(new Box3DSampleEntry("Geometry", "Hull Reduction", GeometryHullReductionSample::new,
                new Box3DSampleCamera(20.0f, 12.0f, 24.0f, 0.0f, 4.0f, 0.0f)));
        entries.add(new Box3DSampleEntry("Geometry", "Hull Transform", GeometryHullTransformSample::new,
                new Box3DSampleCamera(20.0f, 12.0f, 24.0f, 0.0f, 4.0f, 0.0f)));
        entries.add(new Box3DSampleEntry("Joints", "Driving", DrivingSample::new,
                new Box3DSampleCamera(20.0f, 10.0f, 22.0f, 0.0f, 3.0f, 0.0f)));
        entries.add(new Box3DSampleEntry("Joints", "Gear Lift", GearLiftSample::new,
                new Box3DSampleCamera(20.0f, 12.0f, 22.0f, 0.0f, 4.0f, 0.0f)));
        entries.add(new Box3DSampleEntry("Manifold", "Capsule vs Capsule", ManifoldCapsuleVsCapsuleSample::new,
                new Box3DSampleCamera(15.0f, 10.0f, 18.0f, 0.0f, 4.0f, 0.0f)));
        entries.add(new Box3DSampleEntry("Manifold", "Capsule vs Hull", ManifoldCapsuleVsHullSample::new,
                new Box3DSampleCamera(15.0f, 10.0f, 18.0f, 0.0f, 4.0f, 0.0f)));
        entries.add(new Box3DSampleEntry("Manifold", "Capsule vs Sphere", ManifoldCapsuleVsSphereSample::new,
                new Box3DSampleCamera(15.0f, 10.0f, 18.0f, 0.0f, 4.0f, 0.0f)));
        entries.add(new Box3DSampleEntry("Manifold", "Hull vs Hull", ManifoldHullVsHullSample::new,
                new Box3DSampleCamera(15.0f, 10.0f, 18.0f, 0.0f, 4.0f, 0.0f)));
        entries.add(new Box3DSampleEntry("Manifold", "Hull vs Sphere", ManifoldHullVsSphereSample::new,
                new Box3DSampleCamera(15.0f, 10.0f, 18.0f, 0.0f, 4.0f, 0.0f)));
        entries.add(new Box3DSampleEntry("Manifold", "Sphere vs Sphere", ManifoldSphereVsSphereSample::new,
                new Box3DSampleCamera(15.0f, 10.0f, 18.0f, 0.0f, 4.0f, 0.0f)));
        entries.add(new Box3DSampleEntry("Manifold", "Triangle vs Capsule", ManifoldTriangleVsCapsuleSample::new,
                new Box3DSampleCamera(15.0f, 10.0f, 18.0f, 0.0f, 4.0f, 0.0f)));
        entries.add(new Box3DSampleEntry("Manifold", "Triangle vs Hull", ManifoldTriangleVsHullSample::new,
                new Box3DSampleCamera(15.0f, 10.0f, 18.0f, 0.0f, 4.0f, 0.0f)));
        entries.add(new Box3DSampleEntry("Manifold", "Triangle vs Sphere", ManifoldTriangleVsSphereSample::new,
                new Box3DSampleCamera(15.0f, 10.0f, 18.0f, 0.0f, 4.0f, 0.0f)));
        entries.add(new Box3DSampleEntry("Mesh", "Big Box", MeshBigBoxSample::new,
                new Box3DSampleCamera(25.0f, 14.0f, 30.0f, 0.0f, 5.0f, 0.0f)));
        entries.add(new Box3DSampleEntry("Mesh", "Box", MeshBoxSample::new,
                new Box3DSampleCamera(25.0f, 14.0f, 28.0f, 0.0f, 5.0f, 0.0f)));
        entries.add(new Box3DSampleEntry("Mesh", "Creation Benchmark", MeshCreationBenchmarkSample::new,
                new Box3DSampleCamera(30.0f, 15.0f, 36.0f, 0.0f, 5.0f, 0.0f)));
        entries.add(new Box3DSampleEntry("Mesh", "Grid", MeshGridSample::new,
                new Box3DSampleCamera(30.0f, 15.0f, 34.0f, 0.0f, 5.0f, 0.0f)));
        entries.add(new Box3DSampleEntry("Mesh", "Height Field", MeshHeightFieldSample::new,
                new Box3DSampleCamera(30.0f, 15.0f, 38.0f, 0.0f, 5.0f, 0.0f)));
        entries.add(new Box3DSampleEntry("Mesh", "Hollow Box", MeshHollowBoxSample::new,
                new Box3DSampleCamera(25.0f, 14.0f, 28.0f, 0.0f, 5.0f, 0.0f)));
        entries.add(new Box3DSampleEntry("Mesh", "Reflection", MeshReflectionSample::new,
                new Box3DSampleCamera(25.0f, 14.0f, 28.0f, 0.0f, 5.0f, 0.0f)));
        entries.add(new Box3DSampleEntry("Mesh", "Viewer", MeshViewerSample::new,
                new Box3DSampleCamera(25.0f, 14.0f, 28.0f, 0.0f, 5.0f, 0.0f)));
        entries.add(new Box3DSampleEntry("Mesh", "Voxel", MeshVoxelSample::new,
                new Box3DSampleCamera(25.0f, 14.0f, 28.0f, 0.0f, 5.0f, 0.0f)));
        return Collections.unmodifiableList(entries);
    }
}
