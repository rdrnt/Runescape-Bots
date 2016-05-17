/**
 * Created by rdrnt on 2016-05-15.
 */
import com.epicbot.api.ActiveScript;
import com.epicbot.api.GameType;
import com.epicbot.api.Manifest;
import com.epicbot.api.os.methods.Walking;
import com.epicbot.api.os.methods.interactive.NPCs;
import com.epicbot.api.os.methods.interactive.Players;
import com.epicbot.api.os.methods.widget.Camera;
import com.epicbot.api.os.wrappers.Tile;
import com.epicbot.api.util.Filter;
import com.epicbot.api.os.methods.tab.inventory.Inventory;
import com.epicbot.api.os.wrappers.node.Item;
import java.lang.*;
import java.util.concurrent.TimeUnit;

@Manifest(
        author = "rdurant",
        game = GameType.OldSchool,
        name = "Barbarian Fishing"
)


public class BarbarianFisher extends ActiveScript {

    @Override
    public boolean onStart() {
        run();
        return true;
    }

    @Override
    public void onStop()
    {
        System.out.println("Stopping");
    }

    public boolean isRunning(boolean running){
        return running;
    }

    public void run() {
        isRunning(true);
        Camera.setAngle(340);
        Camera.setPitch(98);
        System.out.print("Running...");
        if (Inventory.isFull()) {
            System.out.print("Full inv...");
            dropFish();
        }
        else {
            System.out.print("Fishing...");
            //Hill tiles
            Tile hill1 = new Tile(3109, 3434);
            Tile hill2 = new Tile(3109, 3433);
            Tile hill3 = new Tile(3109, 3432);

            //Bridge tiles
            Tile bridge1 = new Tile(3103, 3425);
            Tile bridge2 = new Tile(3103, 3424);

            if (Players.getLocal().getLocation() == hill1 && NPCs.getNearest("Fishing spot") == null || Players.getLocal().getLocation() == hill2 && NPCs.getNearest("Fishing spot") == null || Players.getLocal().getLocation() == hill3 && NPCs.getNearest("Fishing spot") == null){
                System.out.println("No more fishing spots nearby (ON HILL)");
                Walking.walkTileMM(bridge2);

            }
            if (Players.getLocal().getLocation() == bridge1 && NPCs.getNearest("Fishing spot") == null || Players.getLocal().getLocation() == bridge2 && NPCs.getNearest("Fishing spot") == null) {
                System.out.println("No more fish near bridge");
                Walking.walkTileMM(hill1);

            }
            else {
                try {
                    TimeUnit.MILLISECONDS.sleep(5000);
                } catch(InterruptedException ex) {

                }
                NPCs.getNearest("Fishing spot").interact("Lure");
            }
        }
        run();
    }

    public int getFreeSpaceInventory () {
        int n = Inventory.getAllItems().length;
        int m = Inventory.getCount();
        System.out.println(n-m);
        return n-m;
    }

    public void dropFish()
    {
        if( Inventory.getCount() > 0 )
        {
            Inventory.dropAll(itemF);
        }
        run();
    }

    public Filter<Item> itemF = new Filter<Item>() {
        @Override
        public boolean accept(Item item) {
            if (item.getID() == 335 || item.getID() == 331) {
                item.interact("Drop");
            }
            return false;
        }
    };
}

