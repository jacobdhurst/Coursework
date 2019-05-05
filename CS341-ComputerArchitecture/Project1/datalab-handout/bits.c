/* 
 * CS:APP Data Lab 
 * 
 * Jacob Hurst - jhurst - 101636492.
 * 
 * bits.c - Source file with your solutions to the Lab.
 *          This is the file you will hand in to your instructor.
 *
 * Restraints:
 * No loops or conditionals and limited arithmetic/logical operators
 * Only use ! ~ & ^ | + << >>
 * No constants longer than 8 bits.
 * 
 * WARNING: Do not include the <stdio.h> header; it confuses the dlc
 * compiler. You can still use printf for debugging without including
 * <stdio.h>, although you might get a compiler warning. In general,
 * it's not good practice to ignore compiler warnings, but in this
 * case it's OK.  
 */

#if 0
/*
 * Instructions to Students:
 *
 * STEP 1: Read the following instructions carefully.
 */

You will provide your solution to the Data Lab by
editing the collection of functions in this source file.

INTEGER CODING RULES:
 
  Replace the "return" statement in each function with one
  or more lines of C code that implements the function. Your code 
  must conform to the following style:
 
  int Funct(arg1, arg2, ...) {
      /* brief description of how your implementation works */
      int var1 = Expr1;
      ...
      int varM = ExprM;

      varJ = ExprJ;
      ...
      varN = ExprN;
      return ExprR;
  }

  Each "Expr" is an expression using ONLY the following:
  1. Integer constants 0 through 255 (0xFF), inclusive. You are
      not allowed to use big constants such as 0xffffffff.
  2. Function arguments and local variables (no global variables).
  3. Unary integer operations ! ~
  4. Binary integer operations & ^ | + << >>
    
  Some of the problems restrict the set of allowed operators even further.
  Each "Expr" may consist of multiple operators. You are not restricted to
  one operator per line.

  You are expressly forbidden to:
  1. Use any control constructs such as if, do, while, for, switch, etc.
  2. Define or use any macros.
  3. Define any additional functions in this file.
  4. Call any functions.
  5. Use any other operations, such as &&, ||, -, or ?:
  6. Use any form of casting.
  7. Use any data type other than int.  This implies that you
     cannot use arrays, structs, or unions.

 
  You may assume that your machine:
  1. Uses 2s complement, 32-bit representations of integers.
  2. Performs right shifts arithmetically.
  3. Has unpredictable behavior when shifting an integer by more
     than the word size.

EXAMPLES OF ACCEPTABLE CODING STYLE:
  /*
   * pow2plus1 - returns 2^x + 1, where 0 <= x <= 31
   */
  int pow2plus1(int x) {
     /* exploit ability of shifts to compute powers of 2 */
     return (1 << x) + 1;
  }

  /*
   * pow2plus4 - returns 2^x + 4, where 0 <= x <= 31
   */
  int pow2plus4(int x) {
     /* exploit ability of shifts to compute powers of 2 */
     int result = (1 << x);
     result += 4;
     return result;
  }

FLOATING POINT CODING RULES

For the problems that require you to implent floating-point operations,
the coding rules are less strict.  You are allowed to use looping and
conditional control.  You are allowed to use both ints and unsigneds.
You can use arbitrary integer and unsigned constants.

You are expressly forbidden to:
  1. Define or use any macros.
  2. Define any additional functions in this file.
  3. Call any functions.
  4. Use any form of casting.
  5. Use any data type other than int or unsigned.  This means that you
     cannot use arrays, structs, or unions.
  6. Use any floating point data types, operations, or constants.


NOTES:
  1. Use the dlc (data lab checker) compiler (described in the handout) to 
     check the legality of your solutions.
  2. Each function has a maximum number of operators (! ~ & ^ | + << >>)
     that you are allowed to use for your implementation of the function. 
     The max operator count is checked by dlc. Note that '=' is not 
     counted; you may use as many of these as you want without penalty.
  3. Use the btest test harness to check your functions for correctness.
  4. Use the BDD checker to formally verify your functions
  5. The maximum number of ops for each function is given in the
     header comment for each function. If there are any inconsistencies 
     between the maximum ops in the writeup and in this file, consider
     this file the authoritative source.

