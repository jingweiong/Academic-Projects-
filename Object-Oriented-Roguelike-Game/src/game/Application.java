package game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.positions.FancyGroundFactory;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.World;
import game.actors.enemies.SkeletalEnemy.HeavySkeletalSwordsman;
import game.actors.players.Bandit;
import game.actors.players.Player;
import game.actors.players.Samurai;
import game.actors.players.Wretch;
import game.actors.traders.MerchantKale;
import game.environments.Dirt;
import game.environments.Floor;
import game.environments.Graveyard;
import game.environments.TheFirstStep;
import game.environments.Wall;
import game.environments.Wind;
import game.environments.Water;

/**
 * The main class to start the game.
 * Created by:
 * @author Adrian Kristanto
 * Modified by:
 * @author Jun Lim
 * @author jingweiong
 */
public class Application {

	public static void main(String[] args) {
		World world = new World(new Display());

		FancyGroundFactory groundFactory = new FancyGroundFactory(new Dirt(), new Wall(), new Floor(), new Graveyard(), new Wind(), new Water(), new TheFirstStep());

		List<String> mapA = Arrays.asList(
				"...........................................................................",
				"......................#####....######......................................",
				"......................#..___....____#......................................",
				"..................................__#......................................",
				"......................._____........#......................................",
				"......................#............_#......................................",
				"......................#...........###......................................",
				"...........................................................................",
				"...........................................................................",
				"..................................###___###................................",
				"..................................________#................................",
				"..................................#___U____................................",
				"..................................#_______#................................",
				"..................................###___###................................",
				"....................................#___#..................................",
				"...........................................................................",
				"...........................................................................",
				"...........................................................................",
				"..####__##....................................................######..##...",
				"..#.....__....................................................#....____....",
				"..#___..........................................................__.....#...",
				"..####__###..................................................._.....__.#...",
				"..............................................................###..__###...",
				"...........................................................................");
		List<String> mapB = Arrays.asList(
				"...........................................................................",
				"......................#####....######......................................",
				"......................#..___....____#......................................",
				".......~~~~~......................__#.....................~~~~~............",
				"......~~~~~............_____........#.....................~~~~~............",
				".....~~~~~............#............_#.....................~~~~~............",
				"......................#...........###......................................",
				"...........................................................................",
				"...........................................................................",
				"..................................###___###................................",
				"..................................________#...............&&&..............",
				"..................................#___U____................................",
				".....................nnn..........#_______#................................",
				"..................................###___###.........nnn....................",
				"....................................#___#..................................",
				"...........................................................................",
				"................&&&........................................................",
				"...........................................................................",
				"..####__##....................................................######..##...",
				"..#.....__....................................................#....____....",
				"..#___..........................................................__.....#...",
				"..####__###..................................................._.....__.#...",
				"..............................................................###..__###...",
				"...........................................................................");
		GameMap gameMap = new GameMap(groundFactory, mapA);
		world.addGameMap(gameMap);

		// (Jun Lim) removed for faster debugging
		// // BEHOLD, ELDEN RING
		// for (String line : FancyMessage.ELDEN_RING.split("\n")) {
		// 	new Display().println(line);
		// 	try {
		// 		Thread.sleep(200);
		// 	} catch (Exception exception) {
		// 		exception.printStackTrace();
		// 	}
		// }
		// Let player choose their class
	
		ArrayList<Player> classList = new ArrayList<>();
		classList.add(new Samurai());
		classList.add(new Bandit());
		classList.add(new Wretch());

		HashMap<Integer, Player> playerClassMap = new HashMap<Integer, Player>();

		System.out.println("Pick a class:");

		int counter = 1;
		for (Player playerClass : classList) {
			playerClassMap.put(counter, playerClass);
			System.out.println(counter + ": " + playerClass);
			counter ++;
		}

		Scanner keyboard = new Scanner(System.in);
		int i = 0;
		do {
			i = keyboard.nextInt();
		} while (!playerClassMap.containsKey(i));
		Player player = playerClassMap.get(i);

		classList.remove(player); // cleaning up list of resettables
		ResetManager resetManager = ResetManager.getInstance();
		for (Player playerClass : classList) {
			resetManager.removeResettable(playerClass);
		}

		world.addPlayer(player, gameMap.at(32, 11));

		// gameMap.at(32, 12).addActor(new GiantCrayfish());
		gameMap.at(31, 12).addActor(new HeavySkeletalSwordsman());
		// gameMap.at(32, 12).addActor(new HeavySkeletalSwordsman());
		// gameMap.at(33, 12).addActor(new HeavySkeletalSwordsman());
		// gameMap.at(31, 11).addActor(new HeavySkeletalSwordsman());
		// gameMap.at(31, 10).addActor(new HeavySkeletalSwordsman());
		// gameMap.at(32, 10).addActor(new HeavySkeletalSwordsman());
		// gameMap.at(33, 10).addActor(new HeavySkeletalSwordsman());
		// gameMap.at(33, 11).addActor(new HeavySkeletalSwordsman());






		// gameMap.at(31, 11).addActor(new GiantCrab());
		// gameMap.at(31, 11).addActor(new GiantCrayfish());
		// gameMap.at(31, 10).addActor(new GiantCrayfish());



		// RuneManager runeManager = new RuneManager(1000);

		// HINT: what does it mean to prefer composition to inheritance?

		gameMap.at(33,11).addActor(new MerchantKale());
		

		world.run();
	}
}