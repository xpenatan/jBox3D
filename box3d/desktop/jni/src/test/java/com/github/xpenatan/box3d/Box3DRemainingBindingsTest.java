package com.github.xpenatan.box3d;

import com.github.xpenatan.jParser.api.NativeObject;
import com.github.xpenatan.jParser.loader.JParserLibraryLoaderListener;
import com.github.xpenatan.jparser.runtime.helper.NativeString;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

/**
 * Runtime coverage for the Box3D C APIs that were absent from the original
 * WebIDL/native wrapper. These tests deliberately call every added entry point;
 * merely generating or linking a symbol is not sufficient coverage.
 */
public class Box3DRemainingBindingsTest {

    private static final float EPSILON = 0.0001f;
    private static final long TREE_USER_DATA = 0x0012_3456_78AB_CDEFL;

    @Test
    public void baseTimingAndMathBindingsExecute() throws Exception {
        loadBox3D();

        AtomicInteger allocations = new AtomicInteger();
        AtomicInteger frees = new AtomicInteger();
        AtomicInteger assertions = new AtomicInteger();
        B3AllocatorEm allocator = new B3AllocatorEm() {
            @Override
            protected long Allocate(int size, int alignment) {
                allocations.incrementAndGet();
                return B3.AllocateMemory(size, alignment);
            }

            @Override
            protected void Free(long address) {
                frees.incrementAndGet();
                B3.FreeMemory(address);
            }
        };
        B3AssertCallbackEm assertCallback = new B3AssertCallbackEm() {
            @Override
            protected boolean Assert(String condition, String fileName, int lineNumber) {
                assertEquals("binding-condition", condition);
                assertEquals("binding-test.java", fileName);
                assertEquals(37, lineNumber);
                assertions.incrementAndGet();
                return false;
            }
        };
        B3LogCallbackEm logCallback = new B3LogCallbackEm();
        B3ByteArray bytes = new B3ByteArray(3);
        B3Timer timer = new B3Timer();
        B3Vec3 x = new B3Vec3(1.0f, 0.0f, 0.0f);
        B3Vec3 y = new B3Vec3(0.0f, 1.0f, 0.0f);
        B3Vec3 z = new B3Vec3(0.0f, 0.0f, 1.0f);
        B3Matrix3 identity = new B3Matrix3(x, y, z);
        B3Quat quaternion = null;
        B3Vec3 origin = new B3Vec3(1.0f, 2.0f, 3.0f);
        B3Matrix3 inertia = null;
        B3Vec3 segmentA = new B3Vec3(0.0f, 0.0f, 0.0f);
        B3Vec3 segmentB = new B3Vec3(2.0f, 0.0f, 0.0f);
        B3Vec3 query = new B3Vec3(1.0f, 3.0f, 0.0f);
        B3Vec3 closest = null;
        B3Vec3 lineP1 = new B3Vec3(0.0f, 0.0f, 0.0f);
        B3Vec3 lineD1 = new B3Vec3(1.0f, 0.0f, 0.0f);
        B3Vec3 lineP2 = new B3Vec3(0.0f, 1.0f, 0.0f);
        B3Vec3 lineD2 = new B3Vec3(0.0f, -1.0f, 0.0f);
        B3SegmentDistanceResult lineResult = null;
        B3Vec3 segmentP2 = new B3Vec3(1.0f, 1.0f, 0.0f);
        B3Vec3 segmentQ2 = new B3Vec3(1.0f, -1.0f, 0.0f);
        B3SegmentDistanceResult segmentResult = null;
        B3Plane plane = new B3Plane(y, 2.0f);
        B3Transform transform = new B3Transform();
        B3Vec3 rayOrigin = new B3Vec3(0.0f, 2.0f, 0.0f);
        B3Vec3 rayTranslation = new B3Vec3(0.0f, -4.0f, 0.0f);
        B3RayCastInput ray = new B3RayCastInput(rayOrigin, rayTranslation, 1.0f);

        try {
            bytes.SetValue(0, 'a');
            bytes.SetValue(1, 'b');
            bytes.SetValue(2, 'c');
            assertEquals(193485963L, B3.Hash(5381L, bytes));
            assertTrue(B3.GetByteCount() >= 0);

            long ticks = B3.GetTicks();
            B3.Yield();
            B3.Sleep(2);
            assertTrue(B3.GetMilliseconds(ticks) >= 0.0f);
            assertTrue(timer.GetTicks() != 0L);
            assertTrue(timer.GetMilliseconds() >= 0.0f);
            assertTrue(timer.GetMillisecondsAndReset() >= 0.0f);
            timer.Reset();

            B3.SetAllocatorCallback(allocator);
            B3DynamicTree allocationProbe = new B3DynamicTree(4);
            allocationProbe.Destroy();
            dispose(allocationProbe);
            B3.SetAllocatorCallback(B3AllocatorEm.NULL);
            assertTrue("the native allocator callback must be invoked", allocations.get() > 0);
            assertEquals("every callback allocation must be released", allocations.get(), frees.get());

            B3.SetAssertCallback(assertCallback);
            assertEquals(0, B3.InternalAssert("binding-condition", "binding-test.java", 37));
            B3.SetAssertCallback(B3AssertCallbackEm.NULL);
            assertEquals(1, assertions.get());
            B3.SetLogCallback(logCallback);
            B3.SetLogCallback(B3LogCallbackEm.NULL);

            B3CosSin cosSin = B3.ComputeCosSin((float)Math.PI * 0.5f);
            assertEquals(0.0f, cosSin.GetCosine(), 0.0002f);
            assertEquals(1.0f, cosSin.GetSine(), 0.0002f);
            dispose(cosSin);

            quaternion = B3.MakeQuatFromMatrix(identity);
            assertEquals(0.0f, quaternion.GetV().GetX(), EPSILON);
            assertEquals(0.0f, quaternion.GetV().GetY(), EPSILON);
            assertEquals(0.0f, quaternion.GetV().GetZ(), EPSILON);
            assertEquals(1.0f, Math.abs(quaternion.GetS()), EPSILON);

            inertia = B3.Steiner(2.0f, origin);
            assertEquals(26.0f, inertia.GetColumnX().GetX(), EPSILON);
            assertEquals(20.0f, inertia.GetColumnY().GetY(), EPSILON);
            assertEquals(10.0f, inertia.GetColumnZ().GetZ(), EPSILON);

            closest = B3.PointToSegmentDistance(segmentA, segmentB, query);
            assertEquals(1.0f, closest.GetX(), EPSILON);
            assertEquals(0.0f, closest.GetY(), EPSILON);
            assertEquals(0.0f, closest.GetZ(), EPSILON);

            lineResult = B3.LineDistance(lineP1, lineD1, lineP2, lineD2);
            assertEquals(0.0f, lineResult.GetPoint1().GetX(), EPSILON);
            assertEquals(0.0f, lineResult.GetPoint1().GetY(), EPSILON);
            assertEquals(0.0f, lineResult.GetPoint2().GetX(), EPSILON);
            assertEquals(0.0f, lineResult.GetPoint2().GetY(), EPSILON);

            segmentResult = B3.SegmentDistance(segmentA, segmentB, segmentP2, segmentQ2);
            assertEquals(0.5f, segmentResult.GetFraction1(), EPSILON);
            assertEquals(0.5f, segmentResult.GetFraction2(), EPSILON);
            assertEquals(1.0f, segmentResult.GetPoint1().GetX(), EPSILON);
            assertEquals(1.0f, segmentResult.GetPoint2().GetX(), EPSILON);

            assertTrue(B3.IsValidMatrix3(identity));
            assertTrue(B3.IsValidPlane(plane));
            assertTrue(B3.IsValidPosition(origin));
            assertTrue(B3.IsValidWorldTransform(transform));
            assertTrue(B3.IsValidRay(ray));
        }
        finally {
            B3.SetAllocatorCallback(B3AllocatorEm.NULL);
            B3.SetAssertCallback(B3AssertCallbackEm.NULL);
            B3.SetLogCallback(B3LogCallbackEm.NULL);
            dispose(ray, rayTranslation, rayOrigin, transform, plane, segmentResult, segmentQ2, segmentP2,
                    lineResult, lineD2, lineP2, lineD1, lineP1, closest, query, segmentB, segmentA, inertia,
                    origin, quaternion, identity, z, y, x, timer, bytes, logCallback, assertCallback, allocator);
        }
    }

