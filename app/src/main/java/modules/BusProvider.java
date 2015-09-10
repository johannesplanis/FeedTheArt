package modules;

import com.squareup.otto.Bus;



//Alternative to dependency injection, not used
public final class BusProvider {
    private static final Bus BUS = new Bus();

    public static Bus getInstance(){
        return BUS;
    }
    private BusProvider(){

    }
}
