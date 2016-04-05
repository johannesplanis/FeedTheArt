package events;

import catactivity.ArtObject;

/**
 * Created by JOHANNES on 4/5/2016.
 */
public class ArtUpdatedEvent {

    private ArtObject art;

    public ArtUpdatedEvent(ArtObject art) {
        this.art = art;
    }

    public ArtObject getArt() {

        return art;
    }
}