/*
 * STEP 2: Modify the following functions according the coding rules.
 * 
 *   IMPORTANT. TO AVOID GRADING SURPRISES:
 *   1. Use the dlc compiler to check that your solutions conform
 *      to the coding rules.
 *   2. Use the BDD checker to formally verify that your solutions produce 
 *      the correct answers.
 */


#endif
/* Copyright (C) 1991-2016 Free Software Foundation, Inc.
   This file is part of the GNU C Library.

   The GNU C Library is free software; you can redistribute it and/or
   modify it under the terms of the GNU Lesser General Public
   License as published by the Free Software Foundation; either
   version 2.1 of the License, or (at your option) any later version.

   The GNU C Library is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
   Lesser General Public License for more details.

   You should have received a copy of the GNU Lesser General Public
   License along with the GNU C Library; if not, see
   <http://www.gnu.org/licenses/>.  */
/* This header is separate from features.h so that the compiler can
   include it implicitly at the start of every compilation.  It must
   not itself include <features.h> or any other header that includes
   <features.h> because the implicit include comes before any feature
   test macros that may be defined in a source file before it first
   explicitly includes a system header.  GCC knows the name of this
   header in order to preinclude it.  */
/* glibc's intent is to support the IEC 559 math functionality, real
   and complex.  If the GCC (4.9 and later) predefined macros
   specifying compiler intent are available, use them to determine
   whether the overall intent is to support these features; otherwise,
   presume an older compiler has intent to support these features and
   define these macros by default.  */
/* wchar_t uses Unicode 8.0.0.  Version 8.0 of the Unicode Standard is
   synchronized with ISO/IEC 10646:2014, plus Amendment 1 (published
   2015-05-15).  */
/* We do not support C11 <threads.h>.  */

/* 
 * bitAnd - x&y using only ~ and | 
 *   Example: bitAnd(6, 5) = 4
 *   Legal ops: ~ |
 *   Max ops: 8
 *   Rating: 1
 */
int bitAnd(int x, int y) {
  /** 
    Using DeMorgan's Law
    not (A and B) = not A or not B 
    => not (not (A and B) = not (not A or not B)
    => A and B = not (not A or not B) 
   **/
  return ~(~x | ~y);
}
/* 
 * getByte - Extract byte n from word x
 *   Bytes numbered from 0 (LSB) to 3 (MSB)
 *   Examples: getByte(0x12345678,1) = 0x56
 *   Legal ops: ! ~ & ^ | + << >>
 *   Max ops: 6
 *   Rating: 2
 */
int getByte(int x, int n) {
    /** 
     Our shift is (n << 3). If we shift x right by this amount, we get the selected byte moved
     to the least significant byte position. In order to filter the remaining bits not in our selected byte,
     we & the expression with 0xFF. This gives us byte n from word x.

     Example: getByte(0x80000000, 3)
     n << 3 = 0x18
     0x80000000 >> 0x18 (24) = 0xFFFFFF80
     0xFFFFFF80 & 0x000000FF = 0x80 
    **/
  return 0xFF & (x >> (n << 3));
}
/* 
 * isAsciiDigit - return 1 if 0x30 <= x <= 0x39 (ASCII codes for characters '0' to '9')
 *   Example: isAsciiDigit(0x35) = 1.
 *            isAsciiDigit(0x3a) = 0.
 *            isAsciiDigit(0x05) = 0.
 *   Legal ops: ! ~ & ^ | + << >>
 *   Max ops: 15
 *   Rating: 3
 * 
 *   0x30 = 0011 0000 = 48 if (x - 48) < 0 then x is not in lower bound
 *   0x2F = 0010 1111 = 47    
 *   0x39 = 0011 1001 = 57 if (57 - x) < 0 then x is not in upper bound
 *   0x3a = 0011 1010 = 58
 *   if either of the above are true then x is not in range => return 0
 */
