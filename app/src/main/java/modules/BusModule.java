package modules;

import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

import javax.inject.Singleton;

import dagger.Module;
import dagger.ObjectGraph;
import dagger.Provides;
import activities.MenuActivity;
import fragments.NewcatChooseFragment;
import fragments.NewcatNameFragment;
import fragments.TutorialFragment;

/**
 * Created by JOHANNES on 8/19/2015.
 */
//creating singleton Otto event bus
//https://github.com/johanpoirier/squarelibs-android-demo/blob/master/src/main/java/org/pullrequest/squarelibs/demo/DaggerModule.java
@Module(
        injects = {
                MenuActivity.class,
                TutorialFragment.class,
                NewcatChooseFragment.class,
                NewcatNameFragment.class
        }
)
public class BusModule{

    private static ObjectGraph graph;
    @Provides
    @Singleton
    public Bus provideBus(){
        return new Bus(ThreadEnforcer.ANY);
    }


    //resolved issues with incorrect injection
    public static ObjectGraph getObjectGraph(){
        if (graph == null) {
            graph = ObjectGraph.create(new BusModule());
        }
        return graph;
    }
}