    @Test
    public void everyDynamicTreeBindingExecutes() throws Exception {
        loadBox3D();

        Path savedTree = Files.createTempFile("jbox3d-tree-", ".bin");
        B3Vec3 lower1 = new B3Vec3(-1.0f, -1.0f, -1.0f);
        B3Vec3 upper1 = new B3Vec3(1.0f, 1.0f, 1.0f);
        B3AABB box1 = new B3AABB(lower1, upper1);
        B3Vec3 lower2 = new B3Vec3(3.0f, -1.0f, -1.0f);
        B3Vec3 upper2 = new B3Vec3(5.0f, 1.0f, 1.0f);
        B3AABB box2 = new B3AABB(lower2, upper2);
        B3Vec3 movedLower = new B3Vec3(2.0f, -2.0f, -2.0f);
        B3Vec3 movedUpper = new B3Vec3(6.0f, 2.0f, 2.0f);
        B3AABB movedBox = new B3AABB(movedLower, movedUpper);
        B3Vec3 queryLower = new B3Vec3(-10.0f, -10.0f, -10.0f);
        B3Vec3 queryUpper = new B3Vec3(10.0f, 10.0f, 10.0f);
        B3AABB queryBox = new B3AABB(queryLower, queryUpper);
        AtomicInteger queryCalls = new AtomicInteger();
        AtomicLong callbackUserData = new AtomicLong();
        B3TreeQueryCallbackEm queryCallback = new B3TreeQueryCallbackEm() {
            @Override
            protected boolean Query(int proxyId, long userData) {
                queryCalls.incrementAndGet();
                if(userData == TREE_USER_DATA) {
                    callbackUserData.set(userData);
                }
                return true;
            }
        };
        AtomicInteger closestCalls = new AtomicInteger();
        B3TreeClosestCallbackEm closestCallback = new B3TreeClosestCallbackEm() {
            @Override
            protected float QueryClosest(float distanceSquaredMin, int proxyId, long userData) {
                closestCalls.incrementAndGet();
                callbackUserData.set(userData);
                return Math.min(distanceSquaredMin, 0.25f);
            }
        };
        AtomicInteger rayCalls = new AtomicInteger();
        B3TreeRayCastCallbackEm rayCallback = new B3TreeRayCastCallbackEm() {
            @Override
            protected float RayCast(B3RayCastInput input, int proxyId, long userData) {
                rayCalls.incrementAndGet();
                callbackUserData.set(userData);
                return input.GetMaxFraction();
            }
        };
        AtomicInteger boxCalls = new AtomicInteger();
        B3TreeBoxCastCallbackEm boxCallback = new B3TreeBoxCastCallbackEm() {
            @Override
            protected float BoxCast(B3BoxCastInput input, int proxyId, long userData) {
                boxCalls.incrementAndGet();
                callbackUserData.set(userData);
                return input.GetMaxFraction();
            }
        };
        B3DynamicTree tree = new B3DynamicTree(4);
        B3DynamicTree loadedTree = null;
        B3Vec3 closestPoint = new B3Vec3(0.0f, 0.0f, 0.0f);
        B3Vec3 rayOrigin = new B3Vec3(-5.0f, 0.0f, 0.0f);
        B3Vec3 rayTranslation = new B3Vec3(12.0f, 0.0f, 0.0f);
        B3RayCastInput rayInput = new B3RayCastInput(rayOrigin, rayTranslation, 1.0f);
        B3Vec3 castLower = new B3Vec3(-5.5f, -0.5f, -0.5f);
        B3Vec3 castUpper = new B3Vec3(-4.5f, 0.5f, 0.5f);
        B3AABB castBox = new B3AABB(castLower, castUpper);
        B3BoxCastInput boxInput = new B3BoxCastInput(castBox, rayTranslation, 1.0f);

        try {
            assertTrue(tree.IsValid());
            int proxy1 = tree.CreateProxy(box1, 0x1L, TREE_USER_DATA);
            int proxy2 = tree.CreateProxy(box2, 0x2L, 77L);
            assertEquals(2, tree.GetProxyCount());
            assertEquals(0x1L, tree.GetCategoryBits(proxy1));
            assertEquals(TREE_USER_DATA, tree.GetUserData(proxy1));
            assertEquals(-1.0f, tree.GetAABB(proxy1).GetLowerBound().GetX(), EPSILON);

            tree.SetCategoryBits(proxy1, 0x3L);
            assertEquals(0x3L, tree.GetCategoryBits(proxy1));
            tree.MoveProxy(proxy2, movedBox);
            tree.EnlargeProxy(proxy1, box1);

            B3TreeStats queryStats = tree.Query(queryBox, -1L, false, queryCallback);
            assertTrue(queryStats.GetNodeVisits() > 0);
            assertTrue(queryStats.GetLeafVisits() >= 2);
            assertEquals(2, queryCalls.get());
            assertEquals(TREE_USER_DATA, callbackUserData.get());
            dispose(queryStats);

            callbackUserData.set(0L);
            B3TreeClosestResult closestResult = tree.QueryClosest(closestPoint, -1L, false, closestCallback,
                    Float.MAX_VALUE);
            assertTrue(closestCalls.get() > 0);
            assertTrue(closestResult.GetStats().GetLeafVisits() > 0);
            assertEquals(0.25f, closestResult.GetMinDistanceSquared(), EPSILON);
            assertNotEquals(0L, callbackUserData.get());
            dispose(closestResult);

            callbackUserData.set(0L);
            B3TreeStats rayStats = tree.RayCast(rayInput, -1L, false, rayCallback);
            assertTrue(rayCalls.get() > 0);
            assertTrue(rayStats.GetLeafVisits() > 0);
            assertNotEquals(0L, callbackUserData.get());
            dispose(rayStats);

            callbackUserData.set(0L);
            B3TreeStats boxStats = tree.BoxCast(boxInput, -1L, false, boxCallback);
            assertTrue(boxCalls.get() > 0);
            assertTrue(boxStats.GetLeafVisits() > 0);
            assertNotEquals(0L, callbackUserData.get());
            dispose(boxStats);

            assertTrue(tree.GetHeight() >= 0);
            assertTrue(Float.isFinite(tree.GetAreaRatio()));
            assertTrue(tree.GetAreaRatio() >= 0.0f);
            assertTrue(tree.GetRootBounds().GetUpperBound().GetX() >= 5.0f);
            assertTrue(tree.GetByteCount() > 0);
            tree.Validate();
            assertTrue(tree.Rebuild(true) >= 0);
            tree.ValidateNoEnlarged();

            tree.Save(savedTree.toString());
            assertTrue(Files.size(savedTree) > 0L);
            loadedTree = B3DynamicTree.Load(savedTree.toString(), 2.0f);
            assertTrue(loadedTree.IsValid());
            assertEquals(2, loadedTree.GetProxyCount());
            assertEquals(-2.0f, loadedTree.GetAABB(proxy1).GetLowerBound().GetX(), EPSILON);
            loadedTree.Validate();

            tree.DestroyProxy(proxy2);
            assertEquals(1, tree.GetProxyCount());
            tree.DestroyProxy(proxy1);
            assertEquals(0, tree.GetProxyCount());
        }
        finally {
            if(loadedTree != null && loadedTree.IsValid()) {
                loadedTree.Destroy();
            }
            if(tree.IsValid()) {
                tree.Destroy();
            }
            Files.deleteIfExists(savedTree);
            dispose(boxInput, castBox, castUpper, castLower, rayInput, rayTranslation, rayOrigin, closestPoint,
                    loadedTree, tree, boxCallback, rayCallback, closestCallback, queryCallback, queryBox,
                    queryUpper, queryLower, movedBox, movedUpper, movedLower, box2, upper2, lower2, box1,
                    upper1, lower1);
        }
    }