int isAsciiDigit(int x) {
  /**
   0x30 = 48 in decimal, 0x39 = 57 in decimal.
   This is the same as checking if 48 <= x <= 57.
   In this question, we need to consider what it would mean for x to be outside
   of the lower or upper bounds. 
   If x < 0x30 then subtracting 0x30 from x should be negative.
   Similarly, if x > 57 then subtracting x from 57 should be negative.
   If either of these results are negative then x is not in the range.
   We first find x with respect to the bounds and then focus on sign bit by shifting >> 31.
   If the sign in either of the checks are negative then x is out of the range.

   Example: isAsciiDigit(0x35)
   lower = 0x35 + 0xFFFFFFD0 = 0x5
   upper = 0x39 + 0xFFFFFFCB = 0x4
   lSign = 0x0
   uSign = 0x0
   return !(0 | 0) = 1
   **/
  int lower = x + (~0x30 + 1);
  int upper = 0x39 + (~x + 1);
  int lSign = lower >> 31;
  int uSign = upper >> 31;

  return !(lSign | uSign);
}
/* 
 * logicalShift - shift x to the right by n, using a logical shift
 *   Can assume that 0 <= n <= 31
 *   Examples: logicalShift(0x87654321,4) = 0x08765432
 *   Legal ops: ! ~ & ^ | + << >>
 *   Max ops: 20
 *   Rating: 3 
 */
int logicalShift(int x, int n) {
  /*
  First we generate a mask by shifting 1 left from lsb position to msb position,
  shifting right n (copying 1s we would get from an arithmetic shift), and shifting
  left 1 to adjust. Our mask is what x may copy from msb position when shifted right n.

  Next we shift x right n performing a regular arithmetic shift, we & that value with the negation
  of our mask to keep all values from xs right shift excluding those possibly copied from the msb shift.

  Example: logicalShift(0x80000000, 4)
  mask: 0x1 << 31 = 0x80000000
        0x80000000 >> 4 = 0xF8000000
        0xF8000000 << 1 = 0xF0000000
  shift: 0x80000000 >> 4 = 0xF80000000
         0xF8000000 & (0x0FFFFFFF) = 0x08000000
  */
  int mask = ((1 << 31) >> n) << 1; 
  int shift = (x >> n) & ~mask;
  return shift;
}
/*
 * bitParity - returns 1 if x contains an odd number of 0's
 *   Examples: bitParity(5) = 0, bitParity(7) = 1
 *   Legal ops: ! ~ & ^ | + << >>
 *   Max ops: 20
 *   Rating: 4
 */
int bitParity(int x) {
  /**
   To obtain bit parity, we successively apply XOR operations on bytes of x. 
   First, we extract byte 0, 1, 2, 3. Our initial results from XORing pairs together is
   given by variables first & second (we now just have two bytes to consider). Each successive XOR halves our number of bits we need to XOR. 
   We XOR first and second together and their result is given by variable third (we now just have a byte to consider). We then split third into
   its nibbles and XOR that together in variable fourth. By the fourth successive XOR, we just have a single nibble to consider. We split it into
   its pairs of bits and XOR them together and store that in variable fifth. On this final iteration, we are just XORing the two bits in fifth and returning that result.
   This successive pattern reveals the bit parity at each level.

   The initial code has been left for purposes of explaining examples. 
   I shortened the answer to this problem by merging in the byte 0, 1, 2, 3 variables into first and second and simplifying.
   This simplification brought me from 22 operations to 19 operations

   Example(0x900000000)
   byte0 = 0x00 | => first = 0x00  |                 |    nibble0 = 0x9 |                 |   pair0 = 0x2 |                |   single0 = 0x1 |
   byte1 = 0x00 |                  | => third = 0x90 | =>               | => fourth = 0x9 | =>            | => fifth = 0x3 | =>              | result = 0x0
   byte2 = 0x00 | => second = 0x90 |                 |    nibble1 = 0x0 |                 |   pair1 = 0x1 |                |   single1 = 0x1 |
   byte3 = 0x90 |

   Example(0x900000001)
   byte0 = 0x01 | => first = 0x01  |                 |    nibble0 = 0x9 |                 |   pair0 = 0x0 |                |   single0 = 0x0 |
   byte1 = 0x00 |                  | => third = 0x91 | =>               | => fourth = 0x8 | =>            | => fifth = 0x2 | =>              | result = 0x1
   byte2 = 0x00 | => second = 0x90 |                 |    nibble1 = 0x1 |                 |   pair1 = 0x2 |                |   single1 = 0x1 |
   byte3 = 0x90 |
   **/ 
  
  //int byte0 = 0xFF & x;
  //int byte1 = 0xFF & (x >> 8);
  //int byte2 = 0xFF & (x >> 16);
  //int byte3 = 0xFF & (x >> 24);
  
  int first = 0xFF & (x ^ (x >> 8));           //byte0 ^ byte1;
  int second = 0xFF & ((x >> 16) ^ (x >> 24)); //byte2 ^ byte3;
  
  int third = first ^ second;
  
  int nibble0 = 0xF & third;
  int nibble1 = 0xF & (third >> 4);
  
  int fourth = nibble0 ^ nibble1;
  
  int pair0 = 0x3 & fourth;
  int pair1 = 0x3 & (fourth >> 2);
  
  int fifth = pair0 ^ pair1;
  
  int single0 = 0x1 & fifth;
  int single1 = 0x1 & (fifth >> 1);
  
  return single0 ^ single1;
}
/* 
 * fitsBits - return 1 if x can be represented as an 
 *  n-bit, two's complement integer.
 *   1 <= n <= 32
 *   Examples: fitsBits(5,3) = 0, fitsBits(-4,3) = 1
 *   Legal ops: ! ~ & ^ | + << >>
 *   Max ops: 15
 *   Rating: 2 
 */
