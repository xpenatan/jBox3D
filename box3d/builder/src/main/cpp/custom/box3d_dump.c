// Box3D 0.1.0 declares b3World_DumpShapeBounds but omits its implementation.
// Keep this bridge in C because Box3D's private implementation headers are C-only.
#include "body.h"
#include "physics_world.h"
#include "shape.h"

#include <stdio.h>

void b3World_DumpShapeBounds(b3WorldId worldId, b3BodyType type)
{
    b3World* world = b3GetUnlockedWorldFromId(worldId);
    if (world == NULL)
    {
        return;
    }

    FILE* file = fopen("box3d_bounds.txt", "w");
    if (file == NULL)
    {
        return;
    }

    fprintf(file, "shapeId,bodyType,lowerX,lowerY,lowerZ,upperX,upperY,upperZ\n");
    for (int i = 0; i < world->shapes.count; ++i)
    {
        const b3Shape* shape = world->shapes.data + i;
        if (shape->id == B3_NULL_INDEX || shape->bodyId < 0 || shape->bodyId >= world->bodies.count)
        {
            continue;
        }

        const b3Body* body = world->bodies.data + shape->bodyId;
        if (body->id == B3_NULL_INDEX || body->type != type)
        {
            continue;
        }

        fprintf(file, "%d,%d,%.9g,%.9g,%.9g,%.9g,%.9g,%.9g\n", shape->id, (int)body->type,
                shape->aabb.lowerBound.x, shape->aabb.lowerBound.y, shape->aabb.lowerBound.z,
                shape->aabb.upperBound.x, shape->aabb.upperBound.y, shape->aabb.upperBound.z);
    }

    fclose(file);
}
