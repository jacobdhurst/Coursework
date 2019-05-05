//Class for quick tests of thoughts.

#include <stdio.h>

int main()
{
    //isTmin
    int x = 0x80000000;
    int maska = ~0;
    //printf("0x%.8x\n", mask);
    int result = ~(x ^ maska);
    //printf("%d\n", x);

    //isPositive
    x = -1;
    x = x >> 31;
    x = x & 0x1;

    //fitsShort (16 bits)
    x = -32769; 
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
    x = 131072;
    printf("AAAAAAAAAAAA 0x%.8x\n", x);
    printf("0x%.8x\n", x << 16);
    printf("0x%.8x\n", (x << 16) >> 16 );
    printf("0x%.8x\n", x ^ ( x << 16 ) >> 16);
    printf("%d\n", ! ( x ^ ( x << 16 ) >> 16 ) );
    x = !(x^(x << 16) >> 16);

    //fitBits
    printf("%d\n", 32 + (~3 + 1) );
    printf("0x%.8x\n", ( -4 << 29 ) >> 29);

    //tc
    printf("0x%.8x\n", ((0x80000000+(~0x70000000+1)) & ((~0x80000000+1)+0x70000000)));
    
    //subok
    printf("0x%.8x\n", ((0x80000000+(~0x70000000+1))^0x80000000)>>31);

    //isAscii
    printf("0x%.8x\n", 0x39 + (~0x35+1));

    //3/4
    printf("%d\n", ((-591995377 << 1) + (-591995377)) >> 2 ); //-443996532
    
    //uf
    unsigned uf = 0x80C00000;
    unsigned mask = ~(1 << 31);
    unsigned abs  = uf & mask;
    unsigned nan  = 0x7F800000;

    printf("0x%.8x\n", (1 << 31) >> 15); //0xFFFF0000
    x = 0xFFEEDDCC;


  int byte0 = 0xFF & x;
  int byte1 = 0xFF & (x >> 8);
  int byte2 = 0xFF & (x >> 16);
  int byte3 = 0xFF & (x >> 24);

  int first = byte0 ^ byte1;
  int second = byte2 ^ byte3;
  
  int third = first ^ second;
  
  int nibble0 = 0xF & third;
  int nibble1 = 0xF & (third >> 4);
  
  //int fourth = nibble0 ^ nibble1;
  
  //int pair0 = 0x3 & fourth;
  //int pair1 = 0x3 & (fourth >> 2);
  
  //int fifth = pair0 ^ pair1;
  
  //int single0 = 0x1 & fifth;
  //int single1 = 0x1 & (fifth >> 1);

//   return single0 ^ single1;
  
    printf("0x%.8x\n", byte0); //
    printf("0x%.8x\n", byte1); //
    printf("0x%.8x\n", byte2); //
    printf("0x%.8x\n", byte3); //

    x = -9;
    int sign = x >> 31;
    int keep = x & 3;

    int fourth = x >> 2;
    int three = fourth + (fourth << 1);
    int adjustment = (keep + (keep << 1) + (sign & 3)) >> 2;
    printf("keep %d\n", keep); //
    printf("4th  %d\n", fourth); //
    printf("3    %d\n", three); //
    printf("adj  %d\n", adjustment); //
    printf("res  %d\n", three + adjustment);

    printf("MIDTERM");
    int x1 = -3;
    int xshift = x1 - (x1 >> 2);
    int xmul = x1*(-3);

    printf("\n\n x - (x >> 2) = %d\n x*(-3) = %d\n", xshift, xmul);

    return 0;
}