int fitsBits(int x, int n) {
  /**
   Method is the same as method for fitsShort, except now instead of 16,
   we use an arbitrary n.

   To copy our integers leading bit, we shift left by 32-n (given by 32 + (~n + 1)),
   and then shift back to the original positioning. If the number fits in this range then
   the bit pattern should not have changed and xor will result in 0x0. We convert this result to 
   0 or 1 by the ! operator.

   Example: fitsBits(5, 3)
   n = 32 + (~3 + 1) = 32 - 3 = 29
   !(0x5 ^ (0x5 << 29) >> 29)
   !(0x5 ^ (0xA0000000) >> 29)
   !(0x5 ^ (0xFFFFFFFD))
   !(0xFFFFFFF8) = 0

   Example: fitsBits(-4, 3)
   n = 32 + (~3 + 1) = 32 - 3 = 29
   !(0xFFFFFFFC ^ (0xFFFFFFFC << 29) >> 29)
   !(0xFFFFFFFC ^ (0x80000000) >> 29)
   !(0xFFFFFFFC ^ (0xFFFFFFFC))
   !(0x0) = 1
   **/

  n = 32 + (~n + 1);
  return !(x^(x << n) >> n);
}
/* 
 * fitsShort - return 1 if x can be represented as a 
 *   16-bit, two's complement integer.
 *   Examples: fitsShort(33000) = 0, fitsShort(-32768) = 1
 *   Legal ops: ! ~ & ^ | + << >>
 *   Max ops: 8
 *   Rating: 1
 */
int fitsShort(int x) {
  /**
     First, we shift x left 16 and then right 16 to copy its 16th bit from bit 32 to 16.
     Next, we xor x with the result from the previous step. If x fits in the 16 bit range, this will be
     the same as x xor with itself which is 0, we convert this to 1 by not operator.
     If x does not fit 16 bits this will be the same as x xor with the first 16 bits of x set which will
     result in a nonzero value, we convert this to 0 by not operator.
     
     Example: fitShort(-32768) 0xffff8000
     step1 = -32768 << 16 = 0x80000000
     step2 = step1 >> 16 = 0xffff8000
     step3 = x ^ step2 = 0x0
     !step3 = 1
     **/
  return !(x^(x << 16) >> 16);
}
/*
 * isTmin - returns 1 if x is the minimum, two's complement number,
 *     and 0 otherwise 
 *   Legal ops: ! ~ & ^ | +
 *   Max ops: 10
 *   Rating: 1
 */
