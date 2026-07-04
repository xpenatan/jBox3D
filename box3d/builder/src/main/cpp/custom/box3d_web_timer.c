// SPDX-FileCopyrightText: 2026 Xpe
// SPDX-License-Identifier: Apache-2.0

#include "core.h"

#include "box3d/base.h"

#include <emscripten/emscripten.h>
#include <stddef.h>
#include <string.h>

float remainderf( float x, float y )
{
    if( y == 0.0f )
    {
        return 0.0f;
    }

    float q = x / y;
    int n = (int)( q < 0.0f ? q - 0.5f : q + 0.5f );
    return x - (float)n * y;
}

static void b3SwapBytes( unsigned char* a, unsigned char* b, size_t size )
{
    while( size-- > 0 )
    {
        unsigned char temp = *a;
        *a = *b;
        *b = temp;
        a++;
        b++;
    }
}

void qsort( void* base, size_t nmemb, size_t size, int ( *compar )( const void*, const void* ) )
{
    if( base == NULL || compar == NULL || size == 0 || nmemb < 2 )
    {
        return;
    }

    unsigned char* bytes = (unsigned char*)base;
    for( size_t i = 1; i < nmemb; i++ )
    {
        size_t j = i;
        while( j > 0 )
        {
            unsigned char* current = bytes + j * size;
            unsigned char* previous = bytes + ( j - 1 ) * size;
            if( compar( current, previous ) >= 0 )
            {
                break;
            }
            b3SwapBytes( current, previous, size );
            j--;
        }
    }
}

uint64_t b3GetTicks( void )
{
    return (uint64_t)( emscripten_get_now() * 1000000.0 );
}

float b3GetMilliseconds( uint64_t ticks )
{
    uint64_t ticksNow = b3GetTicks();
    return (float)( ( ticksNow - ticks ) / 1000000.0 );
}

float b3GetMillisecondsAndReset( uint64_t* ticks )
{
    uint64_t ticksNow = b3GetTicks();
    float ms = (float)( ( ticksNow - *ticks ) / 1000000.0 );
    *ticks = ticksNow;
    return ms;
}

void b3Yield( void )
{
}

void b3Sleep( int milliseconds )
{
    (void)milliseconds;
}

typedef struct b3Mutex
{
    int dummy;
} b3Mutex;

b3Mutex* b3CreateMutex( void )
{
    b3Mutex* m = b3Alloc( sizeof( b3Mutex ) );
    m->dummy = 42;
    return m;
}

void b3DestroyMutex( b3Mutex* m )
{
    *m = (b3Mutex){ 0 };
    b3Free( m, sizeof( b3Mutex ) );
}

void b3LockMutex( b3Mutex* m )
{
    (void)m;
}

void b3UnlockMutex( b3Mutex* m )
{
    (void)m;
}

typedef struct b3Semaphore
{
    int dummy;
} b3Semaphore;

b3Semaphore* b3CreateSemaphore( int initCount )
{
    b3Semaphore* s = b3Alloc( sizeof( b3Semaphore ) );
    (void)initCount;
    s->dummy = 42;
    return s;
}

void b3DestroySemaphore( b3Semaphore* s )
{
    *s = (b3Semaphore){ 0 };
    b3Free( s, sizeof( b3Semaphore ) );
}

void b3WaitSemaphore( b3Semaphore* s )
{
    (void)s;
}

void b3SignalSemaphore( b3Semaphore* s )
{
    (void)s;
}

typedef struct b3Thread
{
    int dummy;
} b3Thread;

b3Thread* b3CreateThread( b3ThreadFunction* function, void* context, const char* name )
{
    (void)function;
    (void)context;
    (void)name;
    b3Thread* t = b3Alloc( sizeof( b3Thread ) );
    t->dummy = 42;
    return t;
}

void b3JoinThread( b3Thread* t )
{
    *t = (b3Thread){ 0 };
    b3Free( t, sizeof( b3Thread ) );
}

uint32_t b3Hash( uint32_t hash, const uint8_t* data, int count )
{
    uint32_t result = hash;
    int i = 0;

    while( i + 8 <= count )
    {
        uint64_t word;
        memcpy( &word, data + i, sizeof( word ) );
#if defined( __BYTE_ORDER__ ) && __BYTE_ORDER__ == __ORDER_BIG_ENDIAN__
        word = ( ( word & 0x00000000000000FFULL ) << 56 ) | ( ( word & 0x000000000000FF00ULL ) << 40 ) |
               ( ( word & 0x0000000000FF0000ULL ) << 24 ) | ( ( word & 0x00000000FF000000ULL ) << 8 ) |
               ( ( word & 0x000000FF00000000ULL ) >> 8 ) | ( ( word & 0x0000FF0000000000ULL ) >> 24 ) |
               ( ( word & 0x00FF000000000000ULL ) >> 40 ) | ( ( word & 0xFF00000000000000ULL ) >> 56 );
#endif
        result = ( result << 5 ) + result + (uint32_t)word;
        result = ( result << 5 ) + result + (uint32_t)( word >> 32 );
        i += 8;
    }

    while( i < count )
    {
        result = ( result << 5 ) + result + data[i];
        i++;
    }

    return result;
}