    @Test
    public void meshHeightFieldAndCompoundBindingsExecute() throws Exception {
        loadBox3D();

        Path heightFile = Files.createTempFile("jbox3d-height-", ".bin");
        B3Vec3 zero = new B3Vec3(0.0f, 0.0f, 0.0f);
        B3Vec3 one = new B3Vec3(1.0f, 1.0f, 1.0f);
        B3Vec3 meshExtents = new B3Vec3(2.0f, 1.0f, 3.0f);
        B3Mesh mesh = B3Mesh.CreateBox(zero, meshExtents, true);
        B3Vec3 queryLower = new B3Vec3(-10.0f, -10.0f, -10.0f);
        B3Vec3 queryUpper = new B3Vec3(10.0f, 10.0f, 10.0f);
        B3AABB queryBounds = new B3AABB(queryLower, queryUpper);
        B3MeshQueryResult meshQuery = null;
        B3HeightFieldDef heightDef = new B3HeightFieldDef(3, 4);
        B3Vec3 heightScale = new B3Vec3(2.0f, 0.5f, 3.0f);
        B3HeightField heightField = null;
        B3HeightField loadedHeightField = null;
        B3Vec3 capsulePoint1 = new B3Vec3(-1.0f, 0.0f, 0.0f);
        B3Vec3 capsulePoint2 = new B3Vec3(1.0f, 0.0f, 0.0f);
        B3Capsule capsule = new B3Capsule(capsulePoint1, capsulePoint2, 0.25f);
        B3Hull hull = B3Hull.CreateBox(0.5f, 0.75f, 1.0f);
        B3Sphere sphere = new B3Sphere(zero, 0.75f);
        B3Transform childTransform = new B3Transform();
        B3SurfaceMaterial capsuleMaterial = new B3SurfaceMaterial();
        B3SurfaceMaterial hullMaterial = new B3SurfaceMaterial();
        B3SurfaceMaterial meshMaterial = new B3SurfaceMaterial();
        B3SurfaceMaterial sphereMaterial = new B3SurfaceMaterial();
        B3SurfaceMaterialArray meshMaterials = null;
        B3CompoundDef compoundDef = new B3CompoundDef(1, 1, 1, 1);
        B3Compound compound = null;
        B3ByteArray compoundBytes = null;
        B3Compound restoredCompound = null;
        B3Hull compoundHullView = null;
        B3Mesh compoundMeshView = null;
        B3WorldDef worldDef = new B3WorldDef();
        B3World world = null;
        B3BodyDef bodyDef = new B3BodyDef();
        B3ShapeDef shapeDef = new B3ShapeDef();
        B3Body meshBody = null;
        B3Body heightBody = null;
        B3Shape meshShape = null;
        B3Shape heightShape = null;
        B3Mesh shapeMeshView = null;
        B3HeightField shapeHeightView = null;

        try {
            assertTrue(mesh.IsValid());
            assertTrue(mesh.GetTreeHeight() >= 0);
            meshQuery = mesh.Query(queryBounds);
            assertEquals(mesh.GetTriangleCount(), meshQuery.GetSize());
            assertTrue(meshQuery.GetSize() > 0);
            B3MeshTriangle triangle = meshQuery.GetValue(0);
            assertTrue(triangle.GetTriangleIndex() >= 0);
            assertTrue(B3.IsValidVec3(triangle.GetA()));
            assertTrue(B3.IsValidVec3(triangle.GetB()));
            assertTrue(B3.IsValidVec3(triangle.GetC()));

            for(int zIndex = 0; zIndex < heightDef.GetCountZ(); zIndex++) {
                for(int xIndex = 0; xIndex < heightDef.GetCountX(); xIndex++) {
                    heightDef.SetHeight(xIndex, zIndex, (float)(xIndex - zIndex) * 0.25f);
                    assertEquals((float)(xIndex - zIndex) * 0.25f,
                            heightDef.GetHeight(xIndex, zIndex), EPSILON);
                }
            }
            heightDef.SetMaterialIndex(0, 0, 7);
            assertEquals(7, heightDef.GetMaterialIndex(0, 0));
            heightDef.SetScale(heightScale);
            heightDef.SetGlobalMinimumHeight(-2.0f);
            heightDef.SetGlobalMaximumHeight(2.0f);
            heightDef.SetClockwiseWinding(true);
            assertEquals(2.0f, heightDef.GetScale().GetX(), EPSILON);
            assertEquals(-2.0f, heightDef.GetGlobalMinimumHeight(), EPSILON);
            assertEquals(2.0f, heightDef.GetGlobalMaximumHeight(), EPSILON);
            assertTrue(heightDef.GetClockwiseWinding());

            heightDef.Dump(heightFile.toString());
            assertTrue(Files.size(heightFile) > 0L);
            heightField = B3HeightField.CreateFromDef(heightDef);
            assertTrue(heightField.IsValid());
            assertEquals(4, heightField.GetRowCount());
            assertEquals(3, heightField.GetColumnCount());
            assertEquals(3.0f, heightField.GetScale().GetZ(), EPSILON);
            loadedHeightField = B3HeightField.Load(heightFile.toString());
            assertTrue(loadedHeightField.IsValid());
            assertEquals(heightField.GetRowCount(), loadedHeightField.GetRowCount());
            assertEquals(heightField.GetColumnCount(), loadedHeightField.GetColumnCount());

            capsuleMaterial.SetUserMaterialId(101L);
            hullMaterial.SetUserMaterialId(102L);
            meshMaterial.SetUserMaterialId(103L);
            sphereMaterial.SetUserMaterialId(104L);
            meshMaterials = new B3SurfaceMaterialArray(mesh.GetMaterialCount());
            for(int i = 0; i < meshMaterials.GetSize(); i++) {
                meshMaterials.SetValue(i, meshMaterial);
            }
            compoundDef.AddCapsule(capsule, capsuleMaterial);
            compoundDef.AddHull(hull, childTransform, hullMaterial);
            compoundDef.AddMesh(mesh, childTransform, one, meshMaterials);
            compoundDef.AddSphere(sphere, sphereMaterial);
            assertEquals(1, compoundDef.GetCapsuleCount());
            assertEquals(1, compoundDef.GetHullCount());
            assertEquals(1, compoundDef.GetMeshCount());
            assertEquals(1, compoundDef.GetSphereCount());

            compound = B3Compound.CreateFromDef(compoundDef);
            assertTrue(compound.IsValid());
            assertCompoundContents(compound);
            compoundHullView = compound.GetHull(0);
            compoundMeshView = compound.GetMesh(0);
            assertTrue(compoundHullView.IsValid());
            assertTrue(compoundMeshView.IsValid());
            assertEquals(0.0f, compound.GetHullTransform(0).GetP().GetX(), EPSILON);
            assertEquals(0.0f, compound.GetMeshTransform(0).GetP().GetX(), EPSILON);
            assertEquals(1.0f, compound.GetMeshScale(0).GetX(), EPSILON);
            assertEquals(101L, compound.GetMaterial(compound.GetCapsuleMaterialIndex(0)).GetUserMaterialId());
            assertEquals(102L, compound.GetMaterial(compound.GetHullMaterialIndex(0)).GetUserMaterialId());
            assertEquals(103L, compound.GetMaterial(compound.GetMeshMaterialIndex(0, 0)).GetUserMaterialId());
            assertEquals(104L, compound.GetMaterial(compound.GetSphereMaterialIndex(0)).GetUserMaterialId());

            compoundBytes = compound.ToBytes();
            assertTrue(compoundBytes.GetSize() > 0);
            assertTrue(compound.IsValid());
            restoredCompound = B3Compound.CreateFromBytes(compoundBytes);
            assertTrue(restoredCompound.IsValid());
            assertCompoundContents(restoredCompound);

            world = new B3World(worldDef);
            meshBody = world.CreateBody(bodyDef);
            meshShape = meshBody.CreateMeshShape(shapeDef, mesh, one);
            shapeMeshView = meshShape.GetMesh();
            assertTrue(shapeMeshView.IsValid());
            assertEquals(mesh.GetTriangleCount(), shapeMeshView.GetTriangleCount());

            B3BodyDef positionedBodyDef = new B3BodyDef();
            B3Vec3 heightPosition = new B3Vec3(0.0f, -10.0f, 0.0f);
            positionedBodyDef.SetPosition(heightPosition);
            heightBody = world.CreateBody(positionedBodyDef);
            heightShape = heightBody.CreateHeightFieldShape(shapeDef, heightField);
            shapeHeightView = heightShape.GetHeightField();
            assertTrue(shapeHeightView.IsValid());
            assertEquals(heightField.GetRowCount(), shapeHeightView.GetRowCount());
            dispose(heightPosition, positionedBodyDef);
        }
        finally {
            if(world != null && world.IsValid()) {
                world.Destroy();
            }
            if(restoredCompound != null && restoredCompound.IsValid()) {
                restoredCompound.Destroy();
            }
            if(compound != null && compound.IsValid()) {
                compound.Destroy();
            }
            if(loadedHeightField != null && loadedHeightField.IsValid()) {
                loadedHeightField.Destroy();
            }
            if(heightField != null && heightField.IsValid()) {
                heightField.Destroy();
            }
            if(mesh.IsValid()) {
                mesh.Destroy();
            }
            Files.deleteIfExists(heightFile);
            dispose(shapeHeightView, shapeMeshView, heightShape, meshShape, heightBody, meshBody, shapeDef, bodyDef,
                    world, worldDef, restoredCompound, compoundBytes, compoundMeshView, compoundHullView, compound,
                    compoundDef, meshMaterials, sphereMaterial, meshMaterial, hullMaterial, capsuleMaterial,
                    childTransform, sphere, hull, capsule, capsulePoint2, capsulePoint1, loadedHeightField,
                    heightField, heightScale, heightDef, meshQuery, queryBounds, queryUpper, queryLower, mesh,
                    meshExtents, one, zero);
        }
    }

