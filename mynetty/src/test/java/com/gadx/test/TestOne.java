package com.gadx.test;

import com.gzdx.domain.User;
import org.junit.Test;

public class TestOne {

    private static final String BASE = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    public static String toBase62(long num) {
        StringBuilder sb = new StringBuilder();
        int targetBase = BASE.length();
        do {
            int i = (int) (num % targetBase);
            sb.append(BASE.charAt(i));
            num /= targetBase;
        } while (num > 0);

        return sb.reverse().toString();
    }

    public static long toBase10(String input) {
        int srcBase = BASE.length();
        long id = 0;
        String r = new StringBuilder(input).reverse().toString();

        for (int i = 0; i < r.length(); i++) {
            int charIndex = BASE.indexOf(r.charAt(i));
            id += charIndex * (long) Math.pow(srcBase, i);
        }

        return id;
    }

//    private static long insertRandomBitPer5Bits(long val) {
//        long result = val;
//        long high = val;
//        for (int i = 0; i < 10; i++) {
//            if (high == 0) {
//                break;
//            }
//            int pos = 5 + 5 * i + i;
//            high = result >> pos;
//            result = ((high << 1 | RandomUtils.nextInt(0, 2)) << pos)
//                    | (result & (-1L >>> (64 - pos)));
//        }
//        return result;
//    }

   @Test
    public void test1(){
       int a = 2 << 29 -1;
       System.out.println(Integer.toBinaryString(a));
       System.out.println(a);
       System.out.println(~a);
   }

   @Test
    public void test2(){
       System.out.println("aaaa".getBytes().length);
   }

   @Test
    public void test3(){
        int a = 10;
        int b = 3;
       System.out.println(a % b);
   }

   @Test
    public void test4(){
       System.out.println("ｖｖｖｖｖｖｖｖｖｖｙ*ｘｗｗｗｗｗｗｗｗｗｗｗｗｗｗｗｗｗｗｗｗｗｗｗｗｗｗｗｗｗｗｗｗｗｗｗｗｗｗｗｗｗｗｗｗｗｗｗｗｗｗｗｗｗｗｗｗｗｗｗｗｗｗｗｗｗｗｗｗｗＭＺ０３０２０Ｆ０５５ＭＺ０３０２０Ｆ０５３７００１０５".getBytes().length);
   }

   @Test
    public void test5(){
       String s = toBase62(100);
       long l = toBase10(s);
       System.out.println(s);
       System.out.println(l);
   }

   @Test
    public void test6(){
       User user = new User();
       user.setUserName("zhangsan");
       user.setPassWord("111");
       System.out.println(user);
   }
}
