package com.yaobing.framemvpproject.app.proxyDemo;

import android.util.Log;

public class Humen implements LifeBehaviorInterface {

    Ape mApe;//代理类Human中的原始类Ape

    public Humen(Ape ape) {
        mApe = ape;
    }

    public static void main(String[] args) {
        Ape ape = new Ape();
        Humen humen = new Humen(ape);
        humen.eat("肉");
    }

    @Override
    public void eat(String food) {
        mApe.eat("做熟了的" + food);
    }
}
