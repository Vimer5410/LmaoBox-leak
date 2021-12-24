package lmaobox.nomodulesfolder.events;

import meteordevelopment.meteorclient.events.Cancellable;
import net.minecraft.entity.Entity;

public class EventEntityRender {
    private static final EventEntityRender INSTANCE = new EventEntityRender();

    protected Entity entity;

    public Entity getEntity() {
        return entity;
    }

    public static class Render extends EventEntityRender {
        public Render(Entity entity) {
            this.entity = entity;
        }
    }

    public static class Label extends EventEntityRender {
        public Label(Entity entity) {
            this.entity = entity;
        }
    }
}
