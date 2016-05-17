import com.epicbot.api.ActiveScript;
import com.epicbot.api.GameType;
import com.epicbot.api.Manifest;
import com.epicbot.api.os.methods.Walking;
import com.epicbot.api.os.methods.interactive.Players;
import com.epicbot.api.os.methods.node.SceneEntities;
import com.epicbot.api.os.methods.tab.inventory.Inventory;
import com.epicbot.api.os.methods.widget.Bank;
import com.epicbot.api.os.wrappers.Locatable;
import com.epicbot.api.os.wrappers.RegionOffset;
import com.epicbot.api.os.wrappers.Tile;
import com.epicbot.api.os.wrappers.interactive.Character;
import com.epicbot.api.os.wrappers.interactive.Player;
import com.epicbot.event.listeners.PaintListener;
import com.epicbot.api.concurrent.Task;
import com.epicbot.api.concurrent.node.Node;

import java.awt.*;

@Manifest(
        author = "rdurant",
        game = GameType.OldSchool,
        name = "Bank Opener"
)
public class DraynorWillows extends ActiveScript implements PaintListener {

    public boolean onStart() {
        if (Inventory.getCount() == 28){
            System.out.println("Full inv...");
            doBanking();
            return true;
        }
        else {
            Tile bankTile = new Tile(3093,3244);
            if (Players.getLocal().getLocation() == bankTile.getLocation()) {
                walkToTree();
            }
            else {
                woodchop();
            }
            return true;
        }
    }

    public boolean shouldExecute() {
        if (Players.getLocal() != null) {
            return true;
        }
        return false;
    }


    public void onStop(){
        System.out.println("Stopping...");
    }


    public void onRepaint(Graphics2D arg0){

    }


    public void run() {
        System.out.println("Running!");
        onStart();
    }

    public int getFreeSpaceInventory () {
        int n = Inventory.getAllItems().length;
        int m = Inventory.getCount();
        System.out.println(n-m);
        return n-m;
    }

    public void woodchop () {
        if (SceneEntities.getNearest("Willow") != null) {
            if (Inventory.getCount() == 28) {
                doBanking();
            }
            else {
                System.out.println("Attempting to interact");
                SceneEntities.getNearest("Willow").interact("Chop down");
                try {
                    Thread.sleep(10000);
                    run();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                walkToTree();

            }
        }
    }

    public void doBanking () {
        System.out.println("goBank()");
        if (Inventory.getCount() == 28){
            System.out.println("Walking to bank!");
            Tile bankTile = new Tile(3093,3244);
            Walking.walkTileMM(bankTile);
            try {
                Thread.sleep(12000);
                depositLogs();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Attempting to deposit...");
            Bank.open();
            if (Bank.isOpen()) {
                System.out.println("Depositing logs!");
                Bank.deposit(1519, 28);
                try {
                    Thread.sleep(4000);
                    depositLogs();
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Bank.close();
                run();
            }
        }
        else {
            run();
        }
    }

    public void depositLogs() {
        System.out.println("Attempting to deposit...");
        Bank.open();
        if (Bank.isOpen()) {
            System.out.println("Depositing!");
            Bank.deposit(1519, 28);
            Bank.close();
            run();
        }
        else {
            run();
        }
    }

    public void walkToTree () {
        System.out.println("WalkingtoTrees()");
        Tile treeTile = new Tile(3088,3233);
        Walking.walkTileMM(treeTile);
        try {
            Thread.sleep(12000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        woodchop();
    }

}