int isTmin(int x) {
  /**
   Minimum 2's C number is -2^(k-1) = 0x80000000
   First, we get the two's complement of x by (~x + 1).
   If the two's complement of x is the same as x => x is T min
   We can check this by !(x ^ tc) as the XOR of the same number is 0 and ! makes the result 1.
   Lastly, we make sure x is not 0 by adding & !!(x ^ 0x0) if x is 0 then this result will be 0.
   & on the two results gives us a final answer in the form of 1 or 0.

   Example: isTmin(0x80000000)
   tc = ~0x80000000 + 1 = 0x7FFFFFFF + 1 = 0x80000000
   result = !(0x80000000 ^ 0x80000000) & !!(0x80000000 ^ 0x0)
          = !(0x0) & !!(0x80000000)
          = 1 & 1 = 1
   **/
  int tc = ~x + 1;
  int result = !(x ^ tc) & !!(x ^ 0x0);
  return result;
}
/* 
 * leastBitPos - return a mask that marks the position of the
 *               least significant 1 bit. If x == 0, return 0
 *   Example: leastBitPos(96) = 0x20
 *   Legal ops: ! ~ & ^ | + << >>
 *   Max ops: 6
 *   Rating: 2 
 */
int leastBitPos(int x) {
  /**
   Two's complement of a number x always has the least significant bit of x set to 1,
   and the preceding bits of x set to 0 with the other bits up to most significant 1 bit of x set to 0,
   this is followed by 1s until wordsize. Two's complement is calculated by ~x + 1.
   Example:
     0x2  = 0000 0010
     ~x+1 = 1111 1101 + 1 = 1111 1110
   If we & x with its two's complement, then we are sure to only extract its least significant 1 bit.
   
   Example: leastBitPos(96)
   x = 96     = 0x60 (0110 0000)
   ~x+1       = 0xA0 (1010 0000)
   x & (~x+1) = 0x20 (0010 0000) 
   **/
  return x & (~x + 1);
}
/* 
 * negate - return -x 
 *   Example: negate(1) = -1.
 *   Legal ops: ! ~ & ^ | + << >>
 *   Max ops: 5
 *   Rating: 2
 */
int negate(int x) {
  /*
  This is just two's complement.
  We negate each bit in x and then simply add 1.

  Example: negate(1)
  ~1 + 1 = 0xFFFFFFFE + 0x00000001
         = 0xFFFFFFFF = -1
  */
  return ~x + 1;
}
/* 
 * isPositive - return 1 if x > 0, return 0 otherwise 
 *   Example: isPositive(-1) = 0.
 *   Legal ops: ! ~ & ^ | + << >>
 *   Max ops: 8
 *   Rating: 3
 */
int isPositive(int x) {
  /**
   If our number is < 0, then performing a right shift of 31 will result in 0xFFFFFFFF always.
   We can get the complement of this (always 0x0 if < 0) and simply & our number with it to check if it is < 0.
   We use !! to covert result into 0 or 1 exclusively.

   Example: isPositive(0x800000000)
   ~(0x80000000 >> 31) = ~0xFFFFFFFF = 0x0
   0x0 & 0x80000000 = 0x0
   !!0x0 => 0
   **/
  x = ~(x >> 31) & x;
  return !!x;
}
/* 
 * subOK - Determine if can compute x-y without overflow
 *   Example: subOK(0x80000000,0x80000000) = 1,
 *            subOK(0x80000000,0x70000000) = 0, 
 *   Legal ops: ! ~ & ^ | + << >>
 *   Max ops: 20
 *   Rating: 3
 */
int subOK(int x, int y) {
  /**
   We should compare sign bits in x & x-y.
   We get x-y by adding two's complement of y to x.
   The initial sign can be found by considering the most significant bit of (x ^ y).
   The resulting sign from x-y can be found by considering the most significant bit of (result ^ x).
   If we & the two together we can determine if sign has changed => overflow.
   We shift this result over to avoid confusion and focus on the sign.
   Finally we ! this value to produce the final answer.
   
   Example: subOK(0x80000000, 0x70000000)
   result = 0x10000000
   initialSign = 0xF0000000
   resultSign = 0x90000000
   return !(0x00000001) = 0
  **/
  int result = x + (~y + 1);
  int initialSign = (x ^ y);
  int resultSign = (result ^ x);

  return !((initialSign & resultSign) >> 31);
}
/* 
 * float_abs - Return bit-level equivalent of absolute value of f for
 *   floating point argument f.
 *   Both the argument and result are passed as unsigned int's, but
 *   they are to be interpreted as the bit-level representations of
 *   single-precision floating point values.
 *   When argument is NaN, return argument..
 *   Legal ops: Any integer/unsigned operations incl. ||, &&. also if, while
 *   Max ops: 10
 *   Rating: 2
 */
