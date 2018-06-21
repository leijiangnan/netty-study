package com.gzdx.synchronizedblock;

public class DrawTest {
    public static void main(String[] args){
        Account acct=new Account("1234567",1000);
        new DrawThread("甲",acct,800).start();
        new DrawThread("乙",acct,800).start();
    }
}
