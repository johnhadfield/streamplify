/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.beryx.streamplify;

import java.math.BigInteger;
import java.util.Arrays;

public class LongCartesianProduct extends LongIndexedSpliterator<int[], LongCartesianProduct> {
    private final int[] dimensions;

    public LongCartesianProduct(int... dimensions) {
        super(0, count(dimensions));
        if(Arrays.stream(dimensions).anyMatch(dim -> dim < 0)) throw new IllegalArgumentException("Invalid dimensions: " + Arrays.toString(dimensions));
        this.dimensions = dimensions;
        setValueSupplier(this::getAt);
        this.withAdditionalCharacteristics(DISTINCT);
    }

    private int[] getAt(long index) {
        int[] val = new int[dimensions.length];
        long dividend = index;
        for(int k = dimensions.length - 1; k >= 0; k--) {
            val[k] = (int)(dividend % dimensions[k]);
            dividend /= dimensions[k];
        }
        return val;
    }

    protected static long count(int[] dimensions) {
        BigInteger bigCount = BigIntegerCartesianProduct.count(dimensions);
        if(bigCount.compareTo(BigInteger.valueOf(Long.MAX_VALUE)) >= 0) throw new IllegalArgumentException("Dimensions too big: " + Arrays.toString(dimensions));
        return bigCount.longValueExact();
    }
}