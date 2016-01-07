package org.mapdb

import org.junit.Test
import org.junit.Assert.*
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import java.io.IOException
import java.util.concurrent.ConcurrentMap

/**
 * Concurrent tests for HTreeMap
 */
@RunWith(Parameterized::class)
class HTreeMapConcTest(val mapMaker:(generic:Boolean)-> ConcurrentMap<Any?, Any?>) {

    companion object {
        @Parameterized.Parameters
        @JvmStatic
        fun params(): Iterable<Any> {
            return HTreeMapTest.Guava.params()
        }
    }
    @Test fun basicTest(){
        val map = mapMaker(false);
        val max = 10000+1e6.toInt()*TT.testScale()
        val threadCount = 16

        TT.fork(threadCount){i->
            for(key in i until max step threadCount){
                map.put(key, "aa"+key)
            }
        }
        if(map is HTreeMap)
            map.stores.toSet().forEach{it.verify()}

        assertEquals(max, map.size)
        for(key in 0 until max){
            assertEquals("aa"+key, map[key])
        }
    }
}