    private static void assertCompoundContents(B3Compound compound) {
        assertEquals(1, compound.GetCapsuleCount());
        assertEquals(1, compound.GetHullCount());
        assertEquals(1, compound.GetMeshCount());
        assertEquals(1, compound.GetSphereCount());
        assertEquals(0.25f, compound.GetCapsule(0).GetRadius(), EPSILON);
        assertEquals(0.75f, compound.GetSphere(0).GetRadius(), EPSILON);
    }

    @Test
    public void worldCallbacksUserDataContactsAndDiagnosticsExecute() throws Exception {
        loadBox3D();

        final long groundMaterialId = 0x0011_2233_4455_6677L;
        final long sphereMaterialId = 0x0022_3344_5566_7788L;
        Path boundsFile = Paths.get("box3d_bounds.txt").toAbsolutePath();
        Files.deleteIfExists(boundsFile);

        AtomicInteger preSolveCalls = new AtomicInteger();
        AtomicInteger frictionCalls = new AtomicInteger();
        AtomicInteger restitutionCalls = new AtomicInteger();
        AtomicInteger castCalls = new AtomicInteger();
        AtomicLong castShapeId = new AtomicLong();
        AtomicLong castMaterialId = new AtomicLong();
        B3PreSolveCallbackEm preSolve = new B3PreSolveCallbackEm() {
            @Override
            protected boolean PreSolve(long shapeIdA, long shapeIdB, B3Vec3 point, B3Vec3 normal) {
                assertNotEquals(0L, shapeIdA);
                assertNotEquals(0L, shapeIdB);
                assertTrue(B3.IsValidVec3(point));
                assertTrue(B3.IsValidVec3(normal));
                preSolveCalls.incrementAndGet();
                return true;
            }
        };
        B3FrictionCallbackEm friction = new B3FrictionCallbackEm() {
            @Override
            protected float MixFriction(float frictionA, long userMaterialIdA, float frictionB,
                    long userMaterialIdB) {
                assertMaterialPair(groundMaterialId, sphereMaterialId, userMaterialIdA, userMaterialIdB);
                frictionCalls.incrementAndGet();
                return 0.35f;
            }
        };
        B3RestitutionCallbackEm restitution = new B3RestitutionCallbackEm() {
            @Override
            protected float MixRestitution(float restitutionA, long userMaterialIdA, float restitutionB,
                    long userMaterialIdB) {
                assertMaterialPair(groundMaterialId, sphereMaterialId, userMaterialIdA, userMaterialIdB);
                restitutionCalls.incrementAndGet();
                return 0.0f;
            }
        };
        B3CastResultCallbackEm castCallback = new B3CastResultCallbackEm() {
            @Override
            protected float ReportHit(B3CastResult result) {
                assertTrue(result.GetFraction() >= 0.0f && result.GetFraction() <= 1.0f);
                assertTrue(B3.IsValidVec3(result.GetPoint()));
                assertTrue(B3.IsValidVec3(result.GetNormal()));
                assertTrue(result.GetTriangleIndex() >= -1);
                assertTrue(result.GetChildIndex() >= -1);
                castShapeId.set(result.GetShapeId());
                castMaterialId.set(result.GetUserMaterialId());
                castCalls.incrementAndGet();
                return result.GetFraction();
            }
        };

        B3WorldDef worldDef = new B3WorldDef();
        B3Vec3 noGravity = new B3Vec3(0.0f, 0.0f, 0.0f);
        worldDef.SetGravity(noGravity);
        B3World world = new B3World(worldDef);
        B3BodyDef groundBodyDef = new B3BodyDef();
        B3Vec3 groundPosition = new B3Vec3(0.0f, -0.5f, 0.0f);
        groundBodyDef.SetPosition(groundPosition);
        B3Body groundBody = world.CreateBody(groundBodyDef);
        B3SurfaceMaterial groundMaterial = new B3SurfaceMaterial();
        groundMaterial.SetFriction(0.4f);
        groundMaterial.SetRestitution(0.1f);
        groundMaterial.SetUserMaterialId(groundMaterialId);
        B3ShapeDef groundShapeDef = new B3ShapeDef();
        groundShapeDef.SetBaseMaterial(groundMaterial);
        groundShapeDef.SetEnableContactEvents(true);
        groundShapeDef.SetEnablePreSolveEvents(true);
        B3Hull groundHull = B3Hull.CreateBox(5.0f, 0.5f, 5.0f);
        B3Shape groundShape = groundBody.CreateHullShape(groundShapeDef, groundHull);

        B3BodyDef sphereBodyDef = new B3BodyDef();
        B3Vec3 spherePosition = new B3Vec3(0.0f, 0.4f, 0.0f);
        sphereBodyDef.SetType(B3.DynamicBody());
        sphereBodyDef.SetPosition(spherePosition);
        B3Body sphereBody = world.CreateBody(sphereBodyDef);
        B3SurfaceMaterial sphereMaterial = new B3SurfaceMaterial();
        sphereMaterial.SetFriction(0.3f);
        sphereMaterial.SetRestitution(0.2f);
        sphereMaterial.SetUserMaterialId(sphereMaterialId);
        B3ShapeDef sphereShapeDef = new B3ShapeDef();
        sphereShapeDef.SetBaseMaterial(sphereMaterial);
        sphereShapeDef.SetDensity(1.0f);
        sphereShapeDef.SetEnableContactEvents(true);
        sphereShapeDef.SetEnablePreSolveEvents(true);
        B3Vec3 sphereCenter = new B3Vec3(0.0f, 0.0f, 0.0f);
        B3Sphere sphere = new B3Sphere(sphereCenter, 0.5f);
        B3Shape sphereShape = sphereBody.CreateSphereShape(sphereShapeDef, sphere);

        B3BodyDef jointBodyDef = new B3BodyDef();
        B3Vec3 jointBodyPosition = new B3Vec3(20.0f, 10.0f, 0.0f);
        jointBodyDef.SetType(B3.DynamicBody());
        jointBodyDef.SetPosition(jointBodyPosition);
        B3Body jointBody = world.CreateBody(jointBodyDef);
        B3FilterJointDef jointDef = new B3FilterJointDef();
        jointDef.SetBodyIdA(groundBody.GetId());
        jointDef.SetBodyIdB(jointBody.GetId());
        B3Joint joint = world.CreateFilterJoint(jointDef);
        B3QueryFilter queryFilter = new B3QueryFilter();
        B3Vec3 rayOrigin = new B3Vec3(0.0f, 5.0f, 0.0f);
        B3Vec3 rayTranslation = new B3Vec3(0.0f, -10.0f, 0.0f);
        B3ContactId contactId = null;
        B3ContactData idContactData = null;
        B3ContactDataArray bodyContacts = null;
        B3ContactDataArray shapeContacts = null;
        B3TreeStats castStats = null;

        try {
            world.SetUserData(0x0010_2030_4050_6070L);
            groundBody.SetUserData(0x0011_2131_4151_6171L);
            sphereShape.SetUserData(0x0012_2232_4252_6272L);
            joint.SetUserData(0x0013_2333_4353_6373L);
            assertEquals(0x0010_2030_4050_6070L, world.GetUserData());
            assertEquals(0x0011_2131_4151_6171L, groundBody.GetUserData());
            assertEquals(0x0012_2232_4252_6272L, sphereShape.GetUserData());
            assertEquals(0x0013_2333_4353_6373L, joint.GetUserData());

            world.SetPreSolveCallback(preSolve);
            world.SetFrictionCallback(friction);
            world.SetRestitutionCallback(restitution);

            castStats = world.CastRay(rayOrigin, rayTranslation, queryFilter, castCallback);
            assertTrue(castStats.GetNodeVisits() > 0);
            assertTrue(castStats.GetLeafVisits() > 0);
            assertTrue(castCalls.get() > 0);
            assertNotEquals(0L, castShapeId.get());
            assertTrue(castMaterialId.get() == sphereMaterialId || castMaterialId.get() == groundMaterialId);

            world.Step(1.0f / 60.0f, 4);
            assertTrue("pre-solve callback was not invoked", preSolveCalls.get() > 0);
            assertTrue("friction callback was not invoked", frictionCalls.get() > 0);
            assertTrue("restitution callback was not invoked", restitutionCalls.get() > 0);

            B3ContactEvents events = world.GetContactEvents();
            assertTrue("the deliberately overlapping shapes must begin contact", events.GetBeginCount() > 0);
            B3ContactId eventId = events.GetBeginEvent(0).GetContactId();
            contactId = new B3ContactId(eventId.GetValue0(), eventId.GetValue1(), eventId.GetValue2());
            assertFalse(contactId.IsNull());
            assertTrue(contactId.IsValid());
            idContactData = contactId.GetData();
            assertContactData(idContactData, groundShape.GetId(), sphereShape.GetId());

            assertTrue(sphereBody.GetContactCapacity() > 0);
            bodyContacts = sphereBody.GetContactData();
            assertTrue(bodyContacts.GetSize() > 0);
            assertContactData(bodyContacts.GetValue(0), groundShape.GetId(), sphereShape.GetId());

            assertTrue(sphereShape.GetContactCapacity() > 0);
            shapeContacts = sphereShape.GetContactData();
            assertTrue(shapeContacts.GetSize() > 0);
            assertContactData(shapeContacts.GetValue(0), groundShape.GetId(), sphereShape.GetId());

            B3Profile profile = world.GetProfile();
            assertProfileIsFinite(profile);
            B3Counters counters = world.GetCounters();
            assertEquals(3, counters.GetBodyCount());
            assertEquals(2, counters.GetShapeCount());
            assertTrue(counters.GetContactCount() > 0);
            assertEquals(1, counters.GetJointCount());
            assertCountersAreReadable(counters);

            world.DumpShapeBounds(B3.DynamicBody());
            assertTrue("b3World_DumpShapeBounds must create output", Files.exists(boundsFile));
            assertTrue("the bounds dump must contain a dynamic shape", Files.readAllLines(boundsFile).size() >= 2);
        }
        finally {
            if(world.IsValid()) {
                world.SetPreSolveCallback(B3PreSolveCallbackEm.NULL);
                world.SetFrictionCallback(B3FrictionCallbackEm.NULL);
                world.SetRestitutionCallback(B3RestitutionCallbackEm.NULL);
                world.Destroy();
            }
            Files.deleteIfExists(boundsFile);
            dispose(castStats, shapeContacts, bodyContacts, idContactData, contactId, rayTranslation, rayOrigin,
                    queryFilter, joint, jointDef, jointBody, jointBodyPosition, jointBodyDef, sphereShape, sphere,
                    sphereCenter, sphereShapeDef, sphereMaterial, sphereBody, spherePosition, sphereBodyDef,
                    groundShape, groundHull, groundShapeDef, groundMaterial, groundBody, groundPosition,
                    groundBodyDef, world, noGravity, worldDef, castCallback, restitution, friction, preSolve);
        }
    }

