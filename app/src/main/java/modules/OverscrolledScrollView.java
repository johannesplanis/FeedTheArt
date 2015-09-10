package modules;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.EdgeEffect;
import android.widget.ScrollView;

import java.lang.reflect.Field;

/**
 * Created by JOHANNES on 9/10/2015.
 * created to enable change of the scrolling properties - fade color
 * so far - not really working - disabled
 *
 */
public class OverscrolledScrollView extends ScrollView {

    private Context inContext;

    public OverscrolledScrollView(Context context){
        super(context);
        inContext = context;
        removeEdges(context);
    }

    public OverscrolledScrollView(Context context,AttributeSet attr){
        super(context,attr);
        inContext = context;
        removeEdges(context);
    }

    public OverscrolledScrollView(Context context, AttributeSet attr, int defStyle){
        super(context,attr,defStyle);
        inContext = context;

        removeEdges(context);
    }
    @Override
    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
        //onOverScrolled method must be overrided, or we will see the background of the listview when overscroll fast.
        super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);
    }

    @Override
    public void setOverScrollMode(int mode) {
        super.setOverScrollMode(OVER_SCROLL_NEVER);
    }

    @Override
    public int getSolidColor(){
        return Color.parseColor("#FFEDEDDA");
    }


    public void removeEdges(Context context) {
        try {
            Class<?> superClass = getClass().getSuperclass().getSuperclass();

            Field field = superClass.getDeclaredField("mEdgeGlowTop");
            field.setAccessible(true);
            field.set(this, new NoEdgeEffect(context));

            Field fieldBottom = superClass.getDeclaredField("mEdgeGlowBottom");
            fieldBottom.setAccessible(true);
            fieldBottom.set(this, new NoEdgeEffect(context));
        } catch (Exception e) {
        }


    }
    class NoEdgeEffect extends EdgeEffect {
        public NoEdgeEffect(Context context) {
            super(context);
        }

        public boolean draw(Canvas canvas) {
            // Do nothing
            return false;
        }
    }
}