unsigned float_abs(unsigned uf) {
  /**
   To convert to absolute value of floating point argument f, we just need to set the
   sign bit to 0. To do this, we make a mask 0x7FFFFFFF and '&' uf with it. Next we just need to check
   if the result is NaN, NaNs are represented with the exponential field filled with 1s and a non-zero number 
   in the significand field. This means we just need to check if our number is > 0x7F800000. If it is, we return the
   argument, if not then we'll return the absolute value representation.

   Example: float_abs(0x80C00000)
   mask = 0x7FFFFFFF
   abs = 0x00C00000
   nan = 0x7F800000
   abs > nan? False
   return abs
   **/
  unsigned mask = ~(1 << 31);
  unsigned abs  = uf & mask;
  unsigned nan  = 0x7F800000;

  if(abs > nan) return uf;
  
  return abs;
}
/*
 * ezThreeFourths - multiplies by 3/4 rounding toward 0,
 *   Should exactly duplicate effect of C expression (x*3/4),
 *   including overflow behavior.
 *   Examples: ezThreeFourths(11) = 8
 *             ezThreeFourths(-9) = -6
 *             ezThreeFourths(1073741824) = -268435456 (overflow)
 *   Legal ops: ! ~ & ^ | + << >>
 *   Max ops: 12
 *   Rating: 3
 */
int ezThreeFourths(int x) {
  /**
   //(x + x + x) >> 2; 
   //((x << 1) + x) >> 2; 
   We first compute three times x by shifting over once and adding another copy of x. 
   Left shifts of binary numbers are equivalent to multiplications by powers of two.
   For negative numbers, we need to account for the bias by adding it in before dividing by four.
   We use the mask in bias to avoid applying it to positive numbers.
   Finally we divide by four by shifting right two. 

   Example: ezThreeFourths(11)
   three = (11 << 1) + 1 = 11*2 + 11 = 33
   bias = 3 & (33 >> 31) = 0
   return (three + bias) >> 2 = 33 / 4 = 8 
   **/
  int three = (x << 1) + x;
  int bias = 3 & (three >> 31);
  return (three + bias) >> 2; 
}
/*
 * trueThreeFourths - multiplies by 3/4 rounding toward 0,
 *   avoiding errors due to overflow
 *   Examples: trueThreeFourths(11) = 8
 *             trueThreeFourths(-9) = -6
 *             trueThreeFourths(1073741824) = 805306368 (no overflow)
 *   Legal ops: ! ~ & ^ | + << >>
 *   Max ops: 20
 *   Rating: 4
 */
int trueThreeFourths(int x)
{
  /**
   Method is similar to exThreeFourths in that we are still dividing and multiplying in a similar fashion.
   However, there is a issue with right shifts of negative values being logical (they don't round to 0), this
   results in errors in a couple bits. To accomodate for this, we save the bits and sign in respective keep and sign variables.
   We then divide by 4 and multiply by three and add our adjustment to the result.
   If the initial sign was negative, the adjustment is (keep*4)/4, if the initial sign was positive, the adjustment is (keep*3)/4.

   Example: trueThreeFourths(11)
   sign = 0
   keep = 11 & 3 = 3
   fourth = 11 >> 2 = 2
   three = 2 + (2 << 1) = 2 + 4 = 6
   adjustment = (3 + (3 << 1) + (0 & 3)) >> 2 = (3 + (6) + (0)) >> 2 = 9 / 4 = 2
   return 6 + 2 = 8
   
   Example: trueThreeFourths(-9)
   sign = 0xFFFFFFFF
   keep = -9 & 3 = 3
   fourth = -9 >> 2 = -3
   three = -3 + (-3 << 1) = -3 + (-6) = -9
   adjustment = (3 + (3 << 1) + (0xFFFFFFFF & 3)) >> 2 = (3 + (6) + (3)) >> 2 = 12 / 4 = 3
   return -9 + 3 = -6
   **/

  int sign = x >> 31;
  int keep = x & 3;

  int fourth = x >> 2;
  int three = fourth + (fourth << 1);

  int adjustment = (keep + (keep << 1) + (sign & 3)) >> 2;
  return three + adjustment;
}