    private static void assertMaterialPair(long expectedA, long expectedB, long actualA, long actualB) {
        assertTrue((actualA == expectedA && actualB == expectedB) ||
                (actualA == expectedB && actualB == expectedA));
    }

    private static void assertContactData(B3ContactData data, long expectedShapeA, long expectedShapeB) {
        long actualA = data.GetShapeIdA();
        long actualB = data.GetShapeIdB();
        assertTrue((actualA == expectedShapeA && actualB == expectedShapeB) ||
                (actualA == expectedShapeB && actualB == expectedShapeA));
        assertTrue(data.GetContactId().IsValid());
        assertTrue(data.GetManifoldCount() > 0);
        B3Manifold manifold = data.GetManifold(0);
        assertTrue(B3.IsValidVec3(manifold.GetNormal()));
        assertTrue(Float.isFinite(manifold.GetTwistImpulse()));
        assertTrue(B3.IsValidVec3(manifold.GetFrictionImpulse()));
        assertTrue(B3.IsValidVec3(manifold.GetRollingImpulse()));
        assertTrue(manifold.GetPointCount() > 0);
        B3ManifoldPoint point = manifold.GetPoint(0);
        assertTrue(B3.IsValidVec3(point.GetAnchorA()));
        assertTrue(B3.IsValidVec3(point.GetAnchorB()));
        assertTrue(Float.isFinite(point.GetSeparation()));
        assertTrue(Float.isFinite(point.GetBaseSeparation()));
        assertTrue(Float.isFinite(point.GetNormalImpulse()));
        assertTrue(Float.isFinite(point.GetTotalNormalImpulse()));
        assertTrue(Float.isFinite(point.GetNormalVelocity()));
        assertTrue(point.GetFeatureId() >= 0);
        assertTrue(point.GetTriangleIndex() >= -1);
        point.GetPersisted();
    }

    private static void assertProfileIsFinite(B3Profile profile) {
        float[] values = {
                profile.GetStep(), profile.GetPairs(), profile.GetCollide(), profile.GetSolve(),
                profile.GetSolverSetup(), profile.GetConstraints(), profile.GetPrepareConstraints(),
                profile.GetIntegrateVelocities(), profile.GetWarmStart(), profile.GetSolveImpulses(),
                profile.GetIntegratePositions(), profile.GetRelaxImpulses(), profile.GetApplyRestitution(),
                profile.GetStoreImpulses(), profile.GetSplitIslands(), profile.GetTransforms(),
                profile.GetSensorHits(), profile.GetJointEvents(), profile.GetHitEvents(), profile.GetRefit(),
                profile.GetBullets(), profile.GetSleepIslands(), profile.GetSensors()
        };
        for(float value : values) {
            assertTrue(Float.isFinite(value));
            assertTrue(value >= 0.0f);
        }
    }

    private static void assertCountersAreReadable(B3Counters counters) {
        int[] values = {
                counters.GetBodyCount(), counters.GetShapeCount(), counters.GetContactCount(),
                counters.GetJointCount(), counters.GetIslandCount(), counters.GetStackUsed(),
                counters.GetArenaCapacity(), counters.GetStaticTreeHeight(), counters.GetTreeHeight(),
                counters.GetSatCallCount(), counters.GetSatCacheHitCount(), counters.GetByteCount(),
                counters.GetTaskCount(), counters.GetAwakeContactCount(), counters.GetRecycledContactCount(),
                counters.GetDistanceIterations(), counters.GetPushBackIterations(), counters.GetRootIterations()
        };
        for(int value : values) {
            assertTrue(value >= 0);
        }
        for(int i = 0; i < 24; i++) {
            assertTrue(counters.GetColorCount(i) >= 0);
            assertTrue(counters.GetManifoldCount(i) >= 0);
        }
    }

    @Test
    public void recordingAndEveryReplayPlayerBindingExecute() throws Exception {
        loadBox3D();

        Path recordingFile = Files.createTempFile("jbox3d-recording-", ".b3rec");
        B3Recording defaultRecording = new B3Recording();
        B3Recording recording = new B3Recording(1024);
        B3Recording loadedRecording = null;
        B3ByteArray recordingData = null;
        B3RecPlayer player = null;
        B3World replayWorldView = null;
        B3DebugDrawEm draw = new B3DebugDrawEm();
        B3WorldDef worldDef = new B3WorldDef();
        B3Vec3 gravity = new B3Vec3(0.0f, -10.0f, 0.0f);
        worldDef.SetGravity(gravity);
        B3World world = new B3World(worldDef);
        B3BodyDef groundDef = new B3BodyDef();
        B3Vec3 groundPosition = new B3Vec3(0.0f, -1.0f, 0.0f);
        groundDef.SetPosition(groundPosition);
        B3Body ground = null;
        B3Hull groundHull = B3Hull.CreateBox(10.0f, 1.0f, 10.0f);
        B3ShapeDef groundShapeDef = new B3ShapeDef();
        B3Shape groundShape = null;
        B3BodyDef dynamicDef = new B3BodyDef();
        B3Vec3 dynamicPosition = new B3Vec3(0.0f, 3.0f, 0.0f);
        dynamicDef.SetType(B3.DynamicBody());
        dynamicDef.SetPosition(dynamicPosition);
        B3Body dynamicBody = null;
        B3ShapeDef dynamicShapeDef = new B3ShapeDef();
        dynamicShapeDef.SetDensity(1.0f);
        B3Vec3 sphereCenter = new B3Vec3(0.0f, 0.0f, 0.0f);
        B3Sphere sphere = new B3Sphere(sphereCenter, 0.5f);
        B3Shape dynamicShape = null;
        B3QueryFilter filter = new B3QueryFilter();
        B3Vec3 rayOrigin = new B3Vec3(0.0f, 6.0f, 0.0f);
        B3Vec3 rayTranslation = new B3Vec3(0.0f, -8.0f, 0.0f);
        B3CastResultCallbackEm recordingCast = new B3CastResultCallbackEm() {
            @Override
            protected float ReportHit(B3CastResult result) {
                return result.GetFraction();
            }
        };

        try {
            assertTrue(defaultRecording.IsValid());
            defaultRecording.Destroy();
            assertFalse(defaultRecording.IsValid());
            assertTrue(recording.IsValid());

            ground = world.CreateBody(groundDef);
            groundShape = ground.CreateHullShape(groundShapeDef, groundHull);
            dynamicBody = world.CreateBody(dynamicDef);
            dynamicShape = dynamicBody.CreateSphereShape(dynamicShapeDef, sphere);

            world.StartRecording(recording);
            for(int frame = 0; frame < 5; frame++) {
                B3TreeStats stats = world.CastRay(rayOrigin, rayTranslation, filter, recordingCast);
                assertTrue(stats.GetLeafVisits() > 0);
                dispose(stats);
                world.Step(1.0f / 60.0f, 4);
            }
            world.StopRecording();
            world.Destroy();

            assertTrue(recording.GetSize() > 0);
            recordingData = recording.GetData();
            assertEquals(recording.GetSize(), recordingData.GetSize());
            assertTrue(recording.ValidateReplay(1));
            assertTrue(recording.Save(recordingFile.toString()));
            assertTrue(Files.size(recordingFile) > 0L);

            loadedRecording = B3Recording.Load(recordingFile.toString());
            assertTrue(loadedRecording.IsValid());
            assertEquals(recording.GetSize(), loadedRecording.GetSize());
            assertTrue(loadedRecording.ValidateReplay(1));

            player = new B3RecPlayer(recordingData, 1);
            assertTrue(player.IsValid());
            player.SetDebugShapeCallbacks();
            assertEquals(0, player.GetFrame());
            assertEquals(5, player.GetFrameCount());
            assertFalse(player.IsAtEnd());
            assertFalse(player.HasDiverged());
            assertEquals(-1, player.GetDivergeFrame());

            B3RecPlayerInfo info = player.GetInfo();
            assertEquals(5, info.GetFrameCount());
            assertEquals(1, info.GetWorkerCount());
            assertEquals(1.0f / 60.0f, info.GetTimeStep(), EPSILON);
            assertEquals(4, info.GetSubStepCount());
            assertTrue(info.GetLengthScale() > 0.0f);
            assertTrue(B3.IsValidAABB(info.GetBounds()));

            player.SetWorkerCount(1);
            assertEquals(1, player.GetInfo().GetWorkerCount());
            player.SetKeyframePolicy(8L * 1024L * 1024L, 2);
            assertEquals(8L * 1024L * 1024L, player.GetKeyframeBudget());
            assertEquals(2, player.GetKeyframeMinInterval());
            assertTrue(player.GetKeyframeInterval() >= 2);
            assertTrue(player.GetKeyframeBytes() >= 0L);

            assertEquals(2, player.GetBodyCount());
            assertNotEquals(0L, player.GetBodyId(0));
            assertNotEquals(0L, player.GetBodyId(1));
            assertEquals(0L, player.GetBodyId(2));
            assertNotEquals(0L, player.GetWorldId());
            replayWorldView = player.GetWorld();
            assertTrue(replayWorldView.IsValid());
            dispose(replayWorldView);
            replayWorldView = player.GetWorld();
            assertTrue("disposing a non-owning replay view must not destroy the player world",
                    replayWorldView.IsValid());

            player.SubStepFrame();
            player.IsAtPreStep();
            player.SubStepFrame();
            assertTrue(player.GetFrame() >= 0);
            player.Restart();
            assertEquals(0, player.GetFrame());
            assertTrue(player.StepFrame());
            assertEquals(1, player.GetFrame());
            player.SeekFrame(3);
            assertEquals(3, player.GetFrame());
            assertFalse(player.HasDiverged());

            assertEquals(1, player.GetFrameQueryCount());
            B3RecQueryInfo query = player.GetFrameQuery(0);
            query.GetType();
            assertEquals(B3.DefaultMaskBits(), query.GetFilter().GetMaskBits());
            query.GetAABB();
            assertEquals(rayOrigin.GetY(), query.GetOrigin().GetY(), EPSILON);
            assertEquals(rayTranslation.GetY(), query.GetTranslation().GetY(), EPSILON);
            assertTrue(query.GetHitCount() > 0);
            assertEquals(0L, query.GetKey());
            assertEquals(0L, query.GetId());
            NativeString queryName = new NativeString();
            query.GetName(queryName);
            assertEquals("", queryName.c_str());
            B3RecQueryHit hit = player.GetFrameQueryHit(0, 0);
            assertNotEquals(0L, hit.GetShapeId());
            assertTrue(B3.IsValidVec3(hit.GetPoint()));
            assertTrue(B3.IsValidVec3(hit.GetNormal()));
            assertTrue(hit.GetFraction() >= 0.0f && hit.GetFraction() <= 1.0f);
            dispose(queryName);

            draw.SetDrawShapes(true);
            player.DrawFrameQueries(draw, -1, -1);
            while(player.StepFrame()) {
                // Drive the rest of the recording through the incremental API.
            }
            assertEquals(player.GetFrameCount(), player.GetFrame());
            assertTrue(player.IsAtEnd());
            assertFalse(player.HasDiverged());
            assertEquals(-1, player.GetDivergeFrame());

            player.Restart();
            assertEquals(0, player.GetFrame());
            assertFalse(player.IsAtEnd());
        }
        finally {
            if(world.IsValid()) {
                world.StopRecording();
                world.Destroy();
            }
            if(player != null && player.IsValid()) {
                player.Destroy();
            }
            if(loadedRecording != null && loadedRecording.IsValid()) {
                loadedRecording.Destroy();
            }
            if(recording.IsValid()) {
                recording.Destroy();
            }
            Files.deleteIfExists(recordingFile);
            dispose(recordingCast, rayTranslation, rayOrigin, filter, dynamicShape, sphere, sphereCenter,
                    dynamicShapeDef, dynamicBody, dynamicPosition, dynamicDef, groundShape, groundShapeDef,
                    groundHull, ground, groundPosition, groundDef, world, gravity, worldDef, draw,
                    replayWorldView, player, recordingData, loadedRecording, recording, defaultRecording);
        }
    }

    private static void loadBox3D() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        AtomicReference<Throwable> error = new AtomicReference<>();
        JBox3DLoader.init(new JParserLibraryLoaderListener() {
            @Override
            public void onLoad(boolean isSuccess, Throwable t) {
                if(!isSuccess) {
                    error.set(t != null ? t : new RuntimeException("Box3D JNI loader returned false"));
                }
                latch.countDown();
            }
        });
        assertTrue("Box3D JNI loader did not finish", latch.await(10, TimeUnit.SECONDS));
        if(error.get() != null) {
            throw new AssertionError("Box3D JNI loader failed", error.get());
        }
    }

    private static void dispose(NativeObject... objects) {
        for(NativeObject object : objects) {
            if(object != null && object.native_hasOwnership() && !object.isDisposed()) {
                object.dispose();
            }
        }
    }
}
